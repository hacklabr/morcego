/*
 * Morcego - 3D network browser Copyright (C) 2004 Luis Fagundes - Arca
 * <lfagundes@arca.ime.usp.br>
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package br.arca.morcego.transport;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcAppletClient;
import org.apache.xmlrpc.XmlRpcException;

import br.arca.morcego.Config;
import br.arca.morcego.structure.Graph;
import br.arca.morcego.structure.GraphElementFactory;
import br.arca.morcego.structure.Link;
import br.arca.morcego.structure.Node;

/**
 * @author lfagundes
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class XmlrpcTransport implements Transport {

	private XmlRpcAppletClient client;
	private String url;
	private int protocolVersion = 1;
	
	public XmlrpcTransport() {
	}
	
	public void setup() {
		this.setServerUrl(Config.getString(Config.serverUrl));
		protocolVersion = fetchVersion();
	}
	
	public void setServerUrl(String server_url) {
		url = server_url;
		try {
			client = new XmlRpcAppletClient(server_url);
		} catch (MalformedURLException e) {
			System.out.println("Bad URL " + url);
			e.printStackTrace();
		}
	}
	
	/**
	 * @param params
	 * @return
	 */
	private Hashtable fetch(String method, Vector params) {
		Hashtable result = new Hashtable();
		try {
			result = (Hashtable) client.execute(method, params);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public Hashtable retrieveData(String centerId, Integer depth) {
		float protocolVersion = fetchVersion();
		if (protocolVersion >= 2) {
			return fetchGraph(centerId, depth);
		} else {
			return oldFetchGraph(centerId, depth);
		}
	}
	
	private int fetchVersion() {
		int version;
		try {
			version = ((Integer) client.execute("getVersion", new Vector())).intValue();
		} catch (XmlRpcException e) {
			version = 1;
		} catch (IOException e) {
			e.printStackTrace();
			version = 1;
		}
		return version;
	}

	public Hashtable fetchGraph(String centerId, Integer depth) {
		Vector params = new Vector();
		params.add(centerId);
		params.add(depth);

		Hashtable result = fetch("getSubGraph",params);
		return result;
	}

	
	/*
	 * v0.4 backward compatibility
	 */
	private Hashtable oldFetchGraph(String centerId, Integer depth) {
		Hashtable graph = fetchGraph(centerId, depth);
		Hashtable nodes = (Hashtable) graph.get("graph");
		Vector links = new Vector();
		
		for (Enumeration e = nodes.keys(); e.hasMoreElements();) {
			String nodeId = (String) e.nextElement();
			Hashtable node = (Hashtable) nodes.get(nodeId);
			
			Vector neighbours = (Vector) node.get("neighbours");
			for (Enumeration eN = neighbours.elements(); eN.hasMoreElements();) {
				Hashtable link = new Hashtable();
				link.put("to", nodeId);
				link.put("from", eN.nextElement());
				links.add(link);
			}
		}
		graph.put("links", links);
		graph.put("nodes", nodes);
		return graph;
	}
}

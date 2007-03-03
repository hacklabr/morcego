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
	private static Hashtable availableProperties = Node.availableProperties();
	
	public XmlrpcTransport() {
	}
	
	public void setup() {
		this.setServerUrl(Config.getString(Config.serverUrl));
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
	 * 
	 */

	public Graph retrieveData(Node centerNode, Integer depth) {

		Graph graph = new Graph();

		Hashtable result = fetchData(centerNode, depth);

		fillGraph(graph, result);
		
		return graph;
	}

	private void fillGraph(Graph graph, Hashtable result) {
		Hashtable nodes = (Hashtable) result.get("nodes");

		for (Enumeration eN = nodes.keys(); eN.hasMoreElements();) {
			String nodeId = (String) eN.nextElement();
			Hashtable<String, Object> nodeData = (Hashtable<String, Object>) nodes.get(nodeId);

			if (nodeData.get("title") == null) {
				nodeData.put("title", nodeId);
			}
			if (nodeData.get("type") == null) {
				nodeData.put("type", Config.getString(Config.nodeDefaultType));
			}
			
			Node node = GraphElementFactory.createNode(nodeId, graph, (String)nodeData.get("type"));

			for (Enumeration<String> eP = nodeData.keys(); eP.hasMoreElements(); ) {
				String key = eP.nextElement();
				Class type = (Class) availableProperties.get(key);
				if (type != null) {
					node.setProperty(key, Config.decode((String) nodeData.get(key), type));
				}
			}
			
			node.init();
		}
		
		Vector links = (Vector) result.get("links");

		for (Enumeration eL = links.elements();
			eL.hasMoreElements();
			) {
			
			Hashtable<String, String> linkData = (Hashtable<String, String>) eL.nextElement();
			
			String type = linkData.get("type");
			if (type == null) {
				type = Config.getString(Config.linkDefaultType);
			}				
			
			Node node1 = graph.getNodeById(linkData.get("from"));
			Node node2 = graph.getNodeById(linkData.get("to"));
			
			if (node1 != null && node2 != null) {
				Link link = GraphElementFactory.createLink(node1, node2, type);
				
				for (Enumeration<String> eP = linkData.keys(); eP.hasMoreElements(); ) {
					String key = eP.nextElement();
					Class propertyType = (Class) availableProperties.get(key);
					if (propertyType != null) {
						link.setProperty(key, Config.decode((String) linkData.get(key), propertyType));
					}
				}
				
				node1.addLink(node2.getId(), link);
				node2.addLink(node1.getId(), link);
			}
			
			

		}

	}
	
	private Hashtable fetchData(Node centerNode, Integer depth) {
		Vector<Comparable> params = new Vector<Comparable>();
		params.add(centerNode.getId());
		params.add(depth);

		Hashtable result = new Hashtable();
		try {
			result = (Hashtable) client.execute("getSubGraph", params);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}

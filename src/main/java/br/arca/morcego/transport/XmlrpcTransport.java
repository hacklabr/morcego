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
	
	public Hashtable retrieveData(String centerId, Integer depth) {
		Vector<Comparable> params = new Vector<Comparable>();
		params.add(centerId);
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

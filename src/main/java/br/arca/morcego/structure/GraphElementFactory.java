/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package br.arca.morcego.structure;

import java.util.Hashtable;

import br.arca.morcego.Config;
import br.arca.morcego.structure.link.SolidLink;
import br.arca.morcego.structure.node.RoundNode;

/**
 * @author lfagundes
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GraphElementFactory {
	
	// Holds type names with hashtable of properties
	private static Hashtable types = new Hashtable();
	
	public static void defineType(String type, Hashtable properties) {
		types.put(type,properties);
	}

	/**
	 * @param node
	 * @param neighbour
	 * @return
	 * @throws LinkingDifferentGraphs 
	 */
	public static Link createLink(Node node, Node neighbour, String type) {
		if (node.isLinkedTo(neighbour)) {
			return node.getLinkTo(neighbour);
		}
		if (type == null) {
			type = Config.getString(Config.linkDefaultType);
		}
		Class linkClass = null;
		Link link = null;
		try {
			linkClass = Config.getLinkClass(type);
			link = (Link) linkClass.newInstance();
			link.setup(node, neighbour);
		} catch (Exception e) {
			e.printStackTrace();
			link = new SolidLink(node, neighbour);
		}
		
		return link;
	}

	/**
	 * @param nodeId
	 * @param graph
	 * @return
	 */
	public static Node createNode(String nodeId, Graph graph, String type) {
		Class nodeClass = null;
		Node node;
		
		if (type == null) {
			type = Config.getString(Config.nodeDefaultType);
		}
		try {
			nodeClass = Config.getNodeClass(type);
			Node existingNode = graph.getNodeById(nodeId);
			if (existingNode != null) {
				if (existingNode.getClass() == nodeClass) {
					return existingNode;
				} else {
					//existingNode.detach();
				}
			}
			node = (Node) nodeClass.newInstance();
			node.setup(nodeId, graph);
		} catch (Exception e) {
			e.printStackTrace();
			if (!type.equals("Round")) {
				node = createNode(nodeId, graph, "Round");
			} else {
				node = new Node(nodeId, graph);
			}
		}
		
		return node;
	}

}

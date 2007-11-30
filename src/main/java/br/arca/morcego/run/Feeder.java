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
package br.arca.morcego.run;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import br.arca.morcego.Config;
import br.arca.morcego.exception.WrongDataFormat;
import br.arca.morcego.structure.Graph;
import br.arca.morcego.structure.GraphElementFactory;
import br.arca.morcego.structure.Link;
import br.arca.morcego.structure.Node;
import br.arca.morcego.transport.Transport;

/**
 * @author lfagundes
 * 
 * Feeder runs in a thread and will retrieve graph data whenever needed, then
 * pass new and old nodes to animator to put them in graph in a fancy way.
 */
public class Feeder extends ControlledRunnable {

	private Graph graph;

	private Transport transport;

	private Animator animator;

	private boolean shouldWait = false;

	public Feeder(Graph g, Transport t) {
		graph = g;
		transport = t;

		animator = new Animator(graph);
	}


	/*
	 * Gets the new graph and feeds it's node to current graph
	 */
	public void feed(Hashtable graphData) throws WrongDataFormat {
		
		synchronized (graph) {
			Vector nodes = extractNodes(graphData);
			extractLinks(graphData);
			animator.animate(nodes);
		}
	}
	
	public Vector extractNodes(Hashtable graphData) throws WrongDataFormat {
		Vector nodes = new Vector();
		Hashtable nodesData;
		try {
			nodesData = (Hashtable) graphData.get("nodes");
		} catch (ClassCastException e) {
			throw new WrongDataFormat();
		}
		for (Enumeration eN = nodesData.keys(); eN.hasMoreElements();) {
			String nodeId = (String) eN.nextElement();
			Hashtable nodeData = (Hashtable) nodesData.get(nodeId);		
			Node node = GraphElementFactory.createNode(nodeId, graph, (String) nodeData.get("type"));
			node.setProperties(nodeData);
			node.init();
			
			nodes.add(node);
		}
		
		return nodes;
	}

	public void extractLinks(Hashtable graphData) throws WrongDataFormat {
		
		Vector linksData;
		try {
			linksData = (Vector) graphData.get("links");
		} catch (ClassCastException e) {
			throw new WrongDataFormat();
		}
		for (Enumeration eN = linksData.elements(); eN.hasMoreElements();) {
			
			Hashtable linkData = (Hashtable) eN.nextElement();
			
			Node node1 = graph.getNodeById((String) linkData.get("from"));
			Node node2 = graph.getNodeById((String) linkData.get("to"));
			
			if (node1 != null && node2 != null) {
				Link link;
				link = GraphElementFactory.createLink(node1, node2, (String) linkData.get("type"));				
				link.setProperties(linkData);
				link.init();				
			}
		}
	}

	public void notifyFeeder() {
		shouldWait = false;
		this.notify();
	}
	
	public void finish() {
		super.finish();
		animator.finish();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		Thread animationThread = new Thread(animator);
		animationThread.start();
		
		running = true;

		while (running) {
			synchronized (this) {
				try {
					if (shouldWait) {
						this.wait();
					}
					shouldWait = true;
				} catch (InterruptedException e1) {
					break;
				}
			}

			Hashtable graphData;
			try {
				graphData = transport.retrieveData(graph.getCenterId(),
						((Integer) Config.getValue(Config.navigationDepth)));
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
			try {
				feed(graphData);
			} catch (WrongDataFormat e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
/*
 * Morcego - 3D network browser Copyright (C) 2004 Luis Fagundes - Arca
 * <lfagundes@arca.ime.usp.br>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package br.arca.morcego.structure;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.event.MouseInputListener;

import br.arca.morcego.Config;
import br.arca.morcego.Morcego;
import br.arca.morcego.physics.PositionedObject;
import br.arca.morcego.physics.Vector3D;
import br.arca.morcego.run.Balancer;
import br.arca.morcego.run.Feeder;
import br.arca.morcego.run.Rotator;
import br.arca.morcego.transport.Transport;

public class Graph extends Component implements MouseInputListener,
		PositionedObject, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4592004647118214474L;

	
	private Vector<Node> visibleNodes;
	private Vector<Link> visibleLinks;
	private Vector<GraphElement> visibleElements;
	private Vector<GraphElement> elements;
	private Vector<Node> nodes;

	private Hashtable<String,Node> nodesFromId = new Hashtable<String,Node>();

	private Node centerNode;
	
	private Rotator rotator;

	private Vector3D orientation;

	private Balancer balancer;

	private Feeder feeder;

	private int dragSpeedX;

	private int dragSpeedY;

	private boolean focusFixed = false;


	private GraphElement oldFocus;
	private GraphElement focus;

	private int previousX, previousY;

	private final class RenderingStrategy implements Comparator {
		public int compare(Object o1, Object o2) {
			if (((GraphElement) o1).getDepth() > ((GraphElement) o2).getDepth()) {
				return -1;
			} else if (((GraphElement) o1).getDepth() == ((GraphElement) o2)
					.getDepth()) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public Graph() {
		elements = new Vector<GraphElement>();
		nodes = new Vector<Node>();
		
		visibleElements = new Vector<GraphElement>();
		visibleNodes = new Vector<Node>();
		visibleLinks = new Vector<Link>();

		//used for hierarchy
		orientation = new Vector3D(1, 0, 0);

	}

	public void addElement(GraphElement e) {
		e.setGraph(this);
		elements.add(e);
	}

	public void addNode(Node node) {

		if (getNodeById(node.getId()) == null) {
			addElement(node);
			nodes.add(node);
			nodesFromId.put(node.getId(), node);
			
			for (Enumeration<Link> e = node.getLinks(); e.hasMoreElements();) {
				Link link = e.nextElement();
				addLink(link);
			}
		}
		
		if (centerNode == null && node.getId().equals(getCenterId())) {
			center(node);
		}
		
	}
	
	public void addLink(Link link) {
		if (!elements.contains(link)) {
			addElement(link);
			if (link.getNode1().isVisible() && link.getNode2().isVisible()) {
				visibleLinks.add(link);
				visibleElements.add(link);
			}
		}
	}
	
	public boolean isVisible(GraphElement element) {
		return visibleElements.contains(element);
	}
	
	public void showNode(Node node) {
		if (!elements.contains(node)) {
			addNode(node);
		}
		if (!isVisible(node)) {
			visibleElements.add(node);
			visibleNodes.add(node);
			for (Enumeration<Link> e = node.getLinks(); e.hasMoreElements();) {
				Link link = e.nextElement();
				if (link.getOther(node).isVisible() &&  !isVisible(link)) {
					visibleElements.add(link);
					visibleLinks.add(link);
				}
			}
		}
		notifyBalancer();
	}
	
	public void hideNode(Node node) {
		visibleElements.remove(node);
		visibleNodes.remove(node);
		for (Enumeration<Link> e = node.getLinks(); e.hasMoreElements();) {
			Link link = e.nextElement();
			visibleElements.remove(link);
			visibleLinks.remove(link);
		}
	}

	public void removeNode(Node node) {
		elements.remove(node);
		nodes.remove(node);
		
		visibleElements.remove(node);
		visibleNodes.remove(node);

		nodesFromId.remove(node.getId());
		
		for (Enumeration<Link> e = node.getLinks(); e.hasMoreElements();) {
			Link link = e.nextElement();
			elements.remove(link);
			visibleElements.remove(link);
			visibleLinks.remove(link);
		}
		
		if (focus == node) {
			focus = null;
		}
	}

	/**
	 *  
	 */
	synchronized private void order() {
		synchronized(visibleElements) {
			Collections.sort(visibleElements, new RenderingStrategy());
		}
	}

	public void navigateTo(Node node) {
		center(node);

		notifyFeeder();
		notifyBalancer();		
	}

	/**
	 * @param newNode
	 */
	public void center(Node newCenter) {
		if (centerNode != null) {
			centerNode.unCenter();
		}

		centerNode = newCenter;
		newCenter.center();
	}
	
	public void calculateDistances() {
		for (Enumeration<Node> e = nodes.elements(); e.hasMoreElements();) {
			e.nextElement().setCenterDistance(null);
		}
		
		//fifo queue of nodes
		Vector<Node> nodeQueue = new Vector<Node>();
		nodeQueue.add(centerNode);
		int distance = 0;
		
		while (!nodeQueue.isEmpty()) {
			int size = nodeQueue.size();
			for (int i=0; i<size; i++) {
				Node node = nodeQueue.remove(0);
				node.setCenterDistance(new Integer(distance));
				for (Enumeration<Link> e = node.getLinks(); e.hasMoreElements();) {
					Node neighbour = e.nextElement().getOther(node);
					if (!nodeQueue.contains(neighbour) && neighbour.getCenterDistance() == null) {
						nodeQueue.add(neighbour);
					}
				}
			}
			distance++;
		}
	}


	public Node getNodeById(String nodeId) {
		Node node = nodesFromId.get(nodeId);
		return node;
	}

	/**
	 * @return Returns the centerNode.
	 */
	public Node getCenterNode() {
		return centerNode;
	}

	/**
	 * @param e
	 */
	private void savePosition(MouseEvent e) {
		previousX = e.getX();
		previousY = e.getY();
	}

	public boolean contains(MouseEvent e) {
		if (focusFixed) {
			return true;
		}
		// get focus
		order();
		boolean contains = false;
		for (int i = visibleElements.size() - 1; i >= 0 && !contains; i--) {
			GraphElement component = visibleElements.elementAt(i);
			if (component.visible() && component.contains(e)) {
				oldFocus = focus;
				focus = component;
				contains = true;
			}
		}

		if (!contains) {
			oldFocus = focus;
			focus = null;
		}

		if (focus != oldFocus) {
			if (focus != null) {
				focus.mouseEntered(e);
			}
			if (oldFocus != null) {
				oldFocus.mouseExited(e);
			}
		}

		// graph claims to contain anything by now
		// TODO strange...
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.arca.morcego.ScreenComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		synchronized (visibleElements) //Sync on elements so that they don't change
		// after order and during painting.
		{
			order();
			for (Enumeration<GraphElement> e = visibleElements.elements(); e.hasMoreElements();) {
				GraphElement element = e.nextElement();
				if (element.visible()) {
					element.paint(g);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		rotator.stop();
		if (contains(e)) {
			if (focus != null) {
				focus.mouseClicked(e);
			}
		}

		savePosition(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {

		rotator.stop();

		if (focus != null) {
			focus.mousePressed(e);
		}

		savePosition(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {

		if (focus != null) {
			focus.mouseReleased(e);
		} else {
			if (Math.abs(dragSpeedX) + Math.abs(dragSpeedY) > 4) {
				// TODO putaquepariuquegambiarradaporra
				rotator.spin(dragSpeedY / 5, -dragSpeedX / 5);
			}
		}
		savePosition(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {

		if (focus != null) {
			focus.mouseDragged(e);
		} else {
			dragSpeedX = e.getX() - previousX;
			dragSpeedY = e.getY() - previousY;
			rotate(dragSpeedY, -dragSpeedX);
		}

		Morcego.notifyRenderer();

		savePosition(e);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		if (contains(e)) {
			if (focus != null) {
				focus.mouseMoved(e);
			}
		}

		savePosition(e);
	}

	/**
	 * @return
	 */
	public Vector<Node> getVisibleNodes() {
		return visibleNodes;
	}

	public Vector<Link> getVisibleLinks() {
		return visibleLinks;
	}

	public void notifyFeeder() {
		synchronized (feeder) {
			feeder.notifyFeeder();
		}
	}

	public void notifyBalancer() {
		if (balancer != null) {
			synchronized (balancer) {
				balancer.awake();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
	
		Class transportClass = null;
		Transport transport = null;
		try {
			transportClass = Config.getTransportClass(Config.transportClass);
			transport = (Transport) transportClass.newInstance();
			transport.setup();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		feeder = new Feeder(this, transport);
		balancer = new Balancer(this);
		rotator = new Rotator(this);

		Thread feedingThread = new Thread(feeder);
		Thread balanceThread = new Thread(balancer);
		Thread spinningThread = new Thread(rotator);

		feedingThread.start();
		spinningThread.start();
		balanceThread.start();

	}

	/**
	 * @param focusFixed
	 *            The focusFixed to set.
	 */
	public void fixFocus() {
		focusFixed = true;
	}

	public void releaseFocus() {
		focusFixed = false;
		focus = null;
	}

	/**
	 * @return Returns the orientation.
	 */
	public Vector3D getOrientation() {
		return orientation;
	}

	synchronized public void rotate(float xTheta, float yTheta) {
		Enumeration<Node> e = getVisibleNodes().elements();
		while (e.hasMoreElements()) {
			Node node = e.nextElement();
			node.rotate(xTheta, yTheta);
		}

		getOrientation().rotate(xTheta, yTheta);
		Morcego.getCamera().adjustPosition(this);
	}

	/**
	 * @return
	 */
	public Node getFocus() {
		// TODO should return GraphElement (link should be focusable)
		return (Node) focus;
	}

	public Vector<GraphElement> getVisibleElements() {
		return visibleElements;
	}

	public String getCenterId() {
		if (centerNode != null) {
			return centerNode.getId();
		} else {
			return Config.getString(Config.startNode);
		}
	}

	public void hideFarNodes() {
		// TODO we should use getVisibleNodes(), but this is causing detached nodes sometime
		for (Enumeration<Node> e = getNodes().elements(); e.hasMoreElements();) {
			Node node = e.nextElement();
			//TODO bug: nodes are being hidden and then shown again in same request
			if (node.getCenterDistance().intValue() > Config.getInteger(Config.navigationDepth)) {
				hideNode((Node) node);
			}
		}
	}

	private Vector<Node> getNodes() {
		return nodes;
	}

}
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import br.arca.morcego.Config;
import br.arca.morcego.Morcego;
import br.arca.morcego.component.DescriptionBox;
import br.arca.morcego.physics.PositionedObject;
import br.arca.morcego.physics.PunctualBody;
import br.arca.morcego.physics.Vector3D;

public class Node extends GraphElement implements PositionedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4471920873578042527L;

	protected String id;

	protected PunctualBody body;

	// Used to check if mouse is over here
	protected Rectangle boundRectangle = new Rectangle();

	// For fixing node position
	//private boolean positionFixed;

	// Linked nodes' names as keys in hash for fast searching,
	// value is not used
	private Hashtable links;

	private boolean isCentered;

	// Maximum module node's position can have when being dragged
	// by user
	private float maxModule;
	
	private Integer centerDistance = null;

	public Node() {
		super();
	}

	public Node(String name, Graph graph) {
		super();
		setup(name, graph);
	}
	
	public void setup(String name, Graph graph) {
		links = new Hashtable();

		this.graph = graph;

		this.id = name;


		int x = randomLength();
		int y = randomLength();
		int z = randomLength();

		// don't let nodes start too near from camera
		// also put center node in evidence during start
		z = -1 * Math.abs(z);
		
		body = new PunctualBody(x, y, z);

		this.graph.addNode(this);
	}
	
	public Hashtable availableProperties() {
		Hashtable prop = super.availableProperties();
		prop.put("color", Color.class);
		prop.put("actionurl", URL.class);
		prop.put("description", String.class);
		prop.put("title", String.class);
		prop.put("mass", Float.class);
		prop.put("charge", Float.class);
		prop.put("level", Integer.class);
		return prop;
	}

	public PunctualBody getBody() {
		return body;
	}
	
	public int getLevel() {
		Integer level = (Integer) getProperty("level");
		if (level == null) {
			return 0;
		} else {
			return level.intValue();
		}
	}

	public void setBody(PunctualBody body) {
		this.body = body;
	}
	/**
	 * @param node
	 * @return
	 */
	public boolean isLinkedTo(Node node) {
		return links.containsKey(node.id);
	}

	/**
	 * @return
	 */
	public boolean centered() {
		return isCentered;
	}

	public void center() {
		isCentered = true;
		body.fixPosition(0,0,0);
	}

	public void unCenter() {
		isCentered = false;
		releasePosition();
	}

	public int linkCount() {
		int i = 0;
		for (Enumeration e = links.keys(); e.hasMoreElements(); i++) {
			e.nextElement();
		}
		return i;
	}

	public void move() {
		body.move();
		proj();
	}

	public void releasePosition() {
		body.releasePosition();
	}

	public void fixPosition() {
		body.fixPosition();
	}

	synchronized public void proj() {
		//TODO refactor everything
		// this is here because move() call proj(), but it's implemented in child class
	}

	public boolean contains(MouseEvent e) {
		return boundRectangle.contains(e.getX(), e.getY());
	}

	public int randomLength() {
		return (int) ((Math.random() * 2 - 1) * 500);
	}

	public void addLink(Node node, Link link) {
		links.put(node.getId(), link);
	}
	
	public void paint(Graphics g) {
		//TODO node shouldn't have to be paintable
	}

	/**
	 * @return Returns the name.
	 */
	public String getId() {
		return id;
	}



	public Enumeration getLinks() {
		return links.elements();
	}

	public Integer getCenterDistance() {
		return centerDistance;
	}

	public void setCenterDistance(Integer centerDistance) {
		this.centerDistance = centerDistance;
		/* debug stuff
		if (centerDistance != null) {
			setDescription(centerDistance.toString());
		}
		*/
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.arca.morcego.ScreenComponent#getDepth()
	 */
	public float getDepth() {
		return Morcego.getCamera().getDistanceTo(body);
	}


	/**
	 *  
	 */
	public void init() {
		super.init();
		
		if (getProperty("title") == null) {
			setProperty("title", id);
		}
		
		if (getProperty("color") == null) {
			setProperty("color", Config.getColor(Config.nodeDefaultColor));
		}
	}
	
	public boolean isVisible() {
		return graph.isVisible(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		boolean navigate = centered() || Config.getBoolean(Config.loadPageOnCenter);
		
		if (!centered()) {
			graph.navigateTo(this);
			if (description != null) {
				Morcego.getApplication().remove(description);
			}
		}
		if (navigate) {
			URL url = (URL) ((Node) graph.getFocus()).getProperty("actionUrl");
			if (url != null) {
				Morcego.showDocument(url, 
						Config.getString(Config.controlWindowName));
			}
		}
		this.callJsEvent("onclick");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		Morcego.setHandCursor();
		showDescription();
		e.consume();
		this.callJsEvent("onmouseover");
	}

	
	/**
	 * 
	 */
	private void showDescription() {
		if (description != null) {
			description.setPosition(body.projection.x, body.projection.y);
			Morcego application = Morcego.getApplication();
			application.add(description);
			description.setVisible(true);
			Morcego.notifyRenderer();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		Morcego.setDefaultCursor();
		hideDescription();
		this.callJsEvent("onmouseout");
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		// Get sphere of reachable universe, 5x the size of
		// engulphing sphere
		// maybe a better camera will avoid this limit
		Enumeration el = graph.getVisibleNodes().elements();
		float reachableRadius = 0;
		while (el.hasMoreElements()) {
			Node node = (Node) el.nextElement();
			float radius = node.getBody().module();
			if (radius > reachableRadius) {
				reachableRadius = radius;
			}
		}
		
		maxModule = 5 * reachableRadius;
		
		fixPosition();
		graph.fixFocus();
		if (description != null) {
			Morcego.getApplication().remove(description);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (!centered()) {
			releasePosition();
		}
		mouseExited(e);
		graph.releaseFocus();	
		graph.notifyBalancer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (!centered()) {
			Vector3D position = body.unproj(e.getX(), e.getY());
			// check for a maximum size of reachable universe, to
			// avoid weird behaviour for user
			if (position.module() > maxModule) {
				position.resize(maxModule/position.module());
			}
			body.moveTo(position);
			graph.notifyBalancer();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
	}

	public void rotate(float xTheta, float yTheta) {
		body.rotate(xTheta, yTheta);
	}
	

	public void setProperty(String name, Object value) {
		if (name.equals("bodyCharge")) {
			body.setCharge(((Float)value).floatValue());
		}
		if (name.equals("bodyMass")) {
			body.setMass(((Float)value).floatValue());
		}
		super.setProperty(name, value);
	}

	/**
	 * @param node2
	 * @return
	 */
	public Link getLinkTo(Node node) {
		if (this.isLinkedTo(node)) {
			return (Link) links.get(node.id);
		}
		return null;
	}

	public boolean visible() {
		return true;
	}
	
	public float getScale() {
		float scale = body.getScale();
		if (centered()) {
			scale *= Config.getFloat(Config.centerNodeScale);
		}
		return scale;
	}
}
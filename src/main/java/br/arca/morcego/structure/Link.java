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
	package br.arca.morcego.structure;
	
	import java.awt.Color;
	import java.awt.Graphics;
import java.awt.Polygon;
	import java.awt.Rectangle;
	import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
	import java.net.URL;
	import java.util.Hashtable;
	
	import br.arca.morcego.Morcego;
import br.arca.morcego.component.DescriptionBox;
	import br.arca.morcego.physics.Spring;
import br.arca.morcego.physics.Vector3D;
	
	/**
	 * @author lfagundes
	 * 
	 * To change the template for this generated type comment go to Window -
	 * Preferences - Java - Code Generation - Code and Comments
	 */
	public abstract class Link extends GraphElement {
	
		//private Graph graph;
		
		private Spring spring;
		
		protected Node node1, node2;


		public Link() {
			super();
		}
		/**
		 * @throws LinkingDifferentGraphs 
		 *  
		 */
		public Link(Node n1, Node n2) {
			super();
			
			setup(n1, n2);
		}
		
		public void setup(Node n1, Node n2) {		
			spring = new Spring(n1.getBody(), n2.getBody());
			
			node1 = n1;
			node2 = n2;
			
			node1.addLink(node2, this);
			node2.addLink(node1, this);
			
			n1.getGraph().addLink(this);	
			
		}
		
		public Hashtable<String, Class> availableProperties() {
			Hashtable<String, Class> prop = new Hashtable<String,Class>();
			prop.put("type", String.class);
			prop.put("color", Color.class);
			prop.put("description", String.class);
			prop.put("size", Float.class);
			prop.put("elasticity", Float.class);
			return prop;
		}
	
		public boolean hasNode(Node node) {
			return node1 == node || node2 == node;
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see br.arca.morcego.ScreenComponent#getDepth()
		 */
		public float getDepth() {		
			return node1.getDepth() + node2.getDepth() / 2;
		}
	
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see br.arca.morcego.ScreenComponent#contains(int, int)
		 */
		public boolean contains(MouseEvent e) {
			
			Vector3D start = new Vector3D(node1.body.projection.x, node1.body.projection.y, 0);
			Vector3D axis = new Vector3D(node2.body.projection.x, node2.body.projection.y, 0).getVectorFrom(start);
			Vector3D ortog = new Vector3D(-1 * axis.y, axis.x, 0);
			ortog.resize(2/ortog.module());
			
			Polygon p = new Polygon();
			
			p.addPoint((int)(start.x+ortog.x), (int)(start.y+ortog.y));
			p.addPoint((int)(start.x-ortog.x), (int)(start.y-ortog.y));
			p.addPoint((int)(start.x-ortog.x+axis.x), (int)(start.y-ortog.y+axis.y));
			p.addPoint((int)(start.x+ortog.x+axis.x), (int)(start.y+ortog.y+axis.y));
			
			return p.contains(e.getX(), e.getY());
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see br.arca.morcego.ScreenComponent#draw(java.awt.Graphics)
		 */
		public void paint(Graphics g) {
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
			Morcego.setHandCursor();
			if (description != null) {
				int x = (node1.getBody().projection.x + node2.getBody().projection.x)/2;
				int y = (node1.getBody().projection.y + node2.getBody().projection.y)/2;
				description.setPosition(x, y);
				Morcego application = Morcego.getApplication();
				application.add(description);
				description.setVisible(true);
				e.consume();
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
			if (description != null) {
				Morcego.getApplication().remove(description);
				Morcego.notifyRenderer();
			}
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
		 */
		public void mouseDragged(MouseEvent e) {
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
		 */
		public void mouseMoved(MouseEvent e) {
		}
	
	
		public Spring getSpring() {
			return spring;
		}
		
		public void setProperty(String name, Object value) {
			if (name.equals("springSize")) {
				spring.setSize(((Float)value).floatValue());
			}
			if (name.equals("springElasticConstant")) {
				spring.setElasticConstant(((Float)value).floatValue());
			}
			super.setProperty(name, value);
		}
		public Node getNode1() {
			return node1;
		}
		public Node getNode2() {
			return node2;
		}
		
		public Node getOther(Node node) {
			if (node1 == node) {
				return node2;
			}
			if (node2 == node) {
				return node1;
			}
			return null;
		}
		
		public boolean visible() {
			return getNode1().visible() && getNode2().visible();
		}
		
	}

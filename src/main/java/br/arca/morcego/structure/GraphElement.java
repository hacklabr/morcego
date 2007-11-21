/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package br.arca.morcego.structure;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.event.MouseInputListener;

import br.arca.morcego.Config;
import br.arca.morcego.Morcego;
import br.arca.morcego.component.DescriptionBox;
import br.arca.morcego.physics.VisibleObject;



/**
 * @author lfagundes
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class GraphElement extends Component implements VisibleObject, MouseInputListener{
	
	private Hashtable properties;
	protected Graph graph;
	
	protected DescriptionBox description;
	
	public GraphElement() {
		properties = new Hashtable();
	}
	
	public Hashtable availableProperties() {
		Hashtable prop = new Hashtable();
		prop.put("type", String.class);
		prop.put("onmouseover", String.class);
		prop.put("onmouseout", String.class);
		prop.put("onclick", String.class);
		
		return prop;
	}
	
	public Object getProperty(String name) {
		return properties.get(name.toLowerCase());
	}
	
	public void setProperty(String name, Object value) {
		properties.put(name.toLowerCase(), value);
	}
	
	public Hashtable getProperties() {
		return properties;
	}

	public void setProperties(Hashtable properties) {
		
		Hashtable available = availableProperties();
		
		for (Enumeration eP = properties.keys(); eP.hasMoreElements(); ) {
			String key = (String) eP.nextElement();
			Class type = (Class) available.get(key.toLowerCase());
			if (type != null) {
				setProperty(key, Config.decode((String) properties.get(key), type));
			}
		}
	}
	
	public void init() {
		setProperty("type", this.getClass().getName());
		
		if (getProperty("description") != null) {
			setDescription((String) getProperty("description"));
		}
	}

	/**
	 * @param nodeDescription
	 */
	public void setDescription(String nodeDescription) {
		hideDescription();
		description = new DescriptionBox(nodeDescription);
	}
	
	/**
	 * 
	 */
	void hideDescription() {
		if (description != null) {
			Morcego.getApplication().remove(description);
			Morcego.notifyRenderer();
		}
	}

	public void setGraph(Graph g) {
		graph = g;
	}
	
	public float getDepth() {
		// TODO I don't know how to tell java that subclass must implement this
		return 0;
	}

	/**
	 * @param e
	 * @return
	 */
	public boolean contains(MouseEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public Graph getGraph() {
		return graph;
	}
	
	public void callJsEvent(String eventName) {
		if (this.getProperty(eventName) != null) {
			Morcego.JSCall((String) this.getProperty(eventName));
		}
	}

}

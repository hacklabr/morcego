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
	
	private Hashtable<String, Object> properties;
	protected Graph graph;
	
	protected DescriptionBox description;
	
	public GraphElement() {
		properties = new Hashtable<String, Object>();
	}
	
	public Hashtable<String, Class> availableProperties() {
		Hashtable<String, Class> prop = new Hashtable<String,Class>();
		prop.put("type", String.class);
		return prop;
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}
	
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}
	
	public Hashtable<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Hashtable<String, String> properties) {
		
		Hashtable available = availableProperties();
		
		for (Enumeration<String> eP = properties.keys(); eP.hasMoreElements(); ) {
			String key = eP.nextElement();
			Class type = (Class) available.get(key);
			if (type != null) {
				setProperty(key, Config.decode(properties.get(key), type));
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

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

package br.arca.morcego;

import java.awt.Color;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import br.arca.morcego.structure.Node;


/**
 * @author lfagundes
 * 
 * Configuration of everything
 */
public class Config {

	private static Hashtable config = new Hashtable();
	
	/*
	 * For documentation purpose, keep development version variables separated
	 * v0.5.0 - stable
	 */

	public static final String backgroundColor = "morcego.backgroundColor";
	public static final String linkDefaultColor = "morcego.linkColor";
	public static final String nodeDefaultType = "morcego.nodeDefaultType";
	public static final String nodeDefaultColor = "morcego.nodeDefaultColor";
    public static final String nodeDefaultImage = "morcego.nodeDefaultImage";
    
	public static final String nodeBorderColor = "morcego.nodeBorderColor";

	public static final String linkDefaultType = "morcego.linkDefaultType";

	public static final String descriptionColor = "morcego.descriptionColor";
	public static final String descriptionBackground = "morcego.descriptionBackground";

	public static final String descriptionBorder = "morcego.descriptionBorder";
	public static final String descriptionMargin = "morcego.descriptionMargin";

	public static final String nodeSize = "morcego.nodeSize";
	public static final String textSize = "morcego.textSize";

	public static final String cameraDistance = "morcego.cameraDistance";
	public static final String minCameraDistance = "morcego.minCameraDistance";
	public static final String fieldOfView = "morcego.fieldOfView";
	
	public static final String width = "morcego.width";
	public static final String height = "morcego.height";

	public static final String maxTheta = "morcego.maxTheta";
	public static final String minTheta = "morcego.minTheta";
	
	public static final String minNodeSize = "morcego.minNodeSize";
	public static final String navigationDepth = "morcego.navigationDepth";
	public static final String feedAnimationInterval = "morcego.feedAnimationInterval";
	public static final String balancingStepInterval = "morcego.balancingStepInterval";
	public static final String fontSizeInterval = "morcego.fontSizeInterval";

	public static final String controlWindowName = "morcego.controlWindowName";

	public static final String renderingFrameInterval = "morcego.renderingFrameInterval";

	public static final String serverUrl = "morcego.serverUrl";

	public static final String transport = "morcego.transportClass";

	public static final String startNode = "morcego.startNode";

	public static String _imageLocation = "morcego._imageLocation";
	
	public static String showMorcegoLogo = "morcego.showMorcegoLogo";
	public static String logoX = "morcego.logoX";
	public static String logoY = "morcego.logoY";

	public static String showArcaLogo = "morcego.showArcaLogo";
	public static String arcaX = "morcego.arcaX";
	public static String arcaY = "morcego.arcaY";

	public static String frictionConstant = "morcego.frictionConstant";
	public static String elasticConstant = "morcego.elasticConstant";
	public static String eletrostaticConstant = "morcego.eletrostaticConstant";
	public static String nodeMass = "morcego.nodeMass";
	public static String nodeCharge = "morcego.nodeCharge";
	public static String springSize = "morcego.springSize";
	public static String punctualElasticConstant = "morcego.punctualElasticConstant";

	public static String loadPageOnCenter = "morcego.loadPageOnCenter";

	public static String centerNodeScale = "morcego.centerNodeScale";

	
	/*
	 * v0.6 - development
	 */
	
	public static String graphIsTree = "morcego.graphIsTree";
	public static String windIntensity = "morcego.windIntensity";

	public static String originX = "morcego.originX";
	public static String originY = "morcego.originY";

	public static String cameraDepth = "morcego.cameraDepth";
	
	/*
	 * Sets all configuration, edit here to modify config
	 */
	public static void init() {

		// These should be set by application, from applet parameters
		// They're set here for developing purposes
		setValue(serverUrl,	"http://morcego.arca.ime.usp.br/tiki-wiki3d_xmlrpc.php");
		setValue(startNode, "HomePage");

		// Colors and layout settings
		
		// Size definition. 
		if (Morcego.getApplication() != null) {
			setValue(width, new Integer(Morcego.getApplication().getBounds().width));
			setValue(height, new Integer(Morcego.getApplication().getBounds().height));
			setValue(originX, new Integer(getInteger(width) / 2));
			setValue(originY, new Integer(getInteger(height) / 2));
		} else {
			// This will only happen during unit tests
			setValue(width, new Integer(100));
			setValue(height, new Integer(100));
			setValue(originX, new Integer(50));
			setValue(originY, new Integer(50));
		}
		
		setValue(showMorcegoLogo, new Boolean("true"));
		setValue(logoX, new Integer(10));
		setValue(logoY, new Integer(10));
		
		setValue(showArcaLogo, new Boolean("true"));
		setValue(arcaX, new Integer(getInteger(width) - 127));
		setValue(arcaY, new Integer(getInteger(height) - 40));
		
		setValue(backgroundColor, new Color(255, 255, 255));

		setValue(linkDefaultType, new String("Solid"));
		setValue(linkDefaultColor, new Color(120, 120, 120));
		
		setValue(nodeDefaultType, new String("Round"));
		setValue(nodeDefaultColor, new Color(255, 0, 0));
        setValue(nodeDefaultImage, new String("default.gif"));
		
        setValue(textSize, new Integer(25));
		setValue(nodeSize, new Integer(15));
		setValue(minNodeSize, new Integer(0));
		setValue(centerNodeScale, new Float(1));

		setValue(nodeBorderColor, new Color(0,0,0));
		setValue(descriptionColor, new Color(40,40,40));
		setValue(descriptionBackground, new Color(200,200,200));
		setValue(descriptionBorder, new Color(0,0,0));
		setValue(descriptionMargin, new Integer(4));

		// Position of the camera
		setValue(cameraDistance, new Integer(500));
		setValue(cameraDepth, new Integer(250));
		setValue(minCameraDistance, new Integer(150));
		setValue(fieldOfView, new Integer(250));

		// Physical model
		setValue(frictionConstant, new Float(0.4f));
		setValue(elasticConstant, new Float(0.3f));
		setValue(punctualElasticConstant, new Float(0.8f));
		setValue(eletrostaticConstant, new Float(1000f));
		setValue(springSize, new Float(100));
		setValue(nodeMass, new Float(5));
		setValue(nodeCharge, new Float(1));
		setValue(windIntensity, new Float(10));
		
		// rotation angle limits (mouse sensitiveness)
		setValue(maxTheta, new Float(20.0f));
		setValue(minTheta, new Float(1.0f));
		
		setValue(renderingFrameInterval, new Integer(50));
		setValue(balancingStepInterval, new Integer(50));
		setValue(fontSizeInterval, new Integer(5));
		
		setValue(transport,"Xmlrpc");
		setValue(feedAnimationInterval, new Integer(100));
		setValue(loadPageOnCenter, new Boolean(false)); 
		setValue(navigationDepth, new Integer(3));

		// Name of window in which URLs should be loaded
		setValue(controlWindowName, "morcegoController");
		
		// Private configuration vars, set by application
		setValue(graphIsTree, new Boolean(false));
		setValue(_imageLocation, new String("br/arca/morcego/image/"));
		
	}

	public static void setValue(String var, Object value) {
		config.put(var, value);
	}

	public static Object getValue(String var) {
		return config.get(var);
	}
	
	public static int getInteger(String var) {
		return ((Integer)getValue(var)).intValue();
	}
	
	public static Color getColor(String var) {
		return (Color)getValue(var);
	}
	
	public static String getString(String var) {
		return (String)getValue(var);
	}
	
	public static boolean getBoolean(String var) {
		return ((Boolean)getValue(var)).booleanValue();
	}
	

	public static Class getTransportClass() {
		return getClass("br.arca.morcego.transport.", getString(transport).concat("Transport"));
	}
	public static Class getNodeClass(String nodeType) {
		return getClass("br.arca.morcego.structure.node.",
						nodeType.concat("Node"));
	}
	public static Class getLinkClass(String linkType) {
		return getClass("br.arca.morcego.structure.link.",
						linkType.concat("Link"));
	}

	public static Class getClass(String domain, String className) {
		Class localClass = null;
		try {
			className = domain.concat(className);
			localClass = Class.forName(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localClass;
	}

	
	public static float getFloat(String var) {
		return ((Float)getValue(var)).floatValue();
	}
	
	public static Enumeration listConfigVars() {
		return config.keys();
	}

	public static Object decode(String value, Class type) {

		if (type.equals(Integer.class)) {
			return Integer.valueOf(value);
		} else if (type.equals(Float.class)) {
			return Float.valueOf(value);
		} else if (type.equals(String.class)) {
			return value;
		} else if (type.equals(Boolean.class)) {
			return new Boolean(value);
		} else if (type.equals(Color.class)) {
			try {
				return Color.decode(value);
			} catch (Exception e) {
				e.printStackTrace();
				return Color.decode("#000000");
			}
		} else if (type.equals(URL.class)) {
			try {
				return new URL(value);
			} catch (MalformedURLException e) {
				// ignore
			}
		}
		return value;
	}

	/*
	 * Tests if a given config var exists, needs introspection
	 */
	public static boolean exists(String varName) {
		Class configClass;
		try {
			configClass = Class.forName("br.arca.morcego.Config");
		} catch (ClassNotFoundException e) {
			// SHOULD NEVER HAPPEN
			e.printStackTrace();
			return false;
		}
		
		Field field;
		
		try {
			field = configClass.getDeclaredField(varName);
		} catch (Exception e) {
			return false;
		} 
				
		try {
			return config.contains(field.get(null));
		} catch (Exception e) {
			return false;
		}
	}
}

/*
 * Created on May 24, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package br.arca.morcego.structure.node;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.image.ImageObserver;
import java.awt.Rectangle;

import java.util.regex.*;

import br.arca.morcego.Morcego;

import br.arca.morcego.Config;
import br.arca.morcego.structure.Graph;
import br.arca.morcego.structure.Node;


/**
 * @author lfagundes
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ImageNode extends FancyNode { 
	
    private Image image;


	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D graphic = (Graphics2D) g;

        ImageObserver o = Morcego.getApplication();
        
		g.drawImage(image, boundRectangle.x, boundRectangle.y,                         
                        image.getWidth(o)*nodeSize/Config.getInteger(Config.nodeSize),
                        image.getHeight(o)*nodeSize/Config.getInteger(Config.nodeSize), this);
        
		boundRectangle = new Rectangle(boundRectangle.x, boundRectangle.y, image.getWidth(o), image.getHeight(o));

		AffineTransform at = new AffineTransform(40, 0, 0, 4, 00, 0);

		FontRenderContext frc = new FontRenderContext(at, false, false);

		int interval = Config.getInteger(Config.fontSizeInterval);
		Font font =
			new Font(null, Font.PLAIN, (int) (textSize / interval) * interval);

		TextLayout l = new TextLayout((String)getProperty("title"), font, frc);

		Rectangle2D textBounds = l.getBounds();

		l.draw(graphic, (int) (getBody().projection.x - textBounds.getWidth() / 2), boundRectangle.y);
	}
               
	public void init() {
		super.init();
		            
		if (getProperty("image") == null) {
			setProperty("image", Config.getString(Config.nodeDefaultImage));
		}
		            
		String imgLocation = (String)getProperty("image");

		Pattern urlPattern = Pattern.compile("^http://");
				
		if (urlPattern.matcher(imgLocation).find()) {
			URL location;
			try {
				location = new URL(imgLocation);
				image = Toolkit.getDefaultToolkit().getImage(location);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} else {
			imgLocation = Config.getString(Config._imageLocation).concat(imgLocation);
			URL location = getClass().getClassLoader().getResource(imgLocation);
			image = Toolkit.getDefaultToolkit().getImage(location);
		}
	}

}

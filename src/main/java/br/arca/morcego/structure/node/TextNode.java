/*
 * Created on Feb 23th, 2004
 * 
 */
package br.arca.morcego.structure.node;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import br.arca.morcego.Config;
import br.arca.morcego.structure.Graph;
import br.arca.morcego.structure.Node;

/**
 * @author lfagundes
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class TextNode extends Node { 

	protected int textSize;

	public TextNode() {
		super();
	}

	/**
	 * @param id
	 * @param graph
	 */
	public TextNode(String id, Graph graph) {
		super(id, graph);
	}

	public void proj() {

		textSize =
			(int) Math.round(
				(double) Config.getInteger(Config.textSize)
					* getScale());
	
		/*
		if (this.centered()) {
			textSize = (int) (textSize * 1.5);
		}
		*/
		
	}
	
	public void paint(Graphics g) {
		proj();
		
		Graphics2D graphic = (Graphics2D) g;

		AffineTransform at = new AffineTransform(40, 0, 0, 4, 00, 0);

		FontRenderContext frc = new FontRenderContext(null, false, false);

		int interval = Config.getInteger(Config.fontSizeInterval);
		Font font =
			new Font(null, Font.PLAIN, (int) (textSize / interval) * interval);

		String nodeName = (String) getProperty("title");
		
		TextLayout l = new TextLayout(nodeName, font, frc);
	
		Rectangle2D textBounds = l.getBounds();
			
		boundRectangle = new Rectangle((int) (getBody().projection.x - textBounds.getWidth() / 2),
									   (int) (getBody().projection.y - textBounds.getHeight() / 2),
									   (int) textBounds.getWidth(),
									   (int) textBounds.getHeight());
				
		l.draw(graphic, boundRectangle.x, (int)(boundRectangle.y+textBounds.getHeight()));
	}



}

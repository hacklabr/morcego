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
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import br.arca.morcego.Config;
import br.arca.morcego.run.Renderer;
import br.arca.morcego.structure.Graph;
import br.arca.morcego.structure.Node;

/**
 * @author lfagundes
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class RoundNode extends Node { 
	
	protected int textSize;

	// relative size of ball and text
	protected int nodeSize;


	public RoundNode() {
		super();
	}

	/**
	 * @param id
	 * @param graph
	 */
	public RoundNode(String id, Graph graph) {
		super(id, graph);
	}

	public void proj() {

		nodeSize = (int) Math.round((double) Config.getInteger(Config.nodeSize)
				* getScale());

		if (nodeSize < Config.getInteger(Config.minNodeSize))
			nodeSize = Config.getInteger(Config.minNodeSize);

		int c = nodeSize / 2;

		int cornerU = body.projection.x - c;
		int cornerV = body.projection.y - c;

		boundRectangle = new Rectangle(cornerU, cornerV, nodeSize, nodeSize);
	
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

		graphic.setColor(
			Renderer.fadeColor((Color) getProperty("color"), getScale()));

		graphic.fillOval(boundRectangle.x, boundRectangle.y, nodeSize, nodeSize);
		graphic.setColor(
			Renderer.fadeColor(
				Config.getColor(Config.nodeBorderColor),
				getScale()));
		graphic.drawOval(boundRectangle.x, boundRectangle.y, nodeSize, nodeSize);

		AffineTransform at = new AffineTransform(40, 0, 0, 4, 00, 0);

		FontRenderContext frc = new FontRenderContext(at, false, false);

		int interval = Config.getInteger(Config.fontSizeInterval);
		Font font =
			new Font(null, Font.PLAIN, (int) (textSize / interval) * interval);

		String nodeName = (String) getProperty("title");
		if (nodeName.length() > 0) {
			TextLayout l = new TextLayout(nodeName, font, frc);
	
			Rectangle2D textBounds = l.getBounds();
	
			l.draw(graphic, (int) (getBody().projection.x - textBounds.getWidth() / 2), boundRectangle.y);
		}
	}



}

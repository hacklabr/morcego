/*
 * Created on Oct 29, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package br.arca.morcego.structure.node;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import br.arca.morcego.Config;
import br.arca.morcego.run.Renderer;

public class TriangleNode extends FancyNode {

	/**
	 * Make eclipse happy
	 */
	private static final long serialVersionUID = 1L;
	

	public void paint(Graphics g) {
		proj();

		Graphics2D graphic = (Graphics2D) g;

		graphic.setColor(
			Renderer.fadeColor((Color) getProperty("color"), getScale()));

		Polygon p = new Polygon();
		p.addPoint(boundRectangle.x, boundRectangle.y + nodeSize);
		p.addPoint(boundRectangle.x + nodeSize, boundRectangle.y + nodeSize);
		p.addPoint(boundRectangle.x + nodeSize/2, boundRectangle.y);
		graphic.fillPolygon(p);
		graphic.setColor(
			Renderer.fadeColor(
				Config.getColor(Config.nodeBorderColor),
				getScale()));
		graphic.drawPolygon(p);

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

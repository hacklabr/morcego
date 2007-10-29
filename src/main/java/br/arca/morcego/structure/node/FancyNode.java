/*
 * Created on Oct 29, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package br.arca.morcego.structure.node;

import java.awt.Rectangle;

import br.arca.morcego.Config;
import br.arca.morcego.structure.Graph;
import br.arca.morcego.structure.Node;

public abstract class FancyNode extends Node {
	
	protected int textSize;
	
	// relative size of image
	protected int nodeSize;


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
}

/*
 * Created on Nov 22, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package br.arca.morcego.structure.link;

import java.awt.Graphics;

import br.arca.morcego.physics.Vector3D;
import br.arca.morcego.run.Renderer;
import br.arca.morcego.structure.Link;
import br.arca.morcego.structure.Node;

public class DashedDirectionalLink extends Link {
	
	private int parts = 10;
	
	public DashedDirectionalLink() {
		super();
	}
	/**
	 * @param n1
	 * @param n2
	 * @throws LinkingDifferentGraphs 
	 */
	public DashedDirectionalLink(Node n1, Node n2) {
		super(n1, n2);
	}
	
	public void paint(Graphics g) {
		Vector3D body1 = node1.getBody();
		Vector3D body2 = node2.getBody();

		g.setColor(Renderer.fadeColor(color, Math.min(body1.getScale(), body2.getScale())));
		
		int sections = parts * 2;

		Vector3D start = new Vector3D(body1.projection.x, body1.projection.y, 0);
		Vector3D inc = new Vector3D(body2.projection.x, body2.projection.y, 0).getVectorFrom(start);
		Vector3D end = start.makeClone();
		
		inc.resize((float)1/sections);
		
		for (int i=2; i<sections; i++) {
			end.add(inc);

			if (i%2 == 0) {
				start.proj();
				end.proj();
				
				g.drawLine((int)start.x, (int)start.y, 
						   (int)end.x, (int)end.y);
			}
			start.add(inc);
			
		}
		
		Vector3D arrow = inc.opposite().multiplyByScalar(2);
		
		double angle = Math.PI/6;
		
		float newX = (float) (arrow.x * Math.cos(angle) + arrow.y * Math.sin(angle));
		float newY = (float) (arrow.y * Math.cos(angle) - arrow.x * Math.sin(angle));
		g.drawLine((int)end.x, (int)end.y, (int)(end.x+newX), (int)(end.y+newY));
		
		angle *= -1;
		
		newX = (float) (arrow.x * Math.cos(angle) + arrow.y * Math.sin(angle));
		newY = (float) (arrow.y * Math.cos(angle) - arrow.x * Math.sin(angle));
		g.drawLine((int)end.x, (int)end.y, (int)(end.x+newX), (int)(end.y+newY));
		
	}
}


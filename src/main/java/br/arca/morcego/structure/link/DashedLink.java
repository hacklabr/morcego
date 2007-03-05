/*
 * Created on May 24, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package br.arca.morcego.structure.link;

import java.awt.Graphics;

import br.arca.morcego.Config;
import br.arca.morcego.physics.PunctualBody;
import br.arca.morcego.physics.Vector3D;
import br.arca.morcego.run.Renderer;
import br.arca.morcego.structure.Graph;
import br.arca.morcego.structure.Link;
import br.arca.morcego.structure.Node;

/**
 * @author lfagundes
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class DashedLink extends Link {

	private Integer parts = 10;
	
	public DashedLink() {
		super();
	}
	/**
	 * @param n1
	 * @param n2
	 * @throws LinkingDifferentGraphs 
	 */
	public DashedLink(Node n1, Node n2) {
		super(n1, n2);
	}
	
	public void paint(Graphics g) {
		Vector3D body1 = node1.getBody();
		Vector3D body2 = node2.getBody();

		g.setColor(
				Renderer.fadeColor(
					Config.getColor(Config.linkColor),
					Math.min(body1.getScale(), body2.getScale())));
		
		int sections = parts * 2 - 1;

		Vector3D start = new Vector3D(body1.projection.x, body1.projection.y, 0);
		Vector3D inc = new Vector3D(body2.projection.x, body2.projection.y, 0).getVectorFrom(start);
		Vector3D end = start.clone();
		
		inc.resize((float)1/sections);
		end.add(inc);
		
		for (int i=0; i<sections; i++) {
			if (i%2 == 0) {
				start.proj();
				end.proj();
				
				g.drawLine((int)start.x, (int)start.y, 
						   (int)end.x, (int)end.y);
			}
			start.add(inc);
			end.add(inc);
			
		}
	}
}

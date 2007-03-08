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
public class DirectionalLink extends Link {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5189503975335282448L;

	public DirectionalLink() {
		super();
	}
	/**
	 * @param n1
	 * @param n2
	 * @throws LinkingDifferentGraphs 
	 */
	public DirectionalLink(Node n1, Node n2) {
		super(n1, n2);
	}
	
	public void paint(Graphics g) {
		Vector3D body1 = node1.getBody();
		Vector3D body2 = node2.getBody();

		g.setColor(
				Renderer.fadeColor(
					Config.getColor(Config.linkColor),
					Math.min(body1.getScale(), body2.getScale())));
		

		Vector3D start = new Vector3D(body1.projection.x, body1.projection.y, 0);
		Vector3D inc = new Vector3D(body2.projection.x, body2.projection.y, 0).getVectorFrom(start);
		inc.resize((float)0.8);
		Vector3D end = start.clone();
		end.add(inc);
		
		g.drawLine((int)start.x, (int)start.y, (int)end.x, (int)end.y);
		
		start = end.clone();
		start.add(inc.multiplyByScalar((float) 0.1));

		Vector3D arrow = inc.opposite().multiplyByScalar((float)0.15);
		
		double angle = Math.PI/6;
		
		float newX = (float) (arrow.x * Math.cos(angle) + arrow.y * Math.sin(angle));
		float newY = (float) (arrow.y * Math.cos(angle) - arrow.x * Math.sin(angle));
		g.drawLine((int)start.x, (int)start.y, (int)(end.x+newX), (int)(end.y+newY));
		
		angle *= -1;
		
		newX = (float) (arrow.x * Math.cos(angle) + arrow.y * Math.sin(angle));
		newY = (float) (arrow.y * Math.cos(angle) - arrow.x * Math.sin(angle));
		g.drawLine((int)start.x, (int)start.y, (int)(end.x+newX), (int)(end.y+newY));
		
		
	}
}

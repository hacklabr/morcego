/*
 * Created on Nov 22, 2007
 *
 */
package br.arca.morcego.structure.link;

import br.arca.morcego.structure.Link;
import br.arca.morcego.structure.Node;

public class InvisibleLink extends Link {

	public InvisibleLink() {
		super();
	}
	/**
	 * @param n1
	 * @param n2
	 * @throws LinkingDifferentGraphs 
	 */
	public InvisibleLink(Node n1, Node n2) {
		super(n1, n2);
	}
}

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
package br.arca.morcego.run;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

import br.arca.morcego.Config;
import br.arca.morcego.structure.Graph;
import br.arca.morcego.structure.GraphElement;
import br.arca.morcego.structure.Node;

/**
 * @author lfagundes
 * 
 * Takes from Feeder a list of nodes that should become visible and show them in a 
 * fancy way
 */
public class Animator extends ControlledRunnable {

	private Graph graph;
	private Vector visible;
	
	/**
	 *  
	 */
	public Animator(Graph g) {
		graph = g;
		visible = new Vector();
	}

	/*
	 * Takes a vector of nodes to be added and one to be removed
	 * and notifies thread that is animating.	 
	 */
	public void animate(Vector visible) {
		synchronized (this) {
			this.visible = visible;
			this.notify();
		}
	}

	/*
	 * (non-Javadoc)
	 * When notified takes the added and removed nodes and
	 * animate the insertion and deletion
	 * TODO: When user navigates during animation the removed
	 * nodes remain
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		running = true;
		
		while (running) {
			try {
				if (visible.isEmpty()) {
					synchronized (this) {
						this.wait();
					}
				}
			} catch (InterruptedException e2) {				
				break;
			}
			
			graph.calculateDistances();
			graph.hideFarNodes();
			Collections.sort(visible, new AnimationStrategy());
			
			while (!visible.isEmpty()) {
				Node node = (Node) visible.remove(0);
				
				graph.showNode(node);
				
				try {
					Thread.sleep(
						Config.getInteger(Config.feedAnimationInterval));
				} catch (InterruptedException e1) {					
					e1.printStackTrace();
				}
			}
			visible = new Vector();
		}
	}
	

	private final class AnimationStrategy implements Comparator {
		public int compare(Object o1, Object o2) {
			Node n1 = (Node) o1;
			Node n2 = (Node) o2;
			
			if (n1.getCenterDistance().intValue() < n2.getCenterDistance().intValue()) {
				return -1;
			}
			if (n1.getCenterDistance().intValue() > n2.getCenterDistance().intValue()) {
				return 1;
			}
			return 0;			
		}
	}

}

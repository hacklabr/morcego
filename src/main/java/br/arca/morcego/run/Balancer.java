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

import java.util.*;

import br.arca.morcego.Config;
import br.arca.morcego.Morcego;
import br.arca.morcego.physics.PunctualBody;
import br.arca.morcego.physics.Vector3D;
import br.arca.morcego.structure.Graph;
import br.arca.morcego.structure.Link;
import br.arca.morcego.structure.Node;

/**
 * @author lfagundes
 * 
 * Balancer is responsible for calculating the forces over each node and
 * setting node speed to get the graph balanced.
 */
public class Balancer extends ControlledRunnable {

	private boolean balancing = true;
	private boolean balancingLock = false;
	private Graph graph;
	private int balancedCount;

	public Balancer(Graph g) {
		graph = g;
		balancedCount = 0;
	}

	/*
	 * Stops the balancing when graph is stable
	 */
	public void stop() {
		if (!balancingLock) {
			balancing = false;
		}
	}

	/*
	 * Avoid stoping, for example when user is holding a node
	 */
	public void lockBalance() {
		balancingLock = true;
	}

	/*
	 *  
	 */
	public void unlockBalance() {
		balancingLock = false;
	}

	/*
	 * Restart balancing
	 */
	public void awake() {
		balancing = true;
		synchronized (this) {
			this.notify();
		}
	}

	/*
	 *  
	 */
	public void blow(Node node) {

		PunctualBody body = node.getBody();
		
		int levelDifference;
		
		try {
			levelDifference = node.getLevel() - graph.getCenterNode().getLevel();
		} catch (NullPointerException e) {
			// no center node, happens during unit tests
			return;
		}
		
		Vector3D wind = graph.getOrientation().multiplyByScalar(Config.getFloat(Config.windIntensity));
		
		node.getBody().getInstantForce().add(wind.multiplyByScalar(levelDifference));
	}

	/*
	 * (non-Javadoc) Calculates all forces acting over nodes and apply them
	 * until graph is stable
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		try {

			int cycleCount = 0;
			
			running = true;
			
			while (running) {

				waitUntilBalancingIsNeeded();

				runBalancingEngine();

				finishedBalancing();
				
				if (cycleCount % 20 == 0) {
					cycleCount = 0;
					
					checkSystemStability();
				}
				
				cycleCount++;

			}
		} catch (InterruptedException e) {
		}

	}

	public void runBalancingEngine() {
		checkAllForces();
		balance();
		Morcego.getCamera().adjustPosition(graph);
	}

	private void finishedBalancing() throws InterruptedException {
		// balances may have stopped animation
		if (balancing) {
			balancedCount = 0;
		}

		Morcego.notifyRenderer();

		Thread.sleep(Config.getInteger(Config.balancingStepInterval));
	}

	private void balance() {
		Enumeration en = getNodes().elements();
		while (en.hasMoreElements()) {
			Node node = (Node) en.nextElement();
			node.move();
		}
	}

	/**
	 * Checks if system is stable, if so stops balancing engine
	 */
	private void checkSystemStability() {
		Enumeration en = getNodes().elements();
		boolean stable = true;
		while (stable && en.hasMoreElements()) {
			Node node = (Node) en.nextElement();
			if (node.getBody().getSpeed().module() > 1) {
				stable = false;
			}
		}
		if (stable) {
			stop();
		}
	}

	private void checkAllForces() {
		boolean isTree =
			Config.getBoolean(Config.graphIsTree);

		for (int j = 0; j < getNodes().size(); j++) {
			Node node1 = (Node) getNodes().elementAt(j);
					
			if (isTree) {
				blow(node1);
			}

			for (int k = j + 1; k < getNodes().size(); k++) {
				Node node2 = (Node) getNodes().elementAt(k);
				Vector3D force = node1.getBody().repel(node2.getBody());
				node1.getBody().getInstantForce().add(force);
				node2.getBody().getInstantForce().add(force.opposite());
			}
			
		}
	
		for (int j = 0; j < getLinks().size(); j++) {
			Link link = (Link) getLinks().elementAt(j);
			Vector3D force = link.getSpring().strech();
			link.getNode1().getBody().getInstantForce().add(force);
			link.getNode2().getBody().getInstantForce().add(force.opposite());
		}
		
			
		for (int j = 0; j < getNodes().size(); j++) {
			((Node)getNodes().elementAt(j)).getBody().applyForce();
		}
	}

	private Vector getNodes() {
		return graph.getVisibleNodes();
	}
	
	
	private Vector getLinks() {
		return graph.getVisibleLinks();
	}

	private void waitUntilBalancingIsNeeded() throws InterruptedException {
		while (!balancing) {
			synchronized (this) {
				this.wait();
			}
		}
	}

	/**
	 * @param node
	 */
	public void notifyBalanced(Node node) {
		balancedCount++;
		if (balancedCount == graph.getVisibleNodes().size()) {
			stop();
		}
	}
}

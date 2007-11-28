/*
 * Created on Nov 28, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package br.arca.morcego.run;

public abstract class ControlledRunnable implements Runnable {

		protected boolean running = false;
		
		public void finish() {
			synchronized (this) {
				running = false;
			}
		}
}

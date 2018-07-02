package org.neotech.tmb;

import org.neotech.tmb.cli.CommandParser;

/**
 * 
 * @author
 *
 */
public class App {

	/* Class */

	public static void main(String[] args) throws Throwable {

		CommandParser cli = new CommandParser();

		Thread destroyHook = new Thread() {
			public void run() {
				if (cli != null) {
					cli._CommandParser();
				}
			}
		};
		
		destroyHook.setName("ShutdownThread");
		Runtime.getRuntime().addShutdownHook(destroyHook);

		Thread executeThread = new Thread() {
			public void run() {
				if (cli != null) {
					cli.execute(args);
				}
			}
		};

		executeThread.setName("ExecuteThread");
		executeThread.start();
	}

}

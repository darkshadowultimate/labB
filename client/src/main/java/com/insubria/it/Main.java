package com.insubria.it;


import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_interface.*;

public class Main {
	/**
	 * This main method starts when the client is started.
	 * The client gets the reference of the remote object in order to communicate with the server
	 * by calling RemoteObjectContextProvider.setServerRemoteObject(args);
	 * Then the the login page will be displayed on the screen (new LoginUtente())
	 *
	 * @param args - args passed while starting the client (it's expected to be the IP address of the server)
	 */
	public static void main(String[] args) {
		RemoteObjectContextProvider.setServerRemoteObject(args);

		LoginUtente loginUtente = new LoginUtente();
	}
}
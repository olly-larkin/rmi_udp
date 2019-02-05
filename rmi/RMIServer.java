/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (totalMessages == -1) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
			for (int i = 0; i < totalMessages; ++i) {
				receivedMessages[i] = 0;
			}
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (msg.messageNum == totalMessages - 1) {
			int messagesReceived = 0;
			System.out.print("Messages dropped:\n");
			for (int i = 0; i < totalMessages; ++i) {
				if (receivedMessages[i] == 1)
					messagesReceived++;
				else
					System.out.print("Message " + i + " dropped\n");
			}
			System.out.print("\n" + messagesReceived + " messages successfully sent: " + ((double)messagesReceived/(double)totalMessages)*100.00 + "% success\n");
		}
	}


	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// TO-DO: Instantiate the server class
		try {
			rmis = new RMIServer();
			rebindServer("rmi://RMIServer", rmis);
		} catch (Exception e) {
			System.out.print("Error: " + e);
		}
	}

	protected static void rebindServer(String serverURL, RMIServer server) throws Exception {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		Registry reg = LocateRegistry.createRegistry(1099);

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		try {
			reg.rebind(serverURL, server);
		} catch(Exception e) {
			throw e;
		}
	}
}

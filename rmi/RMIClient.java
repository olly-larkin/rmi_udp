/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;
		long timeTakenM = 0;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}


		// TO-DO: Bind to RMIServer
		try {
			Registry reg = LocateRegistry.getRegistry(args[0]);
			iRMIServer = (RMIServerI)reg.lookup(urlServer);
		} catch(Exception e) {
			System.out.print("Error: " + e);
		}

		timeTakenM = System.nanoTime();
		// TO-DO: Attempt to send messages the specified number of times
		for (int i = 0; i < numMessages; ++i) {
			MessageInfo msg = new MessageInfo(numMessages, i+1);
			try {
				iRMIServer.receiveMessage(msg);
			} catch(RemoteException e) {
				System.out.print("Message " + i + " not sent: " + e);
			}
		}
		timeTakenM = System.nanoTime() - timeTakenM;

		System.out.println("Took " + (double)timeTakenM/1000000.00 + " milliseconds: Avg " + (double)(timeTakenM/1000000)/(double)numMessages + " milliseconds per message.");
	}
}

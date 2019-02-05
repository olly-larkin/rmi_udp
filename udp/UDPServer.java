/*
 * Created on 01-Mar-2016
 */
package udp;

import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int passedMessages = 0;
	private int[] receivedMessages;

	private void run() {
		byte[]			pacData = new byte[20];
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		pac = new DatagramPacket(pacData, 20);
		while(passedMessages < totalMessages || totalMessages == -1) {
			try {
				recvSoc.receive(pac);
				processMessage(new String(pac.getData()).trim());
			} catch(SocketTimeoutException e) { 
				if (totalMessages != -1) {
					System.out.println("Timeout occurred: " + e);
					break;
				}
			} catch(Exception e) {
				System.out.println("Exception in run: " + e);
				System.exit(-1);
			}
		}

		int messagesReceived = 0;
		System.out.print("Messages dropped:\n");
		for (int i = 0; i < totalMessages; ++i) {
			if (receivedMessages[i] == 1)
				messagesReceived++;
			else
				System.out.print("Message " + i+1 + " dropped\n");
		}
		System.out.print(messagesReceived + " messages successfully recieved: " + ((double)messagesReceived/(double)totalMessages)*100.00 + "% success\n\n");
	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try {
			msg = new MessageInfo(data);
		} catch(Exception e) {
			System.out.println("Exception in processMessage: " + e);
			System.exit(-1);
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (totalMessages == -1) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
			for (int i = 0; i < totalMessages; ++i) {
				receivedMessages[i] = 0;
			}
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum-1] = 1;
		System.out.print(msg.toString());
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
			recvSoc.setSoTimeout(2000);
		} catch(Exception e) {
			System.out.println("Exception in UDPServer: " + e);
			System.exit(-1);
		}
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);
		server.run();
	}

}

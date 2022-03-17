package os.chat;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

import os.chat.client.ChatClient;
import os.chat.client.ChatClientWindow;
import os.chat.server.ChatServerManager;

/**
 * Class used to test different use case iteratively during the implementation of the lab
 * @author MaximeWelcklen
 *
 */
public class ChatMain {
	//Allow fast restart of the app
	//public static final int REGISTRY_PORT = new Random().nextInt(10000, 15000);
	//Finetune the port
	public static final int REGISTRY_PORT = 5678;

	public static void main(String[] args) {
		//Start server (first start)
		start_server();
		

		//create client(s)
		start_client();
	}
	
	/**
	 * Start the backend of the app (create refistry + manager)
	 */
	public static void start_server() {
		//Create the registry
		try {
			System.out.println("Initializing register at port " + REGISTRY_PORT);
			LocateRegistry.createRegistry(REGISTRY_PORT);
		} catch (RemoteException e) {
			System.err.println("Registry creation failed");
			e.printStackTrace();
		}
		
		//create the manager
		ChatServerManager.getInstance();
	}
	
	/**
	 * Open a client (Must have a server already setup)
	 */
	public static void start_client() {
		ChatClientWindow.main(null);
	}
}

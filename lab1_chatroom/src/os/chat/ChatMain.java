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
	public static final int REGISTRY_PORT = new Random().nextInt(10000, 15000);

	public static void main(String[] args) {
		//Create the registry
		try {
			System.out.println("Initializing register at random port " + REGISTRY_PORT);
			LocateRegistry.createRegistry(REGISTRY_PORT);
		} catch (RemoteException e) {
			System.err.println("Registry creation failed");
			e.printStackTrace();
		}

		single_client();
	}
	
	public static void single_client() {
		ChatServerManager.getInstance();
		ChatClientWindow.main(null);
	}
}

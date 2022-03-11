package os.chat.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ChatServerMain {
	public static final int REGISTRY_PORT = 9000;
	
	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(REGISTRY_PORT);
			new ChatServer();
		} catch (RemoteException e) {
			System.err.println("Server registration failed");
			e.printStackTrace();
		}
	}
}

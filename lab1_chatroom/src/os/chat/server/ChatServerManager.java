package os.chat.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * This class manages the available {@link ChatServer}s and available rooms.
 * <p>
 * At first you should not modify its functionalities but only export
 * them for being called by the {@link ChatClient}.
 * <p>
 * Later you will modify this to allow creating new rooms and
 * looking them up from the {@link ChatClient}.
 */
public class ChatServerManager implements ChatServerManagerInterface {
	//const
	private static final int SERVER_MANAGER_REGISTRY_PORT = 0;

	//Remove chatroomnames as to keep both list synchronized
	private Vector<ChatServer> chatRooms;

    private static ChatServerManager instance = null;
    
	private Registry registry;
	
	/**
	 * Constructor of the <code>ChatServerManager</code>.
	 * <p>
	 * Must register its functionalities as stubs to be called from RMI by
	 * the {@link ChatClient}.
	 */
	private ChatServerManager () {
		//Register this manager to the registry using the registry name
		try {
			ChatServerManagerInterface serverManager = (ChatServerManagerInterface) UnicastRemoteObject.exportObject(this, SERVER_MANAGER_REGISTRY_PORT);
			registry = LocateRegistry.getRegistry();
			registry.bind(ChatServerManagerInterface.SERVER_MANAGER_REGISTRY_NAME, serverManager);
		} catch (RemoteException | AlreadyBoundException e) {
			System.err.println("Could not register the server");
			e.printStackTrace();
		}
		
		// initial: we create a single chat room and the corresponding ChatServer
		chatRooms.add(new ChatServer("sports"));
	}
	
	public ChatServerManager ChatServerManagerInstance() {
		if(instance == null) {
			instance = new ChatServerManager();
		}
		return instance;
	}

    /**
     * Retrieves the chat server manager instance. This method creates a
     * singleton chat server manager instance if none was previously created.
     * @return a reference to the singleton chat server manager instance
     */
    public static ChatServerManager getInstance() {
	if (instance == null)
	    instance = new ChatServerManager();

	return instance;
    }

        /**
	 * Getter method for list of chat rooms.
	 * @return  a list of chat rooms
	 * @see Vector
	 */
	public Vector<String> getRoomsList() {
		return chatRooms.stream().map(ChatServer::getRoomName).collect(Collectors.toCollection(Vector::new));
	}

        /**
	 * Creates a chat room with a specified room name <code>roomName</code>.
	 * @param roomName the name of the chat room
	 * @return <code>true</code> if the chat room was successfully created,
	 * <code>false</code> otherwise.
	 */
	public boolean createRoom(String roomName) {
		
		System.err.println("server manager method createRoom not implemented.");
		
		/*
		 * TODO add the code to create a new room
		 */
		
		return false;
	}	
	
}

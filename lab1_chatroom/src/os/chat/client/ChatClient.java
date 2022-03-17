package os.chat.client;


import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import os.chat.ChatMain;
import os.chat.server.ChatServer;
import os.chat.server.ChatServerInterface;
import os.chat.server.ChatServerManagerInterface;

/**
 * This class implements a chat client that can be run locally or remotely to
 * communicate with a {@link ChatServer} using RMI.
 */
public class ChatClient implements CommandsFromWindow, CommandsFromServer {
	
	private static final int CLIENT_REGISTRY_PORT = 0;
	/**
	 * The name of the user of this client
	 */
	private String userName;
	private Registry registry;
	private ChatServerManagerInterface serverManager;
	
  /**
   * The graphical user interface, accessed through its interface. In return,
   * the GUI will use the CommandsFromWindow interface to call methods to the
   * ChatClient implementation.
   */
	private final CommandsToWindow window ;
	
  /**
   * Constructor for the <code>ChatClient</code>. Must perform the connection to the
   * server. If the connection is not successful, it must exit with an error.
   * 
   * @param window reference to the GUI operating the chat client
   * @param userName the name of the user for this client
   * @since Q1
   */
	public ChatClient(CommandsToWindow window, String userName) {
		this.window = window;
		this.userName = userName;
		
		//At construction, the client fetches the server manager
		try {
			registry = LocateRegistry.getRegistry(ChatMain.REGISTRY_PORT);
			serverManager = (ChatServerManagerInterface) registry.lookup(ChatServerManagerInterface.SERVER_MANAGER_REGISTRY_NAME);
			//Register itself so that it can be called (receivemsg from server)
			CommandsFromServer commandsFromServer = (CommandsFromServer) UnicastRemoteObject.exportObject(this, CLIENT_REGISTRY_PORT);
			registry.rebind(userName, commandsFromServer);

		} catch (RemoteException | NotBoundException e) {
			System.err.println("Failure in retreival of the server manager from the client");
			e.printStackTrace();
		}
	}

	/*
	 * Implementation of the functions from the CommandsFromWindow interface.
	 * See methods description in the interface definition.
	 */

	/**
	 * Sends a new <code>message</code> to the server to propagate to all clients
	 * registered to the chat room <code>roomName</code>.
	 * @param roomName the chat room name
	 * @param message the message to send to the chat room on the server
	 */
	public void sendText(String roomName, String message) {
		ChatServerInterface chatServer = lookupChatServer(roomName);
		//operation failed
		if(chatServer == null) {
			System.err.println("Failure - Client can not lookup chat server");
			return;
		}
		
		try {
			chatServer.publish(message, this.userName);
		} catch (RemoteException e) {
			System.err.println("Failure - Client can not send message to chatmanager");
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the list of chat rooms from the server (as a {@link Vector}
	 * of {@link String}s)
	 * @return a list of available chat rooms or an empty Vector if there is
	 * none, or if the server is unavailable
	 * @see Vector
	 */
	public Vector<String> getChatRoomsList() {
		//Return empty list as fallback
		Vector<String> chatRoomsList = new Vector<String>();
		try {
			chatRoomsList = serverManager.getRoomsList();
		} catch (RemoteException e) {
			System.err.println("Could not get room list from server manager");
		}
		return chatRoomsList;
	}

	/**
	 * Join the chat room. Does not leave previously joined chat rooms. To
	 * join a chat room we need to know only the chat room's name.
	 * @param name the name (unique identifier) of the chat room
	 * @return <code>true</code> if joining the chat room was successful,
	 * <code>false</code> otherwise
	 */
	public boolean joinChatRoom(String roomName) {
		ChatServerInterface chatServer = lookupChatServer(roomName);
		//operation failed
		if(chatServer == null) {
			System.err.println("Failure - Client can not lookup chat server");
			return false;
		}
		
		try {
			chatServer.register(this);
			return true;
		} catch (RemoteException e) {
			System.err.println("Failure - Client can not register to chat server");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Leaves the chat room with the specified name
	 * <code>roomName</code>. The operation has no effect if has not
	 * previously joined the chat room.
	 * @param roomName the name (unique identifier) of the chat room
	 * @return <code>true</code> if leaving the chat room was successful,
	 * <code>false</code> otherwise
	 */	
	public boolean leaveChatRoom(String roomName) {
		ChatServerInterface chatServer = lookupChatServer(roomName);
		//operation failed
		if(chatServer == null) {
			System.err.println("Failure - Client can not lookup chat server");
			return false;
		}
		
		try {
			chatServer.unregister(this);
		} catch (RemoteException e) {
			System.err.println("Failure - Client can not register to chat server");
			e.printStackTrace();
		}
		return true;
	}
	
	private ChatServerInterface lookupChatServer(String roomName) {
		//Search chatroom on the registr
		try {
			roomName = ChatServerInterface.registeredRoomName(roomName);
			ChatServerInterface chatServer = (ChatServerInterface) registry.lookup(roomName);
			return chatServer;
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Cannot find the server");
			e.printStackTrace();
		}
		return null;
	}

    /**
     * Creates a new room named <code>roomName</code> on the server.
     * @param roomName the chat room name
     * @return <code>true</code> if chat room was successfully created,
     * <code>false</code> otherwise.
     */
	public boolean createNewRoom(String roomName) {
		try {
			serverManager.createRoom(roomName);
			return true;
		} catch (RemoteException e) {
			System.err.println("Failure - Client can not register to chat server");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Implementation of the functions from the CommandsFromServer interface.
	 * See methods description in the interface definition.
	 */
	
	
	/**
	 * Publish a <code>message</code> in the chat room <code>roomName</code>
	 * of the GUI interface. This method acts as a proxy for the
	 * {@link CommandsToWindow#publish(String chatName, String message)}
	 * interface i.e., when the server calls this method, the {@link
	 * ChatClient} calls the 
	 * {@link CommandsToWindow#publish(String chatName, String message)} method 
	 * of it's window to display the message.
	 * @param roomName the name of the chat room
	 * @param message the message to display
	 */
	public void receiveMsg(String roomName, String message) {
		System.out.println("Receive message " + message + " for chatroom " + roomName);
		this.window.publish(roomName, message);
	}
}

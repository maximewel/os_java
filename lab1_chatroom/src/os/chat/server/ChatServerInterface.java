package os.chat.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import os.chat.client.CommandsFromServer;

/**
 * This interface is the set of commands that can be called remotely for the
 * {@link ChatServer}.
 * @since Q2
 */
public interface ChatServerInterface extends Remote {

	/**
	 * receives a message from a client and send it to all subscribed clients
	 * @param message The message to propagate
	 */
	public void publish(String message, String publisher) throws RemoteException;
	
	/**
	 * registers a new client to the chat room
	 * @param clientLookupName the name of the client as registered on the RMI registry
	 */
	public void register(CommandsFromServer client) throws RemoteException;
	
	/**
	 * unregisters a new client to the chat room
	 * @param clientLookupName the name of the client as registered on the RMI registry
	 */
	public void unregister(CommandsFromServer client) throws RemoteException;
	
	/**
	 * Get the rom name of this chatserver
	 * @return
	 */
	public String getRoomName() throws RemoteException;
	
	/**
	 * Implements the room name generation to the register so that all implementation respect it
	 * @param roomName - the name of the room
	 * @return the name used to register the room on the registry
	 */
	public static String registeredRoomName(String roomName) throws RemoteException{
		return "room_" + roomName;
	}

}

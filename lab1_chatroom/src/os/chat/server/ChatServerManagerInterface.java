package os.chat.server;

import java.rmi.Remote;
import java.util.Vector;

/**
 * This interface is the set of commands that can be called remotely for the
 * {@link ChatServerManager}:
 * <p>
 * for the first part:
 * <ul>
 * <li> getting the list of rooms
 * </ul>
 * <p>
 * for the second part:
 * <ul>
 * <li> creating new rooms
 * </ul>
 * <p>
 * (deletion of rooms not required)
 */
public interface ChatServerManagerInterface extends Remote {
	//The manager is a singleton anyway, independently of the implementation used. The registry name
	//shall remain the same.
	public static final String SERVER_MANAGER_REGISTRY_NAME = "ChatServerManager";
	
        /**
	 * Getter method for list of available chat rooms.
	 * @return  the list of available chat rooms
	 * @see Vector
	 */
	public Vector<String> getRoomsList();
	
        /**
	 * Creates a new chat room with a specified room name <code>roomName</code>.
	 * @param roomName the name of the chat room
	 * @return <code>true</code> if the new chat room was successfully
	 * created, <code>false</code> otherwise.
	 */
	public boolean createRoom(String roomName);
	
}

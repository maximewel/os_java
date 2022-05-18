import java.util.Vector;

public class World {

	// You may tune these values according to your needs
	// Best to keep NB_STANDS at 6 for now.
	private static final int NB_STANDS = 6 ;
	private static final int NB_USERS = 100 ;
	private static final int STAND_CAPACITY = 10 ;
	private static final int STAND_INITIAL_BIKES = 10 ;
	private static final int TRUCK_CAPACITY = 50 ;
	private static final int TRUCK_INITIAL_BIKES = 3 ;
	
	private static Vector<Stand> stands;
	private static Vector<User> users;
		
	public World() {
		System.err.println("The World class is not supposed to be instantiated. Exiting.");
		System.exit(1);
	}
	
	public static Vector<Stand> getStands() {
		return stands;
	}
	
	public static void main(String[] args) {
		// create stands
		System.out.println("World creates "+NB_STANDS+" stands");
		stands = new Vector<Stand>();
		for (int i=0;i<NB_STANDS;i++){
			stands.add(new Stand(i, STAND_CAPACITY, STAND_INITIAL_BIKES));
		}
		
		// create the truck
		System.out.println("World creates truck");
		Truck t = new Truck(TRUCK_INITIAL_BIKES, TRUCK_CAPACITY);
		
		// create the users	
		System.out.println("World creates " + NB_USERS + " users");
		users = new Vector<User>();
		for (int i=0; i<NB_USERS; i++) {
			User u = new User(i);
			users.add(u);
		}
		
		// start the truck and the users as independent threads.
		// the program must stop when all users have finished their travels
		/* the truck needs to stop working when all users have finished their travels
		 * (they wait for users to join)
		 */
		System.out.println("Starting users and truck");
		for (User user : users) {
			user.start();
		}
		t.start();
		
		for (User user : users) {
			try {
				user.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Every user has finished - Stopping truck");
		t.gracefullStop();
		System.out.println("Simulation finished.");
	}
}

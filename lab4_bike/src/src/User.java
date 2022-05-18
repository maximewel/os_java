import java.util.concurrent.ThreadLocalRandom;

public class User extends Thread {
	//Extend thread - This could be a runnable, but as we manipulate a list of users (Vector<User>) in the world,
	//Having them as threads is way more convenient. 
	//ex: joining users directly and not storing Thread variables containing the user
	
	private static final int MIN_RUNS = 3 ;
	private static final int MAX_RUNS = 8 ;
	private static final int TIME_FOR_ONE_HOP = 50 ; // in milliseconds
	
	private static final int MIN_WAIT_TIME = 100 ; // in milliseconds
	private static final int MAX_WAIT_TIME = 200 ; // in milliseconds

		
	private static final int[] startStands = {0,0,0,0,0,1,1,1,2,2,2,3,3,4,4,5}; 
	private static final int[] arrivalStands = {0,1,1,2,2,2,3,3,3,4,4,4,5,5,5,5};  
	
	/**
	 * Get a random starting or arrival stand. The parameter is an array (either startStands or arrivalStands).
	 * The int returned is the index of the stand in the static Vector of stands defined in the World class.
	 * This method allows to bias the selection of the start and arrival stands.
	 * @param source An array with indexes of the stand. More instances of an index increase the probability for it to be picked.
	 * @return the index of the stand, randomly selected in the source array.
	 */
	private int getRandStand (int[] source) {
		return source[(int)(source.length * ThreadLocalRandom.current().nextDouble())];
	}
	
	/**
	 * Selects a number of runs between MIN_RUNS and MAX_RUNS
	 * @return the number of runs.
	 */
	private int getNbRuns () {
		return MIN_RUNS + (int)Math.floor(ThreadLocalRandom.current().nextDouble() * (MAX_RUNS - MIN_RUNS));
	}
	
	/**
	 * Returns the minimal number of hops (distance between two stands on the ring) for a travel between source and destination.
	 * @param source The source stand.
	 * @param destination The destination stand.
	 * @return The minimal number of hops.
	 */
	private int getMinNbHops (int source, int destination) {
		int nbHops = 0;
		int clockWise, counterClockWise;
		if (source > destination) {
			clockWise = destination - source ;
			counterClockWise = World.getStands().size() - destination + source ;
		} else {
			clockWise = World.getStands().size() - source + destination ;
			counterClockWise = source - destination ;
		}
		nbHops = Math.min(clockWise,counterClockWise);
		return nbHops;
	}
	
	private final int id ;
	
	public User(int id) {
		this.id = id ;
	}
	
	@Override
	public void run() {
		// decide on number of runs
		int nbRuns = getNbRuns();
		System.out.println("User " + id + " will make " + nbRuns +" travels.");
		
		for (int r = 0 ; r < nbRuns ; r++) {
			// choose start/stop
			int start = getRandStand(startStands);
			Stand startStand = World.getStands().get(start);
			int stop = getRandStand(arrivalStands);
			Stand stopStand = World.getStands().get(stop);
			
			System.out.println("Thread " + Thread.currentThread().getId() + " - travel " + (1+r) + "/" + nbRuns);
			
			// calculate the number of hops
			int nbHops = getMinNbHops(start, stop);
			
			// implement the travel between stand startStand and stand stopStand
			// Remember that there are traveling times (TIME_BETWEEN_SITES)
			
			//Take bike at start
			startStand.getBike();
			
			//Travel
			int travelTime = Math.abs(nbHops) * TIME_FOR_ONE_HOP;
			try {
				sleep(travelTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Arrive at stand
			stopStand.returnBike();
			
			//From subject - [Users] wait for 100 ms to 200 ms between each trip
			try {
				//All calls to random in the threaded classes have been replaced by threadlocalrandom, which allocates
				//one Rand object per thread, avoiding the global lock that exists on the usual Random objects.
				int timeBetweenTravels = ThreadLocalRandom.current().nextInt(MIN_WAIT_TIME, MAX_WAIT_TIME+1);
				sleep(timeBetweenTravels);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("User " + this.getId() + " finished");
	}
}

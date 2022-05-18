package lab3_sync.q3;
/*
 * Operating Systems - Universite de Neuchatel
 * 
 * Practical #4: an introduction to threads and synchronization in Java
 * 
 * Do not forget to indicate with comments inside the code the 
 * modifications you have made and what problems they fix or 
 * prevent, with references to the questions of the subject (Q1, Q2, etc.)
 */

/**
 * Objects instances of Kitchen represent a kitchen with initially two stoves and 
 * two stocks: initial stock of 16 food and empty final stock. Stoves are used to
 * prepare from the former to the latter.
 */
class Kitchen {
	//Constants, we hate magic numbers
	final int KITCHEN_CAPACITY = 16;
	
	/**
	 * Stock of food to prepare
	 */
    Stock stockInput = new Stock("input", KITCHEN_CAPACITY);
    /**
     * Stock of final (prepared) food
     */
    Stock stockOutput = new Stock("output", 0);
    /**
     * Stoves for the preparations
     */
    Stove stove1 = new Stove(stockInput, stockOutput, KITCHEN_CAPACITY/2);
    Stove stove2 = new Stove(stockInput, stockOutput, KITCHEN_CAPACITY/2);
    
    /**
     * Main entry point: proceed to operate the kitchen work of preparation
     */
    public void work() {
    	/*
    	 * We added a simple output in the prepare function, after the first 'get' method. We consistanly have such resuluts:
    	 * 		Thread-0: passed first 'get' critical section. stock input contains 14 food.
		 *		Thread-1: passed first 'get' critical section. stock input contains 14 food.
		 *		Thread-0: passed first 'get' critical section. stock input contains 13 food.
		 *		Thread-1: passed first 'get' critical section. stock input contains 12 food.
		 *		Thread-0: passed first 'get' critical section. stock input contains 11 food.
		 *		Thread-1: passed first 'get' critical section. stock input contains 10 food.
		 *		Thread-0: passed first 'get' critical section. stock input contains 9 food.
		 *		Thread-1: passed first 'get' critical section. stock input contains 8 food.
		 *		Thread-0: passed first 'get' critical section. stock input contains 7 food.
		 *		Thread-1: passed first 'get' critical section. stock input contains 6 food.
		 *		Thread-0: passed first 'get' critical section. stock input contains 5 food.
		 *		Thread-1: passed first 'get' critical section. stock input contains 4 food.
		 *		Thread-0: passed first 'get' critical section. stock input contains 3 food.
		 *		Thread-1: passed first 'get' critical section. stock input contains 2 food.
		 *		Thread-0: passed first 'get' critical section. stock input contains 1 food.
		 *		Thread-1: passed first 'get' critical section. stock input contains 0 food.
		 *		Stove prepared [8/8] food from imput stockinput into output stock output
		 *		Stove prepared [8/8] food from imput stockinput into output stock output
		 *		The stock input contains 0 food.
		 *		The stock output contains 16 food.
		 *	
		 *	Of course, it is a bit light to make any certain assertion about the scheduler. But the regularity of the 
		 *	Thread CPU time seems to strongly imply a round-robin scheduler. 
		 *		- A FCFS is very improbable as this is consistantly 1-2-1-2 and we would have some 1-1-1, 2-2, etc... 
		 *		- A shortest-job could produce a similar result, given they have the same exec time so there
		 *  	is no reason to favor any of them. but we would probably have some 1-1 or 2-2 at the start before the scheduler
		 *  	Know their average exec time. The start is as regular as the end there, so RR is the most probable.
    	 */
    	System.out.println("Starting kitchen work ...");
    	long initialTime = System.currentTimeMillis();
    	//Here we simply start the thread. This will create a thread and execute the overriden run function inside the new instance
   		stove1.start();
   		stove2.start();
   		try {
			stove1.join();
	   		stove2.join();
		} catch (InterruptedException e) {
			System.err.println("Interrupt error while waiting on the stove(s)");
			e.printStackTrace();
			return;
		}
   		System.out.println(stove1);
   		System.out.println(stove2);
   		stockInput.display();
   		stockOutput.display();
   		System.out.println("... done ("+((double)(System.currentTimeMillis() - initialTime)/1000)+" second(s))");

    }
    
    /**
     * Entry point for the whole program
     * @param args not used
     */
    public static void main(String[] args) {
    	new Kitchen().work();
    }
}

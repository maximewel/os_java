package lab3_sync.q5;
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
     * Added: Transition stock between the two specilalized stoves
     */
    Stock stockIntermediate = new Stock("intermediate", 0);
    /**
     * Stock of final (prepared) food
     */
    Stock stockOutput = new Stock("output", 0);
    /**
     * Stoves for the preparations
     */
    
    // Input stock --> Stove1 --> Intermediate stock --> Stove2 --> Output stock
    Stove stove1 = new Stove(stockInput, stockIntermediate, KITCHEN_CAPACITY);
    Stove stove2 = new Stove(stockIntermediate, stockOutput, KITCHEN_CAPACITY);
    
    /**
     * Main entry point: proceed to operate the kitchen work of preparation
     */
    public void work() {
    	/*
    	 * We added a wat/notify mecanism on the new producer-consumer stock. If there is nothing left, the thread should wait, and
    	 * when adding a new food, a notification is sent. When executed again:
			Starting kitchen work ...
			Thread Thread-1 waiting for food on stock intermediate
			Thread-0: passed first 'get' critical section. stock input contains 15 food.
			Thread-0: passed first 'get' critical section. stock input contains 14 food.
			Thread Thread-1 notified on stock intermediate
			Thread-1: passed first 'get' critical section. stock intermediate contains 0 food.
			Thread Thread-1 waiting for food on stock intermediate
			Thread Thread-1 notified on stock intermediate
			Thread-1: passed first 'get' critical section. stock intermediate contains 0 food.
			Thread-0: passed first 'get' critical section. stock input contains 13 food.
			Thread Thread-1 waiting for food on stock intermediate
			Thread Thread-1 notified on stock intermediate
			(...)
			Thread-1: passed first 'get' critical section. stock intermediate contains 0 food.
			Thread Thread-1 waiting for food on stock intermediate
			Thread Thread-1 notified on stock intermediate
			Thread-1: passed first 'get' critical section. stock intermediate contains 0 food.
			Stove prepared [16/16] food from imput stockinput into output stock intermediate
			Stove prepared [16/16] food from imput stockintermediate into output stock output
			The stock input contains 0 food.
			The stock output contains 16 food.
			... done (1.101 second(s))
		 *
		 * Now we see that the stock never reaches -1, and that the second stove waits for the first stove to add a food before processing
		 * it. The notify was enough, notifyall is not used.
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

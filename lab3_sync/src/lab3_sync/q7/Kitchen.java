package lab3_sync.q7;
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
	final int KITCHEN_CAPACITY = 10000;
	final int JIT_STOCK_CAPACITY = 1;
	
	/**
	 * Stock of food to prepare
	 */
    Stock stockInput = new Stock("input", KITCHEN_CAPACITY, KITCHEN_CAPACITY);
    /**
     * Added: Transition stock between the two specilalized stoves
     */
    Stock stockIntermediate = new Stock("intermediate", 0, JIT_STOCK_CAPACITY); //Added: just in time capacity
    /**
     * Stock of final (prepared) food
     */
    Stock stockOutput = new Stock("output", 0, KITCHEN_CAPACITY);
    /**
     * Stoves for the preparations
     */
    
    // Input stock --> Stove1/Stove2 --> Intermediate stock --> Stove3/Stove4 --> Output stock
    Stove stove1 = new Stove(stockInput, stockIntermediate, KITCHEN_CAPACITY/2);
    // New: stove 1/2 are doing the same half of the  parallelized work
    Stove stove2 = new Stove(stockInput, stockIntermediate, KITCHEN_CAPACITY/2);
    Stove stove3 = new Stove(stockIntermediate, stockOutput, KITCHEN_CAPACITY/2);
    Stove stove4 = new Stove(stockIntermediate, stockOutput, KITCHEN_CAPACITY/2);
    
    /**
     * Main entry point: proceed to operate the kitchen work of preparation
     */
    public void work() {
    	/*
    	 * At first: We have a deadlock problem because a stove that is getting food sends a notification at the end to
    	 * alert that a food has been taken in the hope that a consummer is waiting to put one. With the simple notify, we quickly run
    	 * into a consummer alerting another consummer, with the producers all blocked on the queue:
			Thread Thread-1 waiting to put food on stock intermediate
			Thread Thread-0 waiting to put food on stock intermediate
			Thread Thread-3 notified on stock intermediate
			Thread Thread-3 waiting for food on stock intermediate
			Thread Thread-1 notified on stock intermediate
			Thread Thread-1 waiting to put food on stock intermediate
			Thread Thread-0 notified on stock intermediate
			Thread Thread-0 waiting to put food on stock intermediate
			Thread Thread-2 notified on stock intermediate
			Thread Thread-2 waiting for food on stock intermediate
			Thread Thread-3 notified on stock intermediate
			Thread Thread-3 waiting for food on stock intermediate
			
		* A simple solution is to change the notify to notifyAll so that every waiting thread is woken up.
			Thread Thread-3 notified on stock intermediate
			Thread Thread-3 waiting for food on stock intermediate
			Thread Thread-1 notified on stock intermediate
			Thread Thread-1 waiting to put food on stock intermediate
			Thread Thread-3 notified on stock intermediate
			Thread Thread-3 waiting for food on stock intermediate
			Thread Thread-1 notified on stock intermediate
			Thread Thread-3 notified on stock intermediate
			Stove prepared [5000/5000] food from imput stockinput into output stock intermediate
			Stove prepared [5000/5000] food from imput stockinput into output stock intermediate
			Stove prepared [5000/5000] food from imput stockintermediate into output stock output
			Stove prepared [5000/5000] food from imput stockintermediate into output stock output
			The stock input contains [0/10000] food.
			The stock intermediate contains [0/1] food.
			The stock output contains [10000/10000] food.
			... done (0.496 second(s))
			
		* Although it works, I think we could also go with two synhcronization queues. The consummer would alert the producers, and vice-versa. In an 
		* hypothetical situation with 5000 producers/consummers, it would gain us time. In our case, we could even go back to the single
		* notification method.
		*/
    	System.out.println("Starting kitchen work ...");
    	long initialTime = System.currentTimeMillis();
    	//Here we simply start the thread. This will create a thread and execute the overriden run function inside the new instance
   		stove3.start();
   		stove4.start();
   		//Launch stove1/2 last
   		stove1.start();
   		stove2.start();
   		try {
			stove1.join();
	   		stove2.join();
	   		//start stove 3 too
	   		stove3.join();
	   		stove4.join();
		} catch (InterruptedException e) {
			System.err.println("Interrupt error while waiting on the stove(s)");
			e.printStackTrace();
			return;
		}
   		System.out.println(stove1);
   		System.out.println(stove2);
   		System.out.println(stove3);
   		System.out.println(stove4);
   		stockInput.display();
   		stockIntermediate.display();
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

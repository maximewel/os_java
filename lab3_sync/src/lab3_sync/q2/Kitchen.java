package lab3_sync.q2;
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
	final int KITCHEN_CAPACITY = 100000000;
	
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
    	 * If you just start if without touching at the stock method, you run into the inc/dec problem that was starting to be in Q1
    	 * Example of a result :
    	 * 	Stove prepared [50000000/50000000] food from imput stockinput into output stock output
		 * 	Stove prepared [50000000/50000000] food from imput stockinput into output stock output
		 * 	The stock input contains 28404855 food.
		 * 	The stock output contains 71997002 food.
		 * To solve that, we go to the stock and implement the synchronized access there because the ++/-- are not atomic operations
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
   		
   		/*
   		 * Now that we have changed the stock, here is an example of result:
   		 * 	Stove prepared [50000000/50000000] food from imput stockinput into output stock output
		 * 	Stove prepared [50000000/50000000] food from imput stockinput into output stock output
		 * 	The stock input contains 0 food.
		 * 	The stock output contains 100000000 food.
		 * 	... done (3.228 second(s))
		 * We have consistant parallell operations now. 
		 * Of course, the synchronized blocks means that there will be waiting time and lock acquire/release overhead
		 * That means the computation time is much slower - but it is expected.
   		 */
    }
    
    /**
     * Entry point for the whole program
     * @param args not used
     */
    public static void main(String[] args) {
    	new Kitchen().work();
    }
}

package lab3_sync.q1;
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
	/**
	 * Stock of food to prepare
	 */
    Stock stockInput = new Stock("input", 16);
    /**
     * Stock of final (prepared) food
     */
    Stock stockOutput = new Stock("output", 0);
    /**
     * Stoves for the preparations
     */
    Stove stove1 = new Stove(stockInput, stockOutput, 8);
    Stove stove2 = new Stove(stockInput, stockOutput, 8);
    
    /**
     * Main entry point: proceed to operate the kitchen work of preparation
     */
    public void work() {
    	System.out.println("Starting kitchen work ...");
    	long initialTime = System.currentTimeMillis();
    	//Here we simply start the thread. This will create a thread and execute the overriden run function inside the new instance
   		stove1.start();
   		stove2.start();
   		//Of course, if we immediatly display the result, we will most likely have nothing done. We need
   		//to wait the end of the execution of the work
   		try {
			stove1.join();
	   		stove2.join();
		} catch (InterruptedException e) {
			System.err.println("Interrupt error while waiting on the stove(s)");
			e.printStackTrace();
			return;
		}
   		//Now at Q1 we are mostly good (We wait the end of execution)
   		//The problem is on the synchronization of the increment of the stock. Here we verify with a toStr method that we
   		//did the work intended:
   		System.out.println(stove1);
   		System.out.println(stove2);
   		//But as the shared ressource is not protected, it is possile that not 100% of food is prepared even though we prepare 100%,
   		//specifically because some are not incremented/decremented properly.
   		stockInput.display();
   		stockOutput.display();
   		
   		//Example can be: 2stoves at 8/8 command, but still 1 in input and only 14 as output. We will see the solution for sync later,
   		//Q1 says not to worry about it yet.
   		
   		System.out.println("... done ("+((double)(System.currentTimeMillis() - initialTime)/1000)+" second(s))");
   		//The computation time is indeed close t ~500ms, which is expected (about half the original with 1 thread)
    }
    
    /**
     * Entry point for the whole program
     * @param args not used
     */
    public static void main(String[] args) {
    	new Kitchen().work();
    }
}

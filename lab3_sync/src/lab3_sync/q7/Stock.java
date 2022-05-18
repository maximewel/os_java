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
 * Objects of class Stock represent a set of food. Food is not effectively stored,
 * only a counter is used to represent how much food is available.
 * 
 * It could be possible to use a more realistic queue (FIFO) for the Stock representation.
 * This is left as an exercise for home work. *
 */
class Stock {
	/**
	 * Amount of food
	 */
    private int foodAmount;
    
    /**
     * Added: max food limit
     */
    private int maxFood;
    
    /**
     * Name of the stock
     */
    private String name;

	/**
     * Creates a new Stock object
     * @param name its name
     * @param nbFood initial number of food
     */
    public Stock(String name, int nbFood, int maxFood) {
        this.foodAmount = nbFood;
        this.maxFood = maxFood;
        this.name = name;		
    }

    /**
     * Adds food
     * Add synchronization such as this ++ non-atomic operation is never accessed in parallel by anyome
     */
    public synchronized void put() {
    	//Now, there needs to be a limit on the put - it can't go beyond the max. Of course, there needs to be a notification 
    	//added when a food is taken from the stock on the get function
    	while(foodAmount >= maxFood) {
    		try {
    			System.out.println("Thread " + Thread.currentThread().getName() + " waiting to put food on stock " + this.name);
				wait();
    			System.out.println("Thread " + Thread.currentThread().getName() + " notified on stock " + this.name);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
        foodAmount++;
        //Added: Notify all so that we are sure that if a consummer is waiting, it is woken up
        notifyAll();
        
    }

    /**
     * Removes (takes) food
     * Add synchronization such as this ++ non-atomic operation is never accessed in parallel by anyome
     */
    public synchronized void get() {
    	//If there is no food, don't allow to take one - wait on a producer to add it
    	while(foodAmount <= 0) {
    		try {
    			System.out.println("Thread " + Thread.currentThread().getName() + " waiting for food on stock " + this.name);
				wait();
    			System.out.println("Thread " + Thread.currentThread().getName() + " notified on stock " + this.name);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
        foodAmount--;
        //Added: Notify all so that we are sure that if a producer is waiting, it is woken up
        notifyAll();
    }

    /**
     * Display the stock status
     */
    public void display() {
        System.out.println("The stock " + name + " contains [" + foodAmount + "/" + maxFood + "] food.");
    }

    //Added
    public String getName() {
		return name;
	}
    
    //added
	public int getNbFood() {
		return foodAmount;
	}
}

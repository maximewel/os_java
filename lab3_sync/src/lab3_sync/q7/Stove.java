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
 * Objects that are instances of the Stove class represent stoves to prepare
 * food. The principle is as follows: the call to prepare() picks an element
 * from Stock A, waits for 64 ms, and puts an element to stock B. The work() method
 * runs nbPrepare times the prepare() method, nbPrepare being set by the constructor.
 */
class Stove extends Thread{
	//1: We extend thread, and to use that we need a run function override
	
	/**
	 * The initial stock
	 */
    private Stock A;
    /**
     * The stock where to put prepared food
     */
    private Stock B;
    /** 
     * Number of preparations when calling work().
     */
    private int toPrepare;
    private int prepared;

    /**
     * Constructs an instance of Stove
     * @param A Initial stock
     * @param B Destination stock
     * @param nbPrepare How many preparations should be made
     */
    public Stove(Stock A, Stock B, int nbPrepare) {
        this.A = A;
        this.B = B;
        this.toPrepare = nbPrepare;
    }

    /**
     * Proceeds to a single preparation from food of Stock A to food of Stock B
     */
    public void prepare() {
    	//instant preparation
        A.get();
        B.put();
    }

    /**
     * Proceeds to nbPrepare preparations
     */
    @Override
    public void run() {
        // Now the work method is inside the run method of the thread. This is executed at the start of the thread
        for(prepared = 0; prepared < toPrepare; prepared++) {
            prepare();
        }
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Stove prepared [").append(prepared).append("/").append(toPrepare).append("] food from imput stock")
		.append(A.getName()).append(" into output stock ").append(B.getName());
		return builder.toString();
	}
    
    
}

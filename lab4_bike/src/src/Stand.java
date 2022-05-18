public class Stand {
	private int id;
	private int availableBikes;
	private int capacity;

	public Stand(int id, int capacity, int availableBikes) {
		this.id = id;
		this.capacity = capacity;
		this.availableBikes = availableBikes;
	}

	public int getId() {
		return id;
	}

	// Read-only methods, safe for multi-threaded access
	public int getAvailableBikes() {
		return availableBikes;
	}

	public int getFreeSlots() {
		return capacity - availableBikes;
	}

	public int getCapacity() {
		return capacity;
	}

	// Non-safe in case of multithreaded access. Priority mecanism
	synchronized public void getBike () {
		//Wait in line - get ticket, and keep waiting until it is the current ticket and there is enough bikes
		int waitingTicket = this.getPickLineTicket();
		while(!(isFirstInPickLine(waitingTicket) && availableBikes > 0)) {
			try {
				System.out.println("Thread " + Thread.currentThread().getId() + ", stand " + this.id + " waiting in pick line");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Thread " + Thread.currentThread().getId() + ", stand " + this.id + ", ticket " + waitingTicket + " out of pick line");
		this.usePickLineTicket();
		
		availableBikes = availableBikes-1;
		System.out.println("Stand " + id + " | -1 bike | " + availableBikes + "/" + capacity);
		
		//Cascading signal - Notify in case another bike is available
		this.notifyAll();
	}

	synchronized public void getBikes(int nbBikes) {
		//Entry point for trucks, this function is sync but does not particpiate in the ticket scheme. It takes the
		//next available lock and goes straight to operations. Thus, It should not wait more than one user execution.
		availableBikes = availableBikes - nbBikes;
		this.notifyAll();
	}

	synchronized public void returnBike() {
		//Wait in line - get ticket, and keep waiting until it is the current ticket and there is enough capacity to put the bike
		int waitingTicket = this.getReturnLineTicket();
		while(!(isFirstInReturnLine(waitingTicket) && (capacity > availableBikes))) {
			try {
				System.out.println("Thread " + Thread.currentThread().getId() + ", stand " + this.id + " waiting in return line");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Thread " + Thread.currentThread().getId() + ", stand " + this.id + ", ticket " + waitingTicket + " out of return line");
		this.useReturnLineTicket();
		
		availableBikes = availableBikes + 1;
		System.out.println("Stand " + id + " | +1 bike | " + availableBikes + "/" + capacity);
		
		//cascading signal
		this.notifyAll();
	}

	synchronized public void returnBikes(int nbBikes) {
		//Entry point for trucks, this function is sync but does not participate in the ticket scheme. It takes the
		//next available lock and goes straight to operations. Thus, It should not wait more than one user execution.
		availableBikes = availableBikes + nbBikes ;
		this.notifyAll();	
		}

	/*
	 * We implement a line system for taking a bike and for returning a used bike.
	 * That way, a traveler can properly wait in line to do his task.
	 *
	 * Yet, remember a stand can be used to do at most one task, e.g.
	 * myStartingStand.returnBike() or myStartingStand.getBike() at a time by one
	 * user. A ticket system like at the post office is good for FIFO!
	 */
	
	/**
	 * VERY IMPORTANT:
	 * For both truck and users - Java native object locks, which are used when marking a block or a function sync,
	 * are re-entrant. This means that if obj.A has the lock and ask it again, it is granted because it is the same object.
	 * We can therefore be very generous on marking sync functions/blocks, because an object will never deadlock itself with itself.
	 * 
	 * If the locks were not re-entrant, here, a user having the lock on the obj couldn't take it to have/redeem his ticket
	 * Without re-entrant lock, we would have to be way more parcimonious with the sync marker (one at entrypoint only)
	 * Marking functions sync that are only called by an entrypoint that is also sync might seem stupid, but there is no
	 * guarantee that a third class will not use the stand for the bikes i nthe futur (eg: reparator). If the functions are sync, then
	 * the implementation/sync mecanism is still protected. If they are not, then the mecanism must be enforced on any new implementation
	 * that uses the stands, and this is more difficult to control/enforce.
	 * 
	 * That's all for my rant :-)
	 */

	// tickets for waiting in line to pick
	private int waitingToPickLine = 0;
	private int currentToPickLine = 0;

	synchronized public boolean isFirstInPickLine (int ticket) {
		return (currentToPickLine == ticket);
	}

	synchronized public int getPickLineTicket () {
		int ret = waitingToPickLine;
		waitingToPickLine++; // prepare the next ticket
		return ret;
	}

	synchronized public void usePickLineTicket () {
		currentToPickLine++;
	}

	// tickets for waiting in line to return
	private int waitingToReturnLine = 0;
	private int currentToReturnLine = 0;

	synchronized public boolean isFirstInReturnLine (int ticket) {
		return (currentToReturnLine == ticket);
	}

	synchronized public int getReturnLineTicket () {
		int ret = waitingToReturnLine;
		waitingToReturnLine++; // prepare the ticket for the next one
		return ret;
	}

	synchronized public void useReturnLineTicket () {
		currentToReturnLine++;
	}
	
	//Added to help get a displayable state of this stand
	public String state() {
		StringBuilder sb = new StringBuilder("Stand ").append(id)
				.append(" waiting get: ").append(waitingToPickLine - currentToPickLine)
				.append(" waiting return: ").append(waitingToReturnLine - currentToReturnLine);
		return sb.toString();
	}
}

public class Truck extends Thread {		
	private static final int TIME_BETWEEN_SITES = 25 ; // in milliseconds
	
	private final int capacity ;
	private int bikes ;
	
	public Truck(int bikes, int capacity) {
		this.bikes = bikes;
		this.capacity = capacity;
	}
	
	public Truck(int capacity) {
		this.bikes = 0;
		this.capacity = capacity;
	}
	
	//Added: Always prefer gracefull stop, the stop method for the threads is deprecated because of unknown behavior
	//We simply loop on the gracefull stop variable. Any thread can ask the truck to stop, and the truck has a control point
	//at every point of the loop that makes it stop then asked.
	private boolean gracefulStop = false;
	public void gracefullStop() {
		this.gracefulStop = true;
	}
	
	@Override
	public void run() {
		// circle around sites
		//First control point, although we want the truck to finish the stand and leave, so not sufficient
		while (!gracefulStop) {
			System.out.println("Start of truck round");
			for (Stand stand : World.getStands()) {
				//Second gracefull control point, before every stand
				if(gracefulStop) {
					break;
				}
				// TODO here, equilibrate the number of bikes on the stand.
				// Remember that there are traveling times (TIME_BETWEEN_SITES)
				
				//Equilibrate
				synchronized (stand) {
					StringBuilder sb = new StringBuilder("Truck with ").append(this.bikes).append(" bikes")
							.append(" balancing stand ").append(stand.getId())
							.append(" with ").append(stand.getAvailableBikes()).append("/").append(stand.getCapacity()).append(" bikes");

					int bikeCount = stand.getAvailableBikes();
					int halfCapacity = stand.getCapacity() / 2;
					int targetBikeBalancing = halfCapacity - bikeCount;
					if (targetBikeBalancing > 0) {
						//Positive: There is less bike than halfCapa, we want to replenish the stand as much as we can untill half capacity
						int replenishmentAmount = (targetBikeBalancing > this.bikes ? this.bikes : targetBikeBalancing);
						stand.returnBikes(replenishmentAmount);
						this.bikes -= replenishmentAmount;
						sb.append(" - Replenishing ").append(replenishmentAmount).append(" bikes");
					} else if (targetBikeBalancing < 0) {
						//Negative: These is more bike than halfcapa, we want to diminish the bike count untill half capacity
						targetBikeBalancing = Math.abs(targetBikeBalancing);
						int emptyAmount = (targetBikeBalancing + this.bikes > this.capacity ? this.capacity - this.bikes : targetBikeBalancing);
						stand.getBikes(emptyAmount);
						this.bikes += emptyAmount;
						sb.append(" - Emptying ").append(emptyAmount).append(" bikes");
					} else {
						sb.append(" - Perfect balance on stand, nothing to be done");
					}
					sb.append("\n").append(stand.state());
					System.out.println(sb);
				}
				
				//Travel time
				try {
					sleep(TIME_BETWEEN_SITES);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}


import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Station {
	ThemePark park;
	
	StationID id;
	Station nextStation;
	double distanceToNextDestination;
	int BICYCLES_MAX, DOCKS_MAX;
	int bicyclesAvailable, docksAvailable;

	int numberOfTourists;
	ConcurrentLinkedQueue<Tourist> rentQueue;
	ConcurrentLinkedQueue<Tourist> dropQueue;

	Station(ThemePark park, StationID id, double distance, int noBicycles, int noDocks) {
		this.park = park;
		
		this.id = id;
		
		this.BICYCLES_MAX = noBicycles;
		this.bicyclesAvailable = noBicycles;
		this.DOCKS_MAX = noDocks;
		this.docksAvailable = noDocks;
		
		this.distanceToNextDestination = distance;
		this.numberOfTourists = 0; // Total no of tourists arrives
		this.rentQueue = new ConcurrentLinkedQueue<Tourist>();
		this.dropQueue = new ConcurrentLinkedQueue<Tourist>();
	}

	boolean requestBicycle(Tourist tourist, double currentTime) {
		if(id == StationID.A) System.out.println("bicyclesAvailable: " + bicyclesAvailable);
		// To keep track no of tourist at each station
		this.numberOfTourists++;
		
		if( bicyclesAvailable > 0 && rentQueue.size() == 0){
			// Rent a bicycle
			rentOrDrop(true);
			
			// One dock will be available
			// So other tourist can drop the bicycle and ARRIVE at the current station
			if(dropQueue.size() > 0) {
				Tourist other = dropQueue.remove();
				other.endQueue(currentTime); // Conclude the waiting time
				
				park.addEvent(new Event(this, other, EventType.ENTER, currentTime));
			}
			return true;
		} else {
			// Add to rent queue
			rentQueue.add(tourist);
			
			// Update start time to queue
			tourist.startQueue(currentTime);
			return false;
		}
	}
	
	public boolean requestDock(Tourist tourist, double currentTime) {
		if(id == StationID.A) System.out.println("docksAvailable: " + docksAvailable);
		if( docksAvailable > 0 && dropQueue.size() == 0){
			// Drop the bicycle
			rentOrDrop(false);
			
			// One bicycle will be available
			// So other tourist can rent the bicycle, ride and DROP at the next station
			if(rentQueue.size() > 0) {
				Tourist other = rentQueue.remove();
				other.endQueue(currentTime); // Conclude the waiting time
				
				double travelTime = this.distanceToNextDestination / tourist.speed;
				park.addEvent(new Event(this.nextStation, other, EventType.DROP, currentTime + travelTime));
			}
			return true;
		} else {
			if(id == StationID.A) System.out.println("Add drop queue");
			// Add to drop queue
			dropQueue.add(tourist);
			
			// Update start time to queue
			tourist.startQueue(currentTime);
			return false;
		}
	}
	
	/*
	 * To avoid incrementing or decrementing at the same time
	 */
	synchronized void rentOrDrop(boolean rentOrDrop){
		if(rentOrDrop){
			rentBicycle();
		} else {
			dropBicycle();
		}
	}
	
	private void rentBicycle() {
		this.docksAvailable = Math.min(DOCKS_MAX, this.docksAvailable + 1);
		this.bicyclesAvailable--;
	}

	private void dropBicycle() {
		this.docksAvailable--;
		this.bicyclesAvailable = Math.min(BICYCLES_MAX, this.bicyclesAvailable + 1);
	}
	
	public String getRentQueue() {
		Iterator<Tourist> iter = rentQueue.iterator();
		String q = "[";
		while(iter.hasNext()) {
			q += iter.next().id + ",";
		}
		return q + " ] ";
	}
	
	public String getDropQueue() {
		Iterator<Tourist> iter = dropQueue.iterator();
		String q = "[";
		while(iter.hasNext()) {
			q += iter.next().id + ",";
		}
		return q + " ] ";
	}

}

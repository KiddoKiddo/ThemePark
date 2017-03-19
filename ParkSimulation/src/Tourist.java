

import java.util.ArrayList;
import java.util.List;

public class Tourist {
	int id;
	double speed;
	
	List<Double> waitingTime;
	double startTimeInQueue;
	
	ArrayList<StationID> stationsVisited;
	
	Tourist(int touristId){
		this.id = touristId;
		this.stationsVisited = new ArrayList<StationID>();
		this.speed = InputGenerator.generateSpeed();
		this.waitingTime = new ArrayList<Double>();
	}
	
	public void startQueue(double time) {
		startTimeInQueue = time;
	}
	
	public void endQueue(double time) {
		waitingTime.add(time - startTimeInQueue);
	}
}

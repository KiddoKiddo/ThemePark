import java.util.ArrayList;
import java.util.List;

/*
 * The main class of the simulation project. All the methods are called here.
 * 
 * Theme park contains the stations, an event queue and a constructor.
 * */

public class ThemePark extends Simulation{
	Station a, b, c, d;
	List<Tourist> tourists;

	ThemePark(int noBicycles, int noDocks) {
		
		a = new Station(this, StationID.A, 3.0, noBicycles, noDocks);
		b = new Station(this, StationID.B, 3.5, noBicycles, noDocks);
		c = new Station(this, StationID.C, 4.0, noBicycles, noDocks);
		d = new Station(this, StationID.D, 2.0, noBicycles, noDocks);
		
		a.nextStation = b;
		b.nextStation = c;
		c.nextStation = d;
		d.nextStation = a;
		
		tourists = new ArrayList<Tourist>();
	}
	/**
	 * Distance is in km
	 * Time is in minute
	 */
	public static void main(String[] args) {
		final double START_TIME = 0; 
		final double END_TIME = 1440; // 24 hours = 1440 mins
		
		final int NO_DOCKS = 10;
		final int NO_BICYCLES = 10;
		
		ThemePark park = new ThemePark(NO_BICYCLES, NO_DOCKS);
		park.addTourist(park.a, START_TIME + InputGenerator.generateNextArrival(StationID.A));
		park.addTourist(park.b, START_TIME + InputGenerator.generateNextArrival(StationID.B));
		park.addTourist(park.c, START_TIME + InputGenerator.generateNextArrival(StationID.C));
		park.addTourist(park.d, START_TIME + InputGenerator.generateNextArrival(StationID.D));
		park.run(START_TIME, END_TIME);
		
	}
	
	static public int nextTouristId = 0;
	public void addTourist(Station station, double arrivalTime) {
		Tourist tourist = new Tourist(nextTouristId++);
		addEvent(new Event(station, tourist, EventType.ENTER, arrivalTime));
	}

	public void removeTourist(Tourist tourist) {
		tourists.add(tourist);
	}
	
	public void display() {
		
	}
	
	public void displaySummary() {
		
	}
}

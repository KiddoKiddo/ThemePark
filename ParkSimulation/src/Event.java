public class Event implements Comparable<Event> {
	public Station station;
	public Tourist tourist;
	public EventType type;
	public double time; // absolute time

	public Event(Station station, Tourist tourist, EventType type, double eventTime) {
		this.station = station;
		this.tourist = tourist;
		this.type = type;
		this.time = eventTime;
	}

	public void process(Simulation sim) {
		ThemePark park = (ThemePark) sim;

		if (type == EventType.ENTER) {
			tourist.stationsVisited.add(station.id);
			if(tourist.stationsVisited.size() == 4){
				park.removeTourist(tourist); // End round
			} else {
				// Spend time in the station before RENT a bicycle
				double timespent = InputGenerator.generateTimeSpent(station.id);
				park.addEvent(new Event(station, tourist, EventType.RENT, time + timespent));
				
	
				// If this is the first first station tourist arrives
				// The schedule the next arrival for new tourist at the same station
				if (tourist.stationsVisited.size() == 1) {
					double nextArrivalTime = time + InputGenerator.generateNextArrival(station.id);
					park.addTourist(station, nextArrivalTime);
				}
			}
		} else if (type == EventType.RENT) {
			// Available bicycle?
			// YES: Schedule event to DROP at the next station
			if (station.requestBicycle(tourist, time)) {
				double travelTime = station.distanceToNextDestination / tourist.speed;
				park.addEvent(new Event(station.nextStation, tourist, EventType.DROP, time + travelTime));
			}
			// NO: Tourist waits in the renting queue of the station
			// Do nothing
		} else if (type == EventType.DROP) {
			// Available dock?
			// YES: Schedule event to ARRIVE at the current station
			if (station.requestDock(tourist, time)) {
				park.addEvent(new Event(station, tourist, EventType.ENTER, time));
			}
			// NO: Tourist waits in the dropping queue of the station
			// Do nothing
		}
	}
	
	@Override
	public int compareTo(Event other) {
		if (time < other.time) {
			return -1;
		} else if (time > other.time) {
			return 1;
		} else {
			return 0;
		}
	}
}

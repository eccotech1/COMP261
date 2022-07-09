package comp261.assig1;

import java.util.ArrayList;

public class Trip {
    private String tripId;
    private String stopId;
    ArrayList<String> stops = new ArrayList<>();
    int stop_sequence;

    public Trip (String tripId){
        this.tripId = tripId;
    }

public String getTripId(){
     return tripId;
 }

 public void addStops(String stopId) {
    stops.add(stopId);
 }

 public String getStopId(){
     return stopId;
 }

 public ArrayList<String> getStops(){
     return stops;
 }

//  public void setEdgeList(ArrayList<Edge> edges){
//      this.edges = edges;
//  }

public int getSequence(){
    return stop_sequence;
}
}

package comp261.assig1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

    //Todo add your data structures here
HashMap<String, Stop> stops;
HashMap<String, Trip> trips;
ArrayList<Edge> edges;
public Trie trie;
    
// constructor post parsing
    public Graph(HashMap<String, Stop> stops, HashMap<String, Trip> trips) {
        this.stops=stops;
        this.trips=trips;
        
    }

    // constructor with parsing
    public Graph(File stopFile, File tripFile) {
        //Todo: instantiate y
        //Todo: use the edge form the data in graph to draw the graphour data structures here
        stops = Parser.parseStops(stopFile);
        trips = Parser.parseTrips(tripFile);
        //build trie
        buildTrieData();
        edges = createEdge(); //create all the edges
     
       
    }
    /**
     * function to build trie
     */
    public void buildTrieData(){
        trie = new Trie();
        stops.values().forEach(stop ->{
            trie.add(stop);
        });
    }

    // Todo: getters and setters

    public HashMap<String, Stop> getStops(){
        return this.stops;
    }

    public ArrayList<Edge> createEdge(){
        ArrayList<Edge> e = new ArrayList<>();
            //run through the trips
        for(Trip t:trips.values()){          
            ArrayList<String> stop_List = t.getStops();
             for(int i = 0; i < stop_List.size()-1;i++){
                 Stop fromStop = stops.get(stop_List.get(i));
                 Stop toStop = stops.get(stop_List.get(i+1));
                 String tripId = t.getTripId(); 
                 if (fromStop != null && toStop != null && tripId != null){
                    e.add(new Edge(fromStop, toStop, tripId)); //add edge and append all values
                 }
             }   
        }
        return e;
    }


    public ArrayList<Edge> getEdge(){
        return edges;
    }

    public HashMap<String, Trip> getTrips() {
        return this.trips;
    }

}

package comp261.assig1;

// The edge class represents an edge in the graph.

public class Edge {
    private Stop fromStop;
    private Stop toStop;
    private String tripId;

    //todo: add a constructor
public Edge(Stop fromStop, Stop toStop, String tripID){
    this.fromStop = fromStop;
    this.toStop = toStop;
    this.tripId = tripID;
}

    //todo: add getters and setters

  public Stop fromStop(){
      return fromStop;
  }

  public Stop toStop(){
      return toStop;
  }

  public String gettripId(){
      return this.tripId;
  }


}

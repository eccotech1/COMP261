package comp261.assig1;

import java.util.ArrayList;


// decide the data structure for stops
public class Stop {
    //probably always have these three    
    private GisPoint loc;
    private String name;
    private String id;

    //Todo: add additional data structures
    

        
    // Constructor
    public Stop(GisPoint loc, String name, String id) {
        this.loc = loc;
        this.name = name;
        this.id = id;
    }

    // add getters and setters etc
    public GisPoint getLoc() {
        return loc;
    }

    public void setLoc(GisPoint loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public GisPoint getPoint(){
        return loc;
    }
        
    }

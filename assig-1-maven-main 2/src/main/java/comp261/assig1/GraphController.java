package comp261.assig1;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.*;

public class GraphController {

    // names from the items defined in the FXML file
    @FXML private TextField searchText; 
    @FXML private Button load;
    @FXML private Button quit;
    @FXML private Button up;
    @FXML private Button down;
    @FXML private Button left;
    @FXML private Button right;
    @FXML private Canvas mapCanvas;
    @FXML private Label nodeDisplay;
    @FXML private TextArea tripText;

    // These are use to map the nodes to the location on screen
    private Double scale = 5000.0; //5000 gives 1 pixel ~ 2 meter
    private static final double ratioLatLon = 0.73; // in Wellington ratio of latitude to longitude
    private GisPoint mapOrigin = new GisPoint(174.77, -41.3); // Lon Lat for Wellington
 
    private static int stopSize = 3; // drawing size of stops
    private static int moveDistance = 100; // 100 pixels
    private static double zoomFactor = 1.1; // zoom in/out factor

    private ArrayList<Stop> highlightNodes = new ArrayList<Stop>();
    private ArrayList<Trip> highlightTrips = new ArrayList<Trip>();

    // map model to screen using scale and origin
    private Point2D model2Screen (GisPoint modelPoint) {
        return new Point2D(model2ScreenX(modelPoint.lon), model2ScreenY(modelPoint.lat));
    }
    private double model2ScreenX (double modelLon) {
      return (modelLon - mapOrigin.lon) * (scale*ratioLatLon) + mapCanvas.getWidth()/2; 
    }
    // the getHeight at the start is to flip the Y axis for drawing as JavaFX draws from the top left with Y down.
    private double model2ScreenY (double modelLat) {
      return mapCanvas.getHeight()-((modelLat - mapOrigin.lat)* scale + mapCanvas.getHeight()/2);
    }

    // map screen to model using scale and origin
    private double getScreen2ModelX(Point2D screenPoint) {
        return (((screenPoint.getX()-mapCanvas.getWidth()/2)/(scale*ratioLatLon)) + mapOrigin.lon);
    }
    private double getScreen2ModelY(Point2D screenPoint) {
        return ((((mapCanvas.getHeight()-screenPoint.getY())-mapCanvas.getHeight()/2)/scale) + mapOrigin.lat);
    }
    
    private GisPoint getScreen2Model(Point2D screenPoint) {
        return new GisPoint(getScreen2ModelX(screenPoint), getScreen2ModelY(screenPoint));
    }   

    

    public void initialize() {
       // currently blank
    }

    /* handle the load button being pressed connected using FXML*/
    public void handleLoad(ActionEvent event) {
        Stage stage = (Stage) mapCanvas.getScene().getWindow();
        System.out.println("Handling event " + event.getEventType());
        FileChooser fileChooser = new FileChooser();
        //Set to user directory or go to default if cannot access

        
        File defaultNodePath = new File("data");
        if(!defaultNodePath.canRead()) {
            defaultNodePath = new File("C:/");
        }
        fileChooser.setInitialDirectory(defaultNodePath);
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extentionFilter);

        fileChooser.setTitle("Open Stop File");
        File stopFile = fileChooser.showOpenDialog(stage);

        fileChooser.setTitle("Open Stop Pattern File");
        File tripFile = fileChooser.showOpenDialog(stage);

        Main.graph=new Graph(stopFile,tripFile);
        drawGraph();
        event.consume(); // this prevents other handlers from being called
    }

    public void handleQuit(ActionEvent event) {
        System.out.println("Quitting with event " + event.getEventType()); 
        event.consume();  
        System.exit(0); 
    }

    public void handleZoomin(ActionEvent event) {
        System.out.println("Zoom in event " + event.getEventType()); 
// Todo: zoom in
        scale *= zoomFactor;
        drawGraph();
        event.consume();  
    }

    public void handleZoomout(ActionEvent event) {
        System.out.println("Zoom out event " + event.getEventType());
        scale *= 1.0/zoomFactor;
        drawGraph();
        event.consume();
    }

    public void handleUp(ActionEvent event) {
        System.out.println("Move up event " + event.getEventType());    
 // Todo: move up
        mapOrigin.add(0, moveDistance/scale);
        drawGraph();
        event.consume();  
    }

    public void handleDown(ActionEvent event) {
        System.out.println("Move Down event " + event.getEventType()); 
// Todo: move down
        mapOrigin.subtract(0, moveDistance/scale);
        drawGraph();
        event.consume();  
    }

    public void handleLeft(ActionEvent event) {
        System.out.println("Move Left event " + event.getEventType()); 
// Todo: move left
        mapOrigin.add(moveDistance/scale, 0);
        drawGraph();
        
        event.consume();  
    }

    public void handleRight(ActionEvent event) {
        System.out.println("Move Right event " + event.getEventType()); 
// Todo: move right
        mapOrigin.subtract(moveDistance/scale, 0);
        drawGraph();
        event.consume();  
    }

    public void mouseScroll(ScrollEvent event){
        System.out.println("Mouse Scroll event" + event.getEventType());
        scale += event.getDeltaY()*10;
        drawGraph();
        event.consume();
        
    }
    public void handleSearch(ActionEvent event) {
        System.out.println("Look up event " + event.getEventType()+ "  "+searchText.getText()); 
        String search = searchText.getText();
        //Core
 // Todo: figure out how to add searching and by text
 // This is were a Trie would be used potentially
//  HashMap<String, Stop> resultStop = Main.graph.trie.getAll(search); 
//  highlightNodes.clear();
//     if(resultStop != null){
//      String disp = "";
//     for(Stop stop : resultStop.values()){
//         highlightNodes.add(stop);
//         nodeDisplay.setText(stop.getName());
//         if(!disp.contains(stop.getName())){
//          disp = disp + stop.getName() + "\n";
//      }
//  }
//  tripText.setText(disp);
//  drawGraph();
//  GraphicsContext gc = mapCanvas.getGraphicsContext2D(); 
//  for(Trip trip : Main.graph.getTrips().values()){
//      ArrayList<String> stops = trip.getStops(); 
//      for(Stop stop : foundStops.values()){
//          if(stops.contains(stop.getId())){
//              gc.setStroke(Color.hsb(Math.random()*360,1,1)); 
//              drawTrip(trip, gc);
//          }
//      }
//  }
// }
//     else tripText.setText("Stop '" + search +"' not found");

//         event.consume();  
    }  

    public void handleSearchKey(KeyEvent event) {
        System.out.println("Look up event " + event.getEventType()+ "  "+searchText.getText()); 
        String search = searchText.getText().toLowerCase().trim();
        //clear
        highlightNodes.clear(); 
        highlightTrips.clear(); 
    // Todo: figure out how to add searching and by text after each keypress
    // This is were a Trie would be used potentially
        //check if the search is blank
    if(search.isBlank()) {
        tripText.setText(""); 
        highlightNodes.clear();
        highlightTrips.clear();
        drawGraph();
        return;
    } //hashmap of stops that has same prefix as in the map data
     HashMap<String, Stop> stopResult = Main.graph.trie.getAll(search);

        if(stopResult != null){ //check if it is not empty
         String disp = ""; 
        for(Stop stop : stopResult.values()){
            highlightNodes.add(stop);
            nodeDisplay.setText(stop.getName());
            if(!disp.contains(stop.getName())){
             disp = disp + stop.getName() + "\n";
         }
     }
     tripText.setText(disp);
     drawGraph();
     GraphicsContext gc = mapCanvas.getGraphicsContext2D(); 
     for(Trip trip : Main.graph.getTrips().values()){ //for each trip
         ArrayList<String> stops = trip.getStops(); 
         for(Stop stop : stopResult.values()){
             if(stops.contains(stop.getId())){
                 gc.setStroke(Color.hsb(Math.random()*360,1,1)); //random colour
                 drawTrip(trip, gc); //display the trip
             }
         }
     }
 }
        else tripText.setText("Stop '"+search+"' found"); //else print out this
        event.consume();  

    }  


/* handle mouse clicks on the canvas
   select the node closest to the click */
    public void handleMouseClick(MouseEvent event) {
        System.out.println("Mouse click event " + event.getEventType());
// Todo: find node closed to mouse click
// event.getX(), event.getY() are the mouse coordinates
       Point2D screenPoint = new Point2D(event.getX(), event.getY());
       double x = getScreen2ModelX(screenPoint);
       double y = getScreen2ModelY(screenPoint);
       highlightClosestStop(x,y);

        event.consume();
    }


    //find the Closest stop to the lon,lat postion
    public void highlightClosestStop(double lon, double lat) {
        double minDist = Double.MAX_VALUE;
        Stop closestStop = null;
//Todo: find closest stop and work out how to highlight it
//Todo: Work out highlighting the trips through this node

        Graph mg= Main.graph; 
        HashMap<String, Stop> stps = mg.getStops(); //hashmap of stops
        for(Stop s : stps.values()){ //run through the stops
            GisPoint p = s.getPoint(); //get coord
            double dist = p.distance(lon, lat); //appened the distance
            if(dist < minDist){ 
                minDist = dist;
                closestStop = s; //make closest stop to equal to stop found in the hashmap
            }

        }
        String text = ""; 
        if(closestStop != null){ //check if its not null
            highlightNodes.clear(); //clear
            highlightNodes.add(closestStop); //add the nearest stop
            nodeDisplay.setText(closestStop.getName());    //display the name of the closest stop     
            for (Trip t : Main.graph.getTrips().values()){
                if (t.getStops().contains(closestStop.getId())){
                    text += "Trip ID: " + t.getTripId()  + "  Stops: " +t.getStops() + "\n"; //display the stop and trips
                }   
            }
        
        tripText.setText(text); //set text 
        highlightTrips.clear(); //clear
        for(Trip tr : Main.graph.getTrips().values()){
            if(tr.getStops().contains(closestStop.getId())){
                highlightTrips.add(tr); //highlight connected trips to the given closest stop
            }
            }
        }
        drawGraph();
    }


    public void drawCircle(double x, double y, int radius) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.fillOval(x-radius, y-radius, radius*2, radius*2);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        mapCanvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
    }
    

    // This will draw the graph in the graphics context of the mapcanvas
    public void drawGraph() {
        Graph graph = Main.graph;
        if(graph == null) {
            return;
        }
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        // Todo:  store node list so we can use nodes to find edge end points.
        HashMap<String, Stop> stops = graph.getStops();
        // Todo: use the nodes form the data in graph to draw the graph
        // probably use something like this
        for(Stop stop : stops.values()){
            int size = stopSize;
            if(highlightNodes.contains(stop)){ //check if it contains the stop
                gc.setFill(Color.RED); //change the circle to red
                size = stopSize * 2; //double the size of the circle
            } else {
                gc.setFill(Color.BLUE); //else make it blue as default
            }
            Point2D screenPoint = model2Screen(stop.getPoint());
            drawCircle(screenPoint.getX(), screenPoint.getY(), size);
        }

        //draw edges

        for(Edge e : Main.graph.getEdge()){ //run through the egdes
            gc.setStroke(Color.BLACK); //make the edge black colour
            gc.setLineWidth(1);           
             //Todo: step through the edges and draw them with something like
                    Point2D startPoint = model2Screen(e.fromStop().getLoc());
                    Point2D endPoint = model2Screen(e.toStop().getLoc());
                    drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        }
       // drawTrip(trip, gc, color);
       for (Trip t : highlightTrips) {
           drawTrip(t, gc); //draw the trips
       }
    }



    private void drawTrip(Trip trip, GraphicsContext gc) {
        gc.setStroke(Color.hsb(Math.random()*360, 1, 1));
        gc.setLineWidth(2);
        for (Edge e : Main.graph.edges){ //for each edge
            if (trip.getTripId().equals(e.gettripId())){
                Point2D startPoint = model2Screen(e.fromStop().getPoint());
                Point2D endPoint = model2Screen(e.toStop().getPoint());
                drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY()); //drawline
            }

        }
    
    }
        
    }




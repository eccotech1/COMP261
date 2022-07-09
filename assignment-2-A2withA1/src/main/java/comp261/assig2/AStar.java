package comp261.assig2;

/*
Dijkstra search alogrithm

@author: Simon McCallum, Github Copilot
*/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ResourceBundle.Control;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// dijkstra class for path finding in the graph
public class AStar {
    public static boolean costType;

    public static ArrayList<Edge> findShortestPathEdges(Graph graph, Stop start, Stop end, boolean costT) {
        costType=costT; //if its time

        // check if the start and end stops are null
        if (start == null || end == null) {
            return null;
        }
        // used to check how many nodes you visited. Lower is better H.
        int totalExplored = 0;

        ArrayList<Stop> stops = graph.getStopList();

        // creating a comparison function for the priority queue based on Path
        Comparator<PathItem> pathStopCompare = new PathCostComparator();
        // create a new priority queue for the finge
        PriorityQueue<PathItem> fringe = new PriorityQueue<PathItem>(pathStopCompare);

        // create a new array list for the path to be extracted
        ArrayList<PathItem> visitedStops = new ArrayList<PathItem>();

        double currentFringeCost = 0;

        // vital step to make sure you can find the new path
        graph.resetVisited();

        start.setCost(0);

        double f = g(start, start) + heuristic(start, end); // also could be f(start,start, 0, start,end);
        fringe.add(new PathItem(start, 0, f, null, null)); // the input is (node, cost, f, prev, edge)

        // while the queue is not empty
        while (!fringe.isEmpty()) {
            // get the stop with the lowest cost

            // get the top of the queue
            PathItem current = fringe.poll();
            Stop stopCurr = current.getStop();
            // check if the stop from the queue has been visited
            if (stopCurr.isVisited()) {
                continue;
            }
            visitedStops.add(current);
            // if the stop has not been visited, mark as visited and add to
            // visitedStops list
            stopCurr.setVisited(true);
            stopCurr.setCost(current.cost);
            // set the cost for the current node
            currentFringeCost = current.getCost();
            totalExplored++;
            // if the current stop is the end stop
            {
                if (stopCurr.getId() == end.getId()) {
                    // path to return
                    ArrayList<Edge> shortestEdgePath = makeEdgePath(graph, visitedStops, start, end);
                    return shortestEdgePath;
                }

            }

            // TODO: go through each of the current stop's neighbours and add to the fringe
            {
                for (Edge e : stopCurr.getNeighbours()) {
                    // get the neighbour from the edge
                    Stop neighbourStop = e.getToStop();

                    // if the neighbour has not been visited already

                    if (!neighbourStop.isVisited()) {
                        // set the neighbour's cost to the current stop's cost + the edge's cost
                        // (time or distance)
                        if(costType){
                            e.setCost(e.getTime());
                        }else {
                            e.setCost(e.getDistance()); 
                        }
                        double neighbourCost = stopCurr.getCost() + e.getCost();
                        

                        // calculate the new f value using g edge cost and heuristic

                        f = g(start, stopCurr) + e.getCost() + heuristic(neighbourStop, end);
                        PathItem updateItem = new PathItem(neighbourStop, neighbourCost, f, stopCurr, e);
                        fringe.add(updateItem); //add the neighbour to the queue as a new PathItem
                         
                        
                        
                    }
                }
            }
        }

        // comment on failure
        System.out.println("Error: " + start + " to " + end + " not found");
        return new ArrayList<Edge>();
    }

    /**
     * build a path from the end back to the start from the PathItem data
     * 
     * @param graph
     * @param visited the nodes visited while searching for this trip
     * @param start
     * @param goal
     * @return the list of stops in the path
     */
    private static ArrayList<Edge> makeEdgePath(Graph graph, ArrayList<PathItem> visited, Stop start, Stop goal) {
        ArrayList<Edge> path = new ArrayList<Edge>();
        Edge currentItem = visited.get(visited.size() - 1).getEdge(); // the last edge added is on the path

        // while the current item is not the start
        while (currentItem.getFromStop() != start) {
            path.add(currentItem);
            // find the stop from the visited PathItems
            for (PathItem visitedItems : visited) {
                if (visitedItems.getEdge() == null) {
                    continue;
                }
                if (visitedItems.getEdge().getToStop() == currentItem.getFromStop()) {
                    if (visitedItems.getEdge().getToStop() == null) {
                        return null;
                    }
                    currentItem = visitedItems.getEdge();
                }
            }
            if (currentItem == null) { // if not invalid path
                System.out.println("error: ");
                return null;
            }
        }
        // finally add the start and return
        path.add(currentItem);
        return path;
    }

    public static double f(Stop start, Stop current, double edgeCost, Stop neighbour, Stop end) {
        return g(start, current) + edgeCost + heuristic(neighbour, end);
    }

    public static double g(Stop start, Stop current) {
        return current.getCost();
    }

    public static double heuristic(Stop current, Stop goal) {
       
        double maxSpeed = 27.53;
        double dist = current.getPoint().distance(goal.getPoint());
        if (costType) {
            return dist / maxSpeed;  //calculate a heuristic for the current stop to the goal stop
        } 
        return dist;
    }
    

}

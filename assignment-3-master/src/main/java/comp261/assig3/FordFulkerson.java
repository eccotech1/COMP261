package comp261.assig3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import javafx.util.Pair;

// helper class that does not need local memory

public class FordFulkerson {

    protected enum vehicles{
        bus,
        moped,
        car; 
    }
    // class members
    // Augmentation paths and the corresponding bottleneck flows
    private static ArrayList<Pair<ArrayList<Node>, Double>> augmentationPaths;
    // residual graph
    private static double[][] residualGraph;

    
    // constructor
    public FordFulkerson() {
        augmentationPaths = null;
        residualGraph = null;
    }
    
    // find maximum flow value
    public static double calcMaxflows(Graph graph, Node source, Node sink) {
        
        //error checking
        if(source == sink){
            return 0;
        } 
        //initialize residual graph to adj. matrix
        augmentationPaths = new ArrayList<Pair<ArrayList<Node>,Double>>();
        double flow = 0;
        int rows = graph.getAdjacencyMatrix().length;
        int cols = graph.getAdjacencyMatrix()[0].length;
        residualGraph = new double[rows][cols];
        //initizialise parent node while following a path
        for(int u = 0; u < rows ; u++){
            for(int v = 0; v < cols; v++){
                //together with type of car
                residualGraph[u][v] = graph.getAdjacencyMatrix()[u][v] * GraphController.vehicleWeight;
                
            }
        }
        //use BFS to find an augmentation path and the corressponding flow
        //update residual graph for each path identified
        Node parent[] = new Node[graph.getNodeList().size()];
        double foundflow = 0;
        while((foundflow = bfs(residualGraph, source, sink, parent)) != 0){
            flow += foundflow;
            //update residual graph
            int current = sink.getId();
            while(current != source.getId()){
                int prev = parent[current].getId();
                residualGraph[prev][current] -= foundflow;
                residualGraph[current][prev] += foundflow;
                current = prev;
            }
        }
        return flow;
    }
    
    // TODO:Use BFS to find an augmentation path from s to t
    // add the augmentation path found to the arraylist of augmentation paths
    // return bottleneck flow
    public static double bfs(double[][] residualgraph, Node s, Node t, Node[] parent) {
        // array to store visited nodes
        Arrays.fill(parent,null);
        
        Queue<Pair<Node, Double>> q = new LinkedList<Pair<Node, Double>>();
        
        q.add(new Pair<Node, Double>(s, Double.MAX_VALUE));
        
        while(!q.isEmpty()){
            Pair<Node, Double> first = q.remove();
            Node current = first.getKey();
            double flow = first.getValue();
            ArrayList<Node> adjacentVertices = new ArrayList<>(); //list of the adj.Vertex
            adjacentVertices = current.getNeighbours();
            //run through neighbours of current node
            for(Node n : adjacentVertices){
                if(parent[n.getId()] == null && residualgraph[current.getId()][n.getId()] != 0){
                    parent[n.getId()] = current;
                    
                    //build augmentation path 
                    double new_flow = Math.min(flow, residualgraph[current.getId()][n.getId()]);
                    if(n == t){ //create flow
                        flowPath(s, t, parent, new_flow);
                        return new_flow;
                    }
                    q.add(new Pair<Node, Double>(n, new_flow));
                }
            }
        }
        //return that checks if we are at the destination
        return 0;
    }
    
    // TODO: For each flow identified by bfs() build the path from source to sink
    // Add this path to the Array list of augmentation paths: augmentationPaths
    // along with the corresponding flow
    public static void flowPath(Node s, Node t, Node[] parent, double new_flow) {
        if(parent[t.getId()] == null){
            return;
        }
        ArrayList<Node> augmentationPath = new ArrayList<Node>();

        //find the augmentation path identified by the graph traversal algorithm
        Node child = t;
        while(parent[child.getId()] != null){
            augmentationPath.add(child);
            child = parent[child.getId()];
        }
        //add it to the list of augmentation paths
        augmentationPath.add(s);
        
        Collections.reverse(augmentationPath);

        
        // and add it to the list of augmentation paths
        augmentationPaths.add(new Pair<ArrayList<Node>, Double>(augmentationPath, new_flow));
        
    }
    
    // getter for augmentation paths
    public static ArrayList<Pair<ArrayList<Node>, Double>> getAugmentationPaths() {
        return augmentationPaths;
    }
    
    //find min-cut - as a set of sets and the corresponding cut-capacity
    public static Pair<Pair<HashSet<Node>, HashSet<Node>>, Double> minCut_s_t(Graph graph, Node source, Node sink) {
        calcMaxflows(graph, source, sink); 
        int rows = graph.getAdjacencyMatrix().length;
        Pair<Pair<HashSet<Node>, HashSet<Node>>, Double> minCutwithSets = null;
        HashSet<Node> sSet = new HashSet<Node>();
        HashSet<Node> tSet = new HashSet<Node>();
        //set S contains atleast the source
        sSet.add(source);
        // set T contains atleast sink
        tSet.add(sink);
        //run dfs to find the set of vertices reachable from S
        dfs(graph, source);
        
        for(int u = 0 ; u < rows ; u++){
            if(graph.findNode(u).isVisited()){
                sSet.add(graph.findNode(u));
            }else{
                tSet.add(graph.findNode(u));
            }

        }

        minCutwithSets = new Pair<>(new Pair<>(sSet, tSet),calcMaxflows(graph, source, sink));
        return minCutwithSets;
    }
    
    
    private static void dfs(Graph graph, Node s) {
        s.setVisited(true);
        //Use dfs to find set of nodes connected to s
        for(Node n : graph.getNodeList()){
            if(residualGraph[s.getId()][n.getId()] > 0 && !n.isVisited()){
                dfs(graph, n); //recursion
            }
        }
        
    } 
    
}


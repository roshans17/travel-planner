package sol;

import src.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TravelController implements ITravelController<City, Transport> {

    // Why is this field of type TravelGraph and not IGraph?
    // Are there any advantages to declaring a field as a specific type rather than the interface?
    // If this were of type IGraph, could you access methods in TravelGraph not declared in IGraph?
    // Hint: perhaps you need to define a method!
    private TravelGraph graph;

    public TravelController() {
    }

    @Override
    public String load(String citiesFile, String transportFile) {
        this.graph = new TravelGraph();
        try {
            this.parseLoc(citiesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.parseTransport(transportFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Successfully loaded cities and transportation files.";
    }

    public IOException parseLoc (String citiesFile){
        TravelCSVParser parser = new TravelCSVParser();
        Function<Map<String, String>, Void> addVertex = map -> {
            this.graph.addVertex(new City(map.get("name")));
            return null; // need explicit return null to account for Void type
        };

        try {
            // pass in string for CSV and function to create City (vertex) using city name
            parser.parseLocations(citiesFile, addVertex);
        } catch (IOException e) {
            return new IOException("Error parsing file: " + citiesFile);
        }
        return null;
    }

    public IOException parseTransport (String transportFile){
        TravelCSVParser parser = new TravelCSVParser();
        Function<Map<String, String>, Void> addEdge = map -> {
            this.graph.addEdge(this.graph.getCity(map.get("origin")),
                    new Transport(this.graph.getCity(map.get("origin")), this.graph.getCity(map.get("destination")),
                            TransportType.fromString(map.get("type")), Double.parseDouble(map.get("price")),
                            Double.parseDouble(map.get("duration"))));
            return null; // need explicit return null to account for Void type
        };
        try {
            // pass in string for CSV and function to create City (vertex) using city name
            parser.parseLocations(transportFile, addEdge);
        } catch (IOException e) {
            return new IOException("Error parsing file: " + transportFile);
        }
        return null;
    }

    @Override
    public List<Transport> fastestRoute(String source, String destination) {
        Function<Transport, Double> func = edge -> edge.getMinutes();
        Dijkstra<City, Transport> dijkstra = new Dijkstra<>();
        return dijkstra.getShortestPath(this.graph, this.graph.getCity(source), this.graph.getCity(destination), func);
    }

    @Override
    public List<Transport> cheapestRoute(String source, String destination) {
        Function<Transport, Double> func = edge -> edge.getPrice();
        Dijkstra<City, Transport> dijkstra = new Dijkstra<>();
        return dijkstra.getShortestPath(this.graph, this.graph.getCity(source), this.graph.getCity(destination), func);
    }

    @Override
    public List<Transport> mostDirectRoute(String source, String destination) {
        BFS<City, Transport> bfs = new BFS<>();
        try {
            List<Transport> path = bfs.getPath(this.graph, this.graph.getCity(source), this.graph.getCity(destination));
            return path;
        } catch (IllegalArgumentException i) {
            System.out.println(destination + " cannot be accessed from " + source);
            return new LinkedList<Transport>();
        } catch (RuntimeException r) {
            System.out.println("Error: there was no data to be parsed");
            return new LinkedList<Transport>();
        }
    }

    public TravelGraph getGraph() {
        return this.graph;
    }

}

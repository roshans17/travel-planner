package sol;

import src.City;
import src.IGraph;
import src.Transport;
import test.simple.SimpleEdge;

import java.util.*;
import java.util.function.Function;

public class TravelGraph implements IGraph<City, Transport> {
    HashMap<String, City> graphMap = new HashMap<>();

    //Adds all vertices from CSV file.
    @Override
    public void addVertex(City vertex) {
        this.graphMap.put(vertex.toString(), vertex);
    }

    //Adds all edges from CSV file.
    @Override
    public void addEdge(City origin, Transport edge) {
        City currCity = this.graphMap.get(origin.toString());
        currCity.addOut(edge);
    }

    //Returns the Set of cities located in the graph.
    @Override
    public Set<City> getVertices() {
        Set<String> cityNameSet = this.graphMap.keySet();
        Iterator<String> itrString = cityNameSet.iterator();
        Set<City> citySet = new HashSet<>();
        while (itrString.hasNext()) {
            citySet.add(this.graphMap.get(itrString.next()));
        }
        return citySet;
    }

    //Returns the source of an edge.
    @Override
    public City getEdgeSource(Transport edge) {
        return edge.getSource();
    }

    //Returns the target of an edge.
    @Override
    public City getEdgeTarget(Transport edge) {
        return edge.getTarget();
    }

    //Returns all the edges that a particular city has.
    @Override
    public Set<Transport> getOutgoingEdges(City fromVertex) {
        return fromVertex.getOutgoing();
    }

    //method returns a city by its name
    public City getCity(String cityName) {
        return this.graphMap.get(cityName);
    }

    public Transport getEdgeFromVertex(City vertexOrigin, City vertexDest) {
        Set<Transport> transportSet = vertexOrigin.getOutgoing();
        Iterator<Transport> itrTransportSet = transportSet.iterator();
        while (itrTransportSet.hasNext()) {
            if (itrTransportSet.next().getTarget() == vertexDest) {
                return itrTransportSet.next();
            }
        }
        return null;
    }

    public static double getTotalEdgeWeight(List<Transport> path, Function<Transport, Double> edgeWeight) {
        double total = 0;
        for (Transport transport : path) {
            total += edgeWeight.apply(transport);
        }
        return total;
    }

    public double getTotalPrice(List<Transport> path) {
        Function<Transport, Double> func = edge -> edge.getPrice();
        return this.getTotalEdgeWeight(path, func);
    }

    public double getTotalMinutes(List<Transport> path) {
        Function<Transport, Double> func = edge -> edge.getMinutes();
        return this.getTotalEdgeWeight(path, func);
    }

}
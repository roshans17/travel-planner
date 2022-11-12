package sol;

import src.City;
import src.IDijkstra;
import src.IGraph;
import src.Transport;

import java.util.*;
import java.util.function.Function;

public class Dijkstra<V, E> implements IDijkstra<V, E> {
    HashMap<V, E> cameFrom = new HashMap<>();

    /**
     *This function calls on the Dijklogic method that will populate the cameFrom HashMap.
     *Then the HashMap is passed into the backTrack method that will return the list of edges in the correct order.
     */

    @Override
    public List<E> getShortestPath(IGraph<V, E> graph, V source, V destination,
                                   Function<E, Double> edgeWeight) {
        this.DijkLogic(graph, source, edgeWeight);
        return this.backTrack(graph, this.cameFrom, source, destination);
    }

    public void DijkLogic(IGraph<V, E> graph, V source, Function<E, Double> edgeWeight) {

        //We will utilize a hashmap to keep track of all the weights of the different vertices.
        HashMap<V, Double> weights = new HashMap<>();
        Set<V> allCities = graph.getVertices();

        //We populate the weights HashMap such that source has a weight of 0 and all other vertex has inf.
        for (V city : allCities) {
            if (city.equals(source)) {
                weights.put(source, (double) 0);
            } else {
                weights.put(city, Double.MAX_VALUE);
            }
        }
        //Comparator that will compare two edge weights and sort priority queue based off the edge weights.
        Comparator<V> compareVertices = (v1, v2) -> {
            return Double.compare(weights.get(v1), weights.get(v2));
        };

        //We will utilize this priority queue that ensures that we have looked at all vertices, considering the vertex
        // that has the lowest weight first.
        PriorityQueue<V> pQ = new PriorityQueue<>(compareVertices);
        Set<V> vSet = weights.keySet();
        pQ.addAll(vSet);

        /**
         * This while loop performs the bulk of Dijk logic. It will first get the lowest value vertex from PriorityQueue and check its
         * corresponding neighbors and their weight. If the neighbor's current weight is larger than the value
         * of the current vertex's weight + the cost of the edge weight, the algo will update the neighbor's current weight
         * to that value. When a vertex's weight is updated, the edge from the current vertex gets stored into the cameFrom
         * HashMap. The while loop will go through all the reachable vertices in the graph and update weights and cameFrom
         * HashMap only if the weight value is less than the current weight!
         */
        while (!pQ.isEmpty()) {
            V currCity = pQ.poll();
            Set<E> currCityNeighbors = graph.getOutgoingEdges(currCity);
            for (E neighbor : currCityNeighbors) {
                if (weights.get(currCity) + edgeWeight.apply(neighbor) < weights.get(graph.getEdgeTarget(neighbor))) {
                    weights.replace(graph.getEdgeTarget(neighbor), weights.get(currCity) + edgeWeight.apply(neighbor));
                    this.cameFrom.put(graph.getEdgeTarget(neighbor), neighbor);
                    pQ.remove(graph.getEdgeTarget(neighbor));
                    pQ.add(graph.getEdgeTarget(neighbor));
                    pQ.comparator();
                    pQ.remove(currCity);
                }
            }
        }
    }

    // This method uses the cameFrom HashMap generated from DijkLogic to generate the route.
    public List<E> backTrack(IGraph<V, E> graph, HashMap<V, E> cameFrom, V source, V destination) {
        LinkedList<E> retList = new LinkedList<>();
        V checking = destination;

        /**
         * Checking will start out as the destination. We will then "back track" through the cameFrom HashMap which
         * stores the edges that get you to the destination as its values. Then the loop will continue until the source
         * vertex becomes are checking vertex, indicating that we have found all the edges that connect the source to
         * the destination.
         */
        while (checking != source) {
            E edge = cameFrom.get(checking);
            if (edge == null){
               break;
            }
            checking = graph.getEdgeSource(edge);
            retList.addFirst(edge);
        }
        return retList;
    }

    //Method is used strictly for testing purposes.
    public HashMap<V, E> retCameFrom(){
        return this.cameFrom;
    }
}

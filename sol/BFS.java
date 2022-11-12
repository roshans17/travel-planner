package sol;

import src.IBFS;
import src.IGraph;
import src.Transport;

import java.util.*;
import java.util.HashMap;
import java.util.LinkedList;

public class BFS<V, E> implements IBFS<V, E> {

    HashMap<V, E> cameFrom = new HashMap<V, E>();

    // TODO: implement the getPath method!
    @Override
    public List<E> getPath(IGraph<V, E> graph, V start, V end) {
        // initializing data structures to keep track of cities to check,
        // city pairs for retracing, and a list of already-visited cities

        if (graph.getVertices().isEmpty()) {
            throw new RuntimeException("graph is empty");
        }

        LinkedList<V> toCheck = new LinkedList<V>();
        HashSet<V> visited = new HashSet<V>();

        toCheck.addLast(start);

        // while there are cities to evaluate
        while (!toCheck.isEmpty()) {
            // remove last added item
            V checkingCity = toCheck.removeFirst();
            // if this is the city that we want to reach
            if (end.equals(checkingCity)) {
                LinkedList<E> retList = new LinkedList<E>();
                V checking = end;
                while (checking != start) {
                    E edge = this.cameFrom.get(checking);
                    checking = graph.getEdgeSource(edge);
                    retList.addFirst(edge);
                }
                return (retList);
            }
            visited.add(checkingCity);
            // for each transport edge of the city
            for (E transport : graph.getOutgoingEdges(checkingCity)) {
                // naming the target of the transport edge
                V edgeTarget = graph.getEdgeTarget(transport);
                if ((!visited.contains(edgeTarget)) && (!toCheck.contains(edgeTarget))) {
                    toCheck.addLast(edgeTarget);
                    this.cameFrom.put(edgeTarget, transport);
                }
            }
        }
        throw new IllegalArgumentException("There is no valid path.");
    }

}

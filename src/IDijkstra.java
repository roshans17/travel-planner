package src;

import java.util.List;
import java.util.function.Function;

/**
 * A class the implements Dijkstra's algorithm.
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the edges
 */
public interface IDijkstra<V, E> {

    /**
     * Finds the lowest cost path from source to destination.
     *
     * @param graph       the graph including the vertices
     * @param source      the source vertex
     * @param destination the destination vertex
     * @return a list of edges from source to destination
     */
    public List<E> getShortestPath(IGraph<V, E> graph, V source, V destination,
                                   Function<E, Double> edgeWeight);

}

package test.simple;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that models a simple vertex.
 */
public class SimpleVertex {
    public String id;
    public Set<SimpleEdge> outgoingEdges = new HashSet<>();

    public SimpleVertex(String id) {
        this.id = id;
    }

    public void addEdge(SimpleEdge edge) {
        this.outgoingEdges.add(edge);
    }

    @Override
    public String toString() {
        return this.id;
    }
}

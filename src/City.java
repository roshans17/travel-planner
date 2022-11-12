package src;

import java.util.HashSet;
import java.util.Set;

/**
 * vertex class (vertices are called Cities)
 */
public class City {
    private Set<Transport> outgoing;
    private String name;

    public City(String name) {
        this.name = name;
        this.outgoing = new HashSet<>(); //stores all outgoing Transports (edges) for given City (vertex)
    }

    public Set<Transport> getOutgoing() {
        return this.outgoing;
    }

    /**
     * Adds outgoing edge (Transport) to vertex (City)
     *
     * @param transport
     */
    public void addOut(Transport transport) {
        this.outgoing.add(transport);
    }

    @Override
    public String toString() {
        return this.name;
    }
}

package test.simple;

/**
 * A class that models a simple edge between two simple vertices.
 */
public class SimpleEdge {
    public double weight;
    public SimpleVertex source;
    public SimpleVertex target;

    public SimpleEdge(double weight, SimpleVertex source, SimpleVertex target) {
        this.weight = weight;
        this.source = source;
        this.target = target;
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return "SimpleEdge{" +
            "weight=" + this.weight +
            ", source=" + this.source +
            ", target=" + this.target +
            '}';
    }
}

package src;

/**
 * Edge class (edges are called Transports)
 */
public class Transport {
    private City source;
    private City target;
    private TransportType type;
    private double minutes;
    private double price;

    public Transport(City source, City destination, TransportType type, double price,
                     double minutes) {
        this.source = source;
        this.target = destination;
        this.type = type;
        this.price = price;
        this.minutes = minutes;
    }

    public City getSource() {
        return this.source;
    }

    public City getTarget() {
        return this.target;
    }

    public double getPrice() {
        return this.price;
    }

    public double getMinutes() {
        return this.minutes;
    }

    @Override
    public String toString() {
        return this.source.toString() + " -> " + this.target.toString() +
                ", Type: " + this.type.getLabel() +
                ", Cost: $" + this.price +
                ", Duration: " + this.minutes + " minutes";
    }
}

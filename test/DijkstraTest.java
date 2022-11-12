package test;

import org.junit.Test;
import sol.Dijkstra;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.IDijkstra;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Your Dijkstra's tests should all go in this class!
 * The test we've given you will pass if you've implemented Dijkstra's correctly, but we still
 * expect you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 * TODO: Recreate the test below for the City and Transport classes
 * TODO: Expand on your tests, accounting for basic cases and edge cases
 */
public class DijkstraTest {

    private static final double DELTA = 0.001;

    private SimpleGraph graph;
    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;

    private TravelGraph travelGraph;
    private TravelController travelControl;

    /** Creates a graph of empty values
     *
     */
    public void makeEmpty() {
        TravelController travelControl = new TravelController();
        travelControl.load("data/emptyCities.csv", "data/emptyTransport.csv");
        this.travelControl = travelControl;
        this.travelGraph = travelControl.getGraph();
    }

    /**
     * Creates a TravelGraph and TravelController based on testCities1 and testTransport1, files made
     * for testing in the data folder.
     */
    public void makeTest1() {
        TravelController travelControl = new TravelController();
        travelControl.load("data/testCities1.csv", "data/testTransport1.csv");
        this.travelControl = travelControl;
        this.travelGraph = travelControl.getGraph();
    }

    /**
     * Creates a TravelGraph and TravelController based on testCities2 and testTransport2, files made
     * for testing in the data folder.
     */
    public void makeTest2() {
        TravelController travelControl = new TravelController();
        travelControl.load("data/testCities2.csv", "data/testTransport2.csv");
        this.travelControl = travelControl;
        this.travelGraph = travelControl.getGraph();
    }

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     * TODO: create more setup methods!
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);

        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.b));
        this.graph.addEdge(this.a, new SimpleEdge(3, this.a, this.c));
        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.e));
        this.graph.addEdge(this.c, new SimpleEdge(6, this.c, this.b));
        this.graph.addEdge(this.c, new SimpleEdge(2, this.c, this.d));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.b));
        this.graph.addEdge(this.d, new SimpleEdge(5, this.e, this.d));
    }

    @Test
    public void testSimple() {
        this.createSimpleGraph();

        IDijkstra<SimpleVertex, SimpleEdge> dijkstra = new Dijkstra<>();
        Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
        // a -> c -> d -> b
        List<SimpleEdge> path =
                dijkstra.getShortestPath(this.graph, this.a, this.b, edgeWeightCalculation);
        assertEquals(6, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(3, path.size());

        // c -> d -> b
        path = dijkstra.getShortestPath(this.graph, this.c, this.b, edgeWeightCalculation);
        assertEquals(3, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(2, path.size());
    }

    //Check accuracy of cameFrom HashMap
    @Test
    public void testCameFrom(){
        this.makeTest2();
        Function<Transport, Double> func = edge -> edge.getPrice();
        Dijkstra<City, Transport> dijkstra = new Dijkstra<>();
        dijkstra.DijkLogic(this.travelGraph, this.travelGraph.getCity("A"),func);
        assertEquals("{D=B -> D, Type: bus, Cost: $20.0, Duration: 20.0 minutes, B=A -> B, " +
                "Type: bus, Cost: $20.0, Duration: 100.0 minutes, C=A -> C, Type: train, Cost: $80.0, " +
                "Duration: 300.0 minutes}", dijkstra.retCameFrom().toString());
    }

    //Tests if an empty list is returned when Dijk is given an empty graph.
    @Test
    public void testEmptyDijk() {
        this.makeEmpty();
        TravelController tC = this.travelControl;
        LinkedList<Transport> empty = new LinkedList<>();
        assertEquals(tC.cheapestRoute("A", "E"), empty);
    }

    //Tests the result of Dijk when there is no valid path.
    @Test
    public void testNoPathDijk(){
        this.makeTest2();
        TravelController tC = this.travelControl;
        LinkedList<Transport> empty = new LinkedList<>();
        assertEquals(tC.cheapestRoute("A", "E"), empty);
    }

    //Tests the result of Dijk when there is double edge to the destination vertex
    @Test
    public void testDoubleEdge(){
        this.makeTest1();
        TravelController tC = this.travelControl;
        String route = tC.cheapestRoute("A", "D").toString();
        assertEquals("[A -> B, Type: train, Cost: $50.0, Duration: 30.0 minutes, B -> C, Type: bus, Cost: $10.0, " +
                "Duration: 90.0 minutes, C -> D, Type: train, Cost: $20.0, Duration: 90.0 minutes]", route);
    }

    //Tests the result of Dijk when the source and target destination are the same
    @Test
    public void testSameV() {
        this.makeTest1();
        TravelController tC = this.travelControl;
        LinkedList<Transport> empty = new LinkedList<>();
        assertEquals(tC.cheapestRoute("A", "A"), empty);
    }

    //Tests the result of Dijk when there is a tie.
    @Test
    public void testTiesDijk(){
        this.makeTest2();
        TravelController tC = this.travelControl;
        LinkedList<Transport> tieOpt1 = new LinkedList<>();
        City a = new City("A");
        City c = new City("C");
        Transport aToC = new Transport(a, c, TransportType.TRAIN, 80, 300);
        tieOpt1.add(aToC);
        assertEquals(tC.cheapestRoute("A", "C"), tieOpt1);
    }

    //Tests the result of simple Dijk cases
    @Test
    public void testSimpleDijk() {
        this.makeTest2();
        TravelController tC = this.travelControl;
        String route = tC.cheapestRoute("A", "B").toString();
        assertEquals("[A -> B, Type: bus, Cost: $20.0, Duration: 100.0 minutes]", route);
    }
}

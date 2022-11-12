package test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sol.BFS;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Your BFS tests should all go in this class!
 * The test we've given you will pass if you've implemented BFS correctly, but we still expect
 * you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 * TODO: Recreate the test below for the City and Transport classes
 * TODO: Expand on your tests, accounting for basic cases and edge cases
 */
public class BFSTest {

    private static final double DELTA = 0.001;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;
    private SimpleVertex f;
    private SimpleGraph graph;

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
    public void makeSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");
        this.f = new SimpleVertex("f");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);
        this.graph.addVertex(this.f);

        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.b));
        this.graph.addEdge(this.b, new SimpleEdge(1, this.b, this.c));
        this.graph.addEdge(this.c, new SimpleEdge(1, this.c, this.e));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.e));
        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.f));
        this.graph.addEdge(this.f, new SimpleEdge(100, this.f, this.e));
    }

    @Test
    public void testParse() {
        TravelController tC = new TravelController();
        System.out.println(tC.load("data/cities2.csv", "data/transport2.csv"));
    }

    @Test
    public void testBasicBFS() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        List<SimpleEdge> path = bfs.getPath(this.graph, this.a, this.e);
        assertEquals(SimpleGraph.getTotalEdgeWeight(path), 200.0, DELTA);
        assertEquals(path.size(), 2);
    }

    @Test
    public void testLoad1() {
        this.makeTest1();
        Set<City> vertices = this.travelGraph.getVertices();
        Assert.assertEquals(vertices.size(), 5);
        System.out.println("graph vertices:" + vertices);
    }

    /**
     * Check that values are as expected for direct (BFS) routes in testTransport1.csv
     */
    @Test
    public void testBFS1() {
        this.makeTest1();

        // Most direct is different from both cheapest and fastest
        List<Transport> directAtoE = this.travelControl.mostDirectRoute("A", "E");
        double priceBFSAtoE = this.travelGraph.getTotalPrice(directAtoE);
        double minutesBFSAtoE = this.travelGraph.getTotalMinutes(directAtoE);
        assertEquals(minutesBFSAtoE, 100, DELTA);
        assertEquals(priceBFSAtoE, 950, DELTA);
        Assert.assertTrue(priceBFSAtoE >
                this.travelGraph.getTotalPrice(this.travelControl.cheapestRoute("A", "E")));
        Assert.assertTrue(minutesBFSAtoE >
                this.travelGraph.getTotalMinutes(this.travelControl.fastestRoute("A", "E")));


        // Most direct is same as fastest but shorter than cheapest
        List<Transport> directAtoD = this.travelControl.mostDirectRoute("A", "D");
        double priceBFSAtoD = this.travelGraph.getTotalPrice(directAtoD);
        double minutesBFSAtoD = this.travelGraph.getTotalMinutes(directAtoD);
        assertEquals(minutesBFSAtoD, 50, DELTA);
        assertEquals(priceBFSAtoD, 120, DELTA);
        assertTrue(priceBFSAtoD >
                this.travelGraph.getTotalPrice(this.travelControl.cheapestRoute("A", "D")));
        assertEquals(minutesBFSAtoD,
                this.travelGraph.getTotalMinutes(this.travelControl.fastestRoute("A", "D")), DELTA);
    }

    /**
     * Check that TravelController behaves as expected for invalid routes in testTransport1.csv
     */
    @Test
    public void testBFS1Except() {
        this.makeTest1();
        List<Transport> EtoA = this.travelControl.mostDirectRoute("E", "A");
        List<Transport> BtoA = this.travelControl.mostDirectRoute("B", "A");
        List<Transport> DtoA = this.travelControl.mostDirectRoute("D", "A");
        List<Transport> CtoA = this.travelControl.mostDirectRoute("C", "A");
        LinkedList<Transport> empty = new LinkedList<>();
        Assert.assertEquals(empty, EtoA);
        Assert.assertEquals(empty, DtoA);
        Assert.assertEquals(empty, CtoA);
        Assert.assertEquals(empty, BtoA);
    }

    /**
     * Check correct result returned by TravelController for missing graphs.
     */
    @Test
    public void testBFSEmptyExcept() {
        this.makeEmpty();
        TravelController tc = this.travelControl;
        LinkedList<Transport> empty = new LinkedList<>();
        Assert.assertEquals(tc.mostDirectRoute("A", "E"), empty);
    }

    @Test
    public void testLoad2() {
        this.makeTest2();
        Set<City> vertices = this.travelGraph.getVertices();
        Assert.assertEquals(vertices.size(), 5);
        System.out.println("graph vertices:" + vertices);
    }

    /**
     * Check that TravelController behaves as expected for invalid routes in testTransport2.csv
     */
    @Test
    public void testBFS2Invalid() {
        this.makeTest2();
        LinkedList<Transport> empty = new LinkedList<Transport>();
        Assert.assertEquals(this.travelControl.mostDirectRoute("D", "E"),
                empty);
        Assert.assertEquals(this.travelControl.mostDirectRoute("A", "E"),
                empty);
    }

    /**
     * Check that TravelController behaves as expected for valid routes in testTransport2.csv
     */
    @Test
    public void testBFS2() {
        this.makeTest2();
        Assert.assertEquals((this.travelControl.mostDirectRoute("A", "C")).size(), 1, DELTA);
        Assert.assertEquals((this.travelControl.mostDirectRoute("E", "A")).size(), 2, DELTA);
    }

    // TODO: write more tests + make sure you test all the cases in your testing plan!

    /**
     * Check that we get one of the possible results in a tie between two most direct routes
     */
    @Test
    public void testTieBFS() {
        City A = new City("A");
        City B = new City("B");
        City C = new City("C");
        City D = new City("D");
        City E = new City("E");
        Transport TrainAB = new Transport(A, B, TransportType.TRAIN, 10, 20);
        A.addOut(TrainAB);
        Transport TrainAD = new Transport(A, D, TransportType.TRAIN, 20, 40);
        A.addOut(TrainAD);
        Transport TrainBE = new Transport(B, E, TransportType.TRAIN, 60, 40);
        B.addOut(TrainBE);
        Transport BusBC = new Transport(B, C, TransportType.BUS, 40, 20);
        B.addOut(BusBC);
        Transport BusDC = new Transport(D, C, TransportType.BUS, 40, 20);
        D.addOut(BusDC);
        Transport TrainDE = new Transport(D, E, TransportType.TRAIN, 60, 40);
        D.addOut(TrainDE);
        TravelGraph graph = new TravelGraph();
        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);
        graph.addVertex(E);
        LinkedList<Transport> AtoD1 = new LinkedList<Transport>();
        AtoD1.add(TrainAD);
        AtoD1.add(BusDC);
        LinkedList<Transport> AtoD2 = new LinkedList<Transport>();
        AtoD2.add(TrainAB);
        AtoD2.add(BusBC);
        BFS bfs = new BFS();
        assertTrue((bfs.getPath(graph, A, C).equals(AtoD1))
                || (bfs.getPath(graph, A, C).equals(AtoD2)));
        LinkedList<Transport> AtoE1 = new LinkedList<Transport>();
        LinkedList<Transport> AtoE2 = new LinkedList<Transport>();
        AtoE1.add(TrainAB);
        AtoE1.add(TrainBE);
        AtoE2.add(TrainAD);
        AtoE2.add(TrainDE);
        assertTrue((bfs.getPath(graph, A, E).equals(AtoE1))
                || (bfs.getPath(graph, A, E).equals(AtoE2)));
    }
}

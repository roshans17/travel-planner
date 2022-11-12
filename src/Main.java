package src;

import sol.TravelController;

public class Main {
    public static void main(String[] args) {
        REPL<City, Transport> repl = new REPL<>(new TravelController());
        repl.run();
    }
}

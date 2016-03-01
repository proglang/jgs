package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import soot.Unit;
import soot.toolkits.graph.DirectedGraph;

import java.util.List;

/**
 * Contains methods to assert some assumptions that JGS has about UnitGraphs/method bodies
 * Created by fennell on 1/8/16.
 */
public class Assumptions {

    public static class Violation extends Exception {
        public Violation(String message) {
            super(message);
        }
    }

    public static void validUnitGraph(DirectedGraph<Unit> g) throws Violation {

        /* Branching statements should not be exit nodes.
        This restriction ensures that we always get a non-null immediatePostdominator */
        for (Unit u : g) {
            if (g.getSuccsOf(u).size() > 1 && g.getTails().contains(u)) {
                throw new Violation(String.format("Branching statement is an exit node: %s" +
                        "\n in graph %s", u, g));
            }
        }

    }
}

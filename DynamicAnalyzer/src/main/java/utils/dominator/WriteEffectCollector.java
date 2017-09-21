package utils.dominator;

import soot.Body;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import utils.logging.L1Logger;

import java.util.*;
import java.util.logging.Logger;

/**
 * This class provides Methods to locate assignments of a given Type
 * within the influence of a If within the initialised body.
 *
 * @author Karsten Fix, 21.09.17
 */
public class WriteEffectCollector<Type extends Value> {

    // <editor-fold desc="Graph Members for finding Dominator and Processing ">
    /** The graph, that is the base for all calculations */
    private UnitGraph graph;

    /** Finds all PostDominatorS based on the graph */
    private MHGPostDominatorsFinder<Unit> postDom;

    /** Finds all PreDominatorS based on the graph*/
    private MHGDominatorsFinder<Unit> preDom;
    // </editor-fold>

    /** Caches all instances of the given Type, that
     * are updated of the influence of an if stmt
     **/
    private Map<Unit, Set<Type>> instanceCache;

    private Class typeClass;

    private int callDepth = 0;

    private Logger logger = L1Logger.getLogger();

    public WriteEffectCollector(Class typeClass, Body b) {
        graph = new BriefUnitGraph(b);
        postDom = new MHGPostDominatorsFinder<>(graph);
        preDom = new MHGDominatorsFinder<>(graph);
        instanceCache = new HashMap<>();
        this.typeClass = typeClass;
    }

    private Set<Type> proceess(GraphElement element) {
        logger.fine("Processing Write Effect on depth: "+callDepth);

        // Initialising the Set of instances
        Set<Type> instances = new HashSet<>();

        if (element == null) return instances;

        // In Case the Element is branching, then there shall be a call of 
        // recursion        
        if (element.isBranchingForwards()) {

        }

        // Collecting Assignments
        element.content.apply(new AbstractStmtSwitch() {
            @Override
            public void caseAssignStmt(AssignStmt stmt) {
                if (typeClass.isInstance(stmt.getLeftOp())) {
                    instances.add((Type) stmt.getLeftOp());
                }
            }
        });

        instances.addAll(proceess(element.getSuccessor()));
        return instances;
    }

    public Set<Type> get() {
        // System.out.println(graph.getTails().get(0));
        for (Unit u : graph) {
            GraphElement elm = new GraphElement(u);
            System.out.println("Unit in Graph: "+ elm);

            if (elm.isBranchingForwards()) {
                for (GraphElement suc : elm.getSuccessors())
                    System.out.println("\tSucs: " + suc);
            }

            if (elm.isBranchingBackwards()) {
                for (GraphElement suc : elm.getPredecessors())
                    System.out.println("\tPred: " + suc);
            }

         //   System.out.println();
        }


        return proceess(new GraphElement(graph.getHeads().get(0)));
    }

    // <editor-fold desc="Graph Element for easier processing">

    /** Caches the position, such that it does not have to be
     * calculated every time */
    private static Map<Unit, Integer> positionCache = new HashMap<>();

    /**
     * This class represents Elements on the Graph. It is a certain wrapper
     * for the function, that are provided by the Classes {@link UnitGraph },
     * {@link MHGPostDominatorsFinder} and {@link MHGDominatorsFinder}. <br>
     * <br>
     * Hereby it optimizes the toString Method of the Unit Class,
     * such that it could be located within the graph.
     */
    protected class GraphElement {

        // <editor-fold desc="Members">

        /** The Unit, which is the content of this Element. */
        private Unit content;

        /** The unique position within the graph */
        private int position;

        // </editor-fold>

        /**
         * Creates a new GraphElement for the given Unit
         * @param element The Unit, that shall be located within the graph.
         * @throws IllegalArgumentException if the given Unit is not located in the graph.
         */
        private GraphElement(Unit element) {
            content = element;
            if (positionCache.containsKey(element))
                position = positionCache.get(element);
            else {
                position = 0;
                for (Unit u : graph) {
                    if (u.equals(element))
                        break;
                    position++;
                }
                if (position >= graph.size())
                    throw new IllegalArgumentException("The given Unit is not located in the graph: "+element);
                positionCache.put(element, position);
            }
        }

        /**
         * Gets the Content of the Graph Element
         * @return The Unit, that is actually represented by this Element.
         */
        public Unit getContent() {
            return content;
        }

        // <editor-fold desc="Operations considering the forward direction within the graph">

        /**
         * Returns the Post Dominator of this Graph Element.
         * @return That Graph Element, that will be visited after this Element,
         *         never mind which way it takes.
         *         or <b>null</b>, if there is no such element.
         */
        public GraphElement getPostDom() {
            return postDom.getImmediateDominator(content) == null
                   ? null : new GraphElement(postDom.getImmediateDominator(content));
        }

        /**
         * Gets the Successor of this Graph Element
         * @return The Graph Element, that follows directly after this Element
         *         or <b>null</b>, if there is no such element.
         */
        public GraphElement getSuccessor() {
            List<Unit> suc = graph.getSuccsOf(content);
            return suc != null && !suc.isEmpty() ? new GraphElement(suc.get(0)) : null;
        }

        /**
         * Gets a List of all Successors of This Graph Element
         * @return A List of all Graph Elements, that are followed this Element.
         */
        public List<GraphElement> getSuccessors() {
            List<GraphElement> succ = new ArrayList<>();
            for (Unit u : graph.getSuccsOf(content)) {
                succ.add(new GraphElement(u));
            }
            return succ;
        }

        /**
         * Tells, if this Element has more then one Successor.
         * @return true, if this Element opens a number of branches forwards within the tree.
         */
        public boolean isBranchingForwards() {
            return graph.getSuccsOf(content).size() > 1;
        }

        // </editor-fold>

        // <editor-fold desc="Operations considering the backward direction within the graph">

        /**
         * Returns the (Pre) Dominator of this Graph Element.
         * @return That Graph Element, that will be visited before this Element,
         *         never mind which way it takes.
         *         or <b>null</b>, if there is no such element.
         */
        public GraphElement getPreDom() {
            return preDom.getImmediateDominator(content) == null
                   ? null : new GraphElement(preDom.getImmediateDominator(content));
        }

        /**
         * Gets the Predecessor of this Graph Element
         * @return The Graph Element, that is directly before this Element
         *         or <b>null</b>, if there is no such element.
         */
        public GraphElement getPredecessor() {
            List<Unit> suc = graph.getPredsOf(content);
            return suc != null && !suc.isEmpty()? new GraphElement(suc.get(0)) : null;
        }

        /**
         * Gets a List of all Predecessors of This Graph Element
         * @return A List of all Graph Elements, that are before this Element.
         */
        public List<GraphElement> getPredecessors() {
            List<GraphElement> succ = new ArrayList<>();
            for (Unit u : graph.getPredsOf(content)) {
                succ.add(new GraphElement(u));
            }
            return succ;
        }

        /**
         * Tells, if this Element has more then one Predecessor.
         * @return true, if this Element opens a number of branches backwards within
         *         the graph.
         */
        public boolean isBranchingBackwards() {
            return graph.getPredsOf(content).size() > 1;
        }

        // </editor-fold>

        // <editor-fold desc="Overwriting Methods">

        /**
         * Returns a human readable representation of this Element.
         * @return A String, that really helps to locate this element within the
         *         graph.
         */
        @Override
        public String toString() {
            return "GraphElement["+content+"]@"+position;
        }

        // </editor-fold>
    }

    // </editor-fold>
}

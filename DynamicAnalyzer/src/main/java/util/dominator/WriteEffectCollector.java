package util.dominator;

import soot.Body;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides Methods to locate assignments of a given Type
 * within the influence of a If within the initialised body.
 *
 * @author Karsten Fix, 21.09.17
 */
public class WriteEffectCollector {

    // <editor-fold desc="Graph Members for finding Dominator and Processing ">
    /** The graph, that is the base for all calculations */
    private UnitGraph graph;

    /** Finds all PostDominators based on the graph */
    private MHGPostDominatorsFinder<Unit> postDom;

    /** Finds all PreDominators based on the graph*/
    private MHGDominatorsFinder<Unit> preDom;
    // </editor-fold>

    /** Caches all instances of the given Type, that
     * are updated of the influence of an if stmt
     **/
    private Map<Unit, Set<Value>> instanceCache = new HashMap<>();

    /** Saves all Graph Elements, that have been visited during the Search */
    private List<GraphElement> visitCache = new ArrayList<>();

    /** Defines that Element, that caused the Last branching recursion call */
    private GraphElement currentBranch;

    private int callDepth = -1;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Creates a new WriteEffectCollector for the given body.
     * @param b The body that shall be analyzed for having write Effects.
     */
    public WriteEffectCollector(Body b) {
        graph = new BriefUnitGraph(b);
        postDom = new MHGPostDominatorsFinder<>(graph);
        preDom = new MHGDominatorsFinder<>(graph);
        instanceCache = new HashMap<>();
        logger.setLevel(Level.FINE);
    }

    /**
     * Collects all write Effects within the Body.
     */
    public void collectWriteEffect() {
        visitCache.clear(); callDepth = -1;
        process(new GraphElement(graph.getHeads().get(0)), new HashSet<>());
    }

    /**
     * Processes the Write Effect Recursive.
     * This method is the heart of this class.
     * @param element The GraphElement, that is currently inspected by the Method.
     * @param instances The Set of Values, that have been collected so far, as
     *                  being a write Effect.
     */
    private void process(GraphElement element, Set<Value> instances) {
        callDepth++;
        logger.fine("Processing Write Effect of :"+element+" on depth: "+callDepth + " and visited "+visitCache.size());

        // End of Recursion One:
        // if the sequence of Successors has reached the end
        if (element == null) return;

        // End of Recursion Two:
        // in Case the element is the Post Dominator of the last branching element.
        // This assures, that one branch is processed after the other.
        if (currentBranch != null && element.equals(currentBranch.getPostDom())) return;

        // In Case the Element is branching, then there shall be a call of 
        // recursion. That's the case of an if (or a while - there is no difference here)
        if (element.isBranchingForwards()) {

            logger.fine("Contains? "+element);
            if (visitCache.contains(element)) return; // System.out.println("Seen once before: " + element);

            // Saving Element into cache
            visitCache.add(element);
            logger.fine("Added: "+element);

            logger.fine("Found Branching Element: "+element + " on Depth: "+ callDepth);

            // Every new branch shall have its own new Set, that collects the
            // Writing Effect of this new branch
            Set<Value> branchInst = new HashSet<>();
            currentBranch = element;

            for (GraphElement branch : element.getSuccessors()) {
                process(branch, branchInst);
            }

            // After processing store it within the cache
            // If no post dominator is present, simply do nothing
            if (element.getPostDom() == null) return;

            Set<Value> branchInstanceCache = instanceCache.get(element.getPostDom().content);
            if (branchInstanceCache == null) instanceCache.put(element.getPostDom().content, branchInst);
            else branchInstanceCache.addAll(branchInst);

            logger.fine("Cached collection: " + branchInst + " for post dom: "+element.getPostDom());

            // Reset the last branching, because this branch is now processed, completely
            currentBranch = null;
            // Continue with the Post Dominator, everything in between shall be processed
            process(element.getPostDom(), instances);

        } else {
            // Collecting Assignments
            element.content.apply(new AbstractStmtSwitch() {
                @Override
                public void caseAssignStmt(AssignStmt stmt) {
                    logger.fine("Collected "+stmt.getLeftOp() + " on depth "+callDepth);
                    instances.add(stmt.getLeftOp());
                }
            });
            process(element.getSuccessor(), instances);
        }
    }

    /**
     * Gets the Write Effect of a given Unit. This unit has to be a post dominator
     * of an if of which the Write Effect was calculated.
     * @param typeClass The Class of the Value (or Subclass of Value) which is interesting
     *                  to be considered. As Example: Local.class for all Locals, that
     *                  are within the calculated Write Effect.
     * @param endIf The Post Dominator of the if (or while or for etc) of which
     *              the Write Effect is calculated.
     * @param <Type> The Parameter Type of the subclass of Value, that is given as
     *              a class.
     * @return A Set of those Values (or Subtypes) that are the Write Effect of the given
     * endIf Unit <b>or an EmptySet</b>, if there is no Write Effect. <br>
     *     <b>NOTE:</b> It could be, that {@link WriteEffectCollector#collectWriteEffect()} was not
     *     called before.
     */
    public <Type extends Value> Set<Type> get(Class<Type> typeClass, Unit endIf) {
        if (!instanceCache.containsKey(endIf)) return Collections.emptySet();

        Set<Type> t = new HashSet<>();
        for (Value val : instanceCache.get(endIf)) {
            if (typeClass.isInstance(val)) t.add((Type) val);
        }
        return t;
    }

    /**
     * Gets a set of all Write Effects appearing in the analyzed body.
     * This is the Union of all Sets, that could be gained from {@link WriteEffectCollector#get(Class, Unit)}.
     * Without having any Post Dominator, you could use this function to
     * iterate over all Write Effects.
     * @param typeClass The Class of the Value (or Subclass of Value) which is interesting
     *                  to be considered. As Example: Local.class for all Locals, that
     *                  are within the calculated Write Effect.
     * @param <Type> The Parameter Type of the subclass of Value, that is given as
     *              a class.
     * @return A Set of all Values (or Subtypes), such that the Union of all Sets of Write Effects are this Set.
     *     <b>or an EmptySet</b>, if there is no Write Effect. <br>
     *     <b>NOTE:</b> It could be, that {@link WriteEffectCollector#collectWriteEffect()} was not
     *     called before.
     */
    public <Type extends Value> Set<Type> getAll(Class<Type> typeClass) {
        Set<Type> all = new HashSet<>();
        for (Set<Value> subs: instanceCache.values()) {
            for (Value val : subs) {
                if (typeClass.isInstance(val))
                    all.add((Type) val);
            }
        }
        return all;
    }

    /**
     * Prints the Chain of Methods, as Graph Elements, for easier debugging
     */
    public void printBodyGraph() {
        for (Unit u : graph) {
            GraphElement elm = new GraphElement(u);
            System.out.println(elm);
        }
        System.out.println();
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

        @Override
        @SuppressWarnings("unchecked cast")
        public boolean equals(Object o) {
            // System.out.println(this + " ? "+ o);
            boolean b =  o != null && (o.getClass() == GraphElement.class) && position == (((GraphElement) o).position);
            // System.out.println(b);
            return b;
        }

        // </editor-fold>
    }

    // </editor-fold>
}

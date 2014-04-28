package analysis;

import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;

/**
 * <h1>Wrapper for (post-) dominators</h1>
 * 
 * The {@link Dominator} class provides the post dominator as well as the dominator objects. These allow to distinguish the (post-)
 * dominator set for a specific unit. The class provides a method a method that checks whether the post dominator set of a given
 * if-statement contains the second given statement.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
class Dominator<N> {

	/** Dominator finder object. */
	private final MHGDominatorsFinder<N> dominatorFinder;
	/** Post dominator finder object. */
	private final MHGPostDominatorsFinder postDominatorFinder;

	/**
	 * Constructor of a {@link Dominator} which requires a graph in order to generated the (post-) dominator finder.
	 * 
	 * @param graph
	 *          The <code>DirectedGraph</code> of the analyzed method.
	 */
	protected Dominator(DirectedGraph<N> graph) {
		this.dominatorFinder = new MHGDominatorsFinder<N>(graph);
		this.postDominatorFinder = new MHGPostDominatorsFinder(graph);
	}

	/**
	 * Method returns the dominator finder object.
	 * 
	 * @return The dominator finder.
	 */
	public MHGDominatorsFinder<N> getDominatorFinder() {
		return dominatorFinder;
	}

	/**
	 * Method returns the post-dominator finder object.
	 * 
	 * @return The post-dominator finder.
	 */
	public MHGPostDominatorsFinder getPostDominatorFinder() {
		return postDominatorFinder;
	}

	/**
	 * Checks whether the post-dominator set of the given if-statement contains the given statement. I.e. the given statement is a
	 * post-dominator of the if-statement.
	 * 
	 * @param ifStmt
	 *          if-statement whose post-dominator set is generated.
	 * @param s
	 *          statement for which will be checked whether it is contained by the post-dominator set.
	 * @return {@code true} if the post-dominator set of the given if-statement contains the other given statement, otherwise {@code false}.
	 */
	public boolean postDomSetOfIfContainsS(IfStmt ifStmt, Stmt s) {
		return postDominatorFinder.getDominators(ifStmt).contains(s);
	}

}
package util.dominator;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import util.exceptions.MaximumNumberExceededException;

import java.lang.Long;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Find postdominators for if-statements to determine their scope. Postdominators are represented as integer IDs.
 * 
 * @author koenigr, fennell
 *
 */

// TODO: it is not pretty that everything is static here
// TODO: it is also wasteful to use strings as ids. We should use ints (HandleStmt also should use ints)
public class DominatorFinder {
	private static MHGPostDominatorsFinder<Unit> pdfinder;
	private static HashMap<Unit, String> domMap;
	
	// ID-counter for identifying postdominators 
	private static long identity;
	// Distinguished ID to be used if the virtual postdominator at the end of a method. We use this virtual postdominator to work around the fact that Jimple does not guarantee a single return statement.
	private final static String POSTDOM_ID_END_OF_METHOD = "" + Integer.MIN_VALUE;

	private static final Logger logger = Logger.getLogger(DominatorFinder.class.getName());
  
	/**
	 * Constructor. Has only to be called once in BodyAnalyzer.
	 * @param body The body of the actual analyzed method.
	 */
	public static void init(Body body) {
		pdfinder = new MHGPostDominatorsFinder<>(new BriefUnitGraph(body));
		domMap = new HashMap<>();
		identity = 0;
	}

	/**
	 * Get the hashvalue of the immediate dominator of given IfStmt. 
	 * The unit is stored in an internal list for later analysis
	 * (if it's not already in the list).
	 * @param node IfStmt.
	 * @return Hashvalue of immediate dominator.
	 */
	public static String getImmediateDominatorIdentity(Unit node) {
		Unit dom = pdfinder.getImmediateDominator(node);

		if (dom != null) {
		    String id = getIdentityForUnit(dom);
		    // we have an immediate postdominator
		 logger.info("Postdominator \"" + dom.toString()
				+ "\" has Identity " + id);
		     return id;
		} else {
		 logger.info("Postdominator for node " + node.toString() + "is the end of the method");
		 return POSTDOM_ID_END_OF_METHOD;
		}
	}

	/**
	 * Check whether the given unit is a dominator of an IfStmt.
	 * @param node A Unit.
	 * @return Returns true if the given unit is a dominator of a previously 
	 *     called ifStmt.
	 */
	public static boolean containsStmt(Unit node) {
		return domMap.containsKey(node);
	}
	
	/**
	 * Remove given Unit from DominatorList.
	 * @param node A Unit.
	 *     called ifStmt.
	 */
	public static void removeStmt(Unit node) {
		if (domMap.containsKey(node)) {
			domMap.remove(node);
		}
	}
	
	/** Get the identity for given Unit or create a new identity.
	 * @param dom The Object.
	 * @return The hash-value for given object.
	 */
	public static String getIdentityForUnit(Unit dom) {
		if (domMap.containsKey(dom)) {
			return domMap.get(dom);
		} else {
			if (identity < Long.MAX_VALUE) {
				identity++;
				domMap.put(dom, Long.toString(identity));
			} else {
				new MaximumNumberExceededException("You have exceeded the maximum "
					+ "number of allowed if-statements "
					+ "within a method which is "
					+ Long.toString(identity));
			}
		}
		return domMap.get(dom);
	}
	
	public static void printDomList() {
		System.out.println(domMap.toString());
	}
  
}

package utils.dominator;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import utils.exceptions.MaximumNumberExceededException;
import utils.logging.L1Logger;

import java.lang.Long;
import java.util.HashMap;
import java.util.logging.Logger;

public class DominatorFinder {
	private static MHGPostDominatorsFinder pdfinder;
	private static UnitGraph graph;
	private static HashMap<Unit, String> domList;
	private static long identity;
	private static Logger logger = L1Logger.getLogger();
  
	/**
	 * Constructor. Has only to be called once in BodyAnalyzer.
	 * @param body The body of the actual analyzed method.
	 */
	public static void init(Body body) {
		graph = new BriefUnitGraph(body);
		pdfinder = new MHGPostDominatorsFinder(graph);
		domList = new HashMap<Unit, String>();
		identity = 0;
	}

	/**
	 * Get the hashvalue of the immediate dominator of given IfStmt. 
	 * The unit is stored in an internal list for later analysis
	 * (if it's not already in the list).
	 * @param node IfStmt.
	 * @return Hashvalue of immerdiate dominator.
	 */
	public static String getImmediateDominatorIdentity(Unit node) {
		Unit dom = (Unit) pdfinder.getImmediateDominator(node);
		if (!containsStmt(dom)) {
			domList.put(dom, getIdentityForUnit(dom));
			System.out.println(domList.toString());
		} 

		logger.info("Dominator \"" + dom.toString()
				+ "\" has Identity " + domList.get(dom));
		return domList.get(dom);
	}

	/**
	 * Check whether the given unit is a dominator of an IfStmt. If it is
	 * contained in the list this element is simultaneously removed from the list.
	 * @param node A Unit.
	 * @return Returns true if the given unit is a dominator of a previously 
	 *     called ifStmt.
	 */
	public static boolean containsStmt(Unit node) {
		if (domList.containsKey(node)) {
			domList.remove(node);
			return true;
		}
		return false;
	}
	
	/** Just get the hash-value for the object without storing it in a list.
	 * @param dom The Object.
	 * @return The hash-value for given object.
	 */
	public static String getIdentityForUnit(Unit dom) {
		if (domList.containsKey(dom)) {
			return domList.get(dom);
		} else {
			if (identity < Long.MAX_VALUE) {
				identity++;
				domList.put(dom, Long.toString(identity));
			} else {
				new MaximumNumberExceededException("You have exceeded the maximum number"
					+ "of allowed if-statements within a method which is "
					+ Long.toString(identity));
			}
		}
		return domList.get(dom);
	}
  
}

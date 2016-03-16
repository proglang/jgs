package utils.dominator;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import utils.logging.L1Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DominatorFinder {
	private static MHGPostDominatorsFinder pdfinder;
	private static UnitGraph graph;
	private static List<Unit> domList;
	
	private static Logger logger = L1Logger.getLogger();
  
	/**
	 * Constructor. Has only to be called once in BodyAnalyzer.
	 * @param body The body of the actual analyzed method.
	 */
	public DominatorFinder(Body body) {
		graph = new BriefUnitGraph(body);
		pdfinder = new MHGPostDominatorsFinder(graph);
		domList = new ArrayList<Unit>();
	}
  
	

	/**
	 * Get the hashvalue of the immediate dominator of given IfStmt. 
	 * The unit is stored in an internal list for later analysis.
	 * @param node IfStmt.
	 * @return Hashvalue of immerdiate dominator.
	 */
	public static String getImmediateDominatorHashValue(Unit node) {
		Object dom = pdfinder.getImmediateDominator(node);
		logger.info("Dominator :" + dom.toString());
		if (!containsStmt((Unit) dom)) {
			domList.add((Unit) dom);
		}
		return getHashValueFor(dom);
	}

	/**
	 * Check whether the given unit is a dominator of an IfStmt. If it is
	 * contained in the list this element is simultaneously removed from the list.
	 * @param node A Unit.
	 * @return Returns true if the given unit is a dominator of a previously 
	 *     called ifStmt.
	 */
	public static boolean containsStmt(Unit node) {
		if (domList.contains(node)) {
			domList.remove(node);
			return true;
		}
		return false;
	}
	
	public static String getHashValueFor(Object dom) {
		return Integer.toString(System.identityHashCode(dom));
	}
  
}

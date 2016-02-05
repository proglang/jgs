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
	private MHGPostDominatorsFinder pdfinder;
	private UnitGraph graph;
	private List<Unit> postDomList = new ArrayList<Unit>();
	Logger logger = L1Logger.getLogger();
  
	public DominatorFinder(Body body) {
		graph = new BriefUnitGraph(body);
		pdfinder = new MHGPostDominatorsFinder(graph);
	}
  
	/**
	 * Get the f
	 * @param node -Unit- 
	 */
	public void getImmediateDominator(Unit node) {
		postDomList.add((Unit) pdfinder.getImmediateDominator(node));
		logger.info("postDomList after inserting one element :" + postDomList.toString());
		logger.info("Dominator :" + pdfinder.getImmediateDominator(node).toString());
	}
  
	/**
	 * @param node
	 * @return
	 */
	public boolean containsStmt(Unit node) {
		if (postDomList.contains(node)) {
			postDomList.remove(node);
			logger.info("PostDomList after Removing one element "
					+ postDomList.toString());
			return true;
		} else {
			return false;
		}
	}
  
}

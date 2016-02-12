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
	
	Logger logger = L1Logger.getLogger();
  
	public DominatorFinder(Body body) {
		graph = new BriefUnitGraph(body);
		pdfinder = new MHGPostDominatorsFinder(graph);
	}
  
	/**
	 * Get the f
	 * @param node 
	 */
	public int getImmediateDominatorHashValue(Unit node) {
		Object dom = pdfinder.getImmediateDominator(node);
		logger.info("Dominator :" + dom.toString());
		return System.identityHashCode(dom);
	}
  
}

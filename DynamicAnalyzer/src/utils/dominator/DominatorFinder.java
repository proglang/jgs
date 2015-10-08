package utils.dominator;

import soot.Body;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;

public class DominatorFinder {
  private static MHGPostDominatorsFinder pdfinder;
  private static UnitGraph graph;
  
  public DominatorFinder(Body body) {
	  graph = new BriefUnitGraph(body);
	  pdfinder = new MHGPostDominatorsFinder(graph);
  }
  
  public boolean postDomSetOfStmtContainsS(Stmt stmt, Stmt s) {
      return pdfinder.getDominators(stmt).contains(s);
  }
  
  public Unit getImmediateDominator(Unit node) {
	return  (Unit) pdfinder.getImmediateDominator(node);  
  }
}

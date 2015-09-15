package utils.dominator;

import soot.Body;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;

public class DominatorFinder {
  MHGPostDominatorsFinder pdfinder;
  
  
  public DominatorFinder(Body body) {
	  UnitGraph graph = new BriefUnitGraph(body);
	  pdfinder = new MHGPostDominatorsFinder(graph);
  }
  
  public boolean postDomSetOfStmtContainsS(Stmt stmt, Stmt s) {
      return pdfinder.getDominators(stmt).contains(s);
  }
}

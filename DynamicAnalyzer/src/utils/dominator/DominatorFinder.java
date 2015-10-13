package utils.dominator;

import java.util.ArrayList;
import java.util.List;

import soot.Body;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;

public class DominatorFinder {
  private MHGPostDominatorsFinder pdfinder;
  private UnitGraph graph;
  private List<Unit> postDomList = new ArrayList<Unit>();
  
  public DominatorFinder(Body body) {
	  graph = new BriefUnitGraph(body);
	  pdfinder = new MHGPostDominatorsFinder(graph);
  }
  
  public void getImmediateDominator(Unit node) {
	postDomList.add((Unit) pdfinder.getImmediateDominator(node));  
	System.out.println("postDomList after inserting one element :" + postDomList.toString());
	System.out.println("Dominator :" + pdfinder.getImmediateDominator(node).toString());
  }
  
  public boolean containsStmt(Unit node) {
	  if (postDomList.contains(node)) {
		  postDomList.remove(node);
			System.out.println("PostDomList after Removing one element " + postDomList.toString());
		  return true;
	  } else {
		  return false;
	  }
  }
  
  
}

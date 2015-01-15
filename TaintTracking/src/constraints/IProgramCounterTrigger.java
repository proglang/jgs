package constraints;

import soot.jimple.Stmt;

public interface IProgramCounterTrigger {

	public boolean equals(Object obj);

	public Stmt getStmt();

	public int hashCode();

}
package constraints;

import soot.jimple.IfStmt;
import soot.jimple.Stmt;

public class IfProgramCounterTrigger implements IProgramCounterTrigger {

	private final IfStmt stmt;

	public IfProgramCounterTrigger(IfStmt stmt) {
		this.stmt = stmt;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		IfProgramCounterTrigger other = (IfProgramCounterTrigger) obj;
		if (stmt == null) {
			if (other.stmt != null) return false;
		} else if (!stmt.equals(other.stmt)) return false;
		return true;
	}

	public final Stmt getStmt() {
		return stmt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
		return result;
	}

}
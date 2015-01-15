package constraints;

import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;

public class LookupSwitchProgramCounterTrigger implements IProgramCounterTrigger {

	private final LookupSwitchStmt stmt;

	public LookupSwitchProgramCounterTrigger(LookupSwitchStmt stmt) {
		this.stmt = stmt;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LookupSwitchProgramCounterTrigger other = (LookupSwitchProgramCounterTrigger) obj;
		if (stmt == null) {
			if (other.stmt != null) return false;
		} else if (!stmt.equals(other.stmt)) return false;
		return true;
	}

	public final LookupSwitchStmt getLookupSwitchStmt() {
		return stmt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
		return result;
	}

	@Override
	public Stmt getStmt() {
		return stmt;
	}

}
package constraints;

import soot.Local;

public class ConstraintLocal implements IConstraintComponentVar {
	
	private final Local local;	
	
	public ConstraintLocal(Local local) {
		this.local = local;
	}

	public Local getLocal() {
		return local;
	}
	
	@Override
	public String toString() {
		return local.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((local == null) ? 0 : local.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ConstraintLocal other = (ConstraintLocal) obj;
		if (local == null) {
			if (other.local != null) return false;
		} else if (!local.equals(other.local)) return false;
		return true;
	}
	
}
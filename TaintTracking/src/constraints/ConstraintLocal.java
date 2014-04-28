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
	public boolean equals(Object obj) {
		if (obj == this) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ConstraintLocal loc = (ConstraintLocal) obj;
    return loc.local.equals(local);
	}
	
}
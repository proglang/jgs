package constraints;

public class ConstraintProgramCounterRef implements IConstraintComponentVar {

	public String toString() {
		return "PC";
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    return true;
	}
	
}

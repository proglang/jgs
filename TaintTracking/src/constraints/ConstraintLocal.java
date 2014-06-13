package constraints;

import java.util.regex.Pattern;

import soot.Local;

public class ConstraintLocal implements IConstraintComponentVar {
	
	private final Local local;
	private final boolean generatedLocal;
	
	public ConstraintLocal(Local local) {
		this.local = local;
		this.generatedLocal = Pattern.matches("temp\\$(\\d|[1-9]\\d*)", local.getName());
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

	@Override
	public IConstraintComponent changeSignature(String signature) {
		return new ConstraintLocal(local);
	}

	public boolean isGeneratedLocal() {
		return generatedLocal;
	}
	
}
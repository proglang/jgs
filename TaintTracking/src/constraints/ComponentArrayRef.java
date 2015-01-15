package constraints;

public class ComponentArrayRef implements IComponentVar {
	
	private final int dimesion;
	private final IComponentArrayBase base;

	public ComponentArrayRef(IComponentArrayBase base, int dimension) {
		this.base = base;
		this.dimesion = dimension;
	}

	@Override
	public IComponent changeSignature(String signature) {
		return new ComponentArrayRef(base.changeSignature(signature), this.dimesion);
	}
	
	@Override
	public String toString() {
		return base.toString() + new String(new char[dimesion]).replace("\0", "[");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + dimesion;
		return result;
	}

	protected final int getDimesion() {
		return dimesion;
	}

	protected final IComponent getBase() {
		return base;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ComponentArrayRef other = (ComponentArrayRef) obj;
		if (base == null) {
			if (other.base != null) return false;
		} else if (!base.equals(other.base)) return false;
		if (dimesion != other.dimesion) return false;
		return true;
	}

}

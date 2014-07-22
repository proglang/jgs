package constraints;

public class ComponentPlaceholder implements IComponent, IComponentArrayBase {

	@Override
	public IComponentArrayBase changeSignature(String signature) {
		return new ComponentPlaceholder();
	}

	@Override
	public String toString() {
		return "#";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		return true;
	}
	
}

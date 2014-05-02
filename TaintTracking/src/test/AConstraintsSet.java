package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import security.ILevelMediator;
import constraints.IConstraint;
import constraints.IConstraintComponent;

public abstract class AConstraintsSet<C extends IConstraint> implements Collection<C> {
	
	protected final Map<IConstraintComponent, List<IConstraintComponent>> leftSide = new HashMap<IConstraintComponent, List<IConstraintComponent>>();
	protected final Map<IConstraintComponent, List<IConstraintComponent>> rightSide= new HashMap<IConstraintComponent, List<IConstraintComponent>>();
	protected final List<C> constraints = new ArrayList<C>();
	protected final ILevelMediator mediator;
	
	public AConstraintsSet(ILevelMediator mediator){
		this.mediator = mediator;
	}
	
	
	@Override
	public final Iterator<C> iterator() {
		return constraints.iterator();
	}

	@Override
	public final int size() {
		return constraints.size();
	}
	
	@Override
	public final boolean isEmpty() {
		return constraints.isEmpty();
	}

	@Override
	public final boolean contains(Object obj) {
		return constraints.contains(obj);
	}

	@Override
	public final Object[] toArray() {
		return constraints.toArray();
	}

	@Override
	public final <T> T[] toArray(T[] a) {
		return constraints.toArray(a);
	}
	
	@Override
	public final boolean containsAll(Collection<?> cs) {
		return constraints.containsAll(cs);
	}

	@Override
	public final boolean addAll(Collection<? extends C> constraints) {
		boolean res = false;
		for (C c : constraints) {
			res |= add(c);
		}
		return res;
	}

	@Override
	public final boolean removeAll(Collection<?> cs) {
		boolean res = false;
		for (Object c : cs) {
			res |= remove(c);
		}
		return res;
	}

	@Override
	public final boolean retainAll(Collection<?> cs) {
		List<C> removing = new ArrayList<C>();
		for (C c : constraints) {
			if (! cs.contains(c)) {
				removing.add(c);
			}
		} 
		return removeAll(removing);
	}

	@Override
	public final void clear() {
		rightSide.clear();
		leftSide.clear();
		constraints.clear();
	}
	
	public abstract List<C> getTransitiveClosure();
	
	protected abstract boolean consitentRemove(Object obj);
	
	protected abstract void recalculateTransitiveClosure();
	
	public final List<C> getConstraintsList() {
		return new ArrayList<C>(constraints);
	}
	
}
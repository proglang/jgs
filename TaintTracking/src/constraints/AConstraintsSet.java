package constraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import security.ILevelMediator;
import soot.Local;

public abstract class AConstraintsSet<C extends IConstraint> implements Collection<C> {

	private final Set<C> constraints = new HashSet<C>();

	protected final ILevelMediator mediator;

	public AConstraintsSet(ILevelMediator mediator) {
		this.mediator = mediator;
	}

	public AConstraintsSet(Set<C> constraints, ILevelMediator mediator) {
		this(mediator);
		for (C constraint : constraints) {
			add(constraint);
		}
	}

	@Override
	public boolean add(C e) {
		return constraints.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return constraints.remove(o);
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
			if (!cs.contains(c)) {
				removing.add(c);
			}
		}
		return removeAll(removing);
	}

	@Override
	public final void clear() {
		constraints.clear();
	}

	public abstract Set<C> getTransitiveClosureSet();

	protected void calculateTransitiveClosure() {
		Set<C> closure = calculateTransitiveClosure(constraints);
		constraints.clear();
		constraints.addAll(closure);
	}

	private Set<C> calculateTransitiveClosure(Set<C> set) {
		set = new HashSet<C>(set);
		int size = -1;
		while (size != set.size()) {
			size = set.size();
			Set<C> additional = new HashSet<C>();
			for (C leftConstraint : set) {
				for (C rightConstraint : set) {
					if (transitivClosureMergeCondition(leftConstraint, rightConstraint)) {
						additional.add(transitivClosureMerge(leftConstraint, rightConstraint));
					}
				}
			}
			set.addAll(additional);
		}
		return set;
	}

	public final Set<C> getConstraintsSet() {
		return new HashSet<C>(constraints);
	}

	public Set<C> getTransitiveReductionSet() {
		return calculateTransitiveReduction(constraints);
	}
	
	protected void calculateTransitiveReduction() {
		Set<C> reduction = calculateTransitiveReduction(constraints);
		constraints.clear();
		constraints.addAll(reduction);
	}
	
	public void removeConstraintsContainingReferencesFor(String... signatures) {
		for (String signature : signatures) {
			for (C constraint : getConstraintsSet()) {
				if (constraint.containsReturnReferenceFor(signature) || constraint.containsParameterReferenceFor(signature)
						|| constraint.containsProgramCounterReferenceFor(signature)) {
					remove(constraint);
				}
			}
		}
	}

	public void removeConstraintsContainingLocal() {
		for (C constraint : getConstraintsSet()) {
			if (constraint.containsLocal()) remove(constraint);
		}
	}

	public void removeConstraintsContainingGeneratedLocal() {
		for (C constraint : getConstraintsSet()) {
			if (constraint.containsGeneratedLocal()) remove(constraint);
		}
	}
	
	public void removeConstraintsContainingProgramCounter() {
		for (C constraint : getConstraintsSet()) {
			if (constraint.containsProgramCounterReference()) remove(constraint);
		}
	}
	
	public void removeConstraintsContaining(Local local) {
		for (C constraint : getConstraintsSet()) {
			if (constraint.containsComponent(new ConstraintLocal(local))) remove(constraint);
		}
	}
	
	private Set<C> calculateTransitiveReduction(Set<C> original) {
		Set<C> closure = calculateTransitiveClosure(original);
		Set<C> reduction = new HashSet<C>(closure);
		int size = -1;
		while (size != reduction.size()) {
			size = reduction.size();
			for (C constraint : reduction) {
				Set<C> temp = new HashSet<C>(reduction);
				temp.remove(constraint);
				if (calculateTransitiveClosure(temp).equals(closure)) {
					reduction.remove(constraint);
					break;
				}
			}
		}
		return reduction;
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{ ");
		boolean tail = false;
		for (C constraint : constraints) {
			if (tail) {
				sb.append(", ");
			} else {
				tail = true;
			}
			sb.append(constraint.toString());
		}
		sb.append(" }");
		return sb.toString();
	}
	
	protected abstract boolean transitivClosureMergeCondition(C leftConstraint, C rightConstraint);

	protected abstract C transitivClosureMerge(C leftConstraint, C rightConstraint);

	public abstract boolean equals(Object obj);

	public abstract int hashCode();
	
	public abstract Set<C> getInequality();

}
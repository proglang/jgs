package test;

import static resource.Messages.getMsg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import security.ILevelMediator;

import constraints.IConstraint;
import constraints.LEQConstraint;
import exception.ConstraintUnsupportedException;

public class ConstraintSet implements Collection<IConstraint> {
	
	
	
	private final LEQConstraintsSet leqConstraintsSet;
	private final ILevelMediator mediator;

	public ConstraintSet(ILevelMediator mediator) {
		this.mediator = mediator;
		this.leqConstraintsSet =  new LEQConstraintsSet(mediator);
	}
	
	public ConstraintSet(List<IConstraint> constraints, ILevelMediator mediator) {
		this.mediator = mediator;
		this.leqConstraintsSet =  new LEQConstraintsSet(mediator);
		addAll(constraints);
	}

	@Override
	public int size() {
		return leqConstraintsSet.size();
	}

	@Override
	public boolean isEmpty() {
		return leqConstraintsSet.isEmpty();
	}

	@Override
	public boolean contains(Object obj) {
		return leqConstraintsSet.contains(obj);
	}

	@Override
	public Iterator<IConstraint> iterator() {
		List<IConstraint> ls = new ArrayList<IConstraint>();
		for (LEQConstraint constraint : leqConstraintsSet) {
			ls.add(constraint);
		}
		return ls.iterator();
	}

	@Override
	public Object[] toArray() {
		return leqConstraintsSet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return leqConstraintsSet.toArray(a);
	}

	@Override
	public boolean add(IConstraint constraint) {
		if (constraint instanceof LEQConstraint) {
			return leqConstraintsSet.add((LEQConstraint) constraint);
		} else {
			throw new ConstraintUnsupportedException(getMsg("exception.constraints.unknown_type", constraint.toString()));
		}
	}

	@Override
	public boolean remove(Object obj) {
		if (obj instanceof LEQConstraint) {
			return leqConstraintsSet.remove(obj);
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> cs) {
		boolean res = true;
		for (Object obj : cs) {
			res = res && leqConstraintsSet.contains(obj);
		}
		return res;
	}

	@Override
	public boolean addAll(Collection<? extends IConstraint> constraints) {
		boolean res = false;
		for (IConstraint constraint : constraints) {
			res |= add(constraint);
		}
		return res;
	}

	@Override
	public boolean removeAll(Collection<?> cs) {
		boolean res = false;
		for (Object obj : cs) {
			res |= remove(obj);
		}
		return res;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		leqConstraintsSet.clear();
	}
	
	public List<IConstraint> getConstraintsList() {
		List<IConstraint> constraints = new ArrayList<IConstraint>();
		constraints.addAll(leqConstraintsSet);
		return constraints;
	}
	
	public ConstraintSet getTransitiveClosure() {
		ConstraintSet closure = new ConstraintSet(mediator);
		closure.addAll(leqConstraintsSet.getTransitiveClosure());
		return closure;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Constraints: {\n");
		int count = 0;
		for (LEQConstraint constraint : leqConstraintsSet) {
			if (count != 0) sb.append(", \n");
			sb.append("\t" + constraint.toString());
		}
		sb.append("}\nClosure: {\n");
		for (LEQConstraint constraint : leqConstraintsSet.getTransitiveClosure()) {
			if (count != 0) sb.append(", \n");
			sb.append("\t" + constraint.toString());
		}
		sb.append("}");
		return sb.toString();
	}

}

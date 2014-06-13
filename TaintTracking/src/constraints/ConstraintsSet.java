package constraints;

import static constraints.ConstraintsUtils.isLevel;
import static resource.Messages.getMsg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import security.ILevelMediator;
import soot.Local;
import exception.ConstraintUnsupportedException;

public class ConstraintsSet implements Collection<IConstraint> {

	private final LEQConstraintsSet leqConstraintsSet;
	private final ILevelMediator mediator;
	private final Map<IProgramCounterTrigger, Set<IConstraint>> programCounter = new HashMap<IProgramCounterTrigger, Set<IConstraint>>();
	private final Set<IConstraint> writeEffects = new HashSet<IConstraint>();
	
	public void addWriteEffect(IConstraint constraint) {
		this.writeEffects.add(constraint);
	}
	
	public Set<IConstraint> getWriteEffects() {
		return new HashSet<IConstraint>(writeEffects);
	}

	public Map<IProgramCounterTrigger, Set<IConstraint>> getProgramCounter() {
		return new HashMap<IProgramCounterTrigger, Set<IConstraint>>(programCounter);
	}

	public ConstraintsSet(ILevelMediator mediator) {
		this.mediator = mediator;
		this.leqConstraintsSet = new LEQConstraintsSet(mediator);
	}

	public ConstraintsSet(Set<IConstraint> constraints, ILevelMediator mediator) {
		this(mediator);
		addAll(constraints);
	}

	public ConstraintsSet(Set<IConstraint> constraints, Map<IProgramCounterTrigger, Set<IConstraint>> programCounter, ILevelMediator mediator) {
		this(constraints, mediator);
		this.programCounter.putAll(programCounter);
	}
	
	public ConstraintsSet(Set<IConstraint> constraints, Map<IProgramCounterTrigger, Set<IConstraint>> programCounter, Set<IConstraint> writeEffects,ILevelMediator mediator) {
		this(constraints, programCounter, mediator);
		this.writeEffects.addAll(writeEffects);
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
	public boolean addAll(Collection<? extends IConstraint> constraints) {
		boolean res = false;
		for (IConstraint constraint : constraints) {
			res |= add(constraint);
		}
		return res;
	}

	public void addAllProgramCounterConstraints(Map<IProgramCounterTrigger, Set<IConstraint>> map) {
		this.programCounter.putAll(map);
	}
	
	public void addAllWriteEffects(Set<IConstraint> writeEffects) {
		this.writeEffects.addAll(writeEffects);
	}

	public boolean addProgramCounterConstraints(IProgramCounterTrigger trigger, Set<IConstraint> constraints) {
		return null == programCounter.put(trigger, constraints);
	}
	
	

	public void calculateTransitiveClosure() {
		leqConstraintsSet.calculateTransitiveClosure();
	}

	public void calculateTransitiveReduction() {
		leqConstraintsSet.calculateTransitiveReduction();
	}

	public Set<IConstraint> checkForCompletness(ConstraintsSet expectedConstraints) {
		expectedConstraints.calculateTransitiveClosure();
		calculateTransitiveClosure();
		Set<IConstraint> missing = new HashSet<IConstraint>();
		for (IConstraint constraint : getConstraintsSet()) {
			if (!expectedConstraints.contains(constraint)) {
				if (!(isLevel(constraint.getLhs()) && isLevel(constraint.getRhs()))) {
					missing.add(constraint);
				}
			}
		}
		return missing;
	}

	@Override
	public void clear() {
		leqConstraintsSet.clear();
		programCounter.clear();
	}

	@Override
	public boolean contains(Object obj) {
		return leqConstraintsSet.contains(obj);
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
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ConstraintsSet other = (ConstraintsSet) obj;
		if (leqConstraintsSet == null) {
			if (other.leqConstraintsSet != null) return false;
		} else if (!leqConstraintsSet.equals(other.leqConstraintsSet)) return false;
		if (programCounter == null) {
			if (other.programCounter != null) return false;
		} else if (!programCounter.equals(other.programCounter)) return false;
		return true;
	}

	public Set<IConstraint> getAllProgramCounterConstraints() {
		Set<IConstraint> constraints = new HashSet<IConstraint>();
		for (IProgramCounterTrigger trigger : programCounter.keySet()) {
			constraints.addAll(programCounter.get(trigger));
		}
		return constraints;
	}

	public Set<IConstraint> getConstraintsSet() {
		Set<IConstraint> constraints = new HashSet<IConstraint>();
		constraints.addAll(leqConstraintsSet);
		return constraints;
	}

	public Set<IConstraint> getInconsistent() {
		Set<IConstraint> inequal = new HashSet<IConstraint>();
		inequal.addAll(leqConstraintsSet.getInequality());
		return inequal;
	}

	public Set<IProgramCounterTrigger> getProgramCounterTriggers() {
		return programCounter.keySet();
	}

	public ConstraintsSet getTransitiveClosure() {
		ConstraintsSet closure = new ConstraintsSet(getConstraintsSet(), getProgramCounter(), getWriteEffects(), mediator);
		closure.calculateTransitiveClosure();
		return closure;
	}

	public ConstraintsSet getTransitiveReduction() {
		ConstraintsSet reduction = new ConstraintsSet(mediator);
		reduction.addAll(leqConstraintsSet.getTransitiveReductionSet());
		return reduction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leqConstraintsSet == null) ? 0 : leqConstraintsSet.hashCode());
		result = prime * result + ((programCounter == null) ? 0 : programCounter.hashCode());
		return result;
	}

	@Override
	public boolean isEmpty() {
		return leqConstraintsSet.isEmpty();
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
	public boolean remove(Object obj) {
		if (obj instanceof LEQConstraint) {
			return leqConstraintsSet.remove(obj);
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> cs) {
		boolean res = false;
		for (Object obj : cs) {
			res |= remove(obj);
		}
		return res;
	}

	public void removeConstraintsContaining(Local local) {
		calculateTransitiveClosure();
		leqConstraintsSet.removeConstraintsContaining(local);
	}

	public void removeConstraintsContainingLocal() {
		calculateTransitiveClosure();
		leqConstraintsSet.removeConstraintsContainingLocal();
	}
	
	public void removeConstraintsContainingGeneratedLocal() {
		calculateTransitiveClosure();
		leqConstraintsSet.removeConstraintsContainingGeneratedLocal();
	}

	public void removeConstraintsContainingReferencesFor(String... signatures) {
		calculateTransitiveClosure();
		leqConstraintsSet.removeConstraintsContainingReferencesFor(signatures);
	}
	
	public void removeConstraintsContainingReferencesFor(List<String> signatures) {
		calculateTransitiveClosure();
		String[] array = signatures.toArray(new String[] {});
		leqConstraintsSet.removeConstraintsContainingReferencesFor(array);
	}

	public boolean removeProgramCounterConstraintsFor(IProgramCounterTrigger trigger) {
		return null != programCounter.remove(trigger);
	}

	@Override
	public boolean retainAll(Collection<?> cs) {
		return leqConstraintsSet.retainAll(cs);
	}

	@Override
	public int size() {
		return leqConstraintsSet.size();
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
	public String toString() {
		return ConstraintsUtils.constraintsAsString(getConstraintsSet());
	}
	
	public String toBoundariesString() {
		StringBuilder sb = new StringBuilder("{\n");
		int count = 0;
		for (String boundaries : leqConstraintsSet.toBoundaryStrings()) {
			if (0 != count++) sb.append(",\n");
			sb.append("\t" + boundaries);
		}
		sb.append("\n}");
		return sb.toString();
	}

	public void removeConstraintsContainingProgramCounter() {
		calculateTransitiveClosure();
		leqConstraintsSet.removeConstraintsContainingProgramCounter();
	}
	
}



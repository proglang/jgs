package constraints;

import static constraints.ConstraintsUtils.isLevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import security.ILevel;
import security.ILevelMediator;
import soot.Local;

public class ConstraintsSet implements Collection<LEQConstraint> {

	private final Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();
	private final ILevelMediator mediator;
	private final Map<IProgramCounterTrigger, Set<LEQConstraint>> programCounter = new HashMap<IProgramCounterTrigger, Set<LEQConstraint>>();
	private final Set<LEQConstraint> writeEffects = new HashSet<LEQConstraint>();

	public ConstraintsSet(ILevelMediator mediator) {
		this.mediator = mediator;
	}

	public ConstraintsSet(Set<LEQConstraint> constraints, ILevelMediator mediator) {
		this(mediator);
		addAll(constraints);
	}

	public ConstraintsSet(Set<LEQConstraint> constraints, Map<IProgramCounterTrigger, Set<LEQConstraint>> programCounter,
			ILevelMediator mediator) {
		this(constraints, mediator);
		this.programCounter.putAll(programCounter);
	}

	public ConstraintsSet(Set<LEQConstraint> constraints, Map<IProgramCounterTrigger, Set<LEQConstraint>> programCounter,
			Set<LEQConstraint> writeEffects, ILevelMediator mediator) {
		this(constraints, programCounter, mediator);
		this.writeEffects.addAll(writeEffects);
	}

	@Override
	public boolean add(LEQConstraint constraint) {
		IConstraintComponent left = constraint.getLhs();
		IConstraintComponent right = constraint.getRhs();
		if (!left.equals(right)) {
			if (right.equals(mediator.getGreatestLowerBoundLevel()) || left.equals(mediator.getLeastUpperBoundLevel())) {
				LEQConstraint bound = new LEQConstraint(right, left);
				if (!contains(bound)) add(bound);
			}
			return constraints.add(constraint);
		}
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends LEQConstraint> constraints) {
		boolean res = false;
		for (LEQConstraint constraint : constraints) {
			res |= add(constraint);
		}
		return res;
	}

	public void addAllProgramCounterConstraints(Map<IProgramCounterTrigger, Set<LEQConstraint>> map) {
		this.programCounter.putAll(map);
	}

	public void addAllWriteEffects(Set<LEQConstraint> writeEffects) {
		this.writeEffects.addAll(writeEffects);
	}

	public boolean addProgramCounterConstraints(IProgramCounterTrigger trigger, Set<LEQConstraint> constraints) {
		return null == programCounter.put(trigger, constraints);
	}

	public void addWriteEffect(LEQConstraint constraint) {
		this.writeEffects.add(constraint);
	}

	public void calculateTransitiveClosure() {
		Set<LEQConstraint> closure = calculateTransitiveClosure(constraints);
		constraints.clear();
		constraints.addAll(closure);
	}

	private Set<LEQConstraint> calculateTransitiveClosure(Set<LEQConstraint> set) {
		set = new HashSet<LEQConstraint>(set);
		int size = -1;
		while (size != set.size()) {
			size = set.size();
			Set<LEQConstraint> additional = new HashSet<LEQConstraint>();
			for (LEQConstraint leftConstraint : set) {
				for (LEQConstraint rightConstraint : set) {
					if (leftConstraint.getRhs().equals(rightConstraint.getLhs()) && !leftConstraint.getLhs().equals(rightConstraint.getRhs())) {
						additional.add(new LEQConstraint(leftConstraint.getLhs(), rightConstraint.getRhs()));
					}
				}
			}
			set.addAll(additional);
		}
		return set;
	}

	public void calculateTransitiveReduction() {
		Set<LEQConstraint> reduction = calculateTransitiveReduction(constraints);
		constraints.clear();
		constraints.addAll(reduction);
	}

	private Set<LEQConstraint> calculateTransitiveReduction(Set<LEQConstraint> original) {
		Set<LEQConstraint> closure = calculateTransitiveClosure(original);
		Set<LEQConstraint> reduction = new HashSet<LEQConstraint>(closure);
		int size = -1;
		while (size != reduction.size()) {
			size = reduction.size();
			for (LEQConstraint constraint : reduction) {
				Set<LEQConstraint> temp = new HashSet<LEQConstraint>(reduction);
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
	public void clear() {
		constraints.clear();
		programCounter.clear();
		writeEffects.clear();
	}

	@Override
	public boolean contains(Object obj) {
		return constraints.contains(obj);
	}

	@Override
	public boolean containsAll(Collection<?> cs) {
		return constraints.containsAll(cs);
	}

	public Set<LEQConstraint> getAllProgramCounterConstraints() {
		Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();
		for (IProgramCounterTrigger trigger : programCounter.keySet()) {
			constraints.addAll(programCounter.get(trigger));
		}
		return constraints;
	}

	public Set<LEQConstraint> getConstraintsSet() {
		return new HashSet<LEQConstraint>(constraints);
	}

	public Set<LEQConstraint> getInconsistent() {
		Set<LEQConstraint> inequal = new HashSet<LEQConstraint>();
		for (LEQConstraint constraint : getConstraintsSet()) {
			IConstraintComponent left = constraint.getLhs();
			IConstraintComponent right = constraint.getRhs();
			if (isLevel(left) && isLevel(right)) {
				ILevel l1 = (ILevel) left;
				ILevel l2 = (ILevel) right;
				if (!mediator.isWeaker(l1, l2)) {
					inequal.add(constraint);
				}
			}
		}
		return inequal;
	}

	public Map<IProgramCounterTrigger, Set<LEQConstraint>> getProgramCounter() {
		return new HashMap<IProgramCounterTrigger, Set<LEQConstraint>>(programCounter);
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
		ConstraintsSet reduction = new ConstraintsSet(getConstraintsSet(), getProgramCounter(), getWriteEffects(), mediator);
		reduction.calculateTransitiveReduction();
		return reduction;
	}

	public Set<LEQConstraint> getWriteEffects() {
		return new HashSet<LEQConstraint>(writeEffects);
	}

	@Override
	public boolean isEmpty() {
		return constraints.isEmpty();
	}

	@Override
	public Iterator<LEQConstraint> iterator() {
		return constraints.iterator();
	}

	@Override
	public boolean remove(Object obj) {
		return constraints.remove(obj);
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
		for (LEQConstraint constraint : getConstraintsSet()) {
			if (constraint.containsComponent(new ConstraintLocal(local))) remove(constraint);
		}
	}

	public void removeConstraintsContainingGeneratedLocal() {
		calculateTransitiveClosure();
		for (LEQConstraint constraint : getConstraintsSet()) {
			if (constraint.containsGeneratedLocal()) remove(constraint);
		}
	}

	public void removeConstraintsContainingLocal() {
		calculateTransitiveClosure();
		for (LEQConstraint constraint : getConstraintsSet()) {
			if (constraint.containsLocal()) remove(constraint);
		}
	}

	public void removeConstraintsContainingProgramCounter() {
		calculateTransitiveClosure();
		for (LEQConstraint constraint : getConstraintsSet()) {
			if (constraint.containsProgramCounterReference()) remove(constraint);
		}
	}

	public void removeConstraintsContainingReferencesFor(List<String> signatures) {
		calculateTransitiveClosure();
		String[] array = signatures.toArray(new String[] {});
		removeConstraintsContainingReferencesFor(array);
	}

	public void removeConstraintsContainingReferencesFor(String... signatures) {
		calculateTransitiveClosure();
		for (String signature : signatures) {
			for (LEQConstraint constraint : getConstraintsSet()) {
				if (constraint.containsReturnReferenceFor(signature) || constraint.containsParameterReferenceFor(signature)
						|| constraint.containsProgramCounterReferenceFor(signature)) {
					remove(constraint);
				}
			}
		}
	}

	public boolean removeProgramCounterConstraintsFor(IProgramCounterTrigger trigger) {
		return null != programCounter.remove(trigger);
	}

	@Override
	public boolean retainAll(Collection<?> cs) {
		List<LEQConstraint> removing = new ArrayList<LEQConstraint>();
		for (LEQConstraint c : constraints) {
			if (!cs.contains(c)) {
				removing.add(c);
			}
		}
		return removeAll(removing);
	}

	@Override
	public int size() {
		return constraints.size();
	}

	@Override
	public Object[] toArray() {
		return constraints.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return constraints.toArray(a);
	}

	public String toBoundariesString() {
		StringBuilder sb = new StringBuilder("{\n");
		int count = 0;
		for (String boundaries : toBoundaryStrings()) {
			if (0 != count++) sb.append(",\n");
			sb.append("\t" + boundaries);
		}
		sb.append("\n}");
		return sb.toString();
	}

	private List<String> toBoundaryStrings() {
		List<String> result = new ArrayList<String>();
		Map<IConstraintComponentVar, Set<ILevel>> lower = new HashMap<IConstraintComponentVar, Set<ILevel>>();
		Map<IConstraintComponentVar, Set<ILevel>> upper = new HashMap<IConstraintComponentVar, Set<ILevel>>();
		Set<IConstraintComponentVar> vars = new HashSet<IConstraintComponentVar>();
		for (LEQConstraint constraint : getConstraintsSet()) {
			IConstraintComponent left = constraint.getLhs();
			IConstraintComponent right = constraint.getRhs();
			if (isLevel(left) && right instanceof IConstraintComponentVar) {
				ILevel level = (ILevel) left;
				IConstraintComponentVar var = (IConstraintComponentVar) right;
				if (!lower.containsKey(var)) lower.put(var, new HashSet<ILevel>());
				if (!upper.containsKey(var)) upper.put(var, new HashSet<ILevel>());
				vars.add(var);
				lower.get(var).add(level);
			}
			if (isLevel(right) && left instanceof IConstraintComponentVar) {
				ILevel level = (ILevel) right;
				IConstraintComponentVar var = (IConstraintComponentVar) left;
				if (!lower.containsKey(var)) lower.put(var, new HashSet<ILevel>());
				if (!upper.containsKey(var)) upper.put(var, new HashSet<ILevel>());
				vars.add(var);
				upper.get(var).add(level);
			}
		}
		for (IConstraintComponentVar var : vars) {
			String string = "";
			Set<ILevel> lowerLevels = lower.get(var);
			Set<ILevel> upperLevels = upper.get(var);
			try {
				if (lowerLevels.size() > 0) string += mediator.getLeastUpperBoundLevelOf(new ArrayList<ILevel>(lowerLevels)).toString() + " <= ";
				string += var.toString();
				if (upperLevels.size() > 0)
					string += " <= " + mediator.getGreatestLowerBoundLevelOf(new ArrayList<ILevel>(upperLevels)).toString();
				result.add(string);
			} catch (Exception e) {
				System.err.println("error for " + var.toString() + " up: " + upperLevels.size() + " | low: " + lowerLevels.size());
			}

		}
		return result;
	}

	@Override
	public String toString() {
		return ConstraintsUtils.constraintsAsString(getConstraintsSet());
	}

}

package analysis;

import static resource.Messages.getMsg;

import java.util.HashSet;
import java.util.Set;

import model.AnalyzedMethodEnvironment;
import soot.Local;
import soot.SootField;
import soot.Value;
import constraints.ConstraintsSet;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;
import exception.SwitchException;
import extractor.UsedObjectStore;

public abstract class ASecurityConstraintValueSwitch extends SecurityConstraintSwitch {
	
	private final Set<IConstraintComponent> writeComponents = new HashSet<IConstraintComponent>();
	private final Set<IConstraintComponent> readComponents = new HashSet<IConstraintComponent>();
	private final Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();
	
	protected SootField field = null;
	protected Local local = null;
	
	protected boolean isField() {
		return field != null;
	}
	
	protected SootField getField() {
		return field;
	}
	
	protected boolean isLocal() {
		return local != null;
	}
	
	protected Local getLocal() {
		return local;
	}
	
	
	protected void addWriteComponent(IConstraintComponent component) {
		this.writeComponents.add(component);
	}

	protected void addWriteComponents(Set<IConstraintComponent> components) {
		this.writeComponents.addAll(components);
	}
	
	protected final Set<IConstraintComponent> getWriteComponents() {
		return writeComponents;
	}

	protected void addReadComponent(IConstraintComponent component) {
		this.readComponents.add(component);
	}
	
	protected void addReadComponents(Set<IConstraintComponent> components) {
		this.readComponents.addAll(components);
	}
	
	protected final Set<IConstraintComponent> getReadComponents() {
		return readComponents;
	}
	
	protected void addConstraint(LEQConstraint constraint) {
		this.constraints.add(constraint);
	}
	
	protected void addConstraints(Set<LEQConstraint> constraints) {
		this.constraints.addAll(constraints);
	}

	protected final Set<LEQConstraint> getConstraints() {
		return constraints;
	}
	
	protected void throwInvalidWriteException(Value value) {
		throw new SwitchException(getMsg("exception.analysis.switch.invalid_write", value.toString()));
	}

	protected ASecurityConstraintValueSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, ConstraintsSet in,
			ConstraintsSet out) {
		super(methodEnvironment, store, in, out);
	}
	
}

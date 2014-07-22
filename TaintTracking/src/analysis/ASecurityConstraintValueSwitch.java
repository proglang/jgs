package analysis;

import static resource.Messages.getMsg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.AnalyzedMethodEnvironment;
import soot.Local;
import soot.SootField;
import soot.Value;
import constraints.ConstraintsSet;
import constraints.IComponent;
import constraints.LEQConstraint;
import exception.SwitchException;
import extractor.UsedObjectStore;

public abstract class ASecurityConstraintValueSwitch extends SecurityConstraintSwitch {
	
	private final Set<IComponent> writeComponents = new HashSet<IComponent>();
	private final Set<IComponent> readComponents = new HashSet<IComponent>();
	private final Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();
	private final List<IComponent> equalComponents = new ArrayList<IComponent>();
	private int dimension = 0;
	
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
	
	
	protected void addWriteComponent(IComponent component) {
		this.writeComponents.add(component);
	}

	protected void addWriteComponents(Set<IComponent> components) {
		this.writeComponents.addAll(components);
	}
	
	protected final Set<IComponent> getWriteComponents() {
		return writeComponents;
	}

	protected void addReadComponent(IComponent component) {
		this.readComponents.add(component);
	}
	
	protected void addReadComponents(Set<IComponent> components) {
		this.readComponents.addAll(components);
	}
	
	protected final Set<IComponent> getReadComponents() {
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

	protected List<IComponent> getEqualComponents() {
		return equalComponents;
	}
	
	protected void appendEqualComponent(IComponent component) {
		equalComponents.add(component);
	}

	protected int getDimension() {
		return dimension;
	}

	protected void setDimension(int dimension) {
		this.dimension = dimension;
	}
	
}

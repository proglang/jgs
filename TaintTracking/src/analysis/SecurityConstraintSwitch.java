package analysis;

import static resource.Messages.getMsg;
import soot.Value;
import model.AnalyzedMethodEnvironment;
import constraints.ConstraintsSet;
import exception.SwitchException;
import extractor.UsedObjectStore;

public class SecurityConstraintSwitch extends SecuritySwitch<ConstraintsSet> {

	protected SecurityConstraintSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, ConstraintsSet in, ConstraintsSet out) {
		super(methodEnvironment, store, in, out);
	}

	protected void throwNotImplementedException(Class<?> klass, String content) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", content, getSourceLine(), klass.getSimpleName(), this
				.getClass().getSimpleName()));
	}
	
	protected void throwUnknownObjectException(Object object) {
		throw new SwitchException(getMsg("exception.analysis.switch.unknown_object", object.toString(), this.getClass().getSimpleName()));
	}
	
	protected SecurityConstraintValueSwitch getValueSwitch(Value value) {
		SecurityConstraintValueSwitch valueSwitch = new SecurityConstraintValueSwitch(getAnalyzedEnvironment(), getStore(), getIn(), getOut());
		value.apply(valueSwitch);
		return valueSwitch;
	}
	
	protected String getAnalyzedSignature() {
		return getAnalyzedMethod().getSignature();
	}
	
}

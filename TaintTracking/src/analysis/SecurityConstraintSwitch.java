package analysis;

import static resource.Messages.getMsg;
import model.AnalyzedMethodEnvironment;
import constraints.Constraints;
import exception.SwitchException;
import extractor.UsedObjectStore;

public class SecurityConstraintSwitch extends SecuritySwitch<Constraints> {

	protected SecurityConstraintSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, Constraints in, Constraints out) {
		super(methodEnvironment, store, in, out);
	}

	protected void throwNotImplementedException(Class<?> klass, String content) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", content, getSrcLn(), klass.getSimpleName(), this
				.getClass().getSimpleName()));
	}
	
	protected void throwUnknownObjectException(Object object) {
		throw new SwitchException(getMsg("exception.analysis.switch.unknown_object", object.toString(), this.getClass().getSimpleName()));
	}
	
	protected SecurityConstraintValueSwitch getNewValueSwitch() {
		return new SecurityConstraintValueSwitch(analyzedMethodEnvironment, store, in, out);
	}
	
}

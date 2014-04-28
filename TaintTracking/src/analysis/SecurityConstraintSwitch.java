package analysis;

import model.AnalyzedMethodEnvironment;
import constraints.Constraints;
import extractor.UsedObjectStore;

public class SecurityConstraintSwitch extends SecuritySwitch<Constraints> {

	protected SecurityConstraintSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, Constraints in, Constraints out) {
		super(methodEnvironment, store, in, out);
	}

}

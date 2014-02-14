package securityNewNew;


import logging.SecurityLogger;
import exception.SootException.SecurityLevelException;
import securityNewNew.ALevelDefinitionChecker;

public class DefinitionChecker extends ALevelDefinitionChecker {

	protected DefinitionChecker(ILevelDefinition implementation,
			SecurityLogger logger, boolean logging, boolean throwException)
			throws SecurityLevelException {
		super(implementation, logger, logging, throwException);
		
	}
	
	public DefinitionChecker(ILevelDefinition implementation) {
		super(implementation);
	}

	@Override
	protected boolean checkAdditionalValidityOfImplementation() {
		return true;
	}

}

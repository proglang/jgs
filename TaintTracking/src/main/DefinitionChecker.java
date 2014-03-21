package main;

import logging.AnalysisLog;
import exception.SootException.SecurityLevelException;
import security.ALevelDefinitionChecker;
import security.ILevelDefinition;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class DefinitionChecker extends ALevelDefinitionChecker {

	/**
	 * DOC
	 * 
	 * @param implementation
	 */
	public DefinitionChecker(ILevelDefinition implementation) {
		super(implementation);
	}

	/**
	 * DOC
	 * 
	 * @param implementation
	 * @param logger
	 * @param logging
	 * @param throwException
	 * @throws SecurityLevelException
	 */
	protected DefinitionChecker(ILevelDefinition implementation, AnalysisLog logger, boolean logging, boolean throwException)
			throws SecurityLevelException {
		super(implementation, logger, logging, throwException);

	}

	/**
	 * DOC
	 * 
	 * @return 
	 * 
	 * @see ALevelDefinitionChecker#checkAdditionalValidityOfImplementation()
	 */
	@Override
	protected boolean checkAdditionalValidityOfImplementation() {
		return true;
	}

}

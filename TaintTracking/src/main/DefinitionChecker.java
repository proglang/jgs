package main;

import logging.AnalysisLog;
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
	 */
	protected DefinitionChecker(ILevelDefinition implementation, AnalysisLog logger, boolean logging) {
		super(implementation, logger, logging);
	}

	/**
	 * DOC
	 * 
	 * @return 
	 * 
	 * @see ALevelDefinitionChecker#checkAdditionalValidityOfImplementation()
	 */
	@Override
	protected void checkAdditionalValidityOfImplementation() {
		return;
	}

}

package analyzer.level2;

import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import utils.exceptions.IllegalFlowException;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;


public class HandleStmtUtils {
	
	Logger logger = L2Logger.getLogger();
	private LocalMap localmap;
	private static ObjectMap objectmap;

	private final String ASSIGNMENT_ERROR_MESSAGE = "Found an illegal flow to ";
	
	protected HandleStmtUtils(LocalMap lm, ObjectMap om) {
		this.localmap = lm;
		if (lm == null) {
			throw new InternalAnalyzerException("LocalMap initialization has failed.");
		}
		HandleStmtUtils.objectmap = om;		
		if (om == null) {
			new InternalAnalyzerException("ObjectMap initialization has failed.");
		}
	}
	
	/**
	 * If the result of the check for localPC or globalPC is negative (that means,
	 * that a LOW variable is written in a HIGH context), then
	 * the analysis aborts with an {@link IllegalFlowException}.
	 * @param sink the variable which is written in a HIGH context.
	 */
	protected void abort(String message) {
		throw new IllegalFlowException(message);
	}
	
	//
	// PC check operations
	//

	/**
	 * 
	 * @param signature
	 * @return
	 */
	protected void checkLocalPC(String signature) {		
		if (localmap == null) {
			throw new InternalAnalyzerException("LocalMap is not assigned");
		}
		SecurityLevel level = localmap.getLevel(signature);
		SecurityLevel lpc = localmap.getLocalPC();
		logger.log(Level.INFO, "Check if level of local {0} ({1}) >= lpc ({2})", 
				new Object[] {signature, level, lpc });
		boolean res = true;
		if (level == SecurityLevel.LOW && lpc == SecurityLevel.HIGH) {
			res = false;
		}

		if (!res) {
			abort(ASSIGNMENT_ERROR_MESSAGE + signature);
		}
	}
	
	/**
	 * @param o
	 * @param signature
	 * @return
	 */
	protected void checkGlobalPC(Object o, String signature) {
		logger.log(Level.INFO, "Check if level of field {0} ({1}) >= gpc ({1})",
				new Object[] {
				 signature, objectmap.getFieldLevel(o, signature), objectmap.getGlobalPC()
				});
		boolean res = true;
		if (objectmap.getFieldLevel(o, signature) == SecurityLevel.LOW 
				&& objectmap.getGlobalPC() == SecurityLevel.HIGH) {
			res = false;
		}
		if (!res) {
			abort(ASSIGNMENT_ERROR_MESSAGE + signature);
		}	
	}
	
	/**
	 * @param o
	 * @param field
	 * @param localForObject
	 * @param localForIndex
	 * @return
	 */
	protected boolean checkArrayWithGlobalPC(Object o, String field,
			String localForObject, String localForIndex) {
		boolean res = true;
		SecurityLevel localsAndGPC = joinWithGPC(joinLocals(localForObject, localForIndex));
		if (objectmap.getFieldLevel(o, field) == SecurityLevel.LOW 
				&& localsAndGPC == SecurityLevel.HIGH) {
			res = false;
		}
		if (!res) {
			abort(ASSIGNMENT_ERROR_MESSAGE + signature);
		}	
	}
	
	/**
	 * @param o
	 * @param field
	 * @param localForObject
	 * @return
	 */
	protected void checkArrayWithGlobalPC(Object o, String field,
			String localForObject) {
		boolean res = true;
		SecurityLevel localsAndGPC = joinWithGPC(localmap.getLevel(localForObject));
		if (objectmap.getFieldLevel(o, field) == SecurityLevel.LOW 
				&& localsAndGPC == SecurityLevel.HIGH) {
			res = false;
		}
		if (!res) {
			abort(ASSIGNMENT_ERROR_MESSAGE + signature);
		}
	}

	
	//
	// Join operations
	//
	
	/**
	 * Joins the Levels of the given locals and the local pc
	 * @param stringList 
	 * @return
	 */
	protected SecurityLevel joinLocals(String... stringList) {
		SecurityLevel result = SecurityLevel.LOW;
		for (String op: stringList) {
			if (localmap.getLevel(op) == SecurityLevel.HIGH) {
				result = SecurityLevel.HIGH;
			}
		}
		return result;
	}
	
	/**
	 * @param object
	 * @return
	 */
	protected SecurityLevel joinWithLPC(Object object) {
		SecurityLevel result = SecurityLevel.LOW;
		if (localmap.getLocalPC() == SecurityLevel.HIGH || object == SecurityLevel.HIGH) {
			result = SecurityLevel.HIGH;
		}
		return result;
	}
	
	/**
	 * @param securityLevel
	 * @return
	 */
	protected SecurityLevel joinWithGPC(Object securityLevel) {
		SecurityLevel result = SecurityLevel.LOW;
		if (objectmap.getGlobalPC() == SecurityLevel.HIGH || securityLevel == SecurityLevel.HIGH) {
			result = SecurityLevel.HIGH;
		}
		return result;
	}
	
	/**
	 * @param levels
	 * @return
	 */
	protected SecurityLevel joinLevels(Object... levels) {
		// TODO !!!!!!!!!!!!!!! lub
		SecurityLevel res = SecurityLevel.LOW;
		for (SecurityLevel l: levels) {
			if ( l == SecurityLevel.HIGH) {
				res = SecurityLevel.HIGH;
			}
		}
		return res;
	}
	
	/**
	 * @param signature
	 */
	protected void checkIfLocalExists(String signature) {
		if (!localmap.contains(signature)) {
			throw new InternalAnalyzerException("Missing local " 
				+ signature + " in LocalMap");
		}
	}
	
	protected void checkThatLocalDoesNotExist(String signature) {
		if (localmap.contains(signature)) {
			throw new InternalAnalyzerException("Trying to add local " + signature 
				+ " to LocalMap, but it is already in the LocalMap.");
		}
	}

	protected void checkIfObjectExists(Object o) {
		if (!objectmap.containsObject(o)) {
			throw new InternalAnalyzerException("Missing object " 
				+ o + " in ObjectMap");
		}
	}
	
	protected void checkIfFieldExists(Object o, String signature) {
		if (!objectmap.containsField(o, signature)) {
			throw new InternalAnalyzerException("Missing field " 
				+ signature + " in ObjectMap");
		}
	}
	

}

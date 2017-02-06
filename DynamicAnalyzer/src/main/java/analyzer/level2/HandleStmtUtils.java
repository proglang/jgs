package analyzer.level2;

import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import utils.exceptions.IllegalFlowException;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L2Logger;
import utils.staticResults.superfluousInstrumentation.ControllerFactory;
import utils.staticResults.superfluousInstrumentation.PassivController;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HandleStmtUtils {

    PassivController superfluousInstrumentationCatcher;

	Logger logger = L2Logger.getLogger();
	private LocalMap localmap;
	private static ObjectMap objectmap;

	private final String ASSIGNMENT_ERROR_MESSAGE = "Found an illegal flow to ";
	
	protected HandleStmtUtils(LocalMap lm, ObjectMap om, boolean isActive) {
		this.localmap = lm;
		superfluousInstrumentationCatcher = ControllerFactory.returnSuperfluousInstrumentationController(isActive);
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
	 */
	protected void abort(String message) {
		throw new IllegalFlowException(message);
	}
	
	//
	// PC check operations
	//

	/**
	 * NSU policy: For initialized locals, check if level of given local is greater 
	 * than localPC. If it's not, throw IllegalFlowException
	 * @param signature signature of the local
	 */
	protected void checkLocalPC(String signature) {
		if (localmap == null) {
			throw new InternalAnalyzerException("LocalMap is not assigned");
		}
		//check if local is initialized
		if (!localmap.checkIfInitialized(signature)) {
			logger.log(Level.INFO, "Local {0} has not yet been initialized", signature);
			localmap.initializeLocal(signature);
			return;
		}
		
		// the following check must only be executed if local is initialised
		Object level = localmap.getLevel(signature); 
		Object lpc = localmap.getLocalPC();
		logger.log(Level.INFO, "Check if level of local {0} ({1}) >= lpc ({2}) --- checkLocalPC", 
				new Object[] {signature, level, lpc });
		superfluousInstrumentationCatcher.abortIfActive();
		if (!SecurityLevel.le(lpc, level)) {
			abort(ASSIGNMENT_ERROR_MESSAGE + signature);
		}
	}
	
	/**
	 * Check if level of given field is greater than globalPC.
	 * @param signature signature of the field
	 */
	protected void checkGlobalPC(Object object, String signature) {
		logger.log(Level.INFO, "Check if level of field {0} ({1}) >= gpc ({1})",
				new Object[] {
				 signature, objectmap.getFieldLevel(object, signature),
				 objectmap.getGlobalPC()
				});
		Object fieldLevel = objectmap.getFieldLevel(object, signature);
		Object globalPC = objectmap.getGlobalPC();
		if (!SecurityLevel.le(globalPC, fieldLevel)) {
			abort(ASSIGNMENT_ERROR_MESSAGE + object.toString() + signature);
		}	
		
		
	}
	
	/**
	 * Check if level of given array field is greater than globalPC.
	 * This method is for the case, that the index is stored in a variable.
	 */
	protected void checkArrayWithGlobalPC(Object object, String signature,
			String localForObject, String localForIndex) {
		logger.log(Level.INFO, "Check if level of array-field {0} ({1}) >= gpc ({1})",
				new Object[] {
				 signature, objectmap.getFieldLevel(object, signature),
				 objectmap.getGlobalPC()
				});
		Object localsAndGPC = joinWithGPC(joinLocals(localForObject, localForIndex));
		Object fieldLevel = objectmap.getFieldLevel(object, signature);
		if (!SecurityLevel.le(localsAndGPC, fieldLevel)) {
			abort(ASSIGNMENT_ERROR_MESSAGE + signature);
		}	
	}
	
	/**
	 * Check if level of given array field is greater than globalPC.
	 * This method is for the case, that the index is given as a constant.
	 */
	protected void checkArrayWithGlobalPC(Object object, String signature,
			String localForObject) {
		logger.log(Level.INFO, "Check if level of array-field {0} ({1}) >= gpc ({1})",
					new Object[] {
					 signature, objectmap.getFieldLevel(object, signature),
					 objectmap.getGlobalPC()
					});
		Object localsAndGPC = joinWithGPC(localmap.getLevel(localForObject));
		Object fieldLevel = objectmap.getFieldLevel(object, signature);
		if (!SecurityLevel.le(localsAndGPC, fieldLevel)) {
			abort(ASSIGNMENT_ERROR_MESSAGE + signature);
		}
	}

	
	//
	// Join operations
	//
	
	/**
	 * Joins the Levels of the given locals and the local pc.
	 * @param stringList list of singatures of the locals
	 * @return the new security-level
	 */
	protected Object joinLocals(String... stringList) {
		Object result = SecurityLevel.bottom();
		for (String op: stringList) {
			result = SecurityLevel.lub(result, localmap.getLevel(op));
		}
		return result;
	}
	
	/**
	 * Join given security-level with localPC.
	 * @param securityLevel security-level - is retrieved via objectmap.getAssignmentLevel()
	 * @return new security-level
	 */
	protected Object joinWithLPC(Object securityLevel) {
		Object localPC = localmap.getLocalPC();
		Object result = SecurityLevel.lub(localPC, securityLevel);
		logger.log(Level.INFO, "Local PC is {0}, security Level/assignementLevel is {1}, results in {2}",
				new Object[] { localPC, securityLevel, result });
		return result;
	}
	

	protected Object joinWithGPC(Object securityLevel) {
		Object globalPC = objectmap.getGlobalPC();
		Object result = SecurityLevel.lub(globalPC, securityLevel);
		return result;
	}
	

	protected Object joinLevels(Object... levels) {
		Object res = SecurityLevel.bottom();
		for (Object securityLevel: levels) {
			res = SecurityLevel.lub(res, securityLevel);
		}
		return res;
	}
	

	/**
	 * Check if given local exists in localmap. 
	 * Throw InternalAanalyzerException if not.
	 * @param signature
	 */
	protected void checkIfLocalExists(String signature) {
		if (!localmap.contains(signature)) {
			throw new InternalAnalyzerException("Missing local " 
				+ signature + " in LocalMap");
		}
	}
	
	/**
	 * Called when trying to add a new local to localmap via addLocal(String signature)
	 * Throws InternalAnalyzerException if already present
	 * @param signature
	 */
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

package analyzer.level2;

import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.LowMediumHigh;
import analyzer.level2.storage.ObjectMap;
import util.exceptions.IFCError;
import util.exceptions.InternalAnalyzerException;
import util.exceptions.NSUError;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HandleStmtUtils {

	PassivController controller;

	Logger logger = L2Logger.getLogger();
	private LocalMap localmap;
	private static ObjectMap objectmap;

	public final static String NSU_ERROR_MESSAGE = "Sensitive update to ";

	protected HandleStmtUtils(LocalMap lm, ObjectMap om, PassivController controller) {
		this.localmap = lm;
		this.controller = controller;
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
	 * the analysis aborts with an {@link IFCError}.
	 */
	protected void abort(IFCError e) {
		throw e;
	}

	//
	// PC check operations
	//

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
		if (!CurrentSecurityDomain.le(localsAndGPC, fieldLevel)) {
			abort(new NSUError(NSU_ERROR_MESSAGE + signature));
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
		if (!CurrentSecurityDomain.le(localsAndGPC, fieldLevel)) {
			abort(new NSUError(NSU_ERROR_MESSAGE + signature));
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
		Object result = CurrentSecurityDomain.bottom();
		for (String op: stringList) {
			result = CurrentSecurityDomain.lub(result, localmap.getLevel(op));
		}
		return result;
	}

	protected Object joinLocalLevel(String level) {
		Object result = CurrentSecurityDomain.bottom();
		result = CurrentSecurityDomain.lub(result, CurrentSecurityDomain.readLevel(level));
		return result;
	}

	/**
	 * Join given security-level with localPC.
	 * @param securityLevel security-level - is retrieved via objectmap.getAssignmentLevel()
	 * @return new security-level
	 */
	protected Object joinWithLPC(Object securityLevel) {
		Object localPC = localmap.getLocalPC();
		Object result = CurrentSecurityDomain.lub(localPC, securityLevel);
		logger.log(Level.INFO, "Local PC is {0}, security Level/assignementLevel is {1}, results in {2}",
				new Object[] { localPC, securityLevel, result });
		return result;
	}


	protected Object joinWithGPC(Object securityLevel) {
		Object globalPC = objectmap.getGlobalPC();
		Object result = CurrentSecurityDomain.lub(globalPC, securityLevel);
		return result;
	}


	protected Object joinLevels(Object... levels) {
		Object res = CurrentSecurityDomain.bottom();
		for (Object securityLevel: levels) {
			res = CurrentSecurityDomain.lub(res, securityLevel);
		}
		return res;
	}

	/**
	 * Called when trying to add a new local to localmap via addLocal(String signature)
	 * Throws InternalAnalyzerException if already present
	 * @param signature
	 */
	protected void checkThatLocalDoesNotExist(String signature) {
		if (localmap.isTracked(signature)) {
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

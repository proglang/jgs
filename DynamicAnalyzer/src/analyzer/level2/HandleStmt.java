package analyzer.level2;

import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import utils.exceptions.IllegalFlowException;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L2Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class with all neccessary methods to perform an analysis.
 * 
 * @author koenigr
 */
public class HandleStmt {

	private static final Logger logger = L2Logger.getLogger();

	private LocalMap lm;
	private static ObjectMap om;
	private HandleStmtUtils hsu;

	/**
	 * This must be called at the beginning of every method in the analyzed
	 * code. It creates a new LocalMap for the method and adjusts the
	 * {@link SecurityLevel} of the globalPC
	 */
	public HandleStmt() {
		logger.log(Level.INFO, "Create new HandleStmt instance");
		lm = new LocalMap();
		om = ObjectMap.getInstance();
		hsu = new HandleStmtUtils(lm, om);

		om.pushGlobalPC(hsu.joinLevels(om.getGlobalPC(), lm.getLocalPC()));
	}

	/**
	 * This method must be called just once at the beginning of the main method.
	 * It triggers the setup of the logger.
	 */
	public static void init() {
		logger.log(Level.INFO, "Init HS");
		try {
			L2Logger.setup();
		} catch (IOException e) {
			logger.warning("Set up of logger failed");
			e.printStackTrace();
		}
		if (om == null) {
			om = ObjectMap.getInstance();
		}
		om.flush();
		om.clearAssignmentLevel();
	}

	/**
	 * This must be called at the end of every method in the analyzed code. It
	 * resets the globalPC to its initial value
	 */
	public void close() {
		logger.log(Level.INFO, "Close HS");
		om.popGlobalPC();
		lm.checkisLPCStackEmpty();
	}

	/**
	 * If the result of the check for LPC or GPC is negative (that means,
	 * that a LOW variable is written in a HIGH context), then
	 * the analysis aborts with an {@link IllegalFlowException} and a System.exit.
	 * @param sink the variable which is written in a HIGH context.
	 */
	protected void abort(String sink) {
		new IllegalFlowException(
				"System.exit because of illegal flow to " + sink);
		System.exit(0);
	}

	/**
	 * Set the return level.
	 * @param l The securitylevel of actual return-statement.
	 * @return The new returnlevel.
	 */
	protected SecurityLevel setActualReturnLevel(SecurityLevel l) {
		return om.setActualReturnLevel(l);
	}

	/**
	 * @return The securitylevel of the last return-statement.
	 */
	protected SecurityLevel getActualReturnLevel() {
		return om.getActualReturnLevel();
	}

	/**
	 * Add an object to the ObjectMap.
	 * @param o object
	 */
	public void addObjectToObjectMap(Object o) {
		logger.log(Level.INFO, "Insert Object {0} to ObjectMap", o);
		om.insertNewObject(o);
	}

	/**
	 * Add a field of an object to ObjectMap.
	 * @param o an object
	 * @param signature signature of the field of the object
	 * @return SecurityLevel of the newly set field
	 */
	public SecurityLevel addFieldToObjectMap(Object o, String signature) {
		logger.log(Level.INFO, "Add Field {0} to object {1}", new Object[] {
		    signature, o });
		return om.setField(o, signature);
	}

	/**
	 * Add an array to ObjectMap.
	 * @param a array
	 */
	public void addArrayToObjectMap(Object[] a) {

		logger.info("Array length " + a.length);

		logger.log(Level.INFO, "Add Array {0} to ObjectMap", a.toString());

		addObjectToObjectMap(a);
		for (int i = 0; i < a.length; i++) {
			addFieldToObjectMap(a, Integer.toString(i));
		}

		if (!containsObjectInObjectMap(a)) {
			new InternalAnalyzerException("Add Object " + a
					+ " to ObjectMap failed");
		}
	}

	/**
	 * Check if an object is contained in ObjectMap.
	 * @param o object
	 * @return true, if object is found in ObjectMap
	 */
	protected boolean containsObjectInObjectMap(Object o) {
		return om.containsObject(o);
	}

	/**
	 * Check if a field is contained in ObjectMap.
	 * @param o object of the field
	 * @param signature signature of the field
	 * @return true, if field is found in ObjectMap
	 */
	protected boolean containsFieldInObjectMap(Object o, String signature) {		
		if (!om.containsObject(o)) {
			new InternalAnalyzerException("Missing object " + o + " in ObjectMap");
		}
		return om.containsField(o, signature);
	}

	/**
	 * Get the number of elements in ObjectMap.
	 * @return number of elements
	 */
	protected int getNumberOfElements() {
		return om.getNumberOfElements();
	}

	/**
	 * Get the number of fields for an object in ObjectMap.
	 * @param o object
	 * @return number of fields for given object
	 */
	protected int getNumberOfFields(Object o) {
		if (!om.containsObject(o)) {
			new InternalAnalyzerException("Missing object " + o + " in ObjectMap");
		}
		return om.getNumberOfFields(o);
	}

	/**
	 * @param o
	 * @param signature
	 * @return
	 */
	protected SecurityLevel getFieldLevel(Object o, String signature) {
		return om.getFieldLevel(o, signature);
	}

	/**
	 * @param o
	 * @param signature
	 */
	public void makeFieldHigh(Object o, String signature) {
		logger.log(Level.INFO, "Set SecurityLevel of field {0} to HIGH",
				signature);
		om.setField(o, signature, SecurityLevel.HIGH);
	}

	/**
	 * @param o
	 * @param signature
	 */
	public void makeFieldLow(Object o, String signature) {
		logger.log(Level.INFO, "Set SecurityLevel of field {0} to LOW",
				signature);
		om.setField(o, signature, SecurityLevel.LOW);
	}

	/**
	 * @param signature
	 * @param level
	 */
	public void addLocal(String signature, SecurityLevel level) {
		logger.log(Level.INFO, "Insert Local {0} with Level {1} to LocalMap",
				new Object[] { signature, level });

		lm.insertElement(signature, level);
	}

	/**
	 * @param signature
	 */
	public void addLocal(String signature) {
		logger.log(Level.INFO, "Add Local {0} with Level LOW to LocalMap",
				signature);
		lm.insertElement(signature, SecurityLevel.LOW);
	}

	/**
	 * @param signature
	 * @return
	 */
	protected SecurityLevel getLocalLevel(String signature) {
		return lm.getLevel(signature);
	}

	/**
	 * @param signature
	 */
	public void makeLocalHigh(String signature) {
		logger.info("Set level of local " + signature + " to HIGH");
		lm.setLevel(signature, SecurityLevel.HIGH);
		logger.info("New securitylevel of local "
				+ signature + " is " + lm.getLevel(signature));
	}

	/**
	 * @param signature
	 */
	public void makeLocalLow(String signature) {
		logger.info("Set level of " + signature + " to LOW");
		lm.setLevel(signature, SecurityLevel.LOW);
		logger.info("New securitylevel: " + lm.getLevel(signature));
	}

	/**
	 * Push a new localPC and the hashvalue for its corresponding postdominator
	 * unit to the LPCList.
	 * 
	 * @param l
	 *            The new securitylevel.
	 * @param domHash
	 *            The hastvaule of the postdominator.
	 * @return The new localPC.
	 */
	protected SecurityLevel pushLocalPC(SecurityLevel l, int domHash) {
		lm.pushLocalPC(l, domHash);
		return lm.getLocalPC();
	}

	/**
	 * Remove the first element of the localPC list. The domHash value is used
	 * to check whether the first element belongs to the actual dominator.
	 * 
	 * @param domHash
	 *            Hashvalue for actual dominator.
	 */
	protected void popLocalPC(int domHash) {
		logger.info("Pop local pc.");
		lm.popLocalPC(domHash);
	}

	/**
	 * @return Actual localPC without removing it from the list.
	 */
	protected SecurityLevel getLocalPC() {
		return lm.getLocalPC();
	}

	/**
	 * @param l
	 * @return
	 */
	protected SecurityLevel pushGlobalPC(SecurityLevel l) {
		logger.log(Level.INFO, "Set globalPC to {0}", l);
		om.pushGlobalPC(l);
		return om.getGlobalPC();
	}

	/**
	 * @return
	 */
	protected SecurityLevel getGlobalPC() {
		return om.getGlobalPC();
	}

	/**
	 * @return
	 */
	protected SecurityLevel popGlobalPC() {
		return om.popGlobalPC();
	}

	/**
	 * @param pos
	 * @param local
	 * @return
	 */
	public SecurityLevel assignArgumentToLocal(int pos, String local) {
		lm.setLevel(local, hsu.joinWithLPC(om.getArgLevelAt(pos)));
		return lm.getLevel(local);
	}

	/**
	 * Assign actual returnlevel to local. The returnlevel must
	 * be set again to HIGH because the standard return level is HIGH for all
	 * external methods.
	 * @param local
	 */
	public void assignReturnLevelToLocal(String local) {
		if (!hsu.checkLocalPC(local)) {
			abort(local);
		}
		setLevelOfLocal(local, om.getActualReturnLevel());

		om.setActualReturnLevel(SecurityLevel.HIGH);
	}

	/**
	 * 
	 */
	public void returnConstant() {
		logger.log(Level.INFO, "Return a constant value.");
		om.setActualReturnLevel(hsu.joinWithLPC(SecurityLevel.LOW));
		logger.info("Actual return level is: " 
			+ hsu.joinWithLPC(SecurityLevel.LOW).toString());
	}

	/**
	 * @param signature
	 */
	public void returnLocal(String signature) {
		logger.log(Level.INFO, "Return Local {0} with level {1}", 
			new Object[] { signature, lm.getLevel(signature) });
		om.setActualReturnLevel(lm.getLevel(signature));
	}

	/**
	 * @param arguments
	 */
	public void storeArgumentLevels(String... arguments) {

		ArrayList<SecurityLevel> levelArr = new ArrayList<SecurityLevel>();
		for (String el : arguments) {
			levelArr.add(lm.getLevel(el));
		}
		om.setActualArguments(levelArr);
	}

	/**
	 * Check condition of an if-statement. This operation merges the security-
	 * levels of all locals with the actual localPC and stores them together
	 * with the hash value of the corresponding postdominator in the localmap.
	 * 
	 * @param domHash
	 *            Hashvalue of the postdominator.
	 * @param args
	 *            List of signatore-string of all locals.
	 */
	public void checkCondition(String domHash, String... args) {
		logger.info("Check condition of ifStmt");
		lm.pushLocalPC(hsu.joinWithLPC(hsu.joinLocals(args)),
				Integer.valueOf(domHash));
		om.pushGlobalPC(hsu.joinWithGPC(lm.getLocalPC()));
		logger.info("New LPC is " + lm.getLocalPC().toString());
	}

	/**
	 * Exit scope of an if-Statement. For each if-statement which ends at this
	 * position one lpc is popped from LPCstack. If the lpc belongs to this
	 * position is checked with the hashvalue of the position.
	 * 
	 * @param domHash
	 *            Hashvalue of the dominator.
	 */
	public void exitInnerScope(String domHash) {
		while (lm.domHashEquals(Integer.valueOf(domHash))) {
			logger.info("Pop LPC for hashval " + domHash);
			lm.popLocalPC(Integer.valueOf(domHash));
			om.popGlobalPC();
		}
	}

	/**
	 * @param local
	 * @return
	 */
	public SecurityLevel addLevelOfLocal(String local) {
		SecurityLevel localLevel = lm.getLevel(local);
		logger.log(Level.INFO, "Add level {0} of local {1} to assignment-level",
				new Object[] {localLevel, local });
		om.setAssignmentLevel(hsu.joinLevels(om.getAssignmentLevel(),
				localLevel));
		return om.getAssignmentLevel();
	}

	/**
	 * This method is for instance fields and for static fields.
	 * 
	 * @param o
	 * @param field
	 * @return
	 */
	public SecurityLevel addLevelOfField(Object o, String field) {
		SecurityLevel fieldLevel = om.getFieldLevel(o, field);
		logger.log(Level.INFO, "Add level {0} of field {1} to assignment-level",
				new Object[] {
				fieldLevel, field });
		om.setAssignmentLevel(hsu.joinLevels(om.getAssignmentLevel(),
				fieldLevel));
		return om.getAssignmentLevel();
	}

	/**
	 * @param o
	 * @param field
	 * @return
	 */
	public SecurityLevel addLevelOfArrayField(Object o, String field) {

		// TODO
		SecurityLevel fieldLevel = om.getFieldLevel(o, field);		
		logger.log(Level.INFO, "Add level {0} of array-field {1} to assignment-level",
				new Object[] {fieldLevel, field });
		om.setAssignmentLevel(hsu.joinLevels(om.getAssignmentLevel(),
				fieldLevel));
		return om.getAssignmentLevel();
	}
	
	/**
	 * @param signature
	 * @param level
	 * @return
	 */
	protected SecurityLevel setLevelOfLocal(String signature,
			SecurityLevel level) {
		logger.log(Level.INFO, "Set level of local {0} to {1}", 
				new Object[] {signature, level});
		lm.setLevel(signature, level);
		return lm.getLevel(signature);
	}
	
	/**
	 * @param signature
	 * @return
	 */
	public SecurityLevel setLevelOfLocal(String signature) {		
		logger.log(Level.INFO, "Set level of local {0} to {1}",
				new Object[] {signature, hsu.joinWithLPC(om.getAssignmentLevel())});
		if (!hsu.checkLocalPC(signature)) {
			abort(signature);
		}
		lm.setLevel(signature, hsu.joinWithLPC(om.getAssignmentLevel()));
		logger.log(Level.INFO, "New level of local {0} is {1}",
				new Object[] {signature, lm.getLevel(signature)});
		om.clearAssignmentLevel();
		return lm.getLevel(signature);
	}

	/**
	 * This method is for instance-fields and for static-fields
	 * 
	 * @param o
	 * @param field
	 * @return
	 */
	public SecurityLevel setLevelOfField(Object o, String field) {		
		logger.log(Level.INFO, "Set level of field {0} to {1}",
				new Object[] {field, hsu.joinWithGPC(om.getAssignmentLevel())});
		if (!hsu.checkGlobalPC(o, field)) {
			abort(o.toString() + field);
		}
		om.setField(o, field, hsu.joinWithGPC(om.getAssignmentLevel()));
		logger.log(Level.INFO, "New level of field {0} is {1}",
				new Object[] {field, om.getFieldLevel(o, field)});
		om.clearAssignmentLevel();
		return om.getFieldLevel(o, field);
	}

	/**
	 * Check the array-field and the local-level of the object against the gpc,
	 * and read the level stored as assignment-level. This level - joined with
	 * the gpc - is set as the new level for given array-field. This method is
	 * needed if the index is a local and must be checked against the gpc.
	 * 
	 * @param o
	 *            - Object - The array object
	 * @param field
	 *            - String - the signature of the field (array element)
	 * @param localForObject
	 *            - String - the signature of the local where the object is
	 *            stored
	 * @param localForIndex
	 *            - String - the signature of the local where the index is
	 *            stored
	 * @return Returns the new SecurityLevel of the array-element
	 */
	public SecurityLevel setLevelOfArrayField(Object o, String field,
			String localForObject, String localForIndex) {
		logger.log(Level.INFO, "Set level of array-field {0} to {1}",
				new Object[] {field, hsu.joinWithGPC(om.getAssignmentLevel())});
		if (!hsu.checkArrayWithGlobalPC(o, field, localForObject, localForIndex)) {
			abort(o.toString() + field);
		}
		om.setField(o, field, hsu.joinWithGPC(om.getAssignmentLevel()));
		logger.log(Level.INFO, "New level of array-field {0} is {1}",
				new Object[] {field, om.getFieldLevel(o, field)});
		om.clearAssignmentLevel();
		return om.getFieldLevel(o, field);
	}

	/**
	 * Check the array-field and the local-level of the object against the gpc,
	 * and read the level stored as Assignmentlevel. This level - joined with
	 * the gpc - is set as the new level for given array-field. This method is
	 * needed if the index is a constant and it is not needed to be checked
	 * against the gpc.
	 * 
	 * @param o
	 *            - Object - The array object
	 * @param field
	 *            - String - the signature of the field (array element)
	 * @param localForObject
	 *            - String - the signature of the local where the object is
	 *            stored
	 * @return Returns the new SecurityLevel of the array-element
	 */
	public SecurityLevel setLevelOfArrayField(Object o, String field,
			String localForObject) {
		logger.log(Level.INFO, "Set level of array-field {0} to {1}",
				new Object[] {field, hsu.joinWithGPC(om.getAssignmentLevel())});
		if (!hsu.checkArrayWithGlobalPC(o, field, localForObject)) {
			abort(o.toString() + field);
		}
		om.setField(o, field, hsu.joinWithGPC(om.getAssignmentLevel()));
		logger.log(Level.INFO, "New level of array-field {0} is {1}",
				new Object[] {field, om.getFieldLevel(o, field)});
		om.clearAssignmentLevel();
		return om.getFieldLevel(o, field);
	}

	/**
	 * @param signature
	 */
	public void checkThatNotHigh(String signature) {
		logger.info("Check that " + lm.getLevel(signature)
				+ " is not HIGH");
		if (lm.getLevel(signature) == SecurityLevel.HIGH) {
			logger.info("it's high");
			throw new IllegalFlowException(
					"Passed argument " + signature
					+ " with a high security level to a method "
					+ "which doesn't allow it.");
		}
	}
}

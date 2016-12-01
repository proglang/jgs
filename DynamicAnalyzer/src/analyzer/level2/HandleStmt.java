package analyzer.level2;

import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L2Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class with all neccessary methods to perform an analysis.
 * 
 * @author Regina Koenig (2015)
 */
public class HandleStmt {

	private static final Logger logger = L2Logger.getLogger();

	private LocalMap localmap;
	private static ObjectMap objectmap;
	private HandleStmtUtils handleStatementUtils;

	/**
	 * This must be called at the beginning of every method in the analyzed
	 * code. It creates a new LocalMap for the method and adjusts the
	 * {@link SecurityLevel} of the globalPC
	 */
	public HandleStmt() {
		logger.log(Level.INFO, "Create new HandleStmt instance");
		localmap = new LocalMap();
		objectmap = ObjectMap.getInstance();
		handleStatementUtils = new HandleStmtUtils(localmap, objectmap);

		objectmap.pushGlobalPC(handleStatementUtils.joinLevels(
				objectmap.getGlobalPC(), localmap.getLocalPC()));
	}

	/**
	 * This method must be called just once at the beginning of the main method.
	 * It triggers the setup of the logger.
	 */
	public static void init() {
		logger.log(Level.INFO, "Init HandleStatement");
		try {
			L2Logger.setup();
		} catch (IOException e) {
			logger.warning("Set up of logger failed");
			e.printStackTrace();
		}
		if (objectmap == null) {
			objectmap = ObjectMap.getInstance();
		}
		objectmap.flush();
		objectmap.clearAssignmentLevel();
	}

	/**
	 * This must be called at the end of every method in the analyzed code. It
	 * resets the globalPC to its initial value
	 */
	public void close() {
		logger.log(Level.INFO, "Close HandleStatement");
		objectmap.popGlobalPC();
		localmap.checkisLPCStackEmpty();
	}

	/**
	 * Set the return level.
	 * 
	 * @param securityLevel
	 *            The securitylevel of actual return-statement.
	 * @return The new security-level.
	 */
	protected Object setActualReturnLevel(Object securityLevel) {
		return objectmap.setActualReturnLevel(securityLevel);
	}

	/**
	 * Get the actual return level. The level is not changed in this operation.
	 * 
	 * @return The securitylevel of the last return-statement.
	 */
	protected Object getActualReturnLevel() {
		return objectmap.getActualReturnLevel();
	}

	/**
	 * Add an object to the ObjectMap.
	 * 
	 * @param object
	 *            object
	 */
	public void addObjectToObjectMap(Object object) {
		logger.log(Level.INFO, "Insert Object {0} to ObjectMap", object);
		objectmap.insertNewObject(object);
		if (!objectmap.containsObject(object)) {
			throw new InternalAnalyzerException("Add object " + object
					+ " to ObjectMap failed.");
		}
	}

	/**
	 * Add a field of an object to ObjectMap.
	 * 
	 * @param object
	 *            an object
	 * @param signature
	 *            signature of the field of the object
	 * @return SecurityLevel of the newly set field
	 */
	public Object addFieldToObjectMap(Object object, String signature) {
		logger.log(Level.INFO, "Add Field {0} to object {1}", new Object[] {
				signature, object });
		handleStatementUtils.checkIfObjectExists(object);
		Object fieldLevel = objectmap.setField(object, signature,
				SecurityLevel.bottom());
		if (!objectmap.containsField(object, signature)) {
			throw new InternalAnalyzerException("Add field " + signature
					+ " to ObjectMap failed");
		}
		return fieldLevel;
	}

	/**
	 * Add an array to ObjectMap.
	 * 
	 * @param array
	 *            array
	 */
	public void addArrayToObjectMap(Object[] array) {
		logger.log(Level.INFO, "Add Array {0} to ObjectMap", array.toString());
		logger.info("Array length " + array.length);

		addObjectToObjectMap(array);
		for (int i = 0; i < array.length; i++) {
			addFieldToObjectMap(array, Integer.toString(i));
		}

		if (!containsObjectInObjectMap(array)) {
			throw new InternalAnalyzerException("Add Object " + array
					+ " to ObjectMap failed");
		}
	}

	/**
	 * Check if an object is contained in ObjectMap.
	 * 
	 * @param object
	 *            object
	 * @return true, if object is found in ObjectMap
	 */
	protected boolean containsObjectInObjectMap(Object object) {
		return objectmap.containsObject(object);
	}

	/**
	 * Check if a field is contained in ObjectMap.
	 * 
	 * @param object
	 *            object of the field
	 * @param signature
	 *            signature of the field
	 * @return true, if field is found in ObjectMap
	 */
	protected boolean containsFieldInObjectMap(Object object, String signature) {
		handleStatementUtils.checkIfObjectExists(object);
		return objectmap.containsField(object, signature);
	}

	/**
	 * Get the number of elements in ObjectMap.
	 * 
	 * @return number of elements
	 */
	protected int getNumberOfElementsInObjectMap() {
		return objectmap.getNumberOfElements();
	}

	/**
	 * Get the number of fields for an object in ObjectMap.
	 * 
	 * @param bject
	 *            object
	 * @return number of fields for given object
	 */
	protected int getNumberOfFieldsInObjectMap(Object bject) {
		handleStatementUtils.checkIfObjectExists(bject);
		return objectmap.getNumberOfFields(bject);
	}

	/**
	 * Get the SecurityLevel for a field in ObjectMap.
	 * 
	 * @param object
	 *            object of the field
	 * @param signature
	 *            signature of the field
	 * @return SecurityLevel
	 */
	protected Object getFieldLevel(Object object, String signature) {
		handleStatementUtils.checkIfObjectExists(object);
		return objectmap.getFieldLevel(object, signature);
	}

	/**
	 * Initialize a given variable
	 * 
	 * @param signature
	 *            Name of the variable
	 */
	public void initializeVariable(String signature) {
		localmap.initializeLocal(signature);
	}

	/**
	 * Set the level of a field to SecurityLevel.top().
	 * 
	 * @param object
	 *            object containing the field
	 * @param signature
	 *            signature of the field
	 */
	public void makeFieldHigh(Object object, String signature) {
		logger.log(Level.INFO, "Set SecurityLevel of field {0} to HIGH",
				signature);
		handleStatementUtils.checkIfObjectExists(object);
		objectmap.setField(object, signature, SecurityLevel.top());
	}

	/**
	 * Set the level of a field to LOW.
	 * 
	 * @param object
	 *            object containing the field
	 * @param signature
	 *            signature of the field
	 */
	public void makeFieldLow(Object object, String signature) {
		logger.log(Level.INFO, "Set SecurityLevel of field {0} to LOW",
				signature);
		handleStatementUtils.checkIfObjectExists(object);
		objectmap.setField(object, signature, SecurityLevel.bottom());
	}

	/**
	 * Add a local to LocalMap.
	 * 
	 * @param signature
	 *            signature of the local
	 * @param level
	 *            SecurityLevel for the new local
	 */
	public void addLocal(String signature, Object level) {
		logger.log(Level.INFO, "Insert Local {0} with Level {1} to LocalMap",
				new Object[] { signature, level });
		handleStatementUtils.checkThatLocalDoesNotExist(signature);
		localmap.insertLocal(signature, level);
	}

	/**
	 * Add an uninitialized local to LocalMap with default SecurityLevel LOW.
	 * Used for declaration, e.g: "int i;"
	 * 
	 * @param signature
	 *            signature of the local
	 */
	public void addLocal(String signature) {
		logger.log(Level.INFO,
				"Add Local {0} with SecurityLevel.bottom() to LocalMap",
				signature);
		handleStatementUtils.checkThatLocalDoesNotExist(signature);
		localmap.insertUninitializedLocal(signature); // add uninit local with
														// default sec-level
														// bottom
	}

	/**
	 * Get the SecurityLevel of a local.
	 * 
	 * @param signature
	 *            signature of the local
	 * @return SecurityLevel
	 */
	protected Object getLocalLevel(String signature) {
		handleStatementUtils.checkIfLocalExists(signature);
		localmap.initializeLocal(signature);
		return localmap.getLevel(signature);
	}

	/**
	 * Set SecurityLevel of given local to HIGH.
	 * 
	 * @param signature
	 *            signature of local
	 */
	public void makeLocalHigh(String signature) {
		logger.info("Set level of local " + signature
				+ " to SecurityLevel.top()");
		handleStatementUtils.checkIfLocalExists(signature);
		localmap.initializeLocal(signature);
		localmap.setLevel(signature, SecurityLevel.top());
		logger.info("New securitylevel of local " + signature + " is "
				+ localmap.getLevel(signature));
	}

	/**
	 * Set SecurityLevel of given local to LOW.
	 * 
	 * @param signature
	 *            signature of local
	 */
	public void makeLocalLow(String signature) {
		logger.info("Set level of " + signature + " to SecurityLevel.bottom()");
		handleStatementUtils.checkIfLocalExists(signature);
		localmap.setLevel(signature, SecurityLevel.bottom());
		logger.info("New securitylevel: " + localmap.getLevel(signature));
	}

	/**
	 * Push the level of a given instance to the globalPC (e.g. on top of its stack)
	 * @param localSignature		singature of local to be pushed onto the stack
	 */
	public void pushInstanceLevelToGlobalPC(String localSignature) {
		// get instance level of localSignature, push to globalPC (which calcs
		// the max of all its stack elements)
		
		Object secLevel = getLocalLevel(localSignature);
		pushGlobalPC(handleStatementUtils.joinWithGPC(secLevel));
	
	}
	
	// make PopGlobalPC public
	
	/**
	 * Push a new localPC and the indentity for its corresponding postdominator
	 * unit to the LPCList.
	 * 
	 * @param securityLevel
	 *            The new securitylevel.
	 * @param dominatorIdentity
	 *            The identity of the postdominator.
	 * @return The new localPC.
	 */
	protected Object pushLocalPC(Object securityLevel, int dominatorIdentity) {
		localmap.pushLocalPC(securityLevel, dominatorIdentity);
		logger.info("New LPC: " + localmap.getLocalPC().toString());
		return localmap.getLocalPC();
	}

	/**
	 * Remove the first element of the localPC list. The indentity value is used
	 * to check whether the first element belongs to the actual dominator.
	 * 
	 * @param dominatorIdentity
	 *            Hidentity for actual dominator.
	 */
	protected void popLocalPC(int dominatorIdentity) {
		logger.info("Pop local pc.");
		localmap.popLocalPC(dominatorIdentity);
	}

	/**
	 * Get actual localPC without removing it from the list.
	 * 
	 * @return localPC
	 */
	protected Object getLocalPC() {
		return localmap.getLocalPC();
	}

	/**
	 * Add a globalPC to GPC-stack.
	 * 
	 * @param securityLevel
	 *            SecurityLevel of GPC.
	 * @return new SecurityLevel
	 */
	protected Object pushGlobalPC(Object securityLevel) {
		logger.log(Level.INFO, "Set globalPC to {0}", securityLevel);
		objectmap.pushGlobalPC(securityLevel);
		return objectmap.getGlobalPC();
	}

	/**
	 * Get first element of globalPC stack without removing it.
	 * 
	 * @return SecurityLevel
	 */
	protected Object getGlobalPC() {
		return objectmap.getGlobalPC();
	}

	/**
	 * Remove the first element of globalPC-stack.
	 * 
	 * @return SecurityLevel of last GPC
	 */
	public Object popGlobalPC() {
		return objectmap.popGlobalPC();
	}

	/**
	 * Assign argument at given position to local.
	 * 
	 * @param pos
	 *            position of argument
	 * @param signature
	 *            signature of local
	 * @return new SecurityLevel of local
	 */
	public Object assignArgumentToLocal(int pos, String signature) {
		handleStatementUtils.checkIfLocalExists(signature);
		localmap.setLevel(signature,
				handleStatementUtils.joinWithLPC(objectmap.getArgLevelAt(pos)));
		// if not initialized, initialize it:
		localmap.initializeLocal(signature);
		return localmap.getLevel(signature);
	}

	/**
	 * Assign actual returnlevel to local. The returnlevel must be set again to
	 * HIGH because the standard return level is SecurityLevel.top() for all
	 * external methods.
	 * 
	 * @param signature
	 *            signature of local
	 */
	public void assignReturnLevelToLocal(String signature) {
		handleStatementUtils.checkIfLocalExists(signature);
		handleStatementUtils.checkLocalPC(signature);
		setLevelOfLocal(signature, objectmap.getActualReturnLevel());
		objectmap.setActualReturnLevel(SecurityLevel.top());
	}

	/**
	 * Set Returnlevel to SecurityLevel.bottom().
	 */
	public void returnConstant() {
		logger.log(Level.INFO, "Return a constant value.");
		objectmap.setActualReturnLevel(handleStatementUtils
				.joinWithLPC(SecurityLevel.bottom()));
		logger.info("Actual return level is: "
				+ handleStatementUtils.joinWithLPC(SecurityLevel.bottom())
						.toString());
	}

	/**
	 * Set returnlevel to the level of local.
	 * 
	 * @param signature
	 *            signature of local
	 */
	public void returnLocal(String signature) {
		logger.log(Level.INFO, "Return Local {0} with level {1}", new Object[] {
				signature, localmap.getLevel(signature) });
		objectmap.setActualReturnLevel(localmap.getLevel(signature));
	}

	/**
	 * Store the levels of the arguments in a list in LocalMap.
	 * 
	 * @param arguments
	 *            List of arguments
	 */
	public void storeArgumentLevels(String... arguments) {
		logger.info("Store arguments " + Arrays.toString(arguments)
				+ " in LocalMap");
		ArrayList<Object> levelArr = new ArrayList<Object>();
		for (String el : arguments) {
			handleStatementUtils.checkIfLocalExists(el);
			localmap.initializeLocal(el);
			levelArr.add(localmap.getLevel(el));
		}
		objectmap.setActualArguments(levelArr);
	}

	/**
	 * Check condition of an if-statement. This operation merges the security-
	 * levels of all locals with the actual localPC and stores them together
	 * with the identity value of the corresponding postdominator in the
	 * localmap.
	 * 
	 * @param dominatorIdentity
	 *            identity of the postdominator.
	 * @param args
	 *            List of signatore-string of all locals.
	 */
	public void checkCondition(String dominatorIdentity, String... args) {
		logger.info("Check condition of ifStmt");
		localmap.pushLocalPC(handleStatementUtils
				.joinWithLPC(handleStatementUtils.joinLocals(args)), Integer
				.valueOf(dominatorIdentity));
		objectmap.pushGlobalPC(handleStatementUtils.joinWithGPC(localmap
				.getLocalPC()));
		logger.info("New LPC is " + localmap.getLocalPC().toString());
	}

	/**
	 * Exit scope of an if-Statement. For each if-statement which ends at this
	 * position one lpc is popped from LPCstack. If the lpc belongs to this
	 * position is checked with the identity of the position.
	 * 
	 * @param dominatorIdentity
	 *            identity of the dominator.
	 */
	public void exitInnerScope(String dominatorIdentity) {
		while (localmap.dominatorIdentityEquals(Integer
				.valueOf(dominatorIdentity))) {
			logger.info("Pop LPC for identity " + dominatorIdentity);
			localmap.popLocalPC(Integer.valueOf(dominatorIdentity));
			objectmap.popGlobalPC(); 	// pop needs to be removed
		}
	}

	/**
	 * Join the level of the local to the assignment-level. This possibly
	 * increases, never decreases, the assignment-level.
	 * 
	 * This method is called when an assign happens.
	 * 
	 * @param local
	 *            signature of the local.
	 * @return security-level of the local.
	 */
	public Object joinLevelOfLocalAndAssignmentLevel(String local) {
		localmap.initializeLocal(local);
		Object localLevel = localmap.getLevel(local);
		objectmap.setAssignmentLevel(handleStatementUtils.joinLevels(
				objectmap.getAssignmentLevel(), localLevel));
		logger.log(
				Level.INFO,
				"Set assignment-level to level "
						+ objectmap.getAssignmentLevel());
		return objectmap.getAssignmentLevel();
	}

	/**
	 * This method is for instance fields and for static fields. Join the level
	 * of the field to the assignment-level.
	 * 
	 * @param object
	 *            The object of the field.
	 * @param field
	 *            Signature of the field.
	 * @return SecurityLevel of the field.
	 */
	public Object joinLevelOfFieldAndAssignmentLevel(Object object, String field) {
		Object fieldLevel = objectmap.getFieldLevel(object, field);
		logger.log(
				Level.INFO,
				"Set assignment-level to level {0} (which is the level of local {1})",
				new Object[] { fieldLevel, field });
		objectmap.setAssignmentLevel(handleStatementUtils.joinLevels(
				objectmap.getAssignmentLevel(), fieldLevel));
		return objectmap.getAssignmentLevel();
	}

	/**
	 * Join the level of the field to the assignment-level.
	 * 
	 * @param object
	 *            The object of the field.
	 * @param field
	 *            Signature of the field.
	 * @return SecurityLevel of the field.
	 */
	public Object joinLevelOfArrayFieldAndAssignmentLevel(Object object,
			String field) {
		Object fieldLevel = objectmap.getFieldLevel(object, field);
		logger.log(
				Level.INFO,
				"Set assignment-level to level {0} (which is the level of local {1})",
				new Object[] { fieldLevel, field });
		objectmap.setAssignmentLevel(handleStatementUtils.joinLevels(
				objectmap.getAssignmentLevel(), fieldLevel));
		return objectmap.getAssignmentLevel();
	}

	/**
	 * Set the level of the local to given security level.
	 * 
	 * @param signature
	 *            Signature of the local.
	 * @param level
	 *            security-level
	 * @return The new security-level
	 */
	protected Object setLevelOfLocal(String signature, Object securitylevel) {
		logger.log(Level.INFO, "Set level of local {0} to {1}", new Object[] {
				signature, securitylevel });
		localmap.initializeLocal(signature);
		localmap.setLevel(signature, securitylevel);
		return localmap.getLevel(signature);
	}

	/**
	 * Set the level of a local to default security-level. Called on every
	 * assignment, and on initialization; but not on declaration.
	 * 
	 * @param signature
	 *            signature of the local
	 * @return new security-level
	 */
	public Object setLevelOfLocal(String signature) {
		// Then, calc new level:
		// for assignments like a = x + y, we need to calculate the
		// new security-value of a: this sec-value depends either on
		// the local PC (for example, if inside a high-if), or on either
		// of the right-hand variables' sec-value, which is accumulated
		// in the assignmentLevel
		Object newSecValue = handleStatementUtils.joinWithLPC(objectmap
				.getAssignmentLevel());
		logger.log(Level.INFO, "Set level of local {0} to {1}", new Object[] {
				signature, newSecValue });

		localmap.setLevel(signature, newSecValue);
		logger.log(Level.INFO, "New level of local {0} is {1} ", new Object[] {
				signature, localmap.getLevel(signature) });

		objectmap.clearAssignmentLevel();
		return localmap.getLevel(signature);
	}

	/**
	 * This method is for instance-fields and for static-fields.
	 * 
	 * @param object
	 *            Object of the field.
	 * @param field
	 *            signature of the field.
	 * @return The security-level of the field.
	 */
	public Object setLevelOfField(Object object, String field) {
		logger.log(
				Level.INFO,
				"Set level of field {0} to {1}",
				new Object[] {
						field,
						handleStatementUtils.joinWithGPC(objectmap
								.getAssignmentLevel()) });
		// handleStatementUtils.checkGlobalPC(object, field);
		objectmap.setField(object, field, handleStatementUtils
				.joinWithGPC(objectmap.getAssignmentLevel()));
		logger.log(Level.INFO, "New level of field {0} is {1}", new Object[] {
				field, objectmap.getFieldLevel(object, field) });
		objectmap.clearAssignmentLevel();
		return objectmap.getFieldLevel(object, field);
	}

	/**
	 * Check the array-field and the local-level of the object against the gpc,
	 * and read the level stored as assignment-level. This level - joined with
	 * the gpc - is set as the new level for given array-field. This method is
	 * needed if the index is a local and must be checked against the gpc.
	 * 
	 * @param object
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
	public Object setLevelOfArrayField(Object object, String field,
			String localForObject, String localForIndex) {
		logger.log(
				Level.INFO,
				"Set level of array-field {0} to {1}",
				new Object[] {
						field,
						handleStatementUtils.joinWithGPC(objectmap
								.getAssignmentLevel()) });

		objectmap.setField(object, field, handleStatementUtils
				.joinWithGPC(objectmap.getAssignmentLevel()));
		logger.log(Level.INFO, "New level of array-field {0} is {1}",
				new Object[] { field, objectmap.getFieldLevel(object, field) });
		objectmap.clearAssignmentLevel();
		return objectmap.getFieldLevel(object, field);
	}

	/**
	 * Check the array-field and the local-level of the object against the gpc,
	 * and read the level stored as assignment-level. This level - joined with
	 * the gpc - is set as the new level for given array-field. This method is
	 * needed if the index is a constant and it is not needed to be checked
	 * against the gpc.
	 * 
	 * @param object
	 *            - Object - The array object
	 * @param field
	 *            - String - the signature of the field (array element)
	 * @param localForObject
	 *            - String - the signature of the local where the object is
	 *            stored
	 * @return Returns the new SecurityLevel of the array-element
	 */
	public Object setLevelOfArrayField(Object object, String field,
			String localForObject) {
		logger.log(
				Level.INFO,
				"Set level of array-field {0} to {1}",
				new Object[] {
						field,
						handleStatementUtils.joinWithGPC(objectmap
								.getAssignmentLevel()) });

		objectmap.setField(object, field, handleStatementUtils
				.joinWithGPC(objectmap.getAssignmentLevel()));
		logger.log(Level.INFO, "New level of array-field {0} is {1}",
				new Object[] { field, objectmap.getFieldLevel(object, field) });
		objectmap.clearAssignmentLevel();
		return objectmap.getFieldLevel(object, field);
	}

	/**
	 * Reference to handleStatementUtils.checkArrayWithGlobalPC
	 * 
	 * @param object
	 * @param signature
	 * @param localForObject
	 */
	public void checkArrayWithGlobalPC(Object object, String signature,
			String localForObject) {
		handleStatementUtils.checkArrayWithGlobalPC(object, signature,
				localForObject);
	}
	
	/**
	 * Reference to handeStatementUtils method
	 * @param object
	 * @param signature
	 * @param localForObject
	 * @param localForIndex
	 */
	public void checkArrayWithGlobalPC(Object object, String signature,
			String localForObject, String localForIndex) {
		handleStatementUtils.checkArrayWithGlobalPC(object, signature, localForObject, localForIndex);
	}

	/**
	 * Check if level of field is greater then global PC
	 * 
	 * @param object
	 * @param field
	 */
	public void checkGlobalPC(Object object, String field) { // weiteres arg: Object addLevel
							// localpc bei statischen feldern, localpc + referenz bei instanzfeldern
		handleStatementUtils.checkGlobalPC(object, field);
	}

	/**
	 * Check if local > localPC
	 * 
	 * @param signature
	 *            the local to test against the localPC
	 */
	public void checkLocalPC(String signature) {
		handleStatementUtils.checkLocalPC(signature);
	}

	/**
	 * This method is used if an argument with a HIGH value is passed to a
	 * method which doesn't allow it.
	 * 
	 * @param signature
	 *            signature of the argument
	 */
	public void checkThatNotHigh(String signature) {
		logger.info("Check that " + localmap.getLevel(signature)
				+ " is not HIGH");
		if (localmap.getLevel(signature) == SecurityLevel.top()) {
			logger.info("it's high");
			handleStatementUtils.abort("Passed argument " + signature
					+ " with a high security level to a method "
					+ "which doesn't allow it.");
		}
	}

	/**
	 * This method is used if a print statement is identified. Print statements
	 * must never be called in high sec context. Thus, we must check the
	 * context;
	 */
	public void checkThatPCNotHigh() {
		logger.info("About to print something to public output. Thus, check that PC is not HIGH");
		if (localmap.getLocalPC() == SecurityLevel.top()) {
			logger.info("Trying to print with HIGH PC!");
			handleStatementUtils
					.abort("Tried to print in high-security context: LocalPC was HIGH!");
		}
	}
}

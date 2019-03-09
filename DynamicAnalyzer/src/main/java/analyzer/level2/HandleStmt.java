package analyzer.level2;

import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import util.exceptions.IllegalFlowError;
import util.exceptions.InternalAnalyzerException;
import util.exceptions.NSUError;
import util.logging.L2Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static analyzer.level2.HandleStmtUtils.NSU_ERROR_MESSAGE;

/**
 * MethodTypings for JGS' run-time enforcement. This class makes up the main
 * part of the run-time system.
 *
 * @author Regina Koenig (2015)
 */
@SuppressWarnings("ALL")
public class HandleStmt {

    /**
     * Logger used by the run-time enforcement. Verbose message are available by
     * setting the environment variable JGS_VERBOSE_LOGGING=1.
     */
    private static final Logger logger = L2Logger.getLogger();

    private LocalMap localmap;
    private static ObjectMap objectmap;
    private HandleStmtUtils handleStatementUtils;
    PassivController controller;

    /**
     * This must be called at the beginning of every method in the analyzed
     * code. It creates a new LocalMap for the method and adjusts the
     * {@link CurrentSecurityDomain} of the globalPC
     */
    public HandleStmt() {
        localmap = new LocalMap();
        objectmap = ObjectMap.getInstance();
    }

    @SuppressWarnings("unused")
    /**
     * Initialise the HandleStmtUtils. Use also to specify if, and what kind
     * of exception we expect
     */
    public void initHandleStmtUtils(boolean controllerIsActive, int exptectedException) {
        this.controller = ControllerFactory.returnSuperfluousInstrumentationController(controllerIsActive, exptectedException);
        handleStatementUtils = new HandleStmtUtils(localmap, objectmap, this.controller);
        objectmap.pushGlobalPC(handleStatementUtils.joinLevels(objectmap.getGlobalPC(), localmap.getLocalPC()));
    }

    /**
     * This method must be called just once at the beginning of the main
     * method.
     * It triggers the setup of the logger.
     */
    public static void init() {
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
     * @param securityLevel The securitylevel of actual return-statement.
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
     * @param object object
     */
    public void addObjectToObjectMap(Object object) {
        logger.log(Level.INFO, "Insert Object {0} to ObjectMap", object);
        objectmap.insertNewObject(object);
        if (!objectmap.containsObject(object)) {
            throw new InternalAnalyzerException("Add object " + object + " to ObjectMap failed.");
        }
    }

    /**
     * Add a field of an object to ObjectMap.
     *
     * @param object    an object
     * @param var varName of the field of the object
     * @return SecurityLevel of the newly set field
     */
    public Object addFieldToObjectMap(Object object, String var) {
        logger.log(Level.INFO, "Add Field {0} to object {1}", new Object[]{var, object});
        handleStatementUtils.checkIfObjectExists(object);
        Object fieldLevel = objectmap.setField(object, var, CurrentSecurityDomain.bottom());
        if (!objectmap.containsField(object, var)) {
            throw new InternalAnalyzerException("Add field " + var + " to ObjectMap failed");
        }
        return fieldLevel;
    }

    /**
     * Add an array to ObjectMap.
     *
     * @param array array
     */
    public void addArrayToObjectMap(Object[] array) {
        logger.log(Level.INFO, "Add Array {0} to ObjectMap", array.toString());
        logger.info("Array length " + array.length);

        addObjectToObjectMap(array);
        for (int i = 0; i < array.length; i++) {
            addFieldToObjectMap(array, Integer.toString(i));
        }

        if (!containsObjectInObjectMap(array)) {
            throw new InternalAnalyzerException("Add Object " + array + " to ObjectMap failed");
        }
    }

    /**
     * Check if an object is contained in ObjectMap.
     *
     * @param object object
     * @return true, if object is found in ObjectMap
     */
    protected boolean containsObjectInObjectMap(Object object) {
        return objectmap.containsObject(object);
    }

    /**
     * Check if a field is contained in ObjectMap.
     *
     * @param object    object of the field
     * @param var signature of the field
     * @return true, if field is found in ObjectMap
     */
    protected boolean containsFieldInObjectMap(Object object, String var) {
        handleStatementUtils.checkIfObjectExists(object);
        return objectmap.containsField(object, var);
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
     * @param bject object
     * @return number of fields for given object
     */
    protected int getNumberOfFieldsInObjectMap(Object bject) {
        handleStatementUtils.checkIfObjectExists(bject);
        return objectmap.getNumberOfFields(bject);
    }

    /**
     * Get the SecurityLevel for a field in ObjectMap.
     *
     * @param object    object of the field
     * @param signature signature of the field
     * @return SecurityLevel
     */
    protected Object getFieldLevel(Object object, String signature) {
        handleStatementUtils.checkIfObjectExists(object);
        return objectmap.getFieldLevel(object, signature);
    }

    /**
     * Set the level of a field to SecurityLevel.top().
     *
     * @param object    object containing the field
     * @param var signature of the field
     */
    public void makeFieldHigh(Object object, String var) {
        logger.log(Level.INFO, "Set SecurityLevel of field {0} to HIGH", var);
        handleStatementUtils.checkIfObjectExists(object);
        objectmap.setField(object, var, CurrentSecurityDomain.top());
    }

    /**
     * Set the level of a field to LOW.
     *
     * @param object    object containing the field
     * @param var signature of the field
     */
    public void makeFieldLow(Object object, String var) {
        logger.log(Level.INFO, "Set SecurityLevel of field {0} to LOW", var);
        handleStatementUtils.checkIfObjectExists(object);
        objectmap.setField(object, var, CurrentSecurityDomain.bottom());
    }

    /**
     * Add a local to LocalMap.
     *
     * @param var signature of the local
     * @param level     SecurityLevel for the new local
     */
    public void addLocal(String var, Object level) {
        logger.log(Level.INFO, "Insert Local {0} with Level {1} to LocalMap", new Object[]{var, level});
        handleStatementUtils.checkThatLocalDoesNotExist(var);
        localmap.setLevel(var, level);
    }

    /**
     * Add an uninitialized local to LocalMap with default SecurityLevel LOW.
     * Used for declaration, e.g: "int i;"
     *
     * An uninitialized local is an untracked local, so this method should
     * not be used at all.
     *
     * @param var signature of the local
     */
    @Deprecated
    public void addLocal(String var) {
        logger.log(Level.INFO,"Add Local {0} with SecurityLevel.bottom() to LocalMap", var);
        handleStatementUtils.checkThatLocalDoesNotExist(var);
    }

    /**
     * Get the SecurityLevel of a local.
     *
     * @param var var of the local
     * @return SecurityLevel
     */
    protected Object getLocalLevel(String var) {
        return localmap.getLevel(var);
    }

    public void setLocalFromString(String var, String level) {
        logger.info("Set level of local " + var + " to " + level);
        localmap.setLevel(var, CurrentSecurityDomain.readLevel(level));
    }


    /**
     * Push the level of a given instance to the globalPC (e.g. on top of its
     * stack)
     *
     * @param var singature of local to be pushed onto the stack
     */
    public void pushInstanceLevelToGlobalPC(String var) {
        // get instance level of localSignature, push to globalPC (which calcs
        // the max of all its stack elements)

        Object secLevel = getLocalLevel(var);
        pushGlobalPC(handleStatementUtils.joinWithGPC(secLevel));

    }

    // make PopGlobalPC public

    /**
     * Push a new localPC and the indentity for its corresponding postdominator
     * unit to the LPCList.
     *
     * @param securityLevel     The new securitylevel.
     * @param dominatorIdentity The identity of the postdominator.
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
     * @param dominatorIdentity Hidentity for actual dominator.
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
     * @param securityLevel SecurityLevel of GPC.
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
     * @param pos       position of argument
     * @param var var of local
     * @return new SecurityLevel of local
     */
    public Object assignArgumentToLocal(int pos, String var) {

        // In case somebody wonders: we do not need to check the local pc
        // here. In Jimple, argument-to-local assignments (JIdentityStmt) are always
        // the beginning of the method, where the context is public

        localmap.setLevel(var, handleStatementUtils.joinWithLPC(objectmap.getArgLevelAt(pos)));
        return localmap.getLevel(var);
    }

    /**
     * Assign actual returnlevel to local. The returnlevel must be set again to
     * HIGH because the standard return level is SecurityLevel.top() for all
     * external methods.
     *
     * @param var signature of local
     */
    public void assignReturnLevelToLocal(String var) {
        Object returnLevel = objectmap.getActualReturnLevel();

        checkLocalPC(var);
        setLocal(var, returnLevel);
        objectmap.setActualReturnLevel(CurrentSecurityDomain.top());
    }

    /**
     * Set Returnlevel to SecurityLevel.bottom().
     */
    public void returnConstant() {
        logger.log(Level.INFO, "Return a constant value.");
        objectmap.setActualReturnLevel(handleStatementUtils.joinWithLPC(CurrentSecurityDomain.bottom()));
        logger.info("Actual return level is: "+ handleStatementUtils.joinWithLPC(CurrentSecurityDomain.bottom()).toString());
    }

    /**
     * Set returnlevel to the level of local.
     *
     * @param var signature of local
     */
    public void returnLocal(String var) {
        Object level = localmap.getLevel(var);
        logger.log(Level.INFO, "Return Local {0} with level {1}", new Object[]{var, level});
        objectmap.setActualReturnLevel(level);
    }

    /**
     * Store the levels of the arguments in a list in LocalMap.
     *
     * @param arguments List of arguments
     */
    public void storeArgumentLevels(String... arguments) {
        logger.info("Store arguments " + Arrays.toString(arguments) + " in LocalMap");
        ArrayList<Object> levelArr = new ArrayList<Object>();
        for (String el : arguments) {
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
     * @param dominatorIdentity identity of the postdominator.
     * @param args              List of signatore-string of all locals.
     */
    public void checkCondition(String dominatorIdentity, String... args) {
        logger.info("Check condition of ifStmt");
        localmap.pushLocalPC(handleStatementUtils.joinWithLPC(handleStatementUtils.joinLocals(args)), Integer.valueOf(dominatorIdentity));
        objectmap.pushGlobalPC(handleStatementUtils.joinWithGPC(localmap.getLocalPC()));
        logger.info("New LPC is " + localmap.getLocalPC().toString());
    }

    /**
     * Exit scope of an if-Statement. For each if-statement which ends at this
     * position one lpc is popped from LPCstack. If the lpc belongs to this
     * position is checked with the identity of the position.
     *
     * @param dominatorIdentity identity of the dominator.
     */
    public void exitInnerScope(String dominatorIdentity) {
        while (localmap.dominatorIdentityEquals(Integer.valueOf(dominatorIdentity))) {
            logger.info("Pop LPC for identity " + dominatorIdentity);
            localmap.popLocalPC(Integer.valueOf(dominatorIdentity));
            objectmap.popGlobalPC();    // pop needs to be removed
        }
    }

    /**
     * Join the level of the local to the assignment-level. This possibly
     * increases, never decreases, the assignment-level.
     * <p>
     * This method is called when an assign happens.
     *
     * @param var signature of the local.
     * @return security-level of the local.
     */
    public Object joinLevelOfLocalAndAssignmentLevel(String var) {

        Object localLevel = localmap.getLevel(var);
        objectmap.setAssignmentLevel(handleStatementUtils.joinLevels(objectmap.getAssignmentLevel(), localLevel));
        logger.log(Level.INFO,"Set assignment-level to level " + objectmap.getAssignmentLevel() + " because of " + var);
        return objectmap.getAssignmentLevel();
    }

    /**
     * This method is for instance fields and for static fields. Join the level
     * of the field to the assignment-level.
     *
     * @param object The object of the field.
     * @param field  Signature of the field.
     * @return SecurityLevel of the field.
     */
    public Object joinLevelOfFieldAndAssignmentLevel(Object object, String field) {
        Object fieldLevel = objectmap.getFieldLevel(object, field);
        logger.log(Level.INFO,"Set assignment-level to level {0} (which is the level of " + "local {1})", new Object[]{fieldLevel, field});
        objectmap.setAssignmentLevel(handleStatementUtils.joinLevels(objectmap.getAssignmentLevel(), fieldLevel));
        return objectmap.getAssignmentLevel();
    }

    /**
     * Join the level of the field to the assignment-level.
     *
     * @param object The object of the field.
     * @param field  Signature of the field.
     * @return SecurityLevel of the field.
     */
    public Object joinLevelOfArrayFieldAndAssignmentLevel(Object object, String field) {
        Object fieldLevel = objectmap.getFieldLevel(object, field);
        logger.log(Level.INFO,"Set assignment-level to level {0} (which is the level of " + "local {1})", new Object[]{fieldLevel, field});
        objectmap.setAssignmentLevel(handleStatementUtils.joinLevels(objectmap.getAssignmentLevel(), fieldLevel));
        return objectmap.getAssignmentLevel();
    }

    /**
     * Set the level of the local to given security level.
     *
     * @param var Signature of the local.
     * @param level     security-level
     * @return The new security-level
     */
    public Object setLocal(String var, Object securitylevel) {
        logger.log(Level.INFO, "Set level of local {0} to {1}", new Object[]{var, securitylevel});
        localmap.setLevel(var, securitylevel);
        return localmap.getLevel(var);
    }

    /**
     * Patch to set the security value of a left-hand side, where the
     * statement is a virtualInvoke.
     *
     * @param var
     */
    public void setReturnLevelAfterInvokeStmt(String var) {

        Object leftHandSideSecValue = localmap.getLevel(var);
        leftHandSideSecValue = handleStatementUtils.joinLevels(objectmap.getActualReturnLevel(), leftHandSideSecValue);
        setLocal(var, leftHandSideSecValue);
    }

    /**
     * Set the level of a local to default security-level. Called on every
     * assignment, and on initialization; but not on declaration.
     *
     * @param var signature of the local
     * @return new security-level
     */
    public Object setLocalToCurrentAssingmentLevel(String var) {
        // For assignments like a = x + y, we need to calculate the
        // new security-level of a: this sec-level depends either on
        // the local PC (for example, if inside a high-security if), or on either
        // of the right-hand variables' sec-levels, which is accumulated
        // in the assignmentLevel
        Object newSecValue = handleStatementUtils.joinWithLPC(objectmap.getAssignmentLevel());
        logger.log(Level.INFO, "Set level of local {0} to {1}", new Object[]{var, newSecValue});
        localmap.setLevel(var, newSecValue);
        logger.log(Level.INFO, "New level of local {0} is {1} ", new Object[]{var, localmap.getLevel(var)});
        objectmap.clearAssignmentLevel();
        return localmap.getLevel(var);
    }

    /**
     * This method is for instance-fields and for static-fields.
     *
     * @param object Object of the field.
     * @param field  signature of the field.
     * @return The security-level of the field.
     */
    public Object setLevelOfField(Object object, String field) {
        logger.log(Level.INFO,"Set level of field {0} to {1}",new Object[]{field, handleStatementUtils.joinWithGPC(objectmap.getAssignmentLevel())});
        objectmap.setField(object, field, handleStatementUtils.joinWithGPC(objectmap.getAssignmentLevel()));
        logger.log(Level.INFO, "New level of field {0} is {1}", new Object[]{field, objectmap.getFieldLevel(object, field)});
        objectmap.clearAssignmentLevel();
        return objectmap.getFieldLevel(object, field);
    }

    /**
     * Check the array-field and the local-level of the object against the gpc,
     * and read the level stored as assignment-level. This level - joined with
     * the gpc - is set as the new level for given array-field. This method is
     * needed if the index is a local and must be checked against the gpc.
     *
     * @param object         - Object - The array object
     * @param field          - String - the signature of the field (array
     *                       element)
     * @param localForObject - String - the signature of the local where the
     *                       object is stored
     * @return Returns the new SecurityLevel of the array-element
     */
    public Object setLevelOfArrayField(Object object, String field,String localForObject) {
        logger.log(Level.INFO,"Set level of array-field {0} to {1}",new Object[]{field,handleStatementUtils.joinWithGPC(objectmap.getAssignmentLevel())});
        objectmap.setField(object, field, handleStatementUtils.joinWithGPC(objectmap.getAssignmentLevel()));
        logger.log(Level.INFO, "New level of array-field {0} is {1}", new Object[]{field, objectmap.getFieldLevel(object, field) });
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
     * @param object         - Object - The array object
     * @param field          - String - the signature of the field (array
     *                       element
     * @return Returns the new SecurityLevel of the array-element
     */
    public Object setLevelOfArrayField(Object object, String field) {
        logger.log(Level.INFO,"Set level of array-field {0} to {1}",new Object[]{field, handleStatementUtils.joinWithGPC(objectmap.getAssignmentLevel())});
        objectmap.setField(object, field, handleStatementUtils.joinWithGPC(objectmap.getAssignmentLevel()));
        logger.log(Level.INFO, "New level of array-field {0} is {1}", new Object[]{field, objectmap.getFieldLevel(object, field)});
        objectmap.clearAssignmentLevel();
        return objectmap.getFieldLevel(object, field);
    }

    /**
     * Reference to handleStatementUtils.checkArrayWithGlobalPC
     *
     * @param object
     * @param var
     * @param localForObject
     */
    public void checkArrayWithGlobalPC(Object object, String var, String localForObject) {
        handleStatementUtils.checkArrayWithGlobalPC(object, var, localForObject);
    }

    /**
     * Reference to handeStatementUtils method
     *
     * @param object
     * @param var
     * @param localForObject
     * @param localForIndex
     */
    public void checkArrayWithGlobalPC(Object object, String var, String localForObject, String localForIndex) {
        handleStatementUtils.checkArrayWithGlobalPC(object, var, localForObject, localForIndex);
    }

    /**
     * Check if level of field is greater then global PC
     *
     * @param object
     * @param field
     */
    public void checkGlobalPC(Object object, String field) {
        Object fieldLevel = objectmap.getFieldLevel(object, field);
        Object globalPC = objectmap.getGlobalPC();

        if (!CurrentSecurityDomain.le(globalPC, fieldLevel)) {
            handleStatementUtils.abort(new NSUError(NSU_ERROR_MESSAGE + field));
        }
    }

    /**
     * Check if local > localPC
     *
     * NSU policy: For initialized locals, check if level of given local is greater
     * than localPC. If it's not, throw IFCError
     *
     * @param var the local to test against the localPC
     */
    // TODO: checking the local pc is only a "partial" enforcement primitive, that is, it is never useful by itself. E.g. it is used in assignments and method returns. So, it should be packed together with the other actions needed for the "complete" enforcement primitive.
    // TODO: before fixing the issue above, check why returning from functions  and assignments are different cases.
    public void checkLocalPC(String var) {
        logger.log(Level.INFO, "NSU check for local {0}", var);
        if (localmap == null) {
            throw new InternalAnalyzerException("LocalMap is null");
        }
        //check if local is initialized
        if (!localmap.isTracked(var)) {
            logger.log(Level.INFO, "Local {0} is not tracked; skipping NSU check", var);
            return;
        }

        // the following check must only be executed if local is initialised
        Object level = localmap.getLevel(var);
        Object lpc = localmap.getLocalPC();
        logger.log(Level.INFO, "Check if level of local {0} ({1}) >= lpc ({2}) --- checkLocalPC", new Object[] {var, level, lpc });

        if (!CurrentSecurityDomain.le(lpc, level)) {
            handleStatementUtils.abort(new NSUError(NSU_ERROR_MESSAGE + var));
        }
    }

    /**
     * Check if the current PC is public (i.e. bottom). This methods
     * functions as an NSU check for (non-dynamic) public variable which
     * don't require a cast.
     */
    public void checkNonSensitiveLocalPC() {
        logger.info("NSU check for updating public a variable");
       if (!CurrentSecurityDomain.le(localmap.getLocalPC(),CurrentSecurityDomain.bottom())) {
          handleStatementUtils.abort(new NSUError("Sensitive update to public variable"));
       }
    }

    /**
     * Check level of signature is less/equal than @param level
     *
     * @param signature signature of the local to test
     * @param level     level which mustn't be exceeded
     */
    // TODO: This method is called whenever a local variable is compared to a security level. This comparison happens in several cases: casts, passing of arguments, maybe more. These cases should be distinguished.
    // against remove in favor of more specific checks (casts, etc)
    public void checkThatLe(String signature, String level) {
        checkThatLe(signature, level,"Passed argument " + signature + " with level " + localmap.getLevel(signature) + " to some method" + " which requires a" + " security level of less/equal " + level);
    }

    public void checkCastToStatic(String signature, String level) {
        checkThatLe(signature, level, "Illegal cast to static type " + level + " of " + signature + "(" + localmap.getLevel(signature) + ")");
    }

    public void checkThatLe(String signature, String level, String msg) {
        logger.info("Check if " + signature + " <= " + level);
        if (!CurrentSecurityDomain.le(localmap.getLevel(signature), CurrentSecurityDomain.readLevel(level))) {
            handleStatementUtils.abort(new IllegalFlowError(msg));
        }
    }

    /**
     * This method is used if a print statement is identified. Print statements
     * require the PC to be on a less/equal a certain level (lessThan LOW for
     * system.println, lessThan MEDIUM for SecurePrinter.printMedium(String s);
     *
     * @param level level the PC must be less/equal than.
     */
    @SuppressWarnings("unused")
    public void checkThatPCLe(String level) {
        logger.info("About to print something somewhere. Requires to check that " + "PC is less than "+ level);

        if (!CurrentSecurityDomain.le(localmap.getLocalPC(), CurrentSecurityDomain.readLevel(level))) {
            handleStatementUtils.abort(new IllegalFlowError("Invalid security " + "context: PC " + "must be " + "less/eqal " + level + ", but PC " + "was " + localmap.getLocalPC()));
        }
    }

    public void stopTrackingLocal(String var) {
        logger.log(Level.INFO, "Stop tracking local {0}", var);
        localmap.removeLocal(var);
    }
}

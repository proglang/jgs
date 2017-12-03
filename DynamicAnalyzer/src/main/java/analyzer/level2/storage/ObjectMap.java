package analyzer.level2.storage;

import analyzer.level2.CurrentSecurityDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;
import util.exceptions.InternalAnalyzerException;

import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceIdentityMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The ObjectMap holds all objects which are created in the analyzed code. 
 * To each object belongs a HashMap with the SercurityLevel of the respective 
 * fields.
 * The ObjectMap should never used directly. For each action exists an 
 * appropriate method in {@link analyzer.level2.HandleStmt}.
 * Additionally the ObjectMap holds the SecurityLevels of the arguments and 
 * return variable of the least recently called method.
 * 
 * If we analyze an assignment, assignmentStmtLevel accumulates the security level 
 * of the right-hand-side of the current assignment.
 * 
 * @author Regina König, Karsten Fix (2017)
 * @version 2.0
 */
public class ObjectMap<Level> {

	// <editor-fold desc="Fields">

	/** The Security Domain, that is used for the calculations */
	private SecDomain<Level> secDomain = CurrentSecurityDomain.getInstance();

	// <editor-fold desc="Data structures">

	/** The internal representation of the Object Map.
	 * It maps actual Objects to a Map, that maps the field identifications to their
	 * Security Level.
	 * <br><b>Example:</b> Let C be a class with an int field, that is named foo. Now Let c and d be instances of C,
	 * it could be, that c.foo has a HIGH Security Level, where d.foo is still LOW security Level,
	 * so it would save: c -> foo -> HIGH and d -> foo -> LOW
	 * */
	private ReferenceIdentityMap<Object, HashMap<String, Level>> objectMap;

	/** The internal representation of the global PC or GPC*/
	private LinkedList<Level> globalPC = new LinkedList<>();

	// </editor-fold>

    /** Defines the Security Level of the last checked assign statement
     * of the analyzed code. In specific the right-hand-side security level
     * */
    private Level assignStmtLevel = secDomain.bottom();

	// <editor-fold desc="global levels of last function">

    /** Defines the list of Security Levels of a Methods Arguments, that shall be ordered in the order of appearance */
	private ArrayList<Level> actualArguments = new ArrayList<>();

	/** Defines the Security Level of the last returned value. */
	private Level actualReturnLevel = secDomain.bottom();

	// </editor-fold>

	// </editor-fold>

	// <editor-fold desc="Singleton definition"

	/** The instance of the Object Map, because it is a Singleton.
	 * This is reasonable for the following:
	 * <ul>
	 *     <li>We need access to the same ObjectMap while running the analyzed program.</li>
	 *     <li>Passing an instance of ObjectMap to all Methods is not possible, without manipulating their signature</li>
	 * </ul>
	 **/
	private static final ObjectMap instance = new ObjectMap();

	/**
	 * Gets the instance of the Object Map, that could be used to operate.
	 * @return The only instance of the ObjectMap, which is a Singleton.
	 */
	@SuppressWarnings("unchecked")
	public static <Level> ObjectMap<Level> getInstance() {
		return instance;
	}

	/**
	 * Creates the only instance of the Object Map.
	 * Hereby it initialises the GPC, the Object Map itself and the return Levels
	 */
	private ObjectMap() {
		globalPC.push(secDomain.bottom());
		objectMap = new ReferenceIdentityMap<>(AbstractReferenceMap.ReferenceStrength.WEAK,
											   AbstractReferenceMap.ReferenceStrength.WEAK);
	}

	// </editor-fold>

	// <editor-fold desc="Methods related to last function call">

	/**
	 * Store the arguments security-levels for the next method which will be invoked.
	 * @param args ArrayList containing the security-levels of the arguments
     * @see ObjectMap#getActualArguments()
     * @see ObjectMap#getArgLevelAt(int)
	 */
	public void setActualArguments(ArrayList<Level> args) {
		actualArguments = new ArrayList<>(args);
	}

	/**
	 * Returns ArrayList of the security-levels of the arguments for the least
	 * recently invoked method.
	 * @return ArrayList of the security-levels
     * @see ObjectMap#setActualArguments(ArrayList)
     * @see ObjectMap#getArgLevelAt(int)
	 */
	public ArrayList<Level> getActualArguments() {
		return actualArguments;
	}

	/**
	 * Get the security-level of the argument on the i-th position.
	 * @param i position of the argument
	 * @return SecurityLevel of i-th argument
     * @see ObjectMap#setActualArguments(ArrayList)
     * @see ObjectMap#getActualArguments()
	 */
	public Level getArgLevelAt(int i) {
		return actualArguments.get(i);	
	}

    /**
     * Sets the security level of the last return operation to the given Security Level.
     * @param securityLevel The Security Level of last return statement, that occurred in the analyzed code.
     * @see ObjectMap#getActualReturnLevel()
     */
    public void setActualReturnLevel(Level securityLevel) {
        actualReturnLevel = securityLevel;
    }

    /**
     * Gets the Security Level of the least recently called method.
     * @return The Security Level of the last return Statement.
     * @see ObjectMap#setActualReturnLevel(Object)
     */
    public Level getActualReturnLevel() {
        return actualReturnLevel;
    }

	// </editor-fold>

    // <editor-fold desc="Methods related to assignments.">

    /**
     * Sets the security level of the RHS of the assign statement.
     * @param securityLevel the security level of the last
     * @see ObjectMap#getAssignmentLevel()
     * @see ObjectMap#clearAssignmentLevel()
     */
    public void setAssignmentLevel(Level securityLevel) {
        assignStmtLevel = securityLevel;
    }

    /**
     * Gets the security level of the RHS of the last assign statement.
     * @return the security level of the right-hand-side of the last assignment.
     * @see ObjectMap#setAssignmentLevel(Object)
     * @see ObjectMap#clearAssignmentLevel()
     */
    public Level getAssignmentLevel() {
        return assignStmtLevel;
    }

    /**
     * Sets the security level of RHS of the last assign statement to default-value, that
     * is defined by {@link SecDomain#bottom()}.
     * @see ObjectMap#getAssignmentLevel()
     * @see ObjectMap#setAssignmentLevel(Object)
     */
    public void clearAssignmentLevel() {
        assignStmtLevel = secDomain.bottom();
    }

    // </editor-fold>

    // <editor-fold desc="Global PC Operations">

    // <editor-fold desc="Putting Stuff into GPC">

    /**
     * Puts the given Security Level as top Element of the GPC.
     * This is needed when a method is invoked in the analyzed code.
     * @param securityLevel the security level for globalPC
     * @see ObjectMap#getGlobalPC()
     * @see ObjectMap#popGlobalPC()
     */
    public void pushGlobalPC(Level securityLevel) {
        globalPC.push(securityLevel);
    }

    // </editor-fold>

    // <editor-fold desc="Getting Stuff from GPC">

    /** Gets the Security Level of the global PC without removing it.
     * @return the Security Level, that defines the GPC.
     * @see ObjectMap#pushGlobalPC(Object)
     * @see ObjectMap#popGlobalPC()
     */
    public Level getGlobalPC() {
        return globalPC.getFirst();
    }
    // </editor-fold>

    // <editor-fold desc="Removing Stuff from GPC">

    /**
     * Gets the Security Level from GlobalPC stack and removes it.
     * This is needed when a method in the analyzed code is closed.
     * @return the last globalPC before it was changed.
     * @throws InternalAnalyzerException if GPC is empty.
     * @see ObjectMap#pushGlobalPC(Object)
     * @see ObjectMap#getGlobalPC()
     */
    public Level popGlobalPC() {
        if (globalPC == null || globalPC.size() < 1 )
            throw new InternalAnalyzerException("GPC is empty.");
        if (globalPC.size() > 1) return globalPC.pop();
        return globalPC.getFirst();
    }

    // </editor-fold>

    // </editor-fold>

    // <editor-fold desc="Object Map Operations">

    // <editor-fold desc="Putting Stuff into Map">

    /**
     * Inserts the given instance of an object to the Object Map
     * in case it does not already exist. In Case it exists it will not be overwritten.
     * @param o The instance of an Object, that might have been just created.
     * @see ObjectMap#setField(Object, String, Object)
     */
    public void insertNewObject(Object o) {
        if (!objectMap.containsKey(o)) objectMap.put(o, new HashMap<>());
    }

    /**
     * Sets the security level of the given Object instances field to the given Security Level.
     * In Case the instance or the identification is not known it creates a new entry for it.
     * @param object The instance of an Object, that may be or is then tracked in the Object Map.
     * @param field The signature of the field, which security Level is set.
     * @param securityLevel the security level of the Field for the given instance of an Object
     * @see ObjectMap#insertNewObject(Object)
     * @see ObjectMap#getFieldLevel(Object, String)
     */
    public void setField(Object object, String field, Level securityLevel) {
        HashMap<String, Level> objMap = (!objectMap.containsKey(object)) ? new HashMap<>() : objectMap.get(object);
        objMap.put(field, securityLevel); objectMap.put(object, objMap);
    }

    /**
     * Sets the security level of the given Object instances field to the default Security Level, that is defined by
     * {@link SecDomain#bottom()}.
     * In Case the instance or the identification is not known it creates a new entry for it.
     * @param object The instance of an Object, that may be or is then tracked in the Object Map.
     * @param field The signature of the field, which security Level is set.
     * @see ObjectMap#insertNewObject(Object)
     * @see ObjectMap#getFieldLevel(Object, String)
     * @see ObjectMap#setField(Object, String, Object)
     */
    void addField(Object object, String field) {
        HashMap<String, Level> objMap = (!objectMap.containsKey(object)) ? new HashMap<>() : objectMap.get(object);
        objMap.put(field, secDomain.bottom()); objectMap.put(object, objMap);
    }

    // </editor-fold>

    // <editor-fold desc="Getting Stuff from Object Map">

    /**
     * Gets the security level of the given instances field, which is specified by a identification string.
     * In Case the instance or the identification is not known it creates an entry with the default security level,
     * that is defined by {@link SecDomain#bottom()}
     * @param object The instance of an Object, that may be or is then tracked in the Object Map.
     * @param field The signature of the field, which security Level is asked.
     * @return the security level of the Field for the given instance of an Object
     */
    public Level getFieldLevel(Object object, String field) {
        HashMap<String, Level> objMap = !objectMap.containsKey(object) ? new HashMap<>() : objectMap.get(object);
        if (!objMap.containsKey(field)) objMap.put(field, secDomain.bottom());
        objectMap.put(object, objMap);
        return objectMap.get(object).get(field);
    }

    // </editor-fold>

    // <editor-fold desc="Size Operations">

    /**
     * Tells how many object instances are currently tracked and stored within the Object Map.
     * @return The number of objects contained in the map.
     * @see ObjectMap#getNumberOfFields(Object)
     */
    public int getNumberOfElements() {
        return objectMap.size();
    }

    /**
     * Gets the number of fields, that are currently tracked for an object instance.
     * @param object The Object instance of which the number of tracked fields are asked.
     * @return the number of fields belonging to the given object instance.
     * @see ObjectMap#getNumberOfElements()
     */
    public int getNumberOfFields(Object object) {
        return objectMap.get(object).size();
    }

    // </editor-fold>

    // <editor-fold desc="Contains Operations">

    /**
     * Tells, if the given object instance is currently tracked and stored in the Object Map
     * @param object The Object instance, that could be part of the Object Map.
     * @return <b>true</b> if and only if the object instance is tracked in the Object Map
     * @see ObjectMap#containsField(Object, String)
     */
    public boolean containsObject(Object object) {
        return objectMap.containsKey(object);
    }

    /**
     * Tells, if the given Object instance and the given Field signature is tracked and stored in the Object Map.
     * @param object An Instance of an Object, that could be stored in the Object Map.
     * @param fieldSignature The signature of the field, that could be part of the Object Map.
     * @return <b>true</b> if both are tracked by the Object Map, or <b>false</b> if one is not part in the Object Map
     * @see ObjectMap#containsObject(Object)
     */
    public boolean containsField(Object object, String fieldSignature) {
        return objectMap.containsKey(object) && objectMap.get(object).containsKey(fieldSignature);
    }

    // </editor-fold>

    // </editor-fold>

	/**
	 * Clear the Object map. This operation removes all elements from objectMap
	 * and globalPC stack. The stack then contains only one element 
	 * {@link CurrentSecurityDomain}.bottom().
	 */
	public void flush() {
		objectMap.clear();
		globalPC.clear();
		globalPC.push(secDomain.bottom());
	}
}
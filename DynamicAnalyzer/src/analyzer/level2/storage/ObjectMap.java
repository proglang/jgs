package analyzer.level2.storage;


import analyzer.level2.SecurityLevel;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceIdentityMap;
import utils.exceptions.InternalAnalyzerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The ObjectMap holds all objects which are created in the analyzed code. 
 * To each object belongs a HashMap with the SercurityLevel of the respective fields.
 * The ObjectMap should never used directly. For each action exists an appropriate 
 * method in {@link analyzer.level2.HandleStmt}.
 * Additionally the ObjectMap holds the SecurityLevels of the arguments and 
 * return variable of the least recently called method.
 * 
 * @author Regina König
 * @version 1.0
 */
public class ObjectMap{
	
	private static ReferenceIdentityMap<Object, HashMap<String, SecurityLevel>> innerMap;
	private static LinkedList<SecurityLevel> globalPC;
	private static ObjectMap instance = null;
	private static SecurityLevel actualReturnLevel;
	private static ArrayList<SecurityLevel> actualArguments;
	private static SecurityLevel assignStmtLevel = SecurityLevel.UNDEF;



	/**
	 * Constructor.
	 */
	private ObjectMap() {
		globalPC = new LinkedList<SecurityLevel>();
		globalPC.push(SecurityLevel.LOW); 
		actualReturnLevel = SecurityLevel.HIGH;
		actualArguments = new ArrayList<SecurityLevel>();
		innerMap = new ReferenceIdentityMap<Object, 
				HashMap<String, SecurityLevel>>(
				AbstractReferenceMap.ReferenceStrength.WEAK, 
				AbstractReferenceMap.ReferenceStrength.WEAK);	
	}

	/**
	 * The Objectmap is a singleton. It's instance is used for the whole analysis.
	 * If the instance of the ObjectMap isn't already created, create an instance.
	 * @return The ObjectMap.
	 */
	public static synchronized ObjectMap getInstance() {
		if (instance == null) {
			instance = new ObjectMap();
		}
		return instance;
	}

	/**
	 * Store the argument {@link SecurityLevel} s for the next method which will be invoked.
	 * @param args ArrayList containing {@link SecurityLevel} s of the arguments
	 * @return The ArrayList of currently set argument levels
	 */
	public ArrayList<SecurityLevel> setActualArguments(ArrayList<SecurityLevel> args) {
		actualArguments = args;
		if (actualArguments.size() != args.size()) {
			new InternalAnalyzerException("Wrong number of Arguments");
		}
		return actualArguments;
	}

	/**
	 * Returns ArrayList of {@link SecurityLevel} s of the arguments for the least
	 * recently invoked method.
	 * @return ArrayList of {@link SecurityLevel}s
	 */
	public ArrayList<SecurityLevel> getActualArguments() {
		return actualArguments;
	}

	/**
	 * Get the {@link SecurityLevel} of the argument on the i-th position.
	 * @param i position of the argument
	 * @return SecurityLevel of i-th argument
	 */
	public SecurityLevel getArgLevelAt(int i) {
		if (actualArguments.size() <= i ) {
			new InternalAnalyzerException(
				"You are trying to get argument level at position " + i 
				+ " but the arguments have only size "
				+ actualArguments.size() );	
		}
	  
		return actualArguments.get(i);	
	}

	/**
	 * Push a new globalPC on the stack. This is needed when a method
	 * is invoked in the analyzed code.
	 * @param l {@link SecurityLevel} 
	 * @return recently pushed {@link SecurityLevel} 
	 */
	public SecurityLevel pushGlobalPC(SecurityLevel l) {
		globalPC.push(l);
		return globalPC.getFirst();
	}

	/**
	 * Pops actual globalPC from GlobalPC stack. 
	 * This is needed when a method in the analyzed code is closed.
	 * @return the last globalPC before it was changed.
	 */
	public SecurityLevel popGlobalPC() {
		if (globalPC.size() > 1) {
			return globalPC.pop();
		}
		return globalPC.getFirst();
	}

	/**
	 * @return SecurityLevel of the global PC.
	 */
	public SecurityLevel getGlobalPC() {
		return globalPC.getFirst();
	}

	/**
	 * Set the return level of the actual return operation.
	 * @param l Level of actual return-operation.
	 * @return The return-level.
	 */
	public SecurityLevel setActualReturnLevel(SecurityLevel l) {
		actualReturnLevel = l;
		return actualReturnLevel;
	}

	/**
	 * Get the return level of least recently called method.
	 * @return The return-level of the last return-operation.
	 */
	public SecurityLevel getActualReturnLevel() {
		SecurityLevel returnL = actualReturnLevel;
		return returnL;
	}
  
	/**
 	 * Inserts a new object and creates a new map for the objects fields.
 	 * This method should be called when a new object is created.
 	 * @param o The Object.
 	 */
	public void insertNewObject(Object o) {
		if (!innerMap.containsKey(o)) {
			innerMap.put(o, new HashMap<String, SecurityLevel>());
		}
	}
  
	/**
	 * Clear the Object map. This operation removes all elements from innerMap 
	 * and globalPC stack. The stack then contains only one element 
	 * {@link SecurityLevel}.LOW.
	 */
	public void flush() {
		innerMap.clear();
		globalPC.clear();
		globalPC.push(SecurityLevel.LOW);
	}
 
  
	/**
	 * Get the {@link SecurityLevel} of a field.
	 * @param o The Object it belongs to
	 * @param f The signature of the field
	 * @return {@link SecurityLevel} 
	 */ 
	public SecurityLevel getFieldLevel(Object o, String f) {
		if (!innerMap.containsKey(o)) {
			insertNewObject(o);
		}
		if (!innerMap.get(o).containsKey(f)) {
			setField(o, f);
		}
		return innerMap.get(o).get(f);
	}

	/**
	 * Get the {@link SecurityLevel} of a field.
	 * @param o The Object it belongs to
	 * @param f The signature of the field
	 * @param l The {@link Security Level} of the field
	 * @return {@link SecurityLevel} 
	 */
	public SecurityLevel setField(Object o, String f, SecurityLevel l) {
		innerMap.get(o).put(f, l);
		return innerMap.get(o).get(f);
	}

	/**
	 * Calls {@link setField(Object,String,SecurityLevel)} with {@link SecurityLevel}.LOW.
	 * @param o Object which contains the field.
	 * @param f The field.
	 * @return The new {@link SecurityLevel} of the field.
	 */
	public SecurityLevel setField(Object o, String f) {
		return setField(o, f, SecurityLevel.LOW);
	}
  
	/**
	 * Count the number of elements in innerMap.
	 * @return The number of objects contained in the map.
	 */
	public int getNumberOfElements() {
		return innerMap.size();
	}

	/**
	 * Returns true if the object is contained in the map.
	 * @param o The Object.
	 * @return A boolean. It's true if the object is in the map.
	 */
	public boolean containsObject(Object o) {
		return innerMap.containsKey(o);
	}

	/**
	 * Returns true if the field is contained in the map.
	 * @param o The Object.
	 * @param signature The signature of the field.
	 * @return A boolean. It's true it the field is in the map.
	 */
	public boolean containsField(Object o, String signature) {
		return innerMap.get(o).containsKey(signature);
	}

	/**
	 * Get the number of fields of an object.
	 * @param o The Object.
	 * @return Returns number of fields belonging to the given object.
	 */
	public int getNumberOfFields(Object o) {
		return innerMap.get(o).size();
	}
  
	public void setAssignmentLevel(SecurityLevel l) {
		assignStmtLevel = l;
	}

	public SecurityLevel getAssignmentLevel() {
		return assignStmtLevel;
	}

	public void clearAssignmentLevel() {
		assignStmtLevel = SecurityLevel.LOW;
	}
  
}



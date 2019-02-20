package analyzer.level2.storage;


import analyzer.level2.CurrentSecurityDomain;

import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceIdentityMap;

import util.exceptions.InternalAnalyzerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The ObjectMap holds all objects which are created in the analyzed code. 
 * To each object belongs a HashMap with the Security Level of the respective
 * fields.
 * The ObjectMap should never be used directly. For each action exists an
 * appropriate method in {@link analyzer.level2.HandleStmt}.
 * Additionally the ObjectMap holds the SecurityLevels of the arguments and 
 * return variable of the least recently called method.
 * 
 * If we analyze an assignment, assignmentStmtLevel accumulates the security level 
 * of the right-hand-side of the current assignment.
 * 
 * @author Regina König
 * @version 1.0
 */
public class ObjectMap{
	
	private static ReferenceIdentityMap<Object, HashMap<String, Object>> innerMap;		// maps objects to 2nd map: field -> security type
																						// example: ob1 -> map1, and map1: this.myInt -> LOW
	private static LinkedList<Object> globalPC;
	private static ObjectMap instance = null;
	private static Object actualReturnLevel;
	private static ArrayList<Object> actualArguments;
	private static Object assignStmtLevel = CurrentSecurityDomain.bottom();


	/**
	 * Constructor.
	 */
	public ObjectMap() {
		globalPC = new LinkedList<Object>();
		globalPC.push(CurrentSecurityDomain.bottom());
		actualReturnLevel = CurrentSecurityDomain.bottom();		// was top!?!
		actualArguments = new ArrayList<Object>();
		innerMap = new ReferenceIdentityMap<Object, 
				HashMap<String, Object>>(
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
	 * Store the arguments security-levels for the next method which will be invoked.
	 * @param args ArrayList containing the security-levels of the arguments
	 * @return The ArrayList of currently set argument levels
	 */
	public ArrayList<Object> setActualArguments(ArrayList<Object> args) {		
		if (args == null) {
			throw new InternalAnalyzerException(
					"Received a null pointer as argument list.");
		}
		actualArguments = args;
		return actualArguments;
	}

	/**
	 * Returns ArrayList of the security-levels of the arguments for the least
	 * recently invoked method.
	 * @return ArrayList of the security-levels
	 */
	public ArrayList<Object> getActualArguments() {
		return actualArguments;
	}

	/**
	 * Get the security-level of the argument on the i-th position.
	 * @param i position of the argument
	 * @return SecurityLevel of i-th argument
	 */
	public Object getArgLevelAt(int i) {
		if (actualArguments.size() <= i ) {
			throw new InternalAnalyzerException(
				"You are trying to get argument level at position " + i 
				+ " but the arguments have only size "
				+ actualArguments.size() );	
		}
	  
		return actualArguments.get(i);	
	}

	/**
	 * Push a new globalPC on the stack. This is needed when a method
	 * is invoked in the analyzed code.
	 * @param securityLevel new security-level for globalPC
	 * @return recently pushed {@link CurrentSecurityDomain}
	 */
	public Object pushGlobalPC(Object securityLevel) {
		globalPC.push(securityLevel);
		return globalPC.getFirst();
	}

	/**
	 * Pops actual globalPC from GlobalPC stack. 
	 * This is needed when a method in the analyzed code is closed.
	 * @return the last globalPC before it was changed.
	 */
	public Object popGlobalPC() {
		if (globalPC.size() < 1 || globalPC == null) {
			throw new InternalAnalyzerException("GPC-stack is empty");
		}
		if (globalPC.size() > 1) {
			return globalPC.pop();
		}
		return globalPC.getFirst();
	}

	/** Returns SecurityLevel of the global PC without removing it.
	 * @return SecurityLevel
	 */
	public Object getGlobalPC() {
		return globalPC.getFirst();
	}

	/**
	 * Set the return level of the actual return operation.
	 * @param securityLevel Level of actual return-operation.
	 * @return The return-level.
	 */
	public Object setActualReturnLevel(Object securityLevel) {
		actualReturnLevel = securityLevel;
		return actualReturnLevel;
	}

	/**
	 * Get the return level of least recently called method.
	 * @return The return-level of the last return-operation.
	 */
	public Object getActualReturnLevel() {
		return actualReturnLevel;
	}
  
	/**
 	 * Inserts a new object and creates a new map for the objects fields.
 	 * This method should be called when a new object is created.
 	 * @param o The Object.
 	 */
	public void insertNewObject(Object o) {
		if (!innerMap.containsKey(o)) {
			innerMap.put(o, new HashMap<String, Object>());
		}
	}
  
	/**
	 * Clear the Object map. This operation removes all elements from innerMap 
	 * and globalPC stack. The stack then contains only one element 
	 * {@link CurrentSecurityDomain}.bottom().
	 */
	public void flush() {
		innerMap.clear();
		globalPC.clear();
		globalPC.push(CurrentSecurityDomain.bottom());
	}
 
  
	/**
	 * Get the security-level of a field.
	 * @param object The Object it belongs to
	 * @param field The signature of the field
	 * @return security-level 
	 */ 
	public Object getFieldLevel(Object object, String field) {
		if (!innerMap.containsKey(object)) {
			insertNewObject(object);
		}
		if (!innerMap.get(object).containsKey(field)) {
			setField(object, field, CurrentSecurityDomain.bottom());
		}
		return innerMap.get(object).get(field);
	}

	/**
	 * Set the security-level of a field.
	 * @param object The Object it belongs to
	 * @param field The signature of the field
	 * @param securityLevel The security-level of the field
	 * @return security-level 
	 */
	public Object setField(Object object, String field, Object securityLevel) {
		// TODO seems like implicit dependency on getFieldLevel. Correct?
		// If if-stmt is left out, NullPointerException. Thus, i inserted it. - Nico, 04.02.2016
		if (!innerMap.containsKey(object)) {
			insertNewObject(object);
		}

		innerMap.get(object).put(field, securityLevel);
		return innerMap.get(object).get(field);
	}
	
	/**
	 * Add a new field to the objectMap.
	 * @param object The Object it belongs to
	 * @param field The signature of the field
	 * @return security-level
	 */
	Object addField(Object object, String field) {
		innerMap.get(object).put(field, CurrentSecurityDomain.bottom());
		return innerMap.get(object).get(field);
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
	 * @param object The Object.
	 * @return A boolean. It's true if the object is in the map.
	 */
	public boolean containsObject(Object object) {
		return innerMap.containsKey(object);
	}

	/**
	 * Returns true if the field is contained in the map.
	 * @param object The Object.
	 * @param fieldSignature The signature of the field.
	 * @return A boolean. It's true it the field is in the map.
	 */
	public boolean containsField(Object object, String fieldSignature) {
		return innerMap.get(object).containsKey(fieldSignature);
	}

	/**
	 * Get the number of fields of an object.
	 * @param object The Object.
	 * @return Returns number of fields belonging to the given object.
	 */
	public int getNumberOfFields(Object object) {
		return innerMap.get(object).size();
	}
  
	/**
	 * Set the security-level of the actual assign statement.
	 * @param securityLevel the security-level
	 */
	public void setAssignmentLevel(Object securityLevel) {
		assignStmtLevel = securityLevel;
	}

	/**
	 * Get the security-level of the actual assign statement. the assignmentLevel 
	 * accumulates the security level of the right-hand-side of the current assignment.
	 */
	public Object getAssignmentLevel() {
		return assignStmtLevel;
	}

	/**
	 * Set the security-level of the actual assign statement to default-vaule.
	 */
	public void clearAssignmentLevel() {
		assignStmtLevel = CurrentSecurityDomain.bottom();
	}
  
}



package analyzer.level2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;
import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import utils.exceptions.IllegalFlowException;
import utils.exceptions.InternalAnalyzerException;

/**
 * 
 *
 * @author koenigr
 * 
 */
public class HandleStmt {
	
	private final static Logger LOGGER = L2Logger.getLogger();
	
	private LocalMap lm;
	private static ObjectMap om;
	private HandleStmtUtils hsu;
	

	
	/**
	 * This must be called at the beginning of every method in the analyzed code.
	 * It creates a new LocalMap for the method and adjusts the {@link SecurityLevel}
	 * of the globalPC
	 */
	public HandleStmt() {
		LOGGER.log(Level.INFO, "invoke new HandleStmt");
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
		LOGGER.log(Level.INFO, "init HS");
		try {
			L2Logger.setup();
		} catch (IOException e) {
			LOGGER.warning("setting up the logger was not successful");
			e.printStackTrace();
		}
		if (om == null) {
			om = ObjectMap.getInstance();
		}
		om.flush();
		om.clearAssignmentLevel();
	}
	
	/**
	 * This must be called at the end of every method in the analyzed code.
	 * It resets the globalPC to its initial value
	 */
	public void close() {
		LOGGER.log(Level.INFO, "close HS");
		om.popGlobalPC();
	}
	
	protected void abort(String sink) {
		LOGGER.log(Level.SEVERE, "", new IllegalFlowException("System.exit because of illegal flow to " + sink));
	    System.exit(0);
	}
	
	/**
	 * @param l
	 * @return
	 */
	protected SecurityLevel setActualReturnLevel(SecurityLevel l) {
		return om.setActualReturnLevel(l);
	}
	
	/**
	 * @return
	 */
	protected SecurityLevel getActualReturnLevel() {
		return om.getActualReturnLevel();
	}
	
	public void addObjectToObjectMap(Object o) {
		LOGGER.log(Level.INFO, "Insert Object {0} to ObjectMap", o);
		om.insertNewObject(o);
	}
	
	public SecurityLevel addFieldToObjectMap(Object o, String signature) {
		LOGGER.log(Level.INFO, "Add Field {0} to object {1}", new Object[] {signature, o});
		return om.setField(o, signature);
	}
	

	public void addArrayToObjectMap(Object[] a) {
		
		System.out.println("Array length " + a.length);
		
		LOGGER.log(Level.INFO, "Add Array {0} to ObjectMap", a.toString());
		
		addObjectToObjectMap(a);
		for(int i = 0; i < a.length ; i++) {
			addFieldToObjectMap(a, Integer.toString(i));
		}
		
		if (!containsObjectInObjectMap(a)) {
			new InternalAnalyzerException("Adding Object " + a + " to ObjectMap failed");
		}
	}
	
	
	protected boolean containsObjectInObjectMap(Object o) {
		return om.containsObject(o);
	}
	
	protected boolean containsFieldInObjectMap(Object o, String signature) {
		return om.containsField(o, signature);
	}
	
	protected int getNumberOfElements() {
		return om.getNumberOfElements();
	}
	
	protected int getNumberOfFields(Object o) {
		return om.getNumberOfFields(o);
	}
	
	protected SecurityLevel getFieldLevel(Object o, String signature) {
		return om.getFieldLevel(o, signature);
	}
	
	public void makeFieldHigh(Object o, String signature) {
		om.setField(o, signature, SecurityLevel.HIGH);
	}
	
	public void makeFieldLow(Object o, String signature) {
		LOGGER.log(Level.INFO, "Set SecurityLevel of field {0} to LOW", signature);
		om.setField(o, signature, SecurityLevel.LOW);
	}
	
	public void addLocal(String signature, SecurityLevel level) {
	    LOGGER.log(Level.INFO, "Insert Local {0} with Level {1} to LocalMap", new Object[] {signature, level });

		lm.insertElement(signature, level); 
	}
	
	public void addLocal(String signature) {
		LOGGER.log(Level.INFO, "add Local {0}", signature);
		lm.insertElement(signature, SecurityLevel.LOW); 
	}
	
	protected SecurityLevel setLevelOfLocal(String signature, SecurityLevel level) {
		lm.setLevel(signature, level);
		return lm.getLevel(signature);
	}
	
	protected SecurityLevel getLocalLevel(String signature) {
		return lm.getLevel(signature);
	}
	
	public void makeLocalHigh(String signature) {
		lm.setLevel(signature, SecurityLevel.HIGH);
	}
	
	public void makeLocalLow(String signature) {
		lm.setLevel(signature, SecurityLevel.LOW);
	}




	
	protected SecurityLevel setLocalPC(SecurityLevel l) {
		lm.setLocalPC(l);
		return lm.getLocalPC();
	}
	
	protected SecurityLevel getLocalPC() {
		return lm.getLocalPC();
	}
	
	protected SecurityLevel pushGlobalPC(SecurityLevel l) {
		LOGGER.log(Level.INFO, "Set globalPC to {0}", l);
		om.pushGlobalPC(l);
		return om.getGlobalPC();
	}
	
	protected SecurityLevel getGlobalPC() {
		return om.getGlobalPC();
	}
	
	protected SecurityLevel popGlobalPC() {
		return om.popGlobalPC();
	}


	
	public SecurityLevel assignArgumentToLocal(int pos, String local) {
		lm.setLevel(local, hsu.joinWithLPC(om.getArgLevelAt(pos)));
		return lm.getLevel(local);
	}
	
	public void assignReturnLevelToLocal(String local) {
		if (!hsu.checkLocalPC(local)) {
			abort(local);
		}
		setLevelOfLocal(local, om.getActualReturnLevel());
	}
	

	public void returnConstant() {
		LOGGER.log(Level.INFO, "Return a constant value");
		om.setActualReturnLevel(lm.getLocalPC()); // TODO ??
		// TODO hier vielleicht eher auch addLevel? Dann kann man es n�mlich weglassen...
		// Das Problem ist aber, wenn der Returnwert nicht assigned wird...
	}

	public void returnLocal(String signature) {
		LOGGER.log(Level.INFO, "Return Local {0}", signature);
		lm.setReturnLevel(lm.getLevel(signature)); // TODO: not needed??
		om.setActualReturnLevel(lm.getLevel(signature)); 
	}
	
	public void storeArgumentLevels(String... arguments) {
		
		ArrayList<SecurityLevel> levelArr = new ArrayList<SecurityLevel>();
		for (String el : arguments) {
			levelArr.add(lm.getLevel(el));
		}
		om.setActualArguments(levelArr);
	}
	

	
	public void checkCondition(String... args) {
		lm.pushLocalPC(hsu.joinWithLPC(hsu.joinLocals(args)));
		om.pushGlobalPC(hsu.joinWithGPC(lm.getLocalPC()));
	}
	
	public void exitInnerScope() {
		lm.popLocalPC();
		om.popGlobalPC();
	}
	
	public SecurityLevel addLevelOfLocal(String local) {
		SecurityLevel localLevel = lm.getLevel(local);
		om.setAssignmentLevel(hsu.joinLevels(om.getAssignmentLevel(), localLevel));
		return om.getAssignmentLevel();
	}
	


  /**
   * This method is for instance fields and for static fields.
   * @param o
   * @param field
   * @return
   */
  public SecurityLevel addLevelOfField(Object o, String field) {
    SecurityLevel fieldLevel = om.getFieldLevel(o, field);
    om.setAssignmentLevel(hsu.joinLevels(om.getAssignmentLevel(), fieldLevel));
    return om.getAssignmentLevel();
  }
	
	public SecurityLevel addLevelOfArrayField(Object o, String field) {
		// TODO
		SecurityLevel fieldLevel = om.getFieldLevel(o, field);
		om.setAssignmentLevel(hsu.joinLevels(om.getAssignmentLevel(), fieldLevel));
		return om.getAssignmentLevel();
	}
	
	public SecurityLevel addLevelOfArrayField(Object o, int field) {
		// TODO
		SecurityLevel fieldLevel = om.getFieldLevel(o, Integer.toString(field));
		om.setAssignmentLevel(hsu.joinLevels(om.getAssignmentLevel(), fieldLevel));
		return om.getAssignmentLevel();
	}
	
	public SecurityLevel setLevelOfLocal(String local) {
		
		if (!hsu.checkLocalPC(local)) {
			abort(local);
		}
		lm.setLevel(local, hsu.joinWithLPC(om.getAssignmentLevel()));
		om.clearAssignmentLevel();
		return lm.getLevel(local);
	}
	
	// This method is for Instancefields, Staticfields
	public SecurityLevel setLevelOfField(Object o, String field) {
		if(!hsu.checkGlobalPC(o, field)) {
			abort(o.toString() + field);
		}
		om.setField(o, field, hsu.joinWithGPC(om.getAssignmentLevel()));
		om.clearAssignmentLevel();
		return om.getFieldLevel(o, field);
	}
	
	public SecurityLevel setLevelOfArrayField(Object o, int field, String localForObject, String localForIndex) {
		if(!hsu.checkArrayWithGlobalPC(o, Integer.toString(field), localForObject, localForIndex)) {
			abort(o.toString() + Integer.toString(field));
		}
		om.setField(o, Integer.toString(field), hsu.joinWithGPC(om.getAssignmentLevel()));
		om.clearAssignmentLevel();
		return om.getFieldLevel(o, Integer.toString(field));
	}
	
	/**
	 * @param o - Object - The array object
	 * @param field - String - the signature of the field (array element)
	 * @param localForObject - String - the signature of the Object
	 * @return Returns the new SecurityLevel of the array-element
	 */
	public SecurityLevel setLevelOfArrayField(Object o, String field, String localForObject) {
		if(!hsu.checkArrayWithGlobalPC(o, field, localForObject)) {
			abort(o.toString() + field);
		}
		om.setField(o, field, hsu.joinWithGPC(om.getAssignmentLevel()));
		om.clearAssignmentLevel();
		return om.getFieldLevel(o, field);
		
	}


	public void checkThatNotHigh(String local) {
		System.out.println(lm.getLevel(local));
		System.out.println(SecurityLevel.HIGH);
		if (lm.getLevel(local) == SecurityLevel.HIGH) {
			System.out.println("it's high");
			throw new IllegalFlowException("Passed argument " 
		+ local + " with a high security level to a method which doesn't allow it.");
			
		}
	}

}

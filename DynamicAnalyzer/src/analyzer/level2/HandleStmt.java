package analyzer.level2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Formatter;
import logging.L2Logger;
import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import exceptions.IllegalFlowException;

/**
 * Blabl
 *
 * @author koenigr
 * 
 */
public class HandleStmt {
	
	private final static Logger LOGGER = L2Logger.getLogger();
	
	private LocalMap lm;
	private static ObjectMap om;
	

	
	/**
	 * This must be called at the beginning of every method in the analyzed code.
	 * It creates a new LocalMap for the method and adjusts the {@link SecurityLevel}
	 * of the globalPC
	 */
	public HandleStmt() {
		lm = new LocalMap();
		om = ObjectMap.getInstance();
		om.pushGlobalPC(joinLevels(om.getGlobalPC(), lm.getLocalPC()));
	}
	
	/**
	 * This method must be called just once at the beginning of the main method.
	 * It triggers the setup of the logger.
	 */
	public static void init() {
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
	}
	
	/**
	 * This must be called at the end of every method in the analyzed code.
	 * It resets the globalPC to its initial value
	 */
	public void close() {
		om.popGlobalPC();
	}
	
	public void abort(String sink) {
		LOGGER.log(Level.SEVERE, "", new IllegalFlowException("System.exit because of illegal flow to " + sink));
	    System.exit(0);
	}
	
	/**
	 * @param l
	 * @return
	 */
	public SecurityLevel setActualReturnLevel(SecurityLevel l) {
		return om.setActualReturnLevel(l);
	}
	
	/**
	 * @return
	 */
	public SecurityLevel getActualReturnLevel() {
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
	
	public boolean containsObjectInObjectMap(Object o) {
		return om.containsObject(o);
	}
	
	public boolean containsFieldInObjectMap(Object o, String signature) {
		return om.containsField(o, signature);
	}
	
	public int getNumberOfElements() {
		return om.getNumberOfElements();
	}
	
	public int getNumberOfFields(Object o) {
		return om.getNumberOfFields(o);
	}
	
	public SecurityLevel getFieldLevel(Object o, String signature) {
		return om.getFieldLevel(o, signature);
	}
	
	public void makeFieldHigh(Object o, String signature) {
		om.setField(o, signature, SecurityLevel.HIGH);
	}
	
	public void makeFieldLow(Object o, String signature) {
		om.setField(o, signature, SecurityLevel.LOW);
	}
	
	public void addLocal(String signature, SecurityLevel level) {
	    LOGGER.log(Level.INFO, "Insert Local {0} with Level {1} to LocalMap", new Object[] {signature, level });

		lm.insertElement(signature, level); 
	}
	
	public void addLocal(String signature) {
		lm.insertElement(signature, SecurityLevel.LOW); 
	}
	
	public SecurityLevel setLocalLevel(String signature, SecurityLevel level) {
		lm.setLevel(signature, level);
		return lm.getLevel(signature);
	}
	
	public SecurityLevel getLocalLevel(String signature) {
		return lm.getLevel(signature);
	}
	
	public void makeLocalHigh(String signature) {
		lm.setLevel(signature, SecurityLevel.HIGH);
	}
	
	public void makeLocalLow(String signature) {
		lm.setLevel(signature, SecurityLevel.LOW);
	}



	/**
	 * Joins the Levels of the given locals and the local pc
	 * @param stringList 
	 * @return
	 */
	public SecurityLevel joinLocals(String... stringList) {
		SecurityLevel result = SecurityLevel.LOW;
		for(String op: stringList) {
			if (lm.getLevel(op) == SecurityLevel.HIGH) {
				result = SecurityLevel.HIGH;
			}
		}
		return result;
	}
	
	public SecurityLevel joinWithLPC(SecurityLevel l) {
		SecurityLevel result = SecurityLevel.LOW;
		if (lm.getLocalPC() == SecurityLevel.HIGH || l == SecurityLevel.HIGH) {
			result = SecurityLevel.HIGH;
		}
		return result;
	}
	
	public SecurityLevel joinWithGPC(SecurityLevel l) {
		SecurityLevel result = SecurityLevel.LOW;
		if (om.getGlobalPC() == SecurityLevel.HIGH || l == SecurityLevel.HIGH) {
			result = SecurityLevel.HIGH;
		}
		return result;
	}
	
	public SecurityLevel setLocalPC(SecurityLevel l) {
		lm.setLocalPC(l);
		return lm.getLocalPC();
	}
	
	public SecurityLevel getLocalPC() {
		return lm.getLocalPC();
	}
	
	public SecurityLevel pushGlobalPC(SecurityLevel l) {
		om.pushGlobalPC(l);
		return om.getGlobalPC();
	}
	
	public SecurityLevel getGlobalPC() {
		return om.getGlobalPC();
	}
	
	public SecurityLevel popGlobalPC() {
		return om.popGlobalPC();
	}

	public boolean checkLocalPC(String signature) {
		LOGGER.log(Level.INFO, "Check if {0} >= {1}", new Object[] {lm.getLevel(signature), lm.getLocalPC()  });
		boolean res = true;
		if (lm.getLevel(signature) == SecurityLevel.LOW && lm.getLocalPC() == SecurityLevel.HIGH) {
			res = false;
		}
		return res;		
	}
	
	public boolean checkGlobalPC(Object o, String signature) {
		LOGGER.log(Level.INFO, "Check if {0} >= {1}", new Object[] {om.getFieldLevel(o, signature), om.getGlobalPC()  });
		boolean res = true;
		if (om.getFieldLevel(o, signature) == SecurityLevel.LOW && om.getGlobalPC() == SecurityLevel.HIGH) {
			res = false;
		}
		return res;		
	}

	public SecurityLevel assignLocalsToField(Object o, String field, String... locals) {
		LOGGER.log(Level.INFO, "Assign {0} to {1}", new Object[] {locals, field});
		if (!checkGlobalPC(o, field)) {
			abort(field);
		}
		om.setField(o, field, joinWithGPC(joinLocals(locals)));
		return om.getFieldLevel(o, field);
	}
	
	public SecurityLevel assignLocalsToLocal(String leftOp, String... rightOp) {
		LOGGER.log(Level.INFO, "Assign {0} to {1}", new Object[] {rightOp, leftOp});
		if (!checkLocalPC(leftOp)) {
			abort(leftOp);
		}
		checkLocalPC(leftOp);
		lm.setLevel(leftOp, joinWithLPC(joinLocals(rightOp)));
		return lm.getLevel(leftOp);
	}
	
	public SecurityLevel assignFieldToLocal(Object o,
			String local, String field) {
		LOGGER.log(Level.INFO, "Assign {0} to {1}", new Object[] {field, local});
		if (!checkLocalPC(local)) {
			abort(local);
		}
		lm.setLevel(local, joinWithLPC(om.getFieldLevel(o,  field)));
		return lm.getLevel(local);
	}
	
	public SecurityLevel assignArgumentToLocal(int pos, String local) {
		lm.setLevel(local, joinWithLPC(om.getArgLevelAt(pos)));
		return lm.getLevel(local);
	}
	

	public void returnConstant() {
		// TODO
		om.setActualReturnLevel(lm.getLocalPC()); // ??
	}

	public void returnLocal(String signature) {
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
	

	
	public SecurityLevel joinLevels(SecurityLevel... levels) {
		SecurityLevel res = SecurityLevel.LOW;
		for (SecurityLevel l: levels) {
			if ( l == SecurityLevel.HIGH) {
				res = SecurityLevel.HIGH;
			}
		}
		return res;
	}
	
	public void checkCondition(String... args) {
		lm.pushLocalPC(joinWithLPC(joinLocals(args)));
		om.pushGlobalPC(joinWithGPC(lm.getLocalPC()));
	}
	
	public void exitInnerScope() {
		lm.popLocalPC();
		om.popGlobalPC();
	}
}

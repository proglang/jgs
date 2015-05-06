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

public class HandleStmt {
	
	private static final String LOGGER_NAME = HandleStmt.class.getName();
	private final static Logger LOGGER = L2Logger.getLogger();
	
	private LocalMap lm;
	private ObjectMap om;
	
	/**
	 * This method must be called at the beginning of the main method
	 */
	public static void init() {
		try {
			L2Logger.setup();
		} catch (IOException e) {
			LOGGER.warning("setting up the logger was not successful");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public HandleStmt() {
		lm = new LocalMap();
		om = ObjectMap.getInstance();
		om.pushLocalMap(lm);
	}
	
	/**
	 * 
	 */
	public void close() {
		om.popLocalMap();
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
		return om.contains(o);
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
	
	public void makeFieldLOW(Object o, String signature) {
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

	public SecurityLevel assignLocalsToLocal(String leftOp, String... rightOp) {
		LOGGER.log(Level.INFO, "Assign {0} to {1}", new Object[] {rightOp, leftOp});
		if (!checkLocalPC(leftOp)) {
			abort(leftOp);
		}
		checkLocalPC(leftOp);
		lm.setLevel(leftOp, joinLocals(rightOp));
		return lm.getLevel(leftOp);
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
		if (lm.getLocalPC() == SecurityLevel.HIGH) {
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
		om.setField(o, field, joinLocals(locals));
		return om.getFieldLevel(o, field);
	}
	

	public void returnConstant() {
		// TODO
		om.setActualReturnLevel(lm.getLocalPC()); // ??
	}

	public void returnLocal(String signature) {
		lm.setReturnLevel(lm.getLevel(signature)); // TODO: not needed??
		om.setActualReturnLevel(lm.getLevel(signature)); 
	}

	public SecurityLevel assignFieldToLocal(Object o,
			String local, String field) {
		LOGGER.log(Level.INFO, "Assign {0} to {1}", new Object[] {field, local});
		if (!checkLocalPC(local)) {
			abort(local);
		}
		lm.setLevel(local, om.getFieldLevel(o,  field));
		return lm.getLevel(local);
	}
	
	public void abort(String sink) {

		LOGGER.log(Level.SEVERE, "", new IllegalFlowException("System.exit because of illegal flow to " + sink));
	    System.exit(0);
	}
	
	public void storeArgumentLevels(String... arguments) {
		ArrayList<SecurityLevel> levelArr = new ArrayList<SecurityLevel>();
		for (String el : arguments) {
			levelArr.add(lm.getLevel(el));
		}
		om.setActualArguments(levelArr);
	}
	
	public SecurityLevel assignArgumentToLocal(int pos, String local) {
		lm.setLevel(local, om.getArgLevelAt(pos));
		return lm.getLevel(local);
	}
}

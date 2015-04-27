package analyzer.level2;

import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import exceptions.IllegalFlowException;

public class HandleStmt {
	LocalMap lm = new LocalMap();
	ObjectMap om = ObjectMap.getInstance();
	
	public Level setActualReturnLevel(Level l) {
		return om.setActualReturnLevel(l);
	}
	
	public Level getActualReturnLevel() {
		return om.getActualReturnLevel();
	}
	
	public void addObjectToObjectMap(Object o) {
		System.out.println("Insert Object " + o +" to ObjectMap\n");
		om.insertNewObject(o);
	}
	
	public Level addFieldToObjectMap(Object o, String signature) {
		System.out.println("Add Field " + signature + " to object " + o + "\n");
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
	
	public Level getFieldLevel(Object o, String signature) {
		return om.getFieldLevel(o, signature);
	}
	
	public void makeFieldHigh(Object o, String signature) {
		om.setField(o, signature, Level.HIGH);
	}
	
	public void makeFieldLOW(Object o, String signature) {
		om.setField(o, signature, Level.LOW);
	}
	
	public void addLocal(String signature, Level level) {
		lm.insertElement(signature, level); 
	}
	
	public void addLocal(String signature) {
		lm.insertElement(signature, Level.LOW); 
	}
	
	public Level setLocalLevel(String signature, Level level) {
		lm.setLevel(signature, level);
		return lm.getLevel(signature);
	}
	
	public Level getLocalLevel(String signature) {
		return lm.getLevel(signature);
	}
	
	public void makeLocalHigh(String signature) {
		lm.setLevel(signature, Level.HIGH);
	}
	
	public void makeLocalLow(String signature) {
		lm.setLevel(signature, Level.LOW);
	}

	public Level assignLocalsToLocal(String leftOp, String... rightOp) {
		System.out.println("Assign " + rightOp + " to " + leftOp);
		System.out.println("Check if " + lm.getLevel(leftOp) + " >= " + lm.getLocalPC());
		if (!checkLocalPC(leftOp)) {
			abort(leftOp);
		}
		checkLocalPC(leftOp);
		lm.setLevel(leftOp, joinLocals(rightOp));
		System.out.println("Set " + leftOp + " to level " + lm.getLevel(leftOp) + "\n");
		return lm.getLevel(leftOp);
	}

	/**
	 * Joins the Levels of the given locals and the local pc
	 * @param stringList 
	 * @return
	 */
	public Level joinLocals(String... stringList) {
		Level result = Level.LOW;
		for(String op: stringList) {
			if (lm.getLevel(op) == Level.HIGH) {
				result = Level.HIGH;
			}
		}
		if (lm.getLocalPC() == Level.HIGH) {
			result = Level.HIGH;
		}
		return result;
	}
	
	public Level setLocalPC(Level l) {
		lm.setLocalPC(l);
		return lm.getLocalPC();
	}
	
	public Level getLocalPC() {
		return lm.getLocalPC();
	}

	public boolean checkLocalPC(String signature) {
		boolean res = true;
		if (lm.getLevel(signature) == Level.LOW && lm.getLocalPC() == Level.HIGH) {
			res = false;
		}
		return res;		
	}
	
	public boolean checkLocalPC(Object o, String signature) {
		boolean res = true;
		if (om.getFieldLevel(o, signature) == Level.LOW && lm.getLocalPC() == Level.HIGH) {
			res = false;
		}
		return res;		
	}

	public Level assignLocalsToField(Object o, String field, String... locals) {
		System.out.println("Assign " + locals + " to " + field);
		System.out.println("Check if " + om.getFieldLevel(o, field) + " >= " + lm.getLocalPC());
		if (!checkLocalPC(o, field)) {
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

	public Level assignFieldToLocal(Object o,
			String local, String field) {
		System.out.println("Assign " + field + " to " + local);
		System.out.println("Check if " + lm.getLevel(local) + " >= " + lm.getLocalPC());
		if (!checkLocalPC(local)) {
			abort(local);
		}
		lm.setLevel(local, om.getFieldLevel(o,  field));
		return lm.getLevel(local);
	}
	
	public void abort(String sink) {
		try {
		throw new IllegalFlowException("System.exit because of illegal flow to " + sink);
		} catch(IllegalFlowException e) {
			e.printMessage();
			e.printStackTrace();
		    System.exit(0);
		}
	}
}

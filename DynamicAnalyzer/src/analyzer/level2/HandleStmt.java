package analyzer.level2;

import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import exceptions.IllegalFlowException;

public class HandleStmt {
	LocalMap lm = new LocalMap();
	ObjectMap om = ObjectMap.getInstance();
	
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
			try {
			throw new IllegalFlowException("System.exit because of illegal flow to " + leftOp);
			} catch(IllegalFlowException e) {
				e.printMessage();
				e.printStackTrace();
			   // System.exit(0);
			}
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
	
	/**
	 * Joins the Levels of the given fields and the local pc
	 * @param fields 
	 * @return
	 */
	public Level joinFields(Object o, String... fields) {
		Level result = Level.LOW;
		for(String op: fields) {
			if (om.getFieldLevel(o, op) == Level.HIGH) {
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

	public boolean checkLocalPC(String signature) {
		boolean res = true;
		if (lm.getLevel(signature) == Level.LOW && lm.getLocalPC() == Level.HIGH) {
			res = false;
		}
		return res;		
	}

	public Level assignLocalsToField(Object o, String field, String... locals) {
		System.out.println("Assign " + locals + " to " + field);
		System.out.println("Check if " + om.getFieldLevel(o, field) + " >= " + lm.getLocalPC());
		if (!checkLocalPC(field)) {
			try {
			throw new IllegalFlowException("System.exit because of illegal flow to " + field);
			} catch(IllegalFlowException e) {
				e.printMessage();
				e.printStackTrace();
			   // System.exit(0);
			}
		}
		om.setField(o, field, joinLocals(locals));
		return om.getFieldLevel(o, field);
	}
	


	public void returnLocal(String signature) {
		lm.setReturnLevel(lm.getLevel(signature));
		// TODO Auch CalleeReturn aktualisieren
	}

	public Level assignFieldsToLocal(Object o,
			String local, String... fields) {
		System.out.println("Assign " + fields + " to " + local);
		System.out.println("Check if " + lm.getLevel(local) + " >= " + lm.getLocalPC());
		if (!checkLocalPC(local)) {
			try {
			throw new IllegalFlowException("System.exit because of illegal flow to " + local);
			} catch(IllegalFlowException e) {
				e.printMessage();
				e.printStackTrace();
			   // System.exit(0);
			}
		}
		om.setField(o, local, joinFields(fields));
		return om.getFieldLevel(o, local);
	}
}

package analyzer.level1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import logging.L2Logger;
import exceptions.IllegalFlowException;
import analyzer.level1.storage.UnitStore;
import analyzer.level1.storage.LocalStore;
import analyzer.level1.storage.UnitStore.Element;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;
import soot.Body;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootField;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.AddExpr;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.util.Chain;

public class JimpleInjector {
	
	private final static String HANDLE_CLASS = "analyzer.level2.HandleStmt";


	
	static Body b = Jimple.v().newBody();
    static Chain<Unit> units = b.getUnits();
    static Chain<Local> locals = b.getLocals();
  
    static UnitStore unitStore = new UnitStore();
    static LocalStore localStore = new LocalStore();
    

	static Local hs = Jimple.v().newLocal("hs", RefType.v(HANDLE_CLASS));

	// Locals needed to add Locals to Map
	static Local local = Jimple.v().newLocal("local_name", RefType.v("java.lang.String"));
	static Local level = Jimple.v().newLocal("local_level", RefType.v("java.lang.String"));
	
	
	public static void setBody(Body body) {
		b = body;
		units = b.getUnits();
		locals = b.getLocals();
	}
	
	
	public static void invokeHS() {
		locals.add(hs);
		Unit in = Jimple.v().newAssignStmt(hs, Jimple.v().newNewExpr(RefType.v(HANDLE_CLASS)));
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		Expr specialIn = Jimple.v().newSpecialInvokeExpr(hs, Scene.v().makeConstructorRef(Scene.v().getSootClass(HANDLE_CLASS), paramTypes));
		
		units.insertBefore(in, units.getLast());
		units.insertBefore(Jimple.v().newInvokeStmt(specialIn), units.getLast());
	}
  
	public static void initHS() {}

	public static void closeHS() {
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		Expr invokeClose = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), "close", paramTypes, VoidType.v(), false));
		units.insertBefore(Jimple.v().newInvokeStmt(invokeClose), units.getLast());
	}
	
	public void getActualReturnLevel() {}
	
	public void addObjectToObjectMap(Object o) {}
	
	public void addFieldToObjectMap(Object o, String signature) {}
	
	public void makeFieldHigh(Object o, String signature) {}
	
	public void makeFieldLow(Object o, String signature) {}
	
	public void addLocal(String signature, SecurityLevel level) {}
	
	public void addLocal(String signature) {}
	
	public void setLocalLevel(String signature, SecurityLevel level) {}
	
	public void getLocalLevel(String signature) {}
	
	public void makeLocalHigh(String signature) {}
	
	public void makeLocalLow(String signature) {}

	public void assignLocalsToField(Object o, String field, String... locals) {}
	
	public void assignLocalsToLocal(String leftOp, String... rightOp) {}
	
	public void assignFieldToLocal(Object o, String local, String field) {}
	
	public void assignArgumentToLocal(int pos, String local) {}
	
	public void returnConstant() {}

	public void returnLocal(String signature) {}

	public void storeArgumentLevels(String... arguments) {}
	
	public void checkCondition(String... args) {}
	
	public void exitInnerScope() {}
	
	/*
public static void Join(String resStr, String lO, String rO, Unit pos) {

    Local res = Jimple.v().newLocal("res", RefType.v("java.lang.String"));
    Local leftOp = Jimple.v().newLocal("leftOp", RefType.v("java.lang.String"));
    Local rightOp = Jimple.v().newLocal("rightOp", RefType.v("java.lang.String"));
    
    


    locals.add(res);
    locals.add(leftOp);
    locals.add(rightOp);
    
    Stmt l1 = Jimple.v().newAssignStmt(res, StringConstant.v(resStr));
    Stmt l2 = Jimple.v().newAssignStmt(leftOp, StringConstant.v(lO));
    Stmt l3 = Jimple.v().newAssignStmt(rightOp, StringConstant.v(rO));
    
    unitStore.insertElement(unitStore.new Element(l1, pos));
    unitStore.insertElement(unitStore.new Element(l2, pos));
    unitStore.insertElement(unitStore.new Element(l3, pos));
    
	    
    ArrayList<Type> paramTypes3 = new ArrayList<Type>();
   	paramTypes3.add(RefType.v("java.lang.String"));
   	paramTypes3.add(RefType.v("java.lang.String"));
   	paramTypes3.add(RefType.v("java.lang.String"));
   	
   	ArrayList<Local> params3 = new ArrayList<Local>();
   	params3.add(res);
   	params3.add(leftOp);
   	params3.add(rightOp);
   	
   	
   	SootMethodRef methodtI3 = Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), "Join", paramTypes3, VoidType.v(), true);
    Expr methodInvoke3 = Jimple.v().newStaticInvokeExpr(methodtI3, params3);

    unitStore.insertElement(unitStore.new Element(Jimple.v().newInvokeStmt(methodInvoke3), pos));

    b.validate();
  }
  */

  /**
 * @param stmt
 * @param def
 * @param use
 * 
 */
/*
public static void invokeHandleStmtUnit( Unit stmt, List<ValueBox> def, List<ValueBox> use) {
	  System.out.println("invokeHandleStmt");
	  System.out.print("Definition Box: " + def);
	  System.out.println(" Use Box: " + use);
	  
	  Iterator<ValueBox> ubIt = use.iterator();
	  while (ubIt.hasNext()) {
	  ValueBox vb = (ValueBox) ubIt.next();
	  Value v = vb.getValue();
	  if (v instanceof AddExpr ) {
		  Local lO = (Local) ((AddExpr) v).getOp1();
		  Local rO = (Local) ((AddExpr) v).getOp2();
		  Local res = (Local) def.get(0).getValue(); // TODO : das geschickter machen
		  
		  String lOStr = getSignatureForLocal(lO);
		  String rOStr = getSignatureForLocal(rO);
		  String resStr = getSignatureForLocal(res);
		  // TODO ich habe ein Problem damit, dass handleStmt in die Unit chain schreibt und dadurch eine Exception auslöst
		   Join(resStr, lOStr, rOStr, stmt);
		  
	  }
	  }
	  
	  b.validate();
  }
  
  /*
	public static void addFieldToMap(SootField item, Level level) {
	    Local field = Jimple.v().newLocal("field", RefType.v("java.lang.String"));
	    Local levelStr = Jimple.v().newLocal("level", RefType.v("java.lang.String"));
	    locals.add(field);
	    locals.add(levelStr); // TODO hier das enum einsetzen
	    Stmt l1 = Jimple.v().newAssignStmt(field, StringConstant.v(getSignatureForField(item))); 
	    Stmt l2 = Jimple.v().newAssignStmt(field, StringConstant.v(getSignatureForField(item)));    
	    unitStore.insertElement(unitStore.new Element(l1, item));        
	    unitStore.insertElement(unitStore.new Element(l2, units.getFirst())); 
		    
	    ArrayList paramTypes3 = new ArrayList();
	   	paramTypes3.add(RefType.v("java.lang.String"));
	   	paramTypes3.add(RefType.v("java.lang.String"));
	   	
	   	ArrayList<Local> params3 = new ArrayList();
	   	params3.add(field);
	   	params3.add(levelStr);
	   	
	   	
	   	SootMethodRef methodtI3 = Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), "addField", paramTypes3, VoidType.v(), true);
	    Expr methodInvoke3 = Jimple.v().newStaticInvokeExpr(methodtI3, params3);

	    unitStore.insertElement(unitStore.new Element(Jimple.v().newInvokeStmt(methodInvoke3), units.getFirst()));

	    b.validate();
		
	}
  */
  
/*
	
	public static void addUnitsToChain() {
		
		Iterator<Element> UIt = unitStore.getElements().iterator();
		while(UIt.hasNext()) {
			Element item = (Element) UIt.next();
			if (!item.getPosition().equals(units.getFirst())) { // TODO aendern
				System.out.println("POSITION " + item.getUnit() + " " + item.getPosition());
			units.insertBefore(item.getUnit(), units.getFirst()); // TODO: an richtiger Stelle einfügen
			}
		}
		b.validate();
	}
	*/

/*
	public static void addLocalToMap(Local item) {
	    Stmt l1 = Jimple.v().newAssignStmt(local, StringConstant.v(getSignatureForLocal(item))); 
	    Stmt l2 = Jimple.v().newAssignStmt(level, StringConstant.v("High"));    
	    unitStore.insertElement(unitStore.new Element(l1, units.getFirst()));        
	    unitStore.insertElement(unitStore.new Element(l2, units.getFirst())); 
		    
	    ArrayList<Type> paramTypes3 = new ArrayList<Type>();
	   	paramTypes3.add(RefType.v("java.lang.String"));
	   	paramTypes3.add(RefType.v("java.lang.String"));
	   	
	   	ArrayList<Local> params3 = new ArrayList<Local>();
	   	params3.add(item);
	   	params3.add(level);
	   	
	   	
	   	SootMethodRef methodtI3 = Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), "addLocal", paramTypes3, VoidType.v(), true);
	    Expr methodInvoke3 = Jimple.v().newStaticInvokeExpr(methodtI3, params3);

	    unitStore.insertElement(unitStore.new Element(Jimple.v().newInvokeStmt(methodInvoke3), units.getFirst()));

	    b.validate();	
	}


*/

public static void addNeededLocals() {
	locals.add(local);
	locals.add(level);	
}

private static String getSignatureForLocal(Local l) {
	return l.getType() + "_" + l.getName();
}

private static String getSignatureForField(SootField f) {
	return f.getType() + "_" + f.getName();
}


}

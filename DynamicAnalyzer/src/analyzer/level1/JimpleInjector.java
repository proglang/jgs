package analyzer.level1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L1Logger;
import logging.L2Logger;
import exceptions.IllegalFlowException;
import analyzer.level1.storage.UnitStore;
import analyzer.level1.storage.LocalStore;
import analyzer.level1.storage.UnitStore.Element;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;
import soot.ArrayType;
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
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.IntConstant;
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
	static Local local1 = Jimple.v().newLocal("local_name1", RefType.v("java.lang.String"));
	static Local local2 = Jimple.v().newLocal("local_name2", ArrayType.v(RefType.v("java.lang.String"), 1));
	static Local local3 = Jimple.v().newLocal("local_name3", RefType.v("java.lang.String")); // TODO entfernen?
	static Local level = Jimple.v().newLocal("local_level", RefType.v("java.lang.String"));
	
	static Logger LOGGER = L1Logger.getLogger();
	
	public static void setBody(Body body) {
		b = body;
		units = b.getUnits();
		locals = b.getLocals();
	}
	
	
	public static void invokeHS() {
		LOGGER.log(Level.INFO, "Invoke HandleStmt in method {0}", b.getMethod().getName());
		
		locals.add(hs);
		Unit in = Jimple.v().newAssignStmt(hs, Jimple.v().newNewExpr(RefType.v(HANDLE_CLASS)));
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		Expr specialIn = Jimple.v().newSpecialInvokeExpr(
				hs, Scene.v().makeConstructorRef(Scene.v().getSootClass(HANDLE_CLASS), paramTypes));
		
		Iterator<Unit> it = units.iterator();
		Unit pos = null;
		
		int numOfArgs = getStartPos();
		for(int i = 0; i < numOfArgs; i++) {
			pos = it.next();
		}
		
		unitStore.insertElement(unitStore.new Element(in, pos)); 
		unitStore.lastPos = in;
		Unit inv = Jimple.v().newInvokeStmt(specialIn);
		unitStore.insertElement(unitStore.new Element(inv, unitStore.lastPos));
		unitStore.lastPos = inv;
	}
	
	public static void addLocal(Local l) {
		LOGGER.log(Level.INFO, "Add Local {0} in method {1}",new Object[] {
				getSignatureForLocal(l), b.getMethod().getName()});
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		
		String signature = getSignatureForLocal(l);
	    Stmt sig = Jimple.v().newAssignStmt(local1, StringConstant.v(signature));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"addLocal", paramTypes, VoidType.v(),  false), local1);
		Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

	    unitStore.insertElement(unitStore.new Element(sig, unitStore.lastPos));
	    unitStore.lastPos = sig;
		unitStore.insertElement(unitStore.new Element(ass, unitStore.lastPos));
		unitStore.lastPos = ass;
	}
  
	public static void initHS() {
		LOGGER.log(Level.INFO, "Initialize HandleStmt in method {0}", b.getMethod().getName());
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		Expr invokeInit = Jimple.v().newStaticInvokeExpr(
				Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"init", paramTypes, VoidType.v(), true));
		Unit init = Jimple.v().newInvokeStmt(invokeInit);
		unitStore.insertElement(unitStore.new Element(init, unitStore.lastPos));
		unitStore.lastPos = init;
	}

	public static void closeHS() {
		LOGGER.log(Level.INFO, "Close HandleStmt in method {0} {1}", 
				new Object[] {b.getMethod().getName(), System.getProperty("line.separator")});
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		Expr invokeClose = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"close", paramTypes, VoidType.v(), false));
		units.insertBefore(Jimple.v().newInvokeStmt(invokeClose), units.getLast());
	}
	
	public static void assignReturnLevelToLocal(Local l) {
		LOGGER.log(Level.INFO, "Assign return level of invoked method to local {0}", 
				getSignatureForLocal(l));
		
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.String"));
		
		Expr assignRet = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
						Scene.v().getSootClass(HANDLE_CLASS), "assignReturnLevelToLocal", 
						parameterTypes , VoidType.v(), false), 
						StringConstant.v(getSignatureForLocal(l)));
		Unit assignExpr = Jimple.v().newInvokeStmt(assignRet);
		
		unitStore.insertElement(unitStore.new Element(assignExpr, unitStore.lastPos));
		unitStore.lastPos = assignExpr;
	}
	
	public static void addObjectToObjectMap(Object o) {
		LOGGER.log(Level.INFO, "Add object {0} to ObjectMap", o.toString());
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		
		Unit assignThis = Jimple.v().newIdentityStmt(local1, Jimple.v().newThisRef(RefType.v("java.lang.Object")));
		
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "addObjectToObjectMap", 
				parameterTypes, VoidType.v(), false), 
				local1);
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		//units.addFirst(assignThis);
		//unitStore.insertElement(unitStore.new Element(assignExpr, unitStore.lastPos));
		//unitStore.lastPos = assignExpr;
		
	}
	
	public static void addFieldToObjectMap(Object o, String signature) {}
	
	public static void makeFieldHigh(Object o, String signature) {}
	
	public static void makeFieldLow(Object o, String signature) {}
	
	public static void addLocal(String signature, SecurityLevel level) {}
	
	public static void setLocalLevel(String signature, SecurityLevel level) {}
	
	public static void getLocalLevel(String signature) {}
	
	public static void makeLocalHigh(Local l) {
		LOGGER.log(Level.INFO, "Make Local {0} high in method {1}",
				new Object[] {getSignatureForLocal(l), b.getMethod().getName()});
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		
		String signature = getSignatureForLocal(l);
	    Stmt sig = Jimple.v().newAssignStmt(local1, StringConstant.v(signature));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"makeLocalHigh", paramTypes, VoidType.v(),  false), local1);
		Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

	    unitStore.insertElement(unitStore.new Element(sig, unitStore.lastPos));
	    unitStore.lastPos = sig;
		unitStore.insertElement(unitStore.new Element(ass, unitStore.lastPos));
		unitStore.lastPos = ass;
	}
	
	public static void makeLocalLow(Local l) {
		LOGGER.log(Level.INFO, "Make Local {0} low in method {1}",
				new Object[] {getSignatureForLocal(l), b.getMethod().getName()});
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		
		String signature = getSignatureForLocal(l);
	    Stmt sig = Jimple.v().newAssignStmt(local1, StringConstant.v(signature));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"makeLocalLow", paramTypes, VoidType.v(),  false), local1);
		Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

	    unitStore.insertElement(unitStore.new Element(sig, unitStore.lastPos));
	    unitStore.lastPos = sig;
		unitStore.insertElement(unitStore.new Element(ass, unitStore.lastPos));
		unitStore.lastPos = ass;
	}

	public static void assignLocalsToField(Object o, String field, String... locals) {
	}
	
	public static void assignLocalsToLocal(Local leftOp, Local right1, Local right2,
			Unit pos) {
		//assignLocalsToLocal(String leftOp, String... rightOp)
		LOGGER.log(Level.INFO, "Assign Local {0} and Local {1} to Local {2} in method {2}",
				new Object[] {getSignatureForLocal(right1), 
				getSignatureForLocal(right2),getSignatureForLocal(leftOp),  b.getMethod().getName()});
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		paramTypes.add(ArrayType.v(RefType.v("java.lang.String"), 1));
		
		String signatureLeft = getSignatureForLocal(leftOp);
		String signatureRight1 = getSignatureForLocal(right1);
		String signatureRight2 = getSignatureForLocal(right2);

	    Expr strArr = Jimple.v().newNewArrayExpr(RefType.v("java.lang.String"), IntConstant.v(2));
		
	    Stmt sigLeft = Jimple.v().newAssignStmt(local1, StringConstant.v(signatureLeft));
	    Stmt sigRight = Jimple.v().newAssignStmt(local2, strArr);
	    
	    Stmt arrAssign1 = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(
	    		local2, IntConstant.v(0)), StringConstant.v(signatureRight1));
	    Stmt arrAssign2 = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(
	    		local2, IntConstant.v(1)), StringConstant.v(signatureRight2));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"assignLocalsToLocal", paramTypes, RefType.v("analyzer.level2.SecurityLevel"),  false), local1, local2);
		Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

	    unitStore.insertElement(unitStore.new Element(sigLeft, unitStore.lastPos));
	    unitStore.lastPos = sigLeft;
	    unitStore.insertElement(unitStore.new Element(sigRight, unitStore.lastPos));
	    unitStore.lastPos = sigRight;
	    unitStore.insertElement(unitStore.new Element(arrAssign1, unitStore.lastPos));
	    unitStore.lastPos = arrAssign1;
	    unitStore.insertElement(unitStore.new Element(arrAssign2, unitStore.lastPos));
	    unitStore.lastPos = arrAssign2;
		unitStore.insertElement(unitStore.new Element(ass, unitStore.lastPos));
		unitStore.lastPos = pos;
		
	}
	
	public static void assignLocalToLocal(Local leftOp, Local rightOp, Unit pos) { 
		LOGGER.log(Level.INFO, "Assign Local {1} to Local {0} in method {2}",
				new Object[] {getSignatureForLocal(leftOp), 
				getSignatureForLocal(rightOp), b.getMethod().getName()});
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		paramTypes.add(ArrayType.v(RefType.v("java.lang.String"), 1));
		
		String signatureLeft = getSignatureForLocal(leftOp);
		String signatureRight = getSignatureForLocal(rightOp);

	    Expr strArr = Jimple.v().newNewArrayExpr(RefType.v("java.lang.String"), IntConstant.v(1));
		
	    Stmt sigLeft = Jimple.v().newAssignStmt(local1, StringConstant.v(signatureLeft));
	    Stmt sigRight = Jimple.v().newAssignStmt(local2, strArr);
	    
	    Stmt arrAssign = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(local2, IntConstant.v(0)), StringConstant.v(signatureRight));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"assignLocalsToLocal", paramTypes, RefType.v("analyzer.level2.SecurityLevel"),  false), local1, local2);
		Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

	    unitStore.insertElement(unitStore.new Element(sigLeft, unitStore.lastPos));
	    unitStore.lastPos = sigLeft;
	    unitStore.insertElement(unitStore.new Element(sigRight, unitStore.lastPos));
	    unitStore.lastPos = sigRight;
	    unitStore.insertElement(unitStore.new Element(arrAssign, unitStore.lastPos));
	    unitStore.lastPos = arrAssign;
		unitStore.insertElement(unitStore.new Element(ass, unitStore.lastPos));
		unitStore.lastPos = pos;
	}
	
	public static void assignConstantToLocal(Local leftOp, Unit pos) { // TODO assignConstantToLocal in hs
		//assignLocalsToLocal(String leftOp, String... rightOp)
		LOGGER.log(Level.INFO, "Assign Constant to Local {0} in method {1}",
				new Object[] {getSignatureForLocal(leftOp), b.getMethod().getName()});
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		paramTypes.add(ArrayType.v(RefType.v("java.lang.String"), 1));
		
		String signatureLeft = getSignatureForLocal(leftOp);

	    //  $r2 = newarray (java.lang.String)[0];
	    Expr strArr = Jimple.v().newNewArrayExpr(RefType.v("java.lang.String"), IntConstant.v(0));
		
	    Stmt sigLeft = Jimple.v().newAssignStmt(local1, StringConstant.v(signatureLeft));
	    Stmt right = Jimple.v().newAssignStmt(local2, strArr);
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"assignLocalsToLocal", paramTypes, RefType.v("analyzer.level2.SecurityLevel"),  false), local1, local2);
		Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

	    unitStore.insertElement(unitStore.new Element(sigLeft, unitStore.lastPos));
	    unitStore.lastPos = sigLeft;    
	    unitStore.insertElement(unitStore.new Element(right, unitStore.lastPos));
	    unitStore.lastPos = right;
		unitStore.insertElement(unitStore.new Element(ass, unitStore.lastPos));
		unitStore.lastPos = pos;
	}
	
	public static void assignFieldToLocal(Object o, String local, String field) {}
	
	public static void assignArgumentToLocal(int pos, String local) {}
	
	public static void returnConstant() {
		LOGGER.log(Level.INFO, "Return a constant value");
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		
		Expr returnConst = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "returnConstant", 
				parameterTypes, VoidType.v(), false));
	
		unitStore.insertElement(unitStore.new Element(Jimple.v().newInvokeStmt(returnConst),
				unitStore.lastPos)); // TODO es sollte genau vor HS.close stehen
	}

	public static void returnLocal(Local l) {
		LOGGER.log(Level.INFO, "Return Local {0}", getSignatureForLocal(l));
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.String"));
		
		Stmt sig = Jimple.v().newAssignStmt(local1, StringConstant.v(getSignatureForLocal(l)));
		
		Expr returnLocal = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "returnLocal", parameterTypes,
				VoidType.v(), false), local1);
		
		Stmt returnL = Jimple.v().newInvokeStmt(returnLocal);
		
		unitStore.insertElement(unitStore.new Element(sig, unitStore.lastPos));
		unitStore.lastPos = sig;
		unitStore.insertElement(unitStore.new Element(returnL, unitStore.lastPos));
		unitStore.lastPos = returnL;
	}

	public static void storeArgumentLevels(String... arguments) {}
	
	public static void checkCondition(String... args) {}
	
	public static void exitInnerScope() {}

	
public static void addUnitsToChain() {	
	Iterator<Element> UIt = unitStore.getElements().iterator();
	while(UIt.hasNext()) {
		Element item = (Element) UIt.next();
		if (item.getPosition() == null) {
			units.addFirst(item.getUnit());
		} else {
		    units.insertAfter(item.getUnit(), item.getPosition()); 
		}
	}
	
	unitStore.flush();
	b.validate();
}

public static void addNeededLocals() {
	locals.add(local1);
	locals.add(local2);
	locals.add(local3);
	locals.add(level);	
	
	b.validate();
}

private static String getSignatureForLocal(Local l) {
	return l.getType() + "_" + l.getName();
}

private static String getSignatureForField(SootField f) {
	return f.getType() + "_" + f.getName();
}

private static int getStartPos() {
	if (b.getMethod().isConstructor()) {
		return 1;
	} else {
		return b.getMethod().getParameterCount();
	}
}




}

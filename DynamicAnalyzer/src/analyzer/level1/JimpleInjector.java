package analyzer.level1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L1Logger;
import utils.logging.L2Logger;
import utils.exceptions.IllegalFlowException;
import utils.exceptions.InternalAnalyzerException;
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
import soot.SootClass;
import soot.SootField;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.AddExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.Expr;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
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
	static Local local_for_Strings = Jimple.v().newLocal("local_for_Strings", RefType.v("java.lang.String"));
	static Local local_for_String_Arrays = Jimple.v().newLocal("local_for_String_Arrays", ArrayType.v(RefType.v("java.lang.String"), 1));
	static Local local_for_Objects = Jimple.v().newLocal("local_for_Objects", RefType.v("java.lang.Object"));
	static Local level = Jimple.v().newLocal("local_level", RefType.v("java.lang.String")); // TODO brauch ich das?
	
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
	    Stmt sig = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"addLocal", paramTypes, VoidType.v(),  false), local_for_Strings);
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
	
	public static void addInstanceObjectToObjectMap() {
		
		
		if (!(units.getFirst() instanceof IdentityStmt) 
				|| !(units.getFirst().getUseBoxes().get(0).getValue() instanceof ThisRef)) {
			new InternalAnalyzerException("Expected @this reference");
		}

		String thisObj = units.getFirst().getUseBoxes().get(0).getValue().toString();

		LOGGER.log(Level.INFO, "Add object {0} to ObjectMap in method {1}",
				new Object[] {thisObj, b.getMethod().getName()} );

		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		
		// TODO funktioniert das hier oder muss ich eher mit assign Statement arbeiten?
		// Im Moment muesste hier der etwas anderes in der Local gespeichert sein.
		// va muss die Local auch gespeichert werden.
		local_for_Objects = (Local) units.getFirst().getDefBoxes().get(0).getValue();
		
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "addObjectToObjectMap", 
				parameterTypes, VoidType.v(), false), 
				local_for_Objects); 
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		unitStore.insertElement(unitStore.new Element(assignExpr, unitStore.lastPos));
		unitStore.lastPos = assignExpr;
		
	}
	
	public static void addClassObjectToObjectMap(SootClass sc) {
	
		LOGGER.log(Level.INFO, "Add object {0} to ObjectMap in method {1}",
				new Object[] {sc.getName(), b.getMethod().getName()} );

		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		
	/*	System.out.println("Adding class Object:" + sc.getName().replace(".", "/"));	
		ClassConstant cc = ClassConstant.v(sc.getName().replace(".", "/"));
 		System.out.println("Value: " + cc.value); */
		
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "addObjectToObjectMap", 
				parameterTypes, VoidType.v(), false), 
				ClassConstant.v(sc.getName().replace(".", "/"))); 
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		unitStore.insertElement(unitStore.new Element(assignExpr, unitStore.lastPos));
		unitStore.lastPos = assignExpr;

		
	}
	
	public static void addFieldToObjectMap(SootField field) {
		// TODO statische Felder
		LOGGER.log(Level.INFO, "Adding field {0} to ObjectMap in method {1}", 
				new Object[] { field.getSignature() ,b.getMethod().getName()});
		
		if (!(units.getFirst() instanceof IdentityStmt) 
				|| !(units.getFirst().getUseBoxes().get(0).getValue() instanceof ThisRef)) {
			new InternalAnalyzerException("Expected @this reference");
		}
		
		String fieldSignature = getSignatureForField(field);
		
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		parameterTypes.add(RefType.v("java.lang.String"));
		
		// Es ist vollkommen ok, das so zu lassen, weil es schon eine referenz auf eine bestehende Local ist
		Local tmpLocal = (Local) units.getFirst().getDefBoxes().get(0).getValue();
		
		Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(fieldSignature));
		
		
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "addFieldToObjectMap", 
				parameterTypes, VoidType.v(), false), 
				tmpLocal, local_for_Strings);
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
	
		unitStore.insertElement(unitStore.new Element(assignSignature, unitStore.lastPos));
		unitStore.lastPos = assignSignature;
		unitStore.insertElement(unitStore.new Element(assignExpr, unitStore.lastPos));
		unitStore.lastPos = assignExpr;
	}
	
	public static void makeFieldHigh(Object o, String signature) {}
	// TODO: erst machen, wenn das Problem mit dem Feld-Objekt geklärt ist
	public static void makeFieldLow(Object o, String signature) {}
	// TODO: erst machen, wenn das Problem mit dem Feld-Objekt geklärt ist
	
	public static void addLocal(String signature, SecurityLevel level) {} // TODO Brauch ich das?
	
	
	public static void makeLocalHigh(Local l) {
		LOGGER.log(Level.INFO, "Make Local {0} high in method {1}",
				new Object[] {getSignatureForLocal(l), b.getMethod().getName()});
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		
		String signature = getSignatureForLocal(l);
	    Stmt sig = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"makeLocalHigh", paramTypes, VoidType.v(),  false), local_for_Strings);
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
	    Stmt sig = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"makeLocalLow", paramTypes, VoidType.v(),  false), local_for_Strings);
		Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

	    unitStore.insertElement(unitStore.new Element(sig, unitStore.lastPos));
	    unitStore.lastPos = sig;
		unitStore.insertElement(unitStore.new Element(ass, unitStore.lastPos));
		unitStore.lastPos = ass;
	}

	public static void assignLocalsToField(InstanceFieldRef fieldRef, Local... locals) {
		// TODO: Object o, String field, String... locals
	}
	
	public static void assignLocalsToField(StaticFieldRef fieldRef, Local... locals) {
		// TODO: Object o, String field, String... locals
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
		
	    Stmt sigLeft = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signatureLeft));
	    Stmt sigRight = Jimple.v().newAssignStmt(local_for_String_Arrays, strArr);
	    
	    Stmt arrAssign1 = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(
	    		local_for_String_Arrays, IntConstant.v(0)), StringConstant.v(signatureRight1));
	    Stmt arrAssign2 = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(
	    		local_for_String_Arrays, IntConstant.v(1)), StringConstant.v(signatureRight2));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"assignLocalsToLocal", paramTypes, 
						RefType.v("analyzer.level2.SecurityLevel"),  false), local_for_Strings, local_for_String_Arrays);
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
		
	    Stmt sigLeft = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signatureLeft));
	    Stmt sigRight = Jimple.v().newAssignStmt(local_for_String_Arrays, strArr);
	    
	    Stmt arrAssign = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(local_for_String_Arrays, IntConstant.v(0)), StringConstant.v(signatureRight));
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"assignLocalsToLocal", paramTypes, RefType.v("analyzer.level2.SecurityLevel"),  false), local_for_Strings, local_for_String_Arrays);
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
		
	    Stmt sigLeft = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signatureLeft));
	    Stmt right = Jimple.v().newAssignStmt(local_for_String_Arrays, strArr);
		
		Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"assignLocalsToLocal", paramTypes, RefType.v("analyzer.level2.SecurityLevel"),  false), local_for_Strings, local_for_String_Arrays);
		Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

	    unitStore.insertElement(unitStore.new Element(sigLeft, unitStore.lastPos));
	    unitStore.lastPos = sigLeft;    
	    unitStore.insertElement(unitStore.new Element(right, unitStore.lastPos));
	    unitStore.lastPos = right;
		unitStore.insertElement(unitStore.new Element(ass, unitStore.lastPos));
		unitStore.lastPos = pos;
	}
	
	public static void assignFieldToLocal(String local, InstanceFieldRef field) {
		// TODO: Object o, String local, String field
		}
	
	public static void assignFieldToLocal(String local, StaticFieldRef field) {
		// TODO: Object o, String local, String field
		}
	
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
		
		Stmt sig = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(getSignatureForLocal(l)));
		
		Expr returnLocal = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "returnLocal", parameterTypes,
				VoidType.v(), false), local_for_Strings);
		
		Stmt returnL = Jimple.v().newInvokeStmt(returnLocal);
		
		unitStore.insertElement(unitStore.new Element(sig, unitStore.lastPos));
		unitStore.lastPos = sig;
		unitStore.insertElement(unitStore.new Element(returnL, unitStore.lastPos));
		unitStore.lastPos = returnL;
	}

	public static void storeArgumentLevels(Local... lArguments) {
		// TODO check if its ok
		
		LOGGER.log(Level.INFO, "Store Arguments for next method in method {0}",
				b.getMethod().getName());
		
		int length = lArguments.length;
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(ArrayType.v(RefType.v("java.lang.String"), 1));
		
	    Expr paramArray = Jimple.v().newNewArrayExpr(RefType.v("java.lang.String"), IntConstant.v(length));

	    Unit assignNewStringArray = Jimple.v().newAssignStmt(local_for_String_Arrays, paramArray);
	    
	    Unit[] tmpUnitArray = new Unit[length];
	    
		for (int i = 0; i < length; i++) {
			
			// It's possible to get a null vector as an argument. This happens,
			// if a constant is set as argument. // TODO better explanation
			if (lArguments[i] != null) {
				String signature = getSignatureForLocal(lArguments[i]);
				tmpUnitArray[i] = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(local_for_String_Arrays, IntConstant.v(i)), StringConstant.v(signature));
			} else {
				tmpUnitArray[i] = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(local_for_String_Arrays, IntConstant.v(i)), StringConstant.v(" "));
			}
		}
		
		Expr storeArgs = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "storeArgumentLevels", parameterTypes, 
				VoidType.v(), false), local_for_String_Arrays);
		Stmt invokeStoreArgs = Jimple.v().newInvokeStmt(storeArgs);
		
		
		for (Unit el : tmpUnitArray) {
			unitStore.insertElement(unitStore.new Element(el, unitStore.lastPos));
			unitStore.lastPos = el;
		}
		unitStore.insertElement(unitStore.new Element(assignNewStringArray, unitStore.lastPos));
		unitStore.lastPos = assignNewStringArray;
		unitStore.insertElement(unitStore.new Element(invokeStoreArgs, unitStore.lastPos));
		unitStore.lastPos = invokeStoreArgs;
		
	}
	
	public static void checkCondition(Unit pos, Local... locals) {
		// TODO String... args
		LOGGER.log(Level.INFO, "Check condition in method {0}", b.getMethod());
		
		int numberOfLocals = locals.length;
		ArrayList<Type>	paramTypes = new ArrayList<Type>();
		paramTypes.add(ArrayType.v(RefType.v("java.lang.String"), numberOfLocals));
		
		Expr newStringArray = Jimple.v().newNewArrayExpr(
				RefType.v("java.lang.String"), IntConstant.v(numberOfLocals));
		
		Unit assignNewArray = Jimple.v().newAssignStmt(local_for_String_Arrays, newStringArray);
		
		ArrayList<Unit> tmpUnitList = new ArrayList<Unit>();
		
		for (int i = 0; i < numberOfLocals; i ++) {
			Unit assignSignature = Jimple.v().newAssignStmt(
					Jimple.v().newArrayRef(local_for_String_Arrays, IntConstant.v(i)), 
					StringConstant.v(getSignatureForLocal(locals[i])));
			tmpUnitList.add(assignSignature);
		}
		
		Expr invokeCheckCondition = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"checkCondition", paramTypes, VoidType.v(), false), local_for_String_Arrays);
		Unit invokeCC = Jimple.v().newInvokeStmt(invokeCheckCondition);
		
		unitStore.insertElement(unitStore.new Element(assignNewArray, pos));
		unitStore.lastPos = assignNewArray;
		for (Unit u : tmpUnitList) {
			unitStore.insertElement(unitStore.new Element(u, unitStore.lastPos));
			unitStore.lastPos = u;
		}
		unitStore.insertElement(unitStore.new Element(invokeCC, unitStore.lastPos));
		unitStore.lastPos = invokeCC;
		
	}
	
	public static void exitInnerScope() {
		LOGGER.log(Level.INFO, "Exit inner scope in method {0}", b.getMethod().getName());
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		
		Expr specialIn = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), "exitInnerScope", paramTypes, null, false));
		
		Unit inv = Jimple.v().newInvokeStmt(specialIn);
		
		unitStore.insertElement(unitStore.new Element(inv, unitStore.lastPos));
		unitStore.lastPos = inv;
	}
	
	
	/*
	 * ARRAYS
	 */
	
	
	public static void addArrayToObjectMap(Local a, Unit position) {
		LOGGER.log(Level.INFO, "Add array {0} to ObjectMap in method {1}",
				new Object[] {a, b.getMethod().getName()} );
		
		System.out.println("Object type: " + a.getType() + " and type " + a.getClass());
		
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(ArrayType.v(
				RefType.v("java.lang.Object"), 1));
				
		
	
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "addArrayToObjectMap", 
				parameterTypes, VoidType.v(), false), 
				a); 
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		
		unitStore.insertElement(unitStore.new Element(assignExpr, position));
		unitStore.lastPos = assignExpr;
		
		
	} 

	// Wenn die Arrays Felder sind, werden sie zuerst in eine lokale Variable gespeichert
	public static void assignArrayFieldToLocal(Local local, Local ArrayField) {
		// String local, Object a, String arrField
	}
	
	public static void assignLocalsToArrayField(Object a, String arrField,
			String... locals) {}  // TODO
	

	
protected static void addUnitsToChain() {	
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

protected static void addNeededLocals() {
	locals.add(local_for_Strings);
	locals.add(local_for_String_Arrays);
	locals.add(local_for_Objects);
	locals.add(level);	
	
	b.validate();
}

private static String getSignatureForLocal(Local l) {
	return l.getType() + "_" + l.getName();
}

private static String getSignatureForField(SootField f) {
	return f.getSignature();
}

private static String getSignatureForArrayField(Object o) { // TODO
	return " ";
}

private static int getStartPos() {
	int startPos = 0;
	
	// Jimple requires that @param-assignments statements 
	//shall precede all non-identity statements
	if (b.getMethod().isConstructor()) {
		startPos = 1;
	} else {
		startPos = b.getMethod().getParameterCount();
	}
	
	// At the beginning of every non-static method, the this-reference is assigned to a local.
	// Jimple requires, that it's on the first position
	if(!b.getMethod().isStatic()) startPos++;
	
	return startPos;
}







}

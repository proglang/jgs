package analyzer.level1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L1Logger;
import utils.exceptions.InternalAnalyzerException;
import analyzer.level1.storage.UnitStore;
import analyzer.level1.storage.LocalStore;
import analyzer.level1.storage.UnitStore.Element;
import soot.ArrayType;
import soot.Body;
import soot.IntType;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.ArrayRef;
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
  
    static UnitStore unitStore_After = new UnitStore();
    static UnitStore unitStore_Before = new UnitStore();
    static LocalStore localStore = new LocalStore();

	static Local hs = Jimple.v().newLocal("hs", RefType.v(HANDLE_CLASS));

	// Locals needed to add Locals to Map
	static Local local_for_Strings = Jimple.v().newLocal("local_for_Strings", RefType.v("java.lang.String"));
	static Local local_for_String_Arrays = Jimple.v().newLocal("local_for_String_Arrays", ArrayType.v(RefType.v("java.lang.String"), 1));
	static Local local_for_Objects = Jimple.v().newLocal("local_for_Objects", RefType.v("java.lang.Object"));
	
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
		
		unitStore_After.insertElement(unitStore_After.new Element(in, pos)); 
		unitStore_After.lastPos = in;
		Unit inv = Jimple.v().newInvokeStmt(specialIn);
		unitStore_After.insertElement(unitStore_After.new Element(inv, unitStore_After.lastPos));
		unitStore_After.lastPos = inv;
	}
	
	public static void initHS() {
		LOGGER.log(Level.INFO, "Initialize HandleStmt in method {0}", b.getMethod().getName());
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		Expr invokeInit = Jimple.v().newStaticInvokeExpr(
				Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"init", paramTypes, VoidType.v(), true));
		Unit init = Jimple.v().newInvokeStmt(invokeInit);
		unitStore_After.insertElement(unitStore_After.new Element(init, unitStore_After.lastPos));
		unitStore_After.lastPos = init;
	}

	public static void closeHS() {
		LOGGER.log(Level.INFO, "Close HandleStmt in method {0} {1}", 
				new Object[] {b.getMethod().getName(), System.getProperty("line.separator")});
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		Expr invokeClose = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"close", paramTypes, VoidType.v(), false));
		units.insertBefore(Jimple.v().newInvokeStmt(invokeClose), units.getLast());
		// TODO Abh�ngig davon, ob noch ein returnstmt kommt, oder nicht
	}
	
	/*
	 * Adding Elements to map
	 */
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
		

	    unitStore_After.insertElement(unitStore_After.new Element(sig, unitStore_After.lastPos));
	    unitStore_After.lastPos = sig;
		unitStore_After.insertElement(unitStore_After.new Element(ass, unitStore_After.lastPos));
		unitStore_After.lastPos = ass;
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
		
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "addObjectToObjectMap", 
				parameterTypes, VoidType.v(), false), 
				(Local) units.getFirst().getDefBoxes().get(0).getValue()); 
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		unitStore_After.insertElement(unitStore_After.new Element(assignExpr, unitStore_After.lastPos));
		unitStore_After.lastPos = assignExpr;
		
	}
	
	public static void addClassObjectToObjectMap(SootClass sc) {
	
		LOGGER.log(Level.INFO, "Add object {0} to ObjectMap in method {1}",
				new Object[] {sc.getName(), b.getMethod().getName()} );

		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		
		System.out.println("Adding class Object:" + sc.getName().replace(".", "/"));	
		ClassConstant cc = ClassConstant.v(sc.getName().replace(".", "/"));
 		System.out.println("Value: " + cc.value); 
		
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "addObjectToObjectMap", 
				parameterTypes, VoidType.v(), false), 
				ClassConstant.v(sc.getName().replace(".", "/"))); 
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		unitStore_After.insertElement(unitStore_After.new Element(assignExpr, unitStore_After.lastPos));
		unitStore_After.lastPos = assignExpr;

		
	}
	
	public static void addInstanceFieldToObjectMap(SootField field) {
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
	
		unitStore_After.insertElement(unitStore_After.new Element(assignSignature, unitStore_After.lastPos));
		unitStore_After.lastPos = assignSignature;
		unitStore_After.insertElement(unitStore_After.new Element(assignExpr, unitStore_After.lastPos));
		unitStore_After.lastPos = assignExpr;
	}
	
	public static void addStaticFieldToObjectMap(SootField field) {
		LOGGER.info( "Adding static Field " + field.toString() + " to Object Map");
		
		String signature = getSignatureForField(field);
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		parameterTypes.add(RefType.v("java.lang.String"));
		
		SootClass sc = field.getDeclaringClass();
		
		Unit assignDeclaringClass  = Jimple.v().newAssignStmt(local_for_Objects, ClassConstant.v(sc.getName().replace(".", "/")));
		
		Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
		Expr addObj = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
						"addFieldToObjectMap", parameterTypes, VoidType.v(), false),
						local_for_Objects, local_for_Strings);
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		unitStore_After.insertElement(unitStore_After.new Element(assignDeclaringClass, unitStore_After.lastPos));
		unitStore_After.lastPos = assignDeclaringClass;
		unitStore_After.insertElement(unitStore_After.new Element(assignSignature, unitStore_After.lastPos));
		unitStore_After.lastPos = assignSignature;
		unitStore_After.insertElement(unitStore_After.new Element(assignExpr, unitStore_After.lastPos));
		unitStore_After.lastPos = assignExpr;
		
	}
	
	public static void addArrayToObjectMap(Local a, Unit position) {
		LOGGER.log(Level.INFO, "Add array {0} to ObjectMap in method {1}",
				new Object[] {a, b.getMethod().getName()} );
		
		LOGGER.log(Level.INFO, "Object type of array: " + a.getType() + " and type " + a.getClass());
		LOGGER.log(Level.FINEST, "at position {0}", position.toString());
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(ArrayType.v(
				RefType.v("java.lang.Object"), 1));
				
		
	
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "addArrayToObjectMap", 
				parameterTypes, VoidType.v(), false), 
				a); 
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		System.out.println(unitStore_After.lastPos.toString());
		System.out.println(assignExpr.toString());
		System.out.println(position.toString());
		
		unitStore_After.insertElement(unitStore_After.new Element(assignExpr, position));
		unitStore_After.lastPos = assignExpr;		
	} 
	
	/*
	 * Change level of elements
	 */
	public static void makeFieldHigh(Object o, String signature) {}
	// TODO: erst machen, wenn das Problem mit dem Feld-Objekt geklärt ist
	public static void makeFieldLow(Object o, String signature) {}
	// TODO: erst machen, wenn das Problem mit dem Feld-Objekt geklärt ist
	
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
		

	    unitStore_After.insertElement(unitStore_After.new Element(sig, unitStore_After.lastPos));
	    unitStore_After.lastPos = sig;
		unitStore_After.insertElement(unitStore_After.new Element(ass, unitStore_After.lastPos));
		unitStore_After.lastPos = ass;
	}
	
	// TODO makeArrayFields HIGH and LOW
	
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
		

	    unitStore_After.insertElement(unitStore_After.new Element(sig, unitStore_After.lastPos));
	    unitStore_After.lastPos = sig;
		unitStore_After.insertElement(unitStore_After.new Element(ass, unitStore_After.lastPos));
		unitStore_After.lastPos = ass;
	}

	public static void addLevelInAssignStmt(Local l) {
		LOGGER.info("Adding level in assign statement");
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		
		String signature = getSignatureForLocal(l);
		
		Stmt assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
		Expr invokeAddLevel = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"addLevelOfLocal", paramTypes, RefType.v("analyzer.level2.SecurityLevel"),  false), local_for_Strings);
		Unit invoke = Jimple.v().newInvokeStmt(invokeAddLevel);
		

	    unitStore_After.insertElement(unitStore_After.new Element(assignSignature, unitStore_After.lastPos));
	    unitStore_After.lastPos = assignSignature;
	    unitStore_After.insertElement(unitStore_After.new Element(invoke, unitStore_After.lastPos));
	    unitStore_After.lastPos = invoke;
	}
	
	
	/*
	 * AssignStmt
	 */
	public static void addLevelInAssignStmt(InstanceFieldRef f) {
		LOGGER.log(Level.INFO, "Adding level of field {0} in assignStmt in method {1}", 
				new Object[] { f.getField().getSignature() ,b.getMethod().getName()});
		
		if (!(units.getFirst() instanceof IdentityStmt) 
				|| !(units.getFirst().getUseBoxes().get(0).getValue() instanceof ThisRef)) {
			new InternalAnalyzerException("Expected @this reference");
		}
		
		String fieldSignature = getSignatureForField(f.getField());
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		parameterTypes.add(RefType.v("java.lang.String"));
		
		// units.getFirst is already a reference to @this
		// Local tmpLocal = (Local) units.getFirst().getDefBoxes().get(0).getValue();
		Unit assignBase = Jimple.v().newAssignStmt(local_for_Objects, f.getBase());
		System.out.println("Base "+f.getBase());
		
		Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(fieldSignature));
		
		
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "addLevelOfField", 
				parameterTypes, VoidType.v(), false), 
				local_for_Objects, local_for_Strings);
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
	
		unitStore_After.insertElement(unitStore_After.new Element(assignBase, unitStore_After.lastPos));
		unitStore_After.insertElement(unitStore_After.new Element(assignSignature, unitStore_After.lastPos));
		unitStore_After.lastPos = assignSignature;
		unitStore_After.insertElement(unitStore_After.new Element(assignExpr, unitStore_After.lastPos));
		unitStore_After.lastPos = assignExpr;
	}
	
	public static void addLevelInAssignStmt(StaticFieldRef f) {
		LOGGER.info( "Adding Level of static Field " + f.toString() + " in assign stmt");
		
		SootField field = f.getField();

		String signature = getSignatureForField(field);
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		parameterTypes.add(RefType.v("java.lang.String"));
		
		SootClass sc = field.getDeclaringClass();
		
		Unit assignDeclaringClass  = Jimple.v().newAssignStmt(local_for_Objects, ClassConstant.v(sc.getName().replace(".", "/")));
		
		Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
		Expr addObj = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
						"addLevelOfField", parameterTypes, VoidType.v(), false),
						local_for_Objects, local_for_Strings);
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		unitStore_After.insertElement(unitStore_After.new Element(assignDeclaringClass, unitStore_After.lastPos));
		unitStore_After.lastPos = assignDeclaringClass;
		unitStore_After.insertElement(unitStore_After.new Element(assignSignature, unitStore_After.lastPos));
		unitStore_After.lastPos = assignSignature;
		unitStore_After.insertElement(unitStore_After.new Element(assignExpr, unitStore_After.lastPos));
		unitStore_After.lastPos = assignExpr;
	}
	
	public static void addLevelInAssignStmt(ArrayRef a) {
		LOGGER.info( "Add Level of Array " + a.toString() + " in assign stmt");
		// setLevelOfArrayField(Object o, String field, String localForObject, String localForIndex)

		System.out.println("HIER " +  a.getIndex());
		System.out.println("HIER " +  a.getIndex().getType());
		System.out.println("HIER " +  a.getIndex().getUseBoxes());
		System.out.println(a.getIndex() instanceof Local);
			
			Expr addObj = null;
			
			if (!(a.getIndex() instanceof Local)) {
				String signature = getSignatureForArrayField(a);
				LOGGER.info("Signature of array field in jimple injector is" + signature);
				Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
				
				unitStore_After.insertElement(unitStore_After.new Element(assignSignature, unitStore_After.lastPos));
				unitStore_After.lastPos = assignSignature;

				ArrayList<Type> parameterTypes = new ArrayList<Type>();
				parameterTypes.add(RefType.v("java.lang.Object"));
				parameterTypes.add(RefType.v("java.lang.String"));
				
				addObj = Jimple.v().newVirtualInvokeExpr(
						hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
								"addLevelOfArrayField", parameterTypes, VoidType.v(), false),
								a.getBase(), local_for_Strings);
				
			} else if (a.getIndex() instanceof Local) {
				Value fieldIndex = a.getIndex();
				LOGGER.info("Signature of array field in jimple injector is stored in " + fieldIndex.toString());
				

				ArrayList<Type> parameterTypes = new ArrayList<Type>();
				parameterTypes.add(RefType.v("java.lang.Object"));
				parameterTypes.add(IntType.v());
				
				addObj = Jimple.v().newVirtualInvokeExpr(
						hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
								"addLevelOfArrayField", parameterTypes, VoidType.v(), false),
								a.getBase(), fieldIndex);
				
			} else {
				LOGGER.log(Level.SEVERE, "Unexpected type of index",
						new InternalAnalyzerException("Unexpected type of index")); 
				System.exit(0);
			}


			
			Unit assignExpr = Jimple.v().newInvokeStmt(addObj);


			unitStore_After.insertElement(unitStore_After.new Element(assignExpr, unitStore_After.lastPos));
			unitStore_After.lastPos = assignExpr;
	}
	
	public static void setLevelOfAssignStmt(Local l, Unit pos) {
		LOGGER.info("Setting level in assign statement");
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		
		String signature = getSignatureForLocal(l);
		
		Stmt assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
		Expr invokeSetLevel = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"setLevelOfLocal", paramTypes, RefType.v("analyzer.level2.SecurityLevel"),  false), local_for_Strings);
		Unit invoke = Jimple.v().newInvokeStmt(invokeSetLevel);
		

	    unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
	    unitStore_Before.insertElement(unitStore_Before.new Element(invoke, pos));
	    // unitStore_Before.lastPos = pos;
	}
	
	public static void setLevelOfAssignStmt(InstanceFieldRef f, Unit pos) {
		LOGGER.log(Level.INFO, "Set level to field {0} in assignStmt in method {1}", 
				new Object[] { f.getField().getSignature() ,b.getMethod().getName()});
		
		if (!(units.getFirst() instanceof IdentityStmt) 
				|| !(units.getFirst().getUseBoxes().get(0).getValue() instanceof ThisRef)) {
			new InternalAnalyzerException("Expected @this reference");
		}
		
		String fieldSignature = getSignatureForField(f.getField());
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		parameterTypes.add(RefType.v("java.lang.String"));
		
		// units.getFirst is already a reference to @this
		Local tmpLocal = (Local) units.getFirst().getDefBoxes().get(0).getValue();
		
		Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(fieldSignature));
		
		
		Expr addObj	= Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "setLevelOfField", 
				parameterTypes, VoidType.v(), false), 
				tmpLocal, local_for_Strings);
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
	
		unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
		unitStore_Before.insertElement(unitStore_After.new Element(assignExpr, pos));
		// unitStore_Before.lastPos = assignExpr;
	}
	
	public static void setLevelOfAssignStmt(StaticFieldRef f, Unit pos) {
		LOGGER.info( "Set Level of static Field " + f.toString() + " in assign stmt");
		
		SootField field = f.getField();

		String signature = getSignatureForField(field);
		System.out.println("Signature of static field in jimple injector " + signature);
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.Object"));
		parameterTypes.add(RefType.v("java.lang.String"));
		
		SootClass sc = field.getDeclaringClass();
		
		Unit assignDeclaringClass  = Jimple.v().newAssignStmt(local_for_Objects, ClassConstant.v(sc.getName().replace(".", "/")));
		
		Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
		Expr addObj = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
						"setLevelOfField", parameterTypes, VoidType.v(), false),
						local_for_Objects, local_for_Strings);
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
		unitStore_Before.insertElement(unitStore_Before.new Element(assignDeclaringClass, pos));
		unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
		unitStore_Before.insertElement(unitStore_Before.new Element(assignExpr, pos));
		// unitStore_After.lastPos = assignExpr;
	}
	
	public static void setLevelOfAssignStmt(ArrayRef a, Unit pos) {
	LOGGER.info( "Set Level of Array " + a.toString() + " in assign stmt");
	// setLevelOfArrayField(Object o, String field, String localForObject, String localForIndex)

	System.out.println("HIER " +  a.getIndex());
	System.out.println("HIER " +  a.getIndex().getType());
	System.out.println("HIER " +  a.getIndex().getUseBoxes());
	System.out.println(a.getIndex() instanceof Local);
		
		Expr addObj = null;
		
		if (!(a.getIndex() instanceof Local)) {
			String signature = getSignatureForArrayField(a);
			LOGGER.info("Signature of array field in jimple injector is" + signature);
			Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
			
			unitStore_After.insertElement(unitStore_After.new Element(assignSignature, unitStore_After.lastPos));
			unitStore_After.lastPos = assignSignature;

			ArrayList<Type> parameterTypes = new ArrayList<Type>();
			parameterTypes.add(RefType.v("java.lang.Object"));
			parameterTypes.add(RefType.v("java.lang.String"));
			
			addObj = Jimple.v().newVirtualInvokeExpr(
					hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
							"setLevelOfArrayField", parameterTypes, VoidType.v(), false),
							a.getBase(), local_for_Strings);
			
		} else if (a.getIndex() instanceof Local) {
			Value fieldIndex = a.getIndex();
			LOGGER.info("Signature of array field in jimple injector is stored in " + fieldIndex.toString());
			

			ArrayList<Type> parameterTypes = new ArrayList<Type>();
			parameterTypes.add(RefType.v("java.lang.Object"));
			parameterTypes.add(IntType.v());
			
			addObj = Jimple.v().newVirtualInvokeExpr(
					hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
							"setLevelOfArrayField", parameterTypes, VoidType.v(), false),
							a.getBase(), fieldIndex);
			
		} else {
			LOGGER.log(Level.SEVERE, "Unexpected type of index",
					new InternalAnalyzerException("Unexpected type of index")); 
			System.exit(0);
		}


		
		Unit assignExpr = Jimple.v().newInvokeStmt(addObj);


		unitStore_Before.insertElement(unitStore_Before.new Element(assignExpr, pos));
		// unitStore_After.lastPos = assignExpr;
	}
	
	public static void assignReturnLevelToLocal(Local l, Unit pos) {
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
		
		unitStore_After.insertElement(unitStore_After.new Element(assignExpr, pos));
		// unitStore_After.lastPos = assignExpr;
	}
	
	public static void assignArgumentToLocal(int posInArgList, Local local) {
		LOGGER.log(Level.INFO, "Assign argument level to local " + local.toString());
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(IntType.v());
		parameterTypes.add(RefType.v("Java.lang.String"));
		Expr assignArg = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(
						Scene.v().getSootClass(HANDLE_CLASS), "assignArgumentToLocal",
						parameterTypes, RefType.v("analyzer.level2.SecurityLevel"), false),
				IntConstant.v(posInArgList), StringConstant.v(getSignatureForLocal(local))
				);
		
		Unit assignExpr = Jimple.v().newInvokeStmt(assignArg);
		
		unitStore_After.insertElement(unitStore_After.new Element(assignExpr, unitStore_After.lastPos));
		unitStore_After.lastPos = assignExpr;
		
	}
	
	/*
	 *  Inter-scope
	 */
	
	public static void returnConstant(Unit retStmt) {
		LOGGER.log(Level.INFO, "Return a constant value");
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		
		Expr returnConst = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "returnConstant", 
				parameterTypes, VoidType.v(), false));
	
		unitStore_After.insertElement(unitStore_After.new Element(Jimple.v().newInvokeStmt(returnConst),
				unitStore_After.lastPos)); // TODO es sollte genau vor HS.close stehen
	}

	public static void returnLocal(Local l, Unit pos) {
		LOGGER.log(Level.INFO, "Return Local {0}", getSignatureForLocal(l));
		
		ArrayList<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(RefType.v("java.lang.String"));
		
		Stmt sig = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(getSignatureForLocal(l)));
		
		Expr returnLocal = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "returnLocal", parameterTypes,
				VoidType.v(), false), local_for_Strings);
		
		Stmt returnL = Jimple.v().newInvokeStmt(returnLocal);
		
		unitStore_Before.insertElement(unitStore_Before.new Element(sig, pos));
		unitStore_Before.insertElement(unitStore_Before.new Element(returnL, pos));
		unitStore_Before.lastPos = returnL;
	}

	public static void storeArgumentLevels(Local... lArguments) {
		// TODO check if its ok. Add only Locals
		
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
				tmpUnitArray[i] = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(local_for_String_Arrays, IntConstant.v(i)), StringConstant.v(" ")); // TOD unn�tig
			}
		}
		
		Expr storeArgs = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
				Scene.v().getSootClass(HANDLE_CLASS), "storeArgumentLevels", parameterTypes, 
				VoidType.v(), false), local_for_String_Arrays);
		Stmt invokeStoreArgs = Jimple.v().newInvokeStmt(storeArgs);
		
		
		for (Unit el : tmpUnitArray) {
			unitStore_After.insertElement(unitStore_After.new Element(el, unitStore_After.lastPos));
			unitStore_After.lastPos = el;
		}
		unitStore_After.insertElement(unitStore_After.new Element(assignNewStringArray, unitStore_After.lastPos));
		unitStore_After.lastPos = assignNewStringArray;
		unitStore_After.insertElement(unitStore_After.new Element(invokeStoreArgs, unitStore_After.lastPos));
		unitStore_After.lastPos = invokeStoreArgs;
		
	}
	
	public static void checkThatNotHigh(Unit pos, Local l) {
		LOGGER.info("Check that " + l + " is not high");
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		paramTypes.add(RefType.v("java.lang.String"));
		
		String signature = getSignatureForLocal(l);
		
		Stmt assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
		Expr invokeSetLevel = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
						"checkThatNotHigh", paramTypes, VoidType.v(),  false), local_for_Strings);
		Unit invoke = Jimple.v().newInvokeStmt(invokeSetLevel);
		

	    unitStore_After.insertElement(unitStore_After.new Element(assignSignature, unitStore_After.lastPos));
	    unitStore_After.lastPos = assignSignature;
	    unitStore_After.insertElement(unitStore_After.new Element(invoke, unitStore_After.lastPos));
	    unitStore_After.lastPos = pos;
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
		
		unitStore_After.insertElement(unitStore_After.new Element(assignNewArray, pos));
		unitStore_After.lastPos = assignNewArray;
		for (Unit u : tmpUnitList) {
			unitStore_After.insertElement(unitStore_After.new Element(u, pos));
			unitStore_After.lastPos = u;
		}
		unitStore_After.insertElement(unitStore_After.new Element(invokeCC, pos));
		unitStore_After.lastPos = invokeCC;
		
	}
	
	public static void exitInnerScope(Unit pos) {
		LOGGER.log(Level.INFO, "Exit inner scope in method {0}", b.getMethod().getName());
		
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		
		Expr specialIn = Jimple.v().newVirtualInvokeExpr(
				hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), "exitInnerScope", paramTypes, VoidType.v(), false));
		
		Unit inv = Jimple.v().newInvokeStmt(specialIn);
		
		System.out.println("1 " + units.getPredOf(pos).toString());
		System.out.println("2 " + pos.toString());
		
		Unit endOfScope = units.getPredOf(pos);
		
		unitStore_After.insertElement(unitStore_After.new Element(inv, endOfScope));
		unitStore_After.lastPos = inv;
	}
	
/*
 * Internal methods
 */
	
protected static void addUnitsToChain() {	
	Iterator<Element> UIt = unitStore_After.getElements().iterator();
	while(UIt.hasNext()) {
		Element item = (Element) UIt.next();
		if (item.getPosition() == null) {
			units.addFirst(item.getUnit());
		} else {
		    units.insertAfter(item.getUnit(), item.getPosition()); 
		}
	}
	
	UIt = unitStore_Before.getElements().iterator();
	while(UIt.hasNext()) {
		Element item = (Element) UIt.next();
		if (item.getPosition() == null) {
			units.addFirst(item.getUnit());
		} else {
		    units.insertBefore(item.getUnit(), item.getPosition()); 
		}
	} // TODO Reihenfolge tauschen?
	
	unitStore_After.flush();
	unitStore_Before.flush();
	b.validate();
}

protected static void addNeededLocals() {
	locals.add(local_for_Strings);
	locals.add(local_for_String_Arrays);
	locals.add(local_for_Objects);
	
	b.validate();
}

private static String getSignatureForLocal(Local l) {
	return l.getType() + "_" + l.getName();
}

private static String getSignatureForField(SootField f) {
	return f.getSignature();
}

private static String getSignatureForArrayField(ArrayRef a) {
	System.out.println("Type of index: " + a.getIndex().getType()); 
	String result = "";
	if (a.getIndex().getType().toString() == "int") {
		System.out.println("Int Index");
		result = a.getIndex().toString();
	} else {
		LOGGER.log(Level.SEVERE, "Unexpected type of index",
				new InternalAnalyzerException("Unexpected type of index")); 
		System.exit(0);
	}
	return 	result; 
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

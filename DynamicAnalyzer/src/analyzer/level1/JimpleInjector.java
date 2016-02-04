package analyzer.level1;

import analyzer.level1.storage.LocalStore;
import analyzer.level1.storage.UnitStore;
import analyzer.level1.storage.UnitStore.Element;
import soot.ArrayType;
import soot.Body;
import soot.IntType;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
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
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.util.Chain;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L1Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author koenigr
 *
 */
public class JimpleInjector {
	
  /**
   * 
   */
  private static final String HANDLE_CLASS = "analyzer.level2.HandleStmt";


  /**
   * The body of the actually analyzed method.
   */
  static Body b = Jimple.v().newBody();

  /**
   * Chain with all units in the actual method-body. 
   */
  static Chain<Unit> units = b.getUnits();

  /**
   * Chain with all locals in the actual method-body.
   */
  static Chain<Local> locals = b.getLocals();

  /**
   * Chain containing all new units which have to be set
   * after a given position.
   */
  static UnitStore unitStore_After = new UnitStore("UnitStore_After");

  /**
   * Chain containing all new units which have to be set
   * before a given position. 
   */
  static UnitStore unitStore_Before = new UnitStore("UnitStore_Before");

  /**
   *
   */
  static LocalStore localStore = new LocalStore();

  /**
   * 
   */
  static Local hs = Jimple.v().newLocal("hs", RefType.v(HANDLE_CLASS));

  /**
   * Boolean to check whether the extra locals had already been added.
   */
  static boolean extralocals = false;
	
  /**
   * 
   */
  static Local local_for_Strings = Jimple.v().newLocal(
      "local_for_Strings", RefType.v("java.lang.String"));
  
  /**
   * This local is needed for methods
   * with more than two arguments. 
   */
  static Local local_for_Strings2 = Jimple.v().newLocal(
      "local_for_Strings2", RefType.v("java.lang.String"));
  /**
   * This locals is needed for methods
   * with more than two arguments. 
   */
  static Local local_for_Strings3 = Jimple.v().newLocal(
      "local_for_Strings3", RefType.v("java.lang.String"));

  /**
   * 
   */
  static Local local_for_String_Arrays = Jimple.v().newLocal(
      "local_for_String_Arrays", ArrayType.v(RefType.v("java.lang.String"), 1));

  /**
   * 
   */
  static Local local_for_Objects = Jimple.v().newLocal(
      "local_for_Objects", RefType.v("java.lang.Object"));
	
  /**
   * 
   */
  static Logger logger = L1Logger.getLogger();

  /**
   * Stores the position of the last unit which was analyzed in the unit chain or the last
   * inserted unit. Is needed for further units, which have to be inserted after the last position.
   */
  static Unit lastPos;

  /**
   * Initialization of JimpleInjector. Set all needed variables
   * and compute the start position for inserting new units.
   * @param body The body of the analyzed method.
   */
  public static void setBody(Body body) {
    b = body;
    units = b.getUnits();
    locals = b.getLocals();
    
    extralocals = false;
    
    Iterator<Unit> it = units.iterator();
    
    // Insert after the setting of all arguments and the @this-reference,
    // since Jimple would otherwise complain.
    int startPos = getStartPos(body);
    Iterator<Unit> uIt = units.iterator();
    for (int i = 0; i <= startPos; i++) {
      lastPos = uIt.next();
    }

    logger.info("Start position is " + lastPos.toString());

  }

  /**
   * Add "hs = new HandleStmt()" expression to Jimplecode.
   */
  public static void invokeHS() {
    logger.log(Level.INFO, "Invoke HandleStmt in method {0}", b.getMethod().getName());

    locals.add(hs);
    Unit in = Jimple.v().newAssignStmt(hs, Jimple.v().newNewExpr(RefType.v(HANDLE_CLASS)));
    ArrayList<Type> paramTypes = new ArrayList<Type>();
    Expr specialIn = Jimple.v().newSpecialInvokeExpr(
        hs, Scene.v().makeConstructorRef(Scene.v().getSootClass(HANDLE_CLASS), paramTypes));

    Unit inv = Jimple.v().newInvokeStmt(specialIn);

    unitStore_Before.insertElement(unitStore_Before.new Element(inv, lastPos));
    unitStore_Before.insertElement(unitStore_Before.new Element(in, inv));
    lastPos = inv;
  }

  /**
   * Injects the constructor call of HandleStmt into the analyzed method.
   */
  public static void initHS() {
    logger.log(Level.INFO, "Initialize HandleStmt in method {0}", b.getMethod().getName());
    
    ArrayList<Type> paramTypes = new ArrayList<Type>();
    Expr invokeInit = Jimple.v().newStaticInvokeExpr(
        Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
        "init", paramTypes, VoidType.v(), true));
    Unit init = Jimple.v().newInvokeStmt(invokeInit);
    unitStore_After.insertElement(unitStore_After.new Element(init, lastPos));
    lastPos = init;
  }

  /**
   * Injects the HandleStmt.close() method. This method should be injected at the
   * end of every analyzed method.
   */
  public static void closeHS() {
    logger.log(Level.INFO, "Close HandleStmt in method {0} {1}", 
        new Object[] {b.getMethod().getName(), System.getProperty("line.separator")});

    ArrayList<Type> paramTypes = new ArrayList<Type>();
    Expr invokeClose = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
        "close", paramTypes, VoidType.v(), false));
    units.insertBefore(Jimple.v().newInvokeStmt(invokeClose), units.getLast());
  }

	/*
	 * Adding Elements to map
	 */
	
	
	/**
	 * @param l
	 */
  public static void addLocal(Local l) {
    logger.log(Level.INFO, "Add Local {0} in method {1}",new Object[] {
        getSignatureForLocal(l), b.getMethod().getName()});
		
    ArrayList<Type> paramTypes = new ArrayList<Type>();
    paramTypes.add(RefType.v("java.lang.String"));

    String signature = getSignatureForLocal(l);
    Stmt sig = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
    Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
        "addLocal", paramTypes, VoidType.v(),  false), local_for_Strings);
    Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

    unitStore_After.insertElement(unitStore_After.new Element(sig, lastPos));
    unitStore_After.insertElement(unitStore_After.new Element(ass, sig));
    lastPos = ass;
  }
  
  /**
   * Add the instance of the actual class-object to the object map. This is only done in "init".
   */
  public static void addInstanceObjectToObjectMap() {
		
    // Check if the first unit is a reference to the actual object
    if (!(units.getFirst() instanceof IdentityStmt) 
        || !(units.getFirst().getUseBoxes().get(0).getValue() instanceof ThisRef)) {
      new InternalAnalyzerException("Expected @this reference");
    }

    String thisObj = units.getFirst().getUseBoxes().get(0).getValue().toString();

    logger.log(Level.INFO, "Add object {0} to ObjectMap in method {1}",
        new Object[] {thisObj, b.getMethod().getName()} );

    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(RefType.v("java.lang.Object"));
		
    Expr addObj	= Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(
            Scene.v().getSootClass(HANDLE_CLASS), "addObjectToObjectMap", 
            parameterTypes, VoidType.v(), false), 
            units.getFirst().getDefBoxes().get(0).getValue()); 
    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
    unitStore_After.insertElement(unitStore_After.new Element(assignExpr, lastPos));
    lastPos = assignExpr;
		
  }
	
  /**
   * @param sc
   */
  public static void addClassObjectToObjectMap(SootClass sc) {
	
    logger.log(Level.INFO, "Add object {0} to ObjectMap in method {1}",
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
		
    unitStore_After.insertElement(unitStore_After.new Element(assignExpr, lastPos));
    lastPos = assignExpr;
  }
	
  /**
   * @param field
   */
  public static void addInstanceFieldToObjectMap(SootField field) {
    logger.log(Level.INFO, "Adding field {0} to ObjectMap in method {1}", 
        new Object[] { field.getSignature() ,b.getMethod().getName()});
		
    if (!(units.getFirst() instanceof IdentityStmt) 
        || !(units.getFirst().getUseBoxes().get(0).getValue() instanceof ThisRef)) {
      new InternalAnalyzerException("Expected @this reference");
    }
		
    String fieldSignature = getSignatureForField(field);
		
		
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(RefType.v("java.lang.Object"));
    parameterTypes.add(RefType.v("java.lang.String"));
		
    Local tmpLocal = (Local) units.getFirst().getDefBoxes().get(0).getValue();
		
    Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings,
        StringConstant.v(fieldSignature));
		
		
    Expr addObj	= Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(
            Scene.v().getSootClass(HANDLE_CLASS), "addFieldToObjectMap", 
            parameterTypes, RefType.v("analyzer.level2.SecurityLevel"), false), 
            tmpLocal, local_for_Strings);
    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
	
    unitStore_After.insertElement(unitStore_After.new Element(assignSignature, lastPos));
    unitStore_After.insertElement(unitStore_After.new Element(assignExpr, assignSignature));
    lastPos = assignExpr;
  }
	
  /**
   * @param field
   */
  public static void addStaticFieldToObjectMap(SootField field) {
    logger.info( "Adding static Field " + field.toString() + " to Object Map");
		
    String signature = getSignatureForField(field);
		
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(RefType.v("java.lang.Object"));
    parameterTypes.add(RefType.v("java.lang.String"));
		
    SootClass sc = field.getDeclaringClass();
		
    Unit assignDeclaringClass  = Jimple.v().newAssignStmt(
        local_for_Objects, ClassConstant.v(sc.getName().replace(".", "/")));
		
    Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));

    Expr addObj = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
        "addFieldToObjectMap", parameterTypes, RefType.v("analyzer.level2.SecurityLevel"), false),
        local_for_Objects, local_for_Strings);
    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
    unitStore_After.insertElement(unitStore_After.new Element(assignDeclaringClass, lastPos));
    unitStore_After.insertElement(
        unitStore_After.new Element(assignSignature, assignDeclaringClass));
    unitStore_After.insertElement(unitStore_After.new Element(assignExpr, assignSignature));
    lastPos = assignExpr;
		
  }
	
  /**
   * @param a
   * @param pos
   */
  public static void addArrayToObjectMap(Local a, Unit pos) {
    logger.log(Level.INFO, "Add array {0} to ObjectMap in method {1}",
        new Object[] {a, b.getMethod().getName()} );
		
    logger.log(Level.INFO, "Object type of array: " + a.getType() + " and type " + a.getClass());
    logger.log(Level.FINEST, "at position {0}", pos.toString());
		
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(ArrayType.v(
        RefType.v("java.lang.Object"), 1));
				
		
	
    Expr addObj	= Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(
           Scene.v().getSootClass(HANDLE_CLASS), "addArrayToObjectMap", 
           parameterTypes, VoidType.v(), false), a); 
    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
    unitStore_After.insertElement(unitStore_After.new Element(assignExpr, pos));
    lastPos = assignExpr;		
  } 
	
  /*
   * Change level of elements
   */
	
  /**
   * @param l
   * @param pos
   */
  public static void makeLocalHigh(Local l, Unit pos) {
    logger.log(Level.INFO, "Make Local {0} high in method {1}",
        new Object[] {getSignatureForLocal(l), b.getMethod().getName()});
		
    ArrayList<Type> paramTypes = new ArrayList<Type>();
    paramTypes.add(RefType.v("java.lang.String"));
		
    String signature = getSignatureForLocal(l);
    Stmt sig = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
    Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
        "makeLocalHigh", paramTypes, VoidType.v(),  false), local_for_Strings);
    Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

    unitStore_After.insertElement(unitStore_After.new Element(sig, pos));
    unitStore_After.insertElement(unitStore_After.new Element(ass, sig));
    lastPos = ass;
  }
	
  /**
   * @param l
   * @param pos
   */
  public static void makeLocalLow(Local l, Unit pos) {
    logger.log(Level.INFO, "Make Local {0} low in method {1}",
        new Object[] {getSignatureForLocal(l), b.getMethod().getName()});
		
    ArrayList<Type> paramTypes = new ArrayList<Type>();
    paramTypes.add(RefType.v("java.lang.String"));
		
    String signature = getSignatureForLocal(l);
    Stmt sig = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
    Expr invokeAddLocal = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
        "makeLocalLow", paramTypes, VoidType.v(),  false), local_for_Strings);
    Unit ass = Jimple.v().newInvokeStmt(invokeAddLocal);
		

    unitStore_After.insertElement(unitStore_After.new Element(sig, pos));
    unitStore_After.insertElement(unitStore_After.new Element(ass, sig));
    lastPos = ass;
  }

  /**
   * @param l
   * @param pos
   */
  public static void addLevelInAssignStmt(Local l, Unit pos) {
    logger.info("Adding level in assign statement");
		
    ArrayList<Type> paramTypes = new ArrayList<Type>();
    paramTypes.add(RefType.v("java.lang.String"));
		
    String signature = getSignatureForLocal(l);
		
    Stmt assignSignature = Jimple.v().newAssignStmt(
        local_for_Strings, StringConstant.v(signature));
		
    Expr invokeAddLevel = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
        "addLevelOfLocal", paramTypes, RefType.v("analyzer.level2.SecurityLevel"),
        false), local_for_Strings);
    Unit invoke = Jimple.v().newInvokeStmt(invokeAddLevel);
		

    unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(invoke, pos));
    lastPos = pos;
  }
	
	
  /**********************************************************************************************
   * AssignStmt Functions.
   **********************************************************************************************/
	
	
  /**
   * @param f
   * @param pos
   */
  public static void addLevelInAssignStmt(InstanceFieldRef f, Unit pos) {
    logger.log(Level.INFO, "Adding level of field {0} in assignStmt in method {1}", 
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
		
    Unit assignSignature = Jimple.v().newAssignStmt(
        local_for_Strings, StringConstant.v(fieldSignature));
		
    Expr addObj	= Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(
            Scene.v().getSootClass(HANDLE_CLASS), "addLevelOfField", 
            parameterTypes, RefType.v("analyzer.level2.SecurityLevel"), false), 
            local_for_Objects, local_for_Strings);
    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
	
    unitStore_Before.insertElement(unitStore_Before.new Element(assignBase, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(assignExpr, pos));
    lastPos = pos;
  }
	
  /**
   * @param f
   * @param pos
   */
  public static void addLevelInAssignStmt(StaticFieldRef f, Unit pos) {
    logger.info( "Adding Level of static Field " + f.toString() + " in assign stmt");
		
    SootField field = f.getField();

    String signature = getSignatureForField(field);
		
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(RefType.v("java.lang.Object"));
    parameterTypes.add(RefType.v("java.lang.String"));
		
    SootClass sc = field.getDeclaringClass();
		
    Unit assignDeclaringClass  = Jimple.v().newAssignStmt(
        local_for_Objects, ClassConstant.v(sc.getName().replace(".", "/")));
		
    Unit assignSignature = Jimple.v().newAssignStmt(
        local_for_Strings, StringConstant.v(signature));
		
    Expr addObj = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
        "addLevelOfField", parameterTypes, RefType.v("analyzer.level2.SecurityLevel"), false),
        local_for_Objects, local_for_Strings);
    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
    unitStore_Before.insertElement(unitStore_Before.new Element(assignDeclaringClass, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(assignExpr, pos));
    lastPos = pos;
  }
	
  /**
   * Add the level of a read array field to the security-level-list. 
   * @param a -ArrayRef- The referenced array field
   * @param pos -Unit- The position where this reference occurs
   */
  public static void addLevelInAssignStmt(ArrayRef a, Unit pos) {
    logger.info( "Add Level of Array " + a.toString() + " in assign stmt");
    
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(RefType.v("java.lang.Object"));
    parameterTypes.add(RefType.v("java.lang.String"));
    
    String signature = getSignatureForArrayField(a);
    Unit assignSignature = Jimple.v().newAssignStmt(
        local_for_Strings, StringConstant.v(signature));
  				
    unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
    lastPos = assignSignature;

    Expr addObj = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
        "addLevelOfArrayField", parameterTypes, RefType.v("analyzer.level2.SecurityLevel"), false),
        a.getBase(), local_for_Strings);
    
    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);

    unitStore_Before.insertElement(unitStore_Before.new Element(assignExpr, pos));
    lastPos = pos;
  }
	
  /**
   * @param l
   * @param pos
   */
  public static void setLevelOfAssignStmt(Local l, Unit pos) {
    logger.info("Setting level in assign statement");
		
    ArrayList<Type> paramTypes = new ArrayList<Type>();
    paramTypes.add(RefType.v("java.lang.String"));
		
    String signature = getSignatureForLocal(l);
		
    Stmt assignSignature = Jimple.v().newAssignStmt(
        local_for_Strings, StringConstant.v(signature));
		
    Expr invokeSetLevel = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
        "setLevelOfLocal", paramTypes, RefType.v("analyzer.level2.SecurityLevel"),
        false), local_for_Strings);
    Unit invoke = Jimple.v().newInvokeStmt(invokeSetLevel);
		
    unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(invoke, pos));
    lastPos = pos;
  }
	
  /**
   * @param f
   * @param pos
   */
  public static void setLevelOfAssignStmt(InstanceFieldRef f, Unit pos) {
    logger.log(Level.INFO, "Set level to field {0} in assignStmt in method {1}", 
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
    Unit assignSignature = Jimple.v().newAssignStmt(
        local_for_Strings, StringConstant.v(fieldSignature));
		
    Expr addObj	= Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(
            Scene.v().getSootClass(HANDLE_CLASS), "setLevelOfField", 
            parameterTypes, RefType.v("analyzer.level2.SecurityLevel"), false), 
            tmpLocal, local_for_Strings);
    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
	
    unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
    unitStore_Before.insertElement(unitStore_After.new Element(assignExpr, pos));
    lastPos = pos;
  }
	
  /**
   * @param f
   * @param pos
   */
  public static void setLevelOfAssignStmt(StaticFieldRef f, Unit pos) {
    logger.info( "Set Level of static Field " + f.toString() + " in assign stmt");
		
    SootField field = f.getField();

    String signature = getSignatureForField(field);
    System.out.println("Signature of static field in jimple injector " + signature);
		
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(RefType.v("java.lang.Object"));
    parameterTypes.add(RefType.v("java.lang.String"));
		
    SootClass sc = field.getDeclaringClass();
		
    Unit assignDeclaringClass  = Jimple.v().newAssignStmt(
        local_for_Objects, ClassConstant.v(sc.getName().replace(".", "/")));
		
    Unit assignSignature = Jimple.v().newAssignStmt(
        local_for_Strings, StringConstant.v(signature));
		
    Expr addObj = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
        "setLevelOfField", parameterTypes, RefType.v("analyzer.level2.SecurityLevel"), false),
        local_for_Objects, local_for_Strings);
    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
		
    unitStore_Before.insertElement(unitStore_Before.new Element(assignDeclaringClass, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(assignExpr, pos));
    lastPos = pos;
  }
	
  /**
   * Inject a method of HandleStmt to set the level of an array-field. This method
   * distinguishes two cases, one case where the index of the referenced array-field
   * is a constant number and the other case, where the index is stored in a local variable.
   * In the second case, the signature of the local variable also must be passed as an
   * argument to {@link analyzer.level2.HandleStmt
   *     #setLevelOfArrayField(Object o, int field, String localForObject, String localForIndex)} .
   * @param a -ArrayRef. The reference to the array-field
   * @param pos -Unit- The assignStmt in the analyzed methods body, where this
   *     reference appears. 
   */
  public static void setLevelOfAssignStmt(ArrayRef a, Unit pos) {
    logger.info( "Set level of array " + a.toString() + " in assign stmt");
    
    // Add extra locals for arguments
    if (!extralocals) {
        locals.add(local_for_Strings2);
        locals.add(local_for_Strings3);
        extralocals = true;
    }
		
    // Define the types of the arguments for HandleStmt.setLevelOfArrayField()
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(RefType.v("java.lang.Object")); // for Object o
    parameterTypes.add(RefType.v("java.lang.String")); // for String field
    parameterTypes.add(RefType.v("java.lang.String")); // for String localForObject
    
    Value objectO = a.getBase();
    String signatureForField = getSignatureForArrayField(a);
    String signatureForObjectLocal = getSignatureForLocal((Local) a.getBase());

    // List for the arguments for HandleStmt.setLevelOfArrayField()
    List<Value> args = new ArrayList<Value>();
    args.add(objectO);
    
    // Store all string-arguments in locals for strings and assign the locals to the
    // argument list.  
    Unit assignFieldSignature = Jimple.v().newAssignStmt(
            local_for_Strings, StringConstant.v(signatureForField));
    Unit assignObjectSignature = Jimple.v().newAssignStmt(
            local_for_Strings2, StringConstant.v(signatureForObjectLocal));
    
    args.add(local_for_Strings);
    args.add(local_for_Strings2);
		
    
    if (!(a.getIndex() instanceof Local)) { 
      // Case where the index is a constant.
      // The needed arguments are "Object o, String field, String localForObject".
			
      logger.fine("Index value for the array field is a constant value");
			
    } else if (a.getIndex() instanceof Local) { 
      // The index is a local and must be given as a parameter.
      // The needed arguments are 
      // "Object o, String field, String localForObject, String localForIndex".
    	
      logger.fine("Index value for the array field is stored in a local");
    	
      // add a further parameter type for String localForIndex and add it to the arguments-list.
      parameterTypes.add(RefType.v("java.lang.String"));
      String localSignature = getSignatureForLocal((Local) a.getIndex());
      Unit assignIndexSignature = Jimple.v().newAssignStmt(
              local_for_Strings3, StringConstant.v(localSignature));     
      args.add(local_for_Strings3);
			
      unitStore_Before.insertElement(unitStore_Before.new Element(assignIndexSignature, pos));
      
    } 
    
    Expr addObj = Jimple.v().newVirtualInvokeExpr(
            hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
            "setLevelOfArrayField", parameterTypes, RefType.v("analyzer.level2.SecurityLevel"),
            false), args);

    Unit assignExpr = Jimple.v().newInvokeStmt(addObj);
    unitStore_Before.insertElement(unitStore_Before.new Element(assignFieldSignature, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(assignObjectSignature, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(assignExpr, pos));
    lastPos = pos;
  }
	
  /**
   * @param l
   * @param pos
   */
  public static void assignReturnLevelToLocal(Local l, Unit pos) {
    logger.log(Level.INFO, "Assign return level of invoked method to local {0}", 
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
    lastPos = assignExpr;
  }
	
  /**
   * @param posInArgList
   * @param local
   * @param actualPos
   */
  public static void assignArgumentToLocal(int posInArgList, Local local, Unit actualPos) {
    logger.log(Level.INFO, "Assign argument level to local " + local.toString());
		
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(IntType.v());
    parameterTypes.add(RefType.v("java.lang.String"));
    Expr assignArg = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(
        Scene.v().getSootClass(HANDLE_CLASS), "assignArgumentToLocal",
        parameterTypes, RefType.v("analyzer.level2.SecurityLevel"), false),
        IntConstant.v(posInArgList), StringConstant.v(getSignatureForLocal(local))
    );
		
    Unit assignExpr = Jimple.v().newInvokeStmt(assignArg);
		
    unitStore_After.insertElement(unitStore_After.new Element(assignExpr, lastPos));
    lastPos = assignExpr;
  }
	
  /**********************************************************************************************
   *  Inter-scope functions.
   *********************************************************************************************/
	
  /**
   * @param retStmt
   */
  public static void returnConstant(Unit retStmt) {
    logger.log(Level.INFO, "Return a constant value");
		
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
		
    Expr returnConst = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
        Scene.v().getSootClass(HANDLE_CLASS), "returnConstant", 
        parameterTypes, VoidType.v(), false));
	
    unitStore_Before.insertElement(unitStore_Before.new Element(
        Jimple.v().newInvokeStmt(returnConst), retStmt));
  }

  /**
   * @param l
   * @param pos
   */
  public static void returnLocal(Local l, Unit pos) {
    logger.log(Level.INFO, "Return Local {0}", getSignatureForLocal(l));
		
    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(RefType.v("java.lang.String"));
		
    Stmt sig = Jimple.v().newAssignStmt(
        local_for_Strings, StringConstant.v(getSignatureForLocal(l)));
		
    Expr returnLocal = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
        Scene.v().getSootClass(HANDLE_CLASS), "returnLocal", parameterTypes,
        VoidType.v(), false), local_for_Strings);
		
    Stmt returnL = Jimple.v().newInvokeStmt(returnLocal);
		
    unitStore_Before.insertElement(unitStore_Before.new Element(sig, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(returnL, pos));
    lastPos = pos;
  }

  /**
   * @param pos
   * @param lArguments
   */
  public static void storeArgumentLevels(Unit pos, Local... lArguments) {

    logger.log(Level.INFO, "Store Arguments for next method in method {0}",
        b.getMethod().getName());

    int length = lArguments.length;

    ArrayList<Type> parameterTypes = new ArrayList<Type>();
    parameterTypes.add(ArrayType.v(RefType.v("java.lang.String"), 1));

    Expr paramArray = Jimple.v().newNewArrayExpr(RefType.v(
        "java.lang.String"), IntConstant.v(length));

    Unit assignNewStringArray = Jimple.v().newAssignStmt(local_for_String_Arrays, paramArray);

    Unit[] tmpUnitArray = new Unit[length];

    for (int i = 0; i < length; i++) {

      // It's possible to get a null vector as an argument. This happens,
      // if a constant is set as argument.
      if (lArguments[i] != null) {
        String signature = getSignatureForLocal(lArguments[i]);
        tmpUnitArray[i] = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(
            local_for_String_Arrays, IntConstant.v(i)), StringConstant.v(signature));
      } else {
        tmpUnitArray[i] = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(
            local_for_String_Arrays, IntConstant.v(i)), StringConstant.v(" ")); // TODO unnoetig
      }
    }

    Expr storeArgs = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
        Scene.v().getSootClass(HANDLE_CLASS), "storeArgumentLevels", parameterTypes, 
        VoidType.v(), false), local_for_String_Arrays);
    Stmt invokeStoreArgs = Jimple.v().newInvokeStmt(storeArgs);



    unitStore_Before.insertElement(unitStore_Before.new Element(assignNewStringArray, pos));    
    for (Unit el : tmpUnitArray) {
      unitStore_Before.insertElement(unitStore_Before.new Element(el, pos));
    }
    unitStore_Before.insertElement(unitStore_Before.new Element(invokeStoreArgs, pos));
    lastPos = pos;

  }

  /**
   * @param pos
   * @param l
   */
  public static void checkThatNotHigh(Unit pos, Local l) {
    logger.info("Check that " + l + " is not high");
		
    if (l == null)  {
      new InternalAnalyzerException("Argument is null");
    }
		
    ArrayList<Type> paramTypes = new ArrayList<Type>();
    paramTypes.add(RefType.v("java.lang.String"));
		
    String signature = getSignatureForLocal(l);
		
    Stmt assignSignature = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));
		
    Expr invokeSetLevel = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS), 
        "checkThatNotHigh", paramTypes, VoidType.v(),  false), local_for_Strings);
    Unit invoke = Jimple.v().newInvokeStmt(invokeSetLevel);
		

    unitStore_Before.insertElement(unitStore_Before.new Element(assignSignature, pos));
    unitStore_Before.insertElement(unitStore_Before.new Element(invoke, pos));
    lastPos = pos;
  }
	
  /**
   * @param pos
   * @param locals
   */
  public static void checkCondition(Unit pos, Local... locals) {

    logger.log(Level.INFO, "Check condition in method {0}", b.getMethod());
		
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
    for (Unit u : tmpUnitList) {
      unitStore_After.insertElement(unitStore_After.new Element(u, pos));
    }
    unitStore_After.insertElement(unitStore_After.new Element(invokeCC, pos));
    lastPos = invokeCC;
		
  }
	
  /**
   * @param pos
   */
  public static void exitInnerScope(Unit pos) {
    logger.log(Level.INFO, "Exit inner scope in method {0}", b.getMethod().getName());
		
    ArrayList<Type> paramTypes = new ArrayList<Type>();
		
    Expr specialIn = Jimple.v().newVirtualInvokeExpr(
        hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
            "exitInnerScope", paramTypes, VoidType.v(), false));
		
    Unit inv = Jimple.v().newInvokeStmt(specialIn);
		
    Unit endOfScope = units.getPredOf(pos);
		
    unitStore_After.insertElement(unitStore_After.new Element(inv, endOfScope));
    lastPos = inv;
  }
	
/*
 * Internal methods
 */
	
/**
 * 
 */
  protected static void addUnitsToChain() {	

    // First add all elements from unitStore_Before
    Iterator<Element> uIt = unitStore_Before.getElements().iterator();
    
    // If needed for debugging, you can remove the comment
    // unitStore_Before.print();
    
    while (uIt.hasNext()) {
      Element item = (Element) uIt.next();
      if (item.getPosition() == null) {
        units.addFirst(item.getUnit());
      } else {
        logger.finest("Starting to insert: " + item.getUnit().toString()
            + " after position " + item.getPosition().toString());
        units.insertBefore(item.getUnit(), item.getPosition()); 
        logger.finest("Insertion completed");
      }
    }

    // Now add all elements from unitStore_After
    uIt = unitStore_After.getElements().iterator();
    
    // If needed for debugging, you can remove the comment
    // unitStore_After.print();
    
    while (uIt.hasNext()) {
      Element item = (Element) uIt.next();
      
      if (item.getPosition() == null) {
        units.addFirst(item.getUnit());
      } else {
        logger.finest("Starting to insert: " + item.getUnit().toString()
            + " after position " + item.getPosition().toString());
        units.insertAfter(item.getUnit(), item.getPosition());
        logger.finest("Insertion completed");
      }
    }
	
    unitStore_After.flush();
    unitStore_Before.flush();
    
    // print the units in the right order for debugging.
    // printUnits();
    
    b.validate();
  }

  /**
   * Add all locals which are needed from JimpleInjector to store values
   * of parameters for invoked methods.
   */
  protected static void addNeededLocals() {
    locals.add(local_for_Strings);
    locals.add(local_for_String_Arrays);
    locals.add(local_for_Objects);

    b.validate();
  }

  /**
   * @param l
   * @return
   */
  private static String getSignatureForLocal(Local l) {
    return l.getType() + "_" + l.getName();
  }

  /**
   * @param f
   * @return
   */
  private static String getSignatureForField(SootField f) {
    return f.getSignature();
  }

  /**
   * Create the signature of an array-field based on the index.
   * It simply returns the int-value as string.
   * @param a -ArrayRef-
   * @return -String- The signature for the array-field.
   */
  private static String getSignatureForArrayField(ArrayRef a) {
    logger.fine("Type of index: " + a.getIndex().getType()); 
    String result = "";
    if (a.getIndex().getType().toString() == "int") {
      result = a.getIndex().toString();
    } else {
      logger.log(Level.SEVERE, "Unexpected type of index",
          new InternalAnalyzerException("Unexpected type of index")); 
      System.exit(0);
    }
    logger.info("Signature of array field in jimple injector is: " + result);
    return result; 
  }

  /**
   * Method which calculates the start position for inserting further units.
   * It depends on the number of arguments and wheter it is a static method,
   * or not, because there are statements, which shouldn't be preceeded by
   * other statements.
   * @return the computed start position as int.
   */
  private static int getStartPos(Body b) {
    int startPos = 0;
    
    SootMethod m = b.getMethod();
    
    // If the method is not static the @this reference must be skipped. 
    if (!m.isStatic()) {
      startPos++;
    }
    
    if (m.isConstructor()) {
      startPos++;
    }
    
    // Skip the @parameter-references
    int numOfParams = m.getParameterCount();
    startPos += numOfParams;
    
    logger.info("Calculated start position: " + startPos);
    
    return startPos;
  }
  
  /**
   * This method is only for debugging purposes.
   */
  @SuppressWarnings("unused")
  private static void printUnits() {
    Iterator<Unit> uIt = units.iterator();
    int i = 0;
    System.out.println("Actual method: " + b.getMethod().toString());
    while (uIt.hasNext()) {
      System.out.println(i + " " + uIt.next().toString());
      i++;
    }
  }
}

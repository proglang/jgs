package analyzer.level1;

import analyzer.level1.storage.UnitToInsert;
import analyzer.level2.CurrentSecurityDomain;
import analyzer.level2.HandleStmt;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews;
import de.unifreiburg.cs.proglang.jgs.instrumentation.*;
import scala.Option;
import soot.*;
import soot.Type;
import soot.jimple.*;
import soot.jimple.internal.JAssignStmt;
import soot.util.Chain;
import utils.dominator.DominatorFinder;
import utils.exceptions.InternalAnalyzerException;
import utils.jimple.BoolConstant;
import utils.jimple.JimpleFactory;
import utils.logging.L1Logger;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JimpleInjector is handles the inserts of additional instructions in a methods
 * body, such that the Dynamic Checking is possible.
 *
 * @author Regina Koenig (2015), Karsten Fix (2017), fennell (2017)
 */
public class JimpleInjector {

    /** String for the HandleStmt class. */
    private static final String HANDLE_CLASS = HandleStmt.class.getName();

    /** Local which holds the object of HandleStmt. */
    private static Local hs = Jimple.v().newLocal("hs", RefType.v(HANDLE_CLASS));

    private static JimpleFactory fac = new JimpleFactory(HandleStmt.class, hs);

    // <editor-fold desc="Fields for Body Analysis">

    /** The body of the actually analyzed method. */
    private static Body b = Jimple.v().newBody();

    /** Chain with all units in the actual method-body.*/
    private static Chain<Unit> units = b.getUnits();

    /** Chain with all locals in the actual method-body. */
    private static Chain<Local> locals = b.getLocals();

    /**
     * Stores the position of
     * <ul>
     *     <li>the last unit which was analyzed in the unit chain</li>
     *     <li><b>or</b> the last inserted unit</li>
     * </ul>
     * This is needed for further units, which have to be inserted after this
     * position.
     */
    // TODO: handling the "last positions" like this is absolutely horrible. Cf. also the code in "addUnitsToChain". Instead of this mess, there should be two maps "stmt -> listof(stmt)", mapping to statements-to-be-inserted before, and after a given original statement, respectively.
    private static Unit lastPos;

    // </editor-fold>

    // <editor-fold desc="Fields for Injections"
/**
     * Chain containing all new units which have to be set
     * after a given position.
     */
    private static Queue<UnitToInsert> unitStore_After = new LinkedList<>();

    /**
     * Chain containing all new units which have to be set
     * before a given position.
     */
    private static Queue<UnitToInsert> unitStore_Before = new LinkedList<>();

    // </editor-fold>

    // <editor-fold desc="Locals, that shall be removed">

    /** Boolean to check whether the extra locals had already been added. */
    // Todo: Remove when ready
    private static boolean extralocals = false;

    /** Local wto store String values. */
    private static Local local_for_Strings = Jimple.v().newLocal(
            "local_for_Strings", RefType.v("java.lang.String"));

    /** This local is needed for methods with more than two arguments. */
    private static Local local_for_Strings2 = Jimple.v().newLocal("local_for_Strings2", RefType.v("java.lang.String"));

    /** This locals is needed for methods with more than two arguments. */
    private static Local local_for_Strings3 = Jimple.v().newLocal("local_for_Strings3", RefType.v("java.lang.String"));

    /** Local where String arrays can be stored. Needed to store arguments for injected methods. */
    private static Local local_for_String_Arrays = Jimple.v().newLocal("local_for_String_Arrays", ArrayType.v(RefType.v("java.lang.String"), 1));

    /** Local where Objects can be stored as arguments for injected methods. */
    private static Local local_for_Objects = Jimple.v().newLocal("local_for_Objects", RefType.v("java.lang.Object"));

    // </editor-fold>

    /** Logger */
    private static Logger logger = L1Logger.getLogger();


    private static Casts casts;

    /**
     * Stores the results of the static analysis. Use Lvel instead of Level because of conflicts with the LEVEL of the Logger.
     */
    private static VarTyping varTyping;
    private static CxTyping cxTyping;
    private static Instantiation instantiation;
    /**
     * The list of locals of the body *before* instrumentation.
     */
    private static List<Local> originalLocals;

    /**
     * See method with same name in HandleStatement.
     *
     * @param pos   Statement / Unit where to insert setReturnLevelAfterInvokeStmt
     */
    public static void setReturnLevelAfterInvokeStmt(Unit pos) {
        ArrayList<Type> paramTypes = new ArrayList<>();
        paramTypes.add(RefType.v("java.lang.String"));

        Expr setReturnLevel = Jimple.v().newVirtualInvokeExpr(
                hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
                        "setReturnLevelAfterInvokeStmt", paramTypes,
                        VoidType.v(),
                        false), local_for_Strings);
        Unit invoke = Jimple.v().newInvokeStmt(setReturnLevel);


        // only add setReturnLevelAfterInvokeStmt if the left side is dynamic
        if ( varTyping.getAfter(instantiation, (Stmt) pos, (Local) ((JAssignStmt) pos).leftBox.getValue() ).isDynamic() ) {
            unitStore_After.add(new UnitToInsert(invoke, pos));
        }
    }

    /**
     * Initialization of JimpleInjector. Set all needed variables
     * and compute the start position for inserting new units.
     *
     * @param body The body of the analyzed method.
     */
    public static void setBody(Body body) {
        b = body;
        units = b.getUnits();
        locals = b.getLocals();
        originalLocals = new ArrayList<>(locals);

        // TODO: Remove this flag, when ready to remove
        extralocals = false;

        lastPos = getUnitOf(units, getStartPos(body));
        fac.initialise();
    }

    // <editor-fold desc="HandleStmt Related Methods">

    /**
     * Creates the Local hs in Jimple Code,
     * assigns "hs = new HandleStmt()" and invokes the Constructor within the
     * created Jimple Code.
     */
    static void invokeHS() {
        logger.info("Invoke HandleStmt in method " + b.getMethod().getName());

        locals.add(hs);
        Unit in = Jimple.v().newAssignStmt(hs, Jimple.v().newNewExpr(
                RefType.v(HANDLE_CLASS)));

        Unit inv = fac.createStmt(HandleStmt.class.getName());

        unitStore_Before.add(new UnitToInsert(inv, lastPos));
        unitStore_Before.add(new UnitToInsert(in, inv));
        lastPos = inv;
    }

    /**
     * Inserts a call of {@link HandleStmt#initHandleStmtUtils(boolean, int)}
     * into the generated Jimple Code after the Last Position.
     * @param controllerIsActive {@link HandleStmt#initHandleStmtUtils(boolean, int)}
     * @param expectedException {@link HandleStmt#initHandleStmtUtils(boolean, int)}
     */
    static void initHandleStmtUtils(boolean controllerIsActive, int expectedException) {
        logger.info("Set Handle Stmt Utils and active/passive Mode of superfluous instrumentation checker");

        Unit inv = fac.createStmt("initHandleStmtUtils",
                                  BoolConstant.v(controllerIsActive),
                                  IntConstant.v(expectedException));

        unitStore_After.add(new UnitToInsert(inv, lastPos));
        lastPos = inv;
    }

    /**
     * Inserts {@link HandleStmt#init()}.
     */
    static void initHS() {
        logger.info("Initializing HandleStmt in method: " + b.getMethod().getName());

        Unit init = fac.createStmt("init");
        unitStore_After.add(new UnitToInsert(init, lastPos));
        lastPos = init;
    }

    /**
     * Injects the {@link HandleStmt#close()}, because it should be injected at the
     * end of every analyzed method.
     */
    static void closeHS() {
        logger.info("Closing HandleStmt in Method "+b.getMethod().getName());
        units.insertBefore(fac.createStmt("close"), units.getLast());
    }


    // </editor-fold>

    // <editor-fold desc="Local Related Methods">


    /**
     * Inserts a call of {@link HandleStmt#addLocal(String)}.
     *
     * @param local The Local, that shall be added. Its signature will be calculated
     * @see HandleStmt#addLocal(String)
     * @see JimpleInjector#getSignatureForLocal(Local)
     */
    public static void addLocal(Local local) {
        logger.info("Add Local " + getSignatureForLocal(local) + " in Method " + b.getMethod().getName());

        Unit add = fac.createStmt("addLocal", StringConstant.v(getSignatureForLocal(local)));
        unitStore_After.add(new UnitToInsert(add, lastPos));
        lastPos = add;
    }

    /**
     * Inserts {@link HandleStmt#setLocalFromString(String, String)} into the Jimple Code
     *
     * @param local local The Local with level shall be adjusted.
     * @param level the level to assign to the local
     * @param pos   position where to insert the created Stmt.
     */
    public static void makeLocal(Local local, String level, Unit pos) {
        logger.info("Setting " + local + "to new level " + level);

        // local_for_Strings = getSignatureForLocal(local)
        String signature = getSignatureForLocal(local);
        Stmt sig = Jimple.v().newAssignStmt(local_for_Strings, StringConstant.v(signature));

        Unit setLevelOfL = fac.createStmt("setLocalFromString", StringConstant.v(signature), StringConstant.v(level));

        unitStore_After.add(new UnitToInsert(sig, pos));
        unitStore_After.add(new UnitToInsert(setLevelOfL, sig));
        lastPos = setLevelOfL;
    }

    // </editor-fold>

    // <editor-fold desc="Add To Object Map - Methods">

    /**
     * Inserts a call of {@link HandleStmt#addObjectToObjectMap(Object)}
     *
     * Add the instance of the actual class-object to the object map.
     * This is only done in "init".
     */
    static void addInstanceObjectToObjectMap() {
        logger.info("Add object "+units.getFirst().getUseBoxes().get(0).getValue()+" to ObjectMap in method "+ b.getMethod().getName());
        assureThisRef();
        Unit assignExpr = fac.createStmt("addObjectToObjectMap", units.getFirst().getDefBoxes().get(0).getValue());
        unitStore_After.add(new UnitToInsert(assignExpr, lastPos));
        lastPos = assignExpr;
    }

    /**
     * Inserts a call of {@link HandleStmt#addObjectToObjectMap(Object)}, what is
     * needed for static fields
     *
     * @param sc The SootClass that represents the Class that provides the static field
     */
    static void addClassObjectToObjectMap(SootClass sc) {
        logger.info("Add object "+sc.getName()+" to ObjectMap in method " + b.getMethod().getName());
        Unit assignExpr = fac.createStmt("addObjectToObjectMap", ClassConstant.v(sc.getName().replace(".", "/")));
        unitStore_After.add(new UnitToInsert(assignExpr, lastPos));
        lastPos = assignExpr;
    }

    /**
     * Inserts {@link HandleStmt#addFieldToObjectMap(Object, String)}
     *
     * @param field The Field that shall be added to the Object Map.
     */
    static void addInstanceFieldToObjectMap(SootField field) {
        logger.info("Adding field "+field.getSignature()+" to ObjectMap in method " + b.getMethod().getName());

        assureThisRef();

        String fieldSignature = getSignatureForField(field);
        Value tmpLocal =  units.getFirst().getDefBoxes().get(0).getValue();

        // Todo: is that the same? All tests passing...-> Write Test, that fails, because it is wrong...
        // tmpLocal = ClassConstant.v(field.getDeclaringClass().getName().replace(".", "/"));

        Unit assignSignature = Jimple.v().newAssignStmt(local_for_Strings,
                                                        StringConstant.v(fieldSignature));

        Unit assignExpr = fac.createStmt("addFieldToObjectMap", tmpLocal, StringConstant.v(fieldSignature));

        unitStore_After.add(
                new UnitToInsert(assignSignature, lastPos));
        unitStore_After.add(
                new UnitToInsert(assignExpr, assignSignature));
        lastPos = assignExpr;
    }

    /**
     * Inserts {@link HandleStmt#addFieldToObjectMap(Object, String)}
     *
     * @param field The Field that shall be added to the Object Map.
     */
    // Todo: May be the same as addInstanceField, difference could be figured out by field.isStatic() ?!
    static void addStaticFieldToObjectMap(SootField field) {
        logger.info("Adding static Field " + field + " to Object Map in method "+b.getMethod().getName());

        String signature = getSignatureForField(field);
        SootClass sc = field.getDeclaringClass();

        Unit assignDeclaringClass = Jimple.v().newAssignStmt(
                local_for_Objects, ClassConstant.v(sc.getName().replace(".", "/")));

        Unit assignSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(signature));

        Unit assignExpr = fac.createStmt("addFieldToObjectMap",
                                    ClassConstant.v(sc.getName().replace(".", "/")),
                                    StringConstant.v(signature));

        unitStore_After.add(
                new UnitToInsert(assignDeclaringClass, lastPos));
        unitStore_After.add(
                new UnitToInsert(assignSignature, assignDeclaringClass));
        unitStore_After.add(
                new UnitToInsert(assignExpr, assignSignature));
        lastPos = assignExpr;

    }

    /**
     * Inserts {@link HandleStmt#addArrayToObjectMap(Object[])} call into the Jimple Code.
     *
     * @param a   The Local where the array is stored.
     * @param pos Unit where the array occurs, after that position the invoke Stmt will be inserted.
     */
    public static void addArrayToObjectMap(Local a, Unit pos) {
        logger.info("Add array "+a+" with type "+a.getType()+" to ObjectMap in method " + b.getMethod().getName());
        Unit assignExpr = fac.createStmt("addArrayToObjectMap", a);
        unitStore_After.add(new UnitToInsert(assignExpr, pos));
        lastPos = assignExpr;
    }

    // </editor-fold>

    // <editor-fold desc="Add Level In Assign Stmt - Methods -> Interesting for RHS">

    /**
     * Add the level of a local on the right side of an assign statement.
     * Inserts {@link HandleStmt#joinLevelOfLocalAndAssignmentLevel(String)} into the Jimple Code
     *
     * @param local Local
     * @param pos   Unit where the local occurs
     */
    public static void addLevelInAssignStmt(Local local, Unit pos) {
        logger.info("Adding level of "+local+"in assign statement of Method: "+b.getMethod().getName());

        ArrayList<Type> paramTypes = new ArrayList<>();
        paramTypes.add(RefType.v("java.lang.String"));

        String signature = getSignatureForLocal(local);
        Stmt assignSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(signature));

        Unit invoke = fac.createStmt("joinLevelOfLocalAndAssignmentLevel", StringConstant.v(signature));

        // only insert the joinLevelOfLocal.. stmt if local is in fact dynamically checked
        // TODO CX is irrelevant here?
        if (varTyping.getAfter(instantiation, (Stmt) pos, local).isDynamic()) {
            unitStore_Before.add(new UnitToInsert(assignSignature, pos));
            unitStore_Before.add(new UnitToInsert(invoke, pos));
            lastPos = pos;
        }
    }

    /**
     * Add the level of a field of an object. It can be the field of the actually
     * analyzed object or the field
     * Inserts {@link HandleStmt#joinLevelOfFieldAndAssignmentLevel(Object, String)} into the Jimple Code
     *
     * @param f   Reference to the instance field
     * @param pos The statement where this field occurs
     */
    public static void addLevelInAssignStmt(InstanceFieldRef f, Unit pos) {
        logger.info("Adding level of field "+f.getField().getSignature()+" in assignStmt in method "+  b.getMethod().getName());

        String fieldSignature = getSignatureForField(f.getField());

        // units.getFirst is already a reference to @this
        // Local tmpLocal = (Local) units.getFirst().getDefBoxes().get(0).getValue();
        Unit assignBase = Jimple.v().newAssignStmt(local_for_Objects, f.getBase());
        Unit assignSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(fieldSignature));

        Unit assignExpr = fac.createStmt("joinLevelOfFieldAndAssignmentLevel", f.getBase(), StringConstant.v(fieldSignature));

        // TODO CANNOT CAST ..
        //if (varTyping.getAfter(instantiation, (Stmt) pos, (Local) f).isDynamic()) {
            unitStore_Before.add(new UnitToInsert(assignBase, pos));
            unitStore_Before.add(new UnitToInsert(assignSignature, pos));
            unitStore_Before.add(new UnitToInsert(assignExpr, pos));
            lastPos = pos;
        //}
    }

    /**
     * Inserts {@link HandleStmt#joinLevelOfFieldAndAssignmentLevel(Object, String)} into the Jimple Code
     *
     * @param f   the field
     * @param pos the position where to insert the statement
     */
    public static void addLevelInAssignStmt(StaticFieldRef f, Unit pos) {
        logger.info("Adding Level of static Field " + f + " in Method "+b.getMethod());

        SootField field = f.getField();
        String signature = getSignatureForField(field);

        SootClass sc = field.getDeclaringClass();

        Unit assignDeclaringClass = Jimple.v().newAssignStmt(
                local_for_Objects, ClassConstant.v(sc.getName().replace(".", "/")));

        Unit assignSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(signature));

        Unit assignExpr = fac.createStmt("joinLevelOfFieldAndAssignmentLevel",
                                         ClassConstant.v(sc.getName().replace(".", "/")),
                                         StringConstant.v(signature)
                                         );

            // TODO cannot cast StaticFieldref to Local!
        //if (varTyping.getAfter(instantiation, (Stmt) pos, (Local) f).isDynamic()) {
            unitStore_Before.add(
                    new UnitToInsert(assignDeclaringClass, pos));
            unitStore_Before.add(new UnitToInsert(assignSignature, pos));
            unitStore_Before.add(new UnitToInsert(assignExpr, pos));
            lastPos = pos;
        //}
    }

    /**
     * Add the level of a read array field to the security-level-list.
     * Inserts {@link HandleStmt#joinLevelOfArrayFieldAndAssignmentLevel(Object, String)} into Jimple Code
     *
     * @param a   -ArrayRef- The referenced array field
     * @param pos -Unit- The position where this reference occurs
     */
    public static void addLevelInAssignStmt(ArrayRef a, Unit pos) {
        logger.info("Add Level of Array " + a + " in assign stmt: "+pos);

        String signature = getSignatureForArrayField(a);

        Unit assignSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(signature));

        unitStore_Before.add(new UnitToInsert(assignSignature, pos));
        lastPos = assignSignature;

        Unit assignExpr = fac.createStmt("joinLevelOfArrayFieldAndAssignmentLevel", a.getBase(), StringConstant.v(signature));

        // TODO CANNOT CAST ...
        //if (varTyping.getAfter(instantiation, (Stmt) pos, (Local) a).isDynamic()) {
            unitStore_Before.add(new UnitToInsert(assignExpr, pos));
            lastPos = pos;
        //}
    }

    // </editor-fold>

    // <editor-fold desc="Set Level of Assign Stmt - Methods -> Interesting for LHS">

    public static void setLevelOfAssignStmt(Local l, Unit pos) {
        logger.info("Setting level in assign statement");

        String signature = getSignatureForLocal(l);

        // ToDo: Remove, when ready - currently other Methods rely on it
        Stmt assignSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(signature));

        // insert setLocalToCurrentAssingmentLevel, which accumulates the PC and the right-hand side of the assign stmt.
        // The local's sec-value is then set to that sec-value.
        Unit invoke = fac.createStmt("setLocalToCurrentAssingmentLevel", StringConstant.v(signature));

        Stmt stmt = (Stmt)pos;
        de.unifreiburg.cs.proglang.jgs.instrumentation.Type typeBefore = varTyping.getBefore(instantiation, stmt, l);
        de.unifreiburg.cs.proglang.jgs.instrumentation.Type typeAfter = varTyping.getAfter(instantiation, stmt, l);

        // TODO-no-initialize: remove
        /*
        if (typeBefore.isDynamic()
            && !typeAfter.isDynamic()) {
            logger.fine("Local type switches from dynamic -> static");
            // dynamic -> static
            JimpleInjector.stopTrackingLocal(l, stmt);
        } else if (!typeBefore.isDynamic() && typeAfter.isDynamic()) {
            logger.fine("Local type switches from static -> dynamic");
            // static -> dynamic
            JimpleInjector.startTrackingLocal(l, stmt);
        }
        */

        // insert checkLocalPC to perform NSU check (aka check that level of local greater/equal level of lPC)
        // only needs to be done if CxTyping of Statement is Dynamic.
        // Also, if the variable to update is public, the PC should be "bottom"
        Unit checkLocalPCExpr;
        if (typeBefore.isPublic()) {
            checkLocalPCExpr = fac.createStmt("checkNonSensitiveLocalPC");
        } else {
            checkLocalPCExpr = fac.createStmt("checkLocalPC", StringConstant.v(signature));
        }

        // TODO i did comment this out for some reason .. but why?
        // if variable l is not dynamic after stmt pos, we do not need to call setLocalToCurrentAssingmentLevel at all,
        // and we especially do not need to perform a NSU check!
        if (varTyping.getAfter(instantiation, (Stmt) pos, l).isDynamic()) {

            // ToDo: Remove, when ready - currently other Methods rely on it
            unitStore_Before.add(new UnitToInsert(assignSignature, pos));


            // insert NSU check only if PC is dynamic!
            if (cxTyping.get(instantiation, (Stmt) pos).isDynamic()) {
                unitStore_Before.add(new UnitToInsert(checkLocalPCExpr, pos));
            }
            unitStore_Before.add(new UnitToInsert(invoke, pos));
        }
        lastPos = pos;
    }

    /**
     * Set the level of a field of an object. It can be the field of the actually
     * analyzed object or the field
     *
     * @param f   Reference to the instance field
     * @param pos The statement where this field occurs
     */
    public static void setLevelOfAssignStmt(InstanceFieldRef f, Unit pos) {
        logger.info("Set level of field "+f.getField().getSignature()
                    +" in assign Statement located in" + b.getMethod().getName());

        String fieldSignature = getSignatureForField(f.getField());

        // Retrieve the object it belongs to
        Local tmpLocal = (Local) f.getBase();

        Unit assignSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(fieldSignature));

        // push and pop security level of instance to globalPC
        Unit pushInstanceLevelToGlobalPC
                = fac.createStmt("pushInstanceLevelToGlobalPC",
                                           StringConstant.v(getSignatureForLocal(tmpLocal)));


        Unit popGlobalPC = fac.createStmt("popGlobalPC");


        // insert: checkGlobalPC(Object, String)
        // why do we check the global PC? Because the field is possibily visible everywhere, check that sec-value of field is greater
        // or equal than the global PC.
        Unit checkGlobalPCExpr = fac.createStmt("checkGlobalPC", tmpLocal, StringConstant.v(fieldSignature));

        // insert setLevelOfField, which sets Level of Field to the join of gPC
        // and right-hand side of assign stmt sec-value join
        Unit assignExpr = fac.createStmt( "setLevelOfField", tmpLocal, StringConstant.v(fieldSignature));

        // pushInstanceLevelToGlobalPC and popGlobalPC take the instance, push to global pc; and pop afterwards.
        // see NSU_FieldAccess tests why this is needed
        unitStore_Before.add(new UnitToInsert(pushInstanceLevelToGlobalPC, pos));
        unitStore_Before.add(new UnitToInsert(assignSignature, pos));
        // only if context ist dynamic / pc is dynamc
        if (cxTyping.get(instantiation, (Stmt) pos).isDynamic()) {
            unitStore_Before.add(new UnitToInsert(checkGlobalPCExpr, pos));
        }
        unitStore_Before.add(new UnitToInsert(assignExpr, pos));
        unitStore_Before.add(new UnitToInsert(popGlobalPC, pos));
        lastPos = pos;
    }


    public static void setLevelOfAssignStmt(StaticFieldRef f, Unit pos) {
        logger.info("Set Level of static Field " + f.toString() + " in assign stmt");

        SootField field = f.getField();

        String signature = getSignatureForField(field);


        SootClass sc = field.getDeclaringClass();

        Unit assignDeclaringClass = Jimple.v().newAssignStmt(
                local_for_Objects, ClassConstant.v(sc.getName().replace(".", "/")));

        Unit assignSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(signature));

        // insert: checkGlobalPC(Object, String)
        Unit checkGlobalPCExpr = fac.createStmt("checkGlobalPC",
                ClassConstant.v(sc.getName().replace(".", "/")),
                StringConstant.v(signature)
                );

        // Add setLevelOfField
        Unit assignExpr = fac.createStmt("setLevelOfField",
                ClassConstant.v(sc.getName().replace(".", "/")),
                StringConstant.v(signature)
                );

        unitStore_Before.add(
                new UnitToInsert(assignDeclaringClass, pos));
        unitStore_Before.add(new UnitToInsert(assignSignature, pos));
        if (cxTyping.get(instantiation, (Stmt) pos).isDynamic()) {
            unitStore_Before.add(new UnitToInsert(checkGlobalPCExpr, pos));
        }
        unitStore_Before.add(new UnitToInsert(assignExpr, pos));
        lastPos = pos;
    }

    /**
     * Inject a method of HandleStmt to set the level of an array-field. This method
     * distinguishes two cases, one case where the index of the referenced array-field
     * is a constant number and the other case, where the index is stored in a local variable.
     * In the second case, the signature of the local variable also must be passed as an
     * argument to {@link analyzer.level2.HandleStmt#setLevelOfArrayField} .
     *
     * @param a   -ArrayRef. The reference to the array-field
     * @param pos -Unit- The assignStmt in the analyzed methodTypings body, where this
     *            reference appears.
     */
    public static void setLevelOfAssignStmt(ArrayRef a, Unit pos) {
        logger.info("Set level of array " + a.toString() + " in assign stmt");

        // Add extra locals for arguments
        if (!extralocals) {
            locals.add(local_for_Strings2);
            locals.add(local_for_Strings3);
            extralocals = true;
        }

        String signatureForField = getSignatureForArrayField(a);
        String signatureForObjectLocal = getSignatureForLocal((Local) a.getBase());

        // List for the arguments for HandleStmt.setLevelOfArrayField()
        List<Value> args = new ArrayList<>();
        args.add(a.getBase());

        // Store all string-arguments in locals for strings and assign the locals to the
        // argument list.
        Unit assignFieldSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(signatureForField));
        Unit assignObjectSignature = Jimple.v().newAssignStmt(local_for_Strings2,
                StringConstant.v(signatureForObjectLocal));

        args.add(StringConstant.v(signatureForField));
        args.add(StringConstant.v(signatureForObjectLocal));


        if (!(a.getIndex() instanceof Local)) {
            // Case where the index is a constant.
            // The needed arguments are "Object o, String field, String localForObject".

            logger.fine("Index value for the array field is a constant value");

        } else if (a.getIndex() instanceof Local) {
            // The index is a local and must be given as a parameter.
            // The needed arguments are
            // "Object o, String field, String localForObject, String localForIndex".

            logger.fine("Index value for the array field is stored in a local");

            // add a further parameter type for String localForIndex and
            // add it to the arguments-list.

            String localSignature = getSignatureForLocal((Local) a.getIndex());
            Unit assignIndexSignature = Jimple.v().newAssignStmt(local_for_Strings3,
                    StringConstant.v(localSignature));

            args.add(StringConstant.v(localSignature));

            unitStore_Before.add(new UnitToInsert(
                    assignIndexSignature, pos));

        }

        // checkArrayWithGlobalPC
        Unit checkArrayGlobalPCExpr = fac.createStmt("checkArrayWithGlobalPC", args.toArray(new Value[0]));


        // setLevelOfArrayField
        Unit assignExpr = fac.createStmt("setLevelOfArrayField", args.toArray(new Value[0]));

        unitStore_Before.add(
                new UnitToInsert(assignFieldSignature, pos));
        unitStore_Before.add(
                new UnitToInsert(assignObjectSignature, pos));
        unitStore_Before.add(
                new UnitToInsert(checkArrayGlobalPCExpr, pos));
        unitStore_Before.add(
                new UnitToInsert(assignExpr, pos));
        lastPos = pos;
    }

    // </editor-fold>

    /**
     * Note: Altough method is not used by jimpleInjector, the corresponding handleStatement method is used in the manually instrumented tests.
     */
    @SuppressWarnings("unused")
    public static void assignReturnLevelToLocal(Local l, Unit pos) {
        logger.log(Level.INFO, "Assign return level of invoked method to local {0}",
                getSignatureForLocal(l));

        ArrayList<Type> parameterTypes = new ArrayList<>();
        parameterTypes.add(RefType.v("java.lang.String"));



        Expr assignRet = Jimple.v().newVirtualInvokeExpr(
                hs,
                Scene.v().makeMethodRef(
                        Scene.v().getSootClass(HANDLE_CLASS),
                        "assignReturnLevelToLocal",
                        parameterTypes,
                        VoidType.v(),
                        false),
                StringConstant.v(getSignatureForLocal(l)));

        Unit assignExpr = Jimple.v().newInvokeStmt(assignRet);

        unitStore_After.add(new UnitToInsert(assignExpr, pos));
        lastPos = assignExpr;
    }

    public static void assignArgumentToLocal(int posInArgList, Local local) {
        logger.log(Level.INFO, "Assign argument level to local " + local.toString());

        ArrayList<Type> parameterTypes = new ArrayList<>();
        parameterTypes.add(IntType.v());
        parameterTypes.add(RefType.v("java.lang.String"));

        Expr assignArg = Jimple.v().newVirtualInvokeExpr(
                hs,
                Scene.v().makeMethodRef(
                        Scene.v().getSootClass(HANDLE_CLASS), "assignArgumentToLocal",
                        parameterTypes,
                        Scene.v().getObjectType(),
                        false),
                IntConstant.v(posInArgList),
                StringConstant.v(getSignatureForLocal(local))
        );

        Unit assignExpr = Jimple.v().newInvokeStmt(assignArg);

        // only assign Argument to Local if Argument is of Dynamic Type
        if (instantiation.get(posInArgList).isDynamic()) {
            String localSig = getSignatureForLocal(local);
            unitStore_After.add(new UnitToInsert(assignExpr, lastPos));
            lastPos = assignExpr;
        }
    }



    /*******************************************************************************************
     *	Inter-scope functions.
     ******************************************************************************************/


    public static void returnConstant(Unit retStmt) {
        logger.log(Level.INFO, "Return a constant value");

        ArrayList<Type> parameterTypes = new ArrayList<>();

        Expr returnConst = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
                Scene.v().getSootClass(HANDLE_CLASS), "returnConstant",
                parameterTypes, VoidType.v(), false));

        if (instantiation.getReturn().isDynamic()) {
            unitStore_Before.add(new UnitToInsert(
                    Jimple.v().newInvokeStmt(returnConst), retStmt));
        }
    }

    public static void returnLocal(Local l, Unit pos) {
        logger.log(Level.INFO, "Return Local {0}", getSignatureForLocal(l));

        ArrayList<Type> parameterTypes = new ArrayList<>();
        parameterTypes.add(RefType.v("java.lang.String"));

        Stmt sig = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(getSignatureForLocal(l)));

        Expr returnLocal = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
                Scene.v().getSootClass(HANDLE_CLASS), "returnLocal", parameterTypes,
                VoidType.v(), false), local_for_Strings);

        Stmt returnL = Jimple.v().newInvokeStmt(returnLocal);

        if (instantiation.getReturn().isDynamic()) {
            unitStore_Before.add(new UnitToInsert(sig, pos));
            unitStore_Before.add(new UnitToInsert(returnL, pos));
            lastPos = pos;
        }
    }

    /**
     * Store the levels of all arguments in a list in ObjectMap. If an
     * argument is a constant, then the argument is stored as "DEFAULT_LOW".
     *
     * @param pos        position of actual statement
     * @param lArguments list of arguments
     */

    // obect map ist global erreichbar. dahin lege temporär die argumente.
    // dann beim aufruf schaut die neue local map in der neuen methode in die global
    // map und nimmt sich von da die level der gerade übergebenen argumente.
    public static void storeArgumentLevels(Unit pos, Local... lArguments) {

        logger.log(Level.INFO, "Store Arguments for next method in method {0}",
                b.getMethod().getName());

        int length = lArguments.length;

        ArrayList<Type> parameterTypes = new ArrayList<>();
        parameterTypes.add(ArrayType.v(RefType.v("java.lang.String"), 1));

        Expr paramArray = Jimple.v().newNewArrayExpr(RefType.v(
                "java.lang.String"), IntConstant.v(length));

        Unit assignNewStringArray = Jimple.v().newAssignStmt(
                local_for_String_Arrays, paramArray);

        Unit[] tmpUnitArray = new Unit[length];

        boolean dynamicArgsExist = false;
        for (int i = 0; i < length; i++) {

            // This results in code like: local_for_String_Arrays[0] = "boolean_z0";
            // for each local that is given as an argument, jimple code is injected that assings this local to a slot
            // in a vector. This vector is given to the HandleStmt.storeArgumentLevels method.
            // It's possible to get a null vector as an argument. This happens,
            // if a constant is set as argument.
            if (lArguments[i] != null && varTyping.getBefore(instantiation, (Stmt) pos, lArguments[i]).isDynamic()) {
                String signature = getSignatureForLocal(lArguments[i]);
                tmpUnitArray[i] = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(
                        local_for_String_Arrays, IntConstant.v(i)),
                        StringConstant.v(signature));
                dynamicArgsExist = true;
                // else if () ..

            } else if (lArguments[i] != null && ! varTyping.getBefore(instantiation, (Stmt) pos, lArguments[i]).isDynamic()) {
                tmpUnitArray[i] = null;
            } else {
                // if arguments are all constants
                tmpUnitArray[i] = Jimple.v().newAssignStmt(Jimple.v().newArrayRef(
                        local_for_String_Arrays, IntConstant.v(i)),
                        StringConstant.v("DEFAULT_LOW"));
                dynamicArgsExist = true;        // this is neccessary, else VerifyError ...?
            }
        }

        Expr storeArgs = Jimple.v().newVirtualInvokeExpr(hs, Scene.v().makeMethodRef(
                Scene.v().getSootClass(HANDLE_CLASS), "storeArgumentLevels",
                parameterTypes, VoidType.v(), false), local_for_String_Arrays);
        Stmt invokeStoreArgs = Jimple.v().newInvokeStmt(storeArgs);

        // only store arguments if it's even neccessary (e.g. one of the arguments is public)
        // if no argument is dynamic, don't even call storeArgumentLevels
        if(dynamicArgsExist) {
            unitStore_Before.add(new UnitToInsert(assignNewStringArray, pos));
        }
        for (Unit el : tmpUnitArray) {
            if (el != null) {
                // if el.equals(null), the corresponding local is public. See loop above.
                unitStore_Before.add(new UnitToInsert(el, pos));
            }
        }
        if (dynamicArgsExist) {
            unitStore_Before.add(new UnitToInsert(invokeStoreArgs, pos));
            lastPos = pos;
        }

    }

    public static void checkThatLe(Local l, String level, Unit pos) {
        checkThatLe(l, level, pos, "checkThatLe");
    }

    /**
     * Insert the following check: If Local l is high, throw new IFCError
     */
    public static void checkThatLe(Local l, String level, Unit pos, String methodName) {
        logger.info("Check that " + l + " is not high");

        if (l == null) {
            throw new InternalAnalyzerException("Argument is null");
        }

        ArrayList<Type> paramTypes = new ArrayList<>();
        paramTypes.add(RefType.v("java.lang.String"));
        paramTypes.add(RefType.v("java.lang.String"));

        String signature = getSignatureForLocal(l);

        Stmt assignSignature = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(signature));

        Expr invokeSetLevel = Jimple.v().newVirtualInvokeExpr(
                hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
                        methodName, paramTypes, VoidType.v(), false),
                local_for_Strings, StringConstant.v(level));
        Unit invoke = Jimple.v().newInvokeStmt(invokeSetLevel);

        // TODO: why check for isDynamic here?
        // if (varTyping.getBefore(instantiation, (Stmt) pos, l).isDynamic()) {
            unitStore_Before.add(new UnitToInsert(assignSignature, pos));
            unitStore_Before.add(new UnitToInsert(invoke, pos));
            lastPos = pos;
        // }
    }

    /**
     * Method to check that PC is not of specified level, or above. Called when calling System.out.println(),
     * which must always be called in low context because of its side effects.
     *
     * @param level level that the PC must not exceed
     * @param pos   position where to insert statement
     */
    public static void checkThatPCLe(String level, Unit pos) {
        logger.info("Check that context is " + level + "or above");

        if (pos == null) {
            throw new InternalAnalyzerException("Position is Null");
        }

        ArrayList<Type> paramTypes = new ArrayList<>();
        paramTypes.add(RefType.v("java.lang.String"));

        Expr checkPC = Jimple.v().newVirtualInvokeExpr(
                hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
                        "checkThatPCLe", paramTypes, VoidType.v(), false), StringConstant.v(level));
        Unit invoke = Jimple.v().newInvokeStmt(checkPC);

        // only if PC is dynamic
        if (cxTyping.get(instantiation, (Stmt) pos).isDynamic()) {
            unitStore_Before.add(new UnitToInsert(invoke, pos));
            lastPos = pos;
        }
    }

    /**
     * Check condition of if statements. Needed parameters are all locals (no constants)
     * which occur in the if statement. If the result is high, then the lpc of the if-context
     * is set to high.
     *
     * @param pos    Position of the ifStmt in the method body.
     * @param locals An array of all locals which appear in the condition.
     */
    public static void checkCondition(Unit pos, Local... locals) {

        logger.log(Level.INFO, "Check condition in method {0}", b.getMethod());
        logger.log(Level.INFO, "IfStmt: {0}", pos.toString());

        int numberOfLocals = locals.length;
        ArrayList<Type> paramTypes = new ArrayList<>();
        paramTypes.add(RefType.v("java.lang.String"));
        paramTypes.add(ArrayType.v(RefType.v("java.lang.String"), 1)); // here

        // Add hashvalue for immediate dominator
        String domIdentity = DominatorFinder.getImmediateDominatorIdentity(pos);
        logger.info("Identity of Dominator of \"" + pos.toString()
                + "\" is " + domIdentity);
        Stmt assignHashVal = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(domIdentity));

        // Add all locals to string array
        Expr newStringArray = Jimple.v().newNewArrayExpr(
                RefType.v("java.lang.String"), IntConstant.v(numberOfLocals));

        Unit assignNewArray = Jimple.v().newAssignStmt(
                local_for_String_Arrays, newStringArray);

        ArrayList<Unit> tmpUnitList = new ArrayList<>();

        for (int i = 0; i < numberOfLocals; i++) {
            Unit assignSignature = Jimple.v().newAssignStmt(
                    Jimple.v().newArrayRef(local_for_String_Arrays,
                            IntConstant.v(i)),
                    StringConstant.v(getSignatureForLocal(locals[i])));
            tmpUnitList.add(assignSignature);
        }

        // Invoke HandleStmt.checkCondition(String domHash, String... locals)
        Expr invokeCheckCondition = Jimple.v().newVirtualInvokeExpr(
                hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
                        "checkCondition", paramTypes, VoidType.v(), false),
                local_for_Strings, local_for_String_Arrays);
        Unit invokeCC = Jimple.v().newInvokeStmt(invokeCheckCondition);

        unitStore_Before.add(new UnitToInsert(assignNewArray, pos));
        lastPos = assignNewArray;
        for (Unit u : tmpUnitList) {
            unitStore_After.add(new UnitToInsert(u, lastPos));
            lastPos = u;
        }
        unitStore_After.add(new UnitToInsert(assignHashVal, lastPos));
        lastPos = assignHashVal;
        unitStore_After.add(new UnitToInsert(invokeCC, lastPos));
        lastPos = invokeCC;

    }

    /**
     * If a stmt is a postdominator of an ifStmt then the if-context ends before this stmt.
     * The method exitInnerScope pops the localPCs for all ifStmts which end here.
     *
     * @param pos The position of this stmt.
     */
    public static void exitInnerScope(Unit pos) {
        logger.log(Level.INFO, "Exit inner scope in method {0}", b.getMethod().getName());

        ArrayList<Type> paramTypes = new ArrayList<>();
        paramTypes.add(RefType.v("java.lang.String"));

        String domIdentity = DominatorFinder.getIdentityForUnit(pos);
        logger.info("Dominator \"" + pos.toString()
                + "\" has identity " + domIdentity);

        Stmt assignHashVal = Jimple.v().newAssignStmt(
                local_for_Strings, StringConstant.v(domIdentity));


        Expr specialIn = Jimple.v().newVirtualInvokeExpr(
                hs, Scene.v().makeMethodRef(Scene.v().getSootClass(HANDLE_CLASS),
                        "exitInnerScope", paramTypes, VoidType.v(), false),
                local_for_Strings);

        Unit inv = Jimple.v().newInvokeStmt(specialIn);

        unitStore_Before.add(
                new UnitToInsert(assignHashVal, pos));
        unitStore_Before.add(new UnitToInsert(inv, pos));
        lastPos = pos;
    }
	
/*
 * Internal methodTypings
 */

    /**
     * Assures that a "@this" reference is present
     * @throws InternalAnalyzerException if not present.
     */
    private static void assureThisRef() {
        Unit first = units.getFirst();
        Value obj = first.getUseBoxes().get(0).getValue();
        // Check if the first unit is a reference to the actual object
        if (!( first instanceof IdentityStmt ) || !( obj instanceof ThisRef )) {
            throw new InternalAnalyzerException("Expected @this reference");
        }
    }

    /**
     *
     */
    static void addUnitsToChain() {

        // First add all elements from unitStore_Before
        Iterator<UnitToInsert> uIt = unitStore_Before.iterator();

        while (uIt.hasNext()) {
            UnitToInsert item = uIt.next();
            if (item.getPosition() == null) {
                units.addFirst(item.getUnit());
            } else {
                units.insertBefore(item.getUnit(), item.getPosition());
            }
        }

        // Now add all elements from unitStore_After
        uIt = unitStore_After.iterator();

        while (uIt.hasNext()) {
            UnitToInsert item = uIt.next();

            if (item.getPosition() == null) {
                units.addFirst(item.getUnit());
            } else {
                units.insertAfter(item.getUnit(), item.getPosition());
            }
        }

        unitStore_After.clear();
        unitStore_Before.clear();

        b.validate();
    }

    /**
     * Add all locals which are needed from JimpleInjector to store values
     * of parameters for invoked methodTypings.
     */
    // Todo: Remove, when ready
    static void addNeededLocals() {
        locals.add(local_for_Strings);
        locals.add(local_for_String_Arrays);
        locals.add(local_for_Objects);

        b.validate();
    }

    // <editor-fold desc="Signature Calculation Methods">

    /**
     * Calculates and returns the Signature of a given Local
     * @param l the local which signature is to be retrieved
     * @return corresponding signature
     * @see Local#getType()
     * @see Local#getName()
     */
    private static String getSignatureForLocal(Local l) {
        return l.getType() + "_" + l.getName();
    }

    /**
     * Calculates and returns the Signature of given SootField
     * @param f The Field of which the signature is required.
     * @return The signature of the SootField
     * @see SootField#getSignature()
     */
    private static String getSignatureForField(SootField f) {
        return f.getSignature();
    }

    /**
     * Creates the signature of an array-field based on the index.
     * It simply returns the int-value as string.
     *
     * @param a The ArrayRef of which the Signature is required.
     * @return The signature for the array-field.
     * @throws InternalAnalyzerException if the index type is not int.
     */
    private static String getSignatureForArrayField(ArrayRef a) {
        if (!Objects.equals(a.getIndex().getType().toString(), "int")) {
            throw new InternalAnalyzerException("Unexpected type of index");
        }
        return a.getIndex().toString();
    }

    // </editor-fold>

    // <editor-fold desc="Jimple Helper Methods">

    /**
     * Calculates the start position for inserting further units.
     * @param b The Body, that may contain Units.
     * @return the start position of the first Unit.
     */
    private static int getStartPos(Body b) {
        // Getting the Method, that is currently calculated.
        SootMethod m = b.getMethod();

       /* Depending on the number of arguments and whether it is a static method
       * or a Constructor the position is calculated.
       * Because there are statements, which shouldn't be preceded by other
       * statements.
       * */

        int startPos = (!m.isStatic()) ? 1 : 0;
        startPos = (m.isConstructor()) ? startPos + 1 : startPos;
        startPos += m.getParameterCount();

        logger.fine("Calculated start position: " + startPos + " of " +b);
        return startPos;
    }

    /**
     * Gets the Unit, that is stored in the given units at the given Position.
     * @param units The Chain of Units, that contains severall Units
     * @param pos The position of the Unit, that is wanted to be extracted.
     * @return The unit at the given position.
     * @throws IndexOutOfBoundsException if the Position is greater the number of
     * units in the given Unit chain or lower 0.
     */
    private static Unit getUnitOf(Chain<Unit> units, int pos) {
        if (pos < 0 || pos >= units.size())
            throw new IndexOutOfBoundsException("No legal index: "+pos);
        int idx = 0;
        for (Unit u : units) {
            if (idx == pos) return u;
            idx++;
        }
        return null;
    }

    // </editor-fold>

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

    static <Lvel> void setStaticAnalaysisResults(VarTyping<Lvel> varTy, CxTyping<Lvel> cxTy, Instantiation<Lvel> inst,
                                                 Casts c) {
        varTyping = varTy;
        cxTyping = cxTy;
        instantiation = inst;
        casts = c;
    }

    /**
     * Handle Casts.cast(String s, T local) method
     * @param aStmt         Jimple assign statement whose right-hand side is the cast
     */
    public static void handleCast(AssignStmt aStmt) {

        if (casts.isValueCast(aStmt)) {
            Casts.ValueConversion conversion = casts.getValueCast(aStmt);
            logger.fine("Found value cast: " + conversion);

            if (conversion.getSrcType().isDynamic() && !conversion.getDestType().isDynamic()) {
                // Check eines Security Wert: x = (? => LOW) y
                logger.fine("Convertion is: dynamic->static");
                Option<Value> srcValue = conversion.getSrcValue();
                if (srcValue.isDefined()) {
                    Local rightHandLocal = (Local) srcValue.get();

                    Object destLevel = conversion.getDestType().getLevel();
                    logger.fine( "Inserting check: "
                                 + getSignatureForLocal(rightHandLocal)
                                 + " <= "
                                 + destLevel);
                    checkThatLe(rightHandLocal, destLevel.toString(), aStmt, "checkCastToStatic");

                    logger.fine("Setting destination variable to: " + destLevel);
                    makeLocal((Local) aStmt.getLeftOp(), destLevel.toString(), aStmt);
                } else {
                    logger.info("Source value is pubilc. Not inserting checks.");
                }
            } else if ( !conversion.getSrcType().isDynamic() && conversion.getDestType().isDynamic()) {
                // Initialisierung eines Security Wert: x = (H => ? ) y
                logger.fine("Conversion is: static->dynamic");
                Object srcLevel;
                de.unifreiburg.cs.proglang.jgs.instrumentation.Type type = conversion.getSrcType();
                if (type.isPublic()) {
                    srcLevel = CurrentSecurityDomain.bottom();
                } else {
                    srcLevel = type.getLevel();
                }
                logger.fine("Setting destination variable to: " + srcLevel);
                makeLocal((Local) aStmt.getLeftOp(), srcLevel.toString(), aStmt);
            } else if ( conversion.getSrcType().isDynamic() && conversion.getDestType().isDynamic()) {
                logger.fine("Conversion is: dynamic->dynamic");
                logger.fine("Ignoring trivial conversion.");
                // TODO: casts should be run on the rhs and set the "assignment level" here.
            } else {
                logger.fine("Conversion is: static->static");
                logger.fine("Ignoring trivial conversion.");
                // TODO: casts should be run on the rhs and set the "assignment level" here.
                // TODO: if for some reason the type analysis is not available, we should check that the conversion is correct here
            }
        }
    }


    /**
     * Insert "stopTrackingLocal" call.
     */
    public static void stopTrackingLocal(Local l, Stmt callStmt) {
        unitStore_Before.add(new UnitToInsert(fac.createStmt("stopTrackingLocal", StringConstant.v(getSignatureForLocal(l))),
                                              callStmt));
    }
}

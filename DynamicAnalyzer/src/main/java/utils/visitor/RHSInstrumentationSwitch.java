package utils.visitor;

import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.jimple.*;
import utils.logging.L1Logger;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * This class handles now the right hand side of an Assign Expression
 * as Switch it implements the Visitor Pattern for the Values present in
 * the Soot Framework.
 *
 * An Assignment is something like:
 *  l = e[x] + f(c.foo, Math.PI);
 *
 * This switch only treats the right hand side of that Expression, so
 * therefore are only for interesting or possible cases:
 *  local Variables, Array References, static or instance Field access.
 *
 * @author Karsten Fix, 13.Sept 2017
 */
public class RHSInstrumentationSwitch extends AbstractJimpleValueSwitch {

    /** Gets the Logger to log different, important messages */
    private final Logger logger = L1Logger.getLogger();

    /** Saves the Statement, that calls the referring Method of its type.
     * The Statement shall be known at creation of the Switch. */
    private final AssignStmt callStmt;

    /**
     * Creates a new Value Switch for the Right Side of an Assignment.
     * Therefore the given Stmt has to be an Assignment.
     *
     * @param call The Assignment Stmt, that is found in the
     * {@link AnnotationStmtSwitch::caseAssignStmt } and calls the apply Method.
     */
    public RHSInstrumentationSwitch(AssignStmt call) {
        callStmt = call;
        logger.finer("Created RightAssignVS for: "+call);
    }

    // <editor-fold desc="Variable Cases">

    /**
     * The Case of a local Variable is interesting for the Right Side of an Assignment.
     * Let h = l; be the callStmt, then the given Local shall be l
     * @param l The Local, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector ::addLevelInAssignStmt
     */
    @Override
    public void caseLocal(Local l) {
        logger.finest("case Local with: " + l);
        JimpleInjector.addLevelInAssignStmt(l, callStmt);
    }

    /**
     * The Case of an ArrayRef is interesting for the Left Side of an Assignment.
     * Let h = x[0]; be the callStmt, then the given ArrayRef shall be x[0]
     * @param v The ArrayRef, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector::addLevelInAssignStmt
     */
    @Override
    public void caseArrayRef(ArrayRef v) {
        logger.finest("case ArrayRef with: " + v);
        JimpleInjector.addLevelInAssignStmt(v, callStmt);
    }

    /**
     * The Case of an StaticFieldRef is interesting for the Left Side of an Assignment.
     * Let x[3] = Math.PI; be the callStmt, then the given StaticFieldRef shall be Math.PI
     * Therefore PI is accessed from a static Context.
     * @param v The StaticFieldRef, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector::addLevelInAssignStmt
     */
    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        logger.finest("case StaticFieldRef with: " + v);
        JimpleInjector.addLevelInAssignStmt(v, callStmt);
    }

    /**
     * The Case of an InstanceFieldRef is interesting for the Left Side of an Assignment.
     * Let bar = c.foo; be the callStmt, then the given InstanceFieldRef shall be c.foo
     * Therefore foo is accessed from a non-static Context, means it uses an instance.
     * @param v The InstanceFieldRef, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector::addLevelInAssignStmt
     */
    @Override
    public void caseInstanceFieldRef(InstanceFieldRef v) {
        logger.finest("case InstanceFieldRef with: " + v);
        JimpleInjector.addLevelInAssignStmt(v, callStmt);
    }

    // </editor-fold>

    // <editor-fold desc="Array Cases">

    /**
     * On the right hand side of an assignment could be an array declaration.
     * Let l = new int[1]; be the statement, then new int[1] would be the given
     * newArrayExpression v
     * @param v The NewArrayExpr, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector::addArrayToObjectMap
     */
    @Override
    public void caseNewArrayExpr(NewArrayExpr v) {
        logger.finest("case NewArrayExpr: " + v);
        JimpleInjector.addArrayToObjectMap((Local) callStmt.getLeftOp(), callStmt);
    }

    /**
     * On the right hand side of an assignment could be an multiArray declaration.
     * Let l = new int[1][6]; be the statement, then new int[1][6] would be the given
     * newMultiArrayExpression v
     * @param v The NewMultiArrayExpr, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector::addArrayToObjectMap
     */
    @Override
    public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
        logger.finest("case NewMultiArrayExpr: " + v);
        JimpleInjector.addArrayToObjectMap((Local) callStmt.getLeftOp(), callStmt);
    }

    // </editor-fold>

    // <editor-fold desc="Invoke Cases">

    // </editor-fold>

    @Override
    public void defaultCase(Object obj) {

    }
}

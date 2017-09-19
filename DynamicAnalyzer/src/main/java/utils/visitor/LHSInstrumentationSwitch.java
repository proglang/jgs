package utils.visitor;

import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.jimple.*;
import utils.logging.L1Logger;

import java.util.logging.Logger;

/**
 * This class handles now the left hand side of an Assign Expression
 * as Switch it implements the Visitor Pattern for the Values present in
 * the Soot Framework.
 *
 * An Assignment is something like:
 *  l = e[x] + f(c.foo, Math.PI);
 *
 * This switch only treats the left hand side of that Expression, so
 * therefore are only for interesting or possible cases:
 *  local Variables, Array References, static or instance Field access.
 *
 * @author Karsten Fix, 13.Sept 2017
 */
public class LHSInstrumentationSwitch extends AbstractJimpleValueSwitch {

    /** Gets the Logger to log different, important messages */
    private final Logger logger = L1Logger.getLogger();

    /** Saves the Statement, that calls the referring Method of its type.
     * The Statement shall be known at creation of the Switch. */
    private final Stmt callStmt;

    /**
     * Creates a new Value Switch for the Left Side of an Assignment.
     * Therefore the given Stmt has to be an Assignment.
     *
     * @param call The Assignment Stmt, that is found in the
     * {@link AnnotationStmtSwitch::caseAssignStmt } and calls the apply Method.
     */
    public LHSInstrumentationSwitch(Stmt call) {
        callStmt = call;
        logger.finer("Created LeftAssignVS for: "+call);
    }

    /**
     * The Case of a local Variable is interesting for the Left Side of an Assignment.
     * Let l = 5; be the callStmt, then the given Local shall be l
     * @param l The Local, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector#setLevelOfAssignStmt
     */
    @Override
    public void caseLocal(Local l) {
        logger.finest("case Local with: " + l);
        JimpleInjector.setLevelOfAssignStmt(l, callStmt);
    }

    /**
     * The Case of an ArrayRef is interesting for the Left Side of an Assignment.
     * Let x[0] = 5; be the callStmt, then the given ArrayRef shall be x[0]
     * @param v The ArrayRef, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector::setLevelOfAssignStmt
     */
    @Override
    public void caseArrayRef(ArrayRef v) {
        logger.finest("case ArrayRef with: " + v);
        JimpleInjector.setLevelOfAssignStmt(v, callStmt);
    }

    /**
     * The Case of an StaticFieldRef is interesting for the Left Side of an Assignment.
     * Let Math.PI = 3; be the callStmt, then the given StaticFieldRef shall be Math.PI
     * Therefore PI is accessed from a static Context.
     * @param v The StaticFieldRef, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector::setLevelOfAssignStmt
     */
    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        logger.finest("case StaticFieldRef with: " + v);
        JimpleInjector.setLevelOfAssignStmt(v, callStmt);
    }

    /**
     * The Case of an InstanceFieldRef is interesting for the Left Side of an Assignment.
     * Let c.foo = bar; be the callStmt, then the given InstanceFieldRef shall be c.foo
     * Therefore foo is accessed from a non-static Context, means it uses an instance.
     * @param v The InstanceFieldRef, that is calling this case fulfilling the Visitor Pattern.
     * @see JimpleInjector::setLevelOfAssignStmt
     */
    @Override
    public void caseInstanceFieldRef(InstanceFieldRef v) {
        logger.finest("case InstanceFieldRef with: " + v);
        JimpleInjector.setLevelOfAssignStmt(v, callStmt);
    }

    /**
     * The default case shall never appear, if
     * (1) The case List is complete
     * (2) The Usage of the Switch is right
     * @param obj The Object, that causes the default Case.
     */
    @Override
    public void defaultCase(Object obj) {
        logger.fine("Default Case with: "+obj + " of: " + callStmt);
    }
}
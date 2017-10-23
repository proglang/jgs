package analyzer.level1;

/**
 * This Class Provides the Current Selected Policy for
 * the Dynamic Checking, as well, as the Enum, that
 * defines the available Policies.
 *
 * @author Karsten Fix, 23.10.17
 */
public class DynamicPolicy {

    /**
     * Specifies which Policy is currently selected.
     */
    public static Policy selected = Policy.NSU_POLICY;

    /**
     * As the name suggests it is a policy for the dynamic checker.
     * In specific: Its use specifies which policy should be used
     * for the Assignment of Locals. <br>
     * Either: NSU Policy or the Hybrid Enforcement Technique.
     *
     * @author Karsten Fix, 23.10.17
     */
    public enum Policy {
        /**
         * The NSU Policy does not allow, to assign a Local of LOW Security,
         * within a HIGH Security Content.
         */
        NSU_POLICY,

        /**
         * This Technique Checks the Control Flow Graph of the
         * analyzed Program for finding Post Dominators, and
         * updates the Security Level of the whole Write Effect,
         * that are Locals.
         */
        HYBRID_ENFORCEMENT
    }

}

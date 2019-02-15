package analyzer.level2;

/**
 * An interface to handle particular events that occur during monitoring. The
 * SecurityMonitorLog is *not* intended for security enforcement. It is only
 * intended to provide hooks to "meta-tasks" like logging and unit testing.
 *
 * See the SimpleSecurityMonitorLog and EventChecker for two examples.
 */
public interface SecurityMonitorLog {


    /**
     * Called when checking gpc <= field when writing to field.
     */
    void checkGlobalPCLEPutfield(String fieldSignature,
                                 Object globalPc,
                                 Object fieldLevel);

    /**
     * Called when checking lpc <= level, e.g. when printing.
     */
    void checkLocalPCLELevel(String level);

    /**
     * Called when checking lpc <= labelOf(local)
     */
    void checkLocalPCLELocal(String signature);

    /**
     * Called when checking "label-of-local-variable" <= level.
     */
    void checkLocalLELevel(String signature, String level);

    /**
     * Called when assigning a method argument to a local variable.
     */
    void assignArgumentToLocal(int pos, String signatureOfLocal);

    /**
     * Called when assigning the result of a method call to a local variable.
     */
    void assignReturnValueToLocal(String signatureOfLocal, Object returnLevel);

    /**
     * Called when returning a constant.
     */
    void returnConstant();

    /**
     * Called when setting the security level for returning a local variable.
     */
    void returnLocal(String signature, Object level);

    /**
     * Called when setting the level of a method's return value.
     */
    void setReturnLevelAfterInvokeStmt(String signature);


    /**
     * Called during calculation of an expression's level. The level of the
     * local is joined with the level calculated so far.
     */
    void joinWithLevelOfCurrentExpressions(String local);

    /**
     * Called when the monitor is shut down; probably because the program has ended.
     */
    void endOfMonitoring();


}



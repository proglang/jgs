package analyzer.level2;

/**
 * An enum identifying a set of events that the instrumentation
 * inserts into dynamic code.
 */
public enum SecurityMonitoringEvent {
    PASSED,
    ILLEGAL_FLOW,
    NSU_FAILURE,
    CHECK_LOCAL_PC_CALLED,
    ASSIGN_ARG_TO_LOCAL,
    JOIN_LEVEL_OF_LOCAL_AND_ASSIGNMENT_LEVEL,
    SET_RETURN_AFTER_INVOKE,
    CHECK_THAT_LE,
    CHECK_THAT_PC_LE,
    RETURN_LOCAL,
    RETURN_CONSTANT,
    SET_RETURN_LEVEL_AFTER_INVOKE_STMT,
    CHECK_GLOBAL_PC_LE_PUTFIELD;

}

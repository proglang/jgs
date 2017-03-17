package utils.staticResults.superfluousInstrumentation;

/**
 * Created by Nicolas Müller on 06.02.17.
 * The assigned number is necessary to hand over the expected exception to jimple,
 * see initHandleStmtUtils in class {@link analyzer.level1.JimpleInjector}.
 */
public enum ExpectedException {
    NONE(0),
    ILLEGAL_FLOW(1),
    NSU_FAILURE(12),
    CHECK_LOCAL_PC_CALLED(2),
    ASSIGN_ARG_TO_LOCAL(3),
    JOIN_LEVEL_OF_LOCAL_AND_ASSIGNMENT_LEVEL(4),
    SET_RETURN_AFTER_INVOKE(5),
    CHECK_THAT_LE(6),
    CHECK_THAT_PC_LE(7),
    // STORE_ARGUMENT_LEVELS(8),        // inactive, see corresponding jimpleInjector method for explanation
    RETURN_LOCAL(9),
    RETURN_CONSTANT(10),
    SET_RETURN_LEVEL_AFTER_INVOKE_STMT(11);

    private int number;

    ExpectedException(int number) {
        this.number = number;
    }

    public int getVal() {
        return this.number;
    }

}

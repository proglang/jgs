package utils.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 16.02.17.
 * Exception thrown only if joinLevelOfLocalAndAssignmentLevel method in {@link analyzer.level2.HandleStmt}
 * is called with active controller. As all exceptions in SuperfluousInstrumentation package,
 * this exception is used to find, well, superfluous instrumentation by the dynamic analyzer.
 */
public class joinLevelOfLocalAndAssignmentLevelException extends RuntimeException {
    private static final long serialVersionUID = 23428999234972342L;

    public joinLevelOfLocalAndAssignmentLevelException(String message) {
        super(message);
        printStackTrace();
    }
}

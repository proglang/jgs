package utils.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 16.02.17.
 */
public class joinLevelOfLocalAndAssignmentLevelException extends RuntimeException {
    private static final long serialVersionUID = 23428999234972342L;

    public joinLevelOfLocalAndAssignmentLevelException(String message) {
        super(message);
        printStackTrace();
    }
}

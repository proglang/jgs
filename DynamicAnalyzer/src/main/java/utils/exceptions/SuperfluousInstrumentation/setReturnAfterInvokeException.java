package utils.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 21.02.17.
 */
public class setReturnAfterInvokeException extends RuntimeException {
    private static final long serialVersionUID = 21209801980212L;

    // should take another argument STRING involved_var. benenne variable bei der es abbrechen soll. 1var reicht.
    public setReturnAfterInvokeException(String message) {
        super(message);
        printStackTrace();
    }
}

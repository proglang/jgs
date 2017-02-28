package utils.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 27.02.17.
 */
public class setReturnLevelAfterInvokeStmtException extends RuntimeException{
    public setReturnLevelAfterInvokeStmtException(String message) {
        super(message);
        printStackTrace();
    }
}

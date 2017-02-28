package utils.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 27.02.17.
 */
public class ReturnConstantException extends RuntimeException{
    public ReturnConstantException(String message) {
        super(message);
        printStackTrace();
    }
}

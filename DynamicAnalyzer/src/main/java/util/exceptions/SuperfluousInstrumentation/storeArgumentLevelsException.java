package util.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 27.02.17.
 */
public class storeArgumentLevelsException extends RuntimeException {
    public storeArgumentLevelsException(String message) {
        super(message);
        printStackTrace();
    }
}

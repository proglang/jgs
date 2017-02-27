package utils.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 27.02.17.
 */
public class storeArgumentLevels extends RuntimeException {
    public storeArgumentLevels(String message) {
        super(message);
        printStackTrace();
    }
}

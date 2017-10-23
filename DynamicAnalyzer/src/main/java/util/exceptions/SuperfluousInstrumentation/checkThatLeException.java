package util.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 27.02.17.
 */
public class checkThatLeException extends RuntimeException {
    private static final long serialVersionUID = 23428935435434L;

    public checkThatLeException(String message) {
        super(message);
        printStackTrace();
    }
}

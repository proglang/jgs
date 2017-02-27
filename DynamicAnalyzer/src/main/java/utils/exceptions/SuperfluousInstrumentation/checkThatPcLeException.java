package utils.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 27.02.17.
 */
public class checkThatPcLeException extends RuntimeException{

    private static final long serialVersionUID = 210101015435434L;

    public checkThatPcLeException(String message) {
        super(message);
        printStackTrace();
    }
}

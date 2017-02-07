package utils.exceptions;

/**
 * Created by Nicolas Müller on 06.02.17.
 * Exception to be thrown on unnecessary instrumentation.
 */
public class NSUCheckCalledException extends RuntimeException{

    private static final long serialVersionUID = 13420394341038413L;

    public NSUCheckCalledException(String message) {
        super(message);
        printStackTrace();
    }
}

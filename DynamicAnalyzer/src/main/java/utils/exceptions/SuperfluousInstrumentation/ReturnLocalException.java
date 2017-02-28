package utils.exceptions.SuperfluousInstrumentation;

/**
 * Created by Nicolas Müller on 27.02.17.
 */
public class ReturnLocalException extends RuntimeException{
    public ReturnLocalException(String message){
        super(message);
        printStackTrace();
    }
}

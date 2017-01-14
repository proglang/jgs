package utils.exceptions;

/**
 * When Programm is called with illegal arguments
 * @author Nicolas Müller
 *
 */
public class IllegalArgumentsException extends RuntimeException {
	public IllegalArgumentsException(String message) {
		super(message);
	}
}

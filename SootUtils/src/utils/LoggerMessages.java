package utils;

/**
 * <h1></h1>
 * 
 * 
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class LoggerMessages {

	/** */
	private static final String ERROR_GENERATING_FILE_HANDLER = "Couldn't start the logging via file.";
	/** */
	private static final String ERROR_CLOSING_FILE_HANDLER = "Couldn't finish the logging via file.";
	/** */
	private static final String NO_SERIALIZATION = "Couldn't create the serialized file.";
	/** */
	private static final String NO_DESERIALIZATION = "Error during deserialization of the serialized message store file";

	/**
	 * 
	 * @return
	 */
	public static String deserializationNotPossible() {
		return String.format(NO_DESERIALIZATION);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String creationSerializedNotPossible() {
		return String.format(NO_SERIALIZATION);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String closeFileHandlerWrong() {
		return String.format(ERROR_CLOSING_FILE_HANDLER);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String creationOfhandlerImpossible() {
		return String.format(ERROR_GENERATING_FILE_HANDLER);
	} 

}

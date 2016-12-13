package utils.analyzer;

/**
 * Class to set the security-level of locals, fields and array-fields.
 * Note that the return value must be assigned to the original value,
 * otherwise the security-level won't be assigned to the original field.
 * 
 * This method itself does not do anything, but its existence allows the jimple
 * injector to inject appropriate code.
 * @author koenigr
 */
public class HelperClass {

	public static <T> T makeHigh(T value) {
		return value;
	}
	
	public static <T> T makeMedium(T value) {
		return value;
	}
	
	public static <T> T makeLow(T value) {
		return value;
	}
	
}

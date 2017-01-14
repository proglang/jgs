package utils.printer;

public class SecurePrinter {
	public static <T> void printMedium (T out) {
		System.err.println(out);		// So that System.out.println wont trigger
	}
}

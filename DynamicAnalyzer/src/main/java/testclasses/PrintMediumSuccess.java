package testclasses;

import utils.analyzer.HelperClass;
import utils.printer.SecurePrinter;

public class PrintMediumSuccess {
	public static void main(String[] args) {
		String med = "This is medium information";
		med = HelperClass.makeMedium(med);
		SecurePrinter.printMedium(med);
		
		String low = "This is low information";
		low = HelperClass.makeLow(low);
		SecurePrinter.printMedium(low);
	}
}

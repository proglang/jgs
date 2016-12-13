package main.testclasses;

import utils.analyzer.HelperClass;
import utils.printer.SecurePrinter;

public class PrintMediumFail2 {
	public static void main(String[] args) {
		String hi = "This is high information";
		hi = HelperClass.makeHigh(hi);
		SecurePrinter.printMedium(hi);
	}
}

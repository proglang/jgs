package main.testclasses;

import utils.analyzer.HelperClass;
import utils.printer.SecurePrinter;

public class PrintMediumFail {
	public static void main(String[] args) {
		String med = "This is medium information";
		med = HelperClass.makeMedium(med);
		System.out.println(med);
	}
}
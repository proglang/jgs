package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;
import util.printer.SecurePrinter;

public class PrintMediumSuccess {
	public static void main(String[] args) {
		String med = "This is medium information";
		med = DynamicLabel.makeMedium(med);
		SecurePrinter.printMedium(med);
		
		String low = "This is low information";
		low = DynamicLabel.makeLow(low);
		SecurePrinter.printMedium(low);
	}
}

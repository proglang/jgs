package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;
import util.printer.SecurePrinter;

public class PrintMediumFail2 {
	public static void main(String[] args) {
		String hi = "This is high information";
		hi = DynamicLabel.makeHigh(hi);
		SecurePrinter.printMedium(hi);
	}
}

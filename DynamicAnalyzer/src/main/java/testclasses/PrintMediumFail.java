package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class PrintMediumFail {
	public static void main(String[] args) {
		String med = "This is medium information";
		med = DynamicLabel.makeMedium(med);
		System.out.println(med);
	}
}
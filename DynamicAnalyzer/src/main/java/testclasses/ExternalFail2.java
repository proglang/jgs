package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;
import de.unifreiburg.cs.proglang.jgs.support.IOUtils;

public class ExternalFail2 {
	public static void main(String[] args) {

		int x = retH();
		System.out.println(x);
		IOUtils.printSecret(String.valueOf(x));

	}

	public static int retH() {
		return DynamicLabel.makeHigh(4);
	}
}

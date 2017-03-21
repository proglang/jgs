package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class ExternalFail2 {
	public static void main(String[] args) {

		int x = retH();
		System.out.println(x);

	}

	public static int retH() {
		return DynamicLabel.makeHigh(4);
	}
}

package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class MakeHigh {
	
	public static String field = "initfield";

	/**
	 * Test if setting all kinds of variable types works properly.
	 * @param args The arguments are ignored in this method.
	 */
	public static void main(String[] args) {
		String local = "initlocal";
		String[] arr = {"a", "b"};
		String[][] mularr = { {"aa"},{"bb"}};
		
		local = DynamicLabel.makeHigh(local);
		local = DynamicLabel.makeHigh("bg");
		field = DynamicLabel.makeHigh(field);
		arr[0] = DynamicLabel.makeHigh(arr[1]);
		mularr[1] = DynamicLabel.makeHigh(mularr[1]);
	}

}

package main.testclasses;

import utils.HelperClass.HelperClass;

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
		
		local = HelperClass.makeHigh(local);
		local = HelperClass.makeHigh("bg");
		field = HelperClass.makeHigh(field);
		arr[0] = HelperClass.makeHigh(arr[1]);
		mularr[1] = HelperClass.makeHigh(mularr[1]);
	}

}

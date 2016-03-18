package main.testclasses;

import utils.analyzer.HelperClass;

public class MakeHigh {
	
//	public static String field = "";

	/**
	 * Test if setting all kinds of variable types works properly.
	 * @param args The arguments are ignored in this method.
	 */
	public static void main(String[] args) {
		String local = "initlocal";
//		String[] arr = {"a", "b"};
		
		local = HelperClass.makeHigh(local);
		local = "bg";
//		field = HelperClass.makeHigh(field);
//		arr[0] = HelperClass.makeHigh(arr[1]);

	}

}

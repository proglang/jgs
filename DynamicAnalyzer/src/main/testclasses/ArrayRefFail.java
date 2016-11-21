package main.testclasses;

import utils.analyzer.HelperClass;

/**
 * Information leaked by java.lang.String_$r7 in array pub.
 * @author Nicolas Müller
 */
public class ArrayRefFail {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String secret = read()[1];
		String[] pub = {"x", "y", secret};
		System.out.println(pub[2].toString());
	}
	
	/**
	 * 
	 */
	public static String[] read() {
		String secret = "42";
		secret = HelperClass.makeHigh(secret);
		String[] arr = {"41", secret, "43"};
		return arr;
	}
	
}

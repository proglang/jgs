package testclasses;


import util.analyzer.HelperClass;

/**
 * Array access test which does not violate information flow policy.
 * @author Nicolas Müller
 */
public class ArrayRefSuccess {

	public static void main(String[] args) {
		String secret = read()[1];
		String[] pub = {"x", "y", HelperClass.makeLow(secret)};
		System.out.println(pub[2].toString());
	}

	public static String[] read() {
		String secret = "42";
		secret = HelperClass.makeHigh(secret);
		String[] arr = {"41", secret, "43"};
		return arr;
	}
	
}
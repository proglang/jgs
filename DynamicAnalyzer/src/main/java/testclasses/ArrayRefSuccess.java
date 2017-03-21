package testclasses;


import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Array access test which does not violate information flow policy.
 * @author Nicolas Müller
 */
public class ArrayRefSuccess {

	public static void main(String[] args) {
		String secret = read()[1];
		String[] pub = {"x", "y", DynamicLabel.makeLow(secret)};
		System.out.println(pub[2]);
	}

	public static String[] read() {
		String secret = "42";
		secret = DynamicLabel.makeHigh(secret);
		return new String[]{"41", secret, "43"};
	}
	
}
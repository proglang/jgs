package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class MulArrayFail {
   
	public static void main(String[] args) {
		method(2);
	}
    
	static void method(int x) {
		String[][] twoD = new String[][] {{"e"},{"f"},{"g"}};
		System.out.println("Old val: " + twoD[1][0]);
		twoD[1][0] = DynamicLabel.makeHigh("secretVal");
		String val = twoD[1][0];
		System.out.println("Value of MulArray: " + val);
	}
}

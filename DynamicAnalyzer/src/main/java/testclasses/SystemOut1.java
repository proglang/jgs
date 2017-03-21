package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

// Must obviously fail. See SystemOut2 for comparison and why we need
// this test.
public class SystemOut1 {
	
	public static void main(String[] args) {
		int y = DynamicLabel.makeHigh(4);
		System.out.println(y);
	}
	
}

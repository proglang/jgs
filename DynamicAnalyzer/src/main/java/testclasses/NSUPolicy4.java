package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class NSUPolicy4 {
	public static void main(String[] args) {
		String y = "Case Def";
		int x = DynamicLabel.makeHigh(1);
		if (x == 1) {
			y = "Case 1"; 
		}

		System.out.println(y);  
	}
}

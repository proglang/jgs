package main.testclasses;

import utils.analyzer.HelperClass;

/**
 * Weird behavoir of compiler:
 * If Else clause is removed, the y in line 15 and the y in line 21 are not
 * different variables after compilation, which is why no illegal flow is
 * detected.
 * @author Nicolas Müller
 *
 */
public class NSUPolicy4 {
	public static void main(String[] args) {
		String y = "";
		int x = HelperClass.makeHigh(1);
		if (x == 1) {
			y = "Case 1"; 
		} else {
			y = "Case Def";		
		}

		System.out.println(y);  
	}
}

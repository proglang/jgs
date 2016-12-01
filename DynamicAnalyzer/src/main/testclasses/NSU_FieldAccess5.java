package main.testclasses;

import utils.analyzer.HelperClass;
import utils.test.C;

public class NSU_FieldAccess5 {
	public static void main(String[] args) {
		int secret = HelperClass.makeHigh(2);
		C c = new C();
		c.f = false;
		if (secret == 2) {
			c.f = true;		// should yield NSU exception
		}
	}
}

package testclasses;

import testclasses.utils.C;
import utils.analyzer.HelperClass;

public class NSU_FieldAccess4 {
	//static int f = 0;
	public static void main(String[] args) {

		C b = new C();
		C c = HelperClass.makeHigh(b);
		if (HelperClass.makeHigh(42) == 42) {
			update(c); // should throw an error, since globalPC is HIGH and c.f is LOW		
		}
	}
	
	private static void update(C c) {
		c.f = true;
	}
}

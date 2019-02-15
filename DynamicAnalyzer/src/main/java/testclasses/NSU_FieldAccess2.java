package testclasses;

import testclasses.utils.C;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class NSU_FieldAccess2 {
	//static int f = 0;
	public static void main(String[] args) {

		C b = new C();
		C c = b;
		c.f = DynamicLabel.makeHigh(c.f);
		if (DynamicLabel.makeHigh(42) == 42) {
			c.f = true; // should be ok, c.f is HIGH		
		}
	}
}

/* c.f with makeHigh gives error */
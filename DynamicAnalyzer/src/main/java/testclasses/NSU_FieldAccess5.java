package testclasses;

import testclasses.utils.C;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class NSU_FieldAccess5 {
	public static void main(String[] args) {
		int secret = DynamicLabel.makeHigh(2);
		C c = new C();
		c.f = false;
		if (secret == 2) {
			c.f = true;		// should yield NSU exception
		}
	}
}

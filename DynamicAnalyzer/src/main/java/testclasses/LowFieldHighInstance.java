package testclasses;

import testclasses.utils.C;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class LowFieldHighInstance {
	public static void main(String[] args) {
		C highC = new C();
		highC = DynamicLabel.makeHigh(highC);
		// C.f must also be high
		boolean cf = highC.f;
		System.out.println(cf);
		
	}
}

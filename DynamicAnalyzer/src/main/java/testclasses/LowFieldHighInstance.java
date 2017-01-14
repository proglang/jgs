package testclasses;

import testclasses.utils.C;
import utils.analyzer.HelperClass;

public class LowFieldHighInstance {
	public static void main(String[] args) {
		C highC = new C();
		highC = HelperClass.makeHigh(highC);
		// C.f must also be high
		boolean cf = highC.f;
		System.out.println(cf);
		
	}
}

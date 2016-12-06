package main.testclasses;

import utils.analyzer.HelperClass;
import utils.test.C;

public class LowFieldHighInstance {
	public static void main(String[] args) {
		C highC = new C();
		highC = HelperClass.makeHigh(highC);
		// C.f must also be high
		boolean cf = highC.f;
		System.out.println(cf);
		
	}
}

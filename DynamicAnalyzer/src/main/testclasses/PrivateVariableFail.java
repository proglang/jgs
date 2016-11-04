package main.testclasses;

import utils.analyzer.HelperClass;

// Must obviously fail. Does not fail with initialization check.
public class PrivateVariableFail {
	
	public static void main(String[] args) {
		int y = HelperClass.makeHigh(4);
		System.out.println(y);
	}
	
}

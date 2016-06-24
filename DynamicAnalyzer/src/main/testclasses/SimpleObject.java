package main.testclasses;

import utils.analyzer.HelperClass;

public class SimpleObject {
	
	public String field = "This is a field!";
	
	public void methodWithoutReturn() {
		field = "The fields value changed";
	}
	
	public String methodWithReturn() {
		return field + " and some additional text";
	}
	
	public static int add(int x, int y) {
		return x + y;
	}
	
	public static int returnHigh(int x) {
		return HelperClass.makeHigh(x);
	}
}

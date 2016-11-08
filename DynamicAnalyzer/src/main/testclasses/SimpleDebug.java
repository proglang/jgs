package main.testclasses;

import utils.analyzer.HelperClass;


public class SimpleDebug {
	public static void main(String[] args) {
		returnsHigh(3);
	}
	
	public static int returnsHigh(int i) {
		i = HelperClass.makeHigh(i);
		return i;
	}
	
	
}

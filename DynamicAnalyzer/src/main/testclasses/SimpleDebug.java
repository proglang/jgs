package main.testclasses;

import utils.analyzer.HelperClass;


public class SimpleDebug {
	public static void main(String[] args) {
		
		int x = 3 + 7;
		x = HelperClass.makeHigh(x);
		System.out.println(x);
		
	}
}

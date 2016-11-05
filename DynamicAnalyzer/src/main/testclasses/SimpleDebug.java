package main.testclasses;

import utils.analyzer.HelperClass;


public class SimpleDebug {
	public static void main(String[] args) {
		int secret = HelperClass.makeHigh(42);
		int y;
		switch (secret){
		case 42:
			y = secret;
			break;
		default:
			secret = 42;
			y = 0;
		}
		System.out.println(y);
	}
}

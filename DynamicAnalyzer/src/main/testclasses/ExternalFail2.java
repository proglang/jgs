package main.testclasses;

import utils.analyzer.HelperClass;

public class ExternalFail2 {
	public static void main(String[] args) {

		int x = retH();
		System.out.println(x);

	}

	public static int retH() {
		return HelperClass.makeHigh(4);
	}
}

package main.testclasses;

import utils.analyzer.HelperClass;

public class PrintMediumFail {
	public static void main(String[] args) {
		String med = "This is medium information";
		med = HelperClass.makeMedium(med);
		System.out.println(med);
	}
}
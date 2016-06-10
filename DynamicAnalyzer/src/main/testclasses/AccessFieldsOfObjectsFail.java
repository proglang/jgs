package main.testclasses;

import utils.analyzer.HelperClass;

public class AccessFieldsOfObjectsFail {

	public static void main(String[] args) {
		SimpleObject oneObject = new SimpleObject();
		oneObject.field = HelperClass.makeHigh("New field value");
		System.out.println(oneObject.field);
	}

}

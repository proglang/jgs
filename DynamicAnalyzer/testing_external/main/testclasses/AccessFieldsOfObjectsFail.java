package testclasses;

import utils.analyzer.HelperClass;
import utils.test.SimpleObject;

/**
 * Simple test that should fail, since it leaks a high Field
 */
public class AccessFieldsOfObjectsFail {

	public static void main(String[] args) {
		SimpleObject oneObject = new SimpleObject();
		oneObject.field = HelperClass.makeHigh("New field value");
		System.out.println(oneObject.field);
	}

}

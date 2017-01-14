package testclasses;

import testclasses.utils.SimpleObject;
import utils.analyzer.HelperClass;

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

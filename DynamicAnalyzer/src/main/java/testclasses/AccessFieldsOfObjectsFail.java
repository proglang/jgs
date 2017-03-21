package testclasses;

import testclasses.utils.SimpleObject;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Simple test that should fail, since it leaks a high Field
 */
public class AccessFieldsOfObjectsFail {

	public static void main(String[] args) {
		SimpleObject oneObject = new SimpleObject();
		oneObject.field = DynamicLabel.makeHigh("New field value");
		System.out.println(oneObject.field);
	}

}

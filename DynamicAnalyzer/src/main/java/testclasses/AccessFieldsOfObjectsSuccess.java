package testclasses;

import testclasses.utils.SimpleObject;

public class AccessFieldsOfObjectsSuccess {

	public static void main(String[] args) {
		SimpleObject oneObject = new SimpleObject();
		oneObject.field = "New field value";
		String local = oneObject.field;
		System.out.println(local);
	}

}

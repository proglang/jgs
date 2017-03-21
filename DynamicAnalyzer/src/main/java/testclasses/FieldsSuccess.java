package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class FieldsSuccess {
	
	static String field = "testfield";

	public static void main(String[] args) {
		readField();
		writeField("Test");
	}
	
	public static void writeField(String arg) {
		field = arg;
	}
	
	public static void readField() {
		field = DynamicLabel.makeHigh(field);
		// System.out.println("Value of Field:" + local);
	}

}

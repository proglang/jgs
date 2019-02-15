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
		System.out.println(field);
	}
	
	public static void readField() {
	    field = "hi";
		field = DynamicLabel.makeHigh(field);
		// System.out.println("Value of Field:" + local);
	}

}

/* working code with method constraints and effects - but fails that field cannot be passed to Sysout.

    static String field = "testfield";

	//@Constraints("LOW <= @0")
	@Effects({"LOW", "?", "pub"})
	public static void main(String[] args) {
		readField();
		writeField("Test");
	}

	@Constraints({"@0 <= LOW", "@0 ~ pub", "pub <= LOW", "@0 <= pub"})
	@Effects({"pub"})
	public static void writeField(String arg) {
		field = arg;
		System.out.println(field);
	}

	@Effects({"pub"})
	public static void readField() {
		//field = "hi"; -- works
		field = DynamicLabel.makeHigh(field); -- doesn't work
		// System.out.println("Value of Field:" + local);
	}


 */
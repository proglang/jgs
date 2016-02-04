package main.testclasses;

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
		String local = field;
		System.out.println("Value of Field:" + local);
	}

}

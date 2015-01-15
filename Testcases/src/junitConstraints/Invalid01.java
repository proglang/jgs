package junitConstraints;

import static security.Definition.*;

public class Invalid01 {

	public static void main(String[] args) {}

	// constraints contain a return reference
	@Constraints({ "@return <= low" })
	public Invalid01(int arg) {}

}

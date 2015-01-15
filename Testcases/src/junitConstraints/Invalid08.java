package junitConstraints;

import static security.Definition.*;

// constraints of class contains a invalid return reference
@Constraints({ "@return <= low" })
public class Invalid08 {

	public static void main(String[] args) {}

}

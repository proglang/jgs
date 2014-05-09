package junitConstraints;

import static security.Definition.*;

// constraints of class contains a invalid security level
@Constraints({ "@pc <= confidential" })
public class Invalid07 {

	public static void main(String[] args) {}

}

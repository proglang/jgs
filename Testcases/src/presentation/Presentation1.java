package presentation;

import security.SootSecurityLevel;
import security.Annotations.*;

@WriteEffect({})
public class Presentation1 {

	@FieldSecurity("high")
	public static String highField;

	@ParameterSecurity({})
	@ReturnSecurity("void")
	@WriteEffect({"low"})
	public static void main() {
		String low = SootSecurityLevel.lowId("42");
		if ("" == highField) {
			print(low);
		}
	}

	@ParameterSecurity({ "high" })
	@ReturnSecurity("void")
	@WriteEffect({ "high" })
	public static void store(String string) {
		highField = string;
	}

	@ParameterSecurity({ "low" })
	@ReturnSecurity("void")
	@WriteEffect({"low"})
	public static void print(String string) {
		System.out.println(string);
	}
	
	@ParameterSecurity({})
	@ReturnSecurity("void")
	@WriteEffect({"low"})
	public static void doNothing() {
		new String("Do something");
	}

}

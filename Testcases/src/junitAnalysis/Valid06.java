package junitAnalysis;

import security.Definition.*;

public class Valid06 {
	
	@FieldSecurity("high")
	public int instanceField;
	
	@FieldSecurity("high")
	public static int staticField;
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

}

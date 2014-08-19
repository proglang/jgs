package stubs;

import security.Definition.FieldSecurity;

public class MinimalFields {

	@FieldSecurity("low")
	public int lowIField;

	@FieldSecurity("low")
	public static int lowSField;

	@FieldSecurity("high")
	public int highIField;

	@FieldSecurity("high")
	public static int highSField;

}

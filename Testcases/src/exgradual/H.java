package exgradual;

import static security.Definition.*;

public class H {

	@FieldSecurity("low")
	public static int low = 10;

	@FieldSecurity("high")
	public static int high = 1;
	
}
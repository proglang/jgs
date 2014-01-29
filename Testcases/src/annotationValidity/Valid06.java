package annotationValidity;

import security.Annotations.FieldSecurity;

public class Valid06 {
	
	@FieldSecurity("high")
	public int instanceField;
	
	@FieldSecurity("high")
	public static int staticField;

}

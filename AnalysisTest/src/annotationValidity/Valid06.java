package annotationValidity;

import security.Annotations.FieldSecurity;
import security.Annotations.ParameterSecurity;

public class Valid06 {
	
	@FieldSecurity("high")
	public int instanceField;
	
	@FieldSecurity("high")
	public static int staticField;
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

}

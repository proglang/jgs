package annotationValidity;

import security.Annotations.FieldSecurity;
import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;
import security.Annotations.WriteEffect;

@WriteEffect({"high"})
public class Valid07 {
	
	@FieldSecurity("high")
	public int instanceField = 42;
	
	@FieldSecurity("high")
	public static int staticField = 42;
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@Override
	@ParameterSecurity("low")
	@ReturnSecurity("high")
	public boolean equals(Object obj) {
		return true;
	}
	
	@WriteEffect({"high"})
	public Valid07() {}

}

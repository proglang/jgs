package annotationValidity;

import security.Annotations.FieldSecurity;
import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;

public class Valid07 {
	
	@FieldSecurity("high")
	public int instanceField = 42;
	
	@FieldSecurity("high")
	public static int staticField = 42;
	
	@Override
	@ParameterSecurity("low")
	@ReturnSecurity("high")
	public boolean equals(Object obj) {
		return true;
	}

}

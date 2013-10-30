import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;
import security.Annotations.WriteEffect;

@WriteEffect({"low"})
public class B extends A {

	@WriteEffect({})
	@ParameterSecurity({})
	@ReturnSecurity("void")
	@Override
	public void ABC3() {}

	@WriteEffect({})
	@ParameterSecurity({})
	@ReturnSecurity("void")
	@Override
	public void ABC1() {}

	@WriteEffect({"low"})
	@ParameterSecurity({})
	@ReturnSecurity("void")
	public void BC3() {}
	
	@WriteEffect({})
	@ParameterSecurity({"high"})
	@ReturnSecurity("low")
	@Override
	public boolean equals(Object obj) {
		return true;
	}

	@WriteEffect({"high"})
	@ParameterSecurity({})
	public B(){}
	
}

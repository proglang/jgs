import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;
import security.Annotations.WriteEffect;

@WriteEffect({})
public class C2 extends B {

	@WriteEffect({})
	@ParameterSecurity({})
	@ReturnSecurity("void")
	@Override
	public void ABC3() {}

	@WriteEffect({})
	@ParameterSecurity({})
	@ReturnSecurity("void")
	@Override
	public void AC3() {}

	@WriteEffect({})
	@ParameterSecurity({})
	@ReturnSecurity("void")
	@Override
	public void BC3() {}
	
	@WriteEffect({"low"})
	@ParameterSecurity({})
	public C2(){}
}

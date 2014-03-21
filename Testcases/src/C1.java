import security.Definition.ParameterSecurity;
import security.Definition.ReturnSecurity;
import security.Definition.WriteEffect;

@WriteEffect({})
public class C1 extends B {

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
	public C1(){}
	
}

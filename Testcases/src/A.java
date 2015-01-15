import static security.Definition.*;

@WriteEffect({"high"})
public class A {
	
	@WriteEffect({})
	@ParameterSecurity({})
	@ReturnSecurity("void")
	public void ABC3() {}

	@WriteEffect({})
	@ParameterSecurity({})
	@ReturnSecurity("void")
	public void ABC1() {}

	@WriteEffect({})
	@ParameterSecurity({})
	@ReturnSecurity("void")
	public void AC3() {}
	
	@WriteEffect({})
	@ParameterSecurity({"high"})
	@ReturnSecurity("low")
	@Override
	public boolean equals(Object obj) {
		return true;
	}
	
	@WriteEffect({})
	@ParameterSecurity({})
	public A(){}

}

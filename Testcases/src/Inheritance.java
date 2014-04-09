import static security.Definition.*;

@WriteEffect({})
public class Inheritance {
	
	@WriteEffect({"low", "high"})
	@ParameterSecurity({"low"})
	@ReturnSecurity("void")
	public static void main(String[] args) {
		new A();
		new B();
	}
	
}

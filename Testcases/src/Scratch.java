
// Quick throw-away examples and testcases for the security type checker
import static security.Definition.*;

public class Scratch {
    
	@FieldSecurity("high")
	int f1;
	@FieldSecurity("low")
	int f2;
	@FieldSecurity("high")
	static int f1s;
	@FieldSecurity("low")
	static int f2s;

	//@Constraints({"high <= @return"})
    public void assignSame() {
    	int a1 = 1;
    	int a2 = 2;
    	Scratch.f1s = Scratch.m2(a1, a2);
    }
    
    public static void main(String[] args) {
			
		}
    
    public static int m2(int a1, int a2) {
    	return a1+ a2;
    }
}

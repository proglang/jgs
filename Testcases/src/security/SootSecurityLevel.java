package security;

public class SootSecurityLevel extends SecurityLevel {

	@Override
	public String[] getOrderedSecurityLevels() {
		return new String[] { "high", "low"};
	}

	public static void main(String[] args) {
		//SecurityLevelImplChecker c = new SecurityLevelImplChecker(new SootSecurityLevel());
	}
	
	@Annotations.ReturnSecurity("high")
	public static <T> T highId(T object) {
		return object;
	}
	
	@Annotations.ReturnSecurity("low")
	public static <T> T lowId(T object) {
		return object;
	}
}

import static security.Definition.*;

@SuppressWarnings("unused")
public class TaintTrackingGeneralTest {
	
	@ReturnSecurity("void")
	@ParameterSecurity({ })
	public void dependsOnIf() {
		boolean a = true;
		boolean b = false;
		if (a && b) {
			System.out.println("Depends on 'if (a)' -> if-branch.");
		} else {
			System.out.print("Depends on 'if (a)' -> else-branch.");
		}
		System.out.println("Depends on nothing.");		
	}
	
	@FieldSecurity("high")
	public static int statField = 42;
	
	@FieldSecurity("low")
	public int field = 23;
	
	public static class TaintTrackingTest2 {
		
		@FieldSecurity("low")
		public static int statField = 21;
		
		@ReturnSecurity("low")
		@ParameterSecurity({ "low" })
		public int returnInteger(int i) {
			boolean res = false;
			return res ? i : 0;
		}
		
	}

	// type: void -> String^H
	@ReturnSecurity("void")
	@ParameterSecurity({ "low" })
	public static void assignStatField(int i) {
		if (statField == 1) statField = 2; 
		statField = i;
	}
	
	@ReturnSecurity("void")
	@ParameterSecurity({})
	public void ifTest() {
		boolean cond1 = mkHigh(false);
		boolean cond2 = mkLow(true);
		if (cond1 || cond2) {
			int i = 1;
		}
	}
	
	@ReturnSecurity("void")
	@ParameterSecurity({})
	public void ifIfTest() {
		boolean cond1 = false;
		boolean cond2 = true;
		if (cond1) {
			int i = 1;
			if (cond2)
				i = 2;
		}
	}
	
	@ReturnSecurity("void")
	@ParameterSecurity({})
	public void ifElseTest() {
		boolean cond = false;
		if (cond) {
			int i = 1;
		} else {
			int j = 1;
		}
	}
	
	@ReturnSecurity("void")
	@ParameterSecurity({})
	public void ifElseIfElseTest() {
		boolean cond1 = false;
		boolean cond2 = true;
		if (cond1) {
			int i = 1;
			if (cond2) {
				i = 2;
			} else {
				int j = 2;
			}
		} else {
			int j = 1;
		}
	}
	
	@ReturnSecurity("void")
	@ParameterSecurity({})
	public void forStmt() {
		for (int i = 0; i < 10; i++) {
			int j = i;
		}
	}
	
	// type: void -> String^H
	@ReturnSecurity("void")
	@ParameterSecurity({"low"})
	public void assignField(int i) {
		field = i;
	}

	// type: int^L -> int^L
	@ReturnSecurity("low")
	@ParameterSecurity({ "low" })
	public static int returnInteger(int i) {
		boolean res = mkHigh(false);
		int j = mkLow(3);
		return res ? i : j;
	}

	// type: void -> String^H
	@ReturnSecurity("void")
	@ParameterSecurity({})
	public void assignField2() {
		TaintTrackingTest2 a = mkHigh(new TaintTrackingTest2());
		statField = a.returnInteger(2);
	}

	// type: void -> String^H
	@ReturnSecurity("high")
	@ParameterSecurity({})
	public static String secretSource() {
		String result = mkHigh("Secret!");
		return result;
	}

	// type: void -> String^L
	@ReturnSecurity("low")
	@ParameterSecurity({})
	public static String publicSource() {
		return "Blablabla!";
	}

	// type: String^H -> void
	@ReturnSecurity("void")
	@ParameterSecurity({"high"})
	public static void confidentialSink(String s) {
		//highId(true);
		System.out.println("XXXX");
	}
	
	// type: String^L -> void
	@ReturnSecurity("void")
	@ParameterSecurity({"low"})
	public static void publicSink(String s) {
		System.out.println(s);
	}
	
	// type: (String^B1, String^B2) -> String^B1+B2
	@ReturnSecurity("high")
	@ParameterSecurity({"high", "low"})
	public static String strAppend(String s1, String s2) {
		return s1 + s2;
	}

	/**
	 * @param args
	 */
	
	@ReturnSecurity("void")
	@ParameterSecurity({"low"})
	public static void main(String[] args) {
		
		// type: String^H
		String h = secretSource();
		
		// type: String^L
		String l = publicSource();
		
		int i = returnInteger(42);
		
		publicSink(l); // ok
		confidentialSink(h); // ok
		
		publicSink(h); // error
		confidentialSink(l); // ok
		
		publicSink(strAppend(h, l)); // error
	}

}

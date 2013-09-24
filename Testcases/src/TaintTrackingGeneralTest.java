
import security.Annotations;
import security.SootSecurityLevel;



public class TaintTrackingGeneralTest {
	
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({ })
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
	
	@Annotations.FieldSecurity("high")
	public static int statField = 42;
	
	@Annotations.FieldSecurity("low")
	public int field = 23;
	
	public static class TaintTrackingTest2 {
		
		@Annotations.FieldSecurity("low")
		public static int statField = 21;
		
		@Annotations.ReturnSecurity("low")
		@Annotations.ParameterSecurity({ "low" })
		public int returnInteger(int i) {
			boolean res = false;
			return res ? i : 0;
		}
		
	}

	// type: void -> String^H
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({ "low" })
	public static void assignStatField(int i) {
		if (statField == 1) statField = 2; 
		statField = i;
	}
	
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({})
	public void ifTest() {
		boolean cond1 = SootSecurityLevel.highId(false);
		boolean cond2 = SootSecurityLevel.lowId(true);
		if (cond1 || cond2) {
			int i = 1;
		}
	}
	
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({})
	public void ifIfTest() {
		boolean cond1 = false;
		boolean cond2 = true;
		if (cond1) {
			int i = 1;
			if (cond2)
				i = 2;
		}
	}
	
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({})
	public void ifElseTest() {
		boolean cond = false;
		if (cond) {
			int i = 1;
		} else {
			int j = 1;
		}
	}
	
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({})
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
	
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({})
	public void forStmt() {
		for (int i = 0; i < 10; i++) {
			int j = i;
		}
	}
	
	// type: void -> String^H
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({"low"})
	public void assignField(int i) {
		field = i;
	}

	// type: int^L -> int^L
	@Annotations.ReturnSecurity("low")
	@Annotations.ParameterSecurity({ "low" })
	public static int returnInteger(int i) {
		boolean res = SootSecurityLevel.highId(false);
		int j = SootSecurityLevel.lowId(3);
		return res ? i : j;
	}

	// type: void -> String^H
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({})
	public void assignField2() {
		TaintTrackingTest2 a = SootSecurityLevel.highId(new TaintTrackingTest2());
		statField = a.returnInteger(2);
	}

	// type: void -> String^H
	@Annotations.ReturnSecurity("high")
	@Annotations.ParameterSecurity({})
	public static String secretSource() {
		String result = SootSecurityLevel.highId("Secret!");
		return result;
	}

	// type: void -> String^L
	@Annotations.ReturnSecurity("low")
	@Annotations.ParameterSecurity({})
	public static String publicSource() {
		return "Blablabla!";
	}

	// type: String^H -> void
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({"high"})
	public static void confidentialSink(String s) {
		//SootSecurityLevel.highId(true);
		System.out.println("XXXX");
	}
	
	// type: String^L -> void
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({"low"})
	public static void publicSink(String s) {
		System.out.println(s);
	}
	
	// type: (String^B1, String^B2) -> String^B1+B2
	@Annotations.ReturnSecurity("high")
	@Annotations.ParameterSecurity({"high", "low"})
	public static String strAppend(String s1, String s2) {
		return s1 + s2;
	}

	/**
	 * @param args
	 */
	
	@Annotations.ReturnSecurity("void")
	@Annotations.ParameterSecurity({"low"})
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

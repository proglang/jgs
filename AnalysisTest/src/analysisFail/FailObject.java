package analysisFail;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

public class FailObject {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldLow() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		// @security("The returned value has a stronger security level than expected.")
		return fo.low;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldHigh() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.lowId(fo);
		// @security("The returned value has a stronger security level than expected.")
		return fo.high;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldHigh2() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		// @security("The returned value has a stronger security level than expected.")
		return fo.high;
	}
	
	@Annotations.ReturnSecurity("low")
	public int accessStaticFieldHigh() {
		// @security("The returned value has a stronger security level than expected.")
		return StaticFieldObject.high;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldLow() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		fo.low = high;
		return;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldLow2() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		fo.low = high;
		return;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignStaticFieldLow() {
		int high = SootSecurityLevel.highId(42);
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		StaticFieldObject.low = high;
		return;
	}
		
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleLowMethod() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnLowSecurity();
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleHighMethod() {
		MethodObject mo = new MethodObject();
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnHighSecurity();
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleHighMethod2() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnHighSecurity();
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleHighMethod3() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnHighSecurity();
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnLowSecurity(low);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod2() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnHighSecurity(low);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod3() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod4() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod5() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnHighSecurity(low);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod6() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		mo = SootSecurityLevel.highId(mo);
		// @security("The returned value has a stronger security level than expected.")
		return mo.returnLowSecurity(high);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod7() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnLowSecurity(high);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleLowStaticMethod() {
		// @security("The returned value has a stronger security level than expected.")
		return StaticMethodObject.returnHighSecurity();
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeLowStaticMethod() {
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		int high = SootSecurityLevel.highId(42);
		return StaticMethodObject.returnLowSecurity(high);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeLowStaticMethod2() {
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		int high = SootSecurityLevel.highId(42);
		return StaticMethodObject.returnLowSecurity(high);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeHighStaticMethod() {
		int low = SootSecurityLevel.lowId(42);
		// @security("The returned value has a stronger security level than expected.")
		return StaticMethodObject.returnHighSecurity(low);
	}

	public static class FieldObject {
		
		@Annotations.FieldSecurity("low")
		public int low = 42;
		
		@Annotations.FieldSecurity("high")
		public int high = 42;
		
		@Annotations.WriteEffect({"low", "high"})
		public FieldObject() {
			super();
		}
		
	}

	@Annotations.WriteEffect({"low", "high"})
	public static class StaticFieldObject {
		
		@Annotations.FieldSecurity("low")
		public static int low = 42;
		
		@Annotations.FieldSecurity("high")
		public static int high = 42;
		
	}

	public static class MethodObject {
		
		@Annotations.ReturnSecurity("low")
		public int returnLowSecurity() {
			return SootSecurityLevel.lowId(42);
		}
		
		@Annotations.ReturnSecurity("high")
		public int returnHighSecurity() {
			return SootSecurityLevel.highId(42);
		}
		
		@Annotations.ParameterSecurity({"low"})
		@Annotations.ReturnSecurity("low")
		public int returnLowSecurity(int low) {
			return low;
		}
		
		@Annotations.ParameterSecurity({"high"})
		@Annotations.ReturnSecurity("high")
		public int returnHighSecurity(int high) {
			return high;
		}
		
	}

	public static class StaticMethodObject {
		
		@Annotations.ReturnSecurity("low")
		public static int returnLowSecurity() {
			return SootSecurityLevel.lowId(42);
		}
		
		@Annotations.ReturnSecurity("high")
		public static int returnHighSecurity() {
			return SootSecurityLevel.highId(42);
		}
		
		@Annotations.ParameterSecurity({"low"})
		@Annotations.ReturnSecurity("low")
		public static int returnLowSecurity(int low) {
			return low;
		}
		
		@Annotations.ParameterSecurity({"high"})
		@Annotations.ReturnSecurity("high")
		public static int returnHighSecurity(int high) {
			return high;
		}
		
	}
	
}
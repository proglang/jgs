package analysisSuccess;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

public class SuccessObject {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldLow() {
		FieldObject fo = new FieldObject();
		return fo.low;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldLow2() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.lowId(fo);
		return fo.low;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldLow3() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		return fo.low;
	}
	
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldLow4() {
		FieldObject fo = new FieldObject();
		return fo.low;
	}
	
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldHigh() {
		FieldObject fo = new FieldObject();
		return fo.high;
	}
	
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldHigh2() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.lowId(fo);
		return fo.high;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldHigh3() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		return fo.high;
	}
	
	@Annotations.ReturnSecurity("low")
	public int accessStaticFieldLow() {
		return StaticFieldObject.low;
	}
	
	@Annotations.ReturnSecurity("high")
	public int accessStaticFieldLow2() {
		return StaticFieldObject.low;
	}
	
	@Annotations.ReturnSecurity("high")
	public int accessStaticFieldHigh() {
		return StaticFieldObject.high;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldLow() {
		int low = SootSecurityLevel.lowId(42);
		FieldObject fo = new FieldObject();
		fo.low = low;
		return;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldLow2() {
		int low = SootSecurityLevel.lowId(42);
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		fo.low = low;
		return;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldHigh() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo.high = high;
		return;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldHigh2() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		fo.high = high;
		return;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldHigh3() {
		int low = SootSecurityLevel.lowId(42);
		FieldObject fo = new FieldObject();
		fo.high = low;
		return;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldHigh4() {
		int low = SootSecurityLevel.lowId(42);
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		fo.high = low;
		return;
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleLowMethod() {
		MethodObject mo = new MethodObject();
		return mo.returnLowSecurity();
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleLowMethod2() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnLowSecurity();
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleLowMethod3() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnLowSecurity();
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleHighMethod() {
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity();
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleHighMethod2() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnHighSecurity();
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleHighMethod3() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity();
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		return mo.returnLowSecurity(low);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeLowMethod2() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnLowSecurity(low);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeLowMethod3() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnLowSecurity(low);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod2() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity(low);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod3() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod4() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod5() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity(low);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleLowStaticMethod() {
		return StaticMethodObject.returnLowSecurity();
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleLowStaticMethod2() {
		return StaticMethodObject.returnLowSecurity();
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleHighStaticMethod() {
		return StaticMethodObject.returnHighSecurity();
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeLowStaticMethod() {
		int low = SootSecurityLevel.lowId(42);
		return StaticMethodObject.returnLowSecurity(low);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeLowStaticMethod2() {
		int low = SootSecurityLevel.lowId(42);
		return StaticMethodObject.returnLowSecurity(low);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeHighStaticMethod() {
		int high = SootSecurityLevel.highId(42);
		return StaticMethodObject.returnHighSecurity(high);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeHighStaticMethod2() {
		int low = SootSecurityLevel.lowId(42);
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
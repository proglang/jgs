package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

public class TaintTrackingObject {
	
	public static class FieldObject {
		
		@Annotations.FieldSecurity("low")
		public int low = 42;
		
		@Annotations.FieldSecurity("high")
		public int high = 42;
		
	}
	
	public static class StaticFieldObject {
		
		@Annotations.FieldSecurity("low")
		public static int low = 42;
		
		@Annotations.FieldSecurity("high")
		public static int high = 42;
		
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int accessFieldLow() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		return fo.low;
	}
	
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int accessFieldHigh() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.lowId(fo);
		return fo.high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int accessStaticFieldHigh() {
		return StaticFieldObject.high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignFieldLow() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo.low = high;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignFieldLow2() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		fo.low = high;
		return;
	}
	
	public static class MethodObject {
		
		@Annotations.ParameterSecurity({})
		@Annotations.ReturnSecurity("low")
		public int returnLowSecurity() {
			return SootSecurityLevel.lowId(42);
		}
		
		@Annotations.ParameterSecurity({})
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
		
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleLowMethod() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnLowSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleHighMethod() {
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleHighMethod2() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleHighMethod3() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnLowSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod2() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod3() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod4() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeHighMethod5() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity(low);
	}
	
//	public static class StaticMethodObject {
//		
//		@ParameterSecurity({})
//		@ReturnSecurity("low")
//		public static int returnLowSecurity() {
//			return SootSecurityLevel.lowId(42);
//		}
//		
//		@ParameterSecurity({})
//		@ReturnSecurity("high")
//		public static int returnHighSecurity() {
//			return SootSecurityLevel.highId(42);
//		}
//		
//		@ParameterSecurity({"low"})
//		@ReturnSecurity("low")
//		public static int returnLowSecurity(int low) {
//			return low;
//		}
//		
//		@ParameterSecurity({"high"})
//		@ReturnSecurity("high")
//		public static int returnHighSecurity(int high) {
//			return high;
//		}
//	}
//	
//	@ParameterSecurity({})
//	@ReturnSecurity("low")
//	public int invokeSimpleLowStaticMethod() {
//		return StaticMethodObject.returnLowSecurity();
//	}
//	
//	@ParameterSecurity({})
//	@ReturnSecurity("high")
//	public int invokeSimpleLowStaticMethod2() {
//		return StaticMethodObject.returnLowSecurity();
//	}
//	
//	@ParameterSecurity({})
//	@ReturnSecurity("high")
//	public int invokeSimpleHighStaticMethod() {
//		return StaticMethodObject.returnHighSecurity();
//	}
//	
//	@ParameterSecurity({})
//	@ReturnSecurity("low")
//	public int invokeLowStaticMethod() {
//		int low = SootSecurityLevel.lowId(42);
//		return StaticMethodObject.returnLowSecurity(low);
//	}
//	
//	@ParameterSecurity({})
//	@ReturnSecurity("high")
//	public int invokeLowStaticMethod2() {
//		int low = SootSecurityLevel.lowId(42);
//		return StaticMethodObject.returnLowSecurity(low);
//	}
//	
//	@ParameterSecurity({})
//	@ReturnSecurity("high")
//	public int invokeHighStaticMethod() {
//		int high = SootSecurityLevel.highId(42);
//		return StaticMethodObject.returnHighSecurity(high);
//	}
//	
//	@ParameterSecurity({})
//	@ReturnSecurity("high")
//	public int invokeHighStaticMethod2() {
//		int low = SootSecurityLevel.lowId(42);
//		return StaticMethodObject.returnHighSecurity(low);
//	}
}
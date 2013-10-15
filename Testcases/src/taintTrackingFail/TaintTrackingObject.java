package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingObject {

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldLow() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		return fo.low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldHigh() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.lowId(fo);
		return fo.high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low", "high"})
	public int accessFieldHigh2() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		return fo.high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int accessStaticFieldHigh() {
		return StaticFieldObject.high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldLow() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo.low = high;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void assignFieldLow2() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		fo.low = high;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void assignStaticFieldLow() {
		int high = SootSecurityLevel.highId(42);
		StaticFieldObject.low = high;
		return;
	}
		
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeSimpleLowMethod() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnLowSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeSimpleHighMethod() {
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeSimpleHighMethod2() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeSimpleHighMethod3() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnLowSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeHighMethod() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeHighMethod2() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeHighMethod3() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeHighMethod4() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeHighMethod5() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeHighMethod6() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnLowSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeHighMethod7() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnLowSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeSimpleLowStaticMethod() {
		return StaticMethodObject.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeLowStaticMethod() {
		int high = SootSecurityLevel.highId(42);
		return StaticMethodObject.returnLowSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public int invokeLowStaticMethod2() {
		int high = SootSecurityLevel.highId(42);
		return StaticMethodObject.returnLowSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int invokeHighStaticMethod() {
		int low = SootSecurityLevel.lowId(42);
		return StaticMethodObject.returnHighSecurity(low);
	}

	@Annotations.WriteEffect({})
	public static class FieldObject {
		
		@Annotations.FieldSecurity("low")
		public int low = 42;
		
		@Annotations.FieldSecurity("high")
		public int high = 42;
		
		@Annotations.ParameterSecurity({})
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

	@Annotations.WriteEffect({})
	public static class MethodObject {
		
		@Annotations.ParameterSecurity({})
		@Annotations.ReturnSecurity("low")
		@Annotations.WriteEffect({})
		public int returnLowSecurity() {
			return SootSecurityLevel.lowId(42);
		}
		
		@Annotations.ParameterSecurity({})
		@Annotations.ReturnSecurity("high")
		@Annotations.WriteEffect({})
		public int returnHighSecurity() {
			return SootSecurityLevel.highId(42);
		}
		
		@Annotations.ParameterSecurity({"low"})
		@Annotations.ReturnSecurity("low")
		@Annotations.WriteEffect({})
		public int returnLowSecurity(int low) {
			return low;
		}
		
		@Annotations.ParameterSecurity({"high"})
		@Annotations.ReturnSecurity("high")
		@Annotations.WriteEffect({})
		public int returnHighSecurity(int high) {
			return high;
		}
		
	}

	@Annotations.WriteEffect({})
	public static class StaticMethodObject {
		
		@Annotations.ParameterSecurity({})
		@Annotations.ReturnSecurity("low")
		@Annotations.WriteEffect({})
		public static int returnLowSecurity() {
			return SootSecurityLevel.lowId(42);
		}
		
		@Annotations.ParameterSecurity({})
		@Annotations.ReturnSecurity("high")
		@Annotations.WriteEffect({})
		public static int returnHighSecurity() {
			return SootSecurityLevel.highId(42);
		}
		
		@Annotations.ParameterSecurity({"low"})
		@Annotations.ReturnSecurity("low")
		@Annotations.WriteEffect({})
		public static int returnLowSecurity(int low) {
			return low;
		}
		
		@Annotations.ParameterSecurity({"high"})
		@Annotations.ReturnSecurity("high")
		@Annotations.WriteEffect({})
		public static int returnHighSecurity(int high) {
			return high;
		}
		
	}
	
}
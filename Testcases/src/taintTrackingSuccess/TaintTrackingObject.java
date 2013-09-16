package taintTrackingSuccess;

import security.Annotations;
import security.Annotations.FieldSecurity;
import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;
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
		return fo.low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int accessFieldLow2() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.lowId(fo);
		return fo.low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int accessFieldLow3() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		return fo.low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int accessFieldLow4() {
		FieldObject fo = new FieldObject();
		return fo.low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int accessFieldHigh() {
		FieldObject fo = new FieldObject();
		return fo.high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int accessFieldHigh2() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.lowId(fo);
		return fo.high;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int accessFieldHigh3() {
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		return fo.high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int accessStaticFieldLow() {
		return StaticFieldObject.low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int accessStaticFieldLow2() {
		return StaticFieldObject.low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int accessStaticFieldHigh() {
		return StaticFieldObject.high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignFieldLow() {
		int low = SootSecurityLevel.lowId(42);
		FieldObject fo = new FieldObject();
		fo.low = low;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignFieldLow2() {
		int low = SootSecurityLevel.lowId(42);
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		fo.low = low;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignFieldHigh() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo.high = high;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignFieldHigh2() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		fo.high = high;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignFieldHigh3() {
		int low = SootSecurityLevel.lowId(42);
		FieldObject fo = new FieldObject();
		fo.high = low;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignFieldHigh4() {
		int low = SootSecurityLevel.lowId(42);
		FieldObject fo = new FieldObject();
		fo = SootSecurityLevel.highId(fo);
		fo.high = low;
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
		return mo.returnLowSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleLowMethod2() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnLowSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleLowMethod3() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnLowSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleHighMethod() {
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleHighMethod2() {
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
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
		return mo.returnLowSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeLowMethod2() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnLowSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeLowMethod3() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnLowSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod2() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		return mo.returnHighSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod3() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.lowId(mo);
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod4() {
		int high = SootSecurityLevel.highId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeHighMethod5() {
		int low = SootSecurityLevel.lowId(42);
		MethodObject mo = new MethodObject();
		mo = SootSecurityLevel.highId(mo);
		return mo.returnHighSecurity(low);
	}
	
	public static class StaticMethodObject {
		
		@Annotations.ParameterSecurity({})
		@Annotations.ReturnSecurity("low")
		public static int returnLowSecurity() {
			return SootSecurityLevel.lowId(42);
		}
		
		@Annotations.ParameterSecurity({})
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
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleLowStaticMethod() {
		return StaticMethodObject.returnLowSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleLowStaticMethod2() {
		return StaticMethodObject.returnLowSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleHighStaticMethod() {
		return StaticMethodObject.returnHighSecurity();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int invokeLowStaticMethod() {
		int low = SootSecurityLevel.lowId(42);
		return StaticMethodObject.returnLowSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeLowStaticMethod2() {
		int low = SootSecurityLevel.lowId(42);
		return StaticMethodObject.returnLowSecurity(low);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeHighStaticMethod() {
		int high = SootSecurityLevel.highId(42);
		return StaticMethodObject.returnHighSecurity(high);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int invokeHighStaticMethod2() {
		int low = SootSecurityLevel.lowId(42);
		return StaticMethodObject.returnHighSecurity(low);
	}
}
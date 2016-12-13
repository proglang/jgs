package analyzer.level2;

//public enum SecurityLevel {
//	LOW1, HIGH1;
//	
//	public static Object bottom() {
//		return LOW1;
//	}
//	
//	public static Object top() {
//		return HIGH1;
//	}
//}

import analyzer.level2.storage.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;


public class SecurityLevel {
	@SuppressWarnings("rawtypes")
	public static SecDomain secDomain = new LowHigh();
	
	public static Object bottom() {
		return secDomain.bottom();
	}
	
	public static Object top() {
		return secDomain.top();
	}

	@SuppressWarnings("unchecked")
	public static Object lub(Object l1, Object l2) {
		return secDomain.lub(l1, l2);
	}

	@SuppressWarnings("unchecked")
	public static Object glb(Object l1, Object l2) {
		return secDomain.glb(l1, l2);
	}

	@SuppressWarnings("unchecked")
	public static boolean le(Object l1, Object l2) {
		return secDomain.le(l1, l2);
	}
	
	public static Object redSecLevel(String level) {
		return secDomain.readLevel(level);
	}
}

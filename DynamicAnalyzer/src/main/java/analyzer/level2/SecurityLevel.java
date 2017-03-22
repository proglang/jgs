package analyzer.level2;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

import java.io.File;

import analyzer.level2.storage.LowHigh;
import analyzer.level2.storage.LowMediumHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Edge;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class SecurityLevel {
	@SuppressWarnings("rawtypes")
	// public static SecDomain secDomain = new LowMediumHigh();
	public static SecDomain secDomain =
			UserDefined.lowHigh();
			//UserDefined.aliceBobCharlie();
	
	/*private static Object getSecDomainResource(String name) {
	    InputStream in = Object.class.getResourceAsStream("/secdomain/" + name);
	    if (in == null) {
	        throw new RuntimeException(String.format("Cannot find secdomain resource: %s", name));
	    }
	    try {
	    return new ObjectInputStream(in).readObject();
	    } catch (IOException | ClassNotFoundException e) {
	        throw new RuntimeException(String.format("Error reading secdomain resource %s: %s", name, e));
	    }
	}*/
	static {
	    // TODO: read secdomain from resources
	    /*
	        secDomain = new UserDefined((Set)getSecDomainResource("levels"), 
	                                    (Set)getSecDomainResource("lt"),
	                                    (Map)getSecDomainResource("lubMap"),
	                                    (Map)getSecDomainResource("glbMap"),
	                                    (String)getSecDomainResource("topLevel"),
	                                    (String)getSecDomainResource("bottomLevel"));
	                                   */
	    }

	
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
	
	/**
	 * Strictly less than
	 * @param l1
	 * @param l2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean lt(Object l1, Object l2) {
		return secDomain.le(l1, l2) && !l1.equals(l2);
	}
	
	public static Object readLevel(String level) {
		return secDomain.readLevel(level);
	}
}

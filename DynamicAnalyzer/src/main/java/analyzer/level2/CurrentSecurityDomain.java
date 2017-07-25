package analyzer.level2;


import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;

import java.lang.reflect.InvocationTargetException;


/**
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CurrentSecurityDomain {
	@SuppressWarnings("rawtypes")
	public static final SecDomain INSTANCE;
//	public static final SecDomain secDomain =
//			UserDefined.lowHigh();
			// UserDefined.aliceBobCharlie();
	
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
		try {
			INSTANCE = (SecDomain)Class.forName("de.unifreiburg.cs.proglang.jgs.rt.CurrentSecurityDomainInstance")
                     .getMethod("get").invoke(null);
		} catch ( IllegalAccessException
                | NoSuchMethodException
				| InvocationTargetException
				| ClassNotFoundException
				e) {
		    System.err.println("Cannot load security domain: " + e.toString());
            System.exit(-1);
			throw new RuntimeException("This is unexpected. Should have exited here!");
		}
	}

	
	public static Object bottom() {
		return INSTANCE.bottom();
	}
	
	public static Object top() {
		return INSTANCE.top();
	}

	@SuppressWarnings("unchecked")
	public static Object lub(Object l1, Object l2) {
		return INSTANCE.lub(l1, l2);
	}

	@SuppressWarnings("unchecked")
	public static Object glb(Object l1, Object l2) {
		return INSTANCE.glb(l1, l2);
	}

	@SuppressWarnings("unchecked")
	public static boolean le(Object l1, Object l2) {
		return INSTANCE.le(l1, l2);
	}
	
	/**
	 * Strictly less than
	 * @param l1
	 * @param l2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean lt(Object l1, Object l2) {
		return INSTANCE.le(l1, l2) && !l1.equals(l2);
	}
	
	public static Object readLevel(String level) {
		return INSTANCE.readLevel(level);
	}
}

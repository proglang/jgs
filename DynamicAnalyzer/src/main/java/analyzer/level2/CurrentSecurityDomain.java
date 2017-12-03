package analyzer.level2;

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CurrentSecurityDomain {

	public static final SecDomain INSTANCE;

	public static <Level> SecDomain<Level> getInstance() { return INSTANCE; }

	static {
		try {
			INSTANCE = (SecDomain) Class.forName("de.unifreiburg.cs.proglang.jgs.rt.CurrentSecurityDomainInstance")
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

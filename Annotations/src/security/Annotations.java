package security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class Annotations {
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ReturnSecurity {
		
		/** */
		String value();
	
	}

	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ParameterSecurity {
		
		/** */
		String[] value();
	
	}

	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldSecurity {
		
		/** */
		String value();
	
	}

}

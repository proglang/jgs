package securityNew;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class StringLevel extends ALevel {

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldSecurity {

		String value();

	}

	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ParameterSecurity {

		String[] value();

	}

	@Target({ ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ReturnSecurity {

		String value();

	}

	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface WriteEffect {

		String[] value();

	}

	public final String level;

	public StringLevel(String level) {
		this.level = level;
	}

	@Override
	public String getName() {
		return this.level;
	}

	@Override
	public String toString() {
		return this.level;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() == this.getClass()) {
			StringLevel other = (StringLevel) obj;
			return other.level.equals(level);
		}
		return false;
	}

}

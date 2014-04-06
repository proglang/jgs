package security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import annotation.IAnnotationDAO;

import security.ALevel;
import security.ALevelDefinition;
import security.ILevel;

public class Definition extends ALevelDefinition {
	
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

	public final class StringLevel extends ALevel {
		
		private final String level;
		
		public StringLevel(String level) {
			this.level = level;
		}

		@Override
		public String getName() {
			return level;
		}

		@Override
		public String toString() {
			return "StringLevel: " + level;
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
	
	
	private final ILevel low = new StringLevel("low");
	private final ILevel high = new StringLevel("high");
	
	public Definition() {
		super(FieldSecurity.class, ParameterSecurity.class, ReturnSecurity.class, WriteEffect.class);
	}

	@Override
	public int compare(ILevel level1, ILevel level2) {
		if (level1.equals(level2)) {
			return 0;
		} else {
			if (level1.equals(high)) {
				return 1;
			} else { 
				if (level1.equals(low) || level2.equals(high)) {
					return -1;
				} else {
					return 1;
				}
			}
		}
	}

	@Override
	public ILevel getGreatesLowerBoundLevel() {
		return low;
	}

	@Override
	public ILevel getLeastUpperBoundLevel() {
		return high;
	}

	@Override
	public ILevel[] getLevels() {
		return new ILevel[] {low, high};
	}

	@Override
	public ILevel extractFieldLevel(IAnnotationDAO dao) {
		return new StringLevel(dao.getStringFor("value"));
	}

	@Override
	public List<ILevel> extractParameterLevels(IAnnotationDAO dao) {
		List<ILevel> list = new ArrayList<ILevel>();
		for (String value : dao.getStringArrayFor("value")) {
			list.add(new StringLevel(value));
		}
		return list;
	}

	@Override
	public ILevel extractReturnLevel(IAnnotationDAO dao) {
		return new StringLevel(dao.getStringFor("value"));
	}


	@Override
	public List<ILevel> extractEffects(IAnnotationDAO dao) {
		List<ILevel> list = new ArrayList<ILevel>();
		for (String value : dao.getStringArrayFor("value")) {
			list.add(new StringLevel(value));
		}
		return list;
	}

	@ParameterSecurity({ "high" })
	@ReturnSecurity("high")
	public static <T> T mkHigh(T object) {
		return object;
	}

	@ParameterSecurity({ "low" })
	@ReturnSecurity("low")
	public static <T> T mkLow(T object) {
		return object;
	}
	
}

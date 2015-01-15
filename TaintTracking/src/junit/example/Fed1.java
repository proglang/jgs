package junit.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import security.ALevel;
import security.ALevelDefinition;
import security.ILevel;
import annotation.IAnnotationDAO;
import constraints.ComponentParameterRef;
import constraints.ComponentProgramCounterRef;
import constraints.ComponentReturnRef;
import constraints.IComponent;
import constraints.LEQConstraint;
import exception.AnnotationInvalidConstraintsException;

public class Fed1 extends ALevelDefinition {

	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface MethodConstraints {

		String[] value() default {};

	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface FieldSecurity {

		String[] value();

	}

	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface ParameterSecurity {

		String[] value();

	}

	@Target({ ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface ReturnSecurity {

		String value();

	}

	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface WriteEffect {

		String[] value();

	}

	public static final class VelS extends ALevel {

		private final String level;

		public VelS(String level) {
			this.level = level;
		}

		@Override
		public String getName() {
			return level;
		}

		@Override
		public String toString() {
			return level;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((level == null) ? 0 : level.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			VelS other = (VelS) obj;
			if (level == null) {
				if (other.level != null) return false;
			} else if (!level.equals(other.level)) return false;
			return true;
		}

	}

	private final ILevel low = new VelS("low");
	private final ILevel high = new VelS("high");

	public Fed1() {
		super(FieldSecurity.class, ParameterSecurity.class, ReturnSecurity.class, WriteEffect.class, MethodConstraints.class);
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
		return new ILevel[] { low, high };
	}

	@Override
	public List<ILevel> extractFieldLevel(IAnnotationDAO dao) {
		List<ILevel> list = new ArrayList<ILevel>();
		for (String value : dao.getStringArrayFor("value")) {
			list.add(new VelS(value));
		}
		return list;
	}

	@Override
	public List<ILevel> extractParameterLevels(IAnnotationDAO dao) {
		List<ILevel> list = new ArrayList<ILevel>();
		for (String value : dao.getStringArrayFor("value")) {
			list.add(new VelS(value));
		}
		return list;
	}

	@Override
	public ILevel extractReturnLevel(IAnnotationDAO dao) {
		return new VelS(dao.getStringFor("value"));
	}

	@Override
	public List<ILevel> extractEffects(IAnnotationDAO dao) {
		List<ILevel> list = new ArrayList<ILevel>();
		for (String value : dao.getStringArrayFor("value")) {
			list.add(new VelS(value));
		}
		return list;
	}

	@Override
	public Set<LEQConstraint> extractConstraints(IAnnotationDAO dao, String signature) {
		Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();
		List<String> rawConstraints = dao.getStringArrayFor("value");
		for (String constraint : rawConstraints) {
			String errMsg = String.format("The specified constraint '%s' is invalid.", constraint);
			if (constraint.contains("<=")) {
				String[] components = constraint.split("<=");
				if (components.length == 2) {
					String lhs = components[0].trim();
					String rhs = components[1].trim();
					IComponent l = convertIntoConstraintComponent(lhs, signature);
					IComponent r = convertIntoConstraintComponent(rhs, signature);
					constraints.add(new LEQConstraint(l, r));
				} else {
					throw new AnnotationInvalidConstraintsException(errMsg);
				}
			} else if (constraint.contains("=")) {
				String[] components = constraint.split("=");
				if (components.length == 2) {
					String lhs = components[0].trim();
					String rhs = components[1].trim();
					IComponent l = convertIntoConstraintComponent(lhs, signature);
					IComponent r = convertIntoConstraintComponent(rhs, signature);
					constraints.add(new LEQConstraint(l, r));
					constraints.add(new LEQConstraint(r, l));
				} else {
					throw new AnnotationInvalidConstraintsException(errMsg);
				}
			} else if (constraint.contains(">=")) {
				String[] components = constraint.split(">=");
				if (components.length == 2) {
					String lhs = components[0].trim();
					String rhs = components[1].trim();
					IComponent l = convertIntoConstraintComponent(lhs, signature);
					IComponent r = convertIntoConstraintComponent(rhs, signature);
					constraints.add(new LEQConstraint(r, l));
				} else {
					throw new AnnotationInvalidConstraintsException(errMsg);
				}
			} else {
				throw new AnnotationInvalidConstraintsException(errMsg);
			}
		}
		return constraints;
	}

	private IComponent convertIntoConstraintComponent(String component, String signature) {
		if (component.startsWith("@")) {
			String position = component.substring(1);
			if (position.equals("return")) {
				return new ComponentReturnRef(signature);
			} else if (position.equals("pc")) {
				return new ComponentProgramCounterRef(signature);
			} else {
				return new ComponentParameterRef(Integer.valueOf(position), signature);
			}
		} else {
			return new VelS(component);
		}
	}

	@ParameterSecurity({ "high" })
	@ReturnSecurity("high")
	@MethodConstraints({ "@0 <= high", "@return = high" })
	protected static <T> T mkHigh(T object) {
		return object;
	}

	@ParameterSecurity({ "low" })
	@ReturnSecurity("low")
	@MethodConstraints({ "@0 <= low", "@return = low" })
	protected static <T> T mkLow(T object) {
		return object;
	}

}

## How to use
To check a specific source with the Security-Type for Java project for security violations, several requirements must be satisfied. This includes on the one hand the implementation of the class `Definition` in the to-be-checked project, which is a subclass of [`ALevelDefinition`][ALevelDefinition] and which specifies the *security levels* as well as the Java annotation classes which provides the levels and the effects information of field, methodTypings and classes. On the other hand, adding the required annotations in the source code as well as adding also the *security levels* of local variables.

### Subclass of `ALevelDefinition.java`
The class `Definition` has to define the customized hierarchy of the *security levels*. Also the class has to provide for each *security level* a so-called id function, which takes a value of a specific type with a weaker or equal *security level* and returns the value with original type and the *security level* of the id function. Thus, the following restrictions have to be considered:

1. In the project, which is to be checked, a class named `Definition` needs to be implemented. The package of this implementation needs to be `security` and the implementation needs to inherit from the class [`ALevelDefinition`][ALevelDefinition].

2. The class has to implement all abstract methodTypings of the super class  [`ALevelDefinition`][ALevelDefinition]. I.e. especially the extraction methodTypings (e.g. `ALevelDefinition#extractFieldLevel(IAnnotationDAO dao)`), the comparison methodTypings  (e.g. `ALevelDefinition#getGreatestLowerBoundLevel(ILevel l1, ILevel l2)`) as well as the method which provides all available *security levels* `ALevelDefinition#getLevels()`: this method returns a `ILevel` array, means you can specify a customized level class which implements the `ILevel` interface. This array has to contain at least two *security levels*. Those levels are **not** in a specific order, because the hierarchy is given by the comparison methodTypings. Note that each provided level has to be valid, i.e. the name of the level contains none of the characters `*`, `(`, `)` and `,`.

3. The class provides an id function for every *security level*, which is defined by the returned `ILevel` array of the method `Definition#getLevels()`. Such an id function is `public`, `static`, takes an object of generic type `T` and returns this object of generic type `T`. The name of the id function starts with the corresponding *security level* name and ends with the suffix 'Id', e.g. for level with the name 'high' the id function name will be 'highId'. Also each id function is annotated with the corresponding return and parameter *security level*. I.e. the *security level* of the specified argument of an id function has to be weaker or equal the corresponding *security level* and the *security level* of the returned value is equal to the corresponding *security level*.

4. To call the super constructor, four Java annotation classes are required which represent the annotations for the *field security level*, the *parameter security levels*, the *return security level* and the *write effects*. These classes should be declared as nested classes in the `Definition` class.

#### Example
The following example of an implementation specifies two *security levels*: the strongest level is 'high', the weakest level is 'low'. 

```java
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
	public static <T> T highId(T object) {
		return object;
	}

	@ParameterSecurity({ "low" })
	@ReturnSecurity("low")
	public static <T> T lowId(T object) {
		return object;
	}
	
}
```

# Security Types for Java

## Summary

A research project with the goal to allow gradual integration of refined type systems in Java (e.g. type systems for physical dimensions, effects, security, ...)

## Setup
Required software:

1. IDE Eclipse (tested and developed with [Eclipse Luna][1])
2. [Soot framework (version 2.5.0)][2] (Unpack the archive somewhere - the three directories `soot-2.5.0`, `polyglot-1.3.5`, `jasmin-2.5.0` will be created )
3. Optional: [Soot Eclipse Plugin][3]
4. [Apache Ant][4]
5. [JUnit 4][5] (`junit.jar` and `hamcrest-core.jar` are required)
6. The Eclipse projects [TaintTracking] and [Testcases] from this repository

### Setup the Project TaintTracking in Eclipse
- import [TaintTracking] into eclipse
- Add polyglott.jar, jasminclasses-2.5.0.jar, sootclasses - 2.5.0.jar and ant.jar es external jar in the project's build path.
- Add "classes" folder of the soot project as external classes to the build path.
- use Java 1.7 JRE
 
### Setup the Project TestCases in Eclipse
- import [TestCases] into Eclipse


  
### Setup the Security-Type for Java projects
- Import the projects [TaintTracking] and [Testcases] from this repository into Eclipse.
- The project [Testcases] requires the following:
	- source folder on the build path is `src/`
	- use the Java 1.7 JRE
- The project [TaintTracking] requires the following:
	- Soot Eclipse project on the build path of the project [TaintTracking]
	- JUnit 4 JARs on the build path of the project [TaintTracking]
	- source folder on the build path is `src/`
	- use the Java 1.7 JRE
	- 
	





.
## How to run (currently)
The run configurations AllJUnitTests.launch and AnalysisRun.launch should work out of the box. They run the unit tests and the main application, respectively.
- Command line arguments for the main application are:
  - `-def-classpath` the classpath of the "Definitions"-file, e.g. `./../Testcases/bin`. The "Definitions"-file is the new way to specify the security lattice, etc.
  - `-source-path` path to the source files to be analyzed, e.g. `./../Testcases/src`
  - `-program-classpath` classpath to the code implementing the analysis, e.g. `./bin`
  - `-cp` Soot's classpath parameter. It should be set to the jdk/jre, e.g. `/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/rt.jar`
  - `-main-class` class that contains the main method, e.g. `junitAnalysis.FailArray`
  - `<class>`, e.g. `junitAnalysis.FailArray`. Is this (also) the class to be analyzed, like `-main-class`?

## How to use
To check a specific source with the Security-Type for Java project for security violations, several requirements must be satisfied. This includes on the one hand the implementation of the class `Definition` in the to-be-checked project, which is a subclass of [`ALevelDefinition`][ALevelDefinition] and which specifies the *security levels* as well as the Java annotation classes which provides the levels and the effects information of field, methods and classes. On the other hand, adding the required annotations in the source code as well as adding also the *security levels* of local variables.

### Subclass of `ALevelDefinition.java`
The class `Definition` has to define the customized hierarchy of the *security levels*. Also the class has to provide for each *security level* a so-called id function, which takes a value of a specific type with a weaker or equal *security level* and returns the value with original type and the *security level* of the id function. Thus, the following restrictions have to be considered:

1. In the project, which is to be checked, a class named `Definition` needs to be implemented. The package of this implementation needs to be `security` and the implementation needs to inherit from the class [`ALevelDefinition`][ALevelDefinition].

2. The class has to implement all abstract methods of the super class  [`ALevelDefinition`][ALevelDefinition]. I.e. especially the extraction methods (e.g. `ALevelDefinition#extractFieldLevel(IAnnotationDAO dao)`), the comparison methods  (e.g. `ALevelDefinition#getGreatestLowerBoundLevel(ILevel l1, ILevel l2)`) as well as the method which provides all available *security levels* `ALevelDefinition#getLevels()`: this method returns a `ILevel` array, means you can specify a customized level class which implements the `ILevel` interface. This array has to contain at least two *security levels*. Those levels are **not** in a specific order, because the hierarchy is given by the comparison methods. Note that each provided level has to be valid, i.e. the name of the level contains none of the characters `*`, `(`, `)` and `,`.

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

### Source code specific
*Coming soon...*

### Run the analysis
To perform the analysis, please use the lauch configuration [AnalysisRun] and specify the class which should be analyzed. 

## Violation policy

### Effects

#### Write effects
* **assignment**:
	+ **static field**:  
	Results in a *write effect* to the *security level* of the static field, as well as in the *write effects* of the class which declares the static field.
	+ **instance field**:  
	Results in a *write effect* to the *security level* of the instance field.
	+ **array**:  
	Results in a *write effect* to the *security level* of the array (no matter if the array is a local variable or a field).
* **invocation**:
	+ **static method**:  
	Results in the *write effects* which are defined for the invoked method, as well as in the *write effects* of the class which declares the static method.
	+ **instance method**:  
	Results in the *write effects* which are defined for the invoked method.
	
On the one hand a *write effect* violation happens if the *write effect* annotation doesn't contain a calculated effect. On the other hand also a *write effect* violation occurs if a *write effect* takes place inside a context which is stronger or equals the affected *security level*.

### Security
* **update of level**:
	+ **local variable**:  
	no restrictions, i.e. no matter if the *security level* of the assigned value is weaker, equals or is stronger than the level of the local variable. The level of the local variable will be updated to this *security level*. Note: if the context is stronger than the level of the assigned value, the level of the local variable will be updated to this stronger program counter *security level*.
	+ **field**:  
	the *security level* of the assigned value has to be weaker or equal to the level of the field. The *security level* will not be updated. Note: the context will be considered, thus it is not possible to assign to a field if the the context is stronger than the level of the field.
	+ **array** (special case):  
	no matter which *security level* an array itself has, the values which should be stored in the array must have the weakest *security level*. Also the level of the index should be weaker than the *security level* of the array.
	
* **lookup of level**:
	+ **local variable**:   
	result will be the *security level* of the local.
	+ **field**:  
	result will be the *security level* of the field. If the field is an instance field and the instance has a stronger *security level* than the field level, then the result will be the stronger instance *security level*. 
	+ **array**:  
	result will be the *security level* of the stored value at the specified index (because of restrictions this is the weakest available *security level*). If the array has a stronger level, then the result will be this stronger *security level* of the array. Also if the *security level* of the index is stronger than the level of the array and the stored value, then the result will be the level of the index.
	+ **array length**:  
	the result will be the *security level* of the array.   
	+ **constant**:  
	result will be the weakest available *security level*.
	+ **library field**:  
	result will be the weakest available *security level*.
	+ **library method**:  
	result will be the strongest *security level* of the specified arguments or the weakest available *security level*, if no arguments are specified.
	+ **expression**:   
	result will be the strongest *security level* of the operands.
	+ **method**:  
	the result will be the return *security level* of the method. If the method is an instance method and the instance has a stronger *security level* than the return level, then the result will be the stronger instance *security level*. During the lookup also the level of arguments are compared with the *security level* of the parameters. The *security level* of the argument has to be weaker or equals than the corresponding parameter *security level*.
			
* **return level**:  
looks up the *security level* of the returned value and compares it with the expected return *security level*. The calculated level has to be weaker or equals than the expected return *security level*.

*Security level* violations are triggered by the following phenomena:

* Assignment of a value to a field, where the field has a weaker *security level* than the assigned value. Note that the *security level* of the assigned value also depends on the context.
* The calculated return *security level* is stronger than the expected return level (or the levels are not comparable, e.g. 'void'). Note that if the context is stronger than the *security level* of the returned value, then this stronger program counter *security level* is the calculated return *security level*.
* The *security level* of an argument of a method invocation is stronger than the expected level for this parameter.


-----

[1]: http://www.eclipse.org/downloads/ "Eclipse Luna"
[2]: http://www.sable.mcgill.ca/software/sootall-2.5.0.jar "Download Soot Framework"
[3]: http://www.sable.mcgill.ca/soot/eclipse/updates/index.html "Download Soot Eclipse plugin"
[4]: http://ant.apache.org/bindownload.cgi "Download Apache Ant"
[5]: https://github.com/junit-team/junit/wiki/Download-and-Install "JUnit 4"

[Soot]: http://www.sable.mcgill.ca/soot/ "Soot compiler framework"
[SootCommandline]: http://www.sable.mcgill.ca/soot/tutorial/usage/ "Soot command-line options"
[TaintTracking]: TaintTracking/ "Project TaintTracking"
[Testcases]: Testcases/ "Project Testcase"
[ALevelDefinition]: TaintTracking/src/security/ALevelDefinition.java "ALevelDefinition"
[AnalysisRun]: AnalysisRun.launch "Analysis Run"

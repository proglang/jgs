# Gradual Java
A research project with the goal to allow gradual integration of
refined type systems in Java (e.g. type systems for physical
dimensions, effects, security, ...)

## Setup
Required software:

1. IDE Eclipse (tested and developed with [Eclipse Juno][1])
2. [Soot framework (version 2.5.0)][2]
3. [Soot Eclipse plugin][3]
4. [Apache Ant][4]
5. [JUnit 4][5] (`junit.jar` and `hamcrest-core.jar` are required)
6. The Eclipse projects [TaintTracking], [SootUtils] and [Annotations]


### Install Eclipse
- If not already installed, get [Eclipse (e.g. Juno)][1] and install the IDE

### Install Soot
The static analysis is based on the [Soot compiler framework][Soot]. Here are some quick setup instructions:

- Go to the [Soot website][2], download `sootall-2.5.0.jar`, the complete package
- Unpack the archive somewhere (the three directories `soot-2.5.0`, `polyglot-1.3.5`, `jasmin-2.5.0` will be created)
- Download a binary distribution of [Apache Ant][4]
- Follow instructions in `soot-2.5.0/soot_in_eclipse_howto.html`.
- Install the [Soot Eclipse plugin][3]
- Now you should be able to import the projects ([TaintTracking], [Annotations] and [SootUtils]) from this repository without soot-related compilation errors (see [next step](#output)).
  
### Setup the Gradual Java projects
- Import the projects [TaintTracking], [Annotations] and [SootUtils] from this repository into Eclipse.
- The project [SootUtils] requires following:
	- Soot Eclipse project on the build path of the project [SootUtils]
	- JUnit 4 JARs on the build path of the project [SootUtils]
	- source folder on the build path is `src/`
	- use the Java 1.7 JRE
- The project [Annotations] requires following:
	- Soot Eclipse project as well as [SootUtils] project on the build path of the project [Annotations]
	- JUnit 4 JARs on the build path of the project [Annotations]
	- source folder on the build path is `src/`
	- use the Java 1.7 JRE
- The project [TaintTracking] requires following:
	- Soot Eclipse project, [Annotations] project as well as [SootUtils] project on the build path of the project [TaintTracking]
	- JUnit 4 JARs on the build path of the project [TaintTracking]
	- source folder on the build path is `src/`
	- use the Java 1.7 JRE

## How to use
To check a specific source with the Gradual Java project for security violations, several requirements must be satisfied. This includes on the one hand the implementation of the class `SootSecurityLevel` in the to be checked project, which is a subclass of [`SecurityLevel`][SecurityLevel] and which specifies the *security levels*, and on the other hand, to add the required annotations in the source code as well as to add also the *security levels* of local variables.

### Subclass of `SecurityLevel.java`
The class `SootSecurityLevel` has to define the customized hierarchy of the *security levels*. Also the class has to provide for each *security level* a so-called id function, which takes a value of a specific type and with a weaker or equal *security level* and returns the value with original type and the *security level* of the id function. Thus, following restrictions have to be considered:

1. In the project, which is to be checked, a class named `SootSecurityLevel` needs to be implemented. The package of this implementation needs to be `security` and the implementation needs to inherit from the class [`SecurityLevel`][SecurityLevel].


    ```java
    package security;
    
    public class SootSecurityLevel extends SecurityLevel {
    	
    	…
    	
    }
    ```

2. The class has to inherit the method `SecurityLevel#getOrderedSecurityLevels()` which returns a `String` array. This array has to contain at least two *security levels*. Those levels are ordered, i.e. the strongest *security level* has the smallest index and the weakest *security level* has the greatest index in the returned list. Each provided level has to be valid, i.e. contains none of the characters `*`, `(`, `)` and `,` as well as none of the provided *security levels* is equals to the internal `void` non return *security level*.


    ```java
    @Override
    public String[] getOrderedSecurityLevels() {
    	return new String[] { "high", "normal", "low" };
    }
    ```
  
3. The class provides an id function for every *security level*, which is defined by the returned `String` array of the method `SootSecurityLevel#getOrderedSecurityLevels()`. Such an id function is `public`, `static`, takes an object of generic type `T` and returns this object of generic type `T`. The name of the id function starts with the corresponding *security level* name and ends with the suffix 'Id', e.g. for level 'high' the id function name will be 'highId'. Also each id function is annotated with the corresponding return *security level* (see [Security annotation](#security-annotation)), e.g. for level 'high' the id function is annotated with `@ReturnSecurity("high")`.


    ```java
    @Annotations.ReturnSecurity("high")
	public static <T> T highId(T object) {
		return object;
	}
    ```

#### Implementation check
To verify whether the implementation of the class `SootSecurityLevel` is complete and correct, create an instance of the class [`SecurityLevelImplChecker`][SecurityLevelImplChecker]. By calling the constructor of [`SecurityLevelImplChecker`][SecurityLevelImplChecker], the specified instance of the `SootSecurityLevel` implementation will be checked and errors will be printed to the console.

```java
SecurityLevelImplChecker checker = 
		new SecurityLevelImplChecker(new SootSecurityLevel());
```

#### Example
The following example of an implementation specifies three *security levels*: the strongest level is 'high', the weakest level is 'low'. Between those two levels exists also a third level which is weaker than 'high', but stronger than 'low'.

```java
package security;

public class SootSecurityLevel extends SecurityLevel {

	@Override
	public String[] getOrderedSecurityLevels() {
		return new String[] { "high", "normal", "low" };
	}
		
	@Annotations.ReturnSecurity("high")
	public static <T> T highId(T object) {
		return object;
	}
	
	@Annotations.ReturnSecurity("normal")
	public static <T> T normalId(T object) {
		return object;
	}
	
	@Annotations.ReturnSecurity("low")
	public static <T> T lowId(T object) {
		return object;
	}
	
}
```


### Source code specific
Also the source code of the to be checked project has to be prepared for the analysis. Thus, annotations have to be added for classes, fields and methods as well as the *security level* of local variables have to be specified by calling the id functions. 

#### Security annotation
Because the analysis checks for security violations, each field in the project has to provied a *security level*, no matter if it is an instance or a static field. For this purpose, the annotation `@FieldSecurity` in the class [`Annotations`][Annotations class] can be used. The value of the annotation specifies the level of the field. 
(Consider that it is invalid if the declaration includes also an assignment and the assigned value has a stronger *security level*. In the case of a static field the check of the static initializer method will fail, in the case of an instance field the check of the constructors will fail.)

```java
@Annotations.FieldSecurity("high")
public int highField;
```

```java
@Annotations.ReturnSecurity("level")
```

```java
@Annotations.ParameterSecurity("arg1_level", … )
```

#### Effect annotation

```java
@Annotations.WriteEffect("level", … )
```

#### Security level of locals
To specify the *security level* of a specific local variable, the id functions can be used. The *security level* of the returned object, respectively value is the *security level* which the called id function corresponds to. The id functions take only an argument which has a weaker or equal *security level* than the level to which the id function corresponds. It is not possible to weaken the *security level* of an object, respectively a value.
 
```java
int value = SootSecurityLevel.highId(42);
```
### Run the analysis
```

```

## Violation policy

### Effects

#### Write effects
* Assignment:
	+ static field:
	> Results in a *write effect* to the *security level* of the static field, as well as in the *write effects* of the class which declares the static field.
	+ instance field:
	> Results in a *write effect* to the *security level* of the instance field.
	+ array:
	> Results in a *write effect* to the *security level* of the array (no matter if the array is a local variable or a field).
* Invocation:
	+ static method:
	> Results in the *write effects* which are defined for the invoked method, as well as in the *write effects* of the class which declares the static method.
	+ instance method:
	> Results in the *write effects* which are defined for the invoked method.

### Security

## Output

## Bugfixes


[1]: http://www.eclipse.org/juno/ "Download Eclipse Juno"
[2]: http://www.sable.mcgill.ca/soot/soot_download.html "Download Soot Framework"
[3]: http://www.sable.mcgill.ca/soot/eclipse/updates/index.html "Download Soot Eclipse plugin"
[4]: http://ant.apache.org/bindownload.cgi "Download Apache Ant"
[5]: https://github.com/junit-team/junit/wiki/Download-and-Install "JUnit 4"

[Soot]: http://www.sable.mcgill.ca/soot/ "Soot compiler framework"
[TaintTracking]: TaintTracking/ "Project TaintTracking"
[Annotations]: Annotations/ "Project Annotation"
[SootUtils]: SootUtils/ "Project SootUtils"
[SecurityLevel]: Annotations/src/security/SecurityLevel.java "Class SecurityLevel"
[SecurityLevelImplChecker]: Annotations/src/security/SecurityLevelImplChecker.java "Class SecurityLevelImplChecker"
[Annotations class]: Annotations/src/security/Annotations.java "Class Annotations"

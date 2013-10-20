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
    
    public class SootSecurityLevel extends SecurityLevel { … }
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

1. fields:  
because the analysis checks for security violations, each field in the project has to provied a *security level*, no matter if it is an instance or a static field. For this purpose, the annotation `@FieldSecurity` in the class [`Annotations`][Annotations class] can be used. The value of the annotation specifies the level of the field. The level has to be one of the *security levels* specified by the method `SootSecurityLevel#getOrderedSecurityLevels()`.  
(Consider that it is invalid if the declaration includes also an assignment and if the assigned value has a stronger *security level*. In the case of a static field the check of the static initializer method will fail, in the case of an instance field the check of the constructors will fail.)


    ```java
    @Annotations.FieldSecurity("high")
    public int highField;
    ```

  Note: the `@FieldSecurity` annotation can be used only on fields. If the annotation is not present at a field declaration, this results in an error message.

2. methods:  
to check for security violations, the analysis requires also for every method, no matter if it is a static or instance method, in the project the annotation which specifies the *security levels* of the parameters and the annotation which specifies the *security level* of the returned value. This implies also that the **default constructor must be implemented**, otherwise it is possible that the analysis prints an error for this virtual constructor.


    1. parameter *security levels*:  
    the `@ParameterSecurity` annotation in the class [`Annotations`][Annotations class] can be used to specify the *security levels* of methods parameters. I.e. for each parameter of the method the array has to contain a corresponding *security level*. The arrangement of the levels is identical to the order of the parameters. This means that the first level of the array corresponds to the security level of the first parameter, and so on. In the case of calling the method, it must be ensured that the used arguments have the same or a weaker *security level* than the parameters.  
    As a level in the required array on the one hand a *security level* which is specified by the method `SootSecurityLevel#getOrderedSecurityLevels()` and on the other hand, also a variable *security level* can be used. 


      ```java
      @Annotations.ParameterSecurity({})
      …
      public int test() { … }
      ```

      ```java
      @Annotations.ParameterSecurity({"high"})
      …
      public int test(int high) { … }
      ```
      
      ```java
      @Annotations.ParameterSecurity({"high", "low"})
      …
      public int test(int high, int low) { … }
      ```
      
      A variable level begins with the character '\*' followed by a number. This number starts always at 0 for a method and will be increased by 1 per additional variable level. Consider that the choice of the numbers has to be without interruptions. The benefit of the variable *security level* is the calculation of the return *security level* based on the *security level* of the arguments.
	
	
      ```java
      @Annotations.ParameterSecurity({"*0"})
      …
      public int test(int var) { … }
      ```
      
      ```java
      @Annotations.ParameterSecurity({"*0", "*1"})
      …
      public int test(int var1, int var2) { … }
      ```
      
      Note: the `@ParameterSecurity` annotation can be used only on methods and constructors. If the annotation is not present at a method declaration, the count of the parameters and the count of the given *security levels* are not equals or the array contains an invalid level, this results in an error message. Also, if the method doesn't accept arguments use an empty array, otherwise an error occurs.

    2. return *security level*:  
    to specify the return *security level*, i.e. the level of the returned value of the method, the annotation `@ReturnSecurity` in the class [`Annotations`][Annotations class] can be used. The analysis will check whether the calculated return *security level* is weaker or equals than the expected level.  
    The value of this annotation has to be a concrete *security level* which is specified by the method `SootSecurityLevel#getOrderedSecurityLevels()` or if the method has no return value, then the value of this annotation should be the void return *security level* ('void').
      
      
      ```java
      …
      @Annotations.ReturnSecurity("high")
      public int test() { … return high; } 
      ```   
      
      ```java
      …
      @Annotations.ReturnSecurity("void")
      public void test() { … return; } 
      ```
      
      A variable *security levels* can be used as value of the annotation, too. It must be ensured that the used variable level was defined in the corresponding parameter *security levels* annotation. The benefit of using a variable *security level* is that the return *security level* depends on the *security level* of a specific argument. I.e. the analysis tries solving the return level by looking up the corresponding *security level* of the argument.
      
      ```java
      @Annotations.ParameterSecurity({"*0"})
      @Annotations.ReturnSecurity("*0")
      public int test(int var) { … return var; }
      ```
      
      Also, the value of the annotation can be a *security level* equation. A level equation allows the usage of the operators `min` and `max` and the usage of concrete *security levels*, variable *security levels* as well as a nested equation as operands. I.e. the analysis tries solving the given equation by inserting the corresponding *security levels* of the arguments.
      
      ```java
      …
      @Annotations.ReturnSecurity("max(high,low)")
      public int test() { … return high; }
      ```
      
      ```java
      @Annotations.ParameterSecurity({"*0"})
      @Annotations.ReturnSecurity("min(*0,normal)")
      public int test(int var) { … return … ? var : normal; }
      ```
      
      Note: the `@ReturnSecurity` annotation can be used only on methods. If the annotation is not present at a method declaration or the level is invalid, this results in an error message. Also, if the method is a void method and the given return *security level* isn't the 'void' *security level*. **Constructors do not require a return *security level* annotation.**
      
#### Effect annotation
the `@WriteEffect` annotation in the class [`Annotations`][Annotations class] can be used to specify the *write effects* of a specific class or a specific method. I.e. the *write effects* of a method indicate which *write effects* may occur when executing the method and the *write effects* of a class incdicate which *write effects* occur when the static initializer will be executed (e.g. when an instance of class is created, a static method of class is invoked, a value to a static field is assigned or a non-constant field is used). The value of the annotation has to be an array which contains the *security levels* to which a *write effect* is expected. A valid *security level* is specified by the method `SootSecurityLevel#getOrderedSecurityLevels()`.

```java
@Annotations.WriteEffect("low")
public class Example {
	
	…
	
	@Annotations.ParameterSecurity({})
    @Annotations.ReturnSecurity("void")
    @Annotations.WriteEffect("low", "high")
	public void test() { … }
	
}
```

Note: the `@WriteEffect` annotation can be used on methods, constructors and on classes. If the annotation is not present at those declarations or one of the specified level, to which a *write effect* is expected, is invalid, this results in an error message.

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

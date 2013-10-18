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
- Now you should be able to import the projects ([TaintTracking], [Annotations] and [SootUtils]) from this repository without soot-related compilation errors.
  
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
To check a specific source with the Gradual Java project for security violations, several requirements must be satisfied. This includes on the one hand the implementation of the class `SootSecurityLevel` in the to be checked project, which is a subclass of `SecurityLevel` and which specifies the *security levels*, and on the other hand, to add the required annotations in the source code as well as to add also the *security level* of local variables.

### Subclass of `SecurityLevel.java`
The class `SootSecurityLevel` has to define the customized hierarchy of the *security levels*. Also the class has to provide for each *security level* a so-called id function, which takes a value of a specific type and with a weaker or equal *security level* and returns the value with original type and the *security level* of the id function. Thus, following restrictions have to be considered:

1. In the project, which is to be checked, a class named `SootSecurityLevel` needs to be implemented. The package of this implementation needs to be `security` and the implementation needs to inherit from the class [`SecurityLevel`][SecurityLevel].

        java
        package security;
        
        public class SootSecurityLevel extends SecurityLevel {
        	
        	…
        
        }

2. sdfdgdf
3. 



```
@Override
public String[] getOrderedSecurityLevels() {
	return new String[] { "level", … };
}
```

```
@Annotations.ReturnSecurity("level")
public static <T> T levelId(T object) {
	return object;
}
```

#### Implementation check
```
SecurityLevelImplChecker checker = 
		new SecurityLevelImplChecker(new SootSecurityLevel());
```

#### Example
```
package security;

public class SootSecurityLevel extends SecurityLevel {

	@Override
	public String[] getOrderedSecurityLevels() {
		return new String[] { "high", "low" };
	}
		
	@Annotations.ReturnSecurity("high")
	public static <T> T highId(T object) {
		return object;
	}
	
	@Annotations.ReturnSecurity("low")
	public static <T> T lowId(T object) {
		return object;
	}
}
```


### Source code to check
#### Effect annotation

```
@Annotations.WriteEffect("level", … )
```

#### Security annotation

```
@Annotations.ReturnSecurity("level")
```

```
@Annotations.FieldSecurity("level")
```

```
@Annotations.ParameterSecurity("arg1_level", … )
```

#### Security level of locals
```
int value = SootSecurityLevel.levelId(42);
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

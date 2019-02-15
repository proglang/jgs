# Dynamic Analyzer
Enforcing non-interference using run-time security labels: An implementation of dynamic information flow control, based on the no-sensitive-upgrade (NSU) policy.

## Purpose of the Dynamic Analyzer
The DA's purpose is to take a java file and compile it to one of the following formats: Jimple, or bytecode.
Jimple can be thought of as an intermediate representation between java and java bytecode.
This task is accomplished using the [Soot](https://github.com/Sable/soot/) compiler framework. If the java file has
been annotated by the programmer with security-levels, DA adds run-time checks to ensure non-interference. This process
is called "instrumentation".


## Setup
Setting up the Dynamic Analyzer (DA) is not complicated, although not yet trivial. Follow these steps:
- Clone the full repository to your hard drive
- Open the Dynamic Analyzer folder as an existing project in Eclipse 
- Download and Install Java 7. Then, set Java 7 (1.7) as default JRE in eclipse.
- create a new folder DEPS on your hard drive and paste in the following libraries:
  - junit.jar
  - sootclasses-2.5.0.jar
  - jasminclasses-2.5.0.jar
  - java_cup.jar
  - commons-collections4-4.0.jar from "Apache Commons Collections
  - apache-ant-1.9.7
- add the file `gradualconstraints_instrumentationsupport_2.11-0.1-SNAPSHOT.jar` to the DEPS folder. TODO: Dont know anymore how to create it
- add folder DEPS to your buildpath: Right-click on DynamicAnalyzer in the Package Explorer -> BuildPath -> Configure Build Path -> Add variable, pointing to your DEPS directory

## Command line arguments
The following flags are supported:
- `-j`, indication output to the jimple format. if omitted, output will be a classfile
- `-o`, the output directory. May be absolute or relative. If omitted, output will be in current folder
- `-p`, the path to src directory. May be absolute or relative. If omitted, source must be in current folder

Sample arguments for main method (see tests.end2endtest.compileToJarTests):
- `testclasses.NSUPolicy1`
- `testclasses.NSUPolicy1 -j`
- `testclasses.NSUPolicy1 -o /path/to/outdir`
- `testclasses.NSUPolicy1 -o relativepath/to/outputdir`
- `testclasses.NSUPolicy1 -p /path/to/inputdir`
- `testclasses.NSUPolicy1 -p relativepath/to/inputdir -o /path/to/outdir -j`

## Example
Consider the following java class:
```
public class Simple {
	public static void main(String[] args) {
		String message = "Hello World";
		message = HelperClass.makeHigh(message);
		System.out.println(message);
	}
}
```
This is a simple test-method which tries to print a message with high security level to a public output (System.out.println is defined as public output).
The result should be an illegal flow exception. And indeed, it is: If we compile the program using DA, the code gets instrumented with dynamic run-time checks
and throws an "IllegalFlowException" if executed.


## Compiling via main:
- Choose your Run Configurations (for example, if we want to compile to instrumented binary, use the RunMainAnalyzerSingleC, which has the following arguments: `-f c --classes testclasses.WhileLoopFail --main_class testclasses.WhileLoopFail)` 
-  Execute `DynamicAnalyser.src.main.Main.java` from within Eclipse. This produces a file in 'sootOutput' which is instrumented, meaning
   that it has the original program PLUS more code that checks - on runtime - if the flow is valid or if there is information leakage.
- Go to folder `sootOutput/`
- Run the compiled file using:
```
java -cp .:../bin:../../../DEPS/gradualconstraints_instrumentationsupport_2.11-0.1-SNAPSHOT.jar:../../../DEPS/commons-collections4-4.0/commons-collections4-4.0.jar   testclasses.NameOfTest
```
Note on `.:../bin:../../../DEPS/gradualconstraints_instrumentationsupport_2.11-0.1-SNAPSHOT.jar:../../../DEPS/commons-collections4-4.0/commons-collections4-4.0.jar`:
To run the class file successfully, we have to provide more that just the file, because it contains instrumented code: methodTypings and classes
which also have to be available to the program at runtime!

## Unit tests
There are two kinds of unit tests:
- end to end tests in src.testclasses: These get compiled and instrumented, then executed, and observed for desired Exception.
- manually instrumented testfiles, which can be found in tests.analyzer.level2. Here we include the run-time checks "by hand".
All of these tests can be run by executing the tests.RunAllTests as JUnit test from within Eclipse.

## Brief overview over the source code
To get you started, here is a very brief introduction into the source code:
- main entry point is main.Main
- `analyzer.level1` contains the classes necessary for correct instrumentation, eg contains the code that is responsible for instrumenting an existing java program.
- `analyzer.level2` contains the classes which are executed on runtime. These check if the information flow is valid, and throw appropriate Exceptions (IllegalFlowException) if not.

## How to cause an IllegalFlowException
IllegalFlowExceptions are at the core of DA: They indicate a possible leak of information, and terminate execution of the program. There are two conceptually different ways to cause an IllegalFlowException in this implementation:

### Public Outputs
The easiest way to leak data is to leak it to a public output. Public outputs are defined in `util.visitor.ExternalClasses`. Currently, these are:
- `<java.io.PrintStream: void println(java.lang.String)>`
- `<java.io.PrintStream: void println(int)>`
- `<java.io.PrintStream: void println(boolean)>`
- `<java.io.PrintStream: void println(java.lang.Object)>`
This is just a temporary solution. Note that an explicit leak of a secret double would not be caught, since it's not defined.

### Non sensitive upgrade (NSU) policy
The NSU policy enforces IllegalFlowExceptions in a certain kind of context, even if information has not yet been leaked. This is a theoretical limitation: In a certain context, the type system "gets confused" and doesn't really know what label to assign to a local. Consider the following example:
```
y = makeLow(5);						// y is LOW
	
if (secret == 42) {					// PC is HIGH
	y += 1;							// IllegalFlowException!
}									
```
The local `y` would be upgraded from LOW to HIGH, because the high guard (`secret == 42`) sets the localPC to HIGH inside the if statement. This upgrade will not pass: An IllegalFlowException will be thrown, even though no information has yet been leaked. If the PC is HIGH, we cannot update a LOW variable and have it upgraded to HIGH. The reason for this is purely technical, and can be understood when studying the NSU policy.

A working example can be found in code under `testclasses.NSUPolicy1`.

### Further example
Consider the following code (see `testclasses.NSUPolicy2` for working example) 
```
o1 = new C();
o2 = new C();
o1.f = true;
o2.f = false;
// o1, o2, o1.f, o2.f are LOW

if (secret) {
	o = o1;
} else {
	o = o2;
}
// o ist HIGH

println(o1.f)		// Ok, since o1.f is public
println(o.f)		// IllegalFlowException, leaks information about secret

o1.f = 5			// Ok	
o.f = 5				// IllegalFlowException cause by NSU		 									
```
Note that the level of `o.f` is the join of Level(o) and Level(f).

## Soot overview
At its heart, soot is a compiler: It takes an input (mostly Java Source Code) and returns bytecode 
or intermediate representation (Jimple). Let's consider the process of compiling java source code to 
jimple intermediate representation: Every piece of java code has its counterpart in Soot:

Java 				| Soot
-----				|-----
method body 		| `body` class (JimpleBody)
local variables 	| Locals, `getLocals()`
Statements			| Units, `getUnits()`
Exceptions			| Traps, `getTraps()`
					| Packs (are the phases of execution of Soot)

### Statement
A statement is the smallest standalone element of an imperative programming language that expresses some
 action to be carried out. Examples:
- Assertions, eg: `assert(x > 0)`
- Assignments, eg: `a = a + 42`
- Goto
- Return, eg: `return 42;`
- call, eg: `println("Hello World")`
A statement in Soot is represented by the interface `Unit`, of which there are different implementations. 
Jimple uses `Stmt`. We can perform a variety of analysis on a given Unit, for example `getUseBoxes()`,
which returns all values used in this unit/statement. 











 
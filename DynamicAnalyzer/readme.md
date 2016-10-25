# Dynamic Analyzer
Enforcing non-interference using run-time security labels: An implementation of dynamic information flow control, based on the no-sensitive-upgrade (NSU) policy.

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
- add the file `gradualconstraints_instrumentationsupport_2.11-0.1-SNAPSHOT.jar` to the DEPS folder. TODO: Dont know anymore how to create it
- add folder DEPS to your buildpath: Right-click on DynamicAnalyzer in the Package Explorer -> BuildPath -> Configure Build Path -> Add variable, pointing to your DEPS directory

## Purpose of the Dynamic Analyzer
The DA's purpose is to take a java file and compile it to one of the following formats: Jimple, or bytecode.
Jimple can be thought of as an intermediate representation between java and java bytecode.
This task is accomplished using the [Soot](https://github.com/Sable/soot/) compiler framework. If the java file has
been annotated by the programmer with security-levels, DA adds run-time checks to ensure non-interference. This process
is called "instrumentation".

## Example
Consider the following java class:
```
public class Simple {
	public static void main(String[] args) {
		String message = "Hello World, by you";
		message = HelperClass.makeHigh(message);
		System.out.println(message);
	}
}
```
This is a simple test-method which tries to print a message with high security level to a public output (System.out.println is defined as public output).
The result should be an illegal flow exception. And indeed, it is: If we compile the program using DA, the code gets instrumented with dynamic run-time checks
and throws an "IllegalFlowException" if executed.


## Compiling via main:
- Choose your Run Configurations (for example, if we want to compile to instrumented binary, use the RunMainAnalyzerSingleC, which has the following arguments: `-f c --classes main.testclasses.WhileLoopFail --main_class main.testclasses.WhileLoopFail)` 
-  Execute `DynamicAnalyser.src.main.Main.java.` from within Eclipse. This produces a file in 'sootOutput' which is instrumented, meaning
   that it has the original program PLUS more code that checks - on runtime - if the flow is valid or if there is information leakage.
- Go to folder `sootOutput/`
- Run the compiled file using:
```
java -cp .:../bin:../../../DEPS/gradualconstraints_instrumentationsupport_2.11-0.1-SNAPSHOT.jar:../../../DEPS/commons-collections4-4.0/commons-collections4-4.0.jar   main.testclasses.NameOfTest
```
Note on `.:../bin:../../../DEPS/gradualconstraints_instrumentationsupport_2.11-0.1-SNAPSHOT.jar:../../../DEPS/commons-collections4-4.0/commons-collections4-4.0.jar`:
To run the class file successfully, we have to provide more that just the file, because it contains instrumented code: methods and classes 
which also have to be available to the program at runtime!

## Unit tests
There are two kinds of unit tests:
- end to end tests in src.main.testclasses: These get compiled and instrumented, then executed, and observed for desired Exception.
- manually instrumented testfiles, which can be found in tests.analyzer.level2. Here we include the run-time checks "by hand".
All of these tests can be run by executing the tests.testmain.RunAllTests as JUnit test from within Eclipse.

## Brief overview over the source code
To get you started, here is a very brief introduction into the source code:
- main entry point is main.Main
- `analyzer.level1` contains the classes necessary for correct instrumentation, eg contains the code that is responsible for instrumenting an existing java program.
- `analyzer.level2` contains the classes which are executed on runtime. These check if the information flow is valid, and throw appropriate Exceptions (IllegalFlowException) if not.


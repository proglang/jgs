### Setup the Project TaintTracking in Eclipse
- Import [TaintTracking] into Eclipse
- Add polyglott.jar, jasminclasses-2.5.0.jar, sootclasses - 2.5.0.jar and ant.jar es external jar in the project's build path.
- Add "classes" folder of the soot project as external classes to the build path.
- Use Java 1.7 JRE
 
### Setup the Project TestCases in Eclipse 
This project contains the JUnit tests for ... `TODO`
- Import [TestCases] into Eclipse
- [TaintTracking] needs to be on the build path of [TestCases]

.
## How to run
### Run test cases

Use the run configuration AllJUnitTests.launch to run the unit tests.

### Run own project
- [TaintTracking] needs to be on the build path of your own project.
- The project must contain a security.Definition.java file, which extends ALevelDefinition. You can copy the Definition.java from Testcases/security. To implement your own Definition file, see section `TODO` 
- To perform the analysis, please use the lauch configuration [AnalysisRun] and specify the class which should be analyzed (it should contain the main class). For further configuration details, see [configuration] (#configuration)
- For information about possible annotations, see [Annotations] 

[SampleProject] is a simple example how to setup an own project. The Definition.java file is mandatory.
You can use [ManualAnalysis] to start the analysis of this project.

[SampleProject] contains a single file with small examples of annotations. For more examples you might want to take a look at [TestCases].

<a name="configuration"></a>
### Configuration of the launch file
- Command line arguments for the main application are:
  - `-def-classpath` the classpath of the "Definitions"-file, e.g. `./../Testcases/bin`. The "Definitions"-file is the new way to specify the security lattice, etc.
  - `-source-path` path to the source files to be analyzed, e.g. `./../Testcases/src`
  - `-program-classpath` classpath to the code implementing the analysis, e.g. `./bin`
  - `-cp` Soot's classpath parameter. It should be set to the jdk/jre, e.g. `/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/rt.jar`
  - `-main-class` class that contains the main method, e.g. `junitAnalysis.FailArray`
  - `<class>`, e.g. `junitAnalysis.FailArray`. Is this (also) the class to be analyzed, like `-main-class`?

-----

[SootCommandline]: http://www.sable.mcgill.ca/soot/tutorial/usage/ "Soot command-line options"
[Soot]: http://sable.github.io/soot/ "Soot compiler framework"

# JGS: Gradual Security Types for Java

## Summary

A research project with the goal to allow gradual integration of security types for the sequential fragment of Java 

This project is work in progress. Currently, it consists of the following sub-projects:

- `DynamicAnalyzer`: an implementation of dynamic information flow
  control, based on the no-sensitive-upgrade (NSU) policy.
- `GradualConstraints`: Definition and generation of typing
  constraints for Java methods with gradual security types.
- `TaintTracking`: a fairly complete implementation of a security type
  checker for (sequential) Java that supports security-polymorphic
  method signatures. The project `Testcases` contains test cases for
  `TaintTracking`.

## Eclipse Setup 
JGS is developed with the [Eclipse IDE][1] using Java8. Eclipse version "Luna" or later should work. 

1. Create a new workspace for JGS development.
2. Create a classpath variable called `JGS_DEPS` that points to a folder where you want to put the dependencies
3. Download and unpack [Soot framework (version 2.5.0)][2] into the folder pointed to by `JGS_DEPS`
4. Download and unpack [Apache Commons Collections 4.0][4] into the folder pointed to by `JGS_DEPS`
5. Import the projects from this repository

-----

[1]: http://www.eclipse.org/downloads/ "Eclipse Downloads"
[2]: https://www.sable.mcgill.ca/soot/soot_download.html "Download Soot Framework"
[3]: http://www.sable.mcgill.ca/soot/eclipse/updates/index.html "Download Soot Eclipse plugin"
[4]: http://commons.apache.org/proper/commons-collections/download_collections.cgi "Download Apache Commons Collections 4.0"

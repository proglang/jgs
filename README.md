# JGS: Gradual Security Types for Java

## Summary

A research project with the goal to allow gradual integration of security types for the sequential fragment of Java 

This project is work in progress. Currently, it consists of the following sub-projects (which are not really documented yet):

- `DynamicAnalyzer`: an implementation of dynamic information flow
  control, based on the no-sensitive-upgrade (NSU) policy.
- `GradualConstraints`: Definition and generation of typing
  constraints for Java methods with gradual security types.
- `TaintTracking`: a fairly complete implementation of a simple security type
  checker for (sequential) Java. The project `Testcases` contains test cases for
  `TaintTracking`.


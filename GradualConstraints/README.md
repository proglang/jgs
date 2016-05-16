# JGS-check: a type checker for LJGS (ECOOP2016 artifact)

JGS-check is the accompanying artifact to "LJGS: Gradual Security
Types for Object-Oriented Languages". LJGS is a Java-like
language with gradual security typing. It features a constraint based
information flow type system that includes a type dynamic and type
casts. Dynamically typed fragments are liberally accepted by the type
checker and rely on run-time enforcement for security.

JGS-check is a type checker for the subset of Java that corresponds to
the calculus presented in the paper and that implements the constraint
generation and satisfiability checks of LJGS' type system. It's
purpose is to illustrate and substantiate the behavior of our gradual
security type system. It takes a directory of Java source code as
input and reports methods that violate the typing rules. JGS-check is
merely a type checker and does *not* implement code generation.

The ECOOP2016 artifact includes the compiled type checker, the code of
the example section (Section 2) as well additional examples and
testcases that did not fit into the paper. The user should also be
able to check custom code as long as it corresponds the subset of Java
that is covered by LJGS.

([usage and more details and usage here](doc/index.html))

**This repository** (resp. this branch of the `gradual-java`
repository) is a "maintenance branch" for the artifact; it includes
fixes and slight improvements for the original artifact but does not
track the development of the complete JGS implementation, which is
still work in progress.  See Section
[Fixes](#fixes) for details. Go
[here](https://github.com/proglang/gradual-java/tree/master/) for the
repository of the complete implementation.

## Building and Updating the Artifact VM

TODO

Copy the archive into the VM (e.g. by using the *Shared Folder*
feature of VirtualBox) and unpack it there. 

## Fixes

### Configurable Security Domain

TODO

### Support for `java.lang.String`

TODO

### Parsing of Constraint Syntax

TODO

### Detecting Context Casts

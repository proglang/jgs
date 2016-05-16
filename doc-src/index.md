# Abstract

JGS-check is the accompanying artifact to "LJGS: Gradual Security
Types for Object-Oriented Languages".  LJGS is a Java-like language
with gradual security typing. It features a constraint based
information flow type system that includes a type dynamic and type
casts. Dynamically typed fragments are liberally accepted by the type
checker and rely on run-time enforcement for security. 

JGS-check is a type checker for the subset of Java that corresponds to
the calculus presented in the paper and that implements the constraint
generation and satisfiability checks of LJGS' type system.  It's
purpose is to illustrate and substantiate the behavior of our gradual
security type system. It takes a directory of Java source code as
input and reports methods that violate the typing rules. JGS-check is
merely a type checker and does *not* implement code generation.

The submission archive includes the compiled type checker, the code of
the example section (Section 2) as well additional examples and
testcases that did not fit into the paper. The user should also be
able to check custom code as long as it corresponds the subset of Java
that is covered by LJGS.

The source code of JGS-check is available at
[github](https://github.com/luminousfennell/gradual-java/tree/master/GradualConstraints).

# Setup

The directory `JGS-check-ecoop2016` contains a VirtualBox VM that has
JGS-check installed. After starting the VM, open the folder
`ecoop2016-ae-submission` and press `<F4>` to open a terminal in
it. The tool is run by exectuing `make`. (cf
[Running the provided examples](#running-the-provided-examples)).

# Check procedure

In a first phase JGS-check first checks the class hierarchy: The
signature of an overriding method `m1` should always *refine* the
signature of the "super-"method `m2`. That is, `m1`'s signatures has
to be more restrictive than that `m2`. JGS-check checks each
overriding method of the class hierarchy, printing an error message
when appropriate.

The second phase generates constraints and effects for method bodies
and checks two properties:

1. Satisfiability of the generated constraints.

2. If the generated constraints and effects refine the constraints and
   effects given in the method signature. This check is similar to the
   check between overriding- and super-method in the hierarchy check
   of the first phase.

Each method of the sourcetree is checked and error messages are
printed if a method is ill-typed.


# Running the provided examples 

To run the supplied examples, execute 

~~~
make
~~~

at the root of the submission archive.  The result should be a set of
generated text files with names `JGSTestclasses_XXX.output.txt`, where
`XXX` corresponds to the name of the particular example.  The text
files contain the output of JGS-check.


- **JGSTestclasses/ExamplesFromPaper**: these are the examples from
  Section 2 of the paper. They should not contain type errors. Like in
  the paper, they contain a few commented lines that yield type-errors
  when uncommented.
- **JGSTestclasses/ClassHierarchies**: this sourcetree contains
  classes that exercise the class hierarchy check (cf. Sec. 4.4, Def
  5). Comments on the methods describe the expected output.
- **JGSTextclasses/MethodBodies**: this examples contains a single
  class that demonstrates different aspects of method body
  typing. Again, comments on the methods explain the expected output.

## JGS Annotation and cast syntax

JGS-check security properties are specified using standard Java
annotations and special method calls (both of which are defined in the
`JGSSupport` directory).

For security annotations for fields and signatures of
methods are specified by the following Java Annotation classes.

- `@Sec`: specifies the security type of a field. It takes a **String** as
  argument. Security types recognized are:
    - `HIGH` static high-security level
    - `LOW` static low-security level
    - `?` the dynamic type
    - `pub` the public type
- `@Constraints`: specifies the constraints of a method. It takes an
  **array of Strings** as an argument. Each array element is a
  constraint of the form 
    - *constraint-element* `<=` *constraint-element*, denoting a less-than-or-equal constraint or
    - *constraint-element* `~` *constraint-element*, denoting a compatibility constraint

    The constraint elements can have the form of
    - a security type, e.g. `HIGH`
    - the return symbol `@ret`
    - a parameter, `@0`, `@1`, `@2`, ... parameters are given
    positionally and start with index 0

- `@Effects`: specifies the effects of a method. It takes an **array
  of Strings** as argument. Each element denotes a security type, e.g. `HIGH`.

JGS-check recognizes special static method calls as casts.
The methods are specified `de.unifreiburg.cs.proglang.jgs.support.Casts`. 

- Value casts are identity methods of the form `Casts.castXxToYy`
  where `Xx` denotes the source type and `Yy` denotes the destination
  type of the cast.
- Context casts start with a noop method of the form
  `Casts.castCxXxToYy` and end with a call to `Casts.castCxEnd`. These
  calls have to be properly nested.

The methods in `JGSTestclasses/MethodBodies` illustrate the usage of casts.

## Output format

In the output, methods and fields are specified in an unambiguous,
human-readable way in between angle-bracket. For example, the field
and method of the class

~~~ { .language-java }
package pkg.subpkg;
class C {
  Object anObject;
  Integer getAnInt(Object a);
}
~~~

are represented as

    <pkg.subpkg.C: java.lang.Object anObject>

and 

    <pkg.subpkg.C: java.lang.Integer getAnInt(java.lang.Object)>

respectively.

Refinement errors in the class hierarchy are displayed by 

- listing the constraints of the super-method and the sub-method
- providing a counterexample, that is an assignment for symbols that
  satisfied the super-method constraints, but not the sub-method constraints.
- listing a subset of constraints of the sub-method that conflict with the counterexample
- listing a set of effects that are missing from the signature of the super-method

For example, for a sub-method `m1` that overrides a super-method `m2`

~~~ { .language-java }
@Constraints({"@0 <= @ret", "@1 <= @ret"}) 
@Effects({"LOW"})
int m1(int i, int j) { ... }

@Constraints({"@0 <= @ret"}) 
int m2(int i, int j) { ... }

~~~

yields an error like

~~~
super method constraints: {@0 <= @ret}
sub method constraints: {@0 <= @ret, @1 <= @ret}
counterexample: {@0=LOW, @1=HIGH, @ret=LOW}
conflicts: {@1 <= @ret}
missing effects: {LOW}
~~~

Refinement errors in method bodies are represented similarly; the
signature takes the place of the super-method while the generated
constraints of the body take the place of the sub-method.

Unsatisfiable constraints in method bodies are reported by
reconstructing the illegal information that led to them, for example

~~~ { .language-java }
class C {
 @Sec("HIGH")
 int highField;
 @Sec("LOW")
 int lowField;

 void m() {
  highField = lowField;
 }
}
~~~

yields the message

~~~
<C : int highField> (HIGH) flows into <C : int lowField> (LOW)
~~~


# Running JGS-check on your own code

To run `JGS-check` on custom sources execute the command

~~~
java -jar GradualConstraints.jar <path/to/sources>
~~~

where `<path/to/sources>` should point to the root of the package
hierarchy for the Java sources to be checked. Alternatively, you may
just modify the existing sourcetrees and use ``make``

## External methods

If you want to use methods or fields from the Java library, you need
to specify their security signatures in the file

    JGSSupport/external-signatures.json

It already contains some entries (e.g., for `Integer.valueOf`) that
should be self-explanatory.

## Usability Limitations

At it's current state, the implementation has some minor limitations
with respect to usability and convenience that are decribed in the
following.

## Parse errors in constraints

Unfortunately, the constraint parser currently does not give good
error messages. Please be careful in specifying constraints.

### `java.lang.String` is broken

Due to problems in the underlying analysis framework
[Soot](https://sable.github.io/soot/) checking sources that make use
of `java.lang.String` often crash JGS-check. The exception thrown by
Soot indicates that there is a problem parsing the
`java.lang.CharSequence` classfile. The problem may be related to
running Soot on a Java8 VM. We are currently underway to port
JGS-check to Java7.

### Auto-boxing in context casts

The current way of specifying context casts (using the `castCxXtoY`
and `castCxEnd` methods) has problems with autoboxing which can yield
false type errors. Please do not rely on auto-boxing in
between context cast begin and end.

### Changing the security lattice requires recompilation

The security lattice is defined by classes inside
`GradualConstraints.jar` and currently is not externally
configurable. This limitation is however not conceptual: internally
the security domain is an implementation of the `de.unifreiburg.cs.proglang.jgs.constraints.SecDomain` interface:

~~~ { .language-java }
public interface SecDomain<Level> {
   Level bottom(); 
   Level top();
   Level lub(Level l1, Level l2);
   Level glb(Level l1, Level l2);
   boolean le(Level l1, Level l2);

   AnnotationParser<Level> levelParser();

   /**
    * Optional method for enumerable domains
    * @return A stream of all the security levels.
    */
   default Stream<Level> enumerate() {
       throw new RuntimeException("The security domain is not enumerable.");
   }
}

~~~

The security domain can be switched in the source of the main
class `de/unifreiburg/cs/proglag/jgs/JgsCheck.scala`. The spot is marked by the comment:

~~~
        /****************************
         * Configuration of security lattice
         ****************************/
~~~



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

([usage and more details here](doc/index.html))

**This repository** (resp. this branch of the `gradual-java`
repository) is a "maintenance branch" for the artifact; it includes
fixes and slight improvements for the original artifact but does not
track the development of the complete JGS implementation, which is
still work in progress.  See Section
[Fixes](#fixes) for details. Go
[here](https://github.com/proglang/gradual-java/tree/master/) for the
repository of the complete implementation.

## Building and Updating the Artifact VM

### Requirements

Building JGS-check from source requires the following components installed on the compiling system (host system)

- [Java-7 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html). A Java-8 SDK will most probably **not work**.
- [sbt version 0.13.11](http://www.scala-sbt.org/download.html). Later versions of sbt may work. 

### Downgrading the VM to Java-7

- Set up an empty VirtualBox [Shared Folder](https://www.virtualbox.org/manual/ch04.html#sharedfolders) called `vm-data` (e.g., `~/Desktop/vm-data`)
- Download a Java-7 SDK **.tar.gz** archive for **Linux x64** [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)) and place it in the Shared Folder
- Start the original VirtualBox VM image that is available as an ECOOP 2016 artifact
- Open a super-user terminal (e.g., by opening the "start menu", by
  clicking the circle-shaped icon on the bottom left, and selecting
  `System Tools > Super User Mode - Terminal`). The password is `toor`.
- To free up disk space, delete the folder `/home/guest/Desktop/ecoop2016-ae-submission` 
````
> rm -rf /home/guest/Desktop/ecoop2016-ae-submission
````
- Mount the Shared Folder using the following commands
```
> mkdir -p /media/sf_vm-data
> mount -o uid=1000 -t vboxsf vm-data /media/sf_vm-data
```
- Close the super-user terminal and open a regular terminal, (e.g., by selecting `Accessories > LXTerminal` from the start menu)
- Unpack the Java-7 SDK to the Desktop
```
> cd Desktop
> tar xvf /media/sf_vm-data/<name-of-downloaded-sdk>.tar.gz
```
- Update PATH in `~/.bashrc` to include the JDK binaries. 
    - run `leafpad ~/.bashrc`
    - replace the last line of the file saying

	````
    export PATH=${HOME}/Desktop/ecoop2016-ae-submission/jdk1.8.0_92/bin:$PATH
	````

	with 

	````
    export PATH=${HOME}/Desktop/jdk1.7.0_79/bin:$PATH
	````

	**Attention**: Replace `jdk1.7.0_79` with the exact directory name
      that was created during unpacking in the previous step.
	- save the file (select `File > Save` in the menu), close leafpad and LXTerminal

- To check if everything went well, open a new terminal and type 

  ```` 
  > java -version
  ````

  The output should be something similar to

  ````
  java version "1.7.0_79"
  Java(TM) SE Runtime Environment (build 1.7.0_79-b15)
  Java HotSpot(TM) 64-Bit Server VM (build 24.79-b02, mixed mode)
  ````

### Building JGS-check 

- Make sure `sbt` is in your path. 
- Clone the `ecoop2016-ae` branch of this repository, e.g. from a
  Linux terminal, run

````
> cd ~/Desktop
> git clone -b ecoop2016-ae https://github.com/luminousfennell/gradual-java.git
````
- Enter the `GradualConstraints` directory:

````
> cd gradual-java/GradualConstraints
````
- Run `sbt packageForVM` (initially, this will take quite some
  time). This command will produce a zip archive called
  `ecoop2016-artifact-update.zip`.

### Updating JGS-check in the VM

- Copy the archive `ecoop2016-artifact-update.zip` produced in the
previous step into the VM (e.g. by using the *Shared Folder* feature
of VirtualBox) 
- In the artifact VM, unpack the archive, e.g. by starting a terminal
  and running

````
> cd ~/Desktop
> unzip /media/sf_vm-data/ecoop2016-artifact-update.zip 
> cd ecoop2016-artifact-update
````

- Now, you should be able to run the examples by running `make`.

## Fixes

### Configurable Security Domain

Although the functionality was already implemented in principle, the
artifact submitted to ECOOP2016 had no user-friendly way to specify a
custom security domain (i.e., the lattice of security levels): only
the usual two point lattice `{LOW, HIGH}` was predefined. To change
the domain, the user has to write a custom
`de.unifreiburg.cs.proglang.jgs.constraints.SecDomain` implementation
and recompile JGS-check.

It is now possible to define a custom security domain using
[YAML](http://yaml.org/) configuration files (YAML is a human-friendly superset of JSON, so you may use JSON syntax if you are not familiar with it).

#### Specifying Security Domains

The configuration file for a security domain contains an object with two attributes:
1. `level`: An array of the *security levels* (strings)
2. `lt-edges`: An array of 2-element arrays specifying set of "less-than" relationships between security levels. 

JGS-check constructs the ordering of the lattice by taking the
reflexive-transitive closure of `lt-egdes`. Construction will fail if
the specification does not form a proper lattice (e.g., if the
ordering does not admit a least-upper bound for every combination of
levels).

Example (`secdomain-alice-bob-charlie.ya ml`):

````
# This is the alice-bob-charlie lattice. 
#
# Getting greater from left to right:
#
#           +->  alice  -+
#           |            |
#   bottom -+->  bob   --+-> top
#           |            |
#           +-> charlie -+

levels:
  - bottom
  - alice
  - bob
  - charlie
  - top

lt-edges:
  - [ bottom , alice ]
  - [ bottom , bob ]
  - [ bottom , charlie ]
  - [ alice , top ]
  - [ bob, top ]
  - [ charlie, top ]
````

#### Generic casts

Previously every possible type conversion had to be specified
`cast-methods.json` and corresponding dummy methods had to be
implemented in `de.unifreiburg.cs.proglang.jgs.support.Casts` (in
project `JGSSupport`).

As this becomes cumbersome for lattices of more than two or three
elements, JGS-check now supports the following *generic cast methods*: 

```
// for value casts
public static <T> T cast(String conversion, T x) 

// for context casts
public static void castCx(String conversion)
public static void castCxEnd()
```

These methods are provided by
`de.unifreiburg.cs.proglang.jgs.support.Casts` and
`casts-methods.yaml`.  JGS-check interprets the String argument
`conversion`, which has to be a String literal, at type-checking time
as a string conversion. The syntax for convertyping from type `a` to
type `b` is `a ~> b`.

Example:

```
// convert from type dynamic to alice
int forAlice = Casts.cast("? ~> alice", this.dynamicField);
```

#### Running JGS-check with a Custom Security Domain

Add ``--security-domain <custom-security-domain-file.yaml>
--generic-casts`` to the command line when calling JGS-Check.

For example, you can run the test class for the alice-bob-charlie
domain in sbt like so:

```
run --generic-casts --security-domain secdomain-alice-bob-charlie.yaml --support-classes JGSSupport/target/scala-2.11/jgssupport_2.11-0.1-SNAPSHOT.jar JGSTestclasses/PersonalDomain/src
```


### Support for `java.lang.String`

The original ECOOP2016 submission was unable to type-check most
programs that used `java.lang.String`. This problem was (probably)
caused by an incompatibility of the Soot framework when running under
Java-8.

JGS-check now runs under Java-7 which fixes the problem; thus programs
using `java.lang.String` should now work. Also, the examples in
`JGSTestclasses/ExamplesFromPaper` now use `java.lang.String`.

### Parsing of Constraint Syntax

TODO

### Detecting Context Casts

TODO

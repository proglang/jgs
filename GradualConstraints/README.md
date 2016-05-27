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

TODO

### Support for `java.lang.String`

TODO

### Parsing of Constraint Syntax

TODO

### Detecting Context Casts

TODO

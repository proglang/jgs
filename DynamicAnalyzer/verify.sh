#!/bin/bash

JGS_DEPS="../../../dependencies"

if [ "$2" = "-v" ]
then

	echo "Verify file \"$1\""

  	cd sootOutput
	java -cp .:bcel-5.2.jar:../bin/ org.apache.bcel.verifier.Verifier $1
	cd ..

	echo "Verification completed"

elif [ "$2" = "-e" ]
then

	echo "Execute file \"$1\""

	cd sootOutput
	java -cp .:../bin/:$JGS_DEPS/commons-collections4-4.0/commons-collections4-4.0.jar:$JGS_DEPS/instrumentationsupport_2.11-0.1-SNAPSHOT.jar  $1
	cd ..

	echo "Execution completed"

elif [ "$2" = "-b" ]
then

	echo "Show bytecode of file \"$1\""

  	cd sootOutput
	javap -c $1
	cd ..

	echo "End of bytecode"
elif [ "$1" = "-j" ]
then 
	echo "Compile Jimple files"

	java -cp $JGS_DEPS/soot-2.5.0/lib/soot-2.5.0.jar soot.main.Main -cp sootOutput/:./bin:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/jce.jar:/usr/local/java/java170/jre/lib/rt.jar -f c -process-dir sootOutput -d sootOutput2 -src-prec J

	echo "End of Jimple file compilation"

else 
    	echo "Usage: ./verify.sh ClassName -option"
	echo "Options:"
	echo "-v to verify the bytecode" 
	echo "-e to execute the class"
	echo "-b to show the bytecode"
	echo "-j to compile Jimple Files"
fi


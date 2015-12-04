#!/bin/sh

# This file should be located at the folder sootOutput. bcel-5.2.jar Should also be in sootOutput

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
	java -cp .:../bin/:../../../dependencies/commons-collections4-4.0/commons-collections4-4.0.jar  $1
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

	java -cp ../../dependencies/soot-2.5.0/lib/soot-2.5.0.jar soot.Main -cp sootOutput/:./bin:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/jce.jar:/usr/local/java/java170/jre/lib/rt.jar -f c -process-dir sootOutput -d sootOutput2 -src-prec J

	echo "End of Jimple file compilation"

else 
    	echo "Usage: ./verify.sh ClassName -v (for verify) OR -e (for execute) OR -b (to show bytecode)"
fi


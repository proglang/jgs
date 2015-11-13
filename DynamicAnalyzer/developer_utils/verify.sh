#!/bin/sh

# This file should be located at the folder sootOutput. bcel-5.2.jar Should also be in sootOutput

if [ "$2" = "-v" ]
then

	echo "Verify file \"$1\""

	java -cp .:bcel-5.2.jar:../bin/ org.apache.bcel.verifier.Verifier $1

	echo "Verification completed"

elif [ "$2" = "-e" ]
then

	echo "Execute file \"$1\""

	java -cp .:../bin/  $1

	echo "Execution completed"

elif [ "$2" = "-b" ]
then

	echo "Show bytecode of file \"$1\""

	javap -c $1

	echo "End of bytecode"

else 
    	echo "Usage: ./verify.sh ClassName -v (for verify) OR -e (for execute) OR -b (to show bytecode)"
fi


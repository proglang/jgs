EXAMPLE_CLASS=$1
# CLASSPATH_SECDOMAIN=UserDefinedSecurityDomain/target/scala-2.11/classes
CLASSPATH_SECDOMAIN=LMHSecurityDomain/target/scala-2.11/classes
CLASSPATH_COMPILE=JGSTestclasses/Demo/target/scala-2.11/classes:JGSTestclasses/Scratch/target/scala-2.11/classes:JGSSupport/target/scala-2.11/classes:${CLASSPATH_SECDOMAIN}:DynamicAnalyzer/target/scala-2.11/classes
CLASSPATH_RUN=.:${CLASSPATH_SECDOMAIN}:DynamicAnalyzer/target/scala-2.11/classes:GradualConstraints/InstrumentationSupport/target/scala-2.11/classes:lib/commons-collections4-4.0.jar:JGSSupport/target/scala-2.11/classes
OUTPUT_DIR=out-instrumented
OUTPUT_JIMPLE=out-original

set -e

[[ x$JAVA_HOME = x ]] && echo "Please specify JAVA_HOME" && exit -1

echo ==========================
echo CHECK AND INSTRUMENT
echo ==========================

# $JAVA_HOME/bin/java -jar lib/soot-2.5.0.jar -soot-classpath $CLASSPATH_COMPILE:$JAVA_HOME/lib/openjdk/jre/lib/rt.jar -main-class $EXAMPLE_CLASS -d $OUTPUT_JIMPLE $EXAMPLE_CLASS -f J

JGS_SECDOMAIN_CLASSES=${CLASSPATH_SECDOMAIN} sbt "run -m $EXAMPLE_CLASS -scp $CLASSPATH_SECDOMAIN -cp $CLASSPATH_COMPILE -o $OUTPUT_DIR $2"


echo ==========================
echo RUN $EXAMPLE_CLASS
echo ==========================

$JAVA_HOME/bin/java -cp $OUTPUT_DIR:$CLASSPATH_RUN $EXAMPLE_CLASS

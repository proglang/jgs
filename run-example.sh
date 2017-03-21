EXAMPLE_CLASS=jgstestclasses.SimpleCasts
CLASSPATH_COMPILE=JGSTestclasses/Scratch/target/scala-2.11/classes:JGSSupport/target/scala-2.11/classes:DynamicAnalyzer/target/scala-2.11/classes
CLASSPATH_RUN=.:DynamicAnalyzer/target/scala-2.11/classes:GradualConstraints/InstrumentationSupport/target/scala-2.11/classes:lib/commons-collections4-4.0.jar:JGSSupport/target/scala-2.11/classes
OUTPUT_DIR=soot/Output
export JAVA_HOME=~/opt/openjdk7

echo ==========================
echo CHECK AND INSTRUMENT
echo ==========================

sbt "run -m $EXAMPLE_CLASS -cp $CLASSPATH_COMPILE -o $OUTPUT_DIR"

echo ==========================
echo RUN $EXAMPLE_CLASS
echo ==========================

$JAVA_HOME/bin/java -cp $OUTPUT_DIR:$CLASSPATH_RUN $EXAMPLE_CLASS

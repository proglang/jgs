
JAVAC=javac
JAVA=java

TESTCLASSES = "JGSTestclasses/ExamplesFromPaper" "JGSTestclasses/MethodBodies" "JGSTestclasses/ClassHierarchy"

.PHONY: all build clean $(TESTCLASSES)

all: $(TESTCLASSES)

$(TESTCLASSES): build
	$(JAVA) -jar GradualConstraints.jar $@/src > $(subst /,_,$@).output.txt

build:
	mkdir -p JGSSupport/bin
	$(JAVAC) -d JGSSupport/bin JGSSupport/src/de/unifreiburg/cs/proglang/jgs/support/*.java
	mkdir -p JGSTestclasses/bin
	find JGSTestclasses/ -name '*.java' | xargs $(JAVAC) -cp JGSSupport/bin  -d JGSTestclasses/bin

clean: 
	rm -rf JGSSupport/bin
	rm -rf JGSTestclasses/bin

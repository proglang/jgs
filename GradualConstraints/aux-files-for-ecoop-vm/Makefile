
JAVAC=javac
JAVA=java

TESTCLASSES = "JGSTestclasses/ExamplesFromPaper" "JGSTestclasses/MethodBodies"  "JGSTestclasses/ClassHierarchy"

GRADUAL_CONSTRAINTS = $(JAVA) -jar GradualConstraints.jar --support-classes jgs-support.jar

.PHONY: all build clean JGSTestclasses/PersonalDomain $(TESTCLASSES)

all: $(TESTCLASSES) JGSTestclasses/PersonalDomain

JGSTestclasses/PersonalDomain: build
	$(GRADUAL_CONSTRAINTS) --generic-casts --security-domain secdomain-alice-bob-charlie.yaml $@/src > $(subst /,_,$@).output.txt

$(TESTCLASSES): build
	$(GRADUAL_CONSTRAINTS) $@/src > $(subst /,_,$@).output.txt

build:
	mkdir -p JGSTestclasses/bin
	find JGSTestclasses/ -name '*.java' | xargs $(JAVAC) -cp jgs-support.jar  -d JGSTestclasses/bin

clean: 
	rm -rf JGSSupport/bin
	rm -rf JGSTestclasses/bin

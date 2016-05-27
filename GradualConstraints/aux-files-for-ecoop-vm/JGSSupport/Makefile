.PHONY: clean all

JAVAC = $(HOME)/opt/openjdk7/bin/javac
BIN = bin
SRC = src

all: 
	mkdir -p $(BIN)
	find $(SRC) -name '*.java' | xargs $(JAVAC) -d $(BIN)

clean:
	rm -rf $(BIN)

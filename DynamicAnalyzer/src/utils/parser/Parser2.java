package utils.parser;

import utils.exceptions.IllegalArgumentsException;

public class Parser2 {

	public static String[] getSootOptions(String[] args) {
		// one arg: use as both path and mainClass, output CLASS, to current folder
		if (args.length == 1) {
			checkValidPathStart(args[0], "Must supply path!");
			return new String[] {"-f", "c", "-main-class", args[0], args[0], "-d", ""};
		} else {
			return args;
		}
	}
	
	private static void checkValidPathStart(String s, String m) {
		if (s.startsWith("-")) {
			throw new IllegalArgumentsException(m);
		}
	}

}

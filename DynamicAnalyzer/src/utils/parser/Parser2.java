package utils.parser;

import java.util.ArrayList;

import utils.exceptions.IllegalArgumentsException;

public class Parser2 {
	
	public static void printHelp() {
		System.out.println(" ====== USAGE =======");
		System.out.println(" ");
		System.out.println(" java DynamicAnalyzer path/to/main \t\t Compiles file at path/to/main and sets it as mainfile");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" ");
	}

	public static String[] getSootOptions(String[] args) {
		
		
		ArrayList<String> sootOptions = new ArrayList<String>();
		sootOptions.add("-f");
		sootOptions.add("c");
		
		// one arg: use as both path and mainClass, output CLASS, to current folder
		if (args.length == 1) {
			checkIfStartsWithDash(args[0], "Must supply path to mainClass!");
			if (args[0] == "help") { printHelp(); }
			String[] toAdd = new String[] {"-main-class", args[0], args[0], "-d", System.getProperty("user.dir")};
			for (String string : toAdd) { sootOptions.add(string);}
		}  else if (args.length > 1) {
			checkIfStartsWithDash(args[0], "First argument must be the mainClass!");
		} 
		return sootOptions.toArray(new String[0]);
	}
	
	private static void checkIfStartsWithDash(String s, String m) {
		if (s.startsWith("-")) {
			throw new IllegalArgumentsException(m);
		}
	}

}

package util.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.*;

import util.exceptions.IllegalArgumentsException;

public class Parser2 {
	
	public static void printHelp() {
		System.out.println(" ====== USAGE =======");
		System.out.println(" ");
		System.out.println(" java DynamicAnalyzer mainclass -p path/to/mainclass -j -o path/to/output");
	}

	public static String[] getSootOptions(String[] args) {
		
		
		checkIfStartsWithDash(args[0], "Must supply mainClass as first argument!");
		if (args[0] == "help") { printHelp(); }
		
		Options options = new Options();

        Option pathToMainclass = new Option("p", "path", true, "Optinal: Path to MainClass");
        pathToMainclass.setRequired(false);
        options.addOption(pathToMainclass);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(false);
        options.addOption(output);
        
        Option format = new Option("j", "jimple", false, "output as Jimple instead of as compiled class");
        format.setRequired(false);
        options.addOption(format);
        
        CommandLineParser parser = new DefaultParser();
        
        CommandLine cmd;
		try {
			
			cmd = parser.parse(options, args);
	        
	        String[] template = new String[]{"-f","c", "-main-class", args[0], 
					args[0], "--d", System.getProperty("user.dir")};
	        
	        if (cmd.hasOption("j")) {
	        	template[1] = "J";
	        }
	        
	        if (cmd.hasOption("p")) {
	        	template[4] = cmd.getOptionValue("p").endsWith("/") ? cmd.getOptionValue("p") + args[3] : cmd.getOptionValue("p") + "/" + args[3];
	        }
	        
	        if (cmd.hasOption("o")) {
	        	template[6] = cmd.getOptionValue("p");
	        }
	        		
			return template;
			
		} catch (ParseException e) {
			throw new IllegalArgumentsException(e.getMessage());
		}
	}
	
	private static void checkIfStartsWithDash(String s, String m) {
		if (s.startsWith("-")) {
			throw new IllegalArgumentsException(m);
		}
	}

}

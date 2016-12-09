package utils.parser;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import utils.exceptions.IllegalArgumentsException;
import utils.exceptions.InternalAnalyzerException;

/**
 * Parses the arguments from cmd into soot format. Soot format is like this:
 * [-f, c, -main-class, 
 * 	/Users/NicolasM/Dropbox/hiwi/progLang/jgs/DynamicAnalyzer/testing_external,
 *  main.testclasses.NSUPolicy1, --d, /Users/NicolasM/Dropbox/hiwi/progLang/jgs/DynamicAnalyzer]
 * @author Nicolas Müller
 *
 */
public class ArgParser {

	public static void printHelp() {
		System.out.println(" ====== POSSIBLE FLAGS =======");
		System.out.println("-j: Compile to Jimple. ");
		System.out.println("-p: Set a different input file ");
		System.out.println("-o: Set output folder. ");
		System.out.println("\nExamples:");
		System.out.println("main.testclasses.NSUPolicy1");
		System.out.println("main.testclasses.NSUPolicy1 -j");
		System.out.println("main.testclasses.NSUPolicy1 -o /Users/NicolasM/myOutputFolder");
		System.out.println("main.testclasses.NSUPolicy1 -p /Users/NicolasM/Downloads/Users/NicolasM/Downloads");
		System.out.println("main.testclasses.NSUPolicy1 -p /Users/NicolasM/Downloads/Users/NicolasM/Downloads -j");
		System.out.println("main.testclasses.NSUPolicy1 -f externalTest/main/utils/SomeClass.java");
		System.out.println("main.testclasses.NSUPolicy1 -f externalTest/main/utils/SomeClass.java some/absolut/path/to/additionalFile.java");
	}
	
	/**
	 * Convert a given path to canonical path, regardless whether input is relative or absolute
	 * @param s			relative or absolute path
	 * @return			equivalent canonical path
	 */
	private static String toAbsolutePath(String s) {
		File path = new File(s);

		if (path.isAbsolute()) {
			try {
				return path.getCanonicalPath();
			} catch (IOException e) {
				throw new InternalAnalyzerException(e.getMessage());
			} 
		} else {
			File parent = new File(System.getProperty("user.dir"));
			File fullPath = new File(parent, s);
			try {
				return fullPath.getCanonicalPath();
			} catch (IOException e) {
				throw new InternalAnalyzerException(e.getMessage());
			} 
		}
		
	}
	
	/**
	 * Parse command line arguments into a soot-friendly format
	 * @param args
	 * @param pathArgs
	 * @return
	 */
	public static String[] getSootOptions(String[] args, ArrayList<String> pathArgs) {

		if (args[0].startsWith("-")) {
			throw new IllegalArgumentsException("first argument must be the main Class!");
		}

		Options options = new Options();

		Option pathToMainclass = new Option("p", "path", true,
				"Optinal: Path to MainClass");
		pathToMainclass.setRequired(false);
		options.addOption(pathToMainclass);

		Option output = new Option("o", "output", true, "output file");
		output.setRequired(false);
		options.addOption(output);

		Option format = new Option("j", "jimple", false,
				"output as Jimple instead of as compiled class");
		format.setRequired(false);
		options.addOption(format);
		
		Option filesToAdd = new Option("f", "files", true, "add files to instrumentation process");
		filesToAdd.setRequired(false);
		filesToAdd.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(filesToAdd);

		CommandLineParser parser = new DefaultParser();

		CommandLine cmd;
		try {

			cmd = parser.parse(options, args);

			// construct the template string
			String[] template = new String[cmd.hasOption("f") ? 7 + cmd.getOptionValues("f").length : 7];
			template[0] = "-f";
			template[1] = "c";
			template[2] = "-main-class";
			template[3] = "placeholder_mainclass";
			template[4] = "filesToProcess_firstFile (which will be the mainclass file)";
			
			if (cmd.hasOption("f")) {
				String[] additionalFiles = cmd.getOptionValues("f");
				for (int i = 0; i < additionalFiles.length; i++) {
					template[5 + i] = additionalFiles[i];
				}
			}
			template[template.length - 2] = "--d";
			template[template.length - 1] = "placeholder output path";

			// case no flag
			template[3] = args[0]; // set mainclass
			template[4] = args[0];
			template[template.length - 1] = System.getProperty("user.dir");

			// case j flag
			if (cmd.hasOption("j")) {
				template[1] = "J";
			}

			// case p flag
			if (cmd.hasOption("p")) {
				String[] classPathList = cmd.getOptionValues("p");
				for (String path : classPathList) {
					pathArgs.add(toAbsolutePath(path));
				}
			}

			// case o flag
			if (cmd.hasOption("o")) {
				String outputFolder = cmd.getOptionValue("o");
				template[template.length - 1] = toAbsolutePath(outputFolder);
			}
			
			return template;

			// if illegal input
		} catch (ParseException e) {
			throw new IllegalArgumentsException(e.getMessage());
		}
	}
}

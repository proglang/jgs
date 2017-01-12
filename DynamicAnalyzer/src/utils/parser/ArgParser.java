package utils.parser;

import com.sun.org.apache.xpath.internal.Arg;
import org.apache.commons.cli.*;
import utils.exceptions.IllegalArgumentsException;
import utils.exceptions.InternalAnalyzerException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        // locals variables to hold parsing results
        String mainclass = ArgumentContainer.VALUE_NOT_PRESENT;
        String outputFormat = ArgumentContainer.VALUE_NOT_PRESENT;
        String outputFolder = ArgumentContainer.VALUE_NOT_PRESENT;
        String pathToMainclass = ArgumentContainer.VALUE_NOT_PRESENT;
        List<String> additionalFiles = new ArrayList<>();

        // flags
        final String MAINCLASS_FLAG = "m";
        final String ADD_TO_CLASSPATH_FLAG = "p";
        final String OUTPUT_FOLDER_FLAG = "o";
        final String JIMPLE_FLAG = "j";
        final String ADDITIONAL_FILES_FLAG = "f";

        // ======== PREPARE OPTIONS =========
		Options options = new Options();

        Option mainopt = new Option(MAINCLASS_FLAG, "mainclass", true,
                "mainclass");
        mainopt.setRequired(true);
        options.addOption(mainopt);

		Option pathToMainc = new Option(ADD_TO_CLASSPATH_FLAG, "path", true,
				"Optional: Set path to MainClass");
		pathToMainc.setRequired(false);
		options.addOption(pathToMainc);

		Option output = new Option(OUTPUT_FOLDER_FLAG, "output", true,
                "Optional: Set output folder");
		output.setRequired(false);
		options.addOption(output);

		Option format = new Option(JIMPLE_FLAG, "jimple", false,
				"Optional: Output as Jimple instead of as compiled class");
		format.setRequired(false);
		options.addOption(format);
		
		Option filesToAdd = new Option(ADDITIONAL_FILES_FLAG, "files", true,
                "Optional: add additional files to instrumentation process");
		filesToAdd.setRequired(false);
		filesToAdd.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(filesToAdd);

		CommandLineParser parser = new DefaultParser();

		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);

			// case j flag
			if (cmd.hasOption(JIMPLE_FLAG)) {
                outputFormat = "J";
			} else {
			    outputFormat = "c";
            }

			// case p flag
			if (cmd.hasOption(ADD_TO_CLASSPATH_FLAG)) {
				for (String path : cmd.getOptionValues(ADD_TO_CLASSPATH_FLAG)) {
                    addToClassPath.add(path);
				}
			}

			// case f flag
            if (cmd.hasOption(ADDITIONAL_FILES_FLAG)) {
			    for (String file: cmd.getOptionValues(ADDITIONAL_FILES_FLAG)) {
			        additionalFiles.add(file);
                }
            }

			// case o flag
			if (cmd.hasOption(OUTPUT_FOLDER_FLAG)) {
				outputFolder = cmd.getOptionValue(OUTPUT_FOLDER_FLAG);
			} else {
			    outputFolder = ArgumentContainer.VALUE_NOT_PRESENT;
            }

			
			return template;

			// if illegal input
		} catch (ParseException e) {
			throw new IllegalArgumentsException(e.getMessage());
		}
	}
}

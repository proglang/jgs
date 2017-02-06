package utils.parser;

import org.apache.commons.cli.*;
import utils.exceptions.InternalAnalyzerException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parses the arguments from cmd into soot format. Soot format is like this:
 * [-f, c, -main-class, 
 * 	/Users/NicolasM/Dropbox/hiwi/progLang/jgs/DynamicAnalyzer/testing_external,
 *  testclasses.NSUPolicy1, --d, /Users/NicolasM/Dropbox/hiwi/progLang/jgs/DynamicAnalyzer]
 * @author Nicolas Müller
 *
 */
public class ArgParser {

	private static void printHelp() {
		System.out.println(" ====== HELP ======= ");
		System.out.println("-m: Set mainclass ");
        System.out.println("-j: Compile to Jimple. ");
		System.out.println("-p: Set classpath for soot. Use to add files in several directories. ");
		System.out.println("-o: Set output folder. ");
        System.out.println("-f: Add additional files to be processed (like testclasses.SomeHelperClassForMain)");
		System.out.println("\nExamples:");
		System.out.println("-m testclasses.NSUPolicy1");
		System.out.println("-m testclasses.NSUPolicy1 -j");
		System.out.println("-m testclasses.NSUPolicy1 -o /Users/NicolasM/myOutputFolder");
		System.out.println("-m testclasses.NSUPolicy1 -p /Users/NicolasM/Downloads/Users/NicolasM/Downloads");
		System.out.println("-m testclasses.NSUPolicy1 -p /Users/NicolasM/Downloads/Users/NicolasM/Downloads -j");
		System.out.println("-m testclasses.NSUPolicy1 -f externalTest/main/utils/SomeClass.java");
		System.out.println("-m testclasses.NSUPolicy1 -f externalTest/main/utils/SomeClass.java some/absolut/path/to/additionalFile.java");
	}

    public static ArgumentContainer getSootOptions(String[] args) {

        // locals variables to hold parsing results
        String mainclass;
        boolean toJimple;
        String outputFolder;
        List<String> addDirsToClasspath = new ArrayList<>();
        List<String> additionalFiles = new ArrayList<>();

        // flags
        final String MAINCLASS_FLAG = "m";
        final String ADD_DIRECTORIES_TO_CLASSPATH = "p";
        final String JIMPLE_FLAG = "j";
        final String OUTPUT_FOLDER_FLAG = "o";
        final String ADDITIONAL_FILES_FLAG = "f";

        // ======== PREPARE OPTIONS =========
		Options options = new Options();

        Option mainopt = new Option(MAINCLASS_FLAG, "mainclass", true,
                "mainclass");
        mainopt.setRequired(true);
        options.addOption(mainopt);


		Option addDirsToClasspathOption = new Option(ADD_DIRECTORIES_TO_CLASSPATH, "directory", true,
				"Optional: Add directories to the soot classpath. Use if you want to add files (mainclass or via -f flag) that are" +
                        "not contained in your current directory");
		addDirsToClasspathOption.setRequired(false);
		addDirsToClasspathOption.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(addDirsToClasspathOption);

        Option format = new Option(JIMPLE_FLAG, "jimple", false,
                "Optional: Output as Jimple instead of as compiled class");
        format.setRequired(false);
        options.addOption(format);

		Option output = new Option(OUTPUT_FOLDER_FLAG, "output", true,
                "Optional: Set output folder");
		output.setRequired(false);
		options.addOption(output);

		Option filesToAdd = new Option(ADDITIONAL_FILES_FLAG, "files", true,
                "Optional: add additional files to instrumentation process");
		filesToAdd.setRequired(false);
		filesToAdd.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(filesToAdd);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);

			// m flag
            mainclass = cmd.getOptionValue(MAINCLASS_FLAG);

            // case p flag
            if (cmd.hasOption(ADD_DIRECTORIES_TO_CLASSPATH)) {
                for ( String s : cmd.getOptionValues(ADD_DIRECTORIES_TO_CLASSPATH)) {
                    File tmp = new File(s);
                    addDirsToClasspath.add(tmp.isAbsolute() ? s : new File(System.getProperty("user.dir"), s).getAbsolutePath());
                }
            }

			// case j flag
            toJimple = cmd.hasOption(JIMPLE_FLAG);

            // case o flag
            if (cmd.hasOption(OUTPUT_FOLDER_FLAG)) {
                outputFolder = cmd.getOptionValue(OUTPUT_FOLDER_FLAG);
            } else {
                outputFolder = ArgumentContainer.VALUE_NOT_SET;
            }

			// case f flag
            if (cmd.hasOption(ADDITIONAL_FILES_FLAG)) {
                Collections.addAll(additionalFiles, cmd.getOptionValues(ADDITIONAL_FILES_FLAG));
            }

            // case help flag
            if (cmd.hasOption("h")){
                printHelp();
                System.exit(0);
            }

            return new ArgumentContainer(mainclass,
                    addDirsToClasspath,
                    toJimple,
                    outputFolder,
                    additionalFiles);

			// if illegal input
		} catch (ParseException e) {
			printHelp();
			System.exit(0);
		}

	    throw new InternalAnalyzerException("This line must never be reached"); // compiler complains if i dont put this here?!
	}
}

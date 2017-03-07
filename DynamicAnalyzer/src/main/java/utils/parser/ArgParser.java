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

    // flags
    final static String MAINCLASS_FLAG = "m";
    final static String ADD_DIRECTORIES_TO_CLASSPATH = "p";
    final static String OTHER_CLASSES_FOR_STATIC_ANALYZER = "s";
    final static String JIMPLE_FLAG = "j";
    final static String OUTPUT_FOLDER_FLAG = "o";
    final static String ADDITIONAL_FILES_FLAG = "f";
    final static String PUBLIC_TYPING_FOR_JIMPLE = "x";
    final static String HELP = "h";

	private static void printHelp() {
		System.out.println(" ====== HELP ======= ");
		System.out.println("-" + MAINCLASS_FLAG + ": Set mainclass ");
        System.out.println("-" + JIMPLE_FLAG + ": Compile to Jimple. ");
		System.out.println("-" + ADD_DIRECTORIES_TO_CLASSPATH + ": Set classpath for soot. Use to add multiple folders in several directories. ");
        System.out.println("-" + OTHER_CLASSES_FOR_STATIC_ANALYZER + ": Add other classes to be analyzed");
		System.out.println("-" + OUTPUT_FOLDER_FLAG + ": Set output folder. ");
        System.out.println("-" + ADDITIONAL_FILES_FLAG + ": Add additional files to be processed (like testclasses.SomeHelperClassForMain)");
        System.out.println("-" + PUBLIC_TYPING_FOR_JIMPLE + ": Developer's option. Uses all public typing and produces a reduced jimple file");
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
        List<String> addClasses = new ArrayList<>();
        List<String> additionalFiles = new ArrayList<>();
        boolean usePublicTyping;



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

		Option addOtherClassesForStatic = new Option(OTHER_CLASSES_FOR_STATIC_ANALYZER, "otherClasses", true,
		                "Add other Classes for the static analyzer");
		addOtherClassesForStatic.setRequired(false);
		addOtherClassesForStatic.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(addOtherClassesForStatic);

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

		Option help = new Option(HELP, "help", false, "Print Help");
		help.setRequired(false);
		options.addOption(help);

		Option publicTyping = new Option(PUBLIC_TYPING_FOR_JIMPLE, "publicTyping", false, "Developer's option: Run main with all public. Especially useful in" +
                "combination with -" + JIMPLE_FLAG + "to print out the optimised jimple");
		publicTyping.setRequired(false);
		options.addOption(publicTyping);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);

            // help flag
            if (cmd.hasOption("h")){
                printHelp();
                System.exit(0);
            }

			// m flag
            mainclass = cmd.getOptionValue(MAINCLASS_FLAG);

            // case p flag
            if (cmd.hasOption(ADD_DIRECTORIES_TO_CLASSPATH)) {
                for ( String s : cmd.getOptionValues(ADD_DIRECTORIES_TO_CLASSPATH)) {
                    File tmp = new File(s);
                    addDirsToClasspath.add(tmp.isAbsolute() ? s : new File(System.getProperty("user.dir"), s).getAbsolutePath());
                }
            }

            // case s flag
            if (cmd.hasOption(OTHER_CLASSES_FOR_STATIC_ANALYZER)) {
                for (String s: cmd.getOptionValues(OTHER_CLASSES_FOR_STATIC_ANALYZER)) {
                    File tmp = new File(s);
                    addClasses.add(tmp.isAbsolute() ? s : new File(System.getProperty("user.dir"), s).getAbsolutePath());
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

           usePublicTyping = cmd.hasOption(PUBLIC_TYPING_FOR_JIMPLE);

            //

            return new ArgumentContainer(mainclass,
                    addDirsToClasspath,
                    addClasses,
                    toJimple,
                    outputFolder,
                    additionalFiles,
                    usePublicTyping);

			// if illegal input
		} catch (ParseException e) {
			printHelp();
			System.exit(0);
		}

	    throw new InternalAnalyzerException("This line must never be reached"); // compiler complains if i dont put this here?!
	}
}

package utils.parser;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

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
	}

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

		CommandLineParser parser = new DefaultParser();

		CommandLine cmd;
		try {

			cmd = parser.parse(options, args);

			// template for the string we will return to soot. also use 4th and
			// 7th element for ant (see bottom of Main.main)
			String[] template = new String[] { "-f", "c", "-main-class",
					"path/to/file placeholder", "mainclass placeholder", "--d",
					"placeholder output path" };

			// case no flag
			template[3] = args[0]; // set mainclass
			template[4] = args[0];
			template[6] = System.getProperty("user.dir");

			// case j flag
			if (cmd.hasOption("j")) {
				template[1] = "J";
			}

			// case p flag
			if (cmd.hasOption("p")) {
				File path = new File(cmd.getOptionValue("p"));

				if (path.isAbsolute()) {
					try {
						pathArgs.add(path.getCanonicalPath());
					} catch (IOException e) {
						throw new InternalAnalyzerException(e.getMessage());
					} 
				} else {
					File parent = new File(System.getProperty("user.dir"));
					File fullPath = new File(parent, cmd.getOptionValue("p"));
					try {
						pathArgs.add(fullPath.getCanonicalPath());
					} catch (IOException e) {
						throw new InternalAnalyzerException(e.getMessage());
					} 
				}
			}

			// case o flag
			if (cmd.hasOption("o")) {
				File out = new File(cmd.getOptionValue("o"));

				if (out.isAbsolute()) {
					template[6] = out.getAbsolutePath();
				} else {
					File parent = new File(System.getProperty("user.dir"));
					File fullPath = new File(parent, cmd.getOptionValue("o"));
					template[6] = fullPath.getAbsolutePath();
				}
			}

			return template;

			// if illegal input
		} catch (ParseException e) {
			throw new IllegalArgumentsException(e.getMessage());
		}
	}
}

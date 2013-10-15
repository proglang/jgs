package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logging.SootLoggerLevel;

import analysis.Main;
import analysis.TaintTracking;

/**
 * <h1>{@link TaintTracking} analysis specific utilities</h1>
 * 
 * The {@link GeneralUtils} provides several general methods that are mainly used by {@link Main}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class GeneralUtils {

	/** Command line option for setting up the levels which should be logged. */
	private static final String OPT_LOG_LEVELS = "-log-levels";
	/** Command line option for enabling the export of log files. */
	private static final String OPT_EXPORT_FILE = "-export-file";
	/** Command line option for enabling the instant logging. */
	private static final String OPT_INSTANT_LOGGING = "-instant-logging";

	/**
	 * Method that handles customized arguments. Arguments such as "-log-levels", "-instant-logging"
	 * and "-export-file" lead to specific method invocation.
	 * 
	 * @param args
	 *            The original arguments array of the main method.
	 * @return Argument array where the customized commands are removed.
	 */
	public static String[] precheckArguments(String[] args) {
		List<String> arguments = new ArrayList<String>(Arrays.asList(args));
		if (arguments.contains(OPT_LOG_LEVELS)) {
			int argsPosition = arguments.indexOf(OPT_LOG_LEVELS) + 1;
			if (argsPosition < arguments.size()) {
				String[] levels = arguments.get(argsPosition).split(",");
				setLogLevels(levels);
				arguments.remove(argsPosition);
			}
			arguments.remove(OPT_LOG_LEVELS);
		} else {
			Main.addLevel(SootLoggerLevel.ALL);
		}
		if (arguments.contains(OPT_EXPORT_FILE)) {
			Main.exportFile();
			arguments.remove(OPT_EXPORT_FILE);
		}
		if (arguments.contains(OPT_INSTANT_LOGGING)) {
			Main.instantLogging();
			arguments.remove(OPT_INSTANT_LOGGING);
		}
		String[] result = new String[arguments.size()];
		return arguments.toArray(result);
	}

	/**
	 * Adds valid levels of the given array to the level array of the main class {@link Main}.
	 * 
	 * @param levels
	 *            Array of String which should represent a level.
	 */
	private static void setLogLevels(String[] levels) {
		for (String level : levels) {
			switch (level.toLowerCase()) {
			case "exception":
				Main.addLevel(SootLoggerLevel.EXCEPTION);
				break;
			case "error":
				Main.addLevel(SootLoggerLevel.ERROR);
				break;
			case "warning":
				Main.addLevel(SootLoggerLevel.WARNING);
				break;
			case "information":
				Main.addLevel(SootLoggerLevel.INFORMATION);
				break;
			case "configuration":
				Main.addLevel(SootLoggerLevel.CONFIGURATION);
				break;
			case "structure":
				Main.addLevel(SootLoggerLevel.STRUCTURE);
				break;
			case "debug":
				Main.addLevel(SootLoggerLevel.DEBUG);
				break;
			case "sideeffect":
				Main.addLevel(SootLoggerLevel.SIDEEFFECT);
				break;
			case "security":
				Main.addLevel(SootLoggerLevel.SECURITY);
				break;
			case "effect":
				Main.addLevel(SootLoggerLevel.SIDEEFFECT);
				break;
			case "securitychecker":
				Main.addLevel(SootLoggerLevel.SECURITYCHECKER);
				break;
			case "off":
				Main.addLevel(SootLoggerLevel.OFF);
				break;
			case "all":
				Main.addLevel(SootLoggerLevel.ALL);
				break;
			case "important":
				Main.addLevel(SootLoggerLevel.EXCEPTION);
				Main.addLevel(SootLoggerLevel.ERROR);
				Main.addLevel(SootLoggerLevel.CONFIGURATION);
				Main.addLevel(SootLoggerLevel.STRUCTURE);
				Main.addLevel(SootLoggerLevel.SECURITY);
				Main.addLevel(SootLoggerLevel.SIDEEFFECT);
				Main.addLevel(SootLoggerLevel.SECURITYCHECKER);
				break;
			}
		}
	}
	
}

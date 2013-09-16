package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logging.SootLoggerLevel;

import analysis.Main;

/**
 * Class, which offers various general methods.
 * 
 * @author Thomas Vogel
 * @version 0.1
 *
 */
public class GeneralUtils {
	
	/**
	 * Calculates the difference set of two sets. So the resulting set will contain all 
	 * items of set a which are not in set b.
	 * @param a The first set.
	 * @param b The second set
	 * @return The difference set of a and b: A \ B = { x | x in A and not x in B}
	 */
	public static <T> Set<T> calculateDifferenceOf(Set<? extends T> a, Set<? extends T> b) {
		Set<T> result = new HashSet<T>(a);
		result.removeAll(b);
		return result;
	}

	/**
	 * Method that handles customized arguments. Arguments such as "-log-levels", "-check-classes",
	 * "-export-file" and "-export-jimple" lead to specific method invocation.
	 * 
	 * @param args
	 *            The original arguments array of the main method.
	 * @return Argument array where the customized commands are removed.
	 */
	public static String[] precheckArguments(String[] args) {
		List<String> arguments = new ArrayList<String>(Arrays.asList(args));
		if (arguments.contains("-log-levels")) {
			int argsPosition = arguments.indexOf("-log-levels") + 1;
			if (argsPosition < arguments.size()) {
				String[] levels = arguments.get(argsPosition).split(",");
				setLogLevels(levels);
				arguments.remove(argsPosition);
			}
			arguments.remove("-log-levels");
		} else {
			Main.addLevel(SootLoggerLevel.ALL);
		}
		if (arguments.contains("-check-classes")) {
			Main.checkClasses();
			arguments.remove("-check-classes");
		}
		if (arguments.contains("-export-file")) {
			Main.exportFile();
			arguments.remove("-export-file");
		}
		String[] result = new String[arguments.size()];
		return arguments.toArray(result);
	}

	/**
	 * Adds valid levels of the given array to the level array of the main class.
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
			case "securitychecker":
				Main.addLevel(SootLoggerLevel.SECURITYCHECKER);
				break;
			case "off":
				Main.addLevel(SootLoggerLevel.OFF);
				break;
			case "all":
				Main.addLevel(SootLoggerLevel.ALL);
				break;
			}
		}
	}
}

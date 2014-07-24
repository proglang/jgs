package logging;

import static java.util.logging.Level.ALL;
import static java.util.logging.Level.OFF;
import static logging.AnalysisLogLevel.CONFIGURATION;
import static logging.AnalysisLogLevel.HEADING;
import static logging.AnalysisLogLevel.SECURITY;
import static logging.AnalysisLogLevel.SIDEEFFECT;
import static logging.AnalysisLogLevel.WARNING;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import logging.Settings.Setting;
import soot.G;

/**
 * <h1>Utilities for the {@link SootLogger}</h1>
 * 
 * The {@link AnalysisLogUtils} provides multiple methods and constants which are mainly required by the {@link SootLogger} and its
 * subclasses as well as the formatter {@link SootLoggerFileFormatter} and {@link AnalysisLogConsoleFormatter}. E.g. some methods allow to
 * generate different {@link Handler} which are already adjusted and which will output the messages on different ways. Other methods allow
 * the conversion of certain objects to Strings, or they simplifying the generation of required Strings.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 */
public class AnalysisLogUtils {

	/** Name of the setting which indicates whether the log should be stored as a text file. */
	public static final String TXT_EXPORT_FILE = "Export File";
	/** Name of the setting which indicates whether the log should be printed during the analysis. */
	public static final String TXT_INSTANT_LOGGING = "Instant logging";
	/** Name of the setting which indicates which log levels are enabled. */
	public static final String TXT_LOG_LEVELS = "Log levels";
	/** Name of the setting which shows the start time of the analysis. */
	public static final String TXT_TIME = "Time";
	/** {@link Formatter} which formats the messages for outputting them as text files. */
	private static final AnalysisLogConsoleFormatter FORMATTER_CONSOLE = new AnalysisLogConsoleFormatter();
	/** Characters with which the heading of a message ends. */
	private static final String TXT_CLOSE_TAG = " ] --|";
	/** String represents the value {@code false} for a setting. */
	private static final String TXT_OFF = "OFF";
	/** String represents the value {@code true} for a setting. */
	private static final String TXT_ON = "ON";
	/** The Java file extension as String. */
	protected static final String FILE_SUFFIX_JAVA = ".java";
	/** Character representing the line separator. */
	protected static final String TXT_LINE_SEPARATOR = System.getProperty("line.separator");
	/** Characters with which the heading of a message starts. */
	protected static final String TXT_OPEN_TAG = "|-- [ ";
	/** Characters representing a tab. */
	protected static final String TXT_TAB = "   ";

	/**
	 * Generates a String from the given {@link Settings}. Each single {@link Setting} of the settings is separated by a newline character.
	 * 
	 * @param settings
	 *          Settings for which a String should be generated that contains the information of this settings.
	 * @return The information of the given settings instance as String.s
	 * @see Settings
	 */
	public static String generateSettingsString(Settings settings) {
		String result = "";
		Map<String, String> settingsMap = settings.getSettingsMap();
		for (String name : settingsMap.keySet()) {
			result += (result.equals("") ? "" : TXT_LINE_SEPARATOR) + name + ": " + settingsMap.get(name);
		}
		return result;
	}

	/**
	 * Generates a {@link StreamHandler}, that allows to export logged messages to the standard console. The handler itself is adjusted to
	 * export all levels, but a filter filters those messages which are not enabled, i.e. not contained by the given level array. The logged
	 * messages will be formatted by the {@link AnalysisLogConsoleFormatter} that is given by {@link AnalysisLogUtils#FORMATTER_CONSOLE}.
	 * 
	 * @param levels
	 *          List of enabled levels.
	 * @return A Handler that outputs logged messages to the standard console. The formatting will be done by the
	 *         {@link AnalysisLogConsoleFormatter}.
	 */
	public static StreamHandler getStandardConsoleHandler(final Level[] levels) {
		StreamHandler handler = new StreamHandler(G.v().out, FORMATTER_CONSOLE);
		handler.setLevel(ALL);
		Filter filter = new Filter() {

			/**
			 * Check if a given log record should be published. I.e. checks whether the enabled level array contains the {@link Level#OFF}, the
			 * method will return {@code false}. If this array contains {@link Level#ALL}, the method will return {@code true}. If the level of
			 * the record is {@link AnalysisLogLevel#HEADING}, then the method will also return {@code true}. If none of the previous apply, the
			 * method return {@code true} if the array contains the level of the record, otherwise {@code false}.
			 * 
			 * @param record
			 *          A LogRecord which was logged and is now to be published depending on the level.
			 * @return {@code true} if the log record should be published, otherwise {@code false}.
			 * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
			 */
			@Override
			public boolean isLoggable(LogRecord record) {
				if (containsArraySpecificLevel(levels, ALL) || record.getLevel().equals(HEADING)) return true;
				if (containsArraySpecificLevel(levels, OFF)) return false;
				boolean result = false;
				for (Level level : levels) {
					result = result || record.getLevel().equals(level);
				}
				return result;
			}

		};
		handler.setFilter(filter);
		return handler;
	}

	/**
	 * Generates the {@link Setting}, which indicates whether the logged messages should be printed instantaneously by the logger.
	 * 
	 * @param instantLogging
	 *          {@code true} if the messages should be printed directly, otherwise {@code false}.
	 * @return Setting of the instant logging.
	 */
	public static Setting makeInstantLoggingSetting(boolean instantLogging) {
		return new Setting(TXT_INSTANT_LOGGING, (instantLogging ? TXT_ON : TXT_OFF));
	}

	/**
	 * Generates the {@link Setting}, which lists all the levels which are enabled for the logging.
	 * 
	 * @param logLevels
	 *          All enabled log levels.
	 * @return Setting which represent the loggable levels.
	 */
	public static Setting makeLoggerLevelSetting(Level[] logLevels) {
		return new Setting(TXT_LOG_LEVELS, generateLevelListString(logLevels));
	}

	/**
	 * Generates the {@link Setting}, which represents the start time of the analysis.
	 * 
	 * @return Setting that shows the analysis start time.
	 */
	public static Setting makeTimeSetting() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.applyPattern("MM/dd/yyyy' 'HH:mm:ss");
		return new Setting(TXT_TIME, simpleDateFormat.format(new Date()));
	}

	/**
	 * Returns a String which contains the given String the given number of times. E.g. {@code repeat("a", 3).equals("aaa")},
	 * {@code repeat("a", 0).equals("")} and {@code repeat("Ab", 3).equals("AbAbAb")}.
	 * 
	 * @param str
	 *          String which should be repeated the given number of times.
	 * @param n
	 *          Number that indicates how many times the given String should be repeated.
	 * @return A String containing the given String the given number of times.
	 */
	public static String repeat(String str, int n) {
		if (n < 0) return "";
		return new String(new char[n]).replace("\0", str);
	}

	/**
	 * Checks whether the given level is enable for the logging, i.e. it is contained by the given array that contains those level which are
	 * enabled. If this array contains {@link Level#ALL}, then the method will return for every given level {@code true}. If the the array
	 * contains {@link Level#OFF} then the method will return always {@code false}.
	 * 
	 * @param level
	 *          Level for which should be checked whether the logging for this level is enabled.
	 * @param levels
	 *          Array of levels that contains the levels which are enabled.
	 * @return {@code true} if the given array contains {@link Level#ALL}, {@code false} if the array contains {@link Level#OFF}. Otherwise,
	 *         if both are not contained by the given array, the method will return {@code true} if the given message is contained by the
	 *         array, {@code false} otherwise.s
	 */
	public static boolean shouldLogLevel(Level level, Level[] levels) {
		if (containsArraySpecificLevel(levels, ALL)) {
			return true;
		} else if (containsArraySpecificLevel(levels, OFF)) {
			return false;
		} else {
			return containsArraySpecificLevel(levels, level);
		}
	}

	/**
	 * Checks whether the given level array contains the given level.
	 * 
	 * @param levels
	 *          Array of levels for which should be checked whether it contains the given level.
	 * @param lookup
	 *          {@link Level} for which should be checked whether it is contained by the given level array.
	 * @return {@code true} if the given array contains the given level, otherwise {@code false}.
	 */
	private static boolean containsArraySpecificLevel(Level[] levels, Level lookup) {
		for (Level level : levels) {
			if (level.equals(lookup)) return true;
		}
		return false;
	}

	/**
	 * Generates a comma separated list of the selected levels.
	 * 
	 * @return List of levels as String.
	 */
	private static String generateLevelListString(Level[] levels) {
		String result = "";
		for (Level level : levels) {
			if (!result.equals("")) result += ", ";
			result += level.getLocalizedName();
		}
		return result;
	}

	/**
	 * Generates a formatted String which contains a default message for which the message level has no applicable formatting. The message
	 * string contains the level as well as the given level. Note: this method is provided for the {@link Formatter} subclasses.
	 * 
	 * @param levelName
	 *          {@link AnalysisLogLevel} name, which possesses the given message.
	 * @param msg
	 *          Message which should be formatted for printing.
	 * @return A formatted default message which contains the given level as well as the message itself.
	 * @see SootLoggerFileFormatter
	 * @see AnalysisLogConsoleFormatter
	 */
	protected static String generateDefaultMessage(String levelName, String msg) {
		StringBuilder result = new StringBuilder();
		result.append(levelName + ":" + TXT_TAB);
		result.append(msg);
		result.append(TXT_LINE_SEPARATOR);
		return result.toString();
	}

	/**
	 * Generates a formatted String which represents heading of a message. The given level represents the level of the logged message and the
	 * array of objects may contain additional information which should be shown in the heading. The most messages have multiple tabs in front
	 * of the heading. The number of the tabs can be given by a {@link HeadingInformation} instance, or by a
	 * {@link ExtendedHeadingInformation} instance which also provides informations about the file name and the source line number where the
	 * message was generated. Note: this method is provided for the {@link Formatter} subclasses.
	 * 
	 * @param levelName
	 *          {@link AnalysisLogLevel} name of the message for which the heading will be generated.
	 * @param parameters
	 *          Array of objects, which may contain a {@link HeadingInformation} or a {@link ExtendedHeadingInformation} for providing
	 *          informations about the number of tabs in front of the heading as well as the file name and the source line number where the
	 *          message was generated.
	 * @return A formatted String which represents heading of a message. This contains the given level and if available, the file name and the
	 *         source line number.
	 * @see SootLoggerFileFormatter
	 * @see AnalysisLogConsoleFormatter
	 */
	protected static String generateLogHeading(String levelName, Object[] parameters) {
		StringBuilder result = new StringBuilder();
		String prefix = "";
		String additionalInfo = " ";
		if (parameters != null && parameters.length == 1) {
			Object obj = parameters[0];
			if (obj instanceof ExtendedHeadingInformation) {
				ExtendedHeadingInformation info = (ExtendedHeadingInformation) obj;
				prefix = repeat(TXT_TAB, info.getTabs());
				additionalInfo = " (" + info.getFileName() + FILE_SUFFIX_JAVA + ":" + info.getSrcLn() + ")";
			} else if (obj instanceof HeadingInformation) {
				HeadingInformation info = (HeadingInformation) obj;
				prefix = repeat(TXT_TAB, info.getTabs());
			}
		}
		result.append(prefix + TXT_OPEN_TAG + levelName + additionalInfo + TXT_CLOSE_TAG + TXT_LINE_SEPARATOR);
		return result.toString();
	}

	/**
	 * Generates a formatted String which contains a structural information. The message is framed by predefined tags. Note: this method is
	 * provided for the {@link Formatter} subclasses.
	 * 
	 * @param msg
	 *          Structural message which should be formatted for printing.
	 * @return A formatted structural message.
	 * @see SootLoggerFileFormatter
	 * @see AnalysisLogConsoleFormatter
	 */
	protected static String generateStructureMessage(String msg) {
		return TXT_OPEN_TAG + msg + TXT_CLOSE_TAG + TXT_LINE_SEPARATOR;
	}

	/**
	 * Checks whether the given level is a {@link AnalysisLogLevel} that can be handled as a standard level by the formatter. These standard
	 * levels include the following:
	 * <ul>
	 * <li>{@link AnalysisLogLevel#WARNING}</li>
	 * <li>{@link AnalysisLogLevel#INFORMATION}</li>
	 * <li>{@link AnalysisLogLevel#DEBUGGING}</li>
	 * <li>{@link AnalysisLogLevel#SIDEEFFECT}</li>
	 * <li>{@link AnalysisLogLevel#SECURITY}</li>
	 * <li>{@link AnalysisLogLevel#SECURITYCHECKER}</li>
	 * </ul>
	 * 
	 * @param level
	 *          Level for which should be checked whether it is a standard {@link AnalysisLogLevel} .
	 * @return {@code true} if the given level is one of the standard level, {@code false} otherwise.
	 * @see AnalysisLogConsoleFormatter
	 * @see SootLoggerFileFormatter
	 */
	protected static boolean isStandardLoggableMessage(Level level) {
		return level.equals(WARNING) || level.equals(SIDEEFFECT) || level.equals(SECURITY)
				|| level.equals(CONFIGURATION);

	}

}

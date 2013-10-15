package logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import model.ExtendedHeadingInformation;
import model.HeadingInformation;
import model.MessageStore;
import model.Settings;
import model.Settings.Setting;
import utils.LoggerMessages;

/**
 * <h1>Utilities for the {@link SootLogger}</h1>
 * 
 * The {@link SootLoggerUtils} provides multiple methods and constants which are mainly required by
 * the {@link SootLogger} and its subclasses as well as the formatter
 * {@link SootLoggerFileFormatter} and {@link SootLoggerConsoleFormatter}. E.g. some methods allow
 * to generate different {@link Handler} which are already adjusted and which will output the
 * messages on different ways. Other methods allow the conversion of certain objects to Strings, or
 * they simplifying the generation of required Strings.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 */
public class SootLoggerUtils {

	/** Name of the setting which indicates whether the log should be stored as a text file. */
	public static final String TXT_EXPORT_FILE = "Export File";
	/** Name of the setting which indicates whether the log should be printed during the analysis. */
	public static final String TXT_INSTANT_LOGGING = "Instant logging";
	/** Name of the setting which indicates which log levels are enabled. */
	public static final String TXT_LOG_LEVELS = "Log levels";
	/** Name of the setting which shows the start time of the analysis. */
	public static final String TXT_TIME = "Time";
	/**
	 * Name of the serialized {@link MessageStore} which is located at
	 * {@link SootLoggerUtils#FOLDER_SERIALIZE}.
	 */
	private static final String FILE_SERIALIZE = "messagestore.ser";
	/** File extension for a text file as String. */
	private static final String FILE_SUFFIX_TXT = ".txt";
	/**
	 * Name of the folder which is the root log folder and which contains all sub-folders containing
	 * the log files.
	 */
	private static final String FOLDER_LOG = "log";
	/** Name of the root output folder in which all generate files should be stored. */
	private static final String FOLDER_OUTPUT = "output";
	/** Name of the folder in which the serialized {@link MessageStore} should be stored. */
	private static final String FOLDER_SERIALIZE = "serialize";
	/** {@link Formatter} which formats the messages for outputting them as text files. */
	private static final SootLoggerConsoleFormatter FORMATTER_CONSOLE = new SootLoggerConsoleFormatter();
	/** {@link Formatter} which formats the messages for outputting them via the console. */
	private static final SootLoggerFileFormatter FORMATTER_FILE = new SootLoggerFileFormatter();
	/** Character for separating folders and files in a path. */
	private static final String SIGN_SEPARATOR = System.getProperty("file.separator");
	/** String represents the value {@code false} for a setting. */
	private static final String TXT_OFF = "OFF";
	/** String represents the value {@code true} for a setting. */
	private static final String TXT_ON = "ON";
	/** The Java file extension as String. */
	protected static final String FILE_SUFFIX_JAVA = ".java";
	/** Number of trace elements which should be printed for an exception. */
	protected static final int TRACE_ELEMENTS_MAX = 15;
	/** Characters with which the heading of a message ends. */
	protected static final String TXT_CLOSE_TAG = " ] --|";
	/** Character representing the line separator. */
	protected static final String TXT_LINE_SEPARATOR = System.getProperty("line.separator");
	/** Characters with which the heading of a message starts. */
	protected static final String TXT_OPEN_TAG = "|-- [ ";
	/** Characters representing a tab. */
	protected static final String TXT_TAB = "   ";

	/**
	 * Generates a String from the given {@link Settings}. Each single {@link Setting} of the
	 * settings is separated by a newline character.
	 * 
	 * @param settings
	 *            Settings for which a String should be generated that contains the information of
	 *            this settings.
	 * @return The information of the given settings instance as String.s
	 * @see Settings
	 */
	public static String generateSettingsString(Settings settings) {
		String result = "";
		Map<String, String> settingsMap = settings.getSettingsMap();
		for (String name : settingsMap.keySet()) {
			result += (result.equals("") ? "" : SootLoggerUtils.TXT_LINE_SEPARATOR) + name + ": "
					+ settingsMap.get(name);
		}
		return result;
	}

	/**
	 * Returns the path to the serialized {@link MessageStore} file. Depending on the given flag,
	 * the file will be delete if it exists.
	 * 
	 * @param deleteOld
	 *            Should the file be deleted if it exists.
	 * @return The path to the serialized {@link MessageStore} file.
	 */
	public static String getSerializeFile(boolean deleteOld) {
		File serializeFile = new File(getSerializePath() + SIGN_SEPARATOR + FILE_SERIALIZE);
		if (serializeFile.exists() && deleteOld) {
			serializeFile.delete();
		}
		return serializeFile.getAbsolutePath();
	}

	/**
	 * Generates a {@link StreamHandler}, that allows to export logged messages to the standard
	 * console. The handler itself is adjusted to export all levels, but a filter filters those
	 * messages which are not enabled, i.e. not contained by the given level array. The logged
	 * messages will be formatted by the {@link SootLoggerConsoleFormatter} that is given by
	 * {@link SootLoggerUtils#FORMATTER_CONSOLE}.
	 * 
	 * @param levels
	 *            List of enabled levels.
	 * @return A Handler that outputs logged messages to the standard console. The formatting will
	 *         be done by the {@link SootLoggerConsoleFormatter}.
	 */
	public static StreamHandler getStandardConsoleHandler(final Level[] levels) {
		StreamHandler handler = new StreamHandler(System.out, FORMATTER_CONSOLE);
		handler.setLevel(SootLoggerLevel.ALL);
		Filter filter = new Filter() {

			/**
			 * Check if a given log record should be published. I.e. checks whether the enabled
			 * level array contains the {@link Level#OFF}, the method will return {@code false}. If
			 * this array contains {@link Level#ALL}, the method will return {@code true}. If the
			 * level of the record is {@link SootLoggerLevel#HEADING}, then the method will also
			 * return {@code true}. If none of the previous apply, the method return {@code true} if
			 * the array contains the level of the record, otherwise {@code false}.
			 * 
			 * @param record
			 *            A LogRecord which was logged and is now to be published depending on the
			 *            level.
			 * @return {@code true} if the log record should be published, otherwise {@code false}.
			 * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
			 */
			@Override
			public boolean isLoggable(LogRecord record) {
				if (containsArraySpecificLevel(levels, SootLoggerLevel.ALL)
						|| record.getLevel().equals(SootLoggerLevel.HEADING))
					return true;
				if (containsArraySpecificLevel(levels, SootLoggerLevel.OFF))
					return false;
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
	 * Generates a {@link StreamHandler}, that allows to export logged messages to a file with the
	 * given file name in a sub folder with the given folder name. This folder will be located in
	 * the specific output folder of the given logger. The handler itself is adjusted to export all
	 * levels, but a filter filters those messages which are not enabled, i.e. not contained by the
	 * given level array. The logged messages will be formatted by the
	 * {@link SootLoggerFileFormatter} that is given by {@link SootLoggerUtils#FORMATTER_FILE}. .
	 * 
	 * @param levels
	 *            List of enabled levels.
	 * @param fileName
	 *            File name of the log file.
	 * @param folderName
	 *            Name of the folder which contains the log file.
	 * @param logger
	 *            Logger which handles the returned {@link FileHandler}.
	 * @return A Handler that outputs logged messages to a file with the given file name in the
	 *         given folder that is located in the logger specific output folder. The formatting
	 *         will be done by the {@link SootLoggerFileFormatter}.
	 * @throws IOException
	 *             If the file with the given file name can't be created.
	 * @throws SecurityException
	 *             If the file with the given file name can't be accessed.
	 */
	public static FileHandler getStandardFileHandler(final Level[] levels, String fileName,
			String folderName, SootLogger logger) throws IOException, SecurityException {
		FileHandler handler = new FileHandler(getPathOfLogFile(fileName, folderName, logger));
		handler.setFormatter(FORMATTER_FILE);
		handler.setLevel(SootLoggerLevel.ALL);
		Filter filter = new Filter() {

			/**
			 * Check if a given log record should be published. I.e. checks whether the enabled
			 * level array contains the {@link Level#OFF}, the method will return {@code false}. If
			 * this array contains {@link Level#ALL}, the method will return {@code true}. If the
			 * level of the record is {@link SootLoggerLevel#HEADING}, then the method will also
			 * return {@code true}. If none of the previous apply, the method return {@code true} if
			 * the array contains the level of the record, otherwise {@code false}.
			 * 
			 * @param record
			 *            A LogRecord which was logged and is now to be published depending on the
			 *            level.
			 * @return {@code true} if the log record should be published, otherwise {@code false}.
			 * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
			 */
			@Override
			public boolean isLoggable(LogRecord record) {
				if (containsArraySpecificLevel(levels, SootLoggerLevel.ALL)
						|| record.getLevel().equals(SootLoggerLevel.HEADING))
					return true;
				if (containsArraySpecificLevel(levels, SootLoggerLevel.OFF))
					return false;
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
	 * Generates the {@link Setting}, which indicates whether the logged messages should be written
	 * to a file.
	 * 
	 * @param exportFile
	 *            {@code true} if the messages should be written to a file, otherwise {@code false}.
	 * @return Setting of the file export.
	 */
	public static Setting makeExportFileSetting(boolean exportFile) {
		return new Setting(TXT_EXPORT_FILE, (exportFile ? TXT_ON : TXT_OFF));
	}

	/**
	 * Generates the {@link Setting}, which indicates whether the logged messages should be printed
	 * instantaneously by the logger.
	 * 
	 * @param instantLogging
	 *            {@code true} if the messages should be printed directly, otherwise {@code false}.
	 * @return Setting of the instant logging.
	 */
	public static Setting makeInstantLoggingSetting(boolean instantLogging) {
		return new Setting(TXT_INSTANT_LOGGING, (instantLogging ? TXT_ON : TXT_OFF));
	}

	/**
	 * Generates the {@link Setting}, which lists all the levels which are enabled for the logging.
	 * 
	 * @param logLevels
	 *            All enabled log levels.
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
	 * Returns a String which contains the given String the given number of times. E.g.
	 * {@code repeat("a", 3).equals("aaa")}, {@code repeat("a", 0).equals("")} and
	 * {@code repeat("Ab", 3).equals("AbAbAb")}.
	 * 
	 * @param str
	 *            String which should be repeated the given number of times.
	 * @param n
	 *            Number that indicates how many times the given String should be repeated.
	 * @return A String containing the given String the given number of times.
	 */
	public static String repeat(String str, int n) {
		if (n < 0)
			return "";
		return new String(new char[n]).replace("\0", str);
	}

	/**
	 * Tries to restored the serialized {@link MessageStore} object which is located at the path
	 * that is given by the method {@link SootLoggerUtils#getSerializeFile(boolean)}.
	 * 
	 * @return The restored {@link MessageStore} that was serialized previously at a predefined
	 *         path.
	 * @throws NullPointerException
	 *             If the serialized file doesn't exists or if the deserialization fails.
	 * @see SootLoggerUtils#getSerializeFile(boolean)
	 */
	public static MessageStore restoreSerializedMessageStore() throws NullPointerException {
		try {
			FileInputStream fileInputStream = new FileInputStream(
					SootLoggerUtils.getSerializeFile(false));
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			MessageStore messageStore = (MessageStore) objectInputStream.readObject();
			objectInputStream.close();
			return messageStore;
		} catch (IOException | ClassNotFoundException e) {
			throw new NullPointerException(LoggerMessages.deserializationNotPossible());
		}
	}

	/**
	 * Checks whether the given level is enable for the logging, i.e. it is contained by the given
	 * array that contains those level which are enabled. If this array contains {@link Level#ALL},
	 * then the method will return for every given level {@code true}. If the the array contains
	 * {@link Level#OFF} then the method will return always {@code false}.
	 * 
	 * @param level
	 *            Level for which should be checked whether the logging for this level is enabled.
	 * @param levels
	 *            Array of levels that contains the levels which are enabled.
	 * @return {@code true} if the given array contains {@link Level#ALL}, {@code false} if the
	 *         array contains {@link Level#OFF}. Otherwise, if both are not contained by the given
	 *         array, the method will return {@code true} if the given message is contained by the
	 *         array, {@code false} otherwise.s
	 */
	public static boolean shouldLogLevel(Level level, Level[] levels) {
		if (containsArraySpecificLevel(levels, SootLoggerLevel.ALL)) {
			return true;
		} else if (containsArraySpecificLevel(levels, SootLoggerLevel.OFF)) {
			return false;
		} else {
			return containsArraySpecificLevel(levels, level);
		}
	}

	/**
	 * Checks whether the given level array contains the given level.
	 * 
	 * @param levels
	 *            Array of levels for which should be checked whether it contains the given level.
	 * @param lookup
	 *            {@link Level} for which should be checked whether it is contained by the given
	 *            level array.
	 * @return {@code true} if the given array contains the given level, otherwise {@code false}.
	 */
	private static boolean containsArraySpecificLevel(Level[] levels, Level lookup) {
		for (Level level : levels) {
			if (level.equals(lookup))
				return true;
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
			if (!result.equals(""))
				result += ", ";
			result += level.getLocalizedName();
		}
		return result;
	}

	/**
	 * Generates the file path to the folder which stores the logger specific outputs in the working
	 * directory of the current execution.
	 * 
	 * @param logger
	 *            {@link SootLogger} for which the logger specific log path in the working directory
	 *            should be returned.
	 * @return Path to the logger specific output folder.
	 * @see SootLoggerUtils#getOutputPath()
	 */
	private static String getLoggerPath(SootLogger logger) {
		String loggerPath = logger.getLoggerFolder();
		File loggerFolder = new File(loggerPath.equals("") ? getOutputPath() : (getOutputPath()
				+ SIGN_SEPARATOR + loggerPath));
		if (!loggerFolder.exists() || !loggerFolder.isDirectory()) {
			loggerFolder.mkdir();
		}
		return loggerFolder.getAbsolutePath();
	}

	/**
	 * Generates the file path to the folder which stores the log files of the given in the working
	 * directory of the current execution.
	 * 
	 * @param logger
	 *            {@link SootLogger} for which the logger specific folder of the log files should be
	 *            returned.
	 * @return Path to the log file folder in the logger specific output folder.
	 * @see SootLoggerUtils#getLoggerPath(SootLogger)
	 */
	private static String getLogPath(SootLogger logger) {
		File logFolder = new File(getLoggerPath(logger) + SIGN_SEPARATOR + FOLDER_LOG);
		if (!logFolder.exists() || !logFolder.isDirectory()) {
			logFolder.mkdir();
		}
		return logFolder.getAbsolutePath();
	}

	/**
	 * Generates the file path to the folder which stores the outputs in the working directory of
	 * the current execution.
	 * 
	 * @return Path to the output folder in the working directory.
	 * @see SootLoggerUtils#getProjectPath()
	 */
	private static String getOutputPath() {
		File outputFolder = new File(getProjectPath() + SIGN_SEPARATOR + FOLDER_OUTPUT);
		if (!outputFolder.exists() || !outputFolder.isDirectory()) {
			outputFolder.mkdir();
		}
		return outputFolder.getAbsolutePath();
	}

	/**
	 * Checks whether the given file name exists in the given folder. If this is the case an
	 * increasing number will be appended to the file name until no file exists with the generated
	 * file name. This file will be returned by the method.
	 * 
	 * @param folderPath
	 *            Path of the folder which contains the log file.
	 * @param fileName
	 *            Original name of the log file.
	 * @return {@link File} which is unique in the given folder and has the given file name followed
	 *         by an increased number.
	 */
	private static File getPathOfFileAndHandleExistence(File folderPath, String fileName) {
		File file = new File(folderPath.getAbsolutePath() + SIGN_SEPARATOR + fileName
				+ FILE_SUFFIX_TXT);
		int i = 1;
		while (file.exists()) {
			file = new File(folderPath.getAbsolutePath() + SIGN_SEPARATOR + fileName + "-"
					+ String.valueOf(i++) + FILE_SUFFIX_TXT);
		}
		return file;
	}

	/**
	 * Generates the file path to the file which is named by the given file name and which is
	 * located in the sub folder with the given folder name. This sub folder is located in the log
	 * file folder of the the given {@link SootLogger} in the working directory. The file name
	 * depends also on the fact whether there exists already a file with this name. In this case the
	 * name will be extended with a increasing number.
	 * 
	 * @param fileName
	 *            Name of the log file.
	 * @param folderName
	 *            Name of the folder which contains the log file and which is located in the logger
	 *            specific log file folder.
	 * @param logger
	 *            Logger which is responsible for the path of the log files.
	 * @return Path to the file that gets the given name and is located in a folder with the given
	 *         folder name. To path of this folder depends on the given logger and its logger
	 *         specific output folder.
	 * @see SootLoggerUtils#getLogPath(SootLogger)
	 * @see SootLoggerUtils#getPathOfFileAndHandleExistence(File, String)
	 */
	private static String getPathOfLogFile(String fileName, String folderName, SootLogger logger) {
		File folder = new File(getLogPath(logger) + SIGN_SEPARATOR + folderName);
		if (!folder.exists() || !folder.isDirectory())
			folder.mkdir();
		File file = getPathOfFileAndHandleExistence(folder, fileName);
		return file.getAbsolutePath();
	}

	/**
	 * Generates the file path to the folder of the eclipse project which is the working directory
	 * of the current execution.
	 * 
	 * @return Path to the working directory.
	 */
	private static String getProjectPath() {
		String currentDir = new File("").getAbsolutePath();
		String currentDirName = currentDir.substring(currentDir.lastIndexOf(SIGN_SEPARATOR) + 1);
		String parentDir = currentDir.substring(0, currentDir.lastIndexOf(SIGN_SEPARATOR));
		if (currentDirName.length() > 0 && Character.isUpperCase(currentDirName.charAt(0))) {
			parentDir = currentDir;
		}
		return parentDir;
	}

	/**
	 * Generates the file path to the folder which contains the serialized {@link MessageStore}
	 * file.
	 * 
	 * @return Path to the folder that contains the serialized message store file.
	 */
	private static String getSerializePath() {
		File serializeFolder = new File(getOutputPath() + SIGN_SEPARATOR + FOLDER_SERIALIZE);
		if (!serializeFolder.exists() || !serializeFolder.isDirectory()) {
			serializeFolder.mkdir();
		}
		return serializeFolder.getAbsolutePath();
	}

	/**
	 * Generates a formatted String which contains a default message for which the message level has
	 * no applicable formatting. The message string contains the level as well as the given level.
	 * Note: this method is provided for the {@link Formatter} subclasses.
	 * 
	 * @param levelName
	 *            {@link SootLoggerLevel} name, which possesses the given message.
	 * @param msg
	 *            Message which should be formatted for printing.
	 * @return A formatted default message which contains the given level as well as the message
	 *         itself.
	 * @see SootLoggerFileFormatter
	 * @see SootLoggerConsoleFormatter
	 */
	protected static String generateDefaultMessage(String levelName, String msg) {
		StringBuilder result = new StringBuilder();
		result.append(levelName + ":" + TXT_TAB);
		result.append(msg);
		result.append(TXT_LINE_SEPARATOR);
		return result.toString();
	}

	/**
	 * Generates a formatted String which represents heading of a message. The given level
	 * represents the level of the logged message and the array of objects may contain additional
	 * information which should be shown in the heading. The most messages have multiple tabs in
	 * front of the heading. The number of the tabs can be given by a {@link HeadingInformation}
	 * instance, or by a {@link ExtendedHeadingInformation} instance which also provides
	 * informations about the file name and the source line number where the message was generated.
	 * Note: this method is provided for the {@link Formatter} subclasses.
	 * 
	 * @param levelName
	 *            {@link SootLoggerLevel} name of the message for which the heading will be
	 *            generated.
	 * @param parameters
	 *            Array of objects, which may contain a {@link HeadingInformation} or a
	 *            {@link ExtendedHeadingInformation} for providing informations about the number of
	 *            tabs in front of the heading as well as the file name and the source line number
	 *            where the message was generated.
	 * @return A formatted String which represents heading of a message. This contains the given
	 *         level and if available, the file name and the source line number.
	 * @see SootLoggerFileFormatter
	 * @see SootLoggerConsoleFormatter
	 */
	protected static String generateLogHeading(String levelName, Object[] parameters) {
		StringBuilder result = new StringBuilder();
		String prefix = "";
		String additionalInfo = "";
		if (parameters != null && parameters.length == 1) {
			Object obj = parameters[0];
			if (obj instanceof ExtendedHeadingInformation) {
				ExtendedHeadingInformation info = (ExtendedHeadingInformation) obj;
				prefix = repeat(TXT_TAB, info.getTabs());
				additionalInfo = "(" + info.getFileName() + FILE_SUFFIX_JAVA + ":"
						+ info.getSrcLn() + ")";
			} else if (obj instanceof HeadingInformation) {
				HeadingInformation info = (HeadingInformation) obj;
				prefix = repeat(TXT_TAB, info.getTabs());
			}
		}
		result.append(prefix + TXT_OPEN_TAG + levelName + additionalInfo + TXT_CLOSE_TAG
				+ TXT_LINE_SEPARATOR);
		return result.toString();
	}

	/**
	 * Generates a formatted String which contains a structural information. The message is framed
	 * by predefined tags. Note: this method is provided for the {@link Formatter} subclasses.
	 * 
	 * @param msg
	 *            Structural message which should be formatted for printing.
	 * @return A formatted structural message.
	 * @see SootLoggerFileFormatter
	 * @see SootLoggerConsoleFormatter
	 */
	protected static String generateStructureMessage(String msg) {
		return SootLoggerUtils.TXT_OPEN_TAG + msg + SootLoggerUtils.TXT_CLOSE_TAG
				+ SootLoggerUtils.TXT_LINE_SEPARATOR;
	}

	/**
	 * Generates a formatted String which contains the information about a given {@link Throwable}
	 * and its stack trace. The number of shown stack trace elements depend on the value of
	 * {@link SootLoggerUtils#TRACE_ELEMENTS_MAX}. The given thread id is used to discover the
	 * thread in which the exception occurred. Note: this method is provided for the
	 * {@link Formatter} subclasses.
	 * 
	 * @param thrown
	 *            Throwable for which the formatted String should be generated.
	 * @param threadID
	 *            ID of the thread in which the exception occurred.
	 * @return Formatted String that contains the information of the given {@link Throwable} and
	 *         which occurred in the thread with the given thread id.
	 * @see SootLoggerFileFormatter
	 * @see SootLoggerConsoleFormatter
	 */
	protected static String handleThrownException(Throwable thrown, int threadID) {
		StringBuilder result = new StringBuilder();
		if (thrown != null) {
			String thread = "unknown";
			for (Thread t : Thread.getAllStackTraces().keySet()) {
				if (t.getId() == threadID) {
					thread = t.getName();
				}
			}
			result.append(TXT_TAB + "Exception in thread \"" + thread + "\" "
					+ thrown.getClass().getName());
			result.append(": " + thrown.getLocalizedMessage() + TXT_LINE_SEPARATOR);
			StackTraceElement[] stackTraceElements = thrown.getStackTrace();
			for (int i = 0; i < TRACE_ELEMENTS_MAX && i < stackTraceElements.length; i++) {
				StackTraceElement stackTraceElement = stackTraceElements[i];
				result.append(TXT_TAB + TXT_TAB + "at " + stackTraceElement.getClassName() + "."
						+ stackTraceElement.getMethodName());
				result.append("(" + stackTraceElement.getFileName() + ":"
						+ stackTraceElement.getLineNumber() + ")");
				result.append(TXT_LINE_SEPARATOR);
			}
			if (TRACE_ELEMENTS_MAX <= stackTraceElements.length) {
				result.append(TXT_TAB + TXT_TAB + "..." + TXT_LINE_SEPARATOR);
			}
		}
		return result.toString();
	}

	/**
	 * Checks whether the given level is a {@link SootLoggerLevel} that can be handled as a standard
	 * level by the formatter. These standard levels include the following:
	 * <ul>
	 * <li>{@link SootLoggerLevel#EXCEPTION}</li>
	 * <li>{@link SootLoggerLevel#ERROR}</li>
	 * <li>{@link SootLoggerLevel#WARNING}</li>
	 * <li>{@link SootLoggerLevel#INFORMATION}</li>
	 * <li>{@link SootLoggerLevel#DEBUG}</li>
	 * <li>{@link SootLoggerLevel#SIDEEFFECT}</li>
	 * <li>{@link SootLoggerLevel#SECURITY}</li>
	 * <li>{@link SootLoggerLevel#SECURITYCHECKER}</li>
	 * </ul>
	 * 
	 * @param level
	 *            Level for which should be checked whether it is a standard {@link SootLoggerLevel}
	 *            .
	 * @return {@code true} if the given level is one of the standard level, {@code false}
	 *         otherwise.
	 * @see SootLoggerConsoleFormatter
	 * @see SootLoggerFileFormatter
	 */
	protected static boolean isStandardLoggableMessage(Level level) {
		return level.equals(SootLoggerLevel.EXCEPTION) || level.equals(SootLoggerLevel.ERROR)
				|| level.equals(SootLoggerLevel.WARNING)
				|| level.equals(SootLoggerLevel.INFORMATION) || level.equals(SootLoggerLevel.DEBUG)
				|| level.equals(SootLoggerLevel.SIDEEFFECT)
				|| level.equals(SootLoggerLevel.SECURITY)
				|| level.equals(SootLoggerLevel.SECURITYCHECKER)
				|| level.equals(SootLoggerLevel.CONFIGURATION);

	}

}

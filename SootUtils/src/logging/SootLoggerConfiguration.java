package logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import model.MessageStore;


import soot.SootMethod;

/**
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class SootLoggerConfiguration {

	/** */
	private static final SootLoggerConsoleFormatter CONSOLE_FORMATTER = new SootLoggerConsoleFormatter();
	/** */
	public static final String EXPORT_FILE_TXT = "Export File";
	/** */
	private static final SootLoggerFileFormatter FILE_FORMATTER = new SootLoggerFileFormatter();
	/** */
	private static final String LOG_FOLDER = "log";
	/** */
	public static final String LOG_LEVELS_TXT = "Log levels";
	/** */
	private static final String OUTPUT_FOLDER = "output";
	/** */
	private static final String SEPARATOR = System.getProperty("file.separator");
	/** */
	private static final String SERIALIZE_FILE = "messagestore.ser";
	/** */
	private static final String SERIALIZE_FOLDER = "serialize";

	/**
	 * 
	 * @param levels
	 * @param specific
	 * @return
	 */
	private static boolean containsArraySpecificLevel(Level[] levels, Level specific) {
		for (Level level : levels) {
			if (level.equals(specific))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param logger
	 * @return
	 */
	private static String getLoggerPath(SootLogger logger) {
		String loggerPath = logger.getLoggerPath();
		File loggerFolder = new File(loggerPath.equals("") ? getOutputPath() : (getOutputPath() + SEPARATOR + loggerPath));
		if (! loggerFolder.exists() || ! loggerFolder.isDirectory()) {
			loggerFolder.mkdir();
		}
		return loggerFolder.getAbsolutePath();
	}

	/**
	 * 
	 * @param logger
	 * @return
	 */
	private static String getLogPath(SootLogger logger) {
		File logFolder = new File(getLoggerPath(logger) + SEPARATOR + LOG_FOLDER);
		if (! logFolder.exists() || ! logFolder.isDirectory()) {
			logFolder.mkdir();
		}
		return logFolder.getAbsolutePath();
	}

	/**
	 * 
	 * @return
	 */
	private static String getOutputPath() {
		File outputFolder = new File(getProjectPath() + SEPARATOR + OUTPUT_FOLDER);
		if (! outputFolder.exists() || ! outputFolder.isDirectory()) {
			outputFolder.mkdir();
		}
		return outputFolder.getAbsolutePath();
	}

	/**
	 * 
	 * @param folder
	 * @param methodName
	 * @return
	 */
	private static File getPathOfFileAndHandleExistence(File folder, String methodName) {
		File file = new File(folder.getAbsolutePath() + SEPARATOR + methodName + ".txt");
		int i = 1;
		while (file.exists()) {
			file = new File(folder.getAbsolutePath() + SEPARATOR + methodName + "-"
					+ String.valueOf(i++) + ".txt");
		}
		return file;
	}

	/**
	 * 
	 * @param sootMethod
	 * @param logger
	 * @return
	 */
	private static String getPathOfLogFile(SootMethod sootMethod, SootLogger logger) {
		String className = sootMethod.getDeclaringClass().getName();
		File classOutputDir = new File(getLogPath(logger) + SEPARATOR + className);
		if (!classOutputDir.exists() || !classOutputDir.isDirectory())
			classOutputDir.mkdir();
		String methodName = sootMethod.getSubSignature().replace(" ", "-");
		File file = getPathOfFileAndHandleExistence(classOutputDir, methodName);
		return file.getAbsolutePath();
	}

	/**
	 * 
	 * @return
	 */
	private static String getProjectPath() {
		String currentDir = new File("").getAbsolutePath();
		String currentDirName = currentDir.substring(currentDir.lastIndexOf(SEPARATOR) + 1);
		String parentDir = currentDir.substring(0, currentDir.lastIndexOf(SEPARATOR));
		if (currentDirName.length() > 0 && Character.isUpperCase(currentDirName.charAt(0))) {
			parentDir = currentDir;
		}
		return parentDir;
	}

	/**
	 * 
	 * @param deleteOld
	 * @return
	 */
	public static String getSerializeFile(boolean deleteOld) {
		File serializeFile = new File(getSerializePath() + SEPARATOR + SERIALIZE_FILE);
		if (serializeFile.exists() && deleteOld) {
			serializeFile.delete();
		}
		return serializeFile.getAbsolutePath();
	}

	/**
	 * 
	 * @return
	 */
	private static String getSerializePath() {
		File serializeFolder = new File(getOutputPath() + SEPARATOR + SERIALIZE_FOLDER);
		if (! serializeFolder.exists() || ! serializeFolder.isDirectory()) {
			serializeFolder.mkdir();
		}
		return serializeFolder.getAbsolutePath();
	}

	/**
	 * 
	 * @param levels
	 * @return
	 */
	public static StreamHandler getStandardConsoleHandler(final Level[] levels) {
		StreamHandler handler = new StreamHandler(System.out, CONSOLE_FORMATTER);
		handler.setLevel(SootLoggerLevel.ALL);
		Filter filter = new Filter() {
			
			/**
			 * 
			 * @param record
			 * @return
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
	 * 
	 * @param levels
	 * @param sootMethod
	 * @param logger
	 * @return
	 * @throws IOException
	 * @throws SecurityException
	 * @throws NullPointerException
	 */
	public static FileHandler getStandardFileHandler(final Level[] levels, SootMethod sootMethod, SootLogger logger)
			throws IOException, SecurityException, NullPointerException {
		FileHandler handler = new FileHandler(getPathOfLogFile(sootMethod, logger));
		handler.setFormatter(FILE_FORMATTER);
		handler.setLevel(SootLoggerLevel.ALL);
		Filter filter = new Filter() {

			/**
			 * 
			 * @param record
			 * @return
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
	 * 
	 * @return
	 * @throws NullPointerException
	 */
	public static MessageStore restoreSerializedMessageStore() throws NullPointerException {
		try
		{
			FileInputStream fileInputStream = new FileInputStream(SootLoggerConfiguration.getSerializeFile(false));
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			MessageStore messageStore = (MessageStore) objectInputStream.readObject();
			objectInputStream.close();
			return messageStore;
		}
		catch (IOException | ClassNotFoundException e) {
			throw new NullPointerException("Error during deserialization of the serialized message store file");
		}
	}

	/**
	 * 
	 * @param level
	 * @param levels
	 * @return
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
}

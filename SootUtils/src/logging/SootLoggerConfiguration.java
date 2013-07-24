package logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;


import soot.SootMethod;

public class SootLoggerConfiguration {

	private static final SootLoggerConsoleFormatter CONSOLE_FORMATTER = new SootLoggerConsoleFormatter();
	private static final SootLoggerJimpleFormatter JIMPLE_FORMATTER = new SootLoggerJimpleFormatter();
	private static final SootLoggerFileFormatter FILE_FORMATTER = new SootLoggerFileFormatter();
	private static final String SEPARATOR = System.getProperty("file.separator");
	private static final String OUTPUT_FOLDER = "output";
	private static final String JIMPLE_FOLDER = "jimple";
	private static final String LOG_FOLDER = "log";
	
	
	private static String getProjectPath() {
		String currentDir = new File("").getAbsolutePath();
		String parentDir = currentDir.substring(0, currentDir.lastIndexOf(SEPARATOR));
		return parentDir;
	}
	
	private static String getOutputPath() {
		File outputFolder = new File(getProjectPath() + SEPARATOR + OUTPUT_FOLDER);
		if (! outputFolder.exists() || ! outputFolder.isDirectory()) {
			outputFolder.mkdir();
		}
		return outputFolder.getAbsolutePath();
	}
	
	private static String getLoggerPath(SootLogger logger) {
		String loggerPath = logger.getLoggerPath();
		File loggerFolder = new File(loggerPath.equals("") ? getOutputPath() : (getOutputPath() + SEPARATOR + loggerPath));
		if (! loggerFolder.exists() || ! loggerFolder.isDirectory()) {
			loggerFolder.mkdir();
		}
		return loggerFolder.getAbsolutePath();
	}
	
	private static String getJimplePath(SootLogger logger) {
		File jimpleFolder = new File(getLoggerPath(logger) + SEPARATOR + JIMPLE_FOLDER);
		if (! jimpleFolder.exists() || ! jimpleFolder.isDirectory()) {
			jimpleFolder.mkdir();
		}
		return jimpleFolder.getAbsolutePath();
	}
	
	private static String getLogPath(SootLogger logger) {
		File logFolder = new File(getLoggerPath(logger) + SEPARATOR + LOG_FOLDER);
		if (! logFolder.exists() || ! logFolder.isDirectory()) {
			logFolder.mkdir();
		}
		return logFolder.getAbsolutePath();
	}
	
	private static String getPathOfLogFile(SootMethod sootMethod, SootLogger logger) {
		String className = sootMethod.getDeclaringClass().getName();
		File classOutputDir = new File(getLogPath(logger) + SEPARATOR + className);
		if (!classOutputDir.exists() || !classOutputDir.isDirectory())
			classOutputDir.mkdir();
		String methodName = sootMethod.getSubSignature().replace(" ", "-");
		File file = getPathOfFileAndHandleExistence(classOutputDir, methodName);
		return file.getAbsolutePath();
	}
	
	private static String getPathOfJimpleFile(SootMethod sootMethod, SootLogger logger) {
		String className = sootMethod.getDeclaringClass().getName();
		File classOutputDir = new File(getJimplePath(logger) + SEPARATOR + className);
		if (!classOutputDir.exists() || !classOutputDir.isDirectory())
			classOutputDir.mkdir();
		String methodName = sootMethod.getSubSignature().replace(" ", "-");
		File file = getPathOfFileAndHandleExistence(classOutputDir, methodName);
		return file.getAbsolutePath();
	}

	private static File getPathOfFileAndHandleExistence(File folder, String methodName) {
		File file = new File(folder.getAbsolutePath() + SEPARATOR + methodName + ".txt");
		int i = 1;
		while (file.exists()) {
			file = new File(folder.getAbsolutePath() + SEPARATOR + methodName + "-"
					+ String.valueOf(i++) + ".txt");
		}
		return file;
	}
	
	public static StreamHandler getStandardConsoleHandler(final Level[] levels) {

		StreamHandler handler = new StreamHandler(System.out, CONSOLE_FORMATTER);
		handler.setLevel(SootLoggerLevel.ALL);
		Filter filter = new Filter() {

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
	
	public static FileHandler getStandardFileHandler(final Level[] levels, SootMethod sootMethod, SootLogger logger)
			throws IOException, SecurityException, NullPointerException {
		FileHandler handler = new FileHandler(getPathOfLogFile(sootMethod, logger));
		handler.setFormatter(FILE_FORMATTER);
		handler.setLevel(SootLoggerLevel.ALL);
		Filter filter = new Filter() {

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

	public static FileHandler getJimpleFileHandler(SootMethod sootMethod, SootLogger logger) throws IOException, SecurityException, NullPointerException {
		FileHandler handler = new FileHandler(getPathOfJimpleFile(sootMethod, logger));
		handler.setFormatter(JIMPLE_FORMATTER);
		handler.setLevel(SootLoggerLevel.JIMPLE);
		Filter filter = new Filter() {

			@Override
			public boolean isLoggable(LogRecord record) {
				return record.getLevel().equals(SootLoggerLevel.JIMPLE);
			}

		};
		handler.setFilter(filter);
		return handler;
	}
	
	private static boolean containsArraySpecificLevel(Level[] levels, Level specific) {
		for (Level level : levels) {
			if (level.equals(specific))
				return true;
		}
		return false;
	}

}

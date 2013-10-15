package logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.ExtendedHeadingInformation;
import model.HeadingInformation;
import model.MessageStore;
import model.Settings;
import soot.SootMethod;
import utils.LoggerMessages;

/**
 * <h1>Logger for Soot analyses</h1>
 * 
 * The {@link SootLogger} encapsulates a {@link Logger} and allows to log messages with customized
 * log levels ({@link SootLoggerLevel}) during a Soot analysis. Therefore, the logger provides for
 * each log level a log level method (e.g. {@link SootLogger#error(String, long, String)}). The
 * export is provided as standard for the console and if enabled also to a file. This behaviour can
 * be enable with the constructor of the {@link SootLogger}. In addition, a file handler must be
 * added so that the messages are stored in a file with a chosen name. Also the logger allows the
 * printing of specific levels, i.e. that the logger only outputs messages of the selected levels
 * (Note that if the level contains the level {@link Level#ALL} all messages will be printed , if it
 * contains the level {@link Level#OFF} no message will be printed). This level selection is also
 * set via the constructor. Another property of the logger is that the messages are stored in a
 * persistent message store ( {@link SootLogger#messageStore}), so that the messages can also be
 * output after analysis. If the messages should be printed directly, the corresponding flag of the
 * constructor must be set to {@code true}. Note that the class {@link SootLoggerUtils} provides
 * methods which return handlers for this logger.<br />
 * 
 * For logging messages please use the log level methods:
 * <ul>
 * <li>{@link SootLogger#configuration(Settings)}</li>
 * <li>{@link SootLogger#debug(String, long, String)}</li>
 * <li>{@link SootLogger#error(String, long, String)}</li>
 * <li>{@link SootLogger#exception(String, long, String, Throwable)}</li>
 * <li>{@link SootLogger#information(String, long, String)}</li>
 * <li>{@link SootLogger#warning(String, long, String)}</li>
 * </ul>
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.4
 * @see SootLoggerLevel
 * @see SootLoggerUtils
 * @see MessageStore
 */
public class SootLogger {

	/** Name of the log file which contains all messages of one specific source file. */
	private static final String FILE_ALL_NAME = "all";
	/** Name of the folder in which the log files are put into it. */
	private static final String OUTPUT_FOLDER = "general";
	/**
	 * The {@link Logger} instance which handles the logging and the handler and which is extended
	 * and encapsulated by the {@link SootLogger}.
	 */
	protected static final Logger LOG = Logger.getLogger(SootLogger.class.getName());
	/** Indicates whether the logger should export also the logged messages as text files. */
	protected final boolean exportFile;
	/** Indicates whether the logger should print the messages during the analysis. */
	protected boolean instantLogging = false;
	/**
	 * Array of levels which contains those levels that should be printed by the logger. I.e. if the
	 * array contains the level {@link SootLoggerLevel#ERROR}, then all error messages will be
	 * printed. Note that if the level array contains {@link Level#ALL} then all kinds of messages
	 * will be printed. If the level array contains {@link Level#OFF} then no message will be
	 * printed.
	 */
	protected final Level[] levels;
	/**
	 * {@link MessageStore} that stores all messages of the Level
	 * {@link SootLoggerLevel#CONFIGURATION}, {@link SootLoggerLevel#DEBUG},
	 * {@link SootLoggerLevel#ERROR}, {@link SootLoggerLevel#EXCEPTION},
	 * {@link SootLoggerLevel#INFORMATION}, {@link SootLoggerLevel#SECURITY},
	 * {@link SootLoggerLevel#SECURITYCHECKER}, {@link SootLoggerLevel#SIDEEFFECT} and
	 * {@link SootLoggerLevel#WARNING} persistently, so that these messages are available after the
	 * analysis.
	 */
	protected final MessageStore messageStore = new MessageStore();
	/**
	 * Handler that exports logged messages and writes them to the standard output console. This
	 * handler will be formatted by the {@link SootLoggerConsoleFormatter}.
	 */
	protected Handler standardConsoleHandler = null;
	/**
	 * Handler that exports logged messages and writes them into a file. This handler will be
	 * formatted by the {@link SootLoggerFileFormatter}.
	 */
	protected Handler standardFileHandler = null;
	/**
	 * Indicates whether the logged messages which have a customized level, should be stored in the
	 * {@link SootLogger#messageStore}.
	 */
	protected boolean storeMessages = true;

	/**
	 * Constructor of a {@link SootLogger} which allows to log messages with customized levels.
	 * Depending on the given flag the logger will export the messages not only to the console but
	 * also to a file. Another flag indicates whether the logger should output the messages exactly
	 * at the time in which he gets the message. The given levels represent those levels which are
	 * enabled for exporting to the console and to the file. Note that if the level array contains
	 * {@link Level#ALL} then all kinds of messages will be printed. If the level array contains
	 * {@link Level#OFF} then no message will be printed.
	 * 
	 * @param fileExport
	 *            Indicates whether the messages should be logged also in a file.
	 * @param instantLogging
	 *            Indicates whether the messages should be exported exactly in the moment the logger
	 *            get the message.
	 * @param levelList
	 *            Array of levels which are enabled for the logging.
	 * @see SootLogger#setupStandardConfigurations()
	 */
	public SootLogger(boolean fileExport, boolean instantLogging, Level[] levelList) {
		super();
		this.exportFile = fileExport;
		this.instantLogging = instantLogging;
		this.levels = (levelList != null) ? levelList : new Level[] {};
		setupStandardConfigurations();
	}

	/**
	 * Generates a new file handler and adds this file handler to the logger (see
	 * {@link SootLoggerUtils#getStandardFileHandler(Level[], String, String, SootLogger)}). The
	 * file which will contain the logged messages is named by the given String. The log file will
	 * be formatted by the {@link SootLoggerFileFormatter}. Note: the exporting to a file has to be
	 * enabled.
	 * 
	 * @param fileName
	 *            Name of the file which will contain the logged messages.
	 * @see SootLoggerUtils#getStandardFileHandler(Level[], String, String, SootLogger)
	 * @see SootLoggerFileFormatter
	 */
	public void addStandardFileHandlerForFile(String fileName) {
		if (exportFile) {
			try {
				this.standardFileHandler = SootLoggerUtils.getStandardFileHandler(levels,
						FILE_ALL_NAME, fileName, this);
				LOG.addHandler(this.standardFileHandler);
			} catch (SecurityException | NullPointerException | IOException e) {
				LOG.log(SootLoggerLevel.EXCEPTION, LoggerMessages.creationOfhandlerImpossible(), e);
			}
		}
	}

	/**
	 * Generates a new file handler and adds this file handler to the logger (see
	 * {@link SootLoggerUtils#getStandardFileHandler(Level[], String, String, SootLogger)}). The
	 * file which will contain the logged messages is named by the given {@link SootMethod}. The log
	 * file will be formatted by the {@link SootLoggerFileFormatter}. Note: the exporting to a file
	 * has to be enabled.
	 * 
	 * @param sootMethod
	 *            Method by which the file which will contain the logged messages is named.
	 * @see SootLoggerUtils#getStandardFileHandler(Level[], String, String, SootLogger)
	 * @see SootLoggerFileFormatter
	 */
	public void addStandardFileHandlerForMethod(SootMethod sootMethod) {
		String methodName = sootMethod.getSubSignature().replace(" ", "-");
		String className = sootMethod.getDeclaringClass().getName();
		if (exportFile) {
			try {
				this.standardFileHandler = SootLoggerUtils.getStandardFileHandler(levels,
						methodName, className, this);
				LOG.addHandler(this.standardFileHandler);
			} catch (SecurityException | NullPointerException | IOException e) {
				LOG.log(SootLoggerLevel.EXCEPTION, LoggerMessages.creationOfhandlerImpossible(), e);
			}
		}
	}

	/**
	 * Writes the given {@link Settings} to the available handlers of the logger
	 * {@link SootLogger#LOG} with the level {@link SootLoggerLevel#CONFIGURATION} depending on the
	 * fact whether the logger should log instantaneously. Right before the logged message a heading
	 * will be created that illustrates the level. Also, the given {@link Settings} instance will be
	 * stored in the {@link SootLogger#messageStore} persistently in a separate store for settings.<br />
	 * 
	 * @param settings
	 *            Settings that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void configuration(Settings settings) {
		if (storeMessages) {
			this.messageStore.addConfiguration(settings);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.CONFIGURATION)) {
				HeadingInformation info = new HeadingInformation(0);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.CONFIGURATION_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.CONFIGURATION, SootLoggerUtils.generateSettingsString(settings));
		}
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with
	 * the level {@link SootLoggerLevel#DEBUG} depending on the fact whether the logger should log
	 * instantaneously. Right before the logged message a heading will be created that illustrates
	 * the level, the given file name and the given source line number where the message was
	 * generated. Also, the given message will be stored in the {@link SootLogger#messageStore}
	 * persistently.<br />
	 * <b>This logger level method should be used for printing debug information.</b>
	 * 
	 * @param fileName
	 *            File name in which the given message is generated.
	 * @param srcLn
	 *            Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void debug(String fileName, long srcLn, String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.DEBUG);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.DEBUG)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.DEBUG_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.DEBUG, msg);
		}
	}

	/**
	 * Method which disables the storing of messages in the {@link SootLogger#messageStore} and
	 * which also enables the writing of the messages to the handlers of the logger
	 * {@link SootLogger#LOG}. The method will return whether the instant logging was enabled or
	 * disabled. This method should be used in the case of printing all the stored messages after
	 * the analysis.
	 * 
	 * @return The old value of {@link SootLogger#instantLogging}, i.e. whether the instant logging
	 *         was enabled ({@code true}) or disabled ({@code false}) before invoking this method.
	 */
	public boolean disableStoring() {
		boolean instantLoggingOld = instantLogging;
		instantLogging = true;
		storeMessages = false;
		return instantLoggingOld;
	}

	/**
	 * Method, which restores the settings that existed before deactivation of storing the messages
	 * (see {@link SootLogger#disableStoring()}). The given value should be the value of
	 * {@link SootLogger#instantLogging} before the invoke of the method
	 * {@link SootLogger#disableStoring()}. Thus, this given value will be set to to variable which
	 * indicates the instant logging status and the value which indicates the status of storing
	 * messages will be set to {@code true}.
	 * 
	 * @param instantLogging
	 *            Value of {@link SootLogger#instantLogging} before the invocation of the method
	 *            {@link SootLogger#disableStoring()}.
	 */
	public void enableStoring(boolean instantLogging) {
		this.instantLogging = instantLogging;
		storeMessages = true;
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with
	 * the level {@link SootLoggerLevel#ERROR} depending on the fact whether the logger should log
	 * instantaneously. Right before the logged message a heading will be created that illustrates
	 * the level, the given file name and the given source line number where the message was
	 * generated. Also, the given message will be stored in the {@link SootLogger#messageStore}
	 * persistently.<br />
	 * <b>An error occurs during the analysis and is caused by erroneous annotations, incorrect
	 * levels and other incorrect conditions. But errors are not caused by a caught exception or by
	 * internal Java exceptions. Note: this logger level method shouldn't be used to print side
	 * effects, <em>security level</em> violations or the implementation checking of the
	 * {@code SecurityLevel} subclass.</b>
	 * 
	 * @param fileName
	 *            File name in which the given message is generated.
	 * @param srcLn
	 *            Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void error(String fileName, long srcLn, String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.ERROR);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.ERROR)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.ERROR_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.ERROR, msg);
		}
	}

	/**
	 * Writes the given message as well as the given {@link Throwable} to the available handlers of
	 * the logger {@link SootLogger#LOG} with the level {@link SootLoggerLevel#EXCEPTION} depending
	 * on the fact whether the logger should log instantaneously. Right before the logged message a
	 * heading will be created that illustrates the level, the given file name and the given source
	 * line number where the message was generated. Also, the given message will be stored in the
	 * {@link SootLogger#messageStore} persistently.<br />
	 * <b>Exception messages are errors which occur during the analysis and were caused by erroneous
	 * annotations, incorrect levels, switch exceptions or by internal Java exceptions. Note: this
	 * logger level method shouldn't be used to print side effects, <em>security level</em>
	 * violations or the implementation checking of the {@code SecurityLevel} subclass.</b>
	 * 
	 * @param fileName
	 *            File name in which the given message is generated.
	 * @param srcLn
	 *            Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 * @param e
	 *            The corresponding {@link Throwable} to the given message.
	 */
	public void exception(String fileName, long srcLn, String msg, Throwable e) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.EXCEPTION, e);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.EXCEPTION)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.EXCEPTION_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.EXCEPTION, msg, e);
		}
	}

	/**
	 * Returns the message store ({@link SootLogger#messageStore}) which will contain all important
	 * messages after the analysis.
	 * 
	 * @return The message store of this logger containing all important logged messages.
	 */
	public MessageStore getMessageStore() {
		return this.messageStore;
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with
	 * the level {@link SootLoggerLevel#INFORMATION} depending on the fact whether the logger should
	 * log instantaneously. Right before the logged message a heading will be created that
	 * illustrates the level, the given file name and the given source line number where the message
	 * was generated. Also, the given message will be stored in the {@link SootLogger#messageStore}
	 * persistently.<br />
	 * <b>An information message has no importance and is only of informational nature.</b>
	 * 
	 * @param fileName
	 *            File name in which the given message is generated.
	 * @param srcLn
	 *            Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void information(String fileName, long srcLn, String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.INFORMATION);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.INFORMATION)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.INFORMATION_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.INFORMATION, msg);
		}
	}

	/**
	 * Stops the exporting to the {@link SootLogger#standardFileHandler} if it exists by removing
	 * this handler from the logger and by closing it. After calling this method, a new file handler
	 * must be added, otherwise no messages are written to a file.
	 */
	public void removeStandardFileHandler() {
		if (exportFile && this.standardFileHandler != null) {
			try {
				LOG.removeHandler(this.standardFileHandler);
				this.standardFileHandler.close();
				this.standardFileHandler = null;
			} catch (SecurityException e) {
				LOG.log(SootLoggerLevel.EXCEPTION, LoggerMessages.closeFileHandlerWrong(), e);
			}
		}
	}

	/**
	 * Method which serializes the complete {@link SootLogger#messageStore} to a file. The file name
	 * of this file is generate by {@link SootLoggerUtils#getSerializeFile(boolean)}, so that the
	 * message store is available for other programs or another run of the analysis.
	 * 
	 * @see SootLoggerUtils#getSerializeFile(boolean)
	 */
	public void storeSerializedMessageStore() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(
					SootLoggerUtils.getSerializeFile(true));
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(messageStore);
			objectOutputStream.close();
		} catch (IOException e) {
			LOG.log(SootLoggerLevel.EXCEPTION, LoggerMessages.creationSerializedNotPossible(), e);
		}
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with
	 * the level {@link SootLoggerLevel#STRUCTURE} depending on the fact whether the logger should
	 * log instantaneously. <br />
	 * <b>This logger level method should be used only for structural information of the analysis
	 * such as the checking of a new method or a new file.</b>
	 * 
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void structure(String msg) {
		if (instantLogging) {
			LOG.log(SootLoggerLevel.STRUCTURE, msg);
		}
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with
	 * the level {@link SootLoggerLevel#WARNING} depending on the fact whether the logger should log
	 * instantaneously. Right before the logged message a heading will be created that illustrates
	 * the level, the given file name and the given source line number where the message was
	 * generated. Also, the given message will be stored in the {@link SootLogger#messageStore}
	 * persistently.<br />
	 * <b>A warning message is an important information about the analysis but is not caused by an
	 * error or an exception, rather if the analysis makes specific assumptions. E.g. if a library
	 * method was used (the library is not annotated, thus the analysis has to handle this fact
	 * somehow).</b>
	 * 
	 * @param fileName
	 *            File name in which the given message is generated.
	 * @param srcLn
	 *            Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void warning(String fileName, long srcLn, String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.WARNING);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.WARNING)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.WARNING_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.WARNING, msg);
		}
	}

	/**
	 * Method configures the {@link SootLogger#LOG} so that it outputs all levels (the filtering of
	 * enabled levels is done by the corresponding logger level methods and by the formatters). As
	 * well the method adds the handler that outputs messages on the console (see
	 * {@link SootLogger#standardConsoleHandler}) to the logger.
	 * 
	 * @see SootLoggerUtils#getStandardConsoleHandler(Level[])
	 */
	private void setupStandardConfigurations() {
		LOG.setUseParentHandlers(false);
		LOG.setLevel(Level.ALL);
		this.standardConsoleHandler = SootLoggerUtils.getStandardConsoleHandler(levels);
		LOG.addHandler(this.standardConsoleHandler);
	}

	/**
	 * Method which returns the {@link SootLogger} specific output folder name.
	 * 
	 * @return The logger specific output folder name.
	 */
	protected String getLoggerFolder() {
		return OUTPUT_FOLDER;
	}

	/**
	 * Checks whether the given level is enabled for logging by this {@link SootLogger}. E.g. if
	 * {@link SootLogger#levels} contains the given level or this array contains the level
	 * {@link Level#ALL}, then the method will return {@code true}. If this array does not contain
	 * the given level or it contains the level {@link Level#OFF}, then the method will return
	 * {@code false}.
	 * 
	 * @param level
	 *            Level for which should be checked whether it is enabled for logging.
	 * @return {@code true} if the given level is enabled, otherwise {@code false}.
	 */
	protected boolean isLevelEnabled(Level level) {
		return SootLoggerUtils.shouldLogLevel(level, levels);
	}

}

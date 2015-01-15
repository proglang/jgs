package logging;

import static java.util.Locale.ENGLISH;
import static java.util.logging.Level.ALL;
import static java.util.logging.Logger.getLogger;
import static logging.AnalysisLogLevel.CONFIGURATION;
import static logging.AnalysisLogLevel.CONFIGURATION_NAME;
import static logging.AnalysisLogLevel.HEADING;
import static logging.AnalysisLogLevel.SECURITY;
import static logging.AnalysisLogLevel.SECURITY_NAME;
import static logging.AnalysisLogLevel.SIDEEFFECT;
import static logging.AnalysisLogLevel.SIDEEFFECT_NAME;
import static logging.AnalysisLogLevel.STRUCTURE;
import static logging.AnalysisLogLevel.WARNING;
import static logging.AnalysisLogLevel.WARNING_NAME;
import static logging.AnalysisLogUtils.FILE_SUFFIX_JAVA;
import static logging.AnalysisLogUtils.generateSettingsString;
import static logging.AnalysisLogUtils.getStandardConsoleHandler;
import static logging.AnalysisLogUtils.shouldLogLevel;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.MessageStore;
import model.MessageStore.Message;

/**
 * <h1>Logger for the security analysis</h1>
 * 
 * The {@link SideEffectLogger} extends the {@link SootLogger} and provides additional effect and security specific log level methods as
 * well as a method that allows to print the messages which are stored in the {@link SootLogger#messageStore} after analysis (see
 * {@link AnalysisLog#printAllMessages()}).<br />
 * Additional log level methods are:
 * <ul>
 * <li>{@link AnalysisLog#effect(String, long, String)}</li>
 * <li>{@link AnalysisLog#security(String, long, String)}</li>
 * <li>{@link AnalysisLog#securitychecker(String)}</li>
 * <li>{@link AnalysisLog#securitychecker(String, Throwable)}</li>
 * </ul>
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.5
 */
public class AnalysisLog {

	/**
	 * The {@link Logger} instance which handles the logging and the handler and which is extended and encapsulated by the {@link SootLogger}.
	 */
	protected static final Logger LOG = getLogger(AnalysisLog.class.getName());
	/** Indicates whether the logger should print the messages during the analysis. */
	protected boolean instantLogging = false;
	/**
	 * Array of levels which contains those levels that should be printed by the logger. I.e. if the array contains the level
	 * {@link AnalysisLogLevel#ERROR}, then all error messages will be printed. Note that if the level array contains {@link Level#ALL} then
	 * all kinds of messages will be printed. If the level array contains {@link Level#OFF} then no message will be printed.
	 */
	protected final Level[] levels;
	/**
	 * {@link MessageStore} that stores all messages of the Level {@link AnalysisLogLevel#CONFIGURATION}, {@link AnalysisLogLevel#DEBUGGING},
	 * {@link AnalysisLogLevel#ERROR}, {@link AnalysisLogLevel#EXCEPTION}, {@link AnalysisLogLevel#INFORMATION},
	 * {@link AnalysisLogLevel#SECURITY}, {@link AnalysisLogLevel#SECURITYCHECKER}, {@link AnalysisLogLevel#SIDEEFFECT} and
	 * {@link AnalysisLogLevel#WARNING} persistently, so that these messages are available after the analysis.
	 */
	protected final MessageStore messageStore = new MessageStore();
	/**
	 * Handler that exports logged messages and writes them to the standard output console. This handler will be formatted by the
	 * {@link AnalysisLogConsoleFormatter}.
	 */
	protected Handler standardConsoleHandler = null;
	/**
	 * Indicates whether the logged messages which have a customized level, should be stored in the {@link SootLogger#messageStore}.
	 */
	protected boolean storeMessages = true;

	/**
	 * Constructor of a {@link AnalysisLog} which allows to log messages with customized levels, especially levels which are in relation to
	 * the security analysis. Depending on the given flag the logger will export the messages not only to the console but also to a file.
	 * Another flag indicates whether the logger should output the messages exactly at the time in which he gets the message. The given levels
	 * represent those levels which are enabled for exporting to the console and to the file. Note that if the level array contains
	 * {@link Level#ALL} then all kinds of messages will be printed. If the level array contains {@link Level#OFF} then no message will be
	 * printed.s
	 * 
	 * @param instantLogging
	 *          Indicates whether the messages should be exported exactly in the moment the logger get the message.
	 * @param levels
	 *          Array of levels which are enabled for the logging.
	 * @see SootLogger#SootLogger(boolean, boolean, Level[])
	 */
	public AnalysisLog(boolean instantLogging, Level[] levelList) {
		this.instantLogging = instantLogging;
		this.levels = (levelList != null) ? levelList : new Level[] {};
		setupStandardConfigurations();
	}

	/**
	 * Writes the given {@link Settings} to the available handlers of the logger {@link SootLogger#LOG} with the level
	 * {@link AnalysisLogLevel#CONFIGURATION} depending on the fact whether the logger should log instantaneously. Right before the logged
	 * message a heading will be created that illustrates the level. Also, the given {@link Settings} instance will be stored in the
	 * {@link SootLogger#messageStore} persistently in a separate store for settings.<br />
	 * 
	 * @param settings
	 *          Settings that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void configuration(Settings settings) {
		if (storeMessages) {
			this.messageStore.addConfiguration(settings);
		}
		if (instantLogging) {
			if (isLevelEnabled(CONFIGURATION)) {
				HeadingInformation info = new HeadingInformation(0);
				LOG.log(HEADING, CONFIGURATION_NAME.toUpperCase(ENGLISH), new Object[] { info });
			}
			LOG.log(CONFIGURATION, generateSettingsString(settings));
		}
	}

	/**
	 * Method which disables the storing of messages in the {@link SootLogger#messageStore} and which also enables the writing of the messages
	 * to the handlers of the logger {@link SootLogger#LOG}. The method will return whether the instant logging was enabled or disabled. This
	 * method should be used in the case of printing all the stored messages after the analysis.
	 * 
	 * @return The old value of {@link SootLogger#instantLogging}, i.e. whether the instant logging was enabled ({@code true}) or disabled (
	 *         {@code false}) before invoking this method.
	 */
	public boolean disableStoring() {
		boolean instantLoggingOld = instantLogging;
		instantLogging = true;
		storeMessages = false;
		return instantLoggingOld;
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with the level
	 * {@link AnalysisLogLevel#SIDEEFFECT} depending on the fact whether the logger should log instantaneously. Right before the logged
	 * message a heading will be created that illustrates the level, the given file name and the given source line number where the message
	 * was generated. Also, the given message will be stored in the {@link SootLogger#messageStore} persistently.<br />
	 * <b>This logger level method should be used for side effect violations, e.g. if a <em>write effect</em> occurs inside of a method body,
	 * but the annotation of this method doesn't take this effect into account.</b>
	 * 
	 * @param fileName
	 *          File name in which the given message is generated.
	 * @param srcLn
	 *          Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *          Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void effect(String fileName, long srcLn, String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, SIDEEFFECT);
		}
		if (instantLogging) {
			if (isLevelEnabled(SIDEEFFECT)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(HEADING, SIDEEFFECT_NAME.toUpperCase(ENGLISH), new Object[] { info });
			}
			LOG.log(SIDEEFFECT, msg);
		}
	}

	/**
	 * Method, which restores the settings that existed before deactivation of storing the messages (see {@link SootLogger#disableStoring()}).
	 * The given value should be the value of {@link SootLogger#instantLogging} before the invoke of the method
	 * {@link SootLogger#disableStoring()}. Thus, this given value will be set to to variable which indicates the instant logging status and
	 * the value which indicates the status of storing messages will be set to {@code true}.
	 * 
	 * @param instantLogging
	 *          Value of {@link SootLogger#instantLogging} before the invocation of the method {@link SootLogger#disableStoring()}.
	 */
	public void enableStoring(boolean instantLogging) {
		this.instantLogging = instantLogging;
		storeMessages = true;
	}

	/**
	 * Returns the message store ({@link SootLogger#messageStore}) which will contain all important messages after the analysis.
	 * 
	 * @return The message store of this logger containing all important logged messages.
	 */
	public MessageStore getMessageStore() {
		return this.messageStore;
	}

	/**
	 * Method that prints the content of the message store {@link SootLogger#messageStore}. I.e. the configuration messages will be printed
	 * first, after that all message of the contained file names will be printed in ascending order by the source line number. Therefore the
	 * storing of the messages has to be disabled before the printing of the messages starts. After the printing the original instant logging
	 * state will be restored as well as the storing will be enabled.
	 * 
	 * @see SootLogger#disableStoring()
	 * @see SootLogger#enableStoring(boolean)
	 */
	public void printAllMessages() {
		boolean instantLoggingOld = disableStoring();
		List<Settings> settings = messageStore.getAllConfigurations();
		for (Settings setting : settings) {
			configuration(setting);
		}
		List<Message> messages = messageStore.getAllMessages();
		String currentFile = "";
		for (Message message : messages) {
			String msg = message.getMessage();
			long srcLn = message.getSrcLn();
			String fileName = message.getFileName();
			Level level = message.getLevel();
			if (!currentFile.equals(fileName)) {
				structure(fileName + FILE_SUFFIX_JAVA);
				currentFile = fileName;
			}
			if (level.equals(SIDEEFFECT)) {
				effect(fileName, srcLn, msg);
			} else if (level.equals(SECURITY)) {
				security(fileName, srcLn, msg);
			} else if (level.equals(WARNING)) {
				warning(fileName, srcLn, msg);
			}
		}
		enableStoring(instantLoggingOld);
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with the level
	 * {@link AnalysisLogLevel#SECURITY} depending on the fact whether the logger should log instantaneously. Right before the logged message
	 * a heading will be created that illustrates the level, the given file name and the given source line number where the message was
	 * generated. Also, the given message will be stored in the {@link SootLogger#messageStore} persistently.<br />
	 * <b>This logger level method should be used for security violations, e.g. if a method returns a value with a stronger
	 * <em>security level</em> than expected.</b>
	 * 
	 * @param fileName
	 *          File name in which the given message is generated.
	 * @param srcLn
	 *          Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *          Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void security(String fileName, long srcLn, String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, SECURITY);
		}
		if (instantLogging) {
			if (isLevelEnabled(SECURITY)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(HEADING, SECURITY_NAME.toUpperCase(ENGLISH), new Object[] { info });
			}
			LOG.log(SECURITY, msg);
		}
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with the level
	 * {@link AnalysisLogLevel#STRUCTURE} depending on the fact whether the logger should log instantaneously. <br />
	 * <b>This logger level method should be used only for structural information of the analysis such as the checking of a new method or a
	 * new file.</b>
	 * 
	 * @param msg
	 *          Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void structure(String msg) {
		if (instantLogging) {
			LOG.log(STRUCTURE, msg);
		}
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with the level {@link AnalysisLogLevel#WARNING}
	 * depending on the fact whether the logger should log instantaneously. Right before the logged message a heading will be created that
	 * illustrates the level, the given file name and the given source line number where the message was generated. Also, the given message
	 * will be stored in the {@link SootLogger#messageStore} persistently.<br />
	 * <b>A warning message is an important information about the analysis but is not caused by an error or an exception, rather if the
	 * analysis makes specific assumptions. E.g. if a library method was used (the library is not annotated, thus the analysis has to handle
	 * this fact somehow).</b>
	 * 
	 * @param fileName
	 *          File name in which the given message is generated.
	 * @param srcLn
	 *          Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *          Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void warning(String fileName, long srcLn, String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, WARNING);
		}
		if (instantLogging) {
			if (isLevelEnabled(WARNING)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(HEADING, WARNING_NAME.toUpperCase(ENGLISH), new Object[] { info });
			}
			LOG.log(WARNING, msg);
		}
	}

	/**
	 * Method configures the {@link SootLogger#LOG} so that it outputs all levels (the filtering of enabled levels is done by the
	 * corresponding logger level methods and by the formatters). As well the method adds the handler that outputs messages on the console
	 * (see {@link SootLogger#standardConsoleHandler}) to the logger.
	 * 
	 * @see AnalysisLogUtils#getStandardConsoleHandler(Level[])
	 */
	private void setupStandardConfigurations() {
		LOG.setUseParentHandlers(false);
		LOG.setLevel(ALL);
		this.standardConsoleHandler = getStandardConsoleHandler(levels);
		LOG.addHandler(this.standardConsoleHandler);
	}

	/**
	 * Checks whether the given level is enabled for logging by this {@link SootLogger}. E.g. if {@link SootLogger#levels} contains the given
	 * level or this array contains the level {@link Level#ALL}, then the method will return {@code true}. If this array does not contain the
	 * given level or it contains the level {@link Level#OFF}, then the method will return {@code false}.
	 * 
	 * @param level
	 *          Level for which should be checked whether it is enabled for logging.
	 * @return {@code true} if the given level is enabled, otherwise {@code false}.
	 */
	protected boolean isLevelEnabled(Level level) {
		return shouldLogLevel(level, levels);
	}

}

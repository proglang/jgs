package logging;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import model.ExtendedHeadingInformation;
import model.HeadingInformation;
import model.MessageStore.Message;
import model.Settings;
import utils.Predefined;

/**
 * <h1>Logger for the security analysis</h1>
 * 
 * The {@link SideEffectLogger} extends the {@link SootLogger} and provides additional effect and
 * security specific log level methods as well as a method that allows to print the messages which
 * are stored in the {@link SootLogger#messageStore} after analysis (see
 * {@link SecurityLogger#printAllMessages()}).<br />
 * Additional log level methods are:
 * <ul>
 * <li>{@link SecurityLogger#effect(String, long, String)}</li>
 * <li>{@link SecurityLogger#security(String, long, String)}</li>
 * <li>{@link SecurityLogger#securitychecker(String)}</li>
 * <li>{@link SecurityLogger#securitychecker(String, Throwable)}</li>
 * </ul>
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.5
 */
public class SecurityLogger extends SootLogger {

	/** Name of the folder in which the log files are put into it. */
	private static final String OUTPUT_FOLDER = "security";

	/**
	 * Constructor of a {@link SecurityLogger} which allows to log messages with customized levels,
	 * especially levels which are in relation to the security analysis. Depending on the given flag
	 * the logger will export the messages not only to the console but also to a file. Another flag
	 * indicates whether the logger should output the messages exactly at the time in which he gets
	 * the message. The given levels represent those levels which are enabled for exporting to the
	 * console and to the file. Note that if the level array contains {@link Level#ALL} then all
	 * kinds of messages will be printed. If the level array contains {@link Level#OFF} then no
	 * message will be printed.s
	 * 
	 * @param exportFile
	 *            Indicates whether the messages should be logged also in a file.
	 * @param instantLogging
	 *            Indicates whether the messages should be exported exactly in the moment the logger
	 *            get the message.
	 * @param levels
	 *            Array of levels which are enabled for the logging.
	 * @see SootLogger#SootLogger(boolean, boolean, Level[])
	 */
	public SecurityLogger(boolean exportFile, boolean instantLogging, Level[] levels) {
		super(exportFile, instantLogging, levels);
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with
	 * the level {@link SootLoggerLevel#SIDEEFFECT} depending on the fact whether the logger should
	 * log instantaneously. Right before the logged message a heading will be created that
	 * illustrates the level, the given file name and the given source line number where the message
	 * was generated. Also, the given message will be stored in the {@link SootLogger#messageStore}
	 * persistently.<br />
	 * <b>This logger level method should be used for side effect violations, e.g. if a
	 * <em>write effect</em> occurs inside of a method body, but the annotation of this method
	 * doesn't take this effect into account.</b>
	 * 
	 * @param fileName
	 *            File name in which the given message is generated.
	 * @param srcLn
	 *            Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void effect(String fileName, long srcLn, String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.SIDEEFFECT);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.SIDEEFFECT)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.SIDEEFFECT_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.SIDEEFFECT, msg);
		}
	}

	/**
	 * Method that prints the content of the message store {@link SootLogger#messageStore}. I.e. the
	 * configuration messages will be printed first, after that all message of the contained file
	 * names will be printed in ascending order by the source line number. Therefore the storing of
	 * the messages has to be disabled before the printing of the messages starts. After the
	 * printing the original instant logging state will be restored as well as the storing will be
	 * enabled.
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
				removeStandardFileHandler();
				structure(fileName + SootLoggerUtils.FILE_SUFFIX_JAVA);
				currentFile = fileName;
				addStandardFileHandlerForFile(fileName);
			}
			if (level.equals(SootLoggerLevel.SIDEEFFECT)) {
				effect(fileName, srcLn, msg);
			} else if (level.equals(SootLoggerLevel.SECURITY)) {
				security(fileName, srcLn, msg);
			} else if (level.equals(SootLoggerLevel.DEBUG)) {
				debug(fileName, srcLn, msg);
			} else if (level.equals(SootLoggerLevel.ERROR)) {
				error(fileName, srcLn, msg);
			} else if (level.equals(SootLoggerLevel.INFORMATION)) {
				information(fileName, srcLn, msg);
			} else if (level.equals(SootLoggerLevel.WARNING)) {
				warning(fileName, srcLn, msg);
			} else if (level.equals(SootLoggerLevel.SECURITYCHECKER)) {
				List<Throwable> exceptions = message.getExceptions();
				if (exceptions.size() == 0) {
					securitychecker(msg);
				} else {
					for (Throwable exception : exceptions) {
						securitychecker(msg, exception);
					}
				}
			} else if (level.equals(SootLoggerLevel.EXCEPTION)) {
				List<Throwable> exceptions = message.getExceptions();
				if (exceptions.size() == 0) {
					error(fileName, srcLn, msg);
				} else {
					for (Throwable exception : exceptions) {
						exception(fileName, srcLn, msg, exception);
					}
				}
			}
		}
		enableStoring(instantLoggingOld);
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with
	 * the level {@link SootLoggerLevel#SECURITY} depending on the fact whether the logger should
	 * log instantaneously. Right before the logged message a heading will be created that
	 * illustrates the level, the given file name and the given source line number where the message
	 * was generated. Also, the given message will be stored in the {@link SootLogger#messageStore}
	 * persistently.<br />
	 * <b>This logger level method should be used for security violations, e.g. if a method returns
	 * a value with a stronger <em>security level</em> than expected.</b>
	 * 
	 * @param fileName
	 *            File name in which the given message is generated.
	 * @param srcLn
	 *            Source line number of the given file name at which the given message is generated.
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void security(String fileName, long srcLn, String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.SECURITY);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.SECURITY)) {
				ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.SECURITY_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.SECURITY, msg);
		}
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with
	 * the level {@link SootLoggerLevel#SECURITYCHECKER} depending on the fact whether the logger
	 * should log instantaneously. Right before the logged message a heading will be created that
	 * illustrates the level, the expected file name where the message was generated. Also, the
	 * given message will be stored in the {@link SootLogger#messageStore} persistently.<br />
	 * <b>This logger level method should be used for printing violations during the check of the
	 * {@code SecurityLevel} implementation, e.g. if an id function for a specific
	 * <em>security level</em> does not exist.</b>
	 * 
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 */
	public void securitychecker(String msg) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, Predefined.IMPL_SL_CLASS_NAME, 0,
					SootLoggerLevel.SECURITYCHECKER, null);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.SECURITYCHECKER)) {
				HeadingInformation info = new HeadingInformation(1);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.SECURITYCHECKER_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.SECURITYCHECKER, msg);
		}
	}

	/**
	 * Writes the given message as well as the given {@link Throwable} to the available handlers of
	 * the logger {@link SootLogger#LOG} with the level {@link SootLoggerLevel#SECURITYCHECKER}
	 * depending on the fact whether the logger should log instantaneously. Right before the logged
	 * message a heading will be created that illustrates the level, the expected file name where
	 * the message was generated. Also, the given message will be stored in the
	 * {@link SootLogger#messageStore} persistently.<br />
	 * <b>This logger level method should be used for printing violations during the check of the
	 * {@code SecurityLevel} implementation, e.g. if an id function for a specific
	 * <em>security level</em> does not exist.</b>
	 * 
	 * @param msg
	 *            Message that should be exported by the logger {@link SootLogger#LOG}.
	 * @param e
	 *            The corresponding {@link Throwable} to the given message.
	 */
	public void securitychecker(String msg, Throwable e) {
		if (storeMessages) {
			this.messageStore.addMessage(msg, Predefined.IMPL_SL_CLASS_NAME, 0,
					SootLoggerLevel.SECURITYCHECKER, e);
		}
		if (instantLogging) {
			if (isLevelEnabled(SootLoggerLevel.SECURITYCHECKER)) {
				HeadingInformation info = new HeadingInformation(1);
				LOG.log(SootLoggerLevel.HEADING,
						SootLoggerLevel.SECURITYCHECKER_NAME.toUpperCase(Locale.ENGLISH),
						new Object[] { info });
			}
			LOG.log(SootLoggerLevel.SECURITYCHECKER, msg, e);
		}
	}

	/**
	 * Method which returns the {@link SecurityLogger} specific output folder name.
	 * 
	 * @return The logger specific output folder name.
	 */
	protected String getLoggerFolder() {
		return OUTPUT_FOLDER;
	}
}

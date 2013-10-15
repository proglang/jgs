package logging;

import java.util.logging.Level;

import model.ExtendedHeadingInformation;

/**
 * <h1>Logger for the side effect analysis</h1>
 * 
 * The {@link SideEffectLogger} extends the {@link SootLogger} and provides additional side effect
 * specific log level methods.<br />
 * Additional log level methods are: 
 * <ul>
 * <li>{@link SideEffectLogger#sideeffect(String, long, String)}</li>
 * </ul>
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see SootLogger
 */
public class SideEffectLogger extends SootLogger {

	/** Name of the folder in which the log files are put into it. */
	private static final String OUTPUT_FOLDER = "effect";

	/**
	 * Constructor of a {@link SideEffectLogger} which allows to log messages with customized
	 * levels, especially levels which are in relation to the side effect analysis. Depending on the
	 * given flag the logger will export the messages not only to the console but also to a file.
	 * The given levels represent those levels which are enabled for exporting to the console and to
	 * the file. Note that if the level array contains {@link Level#ALL} then all kinds of messages
	 * will be printed. If the level array contains {@link Level#OFF} then no message will be
	 * printed.
	 * 
	 * @param exportFile
	 *            Indicates whether the messages should be logged also in a file.
	 * @param levels
	 *            Array of levels which are enabled for the logging.
	 * @see SootLogger#SootLogger(boolean, boolean, Level[])
	 */
	public SideEffectLogger(boolean exportFile, Level[] levels) {
		super(exportFile, true, levels);
	}

	/**
	 * Writes the given message to the available handlers of the logger {@link SootLogger#LOG} with
	 * the level {@link SootLoggerLevel#SIDEEFFECT}. Right before the logged message a heading will
	 * be created that illustrates the level, the given file name and the given source line number
	 * where the message was generated. Also, the given message will be stored in the
	 * {@link SootLogger#messageStore} persistently.<br />
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
	public void sideeffect(String fileName, long srcLn, String msg) {
		this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.SIDEEFFECT);
		if (isLevelEnabled(SootLoggerLevel.SIDEEFFECT)) {
			ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
			LOG.log(SootLoggerLevel.HEADING, "SIDEEFFECT", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.SIDEEFFECT, msg);
	}

	/**
	 * Method which returns the {@link SideEffectLogger} specific output folder name.
	 * 
	 * @return The logger specific output folder name.
	 */
	protected String getLoggerFolder() {
		return OUTPUT_FOLDER;
	}

}

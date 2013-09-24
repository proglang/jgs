package logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import soot.SootMethod;

import model.Configurations;
import model.ExtendedHeadingInformation;
import model.MessageStore;
import model.MinimalHeadingInformation;

/**
 * 
 * @author Thomas Vogel
 * @version 0.4
 */
public class SootLogger {

	/** */
	protected static final Logger LOG = Logger.getLogger(SootLogger.class.getName());
	/** */
	protected static boolean exportFile = false;
	/** */
	protected static Level[] levels = {};
	/** */
	protected MessageStore messageStore = null;
	/** */
	protected Handler jimpleFileHandler = null;
	/** */
	protected Handler standardFileHandler = null;
	/** */
	protected Handler standardConsoleHandler = null;

	/**
	 * 
	 * @param fileExport
	 * @param levelList
	 */
	public SootLogger(boolean fileExport, Level[] levelList) {
		super();
		exportFile = fileExport;
		levels = levelList;
		setupStandardConfigurations();
	}

	/**
	 * 
	 * @param fileName
	 * @param srcLn
	 * @param msg
	 * @param e
	 */
	public void exception(String fileName, long srcLn, String msg, Throwable e) {
		this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.EXCEPTION);
		if (isLevelEnabled(SootLoggerLevel.EXCEPTION)) {
			ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
			LOG.log(SootLoggerLevel.HEADING, "EXCEPTION", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.EXCEPTION, msg, e);
	}

	/**
	 * 
	 * @param fileName
	 * @param srcLn
	 * @param msg
	 */
	public void error(String fileName, long srcLn, String msg) {
		this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.ERROR);
		if (isLevelEnabled(SootLoggerLevel.ERROR)) {
			ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
			LOG.log(SootLoggerLevel.HEADING, "ERROR", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.ERROR, msg);
	}

	/**
	 * 
	 * @param fileName
	 * @param srcLn
	 * @param msg
	 */
	public void warning(String fileName, long srcLn, String msg) {
		this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.WARNING);
		if (isLevelEnabled(SootLoggerLevel.WARNING)) {
			ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
			LOG.log(SootLoggerLevel.HEADING, "WARNING", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.WARNING, msg);
	}

	/**
	 * 
	 * @param fileName
	 * @param srcLn
	 * @param msg
	 */
	public void information(String fileName, long srcLn, String msg) {
		this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.INFORMATION);
		if (isLevelEnabled(SootLoggerLevel.INFORMATION)) {
			ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
			LOG.log(SootLoggerLevel.HEADING, "INFORMATION", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.INFORMATION, msg);
	}

	/**
	 * 
	 * @param msg
	 */
	public void structure(String msg) {
		LOG.log(SootLoggerLevel.STRUCTURE, msg);
	}

	/**
	 * 
	 * @param configurations
	 */
	public void configuration(Configurations configurations) {
		if (isLevelEnabled(SootLoggerLevel.CONFIGURATION)) {
			MinimalHeadingInformation info = new MinimalHeadingInformation(0);
			LOG.log(SootLoggerLevel.HEADING, "CONFIGURATION", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.CONFIGURATION, extractConfoguration(configurations));
	}

	/**
	 * 
	 * @param msg
	 */
	public void debug(String msg) {
		if (isLevelEnabled(SootLoggerLevel.DEBUG)) {
			MinimalHeadingInformation info = new MinimalHeadingInformation(1);
			LOG.log(SootLoggerLevel.HEADING, "DEBUG", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.DEBUG, msg);
	}

	/**
	 * 
	 */
	private void setupStandardConfigurations() {
		this.messageStore = new MessageStore();
		LOG.setUseParentHandlers(false);
		LOG.setLevel(Level.ALL);
		this.standardConsoleHandler = SootLoggerConfiguration.getStandardConsoleHandler(levels);
		LOG.addHandler(this.standardConsoleHandler);
	}

	/**
	 * 
	 * @param configurations
	 * @return
	 */
	private String extractConfoguration(Configurations configurations) {
		String result = "";
		Map<String, String> configurationMap = configurations.getConfigurationMap();
		for (String name : configurationMap.keySet()) {
			result += (result.equals("") ? "" : System.getProperty("line.separator")) + name + ": "
					+ configurationMap.get(name);
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	protected String getLoggerPath() {
		return "";
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 */
	protected boolean isLevelEnabled(Level level) {
		return SootLoggerConfiguration.shouldLogLevel(level, levels);
	}

	/**
	 * 
	 * @param sootMethod
	 */
	private void addStandardFileHandlerForMethod(SootMethod sootMethod) {
		if (exportFile) {
			try {
				this.standardFileHandler = SootLoggerConfiguration.getStandardFileHandler(levels,
						sootMethod, this);
				LOG.addHandler(this.standardFileHandler);
			} catch (SecurityException | NullPointerException | IOException e) {
				LOG.log(SootLoggerLevel.EXCEPTION, "Couldn't start the logging via file.", e);
			}
		}
	}

	/**
	 * 
	 */
	private void removeStandardFileHandler() {
		if (exportFile && this.standardFileHandler != null) {
			try {
				LOG.removeHandler(this.standardFileHandler);
				this.standardFileHandler.close();
				this.standardFileHandler = null;
			} catch (SecurityException e) {
				LOG.log(SootLoggerLevel.EXCEPTION, "Couldn't finish the logging via file.", e);
			}
		}
	}

	/**
	 * 
	 * @param sootMethod
	 */
	public void addAdditionalHandlerFor(SootMethod sootMethod) {
		addStandardFileHandlerForMethod(sootMethod);
	}

	/**
	 * 
	 */
	public void removeAdditional() {
		removeStandardFileHandler();
	}
	
	/**
	 * 
	 * @return
	 */
	public MessageStore getMessageStore() {
		return this.messageStore;
	}
	
	/**
	 * 
	 */
	public void storeSerializedMessageStore() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(SootLoggerConfiguration.getSerializeFile(true));
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(this.messageStore);
			objectOutputStream.close();
		} catch (IOException e) {
			LOG.log(SootLoggerLevel.EXCEPTION, "Couldn't create the serialized file.", e);
		}
	}
}

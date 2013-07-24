package logging;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import soot.SootMethod;

import model.Configurations;
import model.ExtendedHeadingInformation;
import model.MessageStore;
import model.MinimalHeadingInformation;

public class SootLogger {

	protected static final Logger LOG = Logger.getLogger(SootLogger.class.getName());

	protected static boolean EXPORT_FILE = false;
	protected static boolean EXPORT_JIMPLE = false;
	protected static Level[] LEVELS = {};

	protected MessageStore messageStore = null;
	protected Handler jimpleFileHandler = null;
	protected Handler standardFileHandler = null;
	protected Handler standardConsoleHandler = null;

	public SootLogger(boolean exportFile, boolean exportJimple, Level[] levels) {
		super();
		EXPORT_FILE = exportFile;
		EXPORT_JIMPLE = exportJimple;
		LEVELS = levels;
		setupStandardConfigurations();
	}

	public void exception(String fileName, long sourceLine, String msg, Throwable e) {
		ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, sourceLine, fileName);
		this.messageStore.addMessage(msg, fileName, sourceLine, SootLoggerLevel.EXCEPTION);
		LOG.log(SootLoggerLevel.HEADING, "EXCEPTION", new Object[] { info });
		LOG.log(SootLoggerLevel.EXCEPTION, msg, e);
	}

	public void exception(String msg, Throwable e) {
		MinimalHeadingInformation info = new MinimalHeadingInformation(1);
		LOG.log(SootLoggerLevel.HEADING, "EXCEPTION", new Object[] { info });
		LOG.log(SootLoggerLevel.EXCEPTION, msg, e);
	}

	public void error(String fileName, long sourceLine, String msg) {
		ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, sourceLine, fileName);
		this.messageStore.addMessage(msg, fileName, sourceLine, SootLoggerLevel.ERROR);
		LOG.log(SootLoggerLevel.HEADING, "ERROR", new Object[] { info });
		LOG.log(SootLoggerLevel.ERROR, msg);
	}

	public void warning(String fileName, long sourceLine, String msg) {
		ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, sourceLine, fileName);
		this.messageStore.addMessage(msg, fileName, sourceLine, SootLoggerLevel.WARNING);
		LOG.log(SootLoggerLevel.HEADING, "WARNING", new Object[] { info });
		LOG.log(SootLoggerLevel.WARNING, msg);
	}

	public void information(String fileName, long sourceLine, String msg) {
		ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, sourceLine, fileName);
		this.messageStore.addMessage(msg, fileName, sourceLine, SootLoggerLevel.INFORMATION);
		LOG.log(SootLoggerLevel.HEADING, "INFORMATION", new Object[] { info });
		LOG.log(SootLoggerLevel.INFORMATION, msg);
	}

	public void structure(String msg) {
		LOG.log(SootLoggerLevel.STRUCTURE, msg);
	}

	public void configuration(Configurations configurations) {
		MinimalHeadingInformation info = new MinimalHeadingInformation(0);
		LOG.log(SootLoggerLevel.HEADING, "CONFIGURATION", new Object[] { info });
		LOG.log(SootLoggerLevel.CONFIGURATION, extractConfoguration(configurations));
	}

	public void debug(String msg) {
		MinimalHeadingInformation info = new MinimalHeadingInformation(1);
		LOG.log(SootLoggerLevel.HEADING, "DEBUG", new Object[] { info });
		LOG.log(SootLoggerLevel.DEBUG, msg);
	}

	public void jimple(String msg) {
		LOG.log(SootLoggerLevel.JIMPLE, msg);
	}

	private void setupStandardConfigurations() {
		this.messageStore = new MessageStore();
		LOG.setUseParentHandlers(false);
		LOG.setLevel(Level.ALL);
		this.standardConsoleHandler = SootLoggerConfiguration.getStandardConsoleHandler(LEVELS);
		LOG.addHandler(this.standardConsoleHandler);
	}

	private String extractConfoguration(Configurations configurations) {
		String result = "";
		Map<String, String> configurationMap = configurations.getConfigurationMap();
		for (String name : configurationMap.keySet()) {
			result += (result.equals("") ? "" : System.getProperty("line.separator")) + name + ": "
					+ configurationMap.get(name);
		}
		return result;
	}

	protected String getLoggerPath() {
		return "";
	}

	private void addJimpleFileHandlerForMethod(SootMethod sootMethod) {
		if (EXPORT_JIMPLE) {
			try {
				this.jimpleFileHandler = SootLoggerConfiguration.getJimpleFileHandler(sootMethod,
						this);
				LOG.addHandler(this.jimpleFileHandler);
			} catch (SecurityException | NullPointerException | IOException e) {
				LOG.log(SootLoggerLevel.EXCEPTION,
						"Couldn't start to output the jimple source via file.", e);
			}
		}
	}

	private void removeJimpleFileHandler() {
		if (EXPORT_JIMPLE && this.jimpleFileHandler != null) {
			try {
				LOG.removeHandler(this.jimpleFileHandler);
				this.jimpleFileHandler.close();
				this.jimpleFileHandler = null;
			} catch (SecurityException e) {
				LOG.log(SootLoggerLevel.EXCEPTION,
						"Couldn't finish to output the jimple source via file.", e);
			}
		}
	}

	private void addStandardFileHandlerForMethod(SootMethod sootMethod) {
		if (EXPORT_FILE) {
			try {
				this.standardFileHandler = SootLoggerConfiguration.getStandardFileHandler(LEVELS,
						sootMethod, this);
				LOG.addHandler(this.standardFileHandler);
			} catch (SecurityException | NullPointerException | IOException e) {
				LOG.log(SootLoggerLevel.EXCEPTION, "Couldn't start the logging via file.", e);
			}
		}
	}

	private void removeStandardFileHandler() {
		if (EXPORT_FILE && this.standardFileHandler != null) {
			try {
				LOG.removeHandler(this.standardFileHandler);
				this.standardFileHandler.close();
				this.standardFileHandler = null;
			} catch (SecurityException e) {
				LOG.log(SootLoggerLevel.EXCEPTION, "Couldn't finish the logging via file.", e);
			}
		}
	}

	public void addAdditionalHandlerFor(SootMethod sootMethod) {
		addStandardFileHandlerForMethod(sootMethod);
		addJimpleFileHandlerForMethod(sootMethod);
	}

	public void removeAdditional() {
		removeJimpleFileHandler();
		removeStandardFileHandler();
	}
	
	public MessageStore getMessageStore() {
		return this.messageStore;
	}
}

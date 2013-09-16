package logging;

import java.util.logging.Level;

import model.ExtendedHeadingInformation;
import model.MinimalHeadingInformation;

/**
 * 
 * @author Thomas Vogel
 * @version 0.5
 */
public class SecurityLogger extends SootLogger {

	/**
	 * 
	 * @param exportFile
	 * @param levels
	 */
	public SecurityLogger(boolean exportFile, Level[] levels) {
		super(exportFile, levels);
	}
	
	/**
	 * 
	 * @param fileName
	 * @param sourceLine
	 * @param msg
	 */
	public void security(String fileName, long sourceLine, String msg) {
		this.messageStore.addMessage(msg, fileName, sourceLine, SootLoggerLevel.SECURITY);
		if (isLevelEnabled(SootLoggerLevel.SECURITY)) {
			ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, sourceLine, fileName);
			LOG.log(SootLoggerLevel.HEADING, "SECURITY", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.SECURITY, msg);
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void securitychecker(String msg) {
		this.messageStore.addMessage(msg, "SootSecurityLevel", 0, SootLoggerLevel.SECURITYCHECKER);
		if (isLevelEnabled(SootLoggerLevel.SECURITYCHECKER)) {
			MinimalHeadingInformation info = new MinimalHeadingInformation(1);
			LOG.log(SootLoggerLevel.HEADING, "SECURITYCHECKER", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.SECURITYCHECKER, msg);
	}
	
	/**
	 * 
	 * @param msg
	 * @param e
	 */
	public void securitychecker(String msg, Throwable e) {
		this.messageStore.addMessage(msg, "SootSecurityLevel", 0, SootLoggerLevel.SECURITYCHECKER);
		if (isLevelEnabled(SootLoggerLevel.SECURITYCHECKER)) {
			MinimalHeadingInformation info = new MinimalHeadingInformation(1);
			LOG.log(SootLoggerLevel.HEADING, "SECURITYCHECKER", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.SECURITYCHECKER, msg, e);
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getLoggerPath() {
		return "Security";
	}
}

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
	 * @param srcLn
	 * @param msg
	 */
	public void security(String fileName, long srcLn, String msg) {
		this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.SECURITY);
		if (isLevelEnabled(SootLoggerLevel.SECURITY)) {
			ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
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
			LOG.log(SootLoggerLevel.HEADING, "SECURITY-CHECKER", new Object[] { info });
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
			LOG.log(SootLoggerLevel.HEADING, "SECURITY-CHECKER", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.SECURITYCHECKER, msg, e);
	}
	
	/**
	 * 
	 * @param fileName
	 * @param srcLn
	 * @param msg
	 */
	public void effect(String fileName, long srcLn, String msg) {
		this.messageStore.addMessage(msg, fileName, srcLn, SootLoggerLevel.SIDEEFFECT);
		if (isLevelEnabled(SootLoggerLevel.SIDEEFFECT)) {
			ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, srcLn, fileName);
			LOG.log(SootLoggerLevel.HEADING, "SIDE-EFFECT", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.SIDEEFFECT, msg);
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getLoggerPath() {
		return "Security";
	}
}

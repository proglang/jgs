package logging;

import java.util.logging.Level;

import model.ExtendedHeadingInformation;
import model.MinimalHeadingInformation;

public class SecurityLogger extends SootLogger {

	
	public SecurityLogger(boolean exportFile, boolean exportJimple, Level[] levels) {
		super(exportFile, exportJimple, levels);
		
	}
	
	public void security(String fileName, long sourceLine, String msg) {
		ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, sourceLine, fileName);
		this.messageStore.addMessage(msg, fileName, sourceLine, SootLoggerLevel.SECURITY);
		LOG.log(SootLoggerLevel.HEADING, "SECURITY", new Object[] { info });
		LOG.log(SootLoggerLevel.SECURITY, msg);
	}
	
	public void securitychecker(String msg) {
		MinimalHeadingInformation info = new MinimalHeadingInformation(1);
		LOG.log(SootLoggerLevel.HEADING, "SECURITYCHECKER", new Object[] { info });
		LOG.log(SootLoggerLevel.SECURITYCHECKER, msg);
	}
	
	public void securitychecker(String msg, Throwable e) {
		MinimalHeadingInformation info = new MinimalHeadingInformation(1);
		LOG.log(SootLoggerLevel.HEADING, "SECURITYCHECKER", new Object[] { info });
		LOG.log(SootLoggerLevel.SECURITYCHECKER, msg, e);
	}
	
	protected String getLoggerPath() {
		return "Security";
	}
	
}

package logging;

import java.util.logging.Level;

import model.ExtendedHeadingInformation;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class SideEffectLogger extends SootLogger {
	
	/**
	 * 
	 * @param exportFile
	 * @param levels
	 */
	public SideEffectLogger(boolean exportFile, Level[] levels) {
		super(exportFile, levels);
	}
	
	/**
	 * 
	 * @param fileName
	 * @param sourceLine
	 * @param msg
	 */
	public void sideeffect(String fileName, long sourceLine, String msg) {
		this.messageStore.addMessage(msg, fileName, sourceLine, SootLoggerLevel.SIDEEFFECT);
		if (isLevelEnabled(SootLoggerLevel.SIDEEFFECT)) {
			ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, sourceLine, fileName);
			LOG.log(SootLoggerLevel.HEADING, "SIDEEFFECT", new Object[] { info });
		}
		LOG.log(SootLoggerLevel.SIDEEFFECT, msg);
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getLoggerPath() {
		return "SideEffect";
	}

}

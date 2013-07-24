package logging;

import java.util.logging.Level;

import model.ExtendedHeadingInformation;

public class SideEffectLogger extends SootLogger {
	
	public SideEffectLogger(boolean exportFile, boolean exportJimple, Level[] levels) {
		super(exportFile, exportJimple, levels);
	}
	
	public void sideeffect(String fileName, long sourceLine, String msg) {
		ExtendedHeadingInformation info = new ExtendedHeadingInformation(1, sourceLine, fileName);
		this.messageStore.addMessage(msg, fileName, sourceLine, SootLoggerLevel.SIDEEFFECT);
		LOG.log(SootLoggerLevel.HEADING, "SIDEEFFECT", new Object[] { info });
		LOG.log(SootLoggerLevel.SIDEEFFECT, msg);
	}
	
	protected String getLoggerPath() {
		return "SideEffect";
	}

}

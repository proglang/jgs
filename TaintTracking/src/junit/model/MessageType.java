package junit.model;

import java.util.logging.Level;

import logging.AnalysisLogLevel;

/**
 * TODO: documentation
 * 
 * @author Thomas Vogel
 * 
 */
public enum MessageType {
	EXCEPTION(AnalysisLogLevel.EXCEPTION), ERROR(AnalysisLogLevel.ERROR), SECURITY(AnalysisLogLevel.SECURITY), SIDEEFFECT(
			AnalysisLogLevel.SIDEEFFECT), SECURITYCHECKER(AnalysisLogLevel.SECURITYCHECKER), WARNING(AnalysisLogLevel.WARNING), INFORMATION(
			AnalysisLogLevel.INFORMATION);

	/**
	 * TODO: documentation
	 * 
	 */
	private Level level;

	/**
	 * TODO: documentation
	 * 
	 * @param level
	 */
	private MessageType(Level level) {
		this.level = level;
	}

	/**
	 * TODO: documentation
	 * 
	 * @return
	 */
	public Level getLevel() {
		return this.level;
	}
}
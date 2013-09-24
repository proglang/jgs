package pattern;

import security.SecurityAnnotation;
import soot.SootMethod;
import utils.*;
import logging.SecurityLogger;
import model.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 *
 */
public class AbstractTaintTrackingSwitch {
	
	/** */
	protected final LocalMap in;
	/** */
	protected final LocalMap out;
	/** */
	protected final AnalyzedMethodEnvironment analyzedMethodEnvironment;
	
	/**
	 * 
	 * @param analysisEnvironment
	 * @param in
	 * @param out
	 */
	public AbstractTaintTrackingSwitch(AnalyzedMethodEnvironment methodEnvironment, LocalMap in, LocalMap out) {
		this.analyzedMethodEnvironment = methodEnvironment;
		this.out = out;
		this.in = in;
	}
	
	/**
	 * 
	 * @return
	 */
	protected long getSrcLn() {
		return analyzedMethodEnvironment.getSrcLn();
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getMethodSignature() {
		return SootUtils.generateMethodSignature(analyzedMethodEnvironment.getSootMethod(), false, true, true);
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getFileName() {
		return SootUtils.generateFileName(analyzedMethodEnvironment.getSootMethod());
	}
	
	/**
	 * 
	 * @return
	 */
	protected SecurityLogger getLog() {
		return analyzedMethodEnvironment.getLog();
	}
	
	/**
	 * 
	 * @return
	 */
	protected SootMethod getSootMethod() {
		return analyzedMethodEnvironment.getSootMethod();
	}
	
	/**
	 * 
	 * @return
	 */
	protected SecurityAnnotation getSecurityAnnotation() {
		return analyzedMethodEnvironment.getSecurityAnnotation();
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getWeakestSecurityLevel() {
		return analyzedMethodEnvironment.getWeakestSecurityLevel();
	}
	
}
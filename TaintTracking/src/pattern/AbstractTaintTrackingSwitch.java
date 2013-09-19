package pattern;

import utils.*;
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
	protected final MethodEnvironment methodEnvironment;
	
	/**
	 * 
	 * @param analysisEnvironment
	 * @param in
	 * @param out
	 */
	public AbstractTaintTrackingSwitch(MethodEnvironment methodEnvironment, LocalMap in, LocalMap out) {
		this.methodEnvironment = methodEnvironment;
		this.out = out;
		this.in = in;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getSourceLine() {
		return methodEnvironment.getSourceLine();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMethodSignature() {
		return SootUtils.generateMethodSignature(methodEnvironment.getSootMethod());
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFileName() {
		return SootUtils.generateFileName(methodEnvironment.getSootMethod());
	}
}

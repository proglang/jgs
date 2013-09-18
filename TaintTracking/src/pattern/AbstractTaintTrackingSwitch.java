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
	protected final MethodAnalysisEnvironment methodAnalysisEnvironment;
	
	/**
	 * 
	 * @param analysisEnvironment
	 * @param in
	 * @param out
	 */
	public AbstractTaintTrackingSwitch(MethodAnalysisEnvironment methodAnalysisEnvironment, LocalMap in, LocalMap out) {
		this.methodAnalysisEnvironment = methodAnalysisEnvironment;
		this.out = out;
		this.in = in;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getSourceLine() {
		return methodAnalysisEnvironment.getSourceLine();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMethodSignature() {
		return SootUtils.generateMethodSignature(methodAnalysisEnvironment.getSootMethod());
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFileName() {
		return SootUtils.generateFileName(methodAnalysisEnvironment.getSootMethod());
	}
}

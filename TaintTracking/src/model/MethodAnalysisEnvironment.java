package model;

import logging.SecurityLogger;
import security.SecurityAnnotation;
import soot.SootMethod;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class MethodAnalysisEnvironment extends AnalysisEnvironment {
	
	/** */
	private final SecurityMethod securityMethod;
	
	/**
	 * 
	 * @param log
	 * @param securityMethod
	 * @param securityAnnotations
	 */
	public MethodAnalysisEnvironment(SecurityLogger log, SecurityMethod securityMethod,	SecurityAnnotation securityAnnotations) {
		super(log, securityAnnotations);
		this.securityMethod = securityMethod;
	}

	/**
	 * 
	 * @return
	 */
	public SecurityMethod getSecurityMethod() {
		return securityMethod;
	}
	
	/**
	 * 
	 * @return
	 */
	public SootMethod getSootMethod() {
		return securityMethod.getSootMethod();
	}

}

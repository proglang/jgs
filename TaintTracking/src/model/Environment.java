package model;

import security.*;
import logging.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class Environment {
	
	/** */
	private final SecurityLogger log;
	/** */
	private final SecurityAnnotation securityAnnotation;
	

	/**
	 * 
	 * @param log
	 * @param securityAnnotations
	 */
	public Environment(SecurityLogger log, SecurityAnnotation securityAnnotations) {
		super();
		this.securityAnnotation = securityAnnotations;
		this.log = log;
	}

	/**
	 * 
	 * @return
	 */
	public SecurityLogger getLog() {
		return log;
	}

	/**
	 * 
	 * @return
	 */
	public SecurityAnnotation getSecurityAnnotation() {
		return securityAnnotation;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getWeakestSecurityLevel() {
		return securityAnnotation.getWeakestSecurityLevel();
	}
}

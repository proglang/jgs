package model;

import security.SecurityAnnotation;
import soot.jimple.Stmt;
import utils.SootUtils;
import logging.SecurityLogger;

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
	/** */
	private Stmt stmt = null;
	/** */
	private long sourceLine = 0;

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
	public Stmt getStmt() {
		return stmt;
	}

	/**
	 * 
	 * @param stmt
	 */
	public void setStmt(Stmt stmt) {
		this.stmt = stmt;
		this.sourceLine = SootUtils.extractLineNumberFrom(stmt);
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
	public long getSourceLine() {
		return sourceLine;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getWeakestSecurityLevel() {
		return securityAnnotation.getWeakestSecurityLevel();
	}
}

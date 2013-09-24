package model;

import logging.SecurityLogger;
import security.SecurityAnnotation;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.Stmt;
import utils.SecurityMessages;
import utils.SootUtils;
import model.Cause.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class AnalyzedMethodEnvironment extends MethodEnvironment {
	
	/** */
	private EffectsStore effectsStore = new EffectsStore();
	/** */
	private Stmt stmt = null;
	/** */
	private long srcLn = 0;

	/**
	 * @param sootMethod
	 * @param log
	 * @param securityAnnotation
	 */
	public AnalyzedMethodEnvironment(SootMethod sootMethod, SecurityLogger log, SecurityAnnotation securityAnnotation) {
		super(sootMethod, log, securityAnnotation);
	}

	/**
	 * 
	 */
	public void checkEffectAnnotations() {
		String fileName = SootUtils.generateFileName(getSootMethod());
		for (String effected : effectsStore.getWriteEffectSet()) {
			if (SootUtils.isClinitMethod(getSootMethod())) {
				if (! getExpectedClassWriteEffects().contains(effected)) {
					for (Effect effect : effectsStore.getWriteEffects(effected)){
						long srcLn = effect.getSrcLn();
						String cause = effect.getCause().getCauseString();
						getLog().effect(fileName, srcLn, SecurityMessages.missingWriteEffect(getSootMethod(), srcLn, effected, cause));
					}
				}
			} else {
				if (! getExpectedWriteEffects().contains(effected)) {
					for (Effect effect : effectsStore.getWriteEffects(effected)){
						long srcLn = effect.getSrcLn();
						String cause = effect.getCause().getCauseString();
						getLog().effect(fileName, srcLn, SecurityMessages.missingWriteEffect(getSootMethod(), srcLn, effected, cause));
					}
				}
			}			
		}
		for (String effected : SootUtils.isClinitMethod(getSootMethod()) ? getExpectedClassWriteEffects() : getExpectedWriteEffects()) {
			getLog().warning(fileName, 0, SecurityMessages.superfluousWriteEffect(getSootMethod(), effected));
		}
	}
	
	/**
	 * 
	 * @param effected
	 * @param srcLn
	 * @param sootField
	 */
	public void addWriteEffectCausedByAssign(String effected, long srcLn, SootField sootField) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn, new AssignCause(sootField)));
	}
	
	/**
	 * 
	 * @param effected
	 * @param srcLn
	 * @param sootMethod
	 */
	public void addWriteEffectCausedByMethodInvocation(String effected, long srcLn, SootMethod sootMethod) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn, new MethodCause(sootMethod)));
	}
	
	/**
	 * 
	 * @param effected
	 * @param srcLn
	 * @param sootClass
	 */
	public void addWriteEffectCausedByClass(String effected, long srcLn, SootClass sootClass) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn, new ClassCause(sootClass)));
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
		this.srcLn = SootUtils.extractLn(stmt);
	}

	/**
	 * 
	 * @return
	 */
	public long getSrcLn() {
		return srcLn;
	}
	
}

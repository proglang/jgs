package model;

import java.util.ArrayList;
import java.util.List;

import logging.AnalysisLog;
import resource.Configuration;
import static resource.Messages.getMsg;
import security.ILevel;
import security.ILevelMediator;
import soot.SootClass;
import soot.SootField;
import utils.AnalysisUtils;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class ClassEnvironment extends Environment {

	/**
	 * DOC
	 */
	private SootClass sootClass;
	/**
	 * The <em>write effects</em> of the class.
	 */
	private List<ILevel> writeEffects = new ArrayList<ILevel>();

	/**
	 * DOC
	 * 
	 * @param sootClass
	 * @param classWriteEffects
	 * @param log
	 * @param mediator
	 */
	public ClassEnvironment(SootClass sootClass, List<ILevel> classWriteEffects, AnalysisLog log, ILevelMediator mediator) {
		super(log, mediator);
		this.sootClass = sootClass;
		this.writeEffects.addAll(classWriteEffects);
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public List<SootField> getFields() {
		List<SootField> fields = new ArrayList<SootField>();
		fields.addAll(sootClass.getFields());
		return fields;
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public SootClass getSootClass() {
		return sootClass;
	}

	/**
	 * Returns the <em>write effects</em> of the class which declares the analyzed class.
	 * 
	 * @return The class <em>write effects</em>.
	 */
	public List<ILevel> getWriteEffects() {
		return writeEffects;
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public boolean isLibrary() {
		return sootClass.isJavaLibraryClass();
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public boolean isReasonable() {
		String classSignature = AnalysisUtils.generateClassSignature(sootClass, Configuration.CLASS_SIGNATURE_PRINT_PACKAGE);
		if (!getLevelMediator().checkLevelsValidity(writeEffects)) {
			// one of the write effects isn't a valid security level
			for (ILevel invalidEffect : getLevelMediator().getInvalidLevels(writeEffects)) {
				getLog().error(AnalysisUtils.generateFileName(sootClass), 0,
						getMsg("effects.class.invalid", invalidEffect.getName(), classSignature));
			}
			return false;
		}
		return true;
	}

}

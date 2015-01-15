package model;

import static constraints.ConstraintsUtils.containsSetParameterReference;
import static constraints.ConstraintsUtils.containsSetProgramCounterReference;
import static constraints.ConstraintsUtils.containsSetReturnReference;
import static constraints.ConstraintsUtils.getContainedLevelsOfSet;
import static main.AnalysisType.CONSTRAINTS;
import static main.AnalysisType.LEVELS;
import static resource.Messages.getMsg;
import static utils.AnalysisUtils.getSignatureOfClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logging.AnalysisLog;
import main.AnalysisType;
import security.ILevel;
import security.ILevelMediator;
import soot.SootClass;
import soot.SootField;
import constraints.LEQConstraint;
import exception.AnalysisTypeException;
import exception.AnnotationInvalidException;
import exception.LevelInvalidException;

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
	 */
	private final Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();

	/**
	 * DOC
	 * 
	 * @param sootClass
	 * @param classWriteEffects
	 * @param log
	 * @param mediator
	 */
	public ClassEnvironment(SootClass sootClass, List<ILevel> classWriteEffects, Set<LEQConstraint> constraints, AnalysisLog log,
			ILevelMediator mediator) {
		super(log, mediator);
		this.sootClass = sootClass;
		this.writeEffects.addAll(classWriteEffects);
		this.constraints.addAll(constraints);
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
	 * @param type
	 * 
	 * @return
	 */
	public void isReasonable(AnalysisType type) {
		String classSignature = getSignatureOfClass(sootClass);
		if (type.equals(LEVELS)) {
			if (!getLevelMediator().checkLevelsValidity(writeEffects)) {
				// one of the write effects isn't a valid security level
				for (ILevel invalidEffect : getLevelMediator().getInvalidLevels(writeEffects)) {
					throw new AnnotationInvalidException(getMsg("exception.effects.class_invalid", invalidEffect.getName(), classSignature));
				}
			}
		} else if (type.equals(CONSTRAINTS)) {
			if (!containsSetProgramCounterReference(constraints)) {
				throw new AnnotationInvalidException(getMsg("exception.constraints.class_no_pc_ref", classSignature));
			}
			if (containsSetParameterReference(constraints)) {
				throw new AnnotationInvalidException(getMsg("exception.constraints.param_ref", classSignature));
			}
			if (containsSetReturnReference(constraints)) {
				throw new AnnotationInvalidException(getMsg("exception.constraints.return_ref", classSignature));
			}
			List<ILevel> containedLevels = new ArrayList<ILevel>(getContainedLevelsOfSet(constraints));
			if (!getLevelMediator().checkLevelsValidity(containedLevels)) {
				for (ILevel invalidEffect : getLevelMediator().getInvalidLevels(containedLevels)) {
					throw new LevelInvalidException(getMsg("exception.constraints.class_invalid_level", invalidEffect.getName(), classSignature));
				}
			}
		} else {
			throw new AnalysisTypeException(getMsg("exception.analysis_type.unknown", type.toString()));
		}
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public Set<LEQConstraint> getSignatureContraints() {
		return new HashSet<LEQConstraint>(constraints);
	}

}

package model;

import static resource.Messages.getMsg;
import static utils.AnalysisUtils.generateFieldSignature;

import java.util.ArrayList;
import java.util.List;

import logging.AnalysisLog;
import security.ILevel;
import security.ILevelMediator;
import soot.SootClass;
import soot.SootField;
import exception.AnnotationInvalidException;
import exception.LevelInvalidException;

/**
 * <h1>Analysis environment for fields</h1>
 * 
 * The {@link FieldEnvironment} provides a environment for analyzing a {@link SootField}. Therefore it extends the base analysis environment
 * {@link Environment} in order to access a logger and the security annotation. The environment provides methods for getting the required
 * annotations at the field as well as at the class which declares the field, and also methods which checks the validity of the levels and
 * effects that are given by those annotations. In addition the environment gives direct access to some methods of the analyzed
 * {@link SootField}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * @see Environment
 */
public class FieldEnvironment extends Environment {

	/**
	 * The <em>write effects</em> of the class which declares the {@link SootField}.
	 */
	private List<ILevel> classWriteEffects = new ArrayList<ILevel>();
	/** The <em>security level</em> of the {@link SootField}. */
	private ILevel level = null;

	/** The {@link SootField} for which this is the environment. */
	private SootField sootField;

	public FieldEnvironment(SootField sootField, ILevel level, List<ILevel> classWriteEffect, AnalysisLog log, ILevelMediator mediator) {
		super(log, mediator);
		this.sootField = sootField;
		this.level = level;
		this.classWriteEffects.addAll(classWriteEffect);
	}

	/**
	 * Returns the <em>write effects</em> of the class which declares the analyzed field.
	 * 
	 * @return The class <em>write effects</em>.
	 */
	public List<ILevel> getClassWriteEffects() {
		return classWriteEffects;
	}

	/**
	 * Method that returns the {@link SootClass} that declares the field for which this is the environment.
	 * 
	 * @return The class which declares the analyzed field.
	 */
	public SootClass getDeclaringSootClass() {
		return sootField.getDeclaringClass();
	}

	/**
	 * Returns the <em>security level</em> of the field {@link FieldEnvironment#sootField}.
	 * 
	 * @return The <em>security level</em> of the field.
	 */
	public ILevel getLevel() {
		return level;
	}

	/**
	 * Method that returns the {@link SootField} for which this is the environment (see {@link FieldEnvironment#sootField}).
	 * 
	 * @return The analyzed field.
	 */
	public SootField getSootField() {
		return sootField;
	}

	/**
	 * Indicates whether the {@link SootClass} which declares the analyzed field is a library class.
	 * 
	 * @return {@code true} if the class is a library class, otherwise {@code false}.
	 */
	public boolean isLibraryClass() {
		return sootField.getDeclaringClass().isJavaLibraryClass();
	}

	/**
	 * TODO: documentation
	 * 
	 * @return
	 */
	public void isReasonable() {
		if (level != null) { // some level given
			if (!getLevelMediator().checkLevelValidity(level)) {
				// level isn't a valid security level
				throw new LevelInvalidException(getMsg("exception.level.field.invalid", level.getName(), generateFieldSignature(sootField)));
			}
		} else { // no level given
			throw new AnnotationInvalidException(getMsg("exception.level.field.no_level", generateFieldSignature(sootField)));
		}
	}

	/**
	 * Indicates whether the analyzed {@link SootField} is a static field (see {@link FieldEnvironment#sootField}).
	 * 
	 * @return {@code true} if the field is a static field, otherwise {@code false}.
	 */
	public boolean isStatic() {
		return sootField.isStatic();
	}

	public void updateSootField(SootField sootField) {
		this.sootField = sootField;
	}

}

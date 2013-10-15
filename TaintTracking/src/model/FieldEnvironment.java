package model;

import java.util.ArrayList;
import java.util.List;

import logging.SecurityLogger;
import security.Annotations;
import security.SecurityAnnotation;
import soot.SootClass;
import soot.SootField;
import utils.SecurityMessages;
import utils.SootUtils;

/**
 * <h1>Analysis environment for fields</h1>
 * 
 * The {@link FieldEnvironment} provides a environment for analyzing a {@link SootField}. Therefore
 * it extends the base analysis environment {@link Environment} in order to access a logger and the
 * security annotation. The environment provides methods for getting the required annotations at the
 * field as well as at the class which declares the field, and also methods which checks the
 * validity of the levels and effects that are given by those annotations. In addition the
 * environment gives direct access to some methods of the analyzed {@link SootField}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * @see Environment
 * @see Annotations.WriteEffect
 * @see Annotations.FieldSecurity
 */
public class FieldEnvironment extends Environment {

	/**
	 * Value which indicates whether the <em>security annotation</em> at the analyzed field is
	 * available.
	 */
	private boolean annotationAvailable = false;
	/**
	 * Value which indicates whether the <em>effect annotation</em> at the analyzed class is
	 * available.
	 */
	private boolean classWriteEffectAnnotationAvailable = false;
	/** The <em>write effects</em> of the class which declares the {@link SootField}. */
	private List<String> expectedClassWriteEffects = new ArrayList<String>();
	/** The <em>security level</em> of the {@link SootField}. */
	private String level = null;
	/** The {@link SootClass} that declares the field for which this is the environment. */
	private SootClass sootClass;
	/** The {@link SootField} for which this is the environment. */
	private SootField sootField;

	/**
	 * Constructor of a {@link FieldEnvironment} that requires a {@link SootField} which should be
	 * analyzed, a logger in order to allow logging for this object as well as a security annotation
	 * object in order to provide the handling of <em>security levels</em>. By calling the
	 * constructor all required extractions of the annotations will be done automatically.
	 * 
	 * @param sootField
	 *            The {@link SootField} that should be analyzed.
	 * @param log
	 *            A {@link SecurityLogger} in order to allow logging for this object.
	 * @param securityAnnotation
	 *            A {@link SecurityAnnotation} in order to provide the handling of
	 *            <em>security levels</em>.
	 */
	public FieldEnvironment(SootField sootField, SecurityLogger log,
			SecurityAnnotation securityAnnotation) {
		super(log, securityAnnotation);
		this.sootField = sootField;
		this.sootClass = sootField.getDeclaringClass();
		extractFieldSecurityLevel();
		extractClassWriteEffects();
	}

	/**
	 * Checks whether the class <em>write effects</em> are valid. I.e. the corresponding annotation
	 * has to be available and all by the annotation provided <em>write effects</em> have to be a
	 * valid <em>security level</em>.
	 * 
	 * @return {@code true} if the annotation is available and if all provided
	 *         <em>write effects</em> are valid, otherwise {@code false}.
	 */
	public boolean areClassWriteEffectsValid() {
		String classSignature = SootUtils.generateClassSignature(sootClass, false);
		if (!this.classWriteEffectAnnotationAvailable) {
			logError(SecurityMessages.noClassWriteEffectAnnotation(classSignature));
			return false;
		}
		if (!getSecurityAnnotation().checkValidityOfLevels(expectedClassWriteEffects)) {
			for (String invalidEffect : getSecurityAnnotation().getInvalidLevels(
					expectedClassWriteEffects)) {
				logError(SecurityMessages.invalidClassWriteEffect(classSignature, invalidEffect));
			}
			return false;
		}
		return true;
	}

	/**
	 * Returns the <em>write effects</em> of the class which declares the analyzed field.
	 * 
	 * @return The class <em>write effects</em>.
	 */
	public List<String> getExpectedClassWriteEffects() {
		return expectedClassWriteEffects;
	}

	/**
	 * Returns the <em>security level</em> of the field {@link FieldEnvironment#sootField}.
	 * 
	 * @return The <em>security level</em> of the field.
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Method that returns the {@link SootClass} that declares the field for which this is the
	 * environment (see {@link FieldEnvironment#sootClass}).
	 * 
	 * @return The class which declares the analyzed field.
	 */
	public SootClass getSootClass() {
		return sootClass;
	}

	/**
	 * Method that returns the {@link SootField} for which this is the environment (see
	 * {@link FieldEnvironment#sootField}).
	 * 
	 * @return The analyzed field.
	 */
	public SootField getSootField() {
		return sootField;
	}

	/**
	 * Indicates whether the <em>security annotation</em> at the analyzed field is available.
	 * 
	 * @return {@code true} if the annotation is available, otherwise {@code false}.
	 */
	public boolean isAnnotationAvailable() {
		return annotationAvailable;
	}

	/**
	 * Indicates whether the <em>effect annotation</em> at the analyzed class is available.
	 * 
	 * @return {@code true} if the annotation is available, otherwise {@code false}.
	 */
	public boolean isClassWriteEffectAnnotationAvailable() {
		return classWriteEffectAnnotationAvailable;
	}

	/**
	 * Checks whether the field <em>security level</em> is valid. I.e. the corresponding annotation
	 * has to be available and the <em>security effects</em> by the annotation provided has to be a
	 * valid <em>security level</em>.
	 * 
	 * @return {@code true} if the annotation is available and if the provided
	 *         <em>security level</em> is valid, otherwise {@code false}.
	 * @return
	 */
	public boolean isFieldSecurityLevelValid() {
		if (!annotationAvailable) {
			logError(SecurityMessages.noFieldAnnotation(SootUtils.generateFieldSignature(sootField,
					false, true, true)));
			return false;
		}
		if (level == null) {
			logError(SecurityMessages.noFieldLevel(SootUtils.generateFieldSignature(sootField,
					false, true, true)));
			return false;
		}
		if (!getSecurityAnnotation().checkValidityOfLevel(level)) {
			logError(SecurityMessages.invalidFieldLevel(
					SootUtils.generateFieldSignature(sootField, false, true, true), level));
			return false;
		}
		return true;
	}

	/**
	 * Indicates whether the {@link SootClass} which declares the analyzed field is a library class
	 * (see {@link FieldEnvironment#sootClass}).
	 * 
	 * @return {@code true} if the class is a library class, otherwise {@code false}.
	 */
	public boolean isLibraryClass() {
		return this.sootClass.isLibraryClass();
	}

	/**
	 * Indicates whether the analyzed {@link SootField} is a static field (see
	 * {@link FieldEnvironment#sootField}).
	 * 
	 * @return {@code true} if the field is a static field, otherwise {@code false}.
	 */
	public boolean isStatic() {
		return sootField.isStatic();
	}

	/**
	 * Extracts the annotation of the type {@link Annotations.WriteEffect} which should exist at the
	 * class that declares the analyzed field. The extracted <em>write effects</em> will be stored
	 * in {@link FieldEnvironment#expectedClassWriteEffects} and based on whether the annotation at
	 * the class exists {@link FieldEnvironment#classWriteEffectAnnotationAvailable} will be set.
	 * 
	 * @see SootUtils#extractAnnotationStringArray(String, List)
	 */
	private void extractClassWriteEffects() {
		List<String> annotations = SootUtils.extractAnnotationStringArray(
				SecurityAnnotation.getSootAnnotationTag(Annotations.WriteEffect.class),
				sootClass.getTags());
		this.classWriteEffectAnnotationAvailable = annotations != null;
		if (classWriteEffectAnnotationAvailable) {
			this.expectedClassWriteEffects = annotations;
		}
	}

	/**
	 * Extracts the annotation of the type {@link Annotations.FieldSecurity} which should exist at
	 * the analyzed field. The extracted <em>security level</em> will be stored in
	 * {@link FieldEnvironment#level} and based on whether the annotation at the field exists
	 * {@link FieldEnvironment#annotationAvailable} will be set.
	 * 
	 * @see SootUtils#extractAnnotationString(String, List)
	 */
	private void extractFieldSecurityLevel() {
		String annotation = SootUtils.extractAnnotationString(
				SecurityAnnotation.getSootAnnotationTag(Annotations.FieldSecurity.class),
				sootField.getTags());
		this.annotationAvailable = annotation != null;
		if (annotationAvailable) {
			this.level = annotation;
		}
	}

	/**
	 * Logs the given message as an error. The file name is created by the class, the source line
	 * number is specified as 0.
	 * 
	 * @param msg
	 *            Message that should be printed as an error.
	 * @see SecurityLogger#error(String, long, String)
	 */
	private void logError(String msg) {
		getLog().error(SootUtils.generateFileName(sootClass), 0, msg);
	}

}

package model;

import java.util.ArrayList;
import java.util.List;


import logging.SecurityLogger;
import main.Configuration;
import security.Annotations;
import security.SecurityAnnotation;
import soot.SootClass;
import soot.SootField;
import utils.SecurityMessages;
import utils.SootUtils;

/**
 * <h1>Analysis environment for fields</h1>
 * 
 * The {@link FieldEnvironment} provides a environment for analyzing a
 * {@link SootField}. Therefore it extends the base analysis environment
 * {@link Environment} in order to access a logger and the security annotation.
 * The environment provides methods for getting the required annotations at the
 * field as well as at the class which declares the field, and also methods
 * which checks the validity of the levels and effects that are given by those
 * annotations. In addition the environment gives direct access to some methods
 * of the analyzed {@link SootField}.
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
	 * The <em>write effects</em> of the class which declares the
	 * {@link SootField}.
	 */
	private List<String> classWriteEffects = new ArrayList<String>();
	/** The <em>security level</em> of the {@link SootField}. */
	private String level = null;

	/** The {@link SootField} for which this is the environment. */
	private SootField sootField;


	public FieldEnvironment(SootField sootField, String level,
			List<String> classWriteEffect, SecurityLogger log,
			SecurityAnnotation securityAnnotation) {
		super(log, securityAnnotation);
		this.sootField = sootField;
		this.level = level;
		this.classWriteEffects.addAll(classWriteEffect);
	}

	/**
	 * Returns the <em>write effects</em> of the class which declares the
	 * analyzed field.
	 * 
	 * @return The class <em>write effects</em>.
	 */
	public List<String> getClassWriteEffects() {
		return classWriteEffects;
	}

	/**
	 * Returns the <em>security level</em> of the field
	 * {@link FieldEnvironment#sootField}.
	 * 
	 * @return The <em>security level</em> of the field.
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Method that returns the {@link SootClass} that declares the field for
	 * which this is the environment.
	 * 
	 * @return The class which declares the analyzed field.
	 */
	public SootClass getDeclaringSootClass() {
		return sootField.getDeclaringClass();
	}

	/**
	 * Method that returns the {@link SootField} for which this is the
	 * environment (see {@link FieldEnvironment#sootField}).
	 * 
	 * @return The analyzed field.
	 */
	public SootField getSootField() {
		return sootField;
	}

	/**
	 * Indicates whether the {@link SootClass} which declares the analyzed field
	 * is a library class.
	 * 
	 * @return {@code true} if the class is a library class, otherwise
	 *         {@code false}.
	 */
	public boolean isLibraryClass() {
		return sootField.getDeclaringClass().isJavaLibraryClass();
	}

	/**
	 * Indicates whether the analyzed {@link SootField} is a static field (see
	 * {@link FieldEnvironment#sootField}).
	 * 
	 * @return {@code true} if the field is a static field, otherwise
	 *         {@code false}.
	 */
	public boolean isStatic() {
		return sootField.isStatic();
	}

	/**
	 * Logs the given message as an error. The file name is created by the
	 * class, the source line number is specified as 0.
	 * 
	 * @param msg
	 *            Message that should be printed as an error.
	 * @see SecurityLogger#error(String, long, String)
	 */
	private void logError(String msg) {
		getLog().error(SootUtils.generateFileName(getDeclaringSootClass()), 0,
				msg);
	}

	/**
	 * TODO: documentation
	 * 
	 * @return
	 */
	public boolean isReasonable() {
		if (level != null) { // some level given
			if (!getSecurityAnnotation().checkValidityOfLevel(level)) {
				// level isn't a valid security level
				logError(SecurityMessages
						.invalidFieldLevel(
								SootUtils
										.generateFieldSignature(
												sootField,
												Configuration.FIELD_SIGNATURE_PRINT_PACKAGE,
												Configuration.FIELD_SIGNATURE_PRINT_TYPE,
												Configuration.FIELD_SIGNATURE_PRINT_VISIBILITY),
								level));
				return false;
			}
			return true;
		} else { // no level given
			logError(SecurityMessages.noFieldLevel(SootUtils
					.generateFieldSignature(sootField,
							Configuration.FIELD_SIGNATURE_PRINT_PACKAGE,
							Configuration.FIELD_SIGNATURE_PRINT_TYPE,
							Configuration.FIELD_SIGNATURE_PRINT_VISIBILITY)));
			return false;
		}
	}
	
	public void updateSootField(SootField sootField) {
		this.sootField = sootField;
	}
}

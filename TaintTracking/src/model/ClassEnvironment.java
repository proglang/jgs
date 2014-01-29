package model;

import java.util.ArrayList;
import java.util.List;


import logging.SecurityLogger;
import main.Configuration;
import security.SecurityAnnotation;
import soot.SootClass;
import soot.SootField;
import utils.SecurityMessages;
import utils.SootUtils;

public class ClassEnvironment extends Environment {

	private SootClass sootClass;
	/**
	 * The <em>write effects</em> of the class.
	 */
	private List<String> writeEffects = new ArrayList<String>();

	public ClassEnvironment(
			SootClass sootClass,
			List<String> classWriteEffects, 
			SecurityLogger log,
			SecurityAnnotation securityAnnotations
			) {
		super(log, securityAnnotations);
		this.sootClass = sootClass;
		this.writeEffects.addAll(classWriteEffects);
	}

	public SootClass getSootClass() {
		return sootClass;
	}

	public List<SootField> getFields() {
		List<SootField> fields = new ArrayList<SootField>();
		fields.addAll(sootClass.getFields());
		return fields;
	}

	public boolean isLibrary() {
		return sootClass.isJavaLibraryClass();
	}
	
	public void updateSootClass(SootClass sootClass) {
		this.sootClass = sootClass;
	}

	/**
	 * Returns the <em>write effects</em> of the class which declares the
	 * analyzed class.
	 * 
	 * @return The class <em>write effects</em>.
	 */
	public List<String> getWriteEffects() {
		return writeEffects;
	}
	
	/**
	 * TODO: documentation
	 * 
	 * @return
	 */
	public boolean isReasonable() {
		String classSignature = SootUtils.generateClassSignature(
				sootClass, Configuration.CLASS_SIGNATURE_PRINT_PACKAGE);
		if (!getSecurityAnnotation().checkValidityOfLevels(writeEffects)) {
			// one of the write effects isn't a valid security level
			for (String invalidEffect : getSecurityAnnotation()
					.getInvalidLevels(writeEffects)) {
				getLog().error(SootUtils.generateFileName(sootClass), 0, SecurityMessages.invalidClassWriteEffect(
						classSignature, invalidEffect));
			}
			return false;
		}
		return true;
	}

}

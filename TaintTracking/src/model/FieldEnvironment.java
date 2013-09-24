package model;

import logging.SecurityLogger;
import security.Annotations;
import security.SecurityAnnotation;
import soot.SootClass;
import soot.SootField;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import utils.SecurityMessages;
import utils.SootUtils;

/**
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class FieldEnvironment extends Environment {

	/** */
	private String level = null;
	/** */
	private boolean annotationAvailable = false;
	/** */
	private SootField sootField;
	/** */
	private SootClass sootClass;
	
	/**
	 * 
	 * @param sootField
	 * @param log
	 * @param securityAnnotation
	 */
	public FieldEnvironment(SootField sootField, SecurityLogger log, SecurityAnnotation securityAnnotation){
		super(log, securityAnnotation);
		this.sootField = sootField;
		this.sootClass = sootField.getDeclaringClass();
		extractFieldSecurityLevel();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isLibraryClass() {
		return this.sootClass.isJavaLibraryClass();
	}
	
	/**
	 * 
	 * @return
	 */
	public SootClass getSootClass() {
		return sootClass;
	}

	/**
	 * 
	 * @return
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAnnotationAvailable() {
		return annotationAvailable;
	}

	/**
	 * 
	 * @return
	 */
	public SootField getSootField() {
		return sootField;
	}
	
	/**
	 * 
	 */
	public void extractFieldSecurityLevel() {
		for (Tag tag : this.sootField.getTags()){
			if (tag instanceof VisibilityAnnotationTag) {
				VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
				for (AnnotationTag  annotationTag : visibilityAnnotationTag.getAnnotations()) {
					if (annotationTag.getType().equals(SecurityAnnotation.getSootAnnotationTag(Annotations.FieldSecurity.class))) {
						this.annotationAvailable = true;
						for (int i = 0; i < annotationTag.getNumElems(); i++) {
							AnnotationElem annotationElem =  annotationTag.getElemAt(i);
							if (annotationElem.getKind() == "s".charAt(0)) {
								AnnotationStringElem annotationStringElem = (AnnotationStringElem) annotationElem;
								this.level = annotationStringElem.getValue();
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFieldSecurityLevelValid() {
		if (! annotationAvailable) {
			getLog().error(SootUtils.generateFileName(sootClass), 0, SecurityMessages.noFieldAnnotation(SootUtils.generateFieldSignature(sootField, false, true, true)));
			return false;
		}
		if (level == null) {
			getLog().error(SootUtils.generateFileName(sootClass), 0, SecurityMessages.noFieldLevel(SootUtils.generateFieldSignature(sootField, false, true, true)));
			return false;
		}
		if (! getSecurityAnnotation().checkValidityOfLevel(level)) {
			getLog().error(SootUtils.generateFileName(sootClass), 0, SecurityMessages.invalidFieldLevel(SootUtils.generateFieldSignature(sootField, false, true, true), level));
			return false;
		}
		return true;
	}

}

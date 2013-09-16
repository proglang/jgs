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
 * @version 0.1
 */
public class SecurityField {

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
	 * @param shouldExtract
	 */
	public SecurityField(SootField sootField, boolean shouldExtract){
		super();
		this.sootField = sootField;
		this.sootClass = sootField.getDeclaringClass();
		if (shouldExtract) {
			extractFieldSecurityLevel();
		}
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
	 * @param securityAnnotation
	 * @param log
	 * @return
	 */
	public boolean isFieldSecurityLevelValid(SecurityAnnotation securityAnnotation, SecurityLogger log) {
		if (! annotationAvailable) {
			log.error(SootUtils.generateFileName(sootClass), 0, SecurityMessages.noFieldAnnotation(SootUtils.generateFieldSignature(sootField)));
			return false;
		}
		if (level == null) {
			log.error(SootUtils.generateFileName(sootClass), 0, SecurityMessages.noFieldLevel(SootUtils.generateFieldSignature(sootField)));
			return false;
		}
		if (! securityAnnotation.checkValidityOfFieldLevel(level)) {
			log.error(SootUtils.generateFileName(sootClass), 0, SecurityMessages.invalidFieldLevel(SootUtils.generateFieldSignature(sootField), level));
			return false;
		}
		return true;
	}

}

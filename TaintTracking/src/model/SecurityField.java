package model;

import java.util.List;

import logging.SecurityLogger;
import security.SecurityAnnotation;
import security.SecurityAnnotation.FieldSecurity;
import soot.SootClass;
import soot.SootField;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import utils.SootUtils;

public class SecurityField {

	private String level = null;
	private boolean annotationAvailable = false;
	private SootField sootField;
	private SootClass sootClass;
	
	public SecurityField(SootField sootField, boolean shouldExtract){
		super();
		this.sootField = sootField;
		this.sootClass = sootField.getDeclaringClass();
		if (shouldExtract) {
			extractFieldSecurityLevel();
		}
	}
	
	public boolean isLibraryClass() {
		return this.sootClass.isJavaLibraryClass();
	}
	
	public SootClass getSootClass() {
		return sootClass;
	}

	public String getLevel() {
		return level;
	}

	public boolean isAnnotationAvailable() {
		return annotationAvailable;
	}

	public SootField getSootField() {
		return sootField;
	}
	
	public void extractFieldSecurityLevel() {
		for (Tag tag : this.sootField.getTags()){
			if (tag instanceof VisibilityAnnotationTag) {
				VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
				for (AnnotationTag  annotationTag : visibilityAnnotationTag.getAnnotations()) {
					if (annotationTag.getType().equals(SecurityAnnotation.getSootAnnotationTag(FieldSecurity.class))) {
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
	
	public boolean isFieldSecurityLevelValid(SecurityAnnotation securityAnnotation, SecurityLogger log) {
		
		if (! annotationAvailable) {
			log.error(SootUtils.generateFileName(sootClass), 0, "Field " + sootField.getSignature() + " has no security level annotation.");
			return false;
		}
		if (level == null) {
			log.error(SootUtils.generateFileName(sootClass), 0, "Field " + sootField.getSignature() + " has no security level.");
			return false;
		}
		if (! securityAnnotation.checkValidityOfFieldLevel(level)) {
			log.error(SootUtils.generateFileName(sootClass), 0, "Field " + sootField.getSignature() + " has an invalid security level named '" + level + "'.");
			return false;
		}
		return true;
	}

}

package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.SootException.InvalidEquationException;
import exception.SootException.InvalidLevelException;

import logging.SecurityLogger;

import security.LevelEquation;
import security.LevelEquationVisitor.LevelEquationCalculateVoidVisitor;
import security.LevelEquationVisitor.LevelEquationValidityVistitor;
import security.SecurityAnnotation;
import security.SecurityAnnotation.ParameterSecurity;
import security.SecurityAnnotation.ReturnSecurity;
import soot.SootMethod;
import soot.Type;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import utils.SootUtils;

public class SecurityMethod {
	
	private Map<Integer, MethodParameter> methodParameters = null;
	private boolean parameterAnnotationAvailable = false;
	private SootMethod sootMethod;
	private String returnLevel = null;
	private LevelEquation returnLevelEquation = null;
	private boolean returnAnnotationAvailable = false;
	
	

	public class MethodParameter {
		
		private int position;
		private Type type;
		private String name;
		private String level;
		
		public MethodParameter(int position, String name, Type type, String level) {
			super();
			this.position = position;
			this.name = name;
			this.type = type;
			this.level = level;
		}
		
		public int getPosition() {
			return this.position;
		}
		
		public Type getType() {
			return this.type;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getLevel() {
			return this.level;
		}	
	}
	
	public SecurityMethod(SootMethod sootMethod, boolean shouldExtract) {
		super();
		this.sootMethod = sootMethod;
		if (shouldExtract) {
			extractReturnSecurityLevel();
			extractParameter();
		}		
	}
	
	public boolean isLibraryMethod() {
		return this.sootMethod.isJavaLibraryMethod();
	}
	
	private void extractReturnSecurityLevel() {
		for (Tag tag : this.sootMethod.getTags()) {
			if (tag instanceof VisibilityAnnotationTag) {
				VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
				for (AnnotationTag  annotationTag : visibilityAnnotationTag.getAnnotations()) {
					if (annotationTag.getType().equals(SecurityAnnotation.getSootAnnotationTag(ReturnSecurity.class))) {
						this.returnAnnotationAvailable  = true;
						for (int i = 0; i < annotationTag.getNumElems(); i++) {
							AnnotationElem annotationElem =  annotationTag.getElemAt(i);
							if (annotationElem.getKind() == "s".charAt(0)) {
								AnnotationStringElem annotationStringElem = (AnnotationStringElem) annotationElem;
								this.returnLevel = annotationStringElem.getValue();
							}
						}
					}
				}
			}
		}
	}
	
	public void extractParameter() {
		List<String> parameterNames = new ArrayList<String>();
		List<Type> parameterTypes = new ArrayList<Type>();
		for (Object object : this.sootMethod.getParameterTypes()) {
			parameterTypes.add((Type) object);
		}
		List<String> parameterSecurityLevels = extractParameterSecurityLevel();
		for (Tag tag : sootMethod.getTags()) {
			if (tag instanceof ParamNamesTag) {
				ParamNamesTag paramNamesTag = (ParamNamesTag) tag;
				for (String name : paramNamesTag.getNames()) {
					parameterNames.add(name);
				}
			}
		}
		int max = Math.max(parameterNames.size(), Math.max(parameterSecurityLevels.size(), parameterTypes.size()));
		for (int i = 0; i < max; i++) {
			String name = (i < parameterNames.size()) ? parameterNames.get(i) : null;
			Type type = (i < parameterTypes.size()) ? parameterTypes.get(i) : null;
			String level = (i < parameterSecurityLevels.size()) ? parameterSecurityLevels.get(i) : null;
			this.addMethodParameter(new MethodParameter(i, name, type, level));
		}
	}
	
	public List<String> extractParameterSecurityLevel() {
		List<String> parameterSecurityLevels = new ArrayList<String>();
		for (Tag tag : sootMethod.getTags()) {
			if (tag instanceof VisibilityAnnotationTag) {
				VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
				for (AnnotationTag  annotationTag : visibilityAnnotationTag.getAnnotations()) {
					if (annotationTag.getType().equals(SecurityAnnotation.getSootAnnotationTag(ParameterSecurity.class))) {
						this.parameterAnnotationAvailable = true;
						for (int i = 0; i < annotationTag.getNumElems(); i++) {
							AnnotationElem annotationElem =  annotationTag.getElemAt(i);
							if (annotationElem.getKind() == "[".charAt(0)) {
								AnnotationArrayElem annotationArrayElem = (AnnotationArrayElem) annotationElem;
								for (int j = 0; j < annotationArrayElem.getNumValues(); j++) {
									AnnotationElem annotationElem1 = annotationArrayElem.getValueAt(j);
									if (annotationElem1.getKind() == "s".charAt(0)) {
										AnnotationStringElem annotationStringElem = (AnnotationStringElem) annotationElem1;
										parameterSecurityLevels.add(annotationStringElem.getValue());
									}
								}
							}
						}
					}
				}
			}
		}
		return parameterSecurityLevels;
	}
	
	public boolean isReturnSecurityValid(SecurityAnnotation securityAnnotation, SecurityLogger log) {
		if (! returnAnnotationAvailable) {
			log.error(SootUtils.generateFileName(sootMethod), 0, "Method " + SootUtils.generateMethodSignature(sootMethod) + " has no return security level annotation.");
			return false;
		}
		if (returnLevel == null) {
			log.error(SootUtils.generateFileName(sootMethod), 0, "Method " + SootUtils.generateMethodSignature(sootMethod) + " has no return security level.");
			return false;
		}
		try {
			LevelEquationValidityVistitor visitor = securityAnnotation.checkValidityOfReturnLevel(returnLevel, getListOfParameterLevels());
			if (visitor.isValid()) {
				if (visitor.isVoidInvalid()) {
					log.error(SootUtils.generateFileName(sootMethod), 0, "Method " + SootUtils.generateMethodSignature(sootMethod) + " has an invalid return security level equation. The level +'" + SecurityAnnotation.VOID_LEVEL + "' is not compatible with other levels.");
					return false;
				} else {
					this.returnLevelEquation = visitor.getLevelEquation();
					return true;
				}
			} else {
				List<String> invalidLevel = visitor.getValidLevels();
				if (invalidLevel.size() != 0) {
					for (String level : invalidLevel) {
						log.error(SootUtils.generateFileName(sootMethod), 0, "Method " + SootUtils.generateMethodSignature(sootMethod) + " has an invalid return security level '" + level + "'.");
					}
				} else {
					log.error(SootUtils.generateFileName(sootMethod), 0, "Method " + SootUtils.generateMethodSignature(sootMethod) + " has an invalid return security level.");
				}
				return false;
			}
		} catch (InvalidLevelException e) {
			log.exception("The return security level at Method " + SootUtils.generateMethodSignature(sootMethod) + " has an invalid level.", e);
			return false;
		} catch (InvalidEquationException e) {
			log.exception("The return security level at Method " + SootUtils.generateMethodSignature(sootMethod) + " is an invalid level equation.", e);
			return false;
		}
	}
	
	public boolean areMethodParameterSecuritiesValid(SecurityAnnotation securityAnnotation, SecurityLogger log) {
		if (!this.parameterAnnotationAvailable) {
			log.error(SootUtils.generateFileName(sootMethod), 0, "Method " + SootUtils.generateMethodSignature(sootMethod) + " has no parameter security levels annotation.");
			return false;
		}
		if (this.methodParameters == null) return true;
		boolean valid = true;
		int countParameter = 0;
		int countLevels = 0;
		for (Integer key : this.methodParameters.keySet()) {
			MethodParameter methodParameter = this.methodParameters.get(key);
			if (methodParameter.level != null) {
				countLevels++;
			}
			if (methodParameter.type != null || methodParameter.name != null) {
				countParameter++;
			}
		}
		if (countLevels > countParameter) {
			log.error(SootUtils.generateFileName(sootMethod), 0, "Method " + SootUtils.generateMethodSignature(sootMethod) + " has " + countParameter + " " + ((countParameter == 1) ? "parameter" : "parameters") + ". But " + countLevels + " parameter security "+ ((countLevels == 1) ? "level" : "levels") + " are defined.");
			valid = false;
		} else if ( countLevels < countParameter) {
			log.error(SootUtils.generateFileName(sootMethod), 0, "Method " + SootUtils.generateMethodSignature(sootMethod) + " has " + countParameter + " " + ((countParameter == 1) ? "parameter" : "parameters") + ". But unfortunately only " + countLevels + " parameter security "+ ((countLevels == 1) ? "level" : "levels") + " are defined.");
			valid = false;
		}
		if (! securityAnnotation.checkValidityOfParameterLevels(getListOfParameterLevels())) {
			for (String level : securityAnnotation.getInvalidParameterLevels(getListOfParameterLevels())) {
				log.error(SootUtils.generateFileName(sootMethod), 0, "Method " + SootUtils.generateMethodSignature(sootMethod) + " has an invalid parameter security level named '" + level + "'.");
			}
			valid = false;
		}
		return valid;
	}


	private List<String> getListOfParameterLevels() {
		List<String> parameterLevels = new ArrayList<String>();
		if (this.methodParameters != null) {
			for (Integer key : this.methodParameters.keySet()) {
				String level = methodParameters.get(key).getLevel();
				if (level != null) {
					parameterLevels.add(level);
				}
			}
		}
		return parameterLevels;
	}
	
	private void addMethodParameter(MethodParameter methodParameter) {
		if (this.methodParameters == null) this.methodParameters = new HashMap<Integer, MethodParameter>();
		this.methodParameters.put(methodParameter.getPosition(), methodParameter);
	}

	public List<MethodParameter> getMethodParameters() {
		List<MethodParameter> methodParametersList = new ArrayList<MethodParameter>();
		if (this.methodParameters != null) {
			for (Integer key : this.methodParameters.keySet()) {
				methodParametersList.add(this.methodParameters.get(key));
			}
		}
		return methodParametersList;
	}


	public SootMethod getSootMethod() {
		return sootMethod;
	}


	public String getReturnLevel() {
		return (returnLevel != null) ? returnLevel : "";
	}


	public LevelEquation getReturnLevelEquation() {
		return returnLevelEquation;
	}


	public boolean isReturnSecurityVoid(SecurityAnnotation securityAnnotation) {
		LevelEquationCalculateVoidVisitor visitor = securityAnnotation.getLevelEquationCalculateVoidVisitor();
		returnLevelEquation.accept(visitor);
		return visitor.isValid();
	}
	
	public MethodParameter getMethodParameterAtIndex(int i) {
		if (i < this.methodParameters.size()) {
			return this.methodParameters.get(new Integer(i));
		}
		return new MethodParameter(-1, null, null, null);
	}
	
	public boolean isParameterAnnotationAvailable() {
		return parameterAnnotationAvailable;
	}

	public boolean isReturnAnnotationAvailable() {
		return returnAnnotationAvailable;
	}
}

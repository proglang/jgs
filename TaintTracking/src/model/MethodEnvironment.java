package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.SootException.InvalidEquationException;
import exception.SootException.InvalidLevelException;

import logging.SecurityLogger;

import security.Annotations;
import security.LevelEquation;
import security.LevelEquationVisitor.LevelEquationCalculateVoidVisitor;
import security.LevelEquationVisitor.LevelEquationValidityVistitor;
import security.SecurityAnnotation;
import soot.SootMethod;
import soot.Type;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import utils.SecurityMessages;
import utils.SootUtils;

/**
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class MethodEnvironment extends Environment {
	
	/** */
	private Map<Integer, MethodParameter> methodParameters = null;
	/** */
	private boolean parameterAnnotationAvailable = false;
	/** */
	private SootMethod sootMethod;
	/** */
	private String returnLevel = null;
	/** */
	private LevelEquation returnLevelEquation = null;
	/** */
	private boolean returnAnnotationAvailable = false;
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public class MethodParameter {
		
		/** */
		private int position;
		/** */
		private Type type;
		/** */
		private String name;
		/** */
		private String level;
		
		/**
		 * 
		 * @param position
		 * @param name
		 * @param type
		 * @param level
		 */
		public MethodParameter(int position, String name, Type type, String level) {
			super();
			this.position = position;
			this.name = name;
			this.type = type;
			this.level = level;
		}
		
		/**
		 * 
		 * @return
		 */
		public int getPosition() {
			return this.position;
		}
		
		/**
		 * 
		 * @return
		 */
		public Type getType() {
			return this.type;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getName() {
			return this.name;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getLevel() {
			return this.level;
		}	
	}
	
	/**
	 * 
	 * @param sootMethod
	 * @param log
	 * @param securityAnnotation
	 */
	public MethodEnvironment(SootMethod sootMethod, SecurityLogger log, SecurityAnnotation securityAnnotation) {
		super(log, securityAnnotation);
		this.sootMethod = sootMethod;
		extractReturnSecurityLevel();
		extractParameter();	
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isLibraryMethod() {
		return this.sootMethod.isJavaLibraryMethod();
	}
	
	/**
	 * 
	 */
	private void extractReturnSecurityLevel() {
		if (getSecurityAnnotation().isIdMethod(this.sootMethod)) {
			this.returnAnnotationAvailable  = true;
			this.returnLevel = getSecurityAnnotation().getReturnSecurityLevelOfIdMethod(sootMethod);
		} else if (SootUtils.isClinitMethod(sootMethod) || SootUtils.isInitMethod(sootMethod)) {
			this.returnLevel = SecurityAnnotation.VOID_LEVEL;
		} else {
			for (Tag tag : this.sootMethod.getTags()) {
				if (tag instanceof VisibilityAnnotationTag) {
					VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
					for (AnnotationTag  annotationTag : visibilityAnnotationTag.getAnnotations()) {
						if (annotationTag.getType().equals(SecurityAnnotation.getSootAnnotationTag(Annotations.ReturnSecurity.class))) {
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
	}
	
	/**
	 * 
	 */
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
	
	/**
	 * 
	 * @return
	 */
	public List<String> extractParameterSecurityLevel() {
		List<String> parameterSecurityLevels = new ArrayList<String>();
		if (getSecurityAnnotation().isIdMethod(this.sootMethod)) {
			this.parameterAnnotationAvailable = true;
			parameterSecurityLevels.add(getSecurityAnnotation().getReturnSecurityLevelOfIdMethod(sootMethod));
		} else if (SootUtils.isClinitMethod(sootMethod)) {
			this.parameterAnnotationAvailable = true;
		} else {
			if (SootUtils.isInitMethod(sootMethod) && sootMethod.getParameterCount() == 0) {
				this.parameterAnnotationAvailable = true;
			}
			for (Tag tag : sootMethod.getTags()) {
				if (tag instanceof VisibilityAnnotationTag) {
					VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
					for (AnnotationTag  annotationTag : visibilityAnnotationTag.getAnnotations()) {
						if (annotationTag.getType().equals(SecurityAnnotation.getSootAnnotationTag(Annotations.ParameterSecurity.class))) {
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
		}
		return parameterSecurityLevels;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isReturnSecurityValid() {
		if (! returnAnnotationAvailable && ! (SootUtils.isClinitMethod(sootMethod) || SootUtils.isInitMethod(sootMethod))) {
			getLog().error(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.noMethodAnnotation(SootUtils.generateMethodSignature(sootMethod)));
			return false;
		}
		if (returnAnnotationAvailable && SootUtils.isInitMethod(sootMethod)) {
			getLog().warning(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.constructorReturnNotRequired(SootUtils.generateMethodSignature(sootMethod)));
		}
		if (returnLevel == null) {
			getLog().error(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.noMethodLevel(SootUtils.generateMethodSignature(sootMethod)));
			return false;
		}
		try {
			LevelEquationValidityVistitor visitor = getSecurityAnnotation().checkValidityOfReturnLevel(returnLevel, getListOfParameterLevels());
			if (visitor.isValid()) {
				if (visitor.isVoidInvalid()) {
					getLog().error(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.incompatibaleParameterLevels(SootUtils.generateMethodSignature(sootMethod), SecurityAnnotation.VOID_LEVEL));
					return false;
				} else {
					this.returnLevelEquation = visitor.getLevelEquation();
					return true;
				}
			} else {
				List<String> invalidLevel = visitor.getValidLevels();
				if (invalidLevel.size() != 0) {
					for (String level : invalidLevel) {
						getLog().error(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.invalidReturnLevel(SootUtils.generateMethodSignature(sootMethod), level));
					}
				} else {
					getLog().error(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.invalidReturnEquation(SootUtils.generateMethodSignature(sootMethod)));
				}
				return false;
			}
		} catch (InvalidLevelException | InvalidEquationException e) {
			getLog().exception(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.invalidReturnEquation(SootUtils.generateMethodSignature(sootMethod)), e);
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean areMethodParameterSecuritiesValid() {
		if (!this.parameterAnnotationAvailable) {
			getLog().error(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.noParameterAnnotation(SootUtils.generateMethodSignature(sootMethod)));
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
			getLog().error(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.moreParameterLevels(SootUtils.generateMethodSignature(sootMethod), countParameter, countLevels));
			valid = false;
		} else if ( countLevels < countParameter) {
			getLog().error(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.moreParameterLevels(SootUtils.generateMethodSignature(sootMethod), countParameter, countLevels));
			valid = false;
		}
		if (! getSecurityAnnotation().checkValidityOfParameterLevels(getListOfParameterLevels())) {
			for (String level : getSecurityAnnotation().getInvalidParameterLevels(getListOfParameterLevels())) {
				getLog().error(SootUtils.generateFileName(sootMethod), 0, SecurityMessages.invalidParameterLevel(SootUtils.generateMethodSignature(sootMethod), level));
			}
			valid = false;
		}
		return valid;
	}

	/**
	 * 
	 * @return
	 */
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
	
	/**
	 * 
	 * @param methodParameter
	 */
	private void addMethodParameter(MethodParameter methodParameter) {
		if (this.methodParameters == null) this.methodParameters = new HashMap<Integer, MethodParameter>();
		this.methodParameters.put(methodParameter.getPosition(), methodParameter);
	}

	/**
	 * 
	 * @return
	 */
	public List<MethodParameter> getMethodParameters() {
		List<MethodParameter> methodParametersList = new ArrayList<MethodParameter>();
		if (this.methodParameters != null) {
			for (Integer key : this.methodParameters.keySet()) {
				methodParametersList.add(this.methodParameters.get(key));
			}
		}
		return methodParametersList;
	}

	/**
	 * 
	 * @return
	 */
	public SootMethod getSootMethod() {
		return sootMethod;
	}

	/**
	 * 
	 * @return
	 */
	public String getReturnLevel() {
		return (returnLevel != null) ? returnLevel : "";
	}

	/**
	 * 
	 * @return
	 */
	public LevelEquation getReturnLevelEquation() {
		return returnLevelEquation;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isReturnSecurityVoid() {
		LevelEquationCalculateVoidVisitor visitor = getSecurityAnnotation().getLevelEquationCalculateVoidVisitor();
		returnLevelEquation.accept(visitor);
		return visitor.isValid();
	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public MethodParameter getMethodParameterAtIndex(int i) {
		if (i < this.methodParameters.size()) {
			return this.methodParameters.get(new Integer(i));
		}
		return new MethodParameter(-1, null, null, null);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isParameterAnnotationAvailable() {
		return parameterAnnotationAvailable;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isReturnAnnotationAvailable() {
		return returnAnnotationAvailable;
	}
}

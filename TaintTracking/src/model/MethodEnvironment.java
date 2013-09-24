package model;

import java.util.*;

import exception.SootException.*;
import logging.SecurityLogger;
import security.*;
import security.LevelEquationVisitor.*;
import soot.*;
import soot.tagkit.*;
import utils.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.3
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
	/** */
	private List<String> expectedWriteEffects = new ArrayList<String>();
	/** */
	private boolean writeEffectAnnotationAvailable = false;
	/** */
	private List<String> expectedClassWriteEffects = new ArrayList<String>();
	/** */
	private boolean classWriteEffectAnnotationAvailable = false;
	
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
		extractWriteEffects();
		extractClassWriteEffects();
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
			String annotations = SootUtils.extractAnnotationString(SecurityAnnotation.getSootAnnotationTag(Annotations.ReturnSecurity.class), sootMethod.getTags());
			this.returnAnnotationAvailable = annotations != null;
			if (returnAnnotationAvailable) {
				this.returnLevel = annotations;
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
			List<String> annotations = SootUtils.extractAnnotationStringArray(SecurityAnnotation.getSootAnnotationTag(Annotations.ParameterSecurity.class), sootMethod.getTags());
			boolean annotationsAvailable = annotations != null;
			if (annotationsAvailable) {
				this.parameterAnnotationAvailable = true;
				parameterSecurityLevels = annotations;
			}
		}
		return parameterSecurityLevels;
	}
	
	/**
	 * 
	 */
	private void extractWriteEffects() {
		if (SootUtils.isClinitMethod(sootMethod) || getSecurityAnnotation().isIdMethod(this.sootMethod)) {
			this.writeEffectAnnotationAvailable = true;
		} else {
			if (SootUtils.isInitMethod(sootMethod) && sootMethod.getParameterCount() == 0) {
				this.writeEffectAnnotationAvailable = true;
			} 
			List<String> annotations = SootUtils.extractAnnotationStringArray(SecurityAnnotation.getSootAnnotationTag(Annotations.WriteEffect.class), sootMethod.getTags());
			boolean annotationAvailable = annotations != null;
			if (annotationAvailable) {
				this.writeEffectAnnotationAvailable = true;
				this.expectedWriteEffects = annotations;
			}
		}
	}
	
	/**
	 * 
	 */
	private void extractClassWriteEffects() {
		if (getSecurityAnnotation().isIdMethod(this.sootMethod)) {
			this.classWriteEffectAnnotationAvailable = true;
		} else {
			SootClass sootClass = sootMethod.getDeclaringClass();
			List<String> annotations = SootUtils.extractAnnotationStringArray(SecurityAnnotation.getSootAnnotationTag(Annotations.WriteEffect.class), sootClass.getTags());
			this.classWriteEffectAnnotationAvailable = annotations != null;
			if (classWriteEffectAnnotationAvailable) {
				this.expectedClassWriteEffects = annotations;
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isReturnSecurityValid() {
		String methodSignature = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		String fileName = SootUtils.generateFileName(sootMethod);
		if (! returnAnnotationAvailable && ! (SootUtils.isClinitMethod(sootMethod) || SootUtils.isInitMethod(sootMethod))) {
			getLog().error(fileName, 0, SecurityMessages.noMethodAnnotation(methodSignature));
			return false;
		}
		if (returnAnnotationAvailable && SootUtils.isInitMethod(sootMethod)) {
			getLog().warning(fileName, 0, SecurityMessages.constructorReturnNotRequired(methodSignature));
		}
		if (returnLevel == null) {
			getLog().error(fileName, 0, SecurityMessages.noMethodLevel(methodSignature));
			return false;
		}
		try {
			LevelEquationValidityVistitor visitor = getSecurityAnnotation().checkValidityOfReturnLevel(returnLevel, getListOfParameterLevels());
			if (visitor.isValid()) {
				if (visitor.isVoidInvalid()) {
					getLog().error(fileName, 0, SecurityMessages.incompatibaleParameterLevels(methodSignature, SecurityAnnotation.VOID_LEVEL));
					return false;
				} else {
					this.returnLevelEquation = visitor.getLevelEquation();
					return true;
				}
			} else {
				List<String> invalidLevel = visitor.getValidLevels();
				if (invalidLevel.size() != 0) {
					for (String level : invalidLevel) {
						getLog().error(fileName, 0, SecurityMessages.invalidReturnLevel(methodSignature, level));
					}
				} else {
					getLog().error(fileName, 0, SecurityMessages.invalidReturnEquation(methodSignature));
				}
				return false;
			}
		} catch (InvalidLevelException | InvalidEquationException e) {
			getLog().exception(fileName, 0, SecurityMessages.invalidReturnEquation(methodSignature), e);
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean areMethodParameterSecuritiesValid() {
		String methodSignature = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		String fileName = SootUtils.generateFileName(sootMethod);
		if (! this.parameterAnnotationAvailable) {
			getLog().error(fileName, 0, SecurityMessages.noParameterAnnotation(methodSignature));
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
			getLog().error(fileName, 0, SecurityMessages.moreParameterLevels(methodSignature, countParameter, countLevels));
			valid = false;
		} else if ( countLevels < countParameter) {
			getLog().error(fileName, 0, SecurityMessages.moreParameterLevels(methodSignature, countParameter, countLevels));
			valid = false;
		}
		if (! getSecurityAnnotation().checkValidityOfParameterLevels(getListOfParameterLevels())) {
			for (String level : getSecurityAnnotation().getInvalidParameterLevels(getListOfParameterLevels())) {
				getLog().error(fileName, 0, SecurityMessages.invalidParameterLevel(methodSignature, level));
			}
			valid = false;
		}
		return valid;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean areWriteEffectsValid() {
		String methodSignature = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		String fileName = SootUtils.generateFileName(sootMethod);
		if (! this.writeEffectAnnotationAvailable) {
			getLog().error(fileName, 0, SecurityMessages.noWriteEffectAnnotation(methodSignature));
			return false;
		}
		if (! getSecurityAnnotation().checkValidityOfLevels(expectedWriteEffects)) {
			for (String invalidEffect : getSecurityAnnotation().getInvalidLevels(expectedWriteEffects)) {
				getLog().error(fileName, 0, SecurityMessages.invalidWriteEffect(methodSignature, invalidEffect));
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean areClassWriteEffectsValid() {
		String classSignature = SootUtils.generateClassSignature(sootMethod.getDeclaringClass(), false);
		String fileName = SootUtils.generateFileName(sootMethod);
		if (! this.classWriteEffectAnnotationAvailable) {
			getLog().error(fileName, 0, SecurityMessages.noClassWriteEffectAnnotation(classSignature));
			return false;
		}
		if (! getSecurityAnnotation().checkValidityOfLevels(expectedClassWriteEffects)) {
			for (String invalidEffect : getSecurityAnnotation().getInvalidLevels(expectedClassWriteEffects)) {
				getLog().error(fileName, 0, SecurityMessages.invalidClassWriteEffect(classSignature, invalidEffect));
			}
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getExpectedWriteEffects() {
		return expectedWriteEffects;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isWriteEffectAnnotationAvailable() {
		return writeEffectAnnotationAvailable;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getExpectedClassWriteEffects() {
		return expectedClassWriteEffects;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isClassWriteEffectAnnotationAvailable() {
		return classWriteEffectAnnotationAvailable;
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

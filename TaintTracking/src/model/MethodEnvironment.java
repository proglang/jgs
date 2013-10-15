package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logging.SecurityLogger;
import security.Annotations;
import security.LevelEquation;
import security.LevelEquationVisitor.LevelEquationCalculateVoidVisitor;
import security.LevelEquationVisitor.LevelEquationValidityVistitor;
import security.SecurityAnnotation;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.Tag;
import utils.SecurityMessages;
import utils.SootUtils;
import exception.SootException.InvalidEquationException;
import exception.SootException.InvalidLevelException;

/**
 * <h1>Analysis environment for methods</h1>
 * 
 * The {@link MethodEnvironment} provides a environment for analyzing a {@link SootMethod}.
 * Therefore it extends the base analysis environment {@link Environment} in order to access a
 * logger and the security annotation. The environment provides methods for getting the required
 * annotations at the method as well as at the class which declares the method, and also methods
 * which checks the validity of the levels and effects that are given by those annotations. In
 * addition the environment gives direct access to some methods of the analyzed {@link SootMethod}.
 * This environment handles {@link SootMethod} which will be analyzed only indirectly (e.g. the
 * invoke of a {@link SootMethod} inside of a method body, see {@link MethodEnvironment}).
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 * @see Environment
 * @see Annotations.WriteEffect
 * @see Annotations.ParameterSecurity
 * @see Annotations.ReturnSecurity
 */
public class MethodEnvironment extends Environment {

	/**
	 * <h1>Single parameter of a method</h1>
	 * 
	 * The {@link MethodParameter} represents a single parameter of the analyzed method and includes
	 * the information about the position, the name, the <em>security level</em> as well as the
	 * {@link Type} of the parameter.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public class MethodParameter {

		/** The <em>security level</em> of this parameter. */
		private String level;
		/** The name of this parameter. */
		private String name;
		/** The position of this parameter in the parameter list. */
		private int position;
		/** The type of this parameter. */
		private Type type;

		/**
		 * Constructor of a {@link MethodParameter} that represents a parameter of the analyzed
		 * method which includes the position, the name, the <em>security level</em> as well as the
		 * type of the parameter.
		 * 
		 * @param position
		 *            The position of the parameter.
		 * @param name
		 *            The name of the parameter.
		 * @param type
		 *            The type of the parameter.
		 * @param level
		 *            The <em>security level</em> of the parameter.
		 */
		public MethodParameter(int position, String name, Type type, String level) {
			super();
			this.position = position;
			this.name = name;
			this.type = type;
			this.level = level;
		}

		/**
		 * The method returns the <em>security level</em> of this parameter.
		 * 
		 * @return The level of the parameter.
		 */
		public String getLevel() {
			return this.level;
		}

		/**
		 * The method returns the name of this parameter.
		 * 
		 * @return The name of the parameter.
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * The method returns the position of this parameter in the parameter list.
		 * 
		 * @return The position of the parameter.
		 */
		public int getPosition() {
			return this.position;
		}

		/**
		 * The method returns the {@link Type} of this parameter.
		 * 
		 * @return The type of the parameter.
		 */
		public Type getType() {
			return this.type;
		}
	}

	/**
	 * Value which indicates whether the <em>effect annotation</em> at the class which declares the
	 * analyzed method is available.
	 */
	private boolean classWriteEffectAnnotationAvailable = false;
	/** The <em>write effects</em> of the class which declares the {@link SootMethod}. */
	private List<String> expectedClassWriteEffects = new ArrayList<String>();
	/** The expected <em>write effects</em> of the analyzed {@link SootMethod}. */
	private List<String> expectedWriteEffects = new ArrayList<String>();
	/**
	 * Map that maps the position of a method parameter to the method parameter object that contains
	 * the name, <em>security level</em>, the {@link Type} as well as the position (see
	 * {@link MethodParameter}).
	 */
	private Map<Integer, MethodParameter> methodParameters = null;
	/**
	 * Value which indicates whether the parameter <em>security annotation</em> at the analyzed
	 * method is available.
	 */
	private boolean parameterAnnotationAvailable = false;
	/**
	 * Value which indicates whether the return <em>security annotation</em> at the analyzed method
	 * is available.
	 */
	private boolean returnAnnotationAvailable = false;
	/** The expected return <em>security level</em> of the analyzed {@link SootMethod}. */
	private String returnLevel = null;
	/**
	 * The return <em>security level</em> equation, if the return <em>security level</em> depends on
	 * variable parameter <em>security levels</em>.
	 */
	private LevelEquation returnLevelEquation = null;
	/** The analyzed method, for which this is the environment. */
	private SootMethod sootMethod;
	/**
	 * Value which indicates whether the <em>effect annotation</em> at the analyzed method is
	 * available.
	 */
	private boolean writeEffectAnnotationAvailable = false;

	/**
	 * Constructor of a {@link MethodEnvironment} that requires a {@link SootMethod} which should be
	 * analyzed, a logger in order to allow logging for this object as well as a security annotation
	 * object in order to provide the handling of <em>security levels</em>. By calling the
	 * constructor all required extractions of the annotations will be done automatically.
	 * 
	 * @param sootMethod
	 *            The {@link SootMethod} that should be analyzed.
	 * @param log
	 *            A {@link SecurityLogger} in order to allow logging for this object.
	 * @param securityAnnotation
	 *            A {@link SecurityAnnotation} in order to provide the handling of
	 *            <em>security levels</em>.
	 */
	public MethodEnvironment(SootMethod sootMethod, SecurityLogger log,
			SecurityAnnotation securityAnnotation) {
		super(log, securityAnnotation);
		this.sootMethod = sootMethod;
		extractReturnSecurityLevel();
		extractParameter();
		extractWriteEffects();
		extractClassWriteEffects();
	}

	/**
	 * The method checks whether the class <em>write effects</em> are valid, i.e. checks whether the
	 * annotation is available and also whether the provided <em>write effects</em> are valid
	 * <em>security levels</em>. If a failure is detected, this is logged with the logger
	 * {@link MethodEnvironment#getLog()}.
	 * 
	 * @return {@code true} if the annotation is available and all provided class
	 *         <em>write effect</em> are valid, otherwise {@code false}.
	 */
	public boolean areClassWriteEffectsValid() {
		String classSignature = SootUtils.generateClassSignature(sootMethod.getDeclaringClass(),
				false);
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
	 * The method checks whether the parameter <em>security levels</em> are valid, i.e. checks
	 * whether the annotation is available, checks whether the given number of parameter
	 * <em>security levels</em> match the number of the method parameter and also whether the
	 * provided levels are valid <em>security levels</em>. If a failure is detected, this is logged
	 * with the logger {@link MethodEnvironment#getLog()}.
	 * 
	 * @return {@code true} if the security annotation is available for the method parameters as
	 *         well as all provided <em>security levels</em> are valid and the number of the levels
	 *         match the count of the method parameter, otherwise {@code false}.
	 */
	public boolean areMethodParameterSecuritiesValid() {
		String methodSignature = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		if (!this.parameterAnnotationAvailable) {
			logError(SecurityMessages.noParameterAnnotation(methodSignature));
			return false;
		}
		if (this.methodParameters == null)
			return true;
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
			logError(SecurityMessages.moreParameterLevels(methodSignature, countParameter,
					countLevels));
			valid = false;
		} else if (countLevels < countParameter) {
			logError(SecurityMessages.moreParameterLevels(methodSignature, countParameter,
					countLevels));
			valid = false;
		}
		if (!getSecurityAnnotation().checkValidityOfParameterLevels(getListOfParameterLevels())) {
			for (String level : getSecurityAnnotation().getInvalidParameterLevels(
					getListOfParameterLevels())) {
				logError(SecurityMessages.invalidParameterLevel(methodSignature, level));
			}
			valid = false;
		}
		return valid;
	}

	/**
	 * The method checks whether the <em>write effects</em> of the analyzed method are valid, i.e.
	 * checks whether the annotation is available at the method and also whether the provided
	 * <em>write effects</em> are valid <em>security level</em>. If a failure is detected, this is
	 * logged with the logger {@link MethodEnvironment#getLog()}.
	 * 
	 * @return {@code true} if the annotation is available and all provided <em>write effect</em>
	 *         are valid, otherwise {@code false}.
	 */
	public boolean areWriteEffectsValid() {
		String methodSignature = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		if (!this.writeEffectAnnotationAvailable) {
			logError(SecurityMessages.noWriteEffectAnnotation(methodSignature));
			return false;
		}
		if (!getSecurityAnnotation().checkValidityOfLevels(expectedWriteEffects)) {
			for (String invalidEffect : getSecurityAnnotation().getInvalidLevels(
					expectedWriteEffects)) {
				logError(SecurityMessages.invalidWriteEffect(methodSignature, invalidEffect));
			}
			return false;
		}
		return true;
	}

	/**
	 * Returns the <em>write effects</em> of the class which declares the analyzed method.
	 * 
	 * @return The class <em>write effects</em>.
	 */
	public List<String> getExpectedClassWriteEffects() {
		return expectedClassWriteEffects;
	}

	/**
	 * Returns the <em>write effects</em> of the analyzed method.
	 * 
	 * @return The method <em>write effects</em>.
	 */
	public List<String> getExpectedWriteEffects() {
		return expectedWriteEffects;
	}

	/**
	 * Returns the {@link MethodParameter} that represent the parameter of the analyzed method at
	 * the given position. Note, that the method will return a invalid {@link MethodParameter}
	 * object (position will be {@code -1}, the level, the name and the {@link Type} will be
	 * {@code null}) if the map {@link MethodEnvironment#methodParameters} does not contain the
	 * given index.
	 * 
	 * @param i
	 *            The index of the parameter which should be returned.
	 * @return The method parameter which represent the parameter at the given index.
	 * @see MethodParameter
	 */
	public MethodParameter getMethodParameterAt(int i) {
		if (i < this.methodParameters.size()) {
			return this.methodParameters.get(new Integer(i));
		}
		return new MethodParameter(-1, null, null, null);
	}

	/**
	 * Returns a list that contains the method parameter of this analyzed method. Note, that the
	 * position in the returned list does not match the position of the method declaration.
	 * 
	 * @return List of all parameter of the analyzed method.
	 * @see MethodParameter
	 * @see MethodEnvironment#methodParameters
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
	 * Returns the extracted return <em>security level</em> of the analyzed method, if it exists.
	 * Otherwise the method will return an empty String as <em>security level</em>.
	 * 
	 * @return The return <em>security level</em> of the analyzed method, if it exists.
	 */
	public String getReturnLevel() {
		return (returnLevel != null) ? returnLevel : "";
	}

	/**
	 * Returns the <em>security level</em> equation of the analyzed method. Note, that the method
	 * will return {@code null} as long as the method
	 * {@link MethodEnvironment#isReturnSecurityValid()} was not called. This method will create the
	 * equation from the return <em>security level</em>.
	 * 
	 * @return The <em>security level</em> equation, if the method
	 *         {@link MethodEnvironment#isReturnSecurityValid()} was called, otherwise {@code null}.
	 */
	public LevelEquation getReturnLevelEquation() {
		return returnLevelEquation;
	}

	/**
	 * The method returns the analyzed {@link SootMethod} for which this is the environment.
	 * 
	 * @return The analyzed method.
	 */
	public SootMethod getSootMethod() {
		return sootMethod;
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
	 * Indicates whether the analyzed {@link SootMethod} is a library method.
	 * 
	 * @return {@code true} if the method is a library method, otherwise {@code false}.
	 */
	public boolean isLibraryMethod() {
		return this.sootMethod.isJavaLibraryMethod();
	}

	/**
	 * Indicates whether the parameter <em>security annotation</em> at the analyzed method is
	 * available.
	 * 
	 * @return {@code true} if the annotation is available, otherwise {@code false}.
	 */
	public boolean isParameterAnnotationAvailable() {
		return parameterAnnotationAvailable;
	}

	/**
	 * Indicates whether the return <em>security annotation</em> at the analyzed method is
	 * available.
	 * 
	 * @return {@code true} if the annotation is available, otherwise {@code false}.
	 */
	public boolean isReturnAnnotationAvailable() {
		return returnAnnotationAvailable;
	}

	/**
	 * The method checks whether the return <em>security level</em> is valid, i.e. checks whether
	 * the annotation is available, checks whether the extracted return <em>security level</em> is
	 * not {@code null}. If this is the case, a visitor will be created, which checks the components
	 * of the level equation for validity. If a failure is detected, this is logged with the logger
	 * {@link MethodEnvironment#getLog()}. If none failure is detected, then the method will store
	 * the return level equation in {@link MethodEnvironment#returnLevelEquation} that is generated
	 * from the {@link MethodEnvironment#returnLevel}.
	 * 
	 * @return {@code true} if the return security annotation is available for the method as well as
	 *         all provided <em>security levels</em> are valid, otherwise {@code false}.
	 */
	public boolean isReturnSecurityValid() {
		String methodSignature = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		if (!returnAnnotationAvailable
				&& !(SootUtils.isClinitMethod(sootMethod) || SootUtils.isInitMethod(sootMethod))) {
			logError(SecurityMessages.noMethodAnnotation(methodSignature));
			return false;
		}
		if (returnAnnotationAvailable && SootUtils.isInitMethod(sootMethod)) {
			logWarning(SecurityMessages.constructorReturnNotRequired(methodSignature));
		}
		if (returnLevel == null) {
			logError(SecurityMessages.noMethodLevel(methodSignature));
			return false;
		}
		try {
			LevelEquationValidityVistitor visitor = getSecurityAnnotation()
					.checkValidityOfReturnLevel(returnLevel, getListOfParameterLevels());
			if (visitor.isValid()) {
				if (visitor.isVoidInvalid()) {
					logError(SecurityMessages.incompatibaleParameterLevels(methodSignature,
							SecurityAnnotation.VOID_LEVEL));
					return false;
				} else {
					this.returnLevelEquation = visitor.getLevelEquation();
					return true;
				}
			} else {
				List<String> invalidLevel = visitor.getValidLevels();
				if (invalidLevel.size() != 0) {
					for (String level : invalidLevel) {
						logError(SecurityMessages.invalidReturnLevel(methodSignature, level));
					}
				} else {
					logError(SecurityMessages.invalidReturnEquation(methodSignature));
				}
				return false;
			}
		} catch (InvalidLevelException | InvalidEquationException e) {
			logException(SecurityMessages.invalidReturnEquation(methodSignature), e);
			return false;
		}
	}

	/**
	 * Method checks whether the the analyzed method has a 'void' return <em>security level</em>.
	 * This will done by generating a {@link LevelEquationCalculateVoidVisitor} and calculating the
	 * {@link MethodEnvironment#returnLevelEquation} with the help of this visitor. Note, that the
	 * method will return {@code false} as long as the method
	 * {@link MethodEnvironment#isReturnSecurityValid()} was not called.
	 * 
	 * @return {@code true} if the method {@link MethodEnvironment#isReturnSecurityValid()} was
	 *         called and the corresponding visitor calculates that the return
	 *         <em>security level</em> is 'void', otherwise {@code false}.
	 * @see LevelEquationCalculateVoidVisitor
	 * @see MethodEnvironment#isReturnSecurityValid()
	 */
	public boolean isReturnSecurityVoid() {
		if (returnLevelEquation == null) {
			return false;
		}
		LevelEquationCalculateVoidVisitor visitor = getSecurityAnnotation()
				.getLevelEquationCalculateVoidVisitor();
		returnLevelEquation.accept(visitor);
		return visitor.isValid();
	}

	/**
	 * Indicates whether the <em>effect annotation</em> at the analyzed method is available.
	 * 
	 * @return {@code true} if the annotation is available, otherwise {@code false}.
	 */
	public boolean isWriteEffectAnnotationAvailable() {
		return writeEffectAnnotationAvailable;
	}

	/**
	 * Adds the given {@link MethodParameter} to the method parameter map. As key in the map the
	 * position of the given method parameter is chosen.
	 * 
	 * @param methodParameter
	 *            {@link MethodParameter} which should be stored as parameter of the analyzed
	 *            method.
	 */
	private void addMethodParameter(MethodParameter methodParameter) {
		if (this.methodParameters == null)
			this.methodParameters = new HashMap<Integer, MethodParameter>();
		this.methodParameters.put(methodParameter.getPosition(), methodParameter);
	}

	/**
	 * Extracts the annotation of the type {@link Annotations.WriteEffect} which should exist at the
	 * class that declares the analyzed method. The extracted <em>write effects</em> will be stored
	 * in {@link MethodEnvironment#expectedClassWriteEffects} and based on whether the annotation at
	 * the class exists {@link MethodEnvironment#classWriteEffectAnnotationAvailable} will be set.
	 * Note, that if the analyzed method is an id function it doesn't require those class
	 * <em>write effects</em>.
	 * 
	 * @see SootUtils#extractAnnotationStringArray(String, List)
	 */
	private void extractClassWriteEffects() {
		if (getSecurityAnnotation().isIdFunction(this.sootMethod)) {
			this.classWriteEffectAnnotationAvailable = true;
		} else {
			SootClass sootClass = sootMethod.getDeclaringClass();
			List<String> annotations = SootUtils.extractAnnotationStringArray(
					SecurityAnnotation.getSootAnnotationTag(Annotations.WriteEffect.class),
					sootClass.getTags());
			this.classWriteEffectAnnotationAvailable = annotations != null;
			if (classWriteEffectAnnotationAvailable) {
				this.expectedClassWriteEffects = annotations;
			}
		}
	}

	/**
	 * Extracts the <em>security levels</em> of the parameters and stores them together with the
	 * name, the position and the {@link Type} of each parameter in the method parameter map
	 * {@link MethodEnvironment#methodParameters}. Note that when inconsistencies are between the
	 * number of <em>security levels</em> and the number of parameters, then a correct extraction is
	 * impossible.
	 * 
	 * @see MethodEnvironment#extractParameterSecurityLevel()
	 * @see MethodParameter
	 * @see MethodEnvironment#methodParameters
	 */
	private void extractParameter() {
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
		int max = Math.max(parameterNames.size(),
				Math.max(parameterSecurityLevels.size(), parameterTypes.size()));
		for (int i = 0; i < max; i++) {
			String name = (i < parameterNames.size()) ? parameterNames.get(i) : null;
			Type type = (i < parameterTypes.size()) ? parameterTypes.get(i) : null;
			String level = (i < parameterSecurityLevels.size()) ? parameterSecurityLevels.get(i)
					: null;
			this.addMethodParameter(new MethodParameter(i, name, type, level));
		}
	}

	/**
	 * Extracts the annotation of the type {@link Annotations.ParameterSecurity} which should exist
	 * at the analyzed method. The extracted <em>security levels</em> will be stored in the list
	 * that the method returns. Based on whether the annotation at the method exists
	 * {@link MethodEnvironment#parameterAnnotationAvailable} will be set. Note, that if the
	 * analyzed method is an id function, then the parameter <em>security levels</em> is exactly the
	 * <em>security level</em> of the id function. If the analyzed method is a static initializer
	 * then the parameter <em>security level</em> is not required.
	 * 
	 * @see SootUtils#extractAnnotationStringArray(String, List)
	 * @return The list with the <em>security levels<em> of the parameters of the analyzed method.
	 */
	private List<String> extractParameterSecurityLevel() {
		List<String> parameterSecurityLevels = new ArrayList<String>();
		if (getSecurityAnnotation().isIdFunction(this.sootMethod)) {
			this.parameterAnnotationAvailable = true;
			parameterSecurityLevels.add(getSecurityAnnotation().getReturnSecurityLevelOfIdFunction(
					sootMethod));
		} else if (SootUtils.isClinitMethod(sootMethod)) {
			this.parameterAnnotationAvailable = true;
		} else {
			if (SootUtils.isInitMethod(sootMethod) && sootMethod.getParameterCount() == 0) {
				this.parameterAnnotationAvailable = true;
			}
			List<String> annotations = SootUtils.extractAnnotationStringArray(
					SecurityAnnotation.getSootAnnotationTag(Annotations.ParameterSecurity.class),
					sootMethod.getTags());
			boolean annotationsAvailable = annotations != null;
			if (annotationsAvailable) {
				this.parameterAnnotationAvailable = true;
				parameterSecurityLevels = annotations;
			}
		}
		return parameterSecurityLevels;
	}

	/**
	 * Extracts the annotation of the type {@link Annotations.ReturnSecurity} which should exist at
	 * the analyzed method. The extracted return <em>security effect</em> will be stored in
	 * {@link MethodEnvironment#returnLevel} and based on whether the annotation at the method
	 * exists {@link MethodEnvironment#returnAnnotationAvailable} will be set. Note, that if the
	 * analyzed method is an id function, then the return <em>security level</em> will be the level
	 * of the id function,if it is a static initializer or an initialization method, then the return
	 * <em>security level</em> will be the 'void' <em>security level</em>.
	 * 
	 * @see SootUtils#extractAnnotationString(String, List)
	 * @see SecurityAnnotation#VOID_LEVEL
	 */
	private void extractReturnSecurityLevel() {
		if (getSecurityAnnotation().isIdFunction(this.sootMethod)) {
			this.returnAnnotationAvailable = true;
			this.returnLevel = getSecurityAnnotation().getReturnSecurityLevelOfIdFunction(
					sootMethod);
		} else if (SootUtils.isClinitMethod(sootMethod) || SootUtils.isInitMethod(sootMethod)) {
			this.returnLevel = SecurityAnnotation.VOID_LEVEL;
		} else {
			String annotations = SootUtils.extractAnnotationString(
					SecurityAnnotation.getSootAnnotationTag(Annotations.ReturnSecurity.class),
					sootMethod.getTags());
			this.returnAnnotationAvailable = annotations != null;
			if (returnAnnotationAvailable) {
				this.returnLevel = annotations;
			}
		}
	}

	/**
	 * Extracts the annotation of the type {@link Annotations.WriteEffect} which should exist at the
	 * analyzed method. The extracted <em>write effects</em> will be stored in
	 * {@link MethodEnvironment#expectedWriteEffects} and based on whether the annotation at the
	 * method exists {@link MethodEnvironment#writeEffectAnnotationAvailable} will be set. Note,
	 * that if the analyzed method is an id function or the static initializer it doesn't require
	 * those <em>write effects</em>.
	 * 
	 * @see SootUtils#extractAnnotationStringArray(String, List)
	 */
	private void extractWriteEffects() {
		if (SootUtils.isClinitMethod(sootMethod)
				|| getSecurityAnnotation().isIdFunction(this.sootMethod)) {
			this.writeEffectAnnotationAvailable = true;
		} else {
			if (SootUtils.isInitMethod(sootMethod) && sootMethod.getParameterCount() == 0) {
				this.writeEffectAnnotationAvailable = true;
			}
			List<String> annotations = SootUtils.extractAnnotationStringArray(
					SecurityAnnotation.getSootAnnotationTag(Annotations.WriteEffect.class),
					sootMethod.getTags());
			boolean annotationAvailable = annotations != null;
			if (annotationAvailable) {
				this.writeEffectAnnotationAvailable = true;
				this.expectedWriteEffects = annotations;
			}
		}
	}

	/**
	 * Method returns the <em>security levels</em> of parameters of the analyzed method. Note that
	 * the returned list is not ordered.
	 * 
	 * @return The <em>security levels</em> of the method parameter of the analyzed method.
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
	 * Logs the given message as an error. The file name is created by the analyzed
	 * {@link SootMethod}, which this environment stores (see {@link MethodEnvironment#sootMethod}),
	 * the source line number is specified as 0.
	 * 
	 * @param msg
	 *            Message that should be printed as an error.
	 * @see SecurityLogger#error(String, long, String)
	 */
	protected void logError(String msg) {
		getLog().error(SootUtils.generateFileName(sootMethod), 0, msg);
	}

	/**
	 * Logs the given message as well as the Exception as an exception. The file name is created by
	 * the analyzed {@link SootMethod}, which this environment stores (see
	 * {@link MethodEnvironment#sootMethod}), the source line number is specified as 0.
	 * 
	 * @param msg
	 *            Message that should be printed as an exception.
	 * @param e
	 *            The exception which is the reason for the given exception message.
	 * @see SecurityLogger#exception(String, long, String, Throwable)
	 */
	protected void logException(String msg, Exception e) {
		getLog().exception(SootUtils.generateFileName(sootMethod), 0, msg, e);
	}

	/**
	 * Logs the given message as a warning. The file name is created by the analyzed
	 * {@link SootMethod}, which this environment stores (see {@link MethodEnvironment#sootMethod}),
	 * the source line number is specified as 0.
	 * 
	 * @param msg
	 *            Message that should be printed as a warning.
	 * @see SecurityLogger#warning(String, long, String)
	 */
	protected void logWarning(String msg) {
		getLog().warning(SootUtils.generateFileName(sootMethod), 0, msg);
	}
	

}

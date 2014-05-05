package model;

import static constraints.ConstraintsUtils.containsSetReturnReferenceFor;
import static constraints.ConstraintsUtils.getContainedLevelsOfSet;
import static constraints.ConstraintsUtils.containsSetParameterReferenceFor;
import static constraints.ConstraintsUtils.getInvalidParameterReferencesOfSet;
import static constraints.ConstraintsUtils.getInvalidReturnReferencesOfSet;
import static constraints.ConstraintsUtils.containsSetProgramCounterReference;
import static main.AnalysisType.CONSTRAINTS;
import static main.AnalysisType.LEVELS;
import static resource.Messages.getMsg;
import static utils.AnalysisUtils.generateFileName;
import static utils.AnalysisUtils.generateMethodSignature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logging.AnalysisLog;
import main.AnalysisType;
import security.ILevel;
import security.ILevelMediator;
import soot.SootMethod;
import soot.Type;
import constraints.ConstraintParameterRef;
import constraints.ConstraintReturnRef;
import constraints.IConstraint;
import exception.AnalysisTypeException;
import exception.AnnotationInvalidException;
import exception.LevelInvalidException;
import exception.MethodParameterNotFoundException;

/**
 * <h1>Analysis environment for methods</h1>
 * 
 * The {@link MethodEnvironment} provides a environment for analyzing a {@link SootMethod}. Therefore it extends the base analysis
 * environment {@link Environment} in order to access a logger and the security annotation. The environment provides methods for getting the
 * required annotations at the method as well as at the class which declares the method, and also methods which checks the validity of the
 * levels and effects that are given by those annotations. In addition the environment gives direct access to some methods of the analyzed
 * {@link SootMethod}. This environment handles {@link SootMethod} which will be analyzed only indirectly (e.g. the invoke of a
 * {@link SootMethod} inside of a method body, see {@link MethodEnvironment}).
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 * @see Environment
 */
public class MethodEnvironment extends Environment {

	/**
	 * <h1>Single parameter of a method</h1>
	 * 
	 * The {@link MethodParameter} represents a single parameter of the analyzed method and includes the information about the position, the
	 * name, the <em>security level</em> as well as the {@link Type} of the parameter.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class MethodParameter {

		/** The <em>security level</em> of this parameter. */
		private ILevel level;
		/** The name of this parameter. */
		private String name;
		/** The position of this parameter in the parameter list. */
		private int position;
		/** The type of this parameter. */
		private Type type;

		/**
		 * Constructor of a {@link MethodParameter} that represents a parameter of the analyzed method which includes the position, the name,
		 * the <em>security level</em> as well as the type of the parameter.
		 * 
		 * @param position
		 *          The position of the parameter.
		 * @param name
		 *          The name of the parameter.
		 * @param type
		 *          The type of the parameter.
		 * @param level
		 *          The <em>security level</em> of the parameter.
		 */
		public MethodParameter(int position, String name, Type type, ILevel level) {
			super();
			this.position = position;
			this.name = (name != null) ? name : "arg" + position;
			this.type = type;
			this.level = level;
		}

		/**
		 * The method returns the <em>security level</em> of this parameter.
		 * 
		 * @return The level of the parameter.
		 */
		public ILevel getLevel() {
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
	 * The <em>write effects</em> of the class which declares the {@link SootMethod}.
	 */
	private List<ILevel> classWriteEffects = new ArrayList<ILevel>();
	/**
	 * DOC
	 */
	private final Set<IConstraint> constraints = new HashSet<IConstraint>();
	/**
	 * DOC
	 */
	private final boolean isClinit;
	/**
	 * DOC
	 */
	private final boolean isIdFunction;
	/**
	 * DOC
	 */
	private final boolean isInit;
	/**
	 * DOC
	 */
	private final boolean isSootSecurityMethod;
	/**
	 * DOC
	 */
	private final boolean isVoid;
	/**
	 * Map that maps the position of a method parameter to the method parameter object that contains the name, <em>security level</em>, the
	 * {@link Type} as well as the position (see {@link MethodParameter}).
	 */
	private Map<Integer, MethodParameter> methodParameters = new HashMap<Integer, MethodParameter>();
	/**
	 * The expected return <em>security level</em> of the analyzed {@link SootMethod}.
	 */
	private ILevel returnLevel = null;
	/** The analyzed method, for which this is the environment. */
	private SootMethod sootMethod;

	/** The expected <em>write effects</em> of the analyzed {@link SootMethod}. */
	private List<ILevel> writeEffects = new ArrayList<ILevel>();

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @param isIdFunction
	 * @param isClinit
	 * @param isInit
	 * @param isVoid
	 * @param isSootSecurityMethod
	 * @param parameterSecurityLevel
	 * @param returnSecurityLevel
	 * @param methodWriteEffects
	 * @param classWriteEffects
	 * @param log
	 * @param mediator
	 */
	public MethodEnvironment(SootMethod sootMethod, boolean isIdFunction, boolean isClinit, boolean isInit, boolean isVoid,
			boolean isSootSecurityMethod, List<MethodParameter> parameterSecurityLevel, ILevel returnSecurityLevel,
			List<ILevel> methodWriteEffects, List<ILevel> classWriteEffects, Set<IConstraint> constraints, AnalysisLog log,
			ILevelMediator mediator) {
		super(log, mediator);
		this.sootMethod = sootMethod;
		this.isIdFunction = isIdFunction;
		this.isClinit = isClinit;
		this.isInit = isInit;
		this.isVoid = isVoid;
		this.isSootSecurityMethod = isSootSecurityMethod;
		for (int i = 0; i < parameterSecurityLevel.size(); i++) {
			methodParameters.put(new Integer(i), parameterSecurityLevel.get(i));
		}
		this.returnLevel = returnSecurityLevel;
		this.writeEffects.addAll(methodWriteEffects);
		this.classWriteEffects.addAll(classWriteEffects);
		this.constraints.addAll(constraints);
	}

	/**
	 * Returns the <em>write effects</em> of the class which declares the analyzed method.
	 * 
	 * @return The class <em>write effects</em>.
	 */
	public List<ILevel> getClassWriteEffects() {
		return classWriteEffects;
	}

	public Set<IConstraint> getSignatureContraints() {
		return new HashSet<IConstraint>(constraints);
	}

	/**
	 * Returns the {@link MethodParameter} that represent the parameter of the analyzed method at the given position. Note, that the method
	 * will return a invalid {@link MethodParameter} object (position will be {@code -1}, the level, the name and the {@link Type} will be
	 * {@code null}) if the map {@link MethodEnvironment#methodParameters} does not contain the given index.
	 * 
	 * @param i
	 *          The index of the parameter which should be returned.
	 * @return The method parameter which represent the parameter at the given index.
	 * @see MethodParameter
	 */
	public MethodParameter getMethodParameterAt(int i) {
		if (i < this.methodParameters.size()) {
			return this.methodParameters.get(new Integer(i));
		}
		throw new MethodParameterNotFoundException(getMsg("exception.environment.method_parameter_not_found",
				generateMethodSignature(sootMethod), i));
	}

	/**
	 * Returns a list that contains the method parameter of this analyzed method. Note, that the position in the returned list does not match
	 * the position of the method declaration.
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
	 * Returns the extracted return <em>security level</em> of the analyzed method, if it exists. Otherwise the method will return an empty
	 * String as <em>security level</em>.
	 * 
	 * @return The return <em>security level</em> of the analyzed method, if it exists.
	 */
	public ILevel getReturnLevel() {
		return returnLevel;
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
	 * Returns the <em>write effects</em> of the analyzed method.
	 * 
	 * @return The method <em>write effects</em>.
	 */
	public List<ILevel> getWriteEffects() {
		return writeEffects;
	}

	/**
	 * Indicates whether this method is a clinit method.
	 * 
	 * @return {@code true} if it is a clinit method, otherwise {@code false}.
	 */
	public boolean isClinit() {
		return isClinit;
	}

	/**
	 * Indicates whether this method is an id function or not.
	 * 
	 * @return {@code true} if it is an id function, otherwise {@code false}.
	 */
	public boolean isIdFunction() {
		return isIdFunction;
	}

	/**
	 * Indicates whether this method is an init method or not.
	 * 
	 * @return {@code true} if it is an init method, otherwise {@code false}.
	 */
	public boolean isInit() {
		return isInit;
	}

	/**
	 * Indicates whether the analyzed {@link SootMethod} is a library method.
	 * 
	 * @return {@code true} if the method is a library method, otherwise {@code false}.
	 */
	public boolean isLibraryMethod() {
		return sootMethod.isJavaLibraryMethod();
	}

	/**
	 * DOC
	 * 
	 * @param type
	 * 
	 * @return
	 */
	public void isReasonable(AnalysisType type) {
		String methodSignature = generateMethodSignature(sootMethod);
		if (type.equals(CONSTRAINTS)) {
			String signature = sootMethod.getSignature();
			for (ConstraintReturnRef returnRef : getInvalidReturnReferencesOfSet(constraints, signature)) {
				throw new AnnotationInvalidException(getMsg("exception.constraints.invalid_return_ref", methodSignature, returnRef.toString()));
			}
			if (isVoid) {
				if (containsSetReturnReferenceFor(constraints, signature)) {
					throw new AnnotationInvalidException(getMsg("exception.constraints.void_method", methodSignature));
				}
			} else {
				if (!containsSetReturnReferenceFor(constraints, signature)) {
					throw new AnnotationInvalidException(getMsg("exception.constraints.no_return_ref", methodSignature));
				}
			}
			List<ILevel> containedLevels = getContainedLevelsOfSet(constraints);
			if (!getLevelMediator().checkLevelsValidity(containedLevels)) {
				for (ILevel invalidEffect : getLevelMediator().getInvalidLevels(containedLevels)) {
					throw new LevelInvalidException(getMsg("exception.constraints.method_invalid_level", invalidEffect.getName(), methodSignature));
				}
			}
			for (ConstraintParameterRef paramRef : getInvalidParameterReferencesOfSet(constraints, signature, sootMethod.getParameterCount())) {
				throw new AnnotationInvalidException(getMsg("exception.constraints.invalid_param_ref", methodSignature, paramRef.toString()));
			}
			for (int i = 0; i < sootMethod.getParameterCount(); i++) {
				if (!containsSetParameterReferenceFor(constraints, signature, i)) {
					throw new AnnotationInvalidException(getMsg("exception.constraints.no_param_ref", methodSignature, i));
				}
			}
			if (! containsSetProgramCounterReference(constraints) && ! isClinit) {
				throw new AnnotationInvalidException(getMsg("exception.constraints.method_no_pc_ref", methodSignature));
			}
		} else if (type.equals(LEVELS)) {
			if (!getLevelMediator().checkParameterLevelsValidity(getListOfParameterLevels())) {
				for (ILevel level : getLevelMediator().getInvalidParameterLevels(getListOfParameterLevels())) {
					throw new LevelInvalidException(getMsg("exception.level.parameter.invalid", level.getName(), methodSignature));
				}
			}
			if (isVoid) {
				if (returnLevel != null) {
					throw new AnnotationInvalidException(getMsg("exception.level.return.void_method", methodSignature));
				}
			} else {
				if (returnLevel == null) {
					throw new AnnotationInvalidException(getMsg("exception.level.return.no_level", methodSignature));
				} else {
					if (!getLevelMediator().checkLevelValidity(returnLevel)) {
						throw new LevelInvalidException(getMsg("exception.level.return.invalid", returnLevel.getName(), methodSignature));
					}
				}
			}
			if (!getLevelMediator().checkLevelsValidity(writeEffects)) {
				for (ILevel invalidEffect : getLevelMediator().getInvalidLevels(writeEffects)) {
					throw new AnnotationInvalidException(getMsg("exception.effects.method_invalid", invalidEffect.getName(), methodSignature));
				}
			}
		} else {
			throw new AnalysisTypeException(getMsg("exception.analysis_type.unknown", type.toString()));
		}
		// FIXME: depends on the analysis type in future...
		if (!getLevelMediator().checkLevelsValidity(classWriteEffects)) {
			for (ILevel invalidEffect : getLevelMediator().getInvalidLevels(classWriteEffects)) {
				throw new AnnotationInvalidException(getMsg("exception.effects.method_class_invalid", invalidEffect.getName(), methodSignature));
			}
		}
	}

	/**
	 * Indicates whether this method is a method of the SootSecurity class.
	 * 
	 * @return {@code true} if it is a method of the SootSecurity class, otherwise {@code false}.
	 */
	public boolean isSootSecurityMethod() {
		return isSootSecurityMethod;
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public boolean isVoid() {
		return isVoid;
	}

	/**
	 * Method returns the <em>security levels</em> of parameters of the analyzed method. Note that the returned list is not ordered.
	 * 
	 * @return The <em>security levels</em> of the method parameter of the analyzed method.
	 */
	private List<ILevel> getListOfParameterLevels() {
		List<ILevel> parameterLevels = new ArrayList<ILevel>();
		if (this.methodParameters != null) {
			for (Integer key : this.methodParameters.keySet()) {
				ILevel level = methodParameters.get(key).getLevel();
				if (level != null) {
					parameterLevels.add(level);
				}
			}
		}
		return parameterLevels;
	}

	/**
	 * Logs the given message as a warning. The file name is created by the analyzed {@link SootMethod}, which this environment stores (see
	 * {@link MethodEnvironment#sootMethod}), the source line number is specified as 0.
	 * 
	 * @param msg
	 *          Message that should be printed as a warning.
	 * @see AnalysisLog#warning(String, long, String)
	 */
	protected void logWarning(String msg) {
		getLog().warning(generateFileName(sootMethod), 0, msg);
	}

}

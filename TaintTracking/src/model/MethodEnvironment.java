package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constraints.Constraints;

import logging.AnalysisLog;
import resource.Configuration;
import static resource.Messages.getMsg;
import security.ILevel;
import security.ILevelMediator;
import soot.SootMethod;
import soot.Type;
import utils.AnalysisUtils;

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
	
	private final Constraints contraints;

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
			List<ILevel> methodWriteEffects, List<ILevel> classWriteEffects, Constraints constraints, AnalysisLog log, ILevelMediator mediator) {
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
		this.contraints = constraints;
	}

	/**
	 * Returns the <em>write effects</em> of the class which declares the analyzed method.
	 * 
	 * @return The class <em>write effects</em>.
	 */
	public List<ILevel> getClassWriteEffects() {
		return classWriteEffects;
	}

	/**
	 * Returns the {@link MethodParameter} that represent the parameter of the analyzed method at the given position. Note, that the method
	 * will return a invalid {@link MethodParameter} object (position will be {@code -1}, the level, the name and the {@link Type} will be
	 * {@code null}) if the map {@link MethodEnvironment#methodParameters} does not contain the given index.
	 * 
	 * @param i
	 *          The index of the parameter which should be returned.
	 * @return The method parameter which represent the parameter at the given index.
	 * @throws Exception
	 * @see MethodParameter
	 */
	public MethodParameter getMethodParameterAt(int i) throws Exception {
		if (i < this.methodParameters.size()) {
			return this.methodParameters.get(new Integer(i));
		}
		// TODO: Exception Handling
		throw new Exception();
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

	/*
	 * ----------------------------------------------------
	 */

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
	 * @return
	 */
	public boolean isReasonable() {
		String methodSignature = AnalysisUtils.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
				Configuration.METHOD_SIGNATURE_PRINT_TYPE, Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY);
		boolean reasonability = true;
		if (!getLevelMediator().checkParameterLevelsValidity(getListOfParameterLevels())) {
			for (ILevel level : getLevelMediator().getInvalidParameterLevels(getListOfParameterLevels())) {
				logError(getMsg("parameter_levels.invalid", level.getName(), methodSignature));
			}
			reasonability = false;
		}
		if (isVoid) {
			if (returnLevel != null) {
				logError(getMsg("other.void_method", methodSignature));
				reasonability = false;
			}
			// NEW
			if (contraints.containsReturnRef()) {
				logError(getMsg("other.void_method", methodSignature));
				reasonability = false;
			}
		} else {
			if (returnLevel == null) {
				logError(getMsg("other.non_void_method", methodSignature));
				reasonability = false;
			} else {
				if (!getLevelMediator().checkLevelValidity(returnLevel)) {
					logError(getMsg("other.invalid_return_level", returnLevel.getName(), methodSignature));
					reasonability = false;
				}
			}
		}
		if (!getLevelMediator().checkLevelsValidity(writeEffects)) {
			for (ILevel invalidEffect : getLevelMediator().getInvalidLevels(writeEffects)) {
				logError(getMsg("effects.method.invalid", invalidEffect.getName(), methodSignature));
				reasonability = false;
			}
		}
		if (!getLevelMediator().checkLevelsValidity(classWriteEffects)) {
			for (ILevel invalidEffect : getLevelMediator().getInvalidLevels(classWriteEffects)) {
				logError(getMsg("effects.method.invalid_class", invalidEffect.getName(), methodSignature));
				reasonability = false;
			}
		}
		if (!getLevelMediator().checkLevelsValidity(new ArrayList<ILevel>(contraints.getContainedLevels()))) {
			for (ILevel invalidEffect : getLevelMediator().getInvalidLevels(new ArrayList<ILevel>(contraints.getContainedLevels()))) {
				logError(getMsg("effects.method.invalid_class", invalidEffect.getName(), methodSignature));
				reasonability = false;
			}
		}
		if (sootMethod.getParameterCount() < contraints.highestParameterRefNumber() + 1) {
			logError(getMsg("constraints.invalid_param_ref", methodSignature, sootMethod.getParameterCount()));
			reasonability = false;
		}
		return reasonability;
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
	 * Logs the given message as an error. The file name is created by the analyzed {@link SootMethod}, which this environment stores (see
	 * {@link MethodEnvironment#sootMethod}), the source line number is specified as 0.
	 * 
	 * @param msg
	 *          Message that should be printed as an error.
	 * @see AnalysisLog#error(String, long, String)
	 */
	protected void logError(String msg) {
		getLog().error(AnalysisUtils.generateFileName(sootMethod), 0, msg);
	}

	/**
	 * Logs the given message as well as the Exception as an exception. The file name is created by the analyzed {@link SootMethod}, which
	 * this environment stores (see {@link MethodEnvironment#sootMethod}), the source line number is specified as 0.
	 * 
	 * @param msg
	 *          Message that should be printed as an exception.
	 * @param e
	 *          The exception which is the reason for the given exception message.
	 * @see AnalysisLog#exception(String, long, String, Throwable)
	 */
	protected void logException(String msg, Exception e) {
		getLog().exception(AnalysisUtils.generateFileName(sootMethod), 0, msg, e);
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
		getLog().warning(AnalysisUtils.generateFileName(sootMethod), 0, msg);
	}

	public Constraints getContraints() {
		return contraints;
	}

}

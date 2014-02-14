package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import logging.SecurityLogger;
import resource.Configuration;
import resource.SecurityMessages;
import security.Annotations;
import security.LevelEquation;
import security.LevelEquationVisitor.LevelEquationValidityVistitor;
import security.LevelMediator;
import soot.SootMethod;
import soot.Type;
import utils.SootUtils;
import exception.SootException.InvalidEquationException;
import exception.SootException.InvalidLevelException;

/**
 * <h1>Analysis environment for methods</h1>
 * 
 * The {@link MethodEnvironment} provides a environment for analyzing a
 * {@link SootMethod}. Therefore it extends the base analysis environment
 * {@link Environment} in order to access a logger and the security annotation.
 * The environment provides methods for getting the required annotations at the
 * method as well as at the class which declares the method, and also methods
 * which checks the validity of the levels and effects that are given by those
 * annotations. In addition the environment gives direct access to some methods
 * of the analyzed {@link SootMethod}. This environment handles
 * {@link SootMethod} which will be analyzed only indirectly (e.g. the invoke of
 * a {@link SootMethod} inside of a method body, see {@link MethodEnvironment}).
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
	 * The {@link MethodParameter} represents a single parameter of the analyzed
	 * method and includes the information about the position, the name, the
	 * <em>security level</em> as well as the {@link Type} of the parameter.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class MethodParameter {

		/** The <em>security level</em> of this parameter. */
		private String level;
		/** The name of this parameter. */
		private String name;
		/** The position of this parameter in the parameter list. */
		private int position;
		/** The type of this parameter. */
		private Type type;

		/**
		 * Constructor of a {@link MethodParameter} that represents a parameter
		 * of the analyzed method which includes the position, the name, the
		 * <em>security level</em> as well as the type of the parameter.
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
		public MethodParameter(int position, String name, Type type,
				String level) {
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
		 * The method returns the position of this parameter in the parameter
		 * list.
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
	 * The <em>write effects</em> of the class which declares the
	 * {@link SootMethod}.
	 */
	private List<String> classWriteEffects = new ArrayList<String>();
	/** The expected <em>write effects</em> of the analyzed {@link SootMethod}. */
	private List<String> writeEffects = new ArrayList<String>();
	/**
	 * Map that maps the position of a method parameter to the method parameter
	 * object that contains the name, <em>security level</em>, the {@link Type}
	 * as well as the position (see {@link MethodParameter}).
	 */
	private Map<Integer, MethodParameter> methodParameters = new HashMap<Integer, MethodParameter>();
	/**
	 * The expected return <em>security level</em> of the analyzed
	 * {@link SootMethod}.
	 */
	private String returnLevel = null;
	/** The analyzed method, for which this is the environment. */
	private SootMethod sootMethod;
	/**
	 * DOC
	 */
	private final boolean isIdFunction;
	/**
	 * DOC
	 */
	private final boolean isClinit;
	/**
	 * DOC
	 */
	private final boolean isInit;
	/**
	 * DOC
	 */
	private final boolean isVoid;
	/**
	 * DOC
	 */
	private final boolean isSootSecurityMethod;
	
	@Deprecated
	private LevelEquation returnLevelEquation = null;

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
	public MethodEnvironment(SootMethod sootMethod, boolean isIdFunction,
			boolean isClinit, boolean isInit, boolean isVoid,
			boolean isSootSecurityMethod,
			List<MethodParameter> parameterSecurityLevel,
			String returnSecurityLevel, List<String> methodWriteEffects,
			List<String> classWriteEffects, SecurityLogger log,
			LevelMediator mediator) {

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
	}

	/**
	 * Indicates whether this method is a method of the SootSecurity class.
	 * 
	 * @return {@code true} if it is a method of the SootSecurity class,
	 *         otherwise {@code false}.
	 */
	public boolean isSootSecurityMethod() {
		return isSootSecurityMethod;
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
	 * Indicates whether this method is a clinit method.
	 * 
	 * @return {@code true} if it is a clinit method, otherwise {@code false}.
	 */
	public boolean isClinit() {
		return isClinit;
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
	 * DOC
	 * 
	 * @return
	 */
	public boolean isVoid() {
		return isVoid;
	}
	
	/**
	 * DOC
	 * 
	 * @param sootMethod
	 */
	public void updateSootMethod(SootMethod sootMethod) {
		this.sootMethod = sootMethod;
		for (int i = 0; i < sootMethod.getParameterCount(); i++) {
			methodParameters.get(new Integer(i)).type = sootMethod.getParameterType(i);
		}
	}

	/*
	 * ----------------------------------------------------
	 */

	/**
	 * Returns the <em>write effects</em> of the class which declares the
	 * analyzed method.
	 * 
	 * @return The class <em>write effects</em>.
	 */
	public List<String> getClassWriteEffects() {
		return classWriteEffects;
	}

	/**
	 * Returns the <em>write effects</em> of the analyzed method.
	 * 
	 * @return The method <em>write effects</em>.
	 */
	public List<String> getWriteEffects() {
		return writeEffects;
	}

	/**
	 * Returns the {@link MethodParameter} that represent the parameter of the
	 * analyzed method at the given position. Note, that the method will return
	 * a invalid {@link MethodParameter} object (position will be {@code -1},
	 * the level, the name and the {@link Type} will be {@code null}) if the map
	 * {@link MethodEnvironment#methodParameters} does not contain the given
	 * index.
	 * 
	 * @param i
	 *            The index of the parameter which should be returned.
	 * @return The method parameter which represent the parameter at the given
	 *         index.
	 * @see MethodParameter
	 */
	public MethodParameter getMethodParameterAt(int i) {
		if (i < this.methodParameters.size()) {
			return this.methodParameters.get(new Integer(i));
		}
		// TODO: Exception Handling
		return new MethodParameter(-1, null, null, null);
	}

	/**
	 * Returns a list that contains the method parameter of this analyzed
	 * method. Note, that the position in the returned list does not match the
	 * position of the method declaration.
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
	 * Returns the extracted return <em>security level</em> of the analyzed
	 * method, if it exists. Otherwise the method will return an empty String as
	 * <em>security level</em>.
	 * 
	 * @return The return <em>security level</em> of the analyzed method, if it
	 *         exists.
	 */
	public String getReturnLevel() {
		// TODO: Exception Handling
		return (returnLevel != null) ? returnLevel : "";
	}

	/**
	 * The method returns the analyzed {@link SootMethod} for which this is the
	 * environment.
	 * 
	 * @return The analyzed method.
	 */
	public SootMethod getSootMethod() {
		return sootMethod;
	}

	/**
	 * Indicates whether the analyzed {@link SootMethod} is a library method.
	 * 
	 * @return {@code true} if the method is a library method, otherwise
	 *         {@code false}.
	 */
	public boolean isLibraryMethod() {
		return sootMethod.isJavaLibraryMethod();
	}

	/**
	 * Method returns the <em>security levels</em> of parameters of the analyzed
	 * method. Note that the returned list is not ordered.
	 * 
	 * @return The <em>security levels</em> of the method parameter of the
	 *         analyzed method.
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
	 * Logs the given message as an error. The file name is created by the
	 * analyzed {@link SootMethod}, which this environment stores (see
	 * {@link MethodEnvironment#sootMethod}), the source line number is
	 * specified as 0.
	 * 
	 * @param msg
	 *            Message that should be printed as an error.
	 * @see SecurityLogger#error(String, long, String)
	 */
	protected void logError(String msg) {
		getLog().error(SootUtils.generateFileName(sootMethod), 0, msg);
	}

	/**
	 * Logs the given message as well as the Exception as an exception. The file
	 * name is created by the analyzed {@link SootMethod}, which this
	 * environment stores (see {@link MethodEnvironment#sootMethod}), the source
	 * line number is specified as 0.
	 * 
	 * @param msg
	 *            Message that should be printed as an exception.
	 * @param e
	 *            The exception which is the reason for the given exception
	 *            message.
	 * @see SecurityLogger#exception(String, long, String, Throwable)
	 */
	protected void logException(String msg, Exception e) {
		getLog().exception(SootUtils.generateFileName(sootMethod), 0, msg, e);
	}

	/**
	 * Logs the given message as a warning. The file name is created by the
	 * analyzed {@link SootMethod}, which this environment stores (see
	 * {@link MethodEnvironment#sootMethod}), the source line number is
	 * specified as 0.
	 * 
	 * @param msg
	 *            Message that should be printed as a warning.
	 * @see SecurityLogger#warning(String, long, String)
	 */
	protected void logWarning(String msg) {
		getLog().warning(SootUtils.generateFileName(sootMethod), 0, msg);
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public boolean isReasonable() {
		String methodSignature = SootUtils.generateMethodSignature(sootMethod,
				Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
				Configuration.METHOD_SIGNATURE_PRINT_TYPE,
				Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY);
		boolean reasonability = true;
		if (!getLevelMediator().checkValidityOfParameterLevels(
				getListOfParameterLevels())) {
			for (String level : getLevelMediator()
					.getInvalidParameterLevels(getListOfParameterLevels())) {
				logError(SecurityMessages.invalidParameterLevel(
						methodSignature, level));
			}
			reasonability = false;
		}
		if (isVoid) {
			if (!returnLevel.equals(LevelMediator.VOID_LEVEL)) {
				// if (returnLevel != null)
				// TODO: Logging (Void but has a return security level)
				reasonability = false;
			}
		} else {
			if (returnLevel == null) {
				// TODO: Logging (Method has no return security level)
				reasonability = false;
			}
			try {
				LevelEquationValidityVistitor visitor = getLevelMediator()
						.checkValidityOfReturnLevel(returnLevel,
								getListOfParameterLevels());
				if (visitor.isValid()) {
					if (visitor.isVoidInvalid()) {
						logError(SecurityMessages.incompatibaleParameterLevels(
								methodSignature, LevelMediator.VOID_LEVEL));
						reasonability = false;
					} else {
						this.returnLevelEquation = visitor.getLevelEquation();
					}
				} else {
					List<String> invalidLevel = visitor.getValidLevels();
					if (invalidLevel.size() != 0) {
						for (String level : invalidLevel) {
							logError(SecurityMessages.invalidReturnLevel(
									methodSignature, level));
						}
					} else {
						logError(SecurityMessages
								.invalidReturnEquation(methodSignature));
					}
					reasonability = false;
				}
			} catch (InvalidLevelException | InvalidEquationException e) {
				logException(
						SecurityMessages.invalidReturnEquation(methodSignature),
						e);
				reasonability = false;
			}
		}
		if (!getLevelMediator().checkValidityOfLevels(writeEffects)) {
			for (String invalidEffect : getLevelMediator()
					.getInvalidLevels(writeEffects)) {
				logError(SecurityMessages.invalidWriteEffect(methodSignature,
						invalidEffect));
			}
		}
		return reasonability;
	}

}

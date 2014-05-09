package extractor;

import static main.AnalysisType.CONSTRAINTS;
import static main.AnalysisType.LEVELS;
import static resource.Messages.getMsg;
import static utils.AnalysisUtils.containsStaticInitializer;
import static utils.AnalysisUtils.getSignatureOfClass;
import static utils.AnalysisUtils.getSignatureOfField;
import static utils.AnalysisUtils.getSignatureOfMethod;
import static utils.AnalysisUtils.generatedEmptyStaticInitializer;
import static utils.AnalysisUtils.getOverridenMethods;
import static utils.AnalysisUtils.getParameterNames;
import static utils.AnalysisUtils.isClinitMethod;
import static utils.AnalysisUtils.isDefinitionClass;
import static utils.AnalysisUtils.isInitMethod;
import static utils.AnalysisUtils.isInnerClassOfDefinitionClass;
import static utils.AnalysisUtils.isLevelFunction;
import static utils.AnalysisUtils.isMethodOfDefinitionClass;
import static utils.AnalysisUtils.isVoidMethod;
import static utils.AnalysisUtils.overridesMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logging.AnalysisLog;
import main.AnalysisType;
import model.AnalyzedMethodEnvironment;
import model.ClassEnvironment;
import model.FieldEnvironment;
import model.MethodEnvironment;
import model.MethodEnvironment.MethodParameter;
import security.ILevel;
import security.ILevelMediator;
import soot.Body;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import constraints.ConstraintParameterRef;
import constraints.ConstraintProgramCounterRef;
import constraints.ConstraintReturnRef;
import constraints.IConstraint;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;
import exception.AnalysisTypeException;
import exception.AnnotationExtractionException;
import exception.AnnotationInvalidException;
import exception.ExtractorException;
import exception.LevelInvalidException;
import exception.SwitchException;

/**
 * <h1>Annotation Transformer</h1>
 * 
 * The class {@link AnnotationExtractor} acts on a {@link Body}. This class will be used to calculate all {@link MethodEnvironment},
 * {@link FieldEnvironment} and {@link AnalyzedMethodEnvironment}s. I.e. the Transformer stores for every Method in the Body a
 * {@link MethodEnvironment} and also for Field a {@link FieldEnvironment}. For every directly processed Method an
 * {@link AnalyzedMethodEnvironment} will be stored. If an error occurs during the transformation this will be indicated by a flag.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class AnnotationExtractor extends SceneTransformer {

	/**
	 * DOC
	 */
	private final AnalysisLog log;
	/**
	 * DOC
	 */
	private final ILevelMediator mediator;
	/**
	 * DOC
	 */
	private final AnnotationStmtSwitch stmtSwitch;
	/**
	 * DOC
	 */
	private final UsedObjectStore store = new UsedObjectStore();
	/**
	 * DOC
	 */
	private final AnalysisType type;

	/**
	 * DOC
	 * 
	 * @param log
	 * @param mediator
	 * @param type
	 */
	public AnnotationExtractor(AnalysisLog log, ILevelMediator mediator, AnalysisType type) {
		super();
		this.log = log;
		this.mediator = mediator;
		this.type = type;
		this.stmtSwitch = new AnnotationStmtSwitch(this);
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public void checkReasonability() {
			for (SootClass sootClass : store.getClasses()) {
				ClassEnvironment ce = store.getClassEnvironment(sootClass);
				if (!ce.isLibrary()) {
					try {
						ce.isReasonable(type);
					} catch (AnnotationInvalidException | LevelInvalidException e) {
						String signature = getSignatureOfClass(sootClass);
						throw new ExtractorException(getMsg("exception.extractor.other.class_not_reasonable", signature), e);
					}
					
				}
			}
			for (SootField sootField : store.getFields()) {
				FieldEnvironment fe = store.getFieldEnvironment(sootField);
				if (!fe.isLibraryClass()) {
					try {
						fe.isReasonable(type);
					} catch (AnnotationInvalidException | LevelInvalidException e) {
						String signature = getSignatureOfField(sootField);
						throw new ExtractorException(getMsg("exception.extractor.other.field_not_reasonable", signature), e);
					}
					
				}
			}
			for (SootMethod sootMethod : store.getMethods()) {
				MethodEnvironment me = store.getMethodEnvironment(sootMethod);
				if (!me.isLibraryMethod()) {
					try {
						me.isReasonable(type);
					} catch (AnnotationInvalidException | LevelInvalidException e) {
						String signature = getSignatureOfMethod(sootMethod);
						throw new ExtractorException(getMsg("exception.extractor.other.method_not_reasonable", signature), e);
					}	
					
				}
			}
 	
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public UsedObjectStore getUsedObjectStore() {
		return store;
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @param overriden
	 */
	private void addMethodEnvironmentForMethod(SootMethod sootMethod, boolean overriden) {
		if (!store.containsMethod(sootMethod)) {
			MethodEnvironment me = checkAndBuildMethodEnvironment(sootMethod);
			store.addMethodEnvironment(me);
		}
		if (overriden && overridesMethod(sootMethod)) {
			List<SootMethod> methods = getOverridenMethods(sootMethod);
			if (methods.size() >= 1) {
				addMethodEnvironmentForMethod(methods.get(0), false);
			}
		}
	}

	/**
	 * DOC
	 * 
	 * @param sootClass
	 * @return
	 */
	private ClassEnvironment checkAndBuildClassEnvironment(SootClass sootClass) {
		boolean isLibrary = sootClass.isJavaLibraryClass();
		boolean hasClassWriteEffectAnnotation = mediator.hasClassWriteEffectAnnotation(sootClass);
		boolean hasConstraintsAnnotation = mediator.hasConstraintsAnnotation(sootClass);
		List<ILevel> classWriteEffects = new ArrayList<ILevel>();
		Set<IConstraint> constraints = generateInitalClassConstraintsSignature(sootClass);
		if (type.equals(CONSTRAINTS)) {
			if (! isLibrary) {
				if (hasConstraintsAnnotation) {					
					try {
						constraints.addAll(mediator.extractConstraints(sootClass));
					} catch (AnnotationExtractionException e) {
						throw new ExtractorException(getMsg("exception.extractor.class_constraints.error", getSignatureOfClass(sootClass)), e);
					}
				} 
//				else {
//					throw new ExtractorException(getMsg("exception.extractor.class_constraints.no_constraints", generateClassSignature(sootClass)));
//				}
			} else {
				constraints.addAll(mediator.getLibraryConstraints(sootClass));
			}
		} else if (type.equals(LEVELS)) {
			if (!isLibrary) {
				if (hasClassWriteEffectAnnotation) {
					try {
						classWriteEffects.addAll(mediator.extractClassEffects(sootClass));
					} catch (AnnotationExtractionException e) {
						throw new ExtractorException(getMsg("exception.extractor.effects.error_class", getSignatureOfClass(sootClass)), e);
					}
				}
			} else {
				classWriteEffects.addAll(mediator.getLibraryClassWriteEffects(sootClass));
			}
		} else {
			throw new AnalysisTypeException(getMsg("exception.analysis_type.unknown", type.toString()));
		}
		ClassEnvironment ce = new ClassEnvironment(sootClass, classWriteEffects, constraints, log, mediator);
		return ce;
	}


	/**
	 * DOC
	 * 
	 * @param sootField
	 * @return
	 */
	private FieldEnvironment checkAndBuildFieldEnvironment(SootField sootField) {
		SootClass declaringClass = sootField.getDeclaringClass();
		boolean isLibrary = declaringClass.isJavaLibraryClass();
		ILevel fieldSecurityLevel = null;
		if (type.equals(LEVELS) || type.equals(CONSTRAINTS)) {
			boolean hasFieldSecurityAnnotation = mediator.hasFieldSecurityAnnotation(sootField);
			if (!isLibrary) {
				if (hasFieldSecurityAnnotation) {
					try {
						fieldSecurityLevel = mediator.extractFieldSecurityLevel(sootField);
					} catch (AnnotationExtractionException e) {
						throw new ExtractorException(getMsg("exception.extractor.level.field.error", getSignatureOfField(sootField)), e);
					}
				} else {
					throw new ExtractorException(getMsg("exception.extractor.level.field.no_level", getSignatureOfField(sootField)));
				}
			} else {
				fieldSecurityLevel = mediator.getLibraryFieldSecurityLevel(sootField);
			}
		} else {
			throw new AnalysisTypeException(getMsg("exception.analysis_type.unknown", type.toString()));
		}
		List<ILevel> classWriteEffects = new ArrayList<ILevel>();
		addClassEnvironmentForClass(declaringClass);
		ClassEnvironment ce = store.getClassEnvironment(declaringClass);
		classWriteEffects.addAll(ce.getWriteEffects());
		FieldEnvironment fe = new FieldEnvironment(sootField, fieldSecurityLevel, classWriteEffects, log, mediator);
		return fe;
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @return
	 */
	private MethodEnvironment checkAndBuildMethodEnvironment(SootMethod sootMethod) {
		SootClass declaringClass = sootMethod.getDeclaringClass();
		boolean isLibrary = sootMethod.isJavaLibraryMethod();
		boolean isIdFunction = isLevelFunction(sootMethod, mediator.getAvailableLevels());
		boolean isClinit = isClinitMethod(sootMethod);
		boolean isInit = isInitMethod(sootMethod);
		boolean isVoid = isVoidMethod(sootMethod);
		boolean isSootSecurityMethod = isMethodOfDefinitionClass(sootMethod);
		ILevel returnSecurityLevel = null;
		int parameterCount = sootMethod.getParameterCount();
		List<MethodParameter> parameterSecurityLevel = new ArrayList<MethodParameter>();
		List<ILevel> methodWriteEffects = new ArrayList<ILevel>();
		List<ILevel> classWriteEffects = new ArrayList<ILevel>();
		Set<IConstraint> constraints = generateInitialMethodConstraintsSignature(sootMethod, isVoid);
		if (type.equals(CONSTRAINTS)) {
			if (!isLibrary) {
				boolean hasConstraintsAnnotation = mediator.hasConstraintsAnnotation(sootMethod);
				if (hasConstraintsAnnotation) {
					try {
						constraints.addAll(mediator.extractConstraints(sootMethod));
					} catch (AnnotationExtractionException e) {
						throw new ExtractorException(getMsg("exception.extractor.method_constraints.error", getSignatureOfMethod(sootMethod)), e);
					}
				} 
//				else if (!(isClinit || (isInit && parameterCount == 0) || (isVoid && parameterCount == 0))) {
//					throw new ExtractorException(getMsg("exception.extractor.method_constraints.no_constraints", generateMethodSignature(sootMethod)));
//				}
			} else {
				constraints.addAll(mediator.getLibraryConstraints(sootMethod));
			}
		} else if (type.equals(LEVELS)) {
			if (!isLibrary) {
				boolean hasReturnSecurityAnnotation = mediator.hasReturnSecurityAnnotation(sootMethod);
				boolean hasParameterSecurityAnnotation = mediator.hasParameterSecurityAnnotation(sootMethod);
				boolean hasMethodWriteEffectAnnotation = mediator.hasMethodWriteEffectAnnotation(sootMethod);
				if (!isVoid) {
					if (hasReturnSecurityAnnotation) {
						try {
							returnSecurityLevel = mediator.extractReturnSecurityLevel(sootMethod);
						} catch (AnnotationExtractionException e) {
							throw new ExtractorException(getMsg("exception.extractor.level.return.error", getSignatureOfMethod(sootMethod)), e);
						}
					} else {
						throw new ExtractorException(getMsg("exception.extractor.level.return.no_level", getSignatureOfMethod(sootMethod)));
					}
				} else {
					if (hasReturnSecurityAnnotation) {
						throw new ExtractorException(getMsg("exception.extractor.level.return.void", getSignatureOfMethod(sootMethod)));
					}
				}
				if (parameterCount != 0) {
					if (hasParameterSecurityAnnotation) {
						List<ILevel> parameterLevels = new ArrayList<ILevel>();
						try {
							parameterLevels.addAll(mediator.extractParameterSecurityLevels(sootMethod));
						} catch (AnnotationExtractionException e) {
							throw new ExtractorException(getMsg("exception.extractor.level.parameter.error", getSignatureOfMethod(sootMethod)), e);
						}
						if (parameterLevels.size() == parameterCount) {
							List<String> names = getParameterNames(sootMethod);
							for (int i = 0; i < parameterLevels.size(); i++) {
								Type type = sootMethod.getParameterType(i);
								ILevel level = parameterLevels.get(i);
								String name = (parameterCount == names.size()) ? names.get(i) : "arg" + (i + 1);
								MethodParameter mp = new MethodParameter(i, name, type, level);
								parameterSecurityLevel.add(mp);
							}
						} else {
							throw new ExtractorException(getMsg("exception.extractor.level.parameter.invalid", getSignatureOfMethod(sootMethod)));
						}
					} else {
						throw new ExtractorException(getMsg("exception.extractor.level.parameter.no_level", getSignatureOfMethod(sootMethod)));
					}
				}
				if (hasMethodWriteEffectAnnotation) {
					try {
						methodWriteEffects.addAll(mediator.extractMethodEffects(sootMethod));
					} catch (AnnotationExtractionException e) {
						throw new ExtractorException(getMsg("exception.extractor.effects.error_method", getSignatureOfMethod(sootMethod)), e);
					}
				}
			} else {
				List<ILevel> parameterLevels = new ArrayList<ILevel>();
				parameterLevels.addAll(mediator.getLibraryParameterSecurityLevel(sootMethod));
				List<String> names = getParameterNames(sootMethod);
				for (int i = 0; i < parameterLevels.size(); i++) {
					Type type = sootMethod.getParameterType(i);
					ILevel level = parameterLevels.get(i);
					String name = (parameterCount == names.size()) ? names.get(i) : "arg" + (i + 1);
					MethodParameter mp = new MethodParameter(i, name, type, level);
					parameterSecurityLevel.add(mp);
				}
				if (!isVoid) {
					returnSecurityLevel = mediator.getLibraryReturnSecurityLevel(sootMethod, parameterLevels);
				}
				methodWriteEffects.addAll(mediator.getLibraryWriteEffects(sootMethod));
			}
		} else {
			throw new AnalysisTypeException(getMsg("exception.analysis_type.unknown", type.toString()));
		}
		addClassEnvironmentForClass(declaringClass);
		ClassEnvironment ce = store.getClassEnvironment(declaringClass);
		classWriteEffects.addAll(ce.getWriteEffects());
		for (SootClass exceptions : sootMethod.getExceptions()) {
			addClassEnvironmentForClass(exceptions);
		}
		MethodEnvironment methodEnvironment = new MethodEnvironment(sootMethod, isIdFunction, isClinit, isInit, isVoid, isSootSecurityMethod,
				parameterSecurityLevel, returnSecurityLevel, methodWriteEffects, classWriteEffects, constraints, log, mediator);
		return methodEnvironment;
	}

	private Set<IConstraint> generateInitialMethodConstraintsSignature(SootMethod sootMethod, boolean isVoid) {
		Set<IConstraint> constraints = new HashSet<IConstraint>();
		addBoundsFor(constraints, new ConstraintProgramCounterRef(sootMethod.getSignature()));
		for (int i = 0; i < sootMethod.getParameterCount(); i++) {
			addBoundsFor(constraints, new ConstraintParameterRef(i, sootMethod.getSignature()));
		}
		if (!isVoid) {
			addBoundsFor(constraints, new ConstraintReturnRef(sootMethod.getSignature()));
		}
		return constraints;
	}

	private void addBoundsFor(Set<IConstraint> constraints, IConstraintComponent component) {
		constraints.add(new LEQConstraint(component, mediator.getLeastUpperBoundLevel()));
		constraints.add(new LEQConstraint(mediator.getGreatestLowerBoundLevel(), component));
	}

	private Set<IConstraint> generateInitalClassConstraintsSignature(SootClass sootClass) {
		Set<IConstraint> constraints = new HashSet<IConstraint>();
		addBoundsFor(constraints, new ConstraintProgramCounterRef(sootClass.getName()));
		return constraints;
	}
	
	/**
	 * DOC
	 * 
	 * @param sootClass
	 */
	private void doExtraction(SootClass sootClass) {
		if (!isInnerClassOfDefinitionClass(sootClass)) {
			addClassEnvironmentForClass(sootClass);
			if (!isDefinitionClass(sootClass)) {
				for (SootField sootField : sootClass.getFields()) {
					addFieldEvironmentForField(sootField);
				}
			}
			if (!containsStaticInitializer(sootClass.getMethods())) {
				generatedEmptyStaticInitializer(sootClass);
			}
			for (SootMethod sootMethod : sootClass.getMethods()) {
				if ((!isMethodOfDefinitionClass(sootMethod)) || isLevelFunction(sootMethod, mediator.getAvailableLevels())) {
					UnitGraph graph = new BriefUnitGraph(sootMethod.retrieveActiveBody());
					sootMethod = graph.getBody().getMethod();
					addMethodEnvironmentForMethod(sootMethod);
					if (sootMethod.hasActiveBody()) {
						for (Unit unit : sootMethod.getActiveBody().getUnits()) {
							try {
								unit.apply(stmtSwitch);
							} catch (SwitchException e) {
								throw new ExtractorException(getMsg("exception.extractor.other.error_switch", unit.toString(),
										getSignatureOfMethod(sootMethod)), e);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * DOC
	 * 
	 * @param sootClass
	 */
	protected void addClassEnvironmentForClass(SootClass sootClass) {
		if (!store.containsClass(sootClass)) {
			ClassEnvironment ce = checkAndBuildClassEnvironment(sootClass);
			store.addClassEnvironment(ce);
		}
	}

	/**
	 * DOC
	 * 
	 * @param sootField
	 */
	protected void addFieldEvironmentForField(SootField sootField) {
		if (!store.containsField(sootField)) {
			FieldEnvironment fe = checkAndBuildFieldEnvironment(sootField);
			store.addFieldEnvironment(fe);
		}
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 */
	protected void addMethodEnvironmentForMethod(SootMethod sootMethod) {
		addMethodEnvironmentForMethod(sootMethod, true);

	}

	/**
	 * DOC
	 * 
	 * @param phaseName
	 * @param options
	 * 
	 * @see soot.SceneTransformer#internalTransform(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	protected void internalTransform(String phaseName, Map options) {
		Chain<SootClass> classes = Scene.v().getApplicationClasses();
		for (SootClass sootClass : classes) {
			doExtraction(sootClass);
		}
	}

}

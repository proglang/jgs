package extractor;

import static resource.Messages.getMsg;
import static utils.AnalysisUtils.containsStaticInitializer;
import static utils.AnalysisUtils.generateClassSignature;
import static utils.AnalysisUtils.generateFieldSignature;
import static utils.AnalysisUtils.generateMethodSignature;
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
import java.util.List;
import java.util.Map;

import logging.AnalysisLog;
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
import constraints.Constraints;
import exception.AnnotationExtractionException;
import exception.ExtractorException;
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
	 * 
	 * @param log
	 * @param mediator
	 */
	public AnnotationExtractor(AnalysisLog log, ILevelMediator mediator) {
		super();
		this.log = log;
		this.mediator = mediator;
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
				ce.isReasonable();
			}
		}
		for (SootField sootField : store.getFields()) {
			FieldEnvironment fe = store.getFieldEnvironment(sootField);
			if (!fe.isLibraryClass()) {
				fe.isReasonable();
			}
		}
		for (SootMethod sootMethod : store.getMethods()) {
			MethodEnvironment me = store.getMethodEnvironment(sootMethod);
			if (!me.isLibraryMethod()) {
				me.isReasonable();
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
		List<ILevel> classWriteEffects = new ArrayList<ILevel>();
		if (!isLibrary) {
			if (hasClassWriteEffectAnnotation) {
				try {
					classWriteEffects.addAll(mediator.extractClassEffects(sootClass));
				} catch (AnnotationExtractionException e) {
					throw new ExtractorException(getMsg("exception.extractor.effects.error_class", generateClassSignature(sootClass)), e);
				}
			}
		} else {
			classWriteEffects.addAll(mediator.getLibraryClassWriteEffects(sootClass));
		}
		ClassEnvironment ce = new ClassEnvironment(sootClass, classWriteEffects, log, mediator);
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
		boolean hasFieldSecurityAnnotation = mediator.hasFieldSecurityAnnotation(sootField);
		if (!isLibrary) {
			if (hasFieldSecurityAnnotation) {
				try {
					fieldSecurityLevel = mediator.extractFieldSecurityLevel(sootField);
				} catch (AnnotationExtractionException e) {
					throw new ExtractorException(getMsg("exception.extractor.level.field.error", generateFieldSignature(sootField)), e);
				}
			} else {
				throw new ExtractorException(getMsg("exception.extractor.level.field.no_level", generateFieldSignature(sootField)));
			}
		} else {
			fieldSecurityLevel = mediator.getLibraryFieldSecurityLevel(sootField);
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
		Constraints constraints = new Constraints();
		if (!isLibrary) {
			boolean hasReturnSecurityAnnotation = mediator.hasReturnSecurityAnnotation(sootMethod);
			boolean hasParameterSecurityAnnotation = mediator.hasParameterSecurityAnnotation(sootMethod);
			boolean hasMethodWriteEffectAnnotation = mediator.hasMethodWriteEffectAnnotation(sootMethod);
			boolean hasConstraintsAnnotation = mediator.hasConstraintsAnnotation(sootMethod);
			if (!isVoid) {
				if (hasReturnSecurityAnnotation) {
					try {
						returnSecurityLevel = mediator.extractReturnSecurityLevel(sootMethod);
					} catch (AnnotationExtractionException e) {
						throw new ExtractorException(getMsg("exception.extractor.level.return.error", generateMethodSignature(sootMethod)), e);
					}
				} else {
					throw new ExtractorException(getMsg("exception.extractor.level.return.no_level", generateMethodSignature(sootMethod)));
				}
			} else {
				if (hasReturnSecurityAnnotation) {
					throw new ExtractorException(getMsg("exception.extractor.level.return.void", generateMethodSignature(sootMethod)));
				}
			}
			if (hasMethodWriteEffectAnnotation) {
				try {
					methodWriteEffects.addAll(mediator.extractMethodEffects(sootMethod));
				} catch (AnnotationExtractionException e) {
					throw new ExtractorException(getMsg("exception.extractor.effects.error_method", generateMethodSignature(sootMethod)), e);
				}
			}
			if (parameterCount != 0) {
				if (hasParameterSecurityAnnotation) {
					List<ILevel> parameterLevels = new ArrayList<ILevel>();
					try {
						parameterLevels.addAll(mediator.extractParameterSecurityLevels(sootMethod));
					} catch (AnnotationExtractionException e) {
						throw new ExtractorException(getMsg("exception.extractor.level.parameter.error", generateMethodSignature(sootMethod)), e);
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
						throw new ExtractorException(getMsg("exception.extractor.level.parameter.invalid", generateMethodSignature(sootMethod)));
					}
				} else {
					throw new ExtractorException(getMsg("exception.extractor.level.parameter.no_level", generateMethodSignature(sootMethod)));
				}
			}
			if (hasConstraintsAnnotation) {
				try {
					constraints = mediator.extractConstraints(sootMethod);
				} catch (AnnotationExtractionException e) {
					throw new ExtractorException(getMsg("exception.extractor.constraints.error", generateMethodSignature(sootMethod)), e);
				}
			} else {
				// FIXME: Is the constraints annotation mandatory???
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
			constraints = mediator.getLibraryConstraints(sootMethod);
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
										generateMethodSignature(sootMethod)), e);
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

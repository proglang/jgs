package extractor;

import interfaces.Cancelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logging.AnalysisLog;
import model.AnalyzedMethodEnvironment;
import model.ClassEnvironment;
import model.FieldEnvironment;
import model.MethodEnvironment;
import model.MethodEnvironment.MethodParameter;
import resource.Configuration;
import static resource.Messages.getMsg;
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
import utils.AnalysisUtils;
import exception.SootException.ExtractionException;
import exception.SootException.NoSuchElementException;

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
public class AnnotationExtractor extends SceneTransformer implements Cancelable {

	/**
	 * DOC
	 */
	private boolean annotationValidity = true;

	/**
	 * DOC
	 */
	private final List<Cancelable> listeners = new ArrayList<Cancelable>();

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

	@Override
	public void addCancelListener(Cancelable cancelable) {
		this.listeners.add(cancelable);

	}

	@Override
	public void cancel() {
		for (Cancelable cancelable : listeners) {
			cancelable.cancel();
		}
		this.annotationValidity = false;
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public boolean checkReasonability() {
		if (annotationValidity) {
			for (SootClass sootClass : store.getClasses()) {
				ClassEnvironment ce = store.getClassEnvironment(sootClass);
				if (!ce.isLibrary()) {
					annotationValidity = annotationValidity && ce.isReasonable();
				}
			}
		}
		if (annotationValidity) {
			for (SootField sootField : store.getFields()) {
				FieldEnvironment fe = store.getFieldEnvironment(sootField);
				if (!fe.isLibraryClass()) {
					annotationValidity = annotationValidity && fe.isReasonable();
				}
			}
		}
		if (annotationValidity) {
			for (SootMethod sootMethod : store.getMethods()) {
				MethodEnvironment me = store.getMethodEnvironment(sootMethod);
				if (!me.isLibraryMethod()) {
					annotationValidity = annotationValidity && me.isReasonable();
				}
			}
		}
		return annotationValidity;
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public UsedObjectStore getUsedObjectStore() {
		return store;
	}

	@Override
	public void removeCancelListener(Cancelable cancelable) {
		this.listeners.remove(cancelable);

	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @param overriden
	 */
	private void addMethodEnvironmentForMethod(SootMethod sootMethod, boolean overriden) {
		if (annotationValidity) {
			if (!store.containsMethod(sootMethod)) {
				MethodEnvironment me = checkAndBuildMethodEnvironment(sootMethod);
				store.addMethodEnvironment(me);
			}
			if (overriden && AnalysisUtils.overridesMethod(sootMethod)) {
				List<SootMethod> methods = AnalysisUtils.getOverridenMethods(sootMethod);
				if (methods.size() >= 1) {
					addMethodEnvironmentForMethod(methods.get(0), false);
				}
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
				} catch (Exception e) {
					log.error(
							AnalysisUtils.generateFileName(sootClass),
							0,
							getMsg("extractor.effects.error_class_extraction",
									AnalysisUtils.generateClassSignature(sootClass, Configuration.CLASS_SIGNATURE_PRINT_PACKAGE)));
					annotationValidity = false;
				}
			}
		} else {
			try {
				classWriteEffects.addAll(mediator.getLibraryClassWriteEffects(sootClass));
			} catch (Exception e) {

				log.error(
						AnalysisUtils.generateFileName(sootClass),
						0,
						getMsg("extractor.effects.error_lib_class_lookup",
								AnalysisUtils.generateClassSignature(sootClass, Configuration.CLASS_SIGNATURE_PRINT_PACKAGE)));
				annotationValidity = false;
			}
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
				} catch (ExtractionException e) {
					log.error(AnalysisUtils.generateFileName(declaringClass), 0, getMsg("extractor.field_level.error_extraction", AnalysisUtils
							.generateFieldSignature(sootField, Configuration.FIELD_SIGNATURE_PRINT_PACKAGE, Configuration.FIELD_SIGNATURE_PRINT_TYPE,
									Configuration.FIELD_SIGNATURE_PRINT_VISIBILITY)));
					annotationValidity = false;
				}
			} else {
				log.error(AnalysisUtils.generateFileName(declaringClass), 0, getMsg("extractor.field_level.error_no_level", AnalysisUtils
						.generateFieldSignature(sootField, Configuration.FIELD_SIGNATURE_PRINT_PACKAGE, Configuration.FIELD_SIGNATURE_PRINT_TYPE,
								Configuration.FIELD_SIGNATURE_PRINT_VISIBILITY)));
				annotationValidity = false;
			}
		} else {
			try {
				fieldSecurityLevel = mediator.getLibraryFieldSecurityLevel(sootField);
			} catch (Exception e) {
				log.error(AnalysisUtils.generateFileName(declaringClass), 0, getMsg("extractor.field_level.error_lib_lookup", AnalysisUtils
						.generateFieldSignature(sootField, Configuration.FIELD_SIGNATURE_PRINT_PACKAGE, Configuration.FIELD_SIGNATURE_PRINT_TYPE,
								Configuration.FIELD_SIGNATURE_PRINT_VISIBILITY)));
				annotationValidity = false;
			}
		}
		List<ILevel> classWriteEffects = new ArrayList<ILevel>();
		addClassEnvironmentForClass(declaringClass);
		if (annotationValidity) {
			try {
				ClassEnvironment ce = store.getClassEnvironment(declaringClass);
				classWriteEffects.addAll(ce.getWriteEffects());
			} catch (NoSuchElementException e) {
				log.exception(
						AnalysisUtils.generateFileName(declaringClass),
						0,
						getMsg("env_store.not_found_class",
								AnalysisUtils.generateClassSignature(declaringClass, Configuration.CLASS_SIGNATURE_PRINT_PACKAGE)), e);
				annotationValidity = false;
			}
		}
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
		boolean isIdFunction = AnalysisUtils.isIdFunction(sootMethod, mediator.getAvailableLevels());
		boolean isClinit = AnalysisUtils.isClinitMethod(sootMethod);
		boolean isInit = AnalysisUtils.isInitMethod(sootMethod);
		boolean isVoid = AnalysisUtils.isVoidMethod(sootMethod);
		boolean isSootSecurityMethod = AnalysisUtils.isMethodOfDefinitionClass(sootMethod);
		ILevel returnSecurityLevel = null;
		int parameterCount = sootMethod.getParameterCount();
		List<MethodParameter> parameterSecurityLevel = new ArrayList<MethodParameter>();
		List<ILevel> methodWriteEffects = new ArrayList<ILevel>();
		List<ILevel> classWriteEffects = new ArrayList<ILevel>();
		if (!isLibrary) {
			boolean hasReturnSecurityAnnotation = mediator.hasReturnSecurityAnnotation(sootMethod);
			boolean hasParameterSecurityAnnotation = mediator.hasParameterSecurityAnnotation(sootMethod);
			boolean hasMethodWriteEffectAnnotation = mediator.hasMethodWriteEffectAnnotation(sootMethod);
			if (!isVoid) {
				if (hasReturnSecurityAnnotation) {
					try {
						returnSecurityLevel = mediator.extractReturnSecurityLevel(sootMethod);
					} catch (ExtractionException e) {
						log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.return_level.error_extraction", AnalysisUtils
								.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
										Configuration.METHOD_SIGNATURE_PRINT_TYPE, Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
						annotationValidity = false;
					}
				} else {
					log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.return_level.error_no_level", AnalysisUtils
							.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
									Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
					annotationValidity = false;
				}
			} else {
				if (hasReturnSecurityAnnotation) {
					log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.return_level.error_void_level", AnalysisUtils
							.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
									Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
					annotationValidity = false;
				}
			}
			if (hasMethodWriteEffectAnnotation) {
				try {
					methodWriteEffects.addAll(mediator.extractMethodEffects(sootMethod));
				} catch (ExtractionException e) {
					log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.effects.error_method_extraction", AnalysisUtils
							.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
									Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
					annotationValidity = false;
				}
			}
			if (parameterCount != 0) {
				if (hasParameterSecurityAnnotation) {
					List<ILevel> parameterLevels = new ArrayList<ILevel>();
					try {
						parameterLevels.addAll(mediator.extractParameterSecurityLevels(sootMethod));
					} catch (ExtractionException e) {
						log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.parameter_levels.error_extraction",
								AnalysisUtils.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
										Configuration.METHOD_SIGNATURE_PRINT_TYPE, Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
						annotationValidity = false;
					}
					if (parameterLevels.size() == parameterCount) {
						List<String> names = AnalysisUtils.getParameterNames(sootMethod);
						for (int i = 0; i < parameterLevels.size(); i++) {
							Type type = sootMethod.getParameterType(i);
							ILevel level = parameterLevels.get(i);
							String name = (parameterCount == names.size()) ? names.get(i) : "arg" + (i + 1);
							MethodParameter mp = new MethodParameter(i, name, type, level);
							parameterSecurityLevel.add(mp);
						}
					} else {
						log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.parameter_levels.error_unequal_count",
								AnalysisUtils.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
										Configuration.METHOD_SIGNATURE_PRINT_TYPE, Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
						annotationValidity = false;
					}
				} else {
					log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.parameter_levels.error_no_level", AnalysisUtils
							.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
									Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
					annotationValidity = false;
				}
			}
		} else {

			List<ILevel> parameterLevels = new ArrayList<ILevel>();
			try {
				parameterLevels.addAll(mediator.getLibraryParameterSecurityLevel(sootMethod));
			} catch (Exception e) {
				log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.parameter_levels.error_lib_lookup", AnalysisUtils
						.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
								Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
				annotationValidity = false;
			}
			if (parameterLevels.size() == parameterCount) {
				List<String> names = AnalysisUtils.getParameterNames(sootMethod);
				for (int i = 0; i < parameterLevels.size(); i++) {
					Type type = sootMethod.getParameterType(i);
					ILevel level = parameterLevels.get(i);
					String name = (parameterCount == names.size()) ? names.get(i) : "arg" + (i + 1);
					MethodParameter mp = new MethodParameter(i, name, type, level);
					parameterSecurityLevel.add(mp);
				}
			} else {
				log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.parameter_levels.error_unequal_count",
						AnalysisUtils.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
								Configuration.METHOD_SIGNATURE_PRINT_TYPE, Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
				annotationValidity = false;
			}

			if (!isVoid) {
				try {
					returnSecurityLevel = mediator.getLibraryReturnSecurityLevel(sootMethod, parameterLevels);
				} catch (Exception e) {
					log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.return_level.error_lib_lookup", AnalysisUtils
							.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
									Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
					annotationValidity = false;
				}
			}
			try {
				methodWriteEffects.addAll(mediator.getLibraryWriteEffects(sootMethod));
			} catch (Exception e) {
				log.error(AnalysisUtils.generateFileName(sootMethod), 0, getMsg("extractor.effects.error_lib_method_lookup", AnalysisUtils
						.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
								Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
				annotationValidity = false;
			}
		}
		addClassEnvironmentForClass(declaringClass);
		if (annotationValidity) {
			try {
				ClassEnvironment ce = store.getClassEnvironment(declaringClass);
				classWriteEffects.addAll(ce.getWriteEffects());
			} catch (NoSuchElementException e) {
				log.exception(
						AnalysisUtils.generateFileName(declaringClass),
						0,
						getMsg("env_store.not_found_class",
								AnalysisUtils.generateClassSignature(declaringClass, Configuration.CLASS_SIGNATURE_PRINT_PACKAGE)), e);
				annotationValidity = false;
			}
		}
		for (SootClass exceptions : sootMethod.getExceptions()) {
			addClassEnvironmentForClass(exceptions);
		}
		MethodEnvironment methodEnvironment = new MethodEnvironment(sootMethod, isIdFunction, isClinit, isInit, isVoid, isSootSecurityMethod,
				parameterSecurityLevel, returnSecurityLevel, methodWriteEffects, classWriteEffects, log, mediator);
		return methodEnvironment;
	}

	/**
	 * DOC
	 * 
	 * @param sootClass
	 */
	private void doExtraction(SootClass sootClass) {
		if (!AnalysisUtils.isInnerClassOfDefinitionClass(sootClass)) {
			addClassEnvironmentForClass(sootClass);
			if (!AnalysisUtils.isDefinitionClass(sootClass)) {
				for (SootField sootField : sootClass.getFields()) {
					addFieldEvironmentForField(sootField);
				}
			}
			if (!AnalysisUtils.containsStaticInitializer(sootClass.getMethods())) {
				AnalysisUtils.generatedEmptyStaticInitializer(sootClass);
			}
			for (SootMethod sootMethod : sootClass.getMethods()) {
				if ((!AnalysisUtils.isMethodOfDefinitionClass(sootMethod)) || AnalysisUtils.isIdFunction(sootMethod, mediator.getAvailableLevels())) {
					UnitGraph graph = new BriefUnitGraph(sootMethod.retrieveActiveBody());
					sootMethod = graph.getBody().getMethod();
					addMethodEnvironmentForMethod(sootMethod);
					if (sootMethod.hasActiveBody()) {
						for (Unit unit : sootMethod.getActiveBody().getUnits()) {
							unit.apply(stmtSwitch);
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
		if (annotationValidity) {
			if (!store.containsClass(sootClass)) {
				ClassEnvironment ce = checkAndBuildClassEnvironment(sootClass);
				store.addClassEnvironment(ce);
			}
		}
	}

	/**
	 * DOC
	 * 
	 * @param sootField
	 */
	protected void addFieldEvironmentForField(SootField sootField) {
		if (annotationValidity) {
			if (!store.containsField(sootField)) {
				FieldEnvironment fe = checkAndBuildFieldEnvironment(sootField);
				store.addFieldEnvironment(fe);
			}
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

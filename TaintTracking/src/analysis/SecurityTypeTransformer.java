package analysis;

import interfaces.Cancelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import extractor.AnnotationExtractor;

import logging.AnalysisLog;

import resource.Configuration;
import security.ILevelMediator;
import soot.Body;
import soot.BodyTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import utils.AnalysisUtils;

/**
 * <h1>Security Transformer</h1>
 * 
 * The class {@link SecurityTypeTransformer} acts on a {@link Body}. This class provides a harness and acts as an interface for classes that
 * wish to transform a {@link Body}. Subclasses provide the actual Body transformation implementation.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 */
public class SecurityTypeTransformer extends BodyTransformer implements Cancelable {

	/** DOC */
	private final AnnotationExtractor extractor;
	/** DOC */
	private final boolean instantLogging;
	private final List<Cancelable> listeners = new ArrayList<Cancelable>();
	/** DOC */
	private final AnalysisLog log;
	/** DOC */
	private final ILevelMediator mediator;
	/** DOC */
	private boolean valid = true;
	/** DOC */
	private List<SootClass> visitedClasses = new ArrayList<SootClass>();

	/**
	 * DOC
	 * 
	 * @param mediator
	 * @param log
	 * @param extractor
	 * @param instantLogging
	 */
	public SecurityTypeTransformer(ILevelMediator mediator, AnalysisLog log, AnnotationExtractor extractor, boolean instantLogging) {
		super();
		this.mediator = mediator;
		this.log = log;
		this.extractor = extractor;
		this.instantLogging = instantLogging;
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
		this.valid = false;
	}

	@Override
	public void removeCancelListener(Cancelable cancelable) {
		this.listeners.remove(cancelable);

	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @param graph
	 */
	private void doAnalysis(SootMethod sootMethod, UnitGraph graph) {
		if (!AnalysisUtils.isMethodOfDefinition(sootMethod)) {
			if (instantLogging) {
				log.structure(AnalysisUtils.generateMethodSignature(sootMethod, Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
						Configuration.METHOD_SIGNATURE_PRINT_TYPE, Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY));
			}
			SecurityTypeAnalysis tt = new SecurityTypeAnalysis(log, sootMethod, mediator, graph, extractor.getUsedObjectStore(), this);
			tt.checkAnalysis();
		}
	}

	/**
	 * This method is called to perform the transformation itself. The method executes the {@link SecurityTypeAnalysis} analysis and checks
	 * the <em>write effects</em>, if the given body isn't from a {@code SootSecurityLevel} method.
	 * 
	 * @param body
	 *          The body on which to apply the transformation.
	 * @param phaseName
	 *          The phase name for this transform; not typically used by implementations.
	 * @param options
	 *          The actual computed options; a combination of default options and Scene specified options.
	 * @see soot.BodyTransformer#internalTransform(soot.Body, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void internalTransform(Body body, String phaseName, Map options) {
		if (valid && extractor.checkReasonability()) {
			UnitGraph graph = new BriefUnitGraph(body);
			SootMethod sootMethod = graph.getBody().getMethod();
			SootClass sootClass = sootMethod.getDeclaringClass();
			if (!AnalysisUtils.isInnerClassOfDefinitionClass(sootClass)) {
				if (!visitedClasses.contains(sootClass)) {
					if (!AnalysisUtils.containsStaticInitializer(sootClass.getMethods())) {
						SootMethod clinit = AnalysisUtils.generatedEmptyStaticInitializer(sootClass);
						UnitGraph clinitGraph = new BriefUnitGraph(clinit.getActiveBody());
						doAnalysis(clinit, clinitGraph);
					}
					visitedClasses.add(sootClass);
				}
				if ((!AnalysisUtils.isMethodOfDefinitionClass(sootMethod)) || AnalysisUtils.isIdFunction(sootMethod, mediator.getAvailableLevels())) {
					doAnalysis(sootMethod, graph);
				}
			}

		}
	}

}

package analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import preanalysis.AnnotationExtractor;
import preanalysis.AnnotationExtractor.UsedObjectStore;

import logging.SecurityLogger;

import resource.Configuration;
import security.LevelMediator;
import soot.Body;
import soot.BodyTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import utils.SootUtils;

/**
 * <h1>Security Transformer</h1>
 * 
 * The class {@link SecurityTransformer} acts on a {@link Body}. This class
 * provides a harness and acts as an interface for classes that wish to
 * transform a {@link Body}. Subclasses provide the actual Body transformation
 * implementation.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 */
public class SecurityTransformer extends BodyTransformer {

	/** DOC */
	private List<SootClass> visitedClasses = new ArrayList<SootClass>();
	/** DOC */
	private final LevelMediator mediator;
	/** DOC */
	private final SecurityLogger log;
	/** DOC */
	private final UsedObjectStore store;
	/** DOC */
	private final boolean instantLogging;
	/** DOC */
	private final AnnotationExtractor extractor;

	/**
	 * DOC
	 * 
	 * @param mediator
	 * @param log
	 * @param extractor
	 * @param instantLogging
	 */
	public SecurityTransformer(LevelMediator mediator,
			SecurityLogger log, AnnotationExtractor extractor,
			boolean instantLogging) {
		super();
		this.mediator = mediator;
		this.log = log;
		this.extractor  = extractor;
		this.store = extractor.getUsedObjectStore();
		this.instantLogging = instantLogging;
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @param graph
	 */
	private void doAnalysis(SootMethod sootMethod, UnitGraph graph) {
		if (!mediator.isMethodOfSootSecurityLevelClass(sootMethod)) {
			if (instantLogging) {
				log.structure(SootUtils.generateMethodSignature(sootMethod,
						Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
						Configuration.METHOD_SIGNATURE_PRINT_TYPE,
						Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY));
				log.addStandardFileHandlerForMethod(sootMethod);
			}
			TaintTracking tt = new TaintTracking(log, sootMethod, mediator, graph, store);
			tt.checkAnalysis();
			if (instantLogging)
				log.removeStandardFileHandler();
		}
	}

	/**
	 * This method is called to perform the transformation itself. The method
	 * executes the {@link TaintTracking} analysis and checks the
	 * <em>write effects</em>, if the given body isn't from a
	 * {@code SootSecurityLevel} method.
	 * 
	 * @param body
	 *            The body on which to apply the transformation.
	 * @param phaseName
	 *            The phase name for this transform; not typically used by
	 *            implementations.
	 * @param options
	 *            The actual computed options; a combination of default options
	 *            and Scene specified options.
	 * @see soot.BodyTransformer#internalTransform(soot.Body, java.lang.String,
	 *      java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void internalTransform(Body body, String phaseName, Map options) {
		if (extractor.checkReasonability()) {
			UnitGraph graph = new BriefUnitGraph(body);
			SootMethod sootMethod = graph.getBody().getMethod();
			SootClass sootClass = sootMethod.getDeclaringClass();
			if (!visitedClasses.contains(sootClass)) {
				if (!SootUtils
						.containsStaticInitializer(sootClass.getMethods())) {
					SootMethod clinit = SootUtils
							.generatedEmptyStaticInitializer(sootClass);
					UnitGraph clinitGraph = new BriefUnitGraph(
							clinit.getActiveBody());
					doAnalysis(clinit, clinitGraph);
				}
				visitedClasses.add(sootClass);
			}
			if (!mediator
					.isMethodOfSootSecurityLevelClass(sootMethod)) {
				doAnalysis(sootMethod, graph);
			}
		}
	}

}

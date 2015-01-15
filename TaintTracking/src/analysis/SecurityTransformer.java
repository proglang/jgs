package analysis;

import static main.AnalysisType.CONSTRAINTS;
import static main.AnalysisType.LEVELS;
import static resource.Messages.getMsg;
import static utils.AnalysisUtils.containsStaticInitializer;
import static utils.AnalysisUtils.getSignatureOfMethod;
import static utils.AnalysisUtils.generatedEmptyStaticInitializer;
import static utils.AnalysisUtils.isInnerClassOfDefinitionClass;
import static utils.AnalysisUtils.isLevelFunction;
import static utils.AnalysisUtils.isMethodOfDefinition;
import static utils.AnalysisUtils.isMethodOfDefinitionClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logging.AnalysisLog;
import main.AnalysisType;
import security.ILevelMediator;
import soot.Body;
import soot.BodyTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import exception.AnalysisTypeException;
import extractor.AnnotationExtractor;

/**
 * <h1>Security Transformer</h1>
 * 
 * The class {@link SecurityTransformer} acts on a {@link Body}. This class provides a harness and acts as an interface for classes that
 * wish to transform a {@link Body}. Subclasses provide the actual Body transformation implementation.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 */
public class SecurityTransformer extends BodyTransformer {

	/** DOC */
	private final AnnotationExtractor extractor;
	/** DOC */
	private final boolean instantLogging;
	/** DOC */
	private final AnalysisLog log;
	/** DOC */
	private final ILevelMediator mediator;
	/** DOC */
	private List<SootClass> visitedClasses = new ArrayList<SootClass>();
	/** DOC */
	private long counter = 0;
	private final AnalysisType type;

	/**
	 * DOC
	 * 
	 * @param mediator
	 * @param log
	 * @param extractor
	 * @param instantLogging
	 * @param type 
	 */
	public SecurityTransformer(ILevelMediator mediator, AnalysisLog log, AnnotationExtractor extractor, boolean instantLogging, AnalysisType type) {
		super();
		this.mediator = mediator;
		this.log = log;
		this.extractor = extractor;
		this.instantLogging = instantLogging;
		this.type = type;
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @param graph
	 */
	private void doAnalysis(SootMethod sootMethod, UnitGraph graph) {
		if (!isMethodOfDefinition(sootMethod)) {
			if (instantLogging) {
				log.structure(getSignatureOfMethod(sootMethod));
			}
			if (type.equals(CONSTRAINTS)) {
				SecurityConstraintsAnalysis sca = new SecurityConstraintsAnalysis(log, sootMethod, mediator, graph, extractor.getUsedObjectStore());
				sca.checkAnalysis();
			} else if (type.equals(LEVELS)) {
				SecurityLevelAnalysis sla = new SecurityLevelAnalysis(log, sootMethod, mediator, graph, extractor.getUsedObjectStore());
				sla.checkAnalysis();
			} else {
				throw new AnalysisTypeException(getMsg("exception.analysis_type.unknown", type.toString()));
			}
			
		}
	}

	/**
	 * This method is called to perform the transformation itself. The method executes the {@link SecurityLevelAnalysis} analysis and checks
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
		doInitialChecks();
		UnitGraph graph = new BriefUnitGraph(body);
		SootMethod sootMethod = graph.getBody().getMethod();
		SootClass sootClass = sootMethod.getDeclaringClass();
		if (!isInnerClassOfDefinitionClass(sootClass)) {
			if (!visitedClasses.contains(sootClass)) {
				if (!containsStaticInitializer(sootClass.getMethods())) {
					SootMethod clinit = generatedEmptyStaticInitializer(sootClass);
					UnitGraph clinitGraph = new BriefUnitGraph(clinit.getActiveBody());
					doAnalysis(clinit, clinitGraph);
				}
				visitedClasses.add(sootClass);
			}
			if ((!isMethodOfDefinitionClass(sootMethod)) || isLevelFunction(sootMethod, mediator.getAvailableLevels())) {
				doAnalysis(sootMethod, graph);
			}
		}
	}

	private void doInitialChecks() {
		if (0 == counter++) {
			extractor.checkReasonability();
			extractor.checkHierarchy();
		}
	}

}

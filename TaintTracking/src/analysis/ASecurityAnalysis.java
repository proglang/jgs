package analysis;

import static resource.Messages.getMsg;
import static util.AnalysisUtils.getSignatureOfMethod;
import logging.AnalysisLog;
import model.AnalyzedMethodEnvironment;
import model.MethodEnvironment;
import security.ILevelMediator;
import soot.SootMethod;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import util.Debugger;
import util.Debugger.Header;
import exception.AnalysisException;
import exception.EnvironmentNotFoundException;
import extractor.UsedObjectStore;

/**
 * <h1>TaintTracking analysis</h1>
 * 
 * The {@link ASecurityAnalysis} is the security analysis and extends the
 * {@link ForwardFlowAnalysis}. The class allows the analysis of the flow of a
 * single analyzed method in order to detect security violations. Note, the call
 * of the constructor will analyze violations of the <em>security level</em> and
 * calculates the <em>write effects</em> of the analyzed method. In order to
 * check those calculated effects the method
 * {@link ASecurityAnalysis#checkAnalysis()} has to be called.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see SecurityLevelStmtSwitch
 */
public abstract class ASecurityAnalysis<N, A> extends ForwardFlowAnalysis<N, A> {

    /** The environment of the current analyzed method. */
    private AnalyzedMethodEnvironment analyzedMethodEnvironment;
    /**
     * The dominator container, which provides the post dominator and the
     * dominator set for a specific unit.
     */
    private final Dominator<N> container;

    /**
     * DOC
     */
    private final UsedObjectStore store;

    /**
     * Constructor of {@link ASecurityAnalysis} which checks automatically the
     * given graph of the also given method for security violations, i.e.
     * violations of <em>security levels</em> or violations of
     * <em>write effects</em>. The constructor requires in addition a logger
     * that allows the logging of exceptions and violations, and a security
     * annotation instance that allows the handling of <em>security levels</em>
     * for this method. Note, that the method
     * {@link ASecurityAnalysis#checkAnalysis()} has to be called for the final
     * check of the <em>write effects</em>.
     * 
     * @param log
     *            The {@link AnalysisLog} which provides the logging of
     *            exceptions, errors, violations, etc.
     * @param sootMethod
     *            The method which should be analyzed.
     * @param mediator
     *            A {@link LevelMediator} in order to provide the handling of
     *            <em>security levels</em>.
     * @param graph
     *            The generated graph for the given method.
     * @param store
     */
    protected ASecurityAnalysis(AnalysisLog log, SootMethod sootMethod,
            ILevelMediator mediator, DirectedGraph<N> graph,
            UsedObjectStore store) {
        super(graph);
        this.container = new Dominator<N>(graph);
        this.store = store;
        try {
            MethodEnvironment me =
                this.store.getMethodEnvironment(sootMethod.retrieveActiveBody()
                                                          .getMethod());
            analyzedMethodEnvironment = new AnalyzedMethodEnvironment(me);
            Debugger.show(new Header("New Method: " + sootMethod.getSignature()));
            doAnalysis();
        } catch (EnvironmentNotFoundException e) {
            throw new AnalysisException(getMsg("exception.analysis.other.error_env",
                                               getSignatureOfMethod(sootMethod)),
                                        e);
        }
    }

    protected final AnalyzedMethodEnvironment getAnalyzedEnvironment() {
        return analyzedMethodEnvironment;
    }

    protected final Dominator<N> getContainer() {
        return container;
    }

    protected final UsedObjectStore getStore() {
        return store;
    }

}
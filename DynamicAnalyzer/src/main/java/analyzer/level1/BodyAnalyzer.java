package analyzer.level1;

import analyzer.level2.HandleStmt;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Casts;
import de.unifreiburg.cs.proglang.jgs.instrumentation.MethodTypings;
import soot.*;
import soot.util.Chain;
import util.dominator.DominatorFinder;
import util.dominator.WriteEffectCollector;
import util.logging.L1Logger;
import util.visitor.AnnotationStmtSwitch;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This Analyzer extends the Body Transformer of the Soot Framework,
 * so the {@link BodyAnalyzer#internalTransform(Body, String, Map)} is called
 * from Soot for every Methods Body of the analyzed Code.
 *
 * The BodyAnalyzer decides for every body what shall be inserted into
 * the Body.
 * E.g for each constructor, an new Object and Field Map is inserted into the ObjectMap.
 * For each method, a HandleStmt object is inserted (which contains a local Map 
 * for the Locals and the localPC).
 * Then every Local is inserted into this map.
 * At least it iterates over all Units and calls the appropriate operation
 * At the end (but before the return statement) it calls HandleStmt.close()
 * @author koenigr, Karsten Fix (2017)
 *
 */
public class BodyAnalyzer<Level> extends BodyTransformer {

    private MethodTypings<Level> methodTypings;
	private boolean controllerIsActive;
	private int expectedException;
	private Casts<Level> casts;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Constructs an new BodyAnalyzer with the given
	 * @param m
	 * @param c
	 */
    public BodyAnalyzer(MethodTypings<Level> m, Casts<Level> c) {
        methodTypings = m;
        casts = c;
    }

	/**
	 * This Method is called from the Soot Framework. In this Specific Implementation
	 * it inserts some invokes of the {@link analyzer.level2.HandleStmt}.
	 *
	 * @param body The Body of the Method, that is analyzed and may be instrumented.
	 * @param s The Phase Name of this Soot Phase, in this case it should be "jtp.analyzer"
	 * @param map A Map of Options, that could be defined and passed to this Method. These
	 *            Options can be passed as command line arguments. As defined by Soot.
	 */
	@Override
	protected void internalTransform(Body body, String s, Map<String, String> map) {
		logger.info(" Analyze of :\"" + body.getMethod().getName() + "\" started.");

		SootMethod sootMethod = body.getMethod();
		Chain<Unit> units  = body.getUnits();

		AnnotationStmtSwitch stmtSwitch =  new AnnotationStmtSwitch(body);
		Chain<SootField> fields = sootMethod.getDeclaringClass().getFields();

		// Using a copy, such that JimpleInjector could inject directly.
		ArrayList<Unit> unMod = new ArrayList<>(units);

		DominatorFinder.init(body);

		WriteEffectCollector wec = null;
		if (DynamicPolicy.selected == DynamicPolicy.Policy.HYBRID_ENFORCEMENT) {
			wec =  new WriteEffectCollector(body);
			wec.collectWriteEffect();
		}

		// The JimpleInjector actually inserts the invokes, that we decide to insert.
		// In order, that the righteous body got the inserts, we have to set up the
		// Body for the Injections.
		JimpleInjector.setBody(body);

		// hand over exactly those Maps that contain Instantiation, Statement and Locals for the currently analyzed method
		JimpleInjector.setStaticAnalaysisResults(methodTypings.getVarTyping(sootMethod),
                                                 methodTypings.getCxTyping(sootMethod),
                                                 // We set the default type to dyn; our RT-system is able to handle untracked variables.
                                                 methodTypings.getSingleInstantiation(sootMethod, new TypeViews.Dyn<>()),
                                                 casts);



		// invokeHS should be at the beginning of every method-body. 
		// It creates a map for locals.
		JimpleInjector.invokeHS();
		JimpleInjector.addNeededLocals();

		// We have to initialize the run-time system at the very beginning.
		// That is, either
		// - at the beginning of the clinit of the Main class (which is // the class that contains the main method), *if it exists*, or
		// - if there is no clinit, at the beginning of main
		// TODO: the run-time system should inititalize itself lazily, perhaps (i.e., on-demand)
		if (isFirstApplicationMethodToRun(sootMethod)) {
			JimpleInjector.initHS();
		}

        JimpleInjector.initHandleStmtUtils(controllerIsActive, expectedException);

		// <editor-fold desc="Add Fields to Object Map, either static or instance; determined by Method name">

		/*
		 * If the method is the constructor, the newly created object
		 * has to be added to the ObjectMap and its fields are added to the
		 * new object
		 */
		if (sootMethod.getName().equals("<init>")) {
			JimpleInjector.addInstanceObjectToObjectMap();
						
			// Add all instance fields to ObjectMap
			for (SootField f : fields) {
				if (!f.isStatic()) {
					JimpleInjector.addInstanceFieldToObjectMap(f);
				}
			}
						
		} else if (sootMethod.getName().equals("<clinit>")) {

			SootClass sc = sootMethod.getDeclaringClass();
			JimpleInjector.addClassObjectToObjectMap(sc);

			// Add all static fields to ObjectMap
			for (SootField f : fields) {
				if (f.isStatic()) {
					JimpleInjector.addStaticFieldToObjectMap(f);
				}
			}
		}

		// </editor-fold>


		// Analyzing Every Statement, step by step.
		for (Unit unit: unMod) {
			// Check if the statements is a postdominator for an IfStmt.
			if (DominatorFinder.containsStmt(unit)) {
				JimpleInjector.exitInnerScope(unit);
				logger.info("Exit inner scope with identity" +	DominatorFinder.getIdentityForUnit(unit));
				DominatorFinder.removeStmt(unit);
			}

			// Add further statements using JimpleInjector.
			unit.apply(stmtSwitch);

			if (wec != null) {
				for (Local l : wec.get(Local.class, unit)) {
					logger.info("Updating " + l);
				}
			}
		}
		
		// Apply all changes.
		JimpleInjector.addUnitsToChain();
		JimpleInjector.closeHS();
	}

	/**
	 * Specifies, if the given Method is the First Application Method,
	 * such that {@link HandleStmt#init()} is not inserted to much, it is enough
	 * to call it at the very begging.
	 * @param method The Method, that is tested.
	 * @return true, iff the given Method is the first Application Method.
	 */
	private boolean isFirstApplicationMethodToRun(SootMethod method) {
	    if (method.isMain()) {
			for (SootMethod m : method.getDeclaringClass().getMethods()) {
				if (m.getName().equals("<clinit>")) {
					return false;
				}
			}
			// there is no clinit in the main-class
			return true;
		} else if (method.getName().equals("<clinit>")){
			for (SootMethod m : method.getDeclaringClass().getMethods()) {
				if (m.isMain()) {
					return true; // we are clinit, and in the main class
				}
			}
			// we are another clinit, not in the main class
			return false;
		} else {
			return false; // neither clinit nor main method
		}
	}
}

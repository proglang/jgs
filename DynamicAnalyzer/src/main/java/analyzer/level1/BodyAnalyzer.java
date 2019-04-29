package analyzer.level1;

import analyzer.level2.HandleStmt;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Casts;
import de.unifreiburg.cs.proglang.jgs.instrumentation.MethodTypings;
import soot.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import util.dominator.DominatorFinder;
import util.visitor.AnnotationStmtSwitch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private List<Unit> successorStmt = new ArrayList<Unit>();
	private List<Unit> nextSuccessorStmt = new ArrayList<Unit>();
	private List<String> unitRhsList = new ArrayList<String>();
	private List<String> unitStmtsListString = new ArrayList<String>();

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
		logger.info(" Analyze of :" + body.getMethod().getName() + " started.");

		SootMethod sootMethod = body.getMethod();

		Chain<Unit> units  = body.getUnits();

		AnnotationStmtSwitch stmtSwitch = new AnnotationStmtSwitch(body, casts);
		Chain<SootField> fields = sootMethod.getDeclaringClass().getFields();

		// Using a copy, such that JimpleInjector could inject directly.
		ArrayList<Unit> unmodifiedStmts = new ArrayList<>(units);

		DominatorFinder.init(body);

		// The JimpleInjector actually inserts the invokes, that we decide to insert.
		// In order, that the righteous body got the inserts, we have to set up the
		// Body for the Injections.
		JimpleInjector.setBody(body);

		UnitGraph unitGraph = new BriefUnitGraph(body);

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

		List<Unit> successorStmt = new ArrayList<Unit>();
		List<String> unitRhsList = new ArrayList<String>(); // contains the RHS of the units in string
		List<String> unitRhsListString = new ArrayList<String>(); // contains the RHS of the units in string - need separate lists to deal with separate data type params
		List<String> unitStmtsListString = new ArrayList<String>(); // contains the units in string

		for (Unit unit: unmodifiedStmts) {
			unitStmtsListString.add(unit.toString());
			if (unit.toString().contains("=")) {
				unitRhsList.add(unit.toString().split("=")[1]);
				unitRhsListString.add(unit.toString().split("=")[1]);
			}
			else {
				unitRhsList.add(unit.toString());
				unitRhsListString.add(unit.toString());
			}
		}

		this.unitRhsList = new ArrayList<>(unitRhsList);
		this.unitStmtsListString = new ArrayList<>(unitStmtsListString);

		// Analyzing Every Statement, step by step.
		boolean isNotStringFlag = false;
		boolean isStringFlag = false;
		int stmtIndex = 0;
		for (Unit unit: unmodifiedStmts) {
			boolean dynLabelFlag = false;
			String unitLhsString = "";
			String unitRhsString = "";

			if(isArithmeticExpression(unit.toString()))
				JimpleInjector.arithmeticExpressionFlag = true;

			if(unit.toString().contains("makeHigh") || unit.toString().contains("makeLow")){
				dynLabelFlag = true;
			}

			if(dynLabelFlag){
				// for int, double, boolean, float, char params
				for(int i = 0; i < unitRhsList.size(); i++) {
					if (unitRhsList.get(i).contains("intValue") || unitRhsList.get(i).contains("doubleValue") || unitRhsList.get(i).contains("floatValue")
							|| unitRhsList.get(i).contains("booleanValue") || unitRhsList.get(i).contains("charValue")) {
						if(stmtIndex + 2 == i) {
							isNotStringFlag = true;
							isStringFlag = false;
							unitLhsString = unitStmtsListString.get(i).split("=")[0];
							//int index = i + 1; // to escape the following assignment statement
							int index = i;
							List<Unit> tempSuccessorList = new ArrayList<Unit>();
							tempSuccessorList.add(unit);

							int u = 0;
							while(unitGraph.getSuccsOf(tempSuccessorList.get(0)).size() > 0){
								tempSuccessorList.set(0, (unitGraph.getSuccsOf(tempSuccessorList.get(0))).get(0));
								u += 1;
							}

							tempSuccessorList.set(0, unit);
							// TODO: get rid of hardcoded 4 !!
							//for (int m = 0; m < 4 && m <= unitStmtsListString.size() - 1; m++) {
							for (int m = 0; m < 4 && m < u; m++) {
								tempSuccessorList.set(0, (unitGraph.getSuccsOf(tempSuccessorList.get(0))).get(0));
							}
							if (tempSuccessorList.get(0).toString().contains("makeHigh") || tempSuccessorList.get(0).toString().contains("makeLow"))
								index = index + 2; // to escape the following assignment and makeHigh/makeLow statements

							for (int j = 0; j <= index; j++) {
								unitRhsList.remove(0);
								unitStmtsListString.remove(0);
							}
							boolean varExistsFlag = varExists(unitRhsList, unitLhsString);
							if(!varExistsFlag){
								JimpleInjector.dynLabelFlag = true;
								successorStmt = unitGraph.getSuccsOf(unit);
								this.successorStmt = successorStmt;
							}
							unitRhsList = new ArrayList<>(this.unitRhsList);
							unitStmtsListString = new ArrayList<>(this.unitStmtsListString);
							break;
						}
					}
				}
				// for string, stringbuilder, object params
				if(!isNotStringFlag) {
					isStringFlag = true;
					successorStmt = unitGraph.getSuccsOf(unit);
					this.successorStmt = successorStmt;
					unitLhsString = successorStmt.get(0).toString().split("=")[0];
					unitRhsString = successorStmt.get(0).toString().split("=")[1];
					int index = unitRhsListString.indexOf(unitRhsString);
					successorStmt = unitGraph.getSuccsOf(successorStmt.get(0));
					if(successorStmt.get(0).toString().contains("makeHigh") || successorStmt.get(0).toString().contains("makeLow"))
						index += 1; // to escape the following makeHigh/makeLow statement
					for (int k = 0; k <= index; k++) {
						unitRhsListString.remove(0);
					}
					boolean varExistsFlag = varExists(unitRhsListString, unitLhsString);
					if(!varExistsFlag){
						JimpleInjector.dynLabelFlag = true;
					}
				}
			}

			if(isStringFlag){
				// check if var is used in code. If not, the tracing for the assignment statement can be skipped
				if(this.successorStmt.size() > 0 && this.successorStmt.get(0).toString().equals(unit.toString())) {
					unitLhsString = this.successorStmt.get(0).toString().split("=")[0];
					boolean varExistsFlag = varExists(unitRhsListString, unitLhsString);
					if(!varExistsFlag){
						JimpleInjector.dynLabelFlag = true;
					}
				}
			}
			else {
				// check if var is used in code. If not, the tracing for the assignment statement can be skipped
				if (this.successorStmt.size() > 0 && this.successorStmt.get(0).toString().equals(unit.toString())) {
					JimpleInjector.dynLabelFlag = true;
					nextSuccessorStmt = unitGraph.getSuccsOf(unit);
				}
			}
			// remove extra tracing for methods like intValue
			if (nextSuccessorStmt.size() > 0 && (nextSuccessorStmt.get(0).toString().contains("intValue") || nextSuccessorStmt.get(0).toString().contains("doubleValue") || nextSuccessorStmt.get(0).toString().contains("booleanValue")
					|| nextSuccessorStmt.get(0).toString().contains("floatValue") || nextSuccessorStmt.get(0).toString().contains("charValue"))
					&& nextSuccessorStmt.get(0).toString().equals(unit.toString())) {
				JimpleInjector.dynLabelFlag = true;
				nextSuccessorStmt = unitGraph.getSuccsOf(unit);
			}

			// remove extra tracing for methods like valueOf
			//if (nextSuccessorStmt.size() > 0 && nextSuccessorStmt.get(0).toString().contains("valueOf") && nextSuccessorStmt.get(0).toString().equals(unit.toString())) {
			if(unit.toString().contains("valueOf"))	{
				nextSuccessorStmt = unitGraph.getSuccsOf(unit);
				if(nextSuccessorStmt.get(0).toString().contains("makeHigh") || nextSuccessorStmt.get(0).toString().contains("makeLow"))
					JimpleInjector.dynLabelFlag = true;
			}

			// Check if the statements is a postdominator for an IfStmt.
			if (DominatorFinder.containsStmt(unit)) {
				JimpleInjector.exitInnerScope(unit);
				logger.info("Exit inner scope with identity" +	DominatorFinder.getIdentityForUnit(unit));
				DominatorFinder.removeStmt(unit);
			}

			// Add further statements using JimpleInjector.
			unit.apply(stmtSwitch);
			JimpleInjector.dynLabelFlag = false;
			JimpleInjector.arithmeticExpressionFlag = false;
			stmtIndex += 1;
		}

		// Apply all changes.
		JimpleInjector.addUnitsToChain();
		JimpleInjector.closeHS();
	}


	// find if the var is used again in code. If not, it does not need to be tracked explicitly
	private static boolean varExists(List<String> unitRhsListString, String unitLhsString){
		boolean varExistsFlag = false;
		for(String a : unitRhsListString){    // source : https://stackoverflow.com/questions/25417363/java-string-contains-matches-exact-word
			String pattern = "\\b"+unitLhsString.trim()+"\\b";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(a);
			varExistsFlag = m.find();
			if(varExistsFlag)
				break;
		}
		return varExistsFlag;
	}

	// check if the unit string is an arithmetic expression
	private static boolean isArithmeticExpression(String s){

		if(Pattern.compile("[-+*/]").matcher(s).find()){
			return true;
		}

		return false;
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

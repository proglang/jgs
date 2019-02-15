package analyzer.level1;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.util.Chain;
import util.dominator.DominatorFinder;
import util.logging.L1Logger;
import util.visitor.AnnotationStmtSwitch;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Analyzer is applied to every method.
 * If it's the main method, then ...
 * For each constructor, an new Object and Field Map is inserted into the ObjectMap.
 * For each method, a HandleStmt object is inserted (which contains a local Map 
 * for the Locals and the localPC).
 * Then every Local is inserted into this map.
 * At least it iterates over all Units and calls the appropriate operation
 * At the end (but before the return statement) it calls HandleStmt.close()
 * @author koenigr
 *
 */
public class BodyAnalyzer extends BodyTransformer{

	/**
	 * internalTransform is an internal soot method, which is called somewhere along the way.
	 * for us, it serves as an entry point to the instrumentation process
	 */
	@Override
	protected void internalTransform(Body arg0, String arg1,
				@SuppressWarnings("rawtypes") Map arg2) {
		SootMethod method;
		Body body;	
		
		/*
		 * Chain<Unit> units contains for TestProgramm Simple:
		 * 
		 * [r0 := @parameter0: java.lang.String[], r1 = "Hello World", 
		 * $r2 = staticinvoke <util.analyzer.HelperClass: java.lang.Object makeHigh(java.lang.Object)>(r1),
		 * r3 = (java.lang.String) $r2, $r4 = <java.lang.System: java.io.PrintStream out>, 
		 * virtualinvoke $r4.<java.io.PrintStream: void println(java.lang.String)>(r3), return]
		 * Invoke HandleStmt in method main
		 */
		Chain<Unit> units;
		
		/*
		 * Chain<Local> locals contains for TestProgram Simple:
		 * 
		 * [r0, r1, $r2, r3, $r4]
		 */
		Chain<Local> locals;
		AnnotationStmtSwitch stmtSwitch;
		
		
		/*
		 * hain<SootField> fields contains for TestProgramm Simple:
		 *
		 * []		//empty
		 */
		Chain<SootField> fields;


		Logger logger = L1Logger.getLogger();
		
		try { 

			System.out.println("Logger Init2");
			L1Logger.setup(Level.ALL);
		} catch (IOException e) {
			logger.log(Level.WARNING, "L1Logger couldn't be initialized properly");
			e.printStackTrace();
		}

		logger.log(Level.SEVERE, "\n BODYTRANSFORM STARTED: {0}",
				arg0.getMethod().getName());
	
		body = arg0;
		method = body.getMethod();
		fields = method.getDeclaringClass().getFields();	

		stmtSwitch = new AnnotationStmtSwitch(body);

		DominatorFinder.init(body);
				
		JimpleInjector.setBody(body);

		units = body.getUnits();
		locals = body.getLocals();
				
		// invokeHS should be at the beginning of every method-body. 
		// It creates a map for locals.
		JimpleInjector.invokeHS();
		JimpleInjector.addNeededLocals();
				
		if (method.isMain()) {
			JimpleInjector.initHS();
		}

		/*
		 * If the method is the constructor, the newly created object
		 * has to be added to the ObjectMap and its fields are added to the
		 * new object
		 */
		if (method.getName().equals("<init>")) {
			logger.log(Level.INFO, "Entering <init>");
			JimpleInjector.addInstanceObjectToObjectMap();
						
			// Add all instance fields to ObjectMap
			Iterator<SootField> fIt = fields.iterator();
			while (fIt.hasNext()) {
				SootField item = fIt.next();
				if (!item.isStatic()) {
					JimpleInjector.addInstanceFieldToObjectMap(item);
				}
			}
						
		} else if (method.getName().equals("<clinit>")) {
			logger.log(Level.INFO, "Entering <clinit>");
			SootClass sc = method.getDeclaringClass();
			JimpleInjector.addClassObjectToObjectMap(sc);
						
			// Add all static fields to ObjectMap
			Iterator<SootField> fIt = fields.iterator();
			while (fIt.hasNext()) {
				SootField item = fIt.next();
				if (item.isStatic()) {
					JimpleInjector.addStaticFieldToObjectMap(item);
				} 
			}
		}
				

		// Add all locals to LocalMap except the locals which 
		// are inserted for analysis purposes.
		Iterator<Local> lit = locals.iterator();
		while (lit.hasNext()) {
			Local item = lit.next();
			if (!(item.getName() == "local_for_Strings") 
					&& !(item.getName() == "local_for_String_Arrays")
					&& !(item.getName() == "local_for_Strings2") 
					&& !(item.getName() == "local_for_Strings3") 
					&& !(item.getName() == "local_for_Objects") 
					&& !(item.getName() == "local_level")
					&& !(item.getName() == "hs")) {
				JimpleInjector.addLocal(item);
			}
		}

				
				
		Iterator<Unit> uit = units.iterator();
		while (uit.hasNext()) {
			Unit item = uit.next();
			
			// Check if the statements is a postdominator for an IfStmt.
			if (DominatorFinder.containsStmt(item)) {
				JimpleInjector.exitInnerScope(item);
				logger.log(Level.INFO, "Exit inner scope with identity {0}", 
					DominatorFinder.getIdentityForUnit(item));

				DominatorFinder.removeStmt(item);
			}
			
			// Add further statements using JimpleInjector.
			item.apply(stmtSwitch);
		}
		
		// Apply all changes.
		JimpleInjector.addUnitsToChain();			
		
		JimpleInjector.closeHS();
	}
}

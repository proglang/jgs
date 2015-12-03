package analyzer.level1;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.dominator.DominatorFinder;
import utils.logging.L1Logger;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.util.Chain;
import utils.visitor.AnnotationStmtSwitch;
import utils.visitor.AnnotationValueSwitch;

/**
 * This Analyzer is applied to every method.
 * If it's the main method, then ...
 * For each constructor, an new Object and Field Map is inserted into the ObjectMap.
 * For each method, a HandleStmt object is inserted (which contains a local Map for the Locals and the localPC).
 * Then every Local is inserted into this map.
 * At least it iterates over all Units and calls the appropriate operation
 * At the end (but before the return statement) it calls HandleStmt.close()
 * @author koenigr
 *
 */
public class BodyAnalyzer extends BodyTransformer{


    
	
  /* (non-Javadoc)
   * @see soot.BodyTransformer#internalTransform(soot.Body, java.lang.String, java.util.Map)
   */
  @Override
  protected void internalTransform(Body arg0, String arg1,  Map arg2) {
	  SootClass sootClass;
	  SootMethod method;
	  Body body;  
	  Chain<Unit> units;
	  Chain<Local> locals;
	  AnnotationStmtSwitch stmtSwitch;
	  AnnotationValueSwitch valueSwitch;
	  Chain<SootField> fields;

	  DominatorFinder df;

	  Logger logger = L1Logger.getLogger();
	  
    try { 

      System.out.println("Logger Init2");
      L1Logger.setup(Level.ALL);
    } catch (IOException e) {
      logger.log(Level.WARNING, "L1Logger couldn't be initialized properly");
      e.printStackTrace();
    }

		logger.log(Level.SEVERE, "\n BODYTRANSFORM STARTED: {0}", arg0.getMethod().getName());
		
		stmtSwitch = new AnnotationStmtSwitch();
    	valueSwitch = new AnnotationValueSwitch();	
    	
        body = arg0;
        method = body.getMethod();
        fields = method.getDeclaringClass().getFields();  

        df = new DominatorFinder(body);
        
        JimpleInjector.setBody(body);

        units = body.getUnits();
        locals = body.getLocals();
        
        // invokeHS should be at the beginning of every method-body. It creates a map for locals.
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
        		while(fIt.hasNext()) {
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
        		while(fIt.hasNext()) {
        			SootField item = fIt.next();
        			if (item.isStatic()) {
        				JimpleInjector.addStaticFieldToObjectMap(item);
        			} 
        		}
        }
        


        Iterator<Local> lit = locals.iterator();
        while(lit.hasNext()) {
        	Local item = lit.next();
        	if (!(item.getName() == "local_for_Strings")&& !(item.getName() == "local_for_String_Arrays")
        			&&!(item.getName() == "local_for_Objects")&&!(item.getName() == "local_level")
        			&& !(item.getName() == "hs")) {
        	  JimpleInjector.addLocal(item);
        	}
        }

        
        
        Iterator<Unit> uit = units.iterator();
        while(uit.hasNext()) {
        	Unit item = uit.next();
			item.apply(stmtSwitch);
			if (item instanceof IfStmt) {
				System.out.println("HIER ==>> " + item.toString());
				df.getImmediateDominator(item);
			}
			while (df.containsStmt(item)) {
				// JimpleInjector.exitInnerScope();
			}
        }
        
        JimpleInjector.addUnitsToChain();      
        
        JimpleInjector.closeHS();
	}




}

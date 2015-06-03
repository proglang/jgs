package analyzer.level1;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L1Logger;
import annotationExtractor.Extractor;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.util.Chain;
import visitor.AnnotationStmtSwitch;
import visitor.AnnotationValueSwitch;

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
	

	Extractor annotationExtractor;
	// TODO die Datei die untersucht werden soll, aus den Soot Argumenten holen
    SootClass sootClass;
    SootMethod method;
    Body body;  
    Chain<Unit> units;
    Chain<Local> locals;
    AnnotationStmtSwitch stmtSwitch;
    AnnotationValueSwitch valueSwitch;
    Chain<SootField> fields;
    Logger LOGGER;
    
    
	
	@Override
	protected void internalTransform(Body arg0, String arg1, Map arg2) {
		try { // TODO gibt es beim BodyTransformer auch eine Init Klasse, in die ich das schieben kann?
			L1Logger.setup();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "L1Logger couldn't be initialized properly");
			e.printStackTrace();
		}
		LOGGER = L1Logger.getLogger();
		LOGGER.log(Level.SEVERE, "BodyTransform started: {0}", arg0.getMethod().getName());
		
		stmtSwitch = new AnnotationStmtSwitch();
    	valueSwitch = new AnnotationValueSwitch();	
		annotationExtractor = new Extractor();
		
        body = arg0;
        method = body.getMethod();
        fields = method.getDeclaringClass().getFields();
        
        JimpleInjector.setBody(body);

        units = body.getUnits();
        locals = body.getLocals();
        
        // invokeHS should be at the beginning of every methodbody. It creates a map for locals.
        JimpleInjector.invokeHS();
        JimpleInjector.addNeededLocals();
        
        if (method.isMain()) {
          JimpleInjector.initHS();
        }

        
        if (method.isConstructor() && !method.isStatic()) {
            // TODO: wenn init(), dann add Object to ObjectMap
            // TODO: wenn init(), dann add Fields to Map
        LOGGER.log(Level.INFO, "Entering <init>");
        
        JimpleInjector.addObjectToObjectMap(this);
        
        Iterator<SootField> fIt = fields.iterator();
        while(fIt.hasNext()) {
        	SootField item = fIt.next();
        	//JimpleInjector.addFieldToMap(item, Level.HIGH);
        }
        }
       

        Iterator<Local> lit = locals.iterator();
        while(lit.hasNext()) {
        	Local item = lit.next();
        	if (!(item.getName() == "local_name1")&& !(item.getName() == "local_name2")
        			&&!(item.getName() == "local_name3")&&!(item.getName() == "local_level")
        			&& !(item.getName() == "hs")) {
        	  JimpleInjector.addLocal(item);
        	}
        }
        
        Iterator<Unit> uit = units.iterator();
        while(uit.hasNext()) {
        	Unit item = uit.next();
			item.apply(stmtSwitch);
        }
        JimpleInjector.addUnitsToChain();      
        
        JimpleInjector.closeHS();
	}




}

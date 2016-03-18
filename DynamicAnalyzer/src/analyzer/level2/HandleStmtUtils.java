package analyzer.level2;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.exceptions.InternalAnalyzerException;
import utils.logging.L2Logger;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;

public class HandleStmtUtils {
	
	Logger LOGGER = L2Logger.getLogger();
	private LocalMap lm;
	private static ObjectMap om;
	
	protected HandleStmtUtils(LocalMap lm, ObjectMap om) {
		this.lm = lm;
		if (lm == null) {
			new InternalAnalyzerException("LocalMap initialization has failed.");
		}
		HandleStmtUtils.om = om;		
		if (om == null) {
			new InternalAnalyzerException("ObjectMap initialization has failed.");
		}
	}
	
	//
	// PC check operations
	//

	/**
	 * @param signature
	 * @return
	 */
	protected boolean checkLocalPC(String signature) {		
		if (lm == null) {
			System.out.println("sdf"); // TODO
		}
		SecurityLevel level = lm.getLevel(signature);
		SecurityLevel lpc = lm.getLocalPC();
		LOGGER.log(Level.INFO, "Check if level of local {0} ({1}) >= lpc ({2})", 
				new Object[] {signature, level, lpc });
		boolean res = true;
		if (level == SecurityLevel.LOW && lpc == SecurityLevel.HIGH) {
			res = false;
		}
		return res;		
	}
	
	/**
	 * @param o
	 * @param signature
	 * @return
	 */
	protected boolean checkGlobalPC(Object o, String signature) {
		LOGGER.log(Level.INFO, "Check if level of field {0} ({1}) >= gpc ({1})",
				new Object[] {
				 signature, om.getFieldLevel(o, signature), om.getGlobalPC()
				});
		boolean res = true;
		if (om.getFieldLevel(o, signature) == SecurityLevel.LOW 
				&& om.getGlobalPC() == SecurityLevel.HIGH) {
			res = false;
		}
		return res;		
	}
	
	/**
	 * @param o
	 * @param field
	 * @param localForObject
	 * @param localForIndex
	 * @return
	 */
	boolean checkArrayWithGlobalPC(Object o, String field,
			String localForObject, String localForIndex) {
		boolean res = true;
		SecurityLevel localsAndGPC = joinWithGPC(joinLocals(localForObject, localForIndex));
		if (om.getFieldLevel(o, field) == SecurityLevel.LOW 
				&& localsAndGPC == SecurityLevel.HIGH) {
			res = false;
		}
		return res;		
	}
	
	/**
	 * @param o
	 * @param field
	 * @param localForObject
	 * @return
	 */
	public boolean checkArrayWithGlobalPC(Object o, String field,
			String localForObject) {
		boolean res = true;
		SecurityLevel localsAndGPC = joinWithGPC(lm.getLevel(localForObject));
		if (om.getFieldLevel(o, field) == SecurityLevel.LOW 
				&& localsAndGPC == SecurityLevel.HIGH) {
			res = false;
		}
		return res;	
	}

	
	//
	// Join operations
	//
	
	/**
	 * Joins the Levels of the given locals and the local pc
	 * @param stringList 
	 * @return
	 */
	protected SecurityLevel joinLocals(String... stringList) {
		SecurityLevel result = SecurityLevel.LOW;
		for (String op: stringList) {
			if (lm.getLevel(op) == SecurityLevel.HIGH) {
				result = SecurityLevel.HIGH;
			}
		}
		return result;
	}
	
	/**
	 * @param l
	 * @return
	 */
	protected SecurityLevel joinWithLPC(SecurityLevel l) {
		SecurityLevel result = SecurityLevel.LOW;
		if (lm.getLocalPC() == SecurityLevel.HIGH || l == SecurityLevel.HIGH) {
			result = SecurityLevel.HIGH;
		}
		return result;
	}
	
	/**
	 * @param l
	 * @return
	 */
	protected SecurityLevel joinWithGPC(SecurityLevel l) {
		SecurityLevel result = SecurityLevel.LOW;
		if (om.getGlobalPC() == SecurityLevel.HIGH || l == SecurityLevel.HIGH) {
			result = SecurityLevel.HIGH;
		}
		return result;
	}
	
	/**
	 * @param levels
	 * @return
	 */
	protected SecurityLevel joinLevels(SecurityLevel... levels) {
		SecurityLevel res = SecurityLevel.LOW;
		for (SecurityLevel l: levels) {
			if ( l == SecurityLevel.HIGH) {
				res = SecurityLevel.HIGH;
			}
		}
		return res;
	}


}

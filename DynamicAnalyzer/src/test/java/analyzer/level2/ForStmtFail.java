package analyzer.level2;

import org.junit.Before;
import org.junit.Test;
import util.exceptions.IFCError;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ForStmtFail {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}
	
	/**
	 * Would be an IFCError because of NSU, but since int res is
	 * not initialized, it's not. See also forStmtLocalTestFail.
	 */
	@SuppressWarnings("unused")
	@Test
	public void forStmtLocalTestSuccess() {
		
		logger.log(Level.INFO, "FOR STMT LOCAL TEST FAIL STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_i");
		hs.setLocalFromString("int_i", "HIGH");
		hs.addLocal("int_res");
		
		int res = 0;
		
		hs.checkCondition("123", "int_i");
		for (int i = 0;i < 1; i++) {
			
			hs.checkLocalPC("int_res");
			hs.setLocalToCurrentAssignmentLevel("int_res");
			res = 2;
			
			hs.exitInnerScope("123");
		}
		
		hs.close();
		
		logger.log(Level.INFO, "FOR STMT LOCAL TEST FAIL FINISHED");
	}

	/**
	 * Same test as forStmtLocalTestSuccess, but this time, int res gets initialized.
	 */
	@SuppressWarnings("unused")
	@Test(expected = IFCError.class)
	public void forStmtLocalTestFail() {
		
		logger.log(Level.INFO, "FOR STMT LOCAL TEST FAIL STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_i");
		hs.setLocalFromString("int_i", "HIGH");
		hs.addLocal("int_res");
		
		int res = 0;
		
		hs.setLocal("int_res", CurrentSecurityDomain.bottom());
		
		hs.checkCondition("123", "int_i");
		for (int i = 0;i < 1; i++) {
			
			hs.checkLocalPC("int_res");
			hs.setLocalToCurrentAssignmentLevel("int_res");
			res = 2;
			
			hs.exitInnerScope("123");
		}
		
		hs.close();
		
		logger.log(Level.INFO, "FOR STMT LOCAL TEST FAIL FINISHED");
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IFCError.class)
	public void forStmtFieldTestFail() {
		
		logger.log(Level.INFO, "FOR STMT FIELD TEST FAIL STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.addLocal("int_i");
		hs.setLocalFromString("int_i", "HIGH");
		hs.addFieldToObjectMap(this, "int_res");
		
		int i = 0; // Local
		int res; // Field
		
		hs.checkCondition("123", "int_i");
		for (i = 0;i < 1; i++) {
			
			hs.checkGlobalPC(this, "int_res");
			hs.setLevelOfField(this, "int_res");
			res = 2;
			
			hs.exitInnerScope("123");
		}
		
		hs.close();
		
		logger.log(Level.INFO, "FOR STMT FIELD TEST FAIL FINISHED");
	}

}

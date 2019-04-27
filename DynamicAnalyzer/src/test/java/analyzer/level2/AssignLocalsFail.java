package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import util.exceptions.IFCError;
import util.logging.L2Logger;


import java.util.logging.Level;
import java.util.logging.Logger;

public class AssignLocalsFail {
	
	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test(expected = IFCError.class)
	public void assignConstantToLocal() {
		
		logger.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_x", CurrentSecurityDomain.bottom());
		hs.pushLocalPC(CurrentSecurityDomain.top(), 123);
		// x = LOW, lpc = HIGH
		hs.checkLocalPC("int_x");
		hs.setLocalToCurrentAssignmentLevel("int_x");
		hs.popLocalPC(123);
		hs.close();
		
		logger.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	

	@Test//(expected = IFCError.class)
	public void assignLocalsToLocal() {
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_x");
		hs.addLocal("int_y");
		hs.addLocal("int_z", CurrentSecurityDomain.top());
		
		/*
		 *  Assign Locals to Local
		 *  int x = y + z;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.pushLocalPC(CurrentSecurityDomain.top(), 123);
		// x = LOW, lpc = HIGH
		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalLevel("int_x"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalLevel("int_y"));
		assertEquals(CurrentSecurityDomain.top(), hs.getLocalLevel("int_z"));
		assertEquals(CurrentSecurityDomain.top(), hs.getLocalPC());
		hs.joinLevelOfLocalAndAssignmentLevel("int_y");
		hs.joinLevelOfLocalAndAssignmentLevel("int_z");
		hs.checkLocalPC("int_x");
		hs.setLocalToCurrentAssignmentLevel("int_x");
		
		
	}
	
	/*
	 * TestSubClass xy is not initialized here, so no NSU IFCError.
	 */
	@SuppressWarnings("unused")
	@Test
	public void assignNewObjectToLocalSuccess() {
		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("TestSubClass_xy");
		
		TestSubClass xy;
		
		hs.pushLocalPC(CurrentSecurityDomain.top(), 123);
		
		hs.checkLocalPC("TestSubClass_xy");
		hs.setLocalToCurrentAssignmentLevel("TestSubClass_xy");
		xy = new TestSubClass();
		
		hs.popLocalPC(123);
		hs.close();	

		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST FINISHED");
		
	}
	
	/*
	 * TestSubClass xy is d initialized here, so no NSU policy forces
	 * an IFCError.
	 */
	@SuppressWarnings("unused")
	@Test(expected = IFCError.class)
	public void assignNewObjectToLocalFail() {
		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("TestSubClass_xy");
		hs.setLocal("TestSubClass_xy", CurrentSecurityDomain.bottom());
		
		TestSubClass xy;
		
		hs.pushLocalPC(CurrentSecurityDomain.top(), 123);
		hs.checkLocalPC("TestSubClass_xy");
		hs.setLocalToCurrentAssignmentLevel("TestSubClass_xy");
		xy = new TestSubClass();
		
		hs.popLocalPC(123);
		hs.close();	

		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST FINISHED");
		
	}
	
	
	
	/**
	 * Testing IFCError thrown by the NSU Policy:
	 * int high has high security value, and thus we cannot upgrade
	 * the security-value of res from low to high.
	 */
	@SuppressWarnings("unused")
	@Test
	public void assignMethodResultToLocalSuccess() {

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("TestSubClass_ts");
		hs.addLocal("int_res");
		hs.addLocal("int_high", CurrentSecurityDomain.top());
		
		TestSubClass ts = new TestSubClass();
		int res ;
		int high = 0;
		
		hs.checkCondition("123", "int_high");
		
		if (high == 0) {
		
			//hs.joinLevelOfLocalAndAssignmentLevel("TestSubClass_ts");	//assignment level is low here. why needed?!
			hs.checkLocalPC("int_res");
			hs.setLocalToCurrentAssignmentLevel("int_res"); //exception expected here!
			res = ts.methodWithConstReturn();	// res is low, ts
			hs.assignReturnLevelToLocal("int_res");
			
			hs.exitInnerScope("123");
		
		}
		
		hs.close();

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
	
	}
	
	
	@SuppressWarnings("unused")
	@Test(expected = IFCError.class)
	public void assignMethodResultToLocalFail() {

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		hs.addLocal("TestSubClass_ts");
		hs.addLocal("int_res");
		hs.addLocal("int_high", CurrentSecurityDomain.top());
		TestSubClass ts = new TestSubClass();
		int res ;
		int high = 0;
		
		hs.setLocal("int_res", CurrentSecurityDomain.bottom());
		
		hs.checkCondition("123", "int_high");
		
		if (high == 0) {
		
			// hs.joinLevelOfLocalAndAssignmentLevel("TestSubClass_ts");
			hs.checkLocalPC("int_res");
			hs.setLocalToCurrentAssignmentLevel("int_res"); //exception expected here!
			res = ts.methodWithConstReturn();
			hs.assignReturnLevelToLocal("int_res");
			
			hs.exitInnerScope("123");
		
		}
		
		hs.close();

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
	
	}
	@Test//(expected = IFCError.class)
	public void assignConstantAndLocalToLocal() {
		
		logger.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL FAIL TEST STARTED");
				
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		/*
		 * x++; or x += 1;  or x = x + 1;
		 */
		
		hs.addLocal("int_x");
		hs.pushLocalPC(CurrentSecurityDomain.top(), 123);
		hs.joinLevelOfLocalAndAssignmentLevel("int_x");
		hs.checkLocalPC("int_x");
		hs.setLocalToCurrentAssignmentLevel("int_x"); // Just ignore the constants
		
		hs.popLocalPC(123);
		hs.close();
				
		logger.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL FAIL TEST FINISHED");
	}

}

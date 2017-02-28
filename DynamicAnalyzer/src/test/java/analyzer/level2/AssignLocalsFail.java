package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import utils.exceptions.IllegalFlowException;
import utils.logging.L2Logger;


import java.util.logging.Level;
import java.util.logging.Logger;

public class AssignLocalsFail {
	
	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void assignConstantToLocal() {
		
		logger.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_x", SecurityLevel.bottom());
		hs.pushLocalPC(SecurityLevel.top(), 123);
		// x = LOW, lpc = HIGH
		hs.checkLocalPC("int_x");
		hs.setLevelOfLocal("int_x");
		hs.popLocalPC(123);
		hs.close();
		
		logger.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	

	@Test(expected = IllegalFlowException.class)
	public void assignLocalsToLocal() {
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_x");
		hs.addLocal("int_y");
		hs.addLocal("int_z", SecurityLevel.top());
		
		/*
		 *  Assign Locals to Local
		 *  int x = y + z;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.pushLocalPC(SecurityLevel.top(), 123);
		// x = LOW, lpc = HIGH
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_x"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_y"));
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("int_z"));
		assertEquals(SecurityLevel.top(), hs.getLocalPC());
		hs.joinLevelOfLocalAndAssignmentLevel("int_y");
		hs.joinLevelOfLocalAndAssignmentLevel("int_z");
		hs.checkLocalPC("int_x");
		hs.setLevelOfLocal("int_x");
		
		
	}
	
	/*
	 * TestSubClass xy is not initialized here, so no NSU IllegalFlowException.
	 */
	@SuppressWarnings("unused")
	@Test
	public void assignNewObjectToLocalSuccess() {
		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("TestSubClass_xy");
		
		TestSubClass xy;
		
		hs.pushLocalPC(SecurityLevel.top(), 123);
		
		hs.checkLocalPC("TestSubClass_xy");
		hs.setLevelOfLocal("TestSubClass_xy");
		xy = new TestSubClass();
		
		hs.popLocalPC(123);
		hs.close();	

		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST FINISHED");
		
	}
	
	/*
	 * TestSubClass xy is d initialized here, so no NSU policy forces
	 * an IllegalFlowException.
	 */
	@SuppressWarnings("unused")
	@Test(expected = IllegalFlowException.class)
	public void assignNewObjectToLocalFail() {
		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("TestSubClass_xy");
		hs.initializeVariable("TestSubClass_xy");
		
		TestSubClass xy;
		
		hs.pushLocalPC(SecurityLevel.top(), 123);
		hs.checkLocalPC("TestSubClass_xy");
		hs.setLevelOfLocal("TestSubClass_xy");
		xy = new TestSubClass();
		
		hs.popLocalPC(123);
		hs.close();	

		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST FINISHED");
		
	}
	
	
	
	/**
	 * Testing IllegalFlowException thrown by the NSU Policy:
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
		hs.addLocal("int_high", SecurityLevel.top());
		
		TestSubClass ts = new TestSubClass();
		int res ;
		int high = 0;
		
		hs.checkCondition("123", "int_high");
		
		if (high == 0) {
		
			//hs.joinLevelOfLocalAndAssignmentLevel("TestSubClass_ts");	//assignment level is low here. why needed?!
			hs.checkLocalPC("int_res");
			hs.setLevelOfLocal("int_res"); //exception expected here!
			res = ts.methodWithConstReturn();	// res is low, ts
			hs.assignReturnLevelToLocal("int_res");
			
			hs.exitInnerScope("123");
		
		}
		
		hs.close();

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
	
	}
	
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalFlowException.class)
	public void assignMethodResultToLocalFail() {

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		hs.addLocal("TestSubClass_ts");
		hs.addLocal("int_res");
		hs.addLocal("int_high", SecurityLevel.top());
		TestSubClass ts = new TestSubClass();
		int res ;
		int high = 0;
		
		hs.initializeVariable("int_res");
		
		hs.checkCondition("123", "int_high");
		
		if (high == 0) {
		
			// hs.joinLevelOfLocalAndAssignmentLevel("TestSubClass_ts");
			hs.checkLocalPC("int_res");
			hs.setLevelOfLocal("int_res"); //exception expected here!
			res = ts.methodWithConstReturn();
			hs.assignReturnLevelToLocal("int_res");
			
			hs.exitInnerScope("123");
		
		}
		
		hs.close();

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
	
	}
	@Test(expected = IllegalFlowException.class)
	public void assignConstantAndLocalToLocal() {
		
		logger.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL FAIL TEST STARTED");
				
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		/*
		 * x++; or x += 1;  or x = x + 1;
		 */
		
		hs.addLocal("int_x");
		hs.pushLocalPC(SecurityLevel.top(), 123);
		hs.joinLevelOfLocalAndAssignmentLevel("int_x");
		hs.checkLocalPC("int_x");
		hs.setLevelOfLocal("int_x"); // Just ignore the constants
		
		hs.popLocalPC(123);
		hs.close();
				
		logger.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL FAIL TEST FINISHED");
	}

}

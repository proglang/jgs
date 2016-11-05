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
		hs.addLocal("int_x", SecurityLevel.bottom());
		hs.pushLocalPC(SecurityLevel.top(), 123);
		// x = LOW, lpc = HIGH
		hs.setLevelOfLocal("int_x");
		hs.popLocalPC(123);
		hs.close();
		
		logger.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	

	@Test(expected = IllegalFlowException.class)
	public void assignLocalsToLocal() {
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
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
		hs.setLevelOfLocal("int_x");
		
		
	}
	
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalFlowException.class)
	public void assignNewObjectToLocal() {
		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.addLocal("TestSubClass_xy");
		
		TestSubClass xy;
		
		hs.pushLocalPC(SecurityLevel.top(), 123);
		
		hs.setLevelOfLocal("TestSubClass_xy");
		xy = new TestSubClass();
		
		hs.popLocalPC(123);
		hs.close();	

		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST FINISHED");
		
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalFlowException.class)
	public void assignMethodResultToLocal() {

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		
		hs.addLocal("TestSubClass_ts");
		hs.addLocal("int_res");
		hs.addLocal("int_high", SecurityLevel.top());
		TestSubClass ts = new TestSubClass();
		int res ;
		int high = 0;
		
		hs.checkCondition("123", "int_high");
		
		if (high == 0) {
		
			hs.joinLevelOfLocalAndAssignmentLevel("TestSubClass_ts");
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
		
		/*
		 * x++; or x += 1;  or x = x + 1;
		 */
		
		hs.addLocal("int_x");
		hs.pushLocalPC(SecurityLevel.top(), 123);
		hs.joinLevelOfLocalAndAssignmentLevel("int_x");
		hs.setLevelOfLocal("int_x"); // Just ignore the constants
		
		hs.popLocalPC(123);
		hs.close();
				
		logger.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL FAIL TEST FINISHED");
	}

}

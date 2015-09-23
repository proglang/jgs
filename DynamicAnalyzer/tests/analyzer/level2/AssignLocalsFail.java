package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import tests.testClasses.TestSubClass;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import utils.exceptions.IllegalFlowException;

public class AssignLocalsFail {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void assignConstantToLocal() {
		
		LOGGER.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x", SecurityLevel.LOW);
		hs.setLocalPC(SecurityLevel.HIGH);
		// x = LOW, lpc = HIGH
		hs.setLevelOfLocal("int_x");
		hs.close();
		
		LOGGER.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	

	@Test(expected = IllegalFlowException.class)
	public void assignLocalsToLocal() {
		
		LOGGER.log(Level.INFO, "ASSIGN LOCALS TO LOCAL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x");
		hs.addLocal("int_y");
		hs.addLocal("int_z", SecurityLevel.HIGH);
		
		/*
		 *  Assign Locals to Local
		 *  int x = y + z;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.setLocalPC(SecurityLevel.HIGH);
		// x = LOW, lpc = HIGH
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_x"));
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_y"));
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("int_z"));
		assertEquals(SecurityLevel.HIGH, hs.getLocalPC());
		hs.addLevelOfLocal("int_y");
		hs.addLevelOfLocal("int_z");
		hs.setLevelOfLocal("int_x");
		
		
	}
	
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalFlowException.class)
	public void assignNewObjectToLocal() {
		LOGGER.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("TestSubClass_xy");
		
		TestSubClass xy;
		
		hs.setLocalPC(SecurityLevel.HIGH);
		
		hs.setLevelOfLocal("TestSubClass_xy");
		xy = new TestSubClass();
		
		
	    hs.close();	

		LOGGER.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST FINISHED");
		
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalFlowException.class)
	public void assignMethodResultToLocal() {

		LOGGER.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		hs.addLocal("TestSubClass_ts");
		hs.addLocal("int_res");
		hs.addLocal("int_high", SecurityLevel.HIGH);
		TestSubClass ts = new TestSubClass();
		int res ;
		int high = 0;
		
		hs.checkCondition("int_high");
		if (high == 0) {
		
			hs.addLevelOfLocal("TestSubClass_ts");
			hs.setLevelOfLocal("int_res");
			res = ts.methodWithConstReturn();
			hs.assignReturnLevelToLocal("int_res");
			
			hs.exitInnerScope();
		
		}
		
		hs.close();

		LOGGER.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
	
	}
	
	@Test(expected = IllegalFlowException.class)
	public void assignConstantAndLocalToLocal() {
		
		LOGGER.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL FAIL TEST STARTED");
				
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * x++; or x += 1;  or x = x + 1;
		 */
		
		hs.addLocal("int_x");
		hs.setLocalPC(SecurityLevel.HIGH);
		hs.addLevelOfLocal("int_x");
		hs.setLevelOfLocal("int_x"); // Just ignore the constants
		
		hs.close();
				
		LOGGER.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL FAIL TEST FINISHED");
	}

}

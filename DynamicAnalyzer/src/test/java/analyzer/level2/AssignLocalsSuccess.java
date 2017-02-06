package analyzer.level2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import utils.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AssignLocalsSuccess {
	
	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void assignConstantToLocal() {
		
		logger.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		hs.initHandleStmtUtils(false);
		hs.addLocal("int_x", SecurityLevel.bottom());
		
		/*
		 *  int x = c;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign level of lpc to local
		 */
		// x = LOW, lpc = LOW
		// hs.checkLocalPC("int_x");
		assertEquals(SecurityLevel.bottom(), hs.setLevelOfLocal("int_x")); 
		
		hs.makeLocal("int_x", "HIGH");
		// x = HIGH, lpc = LOW
		assertEquals(SecurityLevel.bottom(), hs.setLevelOfLocal("int_x")); 
		
		hs.makeLocal("int_x", "HIGH");
		hs.pushLocalPC(SecurityLevel.top(), 123);
		//x = HIGH,lpc = HIGH
		assertEquals(SecurityLevel.top(), hs.setLevelOfLocal("int_x")); 
		
		hs.popLocalPC(123);
		hs.close();

		logger.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignLocalsToLocal() {
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		hs.addLocal("int_x");
		hs.addLocal("int_y");
		hs.addLocal("int_z", SecurityLevel.top());
		
		/*
		 *  Assign Locals to Local
		 *  int x = y + z;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.pushLocalPC(SecurityLevel.bottom(), 123);
		// x = LOW, lpc = LOW
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_x"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_y"));
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("int_z"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_y"));
		assertEquals(SecurityLevel.top(), hs.joinLevelOfLocalAndAssignmentLevel("int_z"));
		assertEquals(SecurityLevel.top(), hs.setLevelOfLocal("int_x"));
		
		hs.makeLocal("int_z", "LOW");
		// x = HIGH, lpc = LOW
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("int_x"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_y"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_z"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_y"));
		assertEquals(SecurityLevel.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_z"));
		assertEquals(SecurityLevel.bottom(), hs.setLevelOfLocal("int_x"));
		
		hs.pushLocalPC(SecurityLevel.top(), 123);
		hs.makeLocal("int_x", "HIGH");
		// x = HIGH, lpc = HIGH
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("int_x"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_y"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_z"));
		assertEquals(SecurityLevel.top(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_y"));
		assertEquals(SecurityLevel.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_z"));
		assertEquals(SecurityLevel.top(), hs.setLevelOfLocal("int_x"));
		
		hs.popLocalPC(123);
		hs.popLocalPC(123);
		hs.close();	

		logger.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignFieldToLocal() {
		
		logger.log(Level.INFO, "ASSIGN FIELD TO LOCAL TEST STARTED");
	    
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);

		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "String_field");
		hs.addLocal("String_local");
		
		/*
		 * Assign Field to Local
		 * 1. Check if Level(local) >= lpc
		 * 2. Assign Level(field) to Level(local)
		 */
		// local = LOW, lpc = LOW, field = LOW
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("String_local"));
		assertEquals(SecurityLevel.bottom(), hs.getFieldLevel(this, "String_field"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.joinLevelOfFieldAndAssignmentLevel(this, "String_field"));
		assertEquals(SecurityLevel.bottom(), hs.setLevelOfLocal("String_local"));
		
		// local = HIGH, lpc = LOW, field = LOW
		hs.makeLocal("String_local", "HIGH");
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("String_local"));
		assertEquals(SecurityLevel.bottom(), hs.getFieldLevel(this, "String_field"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.joinLevelOfFieldAndAssignmentLevel(this, "String_field"));
		assertEquals(SecurityLevel.bottom(), hs.setLevelOfLocal("String_local"));
		
		// local = LOW, lpc = LOW, field = LOW
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("String_local"));
		assertEquals(SecurityLevel.bottom(), hs.getFieldLevel(this, "String_field"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.joinLevelOfFieldAndAssignmentLevel(this, "String_field"));
		assertEquals(SecurityLevel.bottom(), hs.setLevelOfLocal("String_local"));
		
		hs.close();	

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignNewObjectToLocal() {
		
		logger.log(Level.INFO, "ASSIGN FIELD TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		hs.addLocal("TestSubClass_xy");
		
		/*
		 *  Assign new Object
		 *  check(xy) >= lpc
		 *  lpc -> xy
		 *  add new Object to ObjectMap
		 */
		
		
		TestSubClass xy = new TestSubClass();
		assertTrue(hs.containsObjectInObjectMap(xy));
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("TestSubClass_xy"));
		
		hs.setLevelOfLocal("TestSubClass_xy", SecurityLevel.top());
		hs.setLevelOfLocal("TestSubClass_xy");
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("TestSubClass_xy"));
		
		hs.close();	

		logger.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL TEST FINISHED");
	}
	
	@SuppressWarnings("unused")
	@Test
	public void assignMethodResultToLocal() {

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		
		/*
		 * Assign method (result)
		 */
		int res;
		TestSubClass xy = new TestSubClass();
		hs.addLocal("int_res");
		hs.addLocal("TestSubClass_xy");
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_res"));

		hs.joinLevelOfLocalAndAssignmentLevel("TestSubClass_xy");
		hs.checkLocalPC("int_res");
		hs.setLevelOfLocal("int_res");
		res = xy.methodWithConstReturn();
		hs.assignReturnLevelToLocal("int_res");
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_res"));

		hs.joinLevelOfLocalAndAssignmentLevel("TestSubClass_xy");
		hs.checkLocalPC("int_res");
		hs.setLevelOfLocal("int_res");
		res = xy.methodWithLowLocalReturn();
		hs.assignReturnLevelToLocal("int_res");
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_res"));

		hs.joinLevelOfLocalAndAssignmentLevel("TestSubClass_xy");
		hs.checkLocalPC("int_res");
		hs.setLevelOfLocal("int_res");
		res = xy.methodWithHighLocalReturn();
		hs.assignReturnLevelToLocal("int_res");
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("int_res"));
		
		hs.close();	
	    

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignArgumentToLocal() {

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		
		/*
		 * Assign argument
		 */
		
		hs.addLocal("int_a");
		hs.addLocal("int_b", SecurityLevel.top());
		hs.addLocal("int_c");
		
		hs.addLocal("int_res");
		
		hs.storeArgumentLevels("int_a", "int_b", "int_c");

		hs.assignArgumentToLocal(0, "int_res");
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_res"));
		hs.assignArgumentToLocal(1, "int_res");
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("int_res"));
		hs.assignArgumentToLocal(2, "int_res");
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_res"));
		
		hs.close();	
	    

		logger.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@SuppressWarnings("unused")
	@Test
	public void assignConstantAndLocalToLocal() {
		
		logger.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL SUCCESS TEST STARTED");
				
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		
		/*
		 * x++; or x += 1;  or x = x + 1;
		 */
		
		hs.addLocal("int_x");
		hs.addLocal("int_res");
		int x = 0;

		hs.joinLevelOfLocalAndAssignmentLevel("int_x");
		hs.checkLocalPC("int_res");
		hs.setLevelOfLocal("int_res"); // Just ignore the constants
		x++;
		
		hs.close();
				
		logger.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL SUCCESS TEST FINISHED");
	}

}

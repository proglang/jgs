package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Logger;
import tests.testClasses.TestSubClass;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

public class AssignLocalsSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void assignConstantToLocal() {
		
		LOGGER.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x", SecurityLevel.LOW);
		
		/*
		 *  int x = c;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign level of lpc to local
		 */
		assertEquals(SecurityLevel.LOW, hs.assignLocalsToLocal("int_x")); // x = LOW, lpc = LOW
		
		hs.makeLocalHigh("int_x");
		assertEquals(SecurityLevel.LOW, hs.assignLocalsToLocal("int_x")); // x = HIGH, lpc = LOW
		
		hs.makeLocalHigh("int_x");
		hs.setLocalPC(SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, hs.assignLocalsToLocal("int_x")); // x = HIGH, lpc = HIGH
		
	    hs.close();

	    LOGGER.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	
	@Test
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
		hs.setLocalPC(SecurityLevel.LOW);
		// x = LOW, lpc = LOW
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_x"));
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_y"));
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("int_z"));
		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		assertEquals(SecurityLevel.HIGH, hs.assignLocalsToLocal("int_x", "int_y", "int_z"));
		
		hs.makeLocalLow("int_z");
		// x = HIGH, lpc = LOW
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("int_x"));
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_y"));
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_z"));
		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		assertEquals(SecurityLevel.LOW, hs.assignLocalsToLocal("int_x", "int_y", "int_z"));
		
		hs.setLocalPC(SecurityLevel.HIGH);
		hs.makeLocalHigh("int_x");
		// x = HIGH, lpc = HIGH
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("int_x"));
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_y"));
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_z"));
		assertEquals(SecurityLevel.HIGH, hs.getLocalPC());
		assertEquals(SecurityLevel.HIGH, hs.assignLocalsToLocal("int_x", "int_y", "int_z"));
		
	    hs.close();	

	    LOGGER.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignFieldToLocal() {
		
		LOGGER.log(Level.INFO, "ASSIGN FIELD TO LOCAL TEST STARTED");
	    
		HandleStmtForTests hs = new HandleStmtForTests();

		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "String_field");
		hs.addLocal("String_local");
		
		/*
		 * Assign Field to Local
		 * 1. Check if Level(local) >= lpc
		 * 2. Assign Level(field) to Level(local)
		 */
		// local = LOW, lpc = LOW, field = LOW
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("String_local"));
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "String_field"));
		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		assertEquals(SecurityLevel.LOW, hs.assignFieldToLocal(this, "String_local", "String_field"));
		
		// local = HIGH, lpc = LOW, field = LOW
		hs.makeLocalHigh("String_local");
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("String_local"));
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "String_field"));
		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		assertEquals(SecurityLevel.LOW, hs.assignFieldToLocal(this, "String_local", "String_field"));
		
		// local = LOW, lpc = LOW, field = LOW
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("String_local"));
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "String_field"));
		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		assertEquals(SecurityLevel.LOW, hs.assignFieldToLocal(this, "String_local", "String_field"));
		
	    hs.close();	

	    LOGGER.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignNewObjectToLocal() {
		
		LOGGER.log(Level.INFO, "ASSIGN FIELD TO LOCAL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("TestSubClass_xy");
		hs.assignLocalsToLocal("TestSubClass_xy");
		
		/*
		 *  Assign new Object
		 *  check(xy) >= lpc
		 *  lpc -> xy
		 *  add new Object to ObjectMap
		 */
		
		
		TestSubClass xy = new TestSubClass();
		assertTrue(hs.containsObjectInObjectMap(xy));
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("TestSubClass_xy"));
		
		hs.setLocalLevel("TestSubClass_xy", SecurityLevel.HIGH);
		hs.assignLocalsToLocal("TestSubClass_xy");
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("TestSubClass_xy"));
		
	    hs.close();	

	    LOGGER.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL TEST FINISHED");
	}
	
	@SuppressWarnings("unused")
	@Test
	public void assignMethodResultToLocal() {

		LOGGER.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * Assign method (result)
		 */
		int res;
		TestSubClass xy = new TestSubClass();
		hs.addLocal("int_res");
		hs.addLocal("TestSubClass_xy");
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_res"));

		hs.assignLocalsToLocal("int_res", "TestSubClass_xy");
		res = xy.methodWithConstReturn();
		hs.assignReturnLevelToLocal("int_res");
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_res"));
		
		hs.assignLocalsToLocal("int_res", "TestSubClass_xy");
		res = xy.methodWithLowLocalReturn();
		hs.assignReturnLevelToLocal("int_res");
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_res"));
		
		hs.assignLocalsToLocal("int_res", "TestSubClass_xy");
		res = xy.methodWithHighLocalReturn();
		hs.assignReturnLevelToLocal("int_res");
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("int_res"));
		
	    hs.close();	
	    

	    LOGGER.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignArgumentToLocal() {

		LOGGER.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * Assign argument
		 */
		
		hs.addLocal("int_a");
		hs.addLocal("int_b", SecurityLevel.HIGH);
		hs.addLocal("int_c");
		
		hs.addLocal("int_res");
		
		hs.storeArgumentLevels("int_a", "int_b", "int_c");

		hs.assignArgumentToLocal(0, "int_res");
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_res"));
		hs.assignArgumentToLocal(1, "int_res");
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("int_res"));
		hs.assignArgumentToLocal(2, "int_res");
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_res"));
		
	    hs.close();	
	    

	    LOGGER.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@SuppressWarnings("unused")
	@Test
	public void assignConstantAndLocalToLocal() {
		
		LOGGER.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL SUCCESS TEST STARTED");
				
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * x++; or x += 1;  or x = x + 1;
		 */
		
		hs.addLocal("int_x");
		int x = 0;
		
		hs.assignLocalsToLocal("int_x", "int_x"); // Just ignore the constants
		x++;
		
		hs.close();
				
		LOGGER.log(Level.INFO, "ASSIGN CONSTANT AND LOCAL TO LOCAL SUCCESS TEST FINISHED");
	}

}

package tests.handleStmtTests;

import static org.junit.Assert.*;
import tests.testClasses.TestSubClass;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;

public class AssignLocalsSuccess {
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void assignConstantToLocal() {
		
		System.out.println("ASSIGN CONSTANT TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
		
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

		System.out.println("ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignLocalsToLocal() {
		
		System.out.println("ASSIGN LOCALS TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
		
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

		System.out.println("ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignFieldToLocal() {
		
		System.out.println("ASSIGN FIELD TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    
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

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignNewObjectToLocal() {
		
		System.out.println("ASSIGN FIELD TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
		
		HandleStmtForTests hs = new HandleStmtForTests();
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

		System.out.println("ASSIGN NEW OBJECT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignMethodResultToLocal() {

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * Assign method (result)
		 */
		int res;
		TestSubClass xy = new TestSubClass();
		hs.addLocal("int_res");
		hs.addLocal("TestSubClass_xy");
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_res"));
		
		// TODO
		res = xy.methodWithConstReturn();
		res = xy.methodWithLowLocalReturn();
		res = xy.methodWithHighLocalReturn();
		
	    hs.close();	
	    

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignArgumentToLocal() {

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * Assign argument
		 */

		
	    hs.close();	
	    

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}

}

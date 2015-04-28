package tests.handleStmtTests;

import static org.junit.Assert.*;
import main.level2.TestSubClass;

import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;
import analyzer.level2.storage.ObjectMap;

public class assignLocalsSuccess {

	@Test
	public void assignConstantToLocal() {
		
		System.out.println("ASSIGN CONSTANT TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x", Level.LOW);
		
		/*
		 *  int x = c;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign level of lpc to local
		 */
		assertEquals(Level.LOW, hs.assignLocalsToLocal("int_x")); // x = LOW, lpc = LOW
		
		hs.makeLocalHigh("int_x");
		assertEquals(Level.LOW, hs.assignLocalsToLocal("int_x")); // x = HIGH, lpc = LOW
		
		hs.makeLocalHigh("int_x");
		hs.setLocalPC(Level.HIGH);
		assertEquals(Level.HIGH, hs.assignLocalsToLocal("int_x")); // x = HIGH, lpc = HIGH
		
	    hs.close();

		System.out.println("ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignLocalsToLocal() {
		
		System.out.println("ASSIGN LOCALS TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x");
		hs.addLocal("int_y");
		hs.addLocal("int_z", Level.HIGH);
		
		/*
		 *  Assign Locals to Local
		 *  int x = y + z;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.setLocalPC(Level.LOW);
		// x = LOW, lpc = LOW
		assertEquals(Level.LOW, hs.getLocalLevel("int_x"));
		assertEquals(Level.LOW, hs.getLocalLevel("int_y"));
		assertEquals(Level.HIGH, hs.getLocalLevel("int_z"));
		assertEquals(Level.LOW, hs.getLocalPC());
		assertEquals(Level.HIGH, hs.assignLocalsToLocal("int_x", "int_y", "int_z"));
		
		hs.makeLocalLow("int_z");
		// x = HIGH, lpc = LOW
		assertEquals(Level.HIGH, hs.getLocalLevel("int_x"));
		assertEquals(Level.LOW, hs.getLocalLevel("int_y"));
		assertEquals(Level.LOW, hs.getLocalLevel("int_z"));
		assertEquals(Level.LOW, hs.getLocalPC());
		assertEquals(Level.LOW, hs.assignLocalsToLocal("int_x", "int_y", "int_z"));
		
		hs.setLocalPC(Level.HIGH);
		hs.makeLocalHigh("int_x");
		// x = HIGH, lpc = HIGH
		assertEquals(Level.HIGH, hs.getLocalLevel("int_x"));
		assertEquals(Level.LOW, hs.getLocalLevel("int_y"));
		assertEquals(Level.LOW, hs.getLocalLevel("int_z"));
		assertEquals(Level.HIGH, hs.getLocalPC());
		assertEquals(Level.HIGH, hs.assignLocalsToLocal("int_x", "int_y", "int_z"));
		
	    hs.close();	

		System.out.println("ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignFieldToLocal() {
		
		System.out.println("ASSIGN FIELD TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());
	    
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
		assertEquals(Level.LOW, hs.getLocalLevel("String_local"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "String_field"));
		assertEquals(Level.LOW, hs.getLocalPC());
		assertEquals(Level.LOW, hs.assignFieldToLocal(this, "String_local", "String_field"));
		
		// local = HIGH, lpc = LOW, field = LOW
		hs.makeLocalHigh("String_local");
		assertEquals(Level.HIGH, hs.getLocalLevel("String_local"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "String_field"));
		assertEquals(Level.LOW, hs.getLocalPC());
		assertEquals(Level.LOW, hs.assignFieldToLocal(this, "String_local", "String_field"));
		
		// local = LOW, lpc = LOW, field = LOW
		assertEquals(Level.LOW, hs.getLocalLevel("String_local"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "String_field"));
		assertEquals(Level.LOW, hs.getLocalPC());
		assertEquals(Level.LOW, hs.assignFieldToLocal(this, "String_local", "String_field"));
		
	    hs.close();	

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignNewObjectToLocal() {
		
		System.out.println("ASSIGN FIELD TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());
		
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
		assertEquals(Level.LOW, hs.getLocalLevel("TestSubClass_xy"));
		
		hs.setLocalLevel("TestSubClass_xy", Level.HIGH);
		hs.assignLocalsToLocal("TestSubClass_xy");
		assertEquals(Level.LOW, hs.getLocalLevel("TestSubClass_xy"));
		
	    hs.close();	

		System.out.println("ASSIGN NEW OBJECT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignMethodResultToLocal() {

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * Assign method (result)
		 */
		int res;
		TestSubClass xy = new TestSubClass();
		hs.addLocal("int_res");
		hs.addLocal("TestSubClass_xy");
		assertEquals(Level.LOW, hs.getLocalLevel("int_res"));
		
		// TODO
		res = xy.methodWithConstReturn();
		res = xy.methodWithLowLocalReturn();
		res = xy.methodWithHighLocalReturn();
		
	    hs.close();	
	    
	    assertEquals(0, m.sizeOfLocalMapStack());

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}
	
	@Test
	public void assignArgumentToLocal() {

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * Assign argument
		 */

		
	    hs.close();	
	    
	    assertEquals(0, m.sizeOfLocalMapStack());

		System.out.println("ASSIGN METHOD RESULT TO LOCAL TEST FINISHED");
	}

}

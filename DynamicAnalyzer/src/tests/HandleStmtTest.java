package tests;

import static org.junit.Assert.*;
import main.level2.TestSubClass;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import exceptions.IllegalFlowException;
import analyzer.level2.HandleStmt;
import analyzer.level2.Level;

public class HandleStmtTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void assignLocalTest() {
		
		System.out.println("ASSIGN LOCAL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_x");
		hs.addLocal("int_y");
		hs.addLocal("int_z", Level.HIGH);
		hs.addLocal("TestSubClass_xy");
		
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
		
		hs.makeLocalLow("int_x"); // x = LOW, lpc = HIGH
		//thrown.expect(IllegalFlowException.class); TODO
		hs.assignLocalsToLocal("int_x");
		
		
		
		/*
		 *  Assign Locals to Local
		 *  int x = y + z;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.setLocalPC(Level.LOW);
		assertEquals(Level.HIGH, hs.assignLocalsToLocal("int_x", "int_y", "int_z"));
		
		hs.makeLocalLow("int_z");
		assertEquals(Level.LOW, hs.assignLocalsToLocal("int_x", "int_y", "int_z"));
		
		hs.setLocalPC(Level.HIGH);
		assertEquals(Level.HIGH, hs.assignLocalsToLocal("int_x", "int_y", "int_z"));
		
		/*
		 * Assign Field to Local
		 */
		hs.setLocalPC(Level.LOW);
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "String_field");
		hs.addLocal("String_local");
		assertEquals(Level.LOW, hs.assignFieldsToLocal(this, "String_local", "String_field"));
		
		/*
		 *  Assign new Object
		 *  check(xy) >= lpc
		 *  lpc -> xy
		 *  add new Object to ObjectMap
		 */
		hs.setLocalPC(Level.LOW);
		hs.assignLocalsToLocal("TestSubClass_xy");
		TestSubClass xy = new TestSubClass();
		assertTrue(hs.containsObjectInObjectMap(xy));
		assertEquals(Level.LOW, hs.getLocalLevel("TestSubClass_xy"));
		
		hs.setLocalLevel("TestSubClass_xy", Level.HIGH);
		hs.assignLocalsToLocal("TestSubClass_xy");
		assertEquals(Level.LOW, hs.getLocalLevel("TestSubClass_xy"));
		
		/*
		 * Assign method (result)
		 */
		int res;
		hs.addLocal("int_res");
		assertEquals(Level.LOW, hs.getLocalLevel("int_res"));
		
		// TODO
		res = xy.methodWithConstReturn();
		res = xy.methodWithLowLocalReturn();
		res = xy.methodWithHighLocalReturn();
		
		System.out.println("ASSIGN LOCAL TEST FINISHED");
	}

	@Test
	public void invokeStmtTest() {
		
		System.out.println("INVOKE TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		
		/*
		 * Invoke New Stmt
		 * 1. Add new Object to ObjectMap
		 * 2. Add Fields to ObjectMap
		 */
		TestSubClass xy = new TestSubClass();
		assertTrue(hs.containsObjectInObjectMap(xy));
		assertEquals(1, hs.getNumberOfFields(xy));
		
		/*
		 *  Static Invoke
		 */
		// TODO: shold be the same as New Stmt
		
		/*
		 *  Invoke Method
		 *  1. Create new LocalMap
		 *  2. Add Locals to LocalMap
		 */
		
		System.out.println("INVOKE TEST FINISHED");
	}

	@Test
	public void assignFieldTest() {
		
		System.out.println("ASSIGN FIELD TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
		hs.addLocal("int_var1", Level.LOW);
		hs.addLocal("int_var2", Level.LOW);
		hs.addLocal("int_var3", Level.HIGH);
		hs.addFieldToObjectMap(this, "int_field");
		
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_field"));
		assertEquals(Level.LOW, hs.getLocalLevel("int_var1"));
		assertEquals(Level.LOW, hs.getLocalLevel("int_var2"));
		
		/* Assign Constant to Field
		 *  int field = c;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign level of lpc to field
		 */
		assertEquals(Level.LOW, hs.assignLocalsToField(this, "int_field"));
		hs.setLocalPC(Level.HIGH);
		assertEquals(Level.HIGH, hs.assignLocalsToField(this, "int_field"));
		
		/* Assign Local To Field
		 *  int field = var1 + var2;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.setLocalPC(Level.LOW);
		assertEquals(Level.LOW, hs.assignLocalsToField(this, "int_field", "int_var1"));
		assertEquals(Level.LOW, hs.assignLocalsToField(this, "int_field", "int_var1", "int_var2"));
		hs.setLocalLevel("int_var2", Level.HIGH);
		assertEquals(Level.HIGH, hs.assignLocalsToField(this, "int_field", "int_var1", "int_var2"));
		
		/*
		 * Assign new Object
		 */
		
		/*
		 * Assign method (return)
		 */
		
		// TODO What happens if a local doesn't exist?
		
		System.out.println("ASSIGN FIELD TEST FINISHED");
	}
	
	@Test
	public void ifStmtTest() {	
	}
	
	@Test
	public void ifWhileTest() {	
	}
	
	@Test
	public void ifSwitchTest() {	
	}
	
	@Test
	public void returnStmtTest() {
		
		System.out.println("RETURN TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
		hs.setLocalPC(Level.LOW);
		assertEquals(Level.LOW, hs.addFieldToObjectMap(this, "int_resField1"));
		assertEquals(Level.LOW, hs.addFieldToObjectMap(this, "int_resField2"));
		assertEquals(Level.LOW, hs.addFieldToObjectMap(this, "int_resField3"));
		
		
		int resField1;
		int resField2;
		int resField3;
		
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField1"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField2"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField3"));
		
		TestSubClass tsc = new TestSubClass();
		tsc.methodWithConstReturn();
		assertEquals(Level.LOW, hs.getActualReturnLevel());
		tsc.methodWithLowLocalReturn();
		assertEquals(Level.LOW, hs.getActualReturnLevel());
		tsc.methodWithHighLocalReturn();
		assertEquals(Level.HIGH, hs.getActualReturnLevel());
		
		
		tsc.method();
		
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField1"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField2"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField3"));
		
		System.out.println("RETURN TEST FINISHED");
	}
	
	/*
	 * TESTS OF INTERNAL METHODS
	 */
	
	@Test
	public void checkLocalPCTest() {
		System.out.println("LOCAL PC TEST STARTED");

		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_x");
		
		// Level(x) = LOW, Level(lpc) = LOW
		assertEquals(true, hs.checkLocalPC("int_x"));
		
		// Level(x) = HIGH, Level(lpc) = LOW
		hs.makeLocalHigh("int_x");
		assertEquals(true, hs.checkLocalPC("int_x"));
		
		// Level(x) = HIGH, Level(lpc) = HIGH
		hs.setLocalPC(Level.HIGH);
		assertEquals(true, hs.checkLocalPC("int_x"));
		
		// Level(x) = LOW, Level(lpc) = HIGH
		hs.makeLocalLow("int_x");
		assertEquals(false, hs.checkLocalPC("int_x"));
		
		System.out.println("LOCAL PC TEST FINISHED");
	}
	
	@Test
	public void joinLocalsTest() {
		System.out.println("JOIN LOCALS TEST STARTED");

		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_x", Level.LOW);
		hs.addLocal("int_y", Level.HIGH);
		hs.addLocal("int_z", Level.LOW);
		assertEquals(Level.LOW, hs.joinLocals("int_x"));
		assertEquals(Level.HIGH, hs.joinLocals("int_x", "int_y"));		
		assertEquals(Level.HIGH, hs.joinLocals("int_x", "int_y", "int_z"));
		
		
		hs.setLocalPC(Level.HIGH);
		assertEquals(Level.HIGH, hs.joinLocals("int_x"));

		System.out.println("JOIN LOCALS TEST FINISHED");
	}
	
	@Test
	public void joinFieldsTest() {
		System.out.println("JOIN FIELDS TEST STARTED");

		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "int_x");
		hs.addFieldToObjectMap(this, "int_y");
		
		// TODO

		System.out.println("JOIN FIELDS TEST FINISHED");
	}
}

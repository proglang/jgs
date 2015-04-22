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
		
		/*
		 *  int x = c;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign level of lpc to local
		 */
		assertEquals(Level.LOW, hs.assignLocal("int_x")); // x = LOW, lpc = LOW
		
		hs.makeLocalHigh("int_x");
		assertEquals(Level.LOW, hs.assignLocal("int_x")); // x = HIGH, lpc = LOW
		
		hs.makeLocalHigh("int_x");
		hs.setLocalPC(Level.HIGH);
		assertEquals(Level.HIGH, hs.assignLocal("int_x")); // x = HIGH, lpc = HIGH
		
		hs.makeLocalLow("int_x"); // x = LOW, lpc = HIGH
		//thrown.expect(IllegalFlowException.class); TODO
		hs.assignLocal("int_x");
		
		
		
		/*
		 *  int x = y + z;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.setLocalPC(Level.LOW);
		assertEquals(Level.HIGH, hs.assignLocal("int_x", "int_y", "int_z"));
		
		hs.makeLocalLow("int_z");
		assertEquals(Level.LOW, hs.assignLocal("int_x", "int_y", "int_z"));
		
		hs.setLocalPC(Level.HIGH);
		assertEquals(Level.HIGH, hs.assignLocal("int_x", "int_y", "int_z"));
		
		/*
		 *  Assign new Object
		 *  
		 */
		TestSubClass xy = new TestSubClass();
		
		/*
		 * Assign method (result)
		 */
		
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
		 *
		 */
		TestSubClass xy = new TestSubClass();
		assertTrue(hs.containsObjectInObjectMap(xy));
		assertEquals(1, hs.getNumberOfFields(xy));
		
		/*
		 *  Static Invoke
		 */
		
		/*
		 *  Invoke Method
		 */
		
		/*
		 * Invoke Method
		 */
		
		System.out.println("INVOKE TEST FINISHED");
	}

	@Test
	public void assignFieldTest() {
		
		System.out.println("ASSIGN FIELD TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		
		// Assign Constant to Field
		
		// Assign Local To Field
		hs.assignLocalsToField("int_field", "int_var");
		
		// Assign Field to Field
		hs.assignFieldToField("sdf", "dfg");
		
		System.out.println("ASSIGN FIELD TEST FINISHED");
	}
	
	@Test
	public void ifStmtTest() {
		
	}
	
	@Test
	public void returnStmtTest() {
		
		System.out.println("RETURN TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
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
		
		// TODO Wie bekommt das Feld den ReturnWert?
		
		
		tsc.method();
		resField1 = tsc.methodWithConstReturn();
		resField2 = tsc.methodWithFieldReturn();
		resField3 = tsc.methodWithLocalReturn();
		
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
	public void joinTest() {
		System.out.println("JOIN TEST STARTED");

		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_x", Level.LOW);
		hs.addLocal("int_y", Level.HIGH);
		hs.addLocal("int_z", Level.LOW);
		assertEquals(Level.LOW, hs.join("int_x"));
		assertEquals(Level.HIGH, hs.join("int_x", "int_y"));		
		assertEquals(Level.HIGH, hs.join("int_x", "int_y", "int_z"));
		
		
		hs.setLocalPC(Level.HIGH);
		assertEquals(Level.HIGH, hs.join("int_x"));

		System.out.println("JOIN TEST FINISHED");
	}
}

package tests.handleStmtTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;
import exceptions.IllegalFlowException;

public class AssignFieldsFail {
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void assignConstantToField() {
		System.out.println("ASSIGN CONSTANT TO FIELD FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		assertEquals(0,  hs.getNumberOfElements());
		hs.addObjectToObjectMap(this);
		assertEquals(1,  hs.getNumberOfElements());
		hs.addFieldToObjectMap(this, "int_field");
		assertTrue(hs.containsFieldInObjectMap(this, "int_field"));
		
		/* Assign Constant to Field
		 *  int field = c;
		 *  1. Check if Level(field) >= gpc
		 *  2. Assign level of gpc to field
		 */

		// field = LOW, gpc = HIGH
		hs.makeFieldLOW(this, "int_field");
		hs.pushGlobalPC(SecurityLevel.HIGH);
		hs.assignLocalsToField(this, "int_field");
		
	    hs.close();	

		System.out.println("ASSIGN CONSTANT TO FIELD FAIL TEST FINISHED");
	}
	
	@Test
	public void assignLocalsToField() {
		
	}
	
}

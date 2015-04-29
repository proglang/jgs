package tests.handleStmtTests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;
import analyzer.level2.storage.ObjectMap;
import exceptions.IllegalFlowException;

public class assignFieldsFail {

	@Test(expected = IllegalFlowException.class)
	public void assignConstantToField() {
		System.out.println("ASSIGN CONSTANT TO FIELD FAIL TEST STARTED");

		ObjectMap m = ObjectMap.getInstance();
		assertEquals(0, m.sizeOfLocalMapStack());
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "int_field");
		
		/* Assign Constant to Field
		 *  int field = c;
		 *  1. Check if Level(field) >= lpc
		 *  2. Assign level of lpc to field
		 */

		// field = LOW, lpc = HIGH
		hs.makeFieldLOW(this, "int_field");
		hs.setLocalPC(Level.HIGH);
		hs.assignLocalsToField(this, "int_field");
		
	    hs.close();	

		System.out.println("ASSIGN CONSTANT TO FIELD FAIL TEST FINISHED");
	}
	
	@Test
	public void assignLocalsToField() {
		
	}
	
	@After
	public void close() {
	    ObjectMap m = ObjectMap.getInstance();
	    m.deleteLocalMapStack();
	}
}

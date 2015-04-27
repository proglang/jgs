package tests.handleStmtTests;

import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;
import exceptions.IllegalFlowException;

public class assignFieldsFail {

	@Test(expected = IllegalFlowException.class)
	public void assignConstantToField() {
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
	}
	
	@Test
	public void assignLocalsToField() {
		
	}
}

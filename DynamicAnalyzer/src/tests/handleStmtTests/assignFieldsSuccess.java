package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;

public class assignFieldsSuccess {

	@Test
	public void assignConstantToField() {
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "int_field");
		
		/* Assign Constant to Field
		 *  int field = c;
		 *  1. Check if Level(field) >= lpc
		 *  2. Assign level of lpc to field
		 */
		// field = LOW, lpc = LOW
		assertEquals(Level.LOW, hs.assignLocalsToField(this, "int_field"));
		

		// field = HIGH, lpc = LOW
		hs.makeFieldHigh(this, "int_field");
		assertEquals(Level.LOW, hs.assignLocalsToField(this, "int_field"));
		

		// field = HIGH, lpc = HIGH
		hs.makeFieldHigh(this, "int_field");
		hs.setLocalPC(Level.HIGH);
		assertEquals(Level.HIGH, hs.assignLocalsToField(this, "int_field"));
	
	}
	
	@Test
	public void assignLocalsToField() {
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "int_field");
		hs.addLocal("int_var1");
		hs.addLocal("int_var2");
		
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
	}
	
	

}

package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;

public class AssignFieldsSuccess {

	ObjectMap m = ObjectMap.getInstance();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void assignConstantToField() {
		
		System.out.println("ASSIGN CONSTANT TO FIELD TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "int_field");
		
		/* Assign Constant to Field
		 *  int field = c;
		 *  1. Check if Level(field) >= gpc
		 *  2. Assign level of gpc to field
		 */
		// field = LOW, gpc = LOW
		assertEquals(SecurityLevel.LOW, hs.getGlobalPC());
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "int_field"));
		assertEquals(SecurityLevel.LOW, hs.assignLocalsToField(this, "int_field"));
		

		// field = HIGH, gpc = LOW
		hs.makeFieldHigh(this, "int_field");
		assertEquals(SecurityLevel.LOW, hs.getGlobalPC());
		assertEquals(SecurityLevel.HIGH, hs.getFieldLevel(this, "int_field"));
		assertEquals(SecurityLevel.LOW, hs.assignLocalsToField(this, "int_field"));
		
		// field = HIGH, gpc = HIGH
		hs.makeFieldHigh(this, "int_field");
		hs.pushGlobalPC(SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, hs.getGlobalPC());
		assertEquals(SecurityLevel.HIGH, hs.getFieldLevel(this, "int_field"));
		assertEquals(SecurityLevel.HIGH, hs.assignLocalsToField(this, "int_field"));
		
	    hs.close();	

		System.out.println("ASSIGN CONSTANT TO FIELD TEST STARTED");
	}
	
	@Test
	public void assignLocalsToField() {
		
		System.out.println("ASSIGN LOCALS TO FIELD TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		assertEquals(1,  hs.getNumberOfElements());
		hs.addFieldToObjectMap(this, "int_field");
		hs.addLocal("int_var1");
		hs.addLocal("int_var2");
		
		/* Assign Local To Field
		 *  int field = var1 + var2;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.setLocalPC(SecurityLevel.LOW);
		assertEquals(SecurityLevel.LOW, hs.assignLocalsToField(this, "int_field", "int_var1"));
		assertEquals(SecurityLevel.LOW, hs.assignLocalsToField(this, "int_field", "int_var1", "int_var2"));
		
		hs.setLocalLevel("int_var2", SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, hs.assignLocalsToField(this, "int_field", "int_var1", "int_var2"));
		
	    hs.close();	
	    
		System.out.println("ASSIGN LOCALS TO FIELD TEST STARTED");
		
	}
	
	@Test
	public void assignLocalsToAStaticField() {
		
	}
	
	@Test
	public void assignLocalsToAnExternalField() {
		/*
		 * Object o;
		 * o.F = local
		 * 1. check(F >= join(x,gpc))
		 * 2. join(x,gpc,local)->F
		 */
	}
	
	

}

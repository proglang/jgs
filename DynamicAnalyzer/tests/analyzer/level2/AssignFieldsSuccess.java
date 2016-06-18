package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

import org.junit.Before;
import org.junit.Test;

import tests.testclasses.TestSubClass;
import utils.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;


public class AssignFieldsSuccess {

	Logger logger = L2Logger.getLogger();
	
	static int sField;
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void assignConstantToField() {
		
		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD TEST STARTED");
		
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
		assertEquals(SecurityLevel.LOW, hs.setLevelOfField(this, "int_field"));
		

		// field = HIGH, gpc = LOW
		hs.makeFieldHigh(this, "int_field");
		assertEquals(SecurityLevel.LOW, hs.getGlobalPC());
		assertEquals(SecurityLevel.HIGH, hs.getFieldLevel(this, "int_field"));
		assertEquals(SecurityLevel.LOW, hs.setLevelOfField(this, "int_field"));
		
		// field = HIGH, gpc = HIGH
		hs.makeFieldHigh(this, "int_field");
		hs.pushGlobalPC(SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, hs.getGlobalPC());
		assertEquals(SecurityLevel.HIGH, hs.getFieldLevel(this, "int_field"));
		assertEquals(SecurityLevel.HIGH, hs.setLevelOfField(this, "int_field"));
		
		hs.close();	

		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD TEST FINISHED");
	}
	
	@Test
	public void assignLocalsToField() {
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO FIELD TEST STARTED");
	    
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		assertEquals(1,  hs.getNumberOfElements());
		hs.addFieldToObjectMap(this, "int_field");
		hs.addLocal("int_var1");
		hs.addLocal("int_var2");
		
		/* Assign Local To Field
		 *  int field = var1 + var2;
		 *  1. Check if Level(field) >= lpc
		 *  2. Assign Join(y, z, lpc) to field
		 */
		hs.pushLocalPC(SecurityLevel.LOW, 123);
		assertEquals(SecurityLevel.LOW, hs.addLevelOfLocal("int_var1"));
		assertEquals(SecurityLevel.LOW, hs.setLevelOfField(this, "int_field"));
		assertEquals(SecurityLevel.LOW, hs.addLevelOfLocal("int_var1"));
		assertEquals(SecurityLevel.LOW, hs.addLevelOfLocal("int_var2"));
		assertEquals(SecurityLevel.LOW, hs.setLevelOfField(this, "int_field"));
		
		hs.setLevelOfLocal("int_var2", SecurityLevel.HIGH);
		assertEquals(SecurityLevel.LOW, hs.addLevelOfLocal("int_var1"));
		assertEquals(SecurityLevel.HIGH, hs.addLevelOfLocal("int_var2"));
		assertEquals(SecurityLevel.HIGH, hs.setLevelOfField(this, "int_field"));
		
		hs.popLocalPC(123);
		hs.close();	
	    
		logger.log(Level.INFO, "ASSIGN LOCALS TO FIELD TEST FINISHED");
		
	}
	
	@Test
	public void assignLocalsToAStaticField() {
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO A STATIC FIELD STARTED");

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this.getClass());
		assertEquals(1,  hs.getNumberOfElements());
		hs.addFieldToObjectMap(this.getClass(), "int_sField");
		hs.addLocal("int_var1");
		
		/* Assign Local To Field
		 *  int field = var1;
		 *  1. Check if Level(field) >= lpc
		 *  2. Assign Join(y, z, lpc) to field
		 */
		hs.pushLocalPC(SecurityLevel.LOW, 123);
		assertEquals(SecurityLevel.LOW, hs.addLevelOfLocal("int_var1"));
		assertEquals(SecurityLevel.LOW, hs.setLevelOfField(this.getClass(), "int_sField"));
		
		hs.setLevelOfLocal("int_var1", SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, hs.addLevelOfLocal("int_var1"));
		assertEquals(SecurityLevel.HIGH, hs.setLevelOfField(this.getClass(), "int_sField"));
		
		hs.popLocalPC(123);
		hs.close();	
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO A STATIC FIELD FINISHED");
	}
	
	@Test
	public void assignLocalsToAnExternalField() {
		
		logger.log(Level.INFO, "ASSIGN LOCAL TO AN EXTERNAL FIELD STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		
		/*
		 * Object o;
		 * o.F = local
		 * 1. check(F >= join(gpc))
		 * 2. join(local,gpc,local)->F
		 */
		
		hs.addLocal("int_local");
		hs.setLevelOfLocal("int_local");
		int local = 2;
		
		hs.addLocal("TestSubClass_o");
		TestSubClass o = new TestSubClass();
		
		// field = LOW, gpc = LOW
		assertEquals(SecurityLevel.LOW, hs.addLevelOfLocal("int_local"));
		assertEquals(SecurityLevel.LOW, hs.setLevelOfField(o, "int_pField"));
		o.pField = local;
		
		// field = HIGH, gpc = LOW
		hs.makeFieldHigh(o, "int_pField");
		assertEquals(SecurityLevel.LOW, hs.addLevelOfLocal("int_local"));
		assertEquals(SecurityLevel.LOW, hs.setLevelOfField(o, "int_pField"));
		o.pField = local;
		
		// field = HIGH, gpc = HIGH
		hs.makeFieldHigh(o, "int_pField");
		hs.pushGlobalPC(SecurityLevel.HIGH);
		assertEquals(SecurityLevel.LOW, hs.addLevelOfLocal("int_local"));
		assertEquals(SecurityLevel.HIGH, hs.setLevelOfField(o, "int_pField"));
		o.pField = local;
		
		logger.log(Level.INFO, "ASSIGN LOCAL TO AN EXTERNAL FIELD FINISHED");
	}
	
	

}

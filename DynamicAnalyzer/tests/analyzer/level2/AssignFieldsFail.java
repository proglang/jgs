package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import utils.exceptions.IllegalFlowException;
import utils.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AssignFieldsFail {
	
	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void assignConstantToField() {
		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		
		hs.addFieldToObjectMap(this, "int_field");
		
		/* Assign Constant to Field
		 *  int field = c;
		 *  1. Check if Level(field) >= gpc
		 *  2. Assign level of gpc to field
		 */

		// field = LOW, gpc = HIGH
		hs.makeFieldLow(this, "int_field");
		hs.pushGlobalPC(SecurityLevel.HIGH);
		hs.setLevelOfField(this, "int_field");
		
		hs.close();	
	}
	
	@Test(expected = IllegalFlowException.class)
	public void assignLocalsToField() {
		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		
		hs.addFieldToObjectMap(this, "int_field");
		hs.addLocal("int_local", SecurityLevel.LOW);
		
		/* Assign Local to Field
		 *  int field = local;
		 *  1. Check if Level(field) >= gpc
		 *  2. Assign level of join(local, gpc) to field
		 */

		// field = LOW, gpc = HIGH
		hs.makeFieldLow(this, "int_field");
		hs.pushGlobalPC(SecurityLevel.HIGH);
		hs.setLevelOfField(this, "int_field");
		
		hs.close();	
	}
	
	@Test(expected = IllegalFlowException.class)
	public void assignLocalToForeignField() {
		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		
		hs.addLocal("int_local");
		hs.setLevelOfLocal("int_local");
		int local = 2;
		
		hs.addLocal("TestSubClass_o");
		TestSubClass o = new TestSubClass();
		
		/* Assign Constant to Field
		 *  o.field = local;
		 *  1. Check if Level(field) >= gpc
		 *  2. Assign level of gpc to field
		 */

		
		// field = LOW, gpc = HIGH
		hs.makeFieldLow(o, "int_pField");
		hs.pushGlobalPC(SecurityLevel.HIGH);
		
		assertEquals(SecurityLevel.LOW, hs.addLevelOfLocal("int_local"));
		hs.setLevelOfField(o, "int_pField");
		o.pField = local;
		
		hs.close();	
	}
	
}

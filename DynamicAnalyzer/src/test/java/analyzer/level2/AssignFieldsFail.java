package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import util.exceptions.IFCError;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AssignFieldsFail {
	
	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test(expected = IFCError.class)
	public void assignConstantToField() {
		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		
		hs.addFieldToObjectMap(this, "int_field");
		
		/* Assign Constant to Field
		 *  int field = c;
		 *  1. Check if Level(field) >= gpc
		 *  2. Assign level of gpc to field
		 */

		// field = LOW, gpc = HIGH
		hs.makeFieldLow(this, "int_field");
		hs.pushGlobalPC(CurrentSecurityDomain.top());
		hs.checkGlobalPC(this, "int_field");
		hs.setLevelOfField(this, "int_field");
		
		hs.close();	
	}
	
	@Test(expected = IFCError.class)
	public void assignLocalsToField() {
		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		
		hs.addFieldToObjectMap(this, "int_field");
		hs.addLocal("int_local", CurrentSecurityDomain.bottom());
		
		/* Assign Local to Field
		 *  int field = local;
		 *  1. Check if Level(field) >= gpc
		 *  2. Assign level of join(local, gpc) to field
		 */

		// field = LOW, gpc = HIGH
		hs.makeFieldLow(this, "int_field");
		hs.pushGlobalPC(CurrentSecurityDomain.top());
		hs.checkGlobalPC(this, "int_field");
		hs.setLevelOfField(this, "int_field");
		
		hs.close();	
	}
	
	@Test(expected = IFCError.class)
	public void assignLocalToForeignField() {
		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		
		hs.addLocal("int_local");
		hs.setLocalToCurrentAssignmentLevel("int_local");
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
		hs.pushGlobalPC(CurrentSecurityDomain.top());
		
		assertEquals(CurrentSecurityDomain.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_local"));
		hs.checkGlobalPC(o, "int_field");
		hs.setLevelOfField(o, "int_pField");
		o.pField = local;
		
		hs.close();	
	}
	
}

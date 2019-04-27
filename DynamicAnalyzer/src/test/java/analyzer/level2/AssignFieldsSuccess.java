package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import tests.testclasses.TestSubClass;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;


public class AssignFieldsSuccess {

	Logger logger = L2Logger.getLogger();
	
	static int sField;
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void assignConstantToField() {
		
		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "int_field");
		
		/* Assign Constant to Field
		 *  int field = c;
		 *  1. Check if Level(field) >= gpc
		 *  2. Assign level of gpc to field
		 */
		// field = LOW, gpc = LOW
		assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
		assertEquals(CurrentSecurityDomain.bottom(), hs.getFieldLevel(this, "int_field"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.setLevelOfField(this, "int_field"));
		

		// field = HIGH, gpc = LOW
		hs.makeFieldHigh(this, "int_field");
		assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
		assertEquals(CurrentSecurityDomain.top(), hs.getFieldLevel(this, "int_field"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.setLevelOfField(this, "int_field"));
		
		// field = HIGH, gpc = HIGH
		hs.makeFieldHigh(this, "int_field");
		hs.pushGlobalPC(CurrentSecurityDomain.top());
		assertEquals(CurrentSecurityDomain.top(), hs.getGlobalPC());
		assertEquals(CurrentSecurityDomain.top(), hs.getFieldLevel(this, "int_field"));
		assertEquals(CurrentSecurityDomain.top(), hs.setLevelOfField(this, "int_field"));
		
		hs.close();	

		logger.log(Level.INFO, "ASSIGN CONSTANT TO FIELD TEST FINISHED");
	}
	
	@Test
	public void assignLocalsToField() {
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO FIELD TEST STARTED");
	    
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		assertEquals(1,  hs.getNumberOfElementsInObjectMap());
		hs.addFieldToObjectMap(this, "int_field");
		hs.addLocal("int_var1");
		hs.addLocal("int_var2");
		
		/* Assign Local To Field
		 *  int field = var1 + var2;
		 *  1. Check if Level(field) >= lpc
		 *  2. Assign Join(y, z, lpc) to field
		 */
		hs.pushLocalPC(CurrentSecurityDomain.bottom(), 123);
		assertEquals(CurrentSecurityDomain.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_var1"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.setLevelOfField(this, "int_field"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_var1"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_var2"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.setLevelOfField(this, "int_field"));
		
		// hs.checkLocalPC("int_var2");
		hs.setLocal("int_var2", CurrentSecurityDomain.top());
		assertEquals(CurrentSecurityDomain.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_var1"));
		assertEquals(CurrentSecurityDomain.top(), hs.joinLevelOfLocalAndAssignmentLevel("int_var2"));
		assertEquals(CurrentSecurityDomain.top(), hs.setLevelOfField(this, "int_field"));
		
		hs.popLocalPC(123);
		hs.close();	
	    
		logger.log(Level.INFO, "ASSIGN LOCALS TO FIELD TEST FINISHED");
		
	}
	
	@Test
	public void assignLocalsToAStaticField() {
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO A STATIC FIELD STARTED");

		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this.getClass());
		assertEquals(1,  hs.getNumberOfElementsInObjectMap());
		hs.addFieldToObjectMap(this.getClass(), "int_sField");
		hs.addLocal("int_var1");
		
		/* Assign Local To Field
		 *  int field = var1;
		 *  1. Check if Level(field) >= lpc
		 *  2. Assign Join(y, z, lpc) to field
		 */
		hs.pushLocalPC(CurrentSecurityDomain.bottom(), 123);
		assertEquals(CurrentSecurityDomain.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_var1"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.setLevelOfField(
				this.getClass(), "int_sField"));
		
		
		hs.setLocal("int_var1", CurrentSecurityDomain.top());
		assertEquals(CurrentSecurityDomain.top(), hs.joinLevelOfLocalAndAssignmentLevel("int_var1"));
		assertEquals(CurrentSecurityDomain.top(), hs.setLevelOfField(
				this.getClass(), "int_sField"));
		
		hs.popLocalPC(123);
		hs.close();	
		
		logger.log(Level.INFO, "ASSIGN LOCALS TO A STATIC FIELD FINISHED");
	}
	
	@Test
	public void assignLocalsToAnExternalField() {
		
		logger.log(Level.INFO, "ASSIGN LOCAL TO AN EXTERNAL FIELD STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		
		/*
		 * Object o;
		 * o.F = local
		 * 1. check(F >= join(gpc))
		 * 2. join(local,gpc,local)->F
		 */
		
		hs.addLocal("int_local");
		hs.checkLocalPC("int_local");
		hs.setLocalToCurrentAssignmentLevel("int_local");
		int local = 2;
		
		hs.addLocal("TestSubClass_o");
		TestSubClass o = new TestSubClass();
		
		// field = LOW, gpc = LOW
		assertEquals(CurrentSecurityDomain.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_local"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.setLevelOfField(o, "int_pField"));
		o.pField = local;
		
		// field = HIGH, gpc = LOW
		hs.makeFieldHigh(o, "int_pField");
		assertEquals(CurrentSecurityDomain.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_local"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.setLevelOfField(o, "int_pField"));
		o.pField = local;
		
		// field = HIGH, gpc = HIGH
		hs.makeFieldHigh(o, "int_pField");
		hs.pushGlobalPC(CurrentSecurityDomain.top());
		assertEquals(CurrentSecurityDomain.bottom(), hs.joinLevelOfLocalAndAssignmentLevel("int_local"));
		assertEquals(CurrentSecurityDomain.top(), hs.setLevelOfField(o, "int_pField"));
		o.pField = local;
		
		logger.log(Level.INFO, "ASSIGN LOCAL TO AN EXTERNAL FIELD FINISHED");
	}
	
	

}

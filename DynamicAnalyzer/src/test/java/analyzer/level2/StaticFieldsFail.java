package analyzer.level2;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import utils.exceptions.IllegalFlowException;
import utils.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StaticFieldsFail {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void internalStaticFieldFailTest() {
		
		LOGGER.log(Level.INFO, "INTERNAL STATIC FIELD FAIL TEST STARTED");

		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		hs.addFieldToObjectMap(this.getClass(), "int_field");
		
		hs.pushGlobalPC(SecurityLevel.top());
		hs.checkGlobalPC(this.getClass(), "int_field");
		hs.setLevelOfField(this.getClass(), "int_field");
			
		LOGGER.log(Level.INFO, "INTERNAL STATIC FIELD FAIL TEST STARTED");
		
	}
	
	@SuppressWarnings("static-access")
	@Test(expected = IllegalFlowException.class)
	public void externalStaticFieldFailTest() {
		
		LOGGER.log(Level.INFO, "EXTERNAL STATIC FIELD FAIL TEST STARTED");

		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		
		TestSubClass tsc = new TestSubClass();
		
		assertSame(TestSubClass.class, tsc.getClass());
		assertTrue(hs.containsFieldInObjectMap(TestSubClass.class, "int_sField"));
		assertTrue(hs.containsFieldInObjectMap(tsc.getClass(), "int_sField"));
		
		hs.pushGlobalPC(SecurityLevel.top());
		
		hs.checkGlobalPC(tsc.getClass(), "int_sField");
		hs.setLevelOfField(tsc.getClass(), "int_sField");
		TestSubClass.sField = 2;
		tsc.sField = 3;
			
		LOGGER.log(Level.INFO, "EXTERNAL STATIC FIELD FAIL TEST STARTED");
		
	}

}

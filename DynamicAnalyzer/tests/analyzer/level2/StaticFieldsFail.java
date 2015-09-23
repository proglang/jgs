package analyzer.level2;

import static org.junit.Assert.*;

import utils.exceptions.IllegalFlowException;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import tests.testClasses.TestSubClass;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

public class StaticFieldsFail {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void internalStaticFieldFailTest() {
		
		LOGGER.log(Level.INFO, "INTERNAL STATIC FIELD FAIL TEST STARTED");

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		hs.addFieldToObjectMap(this.getClass(), "int_field");
		
		hs.pushGlobalPC(SecurityLevel.HIGH);
		hs.setLevelOfField(this.getClass(), "int_field");
			
		LOGGER.log(Level.INFO, "INTERNAL STATIC FIELD FAIL TEST STARTED");
		
	}
	
	@SuppressWarnings("static-access")
	@Test(expected = IllegalFlowException.class)
	public void externalStaticFieldFailTest() {
		
		LOGGER.log(Level.INFO, "EXTERNAL STATIC FIELD FAIL TEST STARTED");

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		
		TestSubClass tsc = new TestSubClass();
		
		assertSame(TestSubClass.class, tsc.getClass());
		assertTrue(hs.containsFieldInObjectMap(TestSubClass.class, "int_sField"));
		assertTrue(hs.containsFieldInObjectMap(tsc.getClass(), "int_sField"));
		
		hs.pushGlobalPC(SecurityLevel.HIGH);
		
		hs.setLevelOfField(tsc.getClass(), "int_sField");
		TestSubClass.sField = 2;
		tsc.sField = 3;
			
		LOGGER.log(Level.INFO, "EXTERNAL STATIC FIELD FAIL TEST STARTED");
		
	}

}

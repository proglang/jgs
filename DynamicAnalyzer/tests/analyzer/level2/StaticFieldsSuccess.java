package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import tests.testClasses.TestSubClass;

import analyzer.level2.HandleStmtForTests;

public class StaticFieldsSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	static int field;
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void internalStaticFieldSuccessTest() {
		
		LOGGER.log(Level.INFO, "INTERNAL STATIC FIELD SUCCESS TEST STARTED");

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		hs.addFieldToObjectMap(this.getClass(), "int_field");
		
		hs.assignConstantToField(this.getClass(), "int_field");
			
		LOGGER.log(Level.INFO, "INTERNAL STATIC FIELD SUCCESS TEST STARTED");
		
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void externalStaticFieldSuccessTest() {
		
		LOGGER.log(Level.INFO, "EXTERNAL STATIC FIELD SUCCESS TEST STARTED");

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		
		TestSubClass tsc = new TestSubClass();
		
		assertSame(TestSubClass.class, tsc.getClass());
		assertTrue(hs.containsFieldInObjectMap(TestSubClass.class, "int_sField"));
		assertTrue(hs.containsFieldInObjectMap(tsc.getClass(), "int_sField"));
		
		hs.assignConstantToField(tsc.getClass(), "int_sField");
		hs.assignConstantToField(TestSubClass.class, "int_sField");
		TestSubClass.sField = 2;
		tsc.sField = 3;
			
		LOGGER.log(Level.INFO, "EXTERNAL STATIC FIELD SUCCESS TEST STARTED");
		
	}
	

}

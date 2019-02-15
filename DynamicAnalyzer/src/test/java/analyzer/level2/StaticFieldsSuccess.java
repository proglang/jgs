package analyzer.level2;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StaticFieldsSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	static int field;
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void internalStaticFieldSuccessTest() {
		
		LOGGER.log(Level.INFO, "INTERNAL STATIC FIELD SUCCESS TEST STARTED");

		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		hs.addFieldToObjectMap(this.getClass(), "int_field");
		
		hs.setLevelOfField(this.getClass(), "int_field");
			
		LOGGER.log(Level.INFO, "INTERNAL STATIC FIELD SUCCESS TEST ENDED");
		
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void externalStaticFieldSuccessTest() {
		
		LOGGER.log(Level.INFO, "EXTERNAL STATIC FIELD SUCCESS TEST STARTED");

		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		
		TestSubClass tsc = new TestSubClass();
		
		assertSame(TestSubClass.class, tsc.getClass());
		assertTrue(hs.containsFieldInObjectMap(TestSubClass.class, "int_sField"));
		assertTrue(hs.containsFieldInObjectMap(tsc.getClass(), "int_sField"));
		
		hs.setLevelOfField(tsc.getClass(), "int_sField");
		hs.setLevelOfField(TestSubClass.class, "int_sField");
		TestSubClass.sField = 2;
		tsc.sField = 3;
			
		LOGGER.log(Level.INFO, "EXTERNAL STATIC FIELD SUCCESS TEST STARTED");
		
	}
	

}

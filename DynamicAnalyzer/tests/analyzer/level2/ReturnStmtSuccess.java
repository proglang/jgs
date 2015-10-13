package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;
import tests.testClasses.TestSubClass;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

public class ReturnStmtSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void returnStmtTest() {
		
		LOGGER.log(Level.INFO, "RETURN TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.setLocalPC(SecurityLevel.LOW);
		hs.addLocal("int_res1");
		hs.addLocal("int_res2");
		hs.addLocal("int_res3");
		
		@SuppressWarnings("unused")
		int res1;
		
		@SuppressWarnings("unused")
		int res2;
		
		@SuppressWarnings("unused")
		int res3;
		
		
		TestSubClass tsc = new TestSubClass();
		res1 = tsc.methodWithConstReturn();
		assertEquals(SecurityLevel.LOW, hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res1");
		
		res2 = tsc.methodWithLowLocalReturn();
		assertEquals(SecurityLevel.LOW, hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res2");
		
		res3 = tsc.methodWithHighLocalReturn();
		assertEquals(SecurityLevel.HIGH, hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res3");

		
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_res1"));
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_res2"));
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("int_res3"));
		
	    hs.close();	
	    
	    LOGGER.log(Level.INFO, "RETURN TEST FINISHED");
	}

}

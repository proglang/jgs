package analyzer.level2;

import static org.junit.Assert.*;

import exceptions.IllegalFlowException;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import tests.testClasses.TestSubClass;
import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

public class ReturnStmtFail {

	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void returnStmtTest() {
		
		LOGGER.log(Level.INFO, "RETURN TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.setLocalPC(SecurityLevel.HIGH);
		hs.addLocal("int_res1");
		
		@SuppressWarnings("unused")
		int res1;
		
		
		TestSubClass tsc = new TestSubClass();
		res1 = tsc.methodWithConstReturn();
		assertEquals(SecurityLevel.LOW, hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res1");
		

		
	    hs.close();	
	    
	    LOGGER.log(Level.INFO, "RETURN TEST FINISHED");
	}

}

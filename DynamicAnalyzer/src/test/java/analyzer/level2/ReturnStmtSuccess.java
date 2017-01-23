package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import utils.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnStmtSuccess {
	
	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void returnStmtTest() {
		
		logger.log(Level.INFO, "RETURN TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
		hs.pushLocalPC(SecurityLevel.bottom(), 123);
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
		assertEquals(SecurityLevel.bottom(), hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res1");
		// After reading the returnlevel should be set to HIGH again
		assertEquals(SecurityLevel.top(), hs.getActualReturnLevel());
		
		res2 = tsc.methodWithLowLocalReturn();
		assertEquals(SecurityLevel.bottom(), hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res2");
		// After reading the returnlevel should be set to HIGH again
		assertEquals(SecurityLevel.top(), hs.getActualReturnLevel());
		
		res3 = tsc.methodWithHighLocalReturn();
		assertEquals(SecurityLevel.top(), hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res3");
		// After reading the returnlevel should be set to HIGH again
		assertEquals(SecurityLevel.top(), hs.getActualReturnLevel());

		
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_res1"));
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_res2"));
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("int_res3"));
		
		hs.popLocalPC(123);
		hs.close();	
	    
		logger.log(Level.INFO, "RETURN TEST FINISHED");
	}

}

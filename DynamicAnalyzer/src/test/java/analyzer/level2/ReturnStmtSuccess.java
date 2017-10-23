package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import util.logging.L2Logger;

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
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.pushLocalPC(CurrentSecurityDomain.bottom(), 123);
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
		assertEquals(CurrentSecurityDomain.bottom(), hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res1");
		// After reading the returnlevel should be set to HIGH again
		assertEquals(CurrentSecurityDomain.top(), hs.getActualReturnLevel());
		
		res2 = tsc.methodWithLowLocalReturn();
		assertEquals(CurrentSecurityDomain.bottom(), hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res2");
		// After reading the returnlevel should be set to HIGH again
		assertEquals(CurrentSecurityDomain.top(), hs.getActualReturnLevel());
		
		res3 = tsc.methodWithHighLocalReturn();
		assertEquals(CurrentSecurityDomain.top(), hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res3");
		// After reading the returnlevel should be set to HIGH again
		assertEquals(CurrentSecurityDomain.top(), hs.getActualReturnLevel());

		
		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalLevel("int_res1"));
		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalLevel("int_res2"));
		assertEquals(CurrentSecurityDomain.top(), hs.getLocalLevel("int_res3"));
		
		hs.popLocalPC(123);
		hs.close();	
	    
		logger.log(Level.INFO, "RETURN TEST FINISHED");
	}

}

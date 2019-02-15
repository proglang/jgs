package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import util.exceptions.IFCError;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnStmtFail {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	/**
	 * Tests the NSU policy: 
	 * hs = LOW, int_res1 = LOW, localPC = HIGH.
	 * 
	 * the line "hs.assignReturnLevelToLocal("int_res1") is instrumented to assigns like:
	 * res1 = ExternalClass.returnSomething(): The local variable res1 needs to be upgraded with the
	 * sec-value of ExternalClass.returnSomething(). But before that can happen, we need to check
	 * if NSU policy holds (remember: we must not upgrade low-sec vars in high-sec PC environment.
	 * Thus, in assignReturnLevelToLocal, we perform the adequate NSU check (checkLocalPC), and since
	 * we have here: local < PC => IFCError!
	 */
	@Test(expected = IFCError.class)
	public void returnStmtTestFail() {
		
		logger.log(Level.INFO, "RETURN TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.pushLocalPC(CurrentSecurityDomain.top(), 123);
		hs.addLocal("int_res1");
		
		// Initialize int_res1 !
		hs.setLocal("int_res1", CurrentSecurityDomain.bottom());
		
		@SuppressWarnings("unused")
		int res1;
		
		
		TestSubClass tsc = new TestSubClass();
		res1 = tsc.methodWithConstReturn();
		assertEquals(CurrentSecurityDomain.bottom(), hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res1");			// IFexception thrown here
		

		hs.popLocalPC(123);
		hs.close();	
	    
		logger.log(Level.INFO, "RETURN TEST FINISHED");
	}
	
	
	@Test
	public void returnStmtTestSuccess() {
		
		logger.log(Level.INFO, "RETURN TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.pushLocalPC(CurrentSecurityDomain.top(), 123);
		hs.addLocal("int_res1");
		
		@SuppressWarnings("unused")
		int res1;
		
		
		TestSubClass tsc = new TestSubClass();
		res1 = tsc.methodWithConstReturn();
		assertEquals(CurrentSecurityDomain.bottom(), hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res1");
		

		hs.popLocalPC(123);
		hs.close();	
	    
		logger.log(Level.INFO, "RETURN TEST FINISHED");
	}

}

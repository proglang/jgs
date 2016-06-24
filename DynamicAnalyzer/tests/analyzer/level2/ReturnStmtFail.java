package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
import org.junit.Before;
import org.junit.Test;
import tests.testclasses.TestSubClass;
import utils.exceptions.IllegalFlowException;
import utils.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnStmtFail {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void returnStmtTest() {
		
		logger.log(Level.INFO, "RETURN TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
		hs.pushLocalPC(SecurityLevel.top(), 123);
		hs.addLocal("int_res1");
		
		@SuppressWarnings("unused")
		int res1;
		
		
		TestSubClass tsc = new TestSubClass();
		res1 = tsc.methodWithConstReturn();
		assertEquals(SecurityLevel.bottom(), hs.getActualReturnLevel());
		hs.assignReturnLevelToLocal("int_res1");
		

		hs.popLocalPC(123);
		hs.close();	
	    
		logger.log(Level.INFO, "RETURN TEST FINISHED");
	}

}

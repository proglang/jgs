package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import utils.exceptions.IllegalFlowException;
import utils.logging.L2Logger;

import java.util.logging.Logger;

public class CheckThatNotHigh {

	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}
	
	@Test
	public void successTest() {
		
		LOGGER.info("CheckThatNotHigh-successTest started");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		
		hs.addLocal("String_low");
		
//		hs.checkThatNotLe("String_low", "HIGH");
		hs.checkThatLe("String_low", "LOW");
		hs.close();
		
		LOGGER.info("CheckThatNotHigh-successTest finished");
		
	}
	
	@Test(expected = IllegalFlowException.class)
	public void failTest() {
		
		LOGGER.info("CheckThatNotHigh-failTest started");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false);
		
		hs.addLocal("String_high");
		
		hs.setLevelOfLocal("String_high", SecurityLevel.top());
		
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("String_high"));
		
//		hs.checkThatNotLe("String_high", "HIGH");
		hs.checkThatLe("String_high", "LOW");
		
		hs.close();
		
		LOGGER.info("CheckThatNotHigh-failTest finished");
			
	}
}

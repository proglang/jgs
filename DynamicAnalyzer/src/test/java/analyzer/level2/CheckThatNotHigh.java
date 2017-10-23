package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import util.exceptions.IFCError;
import util.logging.L2Logger;

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
		hs.initHandleStmtUtils(false, 0);
		
		hs.addLocal("String_low");

		hs.setLocal("String_low", CurrentSecurityDomain.bottom());
//		hs.checkThatNotLe("String_low", "HIGH");
		hs.checkThatLe("String_low", "LOW");
		hs.close();
		
		LOGGER.info("CheckThatNotHigh-successTest finished");
		
	}
	
	@Test(expected = IFCError.class)
	public void failTest() {
		
		LOGGER.info("CheckThatNotHigh-failTest started");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		hs.addLocal("String_high");
		
		hs.setLocal("String_high", CurrentSecurityDomain.top());
		
		assertEquals(CurrentSecurityDomain.top(), hs.getLocalLevel("String_high"));
		
//		hs.checkThatNotLe("String_high", "HIGH");
		hs.checkThatLe("String_high", "LOW");
		
		hs.close();
		
		LOGGER.info("CheckThatNotHigh-failTest finished");
			
	}
}

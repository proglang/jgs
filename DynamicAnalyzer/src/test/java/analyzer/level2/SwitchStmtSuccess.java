package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SwitchStmtSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void switchStmtLowTest() {
		
		LOGGER.log(Level.INFO, "SWITCH STMT LOW TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		hs.addLocal("int_x");
		int x = 0;
		
		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
		
		hs.checkCondition("123", "int_x");
		switch (x) {
		
		  case 0: 
			  assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
			  hs.exitInnerScope("123");
			  break;
		  case 1:  
			  assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
			  hs.exitInnerScope("123");
			  break;
		  default:  
			  assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
			  hs.exitInnerScope("123");
			  break;
		
		} 

		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());

		LOGGER.log(Level.INFO, "SWITCH STMT LOW TEST FINISHED");
	}
	
	@Test
	public void switchStmtHighTest() {
		
		LOGGER.log(Level.INFO, "SWITCH STMT HIGH TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		hs.addLocal("int_x", CurrentSecurityDomain.top());
		int x = 0;
		
		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
		
		hs.checkCondition("123","int_x");
		switch (x) {
		
		  case 0: 
			  assertEquals(CurrentSecurityDomain.top(), hs.getLocalPC());
			  hs.checkLocalPC("int_x");
			  hs.setLocalToCurrentAssignmentLevel("int_x");
			  hs.exitInnerScope("123");
			  break;
		  case 1:  
			  assertEquals(CurrentSecurityDomain.top(), hs.getLocalPC());
			  hs.exitInnerScope("123");
			  break;
		  default:  
			  assertEquals(CurrentSecurityDomain.top(), hs.getLocalPC());
			  hs.exitInnerScope("123");
			  break;
		} 

		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());

		LOGGER.log(Level.INFO, "SWITCH STMT HIGH TEST FINISHED");
	}

}

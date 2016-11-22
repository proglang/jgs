package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
import org.junit.Before;
import org.junit.Test;
import utils.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WhileStmtSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void whileStmtLowTest() {
		
		LOGGER.log(Level.INFO, "WHILE STMT LOW TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		
		hs.addLocal("int_x");
		hs.checkLocalPC("int_x");
		hs.setLevelOfLocal("int_x");
		int x = 0;
		
		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		
		hs.checkCondition("123", "int_x");
		while (x == 0) {
			
			assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
			
			hs.checkLocalPC("int_x");
			hs.setLevelOfLocal("int_x");
			x = 1;
			
			hs.exitInnerScope("123");
		}

		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		
		LOGGER.log(Level.INFO, "WHILE STMT LOW TEST FINISHED");
	}
	
	@Test
	public void whileStmtHighTest() {
		
		LOGGER.log(Level.INFO, "WHILE STMT HIGH TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		
		hs.addLocal("int_x", SecurityLevel.top());
		int x = 0;
		
		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		
		hs.checkCondition("123", "int_x");
		while (x == 0) {
			
			assertEquals(SecurityLevel.top(), hs.getLocalPC());
			
			hs.checkLocalPC("int_x");
			hs.setLevelOfLocal("int_x");
			x = 1;
			
			hs.exitInnerScope("123");
		}

		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		
		LOGGER.log(Level.INFO, "WHILE STMT HIGH TEST FINISHED");
	}

}

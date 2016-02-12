package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

public class WhileStmtSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void whileStmtLowTest() {
		
		LOGGER.log(Level.INFO, "WHILE STMT LOW TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		hs.addLocal("int_x");
		hs.setLevelOfLocal("int_x");
		int x = 0;
		
		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		
		hs.checkCondition(123, "int_x");
		while (x == 0) {
			
			assertEquals(SecurityLevel.LOW, hs.getLocalPC());
			
			hs.setLevelOfLocal("int_x");
			x = 1;
			
		hs.exitInnerScope(123);
		}

		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		
		LOGGER.log(Level.INFO, "WHILE STMT LOW TEST FINISHED");
	}
	
	@Test
	public void whileStmtHighTest() {
		
		LOGGER.log(Level.INFO, "WHILE STMT HIGH TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		hs.addLocal("int_x", SecurityLevel.HIGH);
		int x = 0;
		
		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		
		hs.checkCondition(123, "int_x");
		while (x == 0) {
			
			assertEquals(SecurityLevel.HIGH, hs.getLocalPC());
			
			hs.setLevelOfLocal("int_x");
			x = 1;
			
		hs.exitInnerScope(123);
		}

		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		
		LOGGER.log(Level.INFO, "WHILE STMT HIGH TEST FINISHED");
	}

}

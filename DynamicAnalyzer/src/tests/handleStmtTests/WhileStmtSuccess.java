package tests.handleStmtTests;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Logger;

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
		hs.assignConstantToLocal("int_x");
		int x = 0;
		
		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		
		hs.checkCondition("int_x");
		while (x == 0) {
			
			assertEquals(SecurityLevel.LOW, hs.getLocalPC());
			
			hs.assignConstantToLocal("int_x");
			x = 1;
			
		hs.exitInnerScope();
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
		
		hs.checkCondition("int_x");
		while (x == 0) {
			
			assertEquals(SecurityLevel.HIGH, hs.getLocalPC());
			
			hs.assignConstantToLocal("int_x");
			x = 1;
			
		hs.exitInnerScope();
		}

		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		
		LOGGER.log(Level.INFO, "WHILE STMT HIGH TEST FINISHED");
	}

}

package analyzer.level2;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import exceptions.IllegalFlowException;

public class WhileStmtFail {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void whileStmtFailTest() {
		
		LOGGER.log(Level.INFO, "WHILE STMT TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		hs.addLocal("int_x");
		hs.assignConstantToLocal("int_x");
		@SuppressWarnings("unused")
		int x = 0;
		
		hs.addLocal("int_y", SecurityLevel.HIGH);
		int y = 0;
		
		hs.checkCondition("int_y");
		while (y == 0) {
			
			hs.assignLocalsToLocal("int_x", "int_x");
			x += 1;
			hs.assignLocalsToLocal("int_y", "int_y");
			y += 1;
			
		hs.exitInnerScope();
		}
		
		LOGGER.log(Level.INFO, "WHILE STMT TEST FINISHED");
	}


}

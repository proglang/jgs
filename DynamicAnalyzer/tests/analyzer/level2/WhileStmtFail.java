package analyzer.level2;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import utils.exceptions.IllegalFlowException;

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
		hs.setLevelOfLocal("int_x");
		int x = 0;
		
		hs.addLocal("int_y", SecurityLevel.HIGH);
		int y = 0;
		
		hs.checkCondition("123", "int_y");
		while (y == 0) {
			
			hs.addLevelOfLocal("int_x");
			hs.setLevelOfLocal("int_x");
			x += 1;
			hs.addLevelOfLocal("int_y");
			hs.setLevelOfLocal("int_y");
			y += 1;
			
		hs.exitInnerScope("123");
		}
		
		LOGGER.log(Level.INFO, "WHILE STMT TEST FINISHED");
	}


}

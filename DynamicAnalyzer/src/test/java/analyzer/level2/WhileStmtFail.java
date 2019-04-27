package analyzer.level2;

import org.junit.Before;
import org.junit.Test;

import util.exceptions.IFCError;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WhileStmtFail {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@SuppressWarnings("unused")
	@Test(expected = IFCError.class)
	public void whileStmtFailTest() {
		
		LOGGER.log(Level.INFO, "WHILE STMT TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		hs.addLocal("int_x");
		hs.checkLocalPC("int_x");
		hs.setLocalToCurrentAssignmentLevel("int_x");
		int x = 0;
		
		hs.addLocal("int_y", CurrentSecurityDomain.top());
		int y = 0;
		
		hs.checkCondition("123", "int_y");
		while (y == 0) {
			
			hs.joinLevelOfLocalAndAssignmentLevel("int_x");
			hs.checkLocalPC("int_x");
			hs.setLocalToCurrentAssignmentLevel("int_x");
			x += 1;
			hs.joinLevelOfLocalAndAssignmentLevel("int_y");
			hs.checkLocalPC("int_y");
			hs.setLocalToCurrentAssignmentLevel("int_y");
			y += 1;
			
			hs.exitInnerScope("123");
		}
		
		LOGGER.log(Level.INFO, "WHILE STMT TEST FINISHED");
	}


}

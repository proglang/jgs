package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

import utils.exceptions.IllegalFlowException;

public class SwitchStmtFail {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void switchStmtLowTest() {
		
		LOGGER.log(Level.INFO, "SWITCH STMT FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		hs.addLocal("int_x", SecurityLevel.HIGH);
		int x = 0;
		hs.addLocal("int_y", SecurityLevel.LOW);
		@SuppressWarnings("unused")
		int y = 0;
		
		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		
		hs.checkCondition("int_x");
		switch(x) {
		
		case 0: 
			assertEquals(SecurityLevel.HIGH, hs.getLocalPC()); 
			
			hs.assignConstantToLocal("int_y");
			x += 2;
			
			hs.exitInnerScope();
			break;
		case 1:  
			assertEquals(SecurityLevel.HIGH, hs.getLocalPC()); 
			hs.exitInnerScope();
			break;
		default:  
			assertEquals(SecurityLevel.HIGH, hs.getLocalPC()); 
			hs.exitInnerScope();
			break;
		
		} 

		assertEquals(SecurityLevel.LOW, hs.getLocalPC());

		LOGGER.log(Level.INFO, "SWITCH STMT FAIL TEST FINISHED");
	}


}

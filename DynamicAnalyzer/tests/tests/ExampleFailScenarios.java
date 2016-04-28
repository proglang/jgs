package tests;


import org.junit.Before;
import org.junit.Test;

import utils.exceptions.IllegalFlowException;
import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

public class ExampleFailScenarios {
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}
	

	@SuppressWarnings("unused")
	@Test(expected = IllegalFlowException.class)
	public void highIfStmt() {
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x");
		hs.addLocal("int_high", SecurityLevel.HIGH);
		int x = 0;
		int high = 0;
		
		hs.checkCondition(123, "int_high");
		if(high == 0) {
			
			hs.setLevelOfLocal("int_x");
			x = 1;
			
			hs.exitInnerScope(123);
		}
		
		hs.close();
	}
	

}

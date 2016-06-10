package analyzer.level2;

import static org.junit.Assert.*;

import utils.exceptions.IllegalFlowException;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

public class IfStmtFail {
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void ifStmtFailTest() {
		
		System.out.println("IF STMT FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		
		hs.addLocal("int_x");
		hs.setLevelOfLocal("int_x");
		int x = 1;
		hs.makeLocalHigh("int_x");
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("int_x"));
		
		hs.addLocal("int_y");
		hs.setLevelOfLocal("int_y");
		@SuppressWarnings("unused")
		int y = 1;
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_y"));

		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		assertEquals(SecurityLevel.LOW, hs.getGlobalPC());	
		
		hs.checkCondition("123", "int_x");
		if (x == 1) {
			assertEquals(SecurityLevel.HIGH, hs.getLocalPC());
			assertEquals(SecurityLevel.HIGH, hs.getGlobalPC());	
			
			hs.setLevelOfLocal("int_y");
			y = 2;
			
			hs.exitInnerScope("123");
		}
		

		assertEquals(SecurityLevel.LOW, hs.getLocalPC());
		assertEquals(SecurityLevel.LOW, hs.getGlobalPC());	
		
		
		System.out.println("IF STMT FAIL TEST FINISHED");
		
	}


}

package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
import org.junit.Before;
import org.junit.Test;
import utils.exceptions.IllegalFlowException;


public class IfStmtFail {
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void ifStmtFailTest() {
		
		System.out.println("IF STMT FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
		
		hs.addLocal("int_x");
		hs.setLevelOfLocal("int_x");
		int x = 1;
		hs.makeLocalHigh("int_x");
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("int_x"));
		
		hs.addLocal("int_y");
		hs.setLevelOfLocal("int_y");
		@SuppressWarnings("unused")
		int y = 1;
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_y"));

		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.getGlobalPC());	
		
		hs.checkCondition("123", "int_x");
		if (x == 1) {
			assertEquals(SecurityLevel.top(), hs.getLocalPC());
			assertEquals(SecurityLevel.top(), hs.getGlobalPC());	
			
			hs.setLevelOfLocal("int_y");
			y = 2;
			
			hs.exitInnerScope("123");
		}
		

		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.getGlobalPC());	
		
		
		System.out.println("IF STMT FAIL TEST FINISHED");
		
	}


}

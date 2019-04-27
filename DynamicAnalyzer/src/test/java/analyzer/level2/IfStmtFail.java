package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import util.exceptions.IFCError;


public class IfStmtFail {
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test(expected = IFCError.class)
	public void ifStmtFailTest() {
		
		System.out.println("IF STMT FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		
		hs.addLocal("int_x");
		hs.checkLocalPC("int_x");
		hs.setLocalToCurrentAssignmentLevel("int_x");
		int x = 1;
		hs.setLocalFromString("int_x", "HIGH");
		assertEquals(CurrentSecurityDomain.top(), hs.getLocalLevel("int_x"));
		
		hs.addLocal("int_y");
		hs.checkLocalPC("int_y");
		hs.setLocalToCurrentAssignmentLevel("int_y");
		@SuppressWarnings("unused")
		int y = 1;
		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalLevel("int_y"));

		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
		assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
		
		hs.checkCondition("123", "int_x");
		if (x == 1) {
			assertEquals(CurrentSecurityDomain.top(), hs.getLocalPC());
			assertEquals(CurrentSecurityDomain.top(), hs.getGlobalPC());
			
			hs.checkLocalPC("int_y");
			hs.setLocalToCurrentAssignmentLevel("int_y");
			y = 2;
			
			hs.exitInnerScope("123");
		}
		

		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
		assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
		
		
		System.out.println("IF STMT FAIL TEST FINISHED");
		
	}


}

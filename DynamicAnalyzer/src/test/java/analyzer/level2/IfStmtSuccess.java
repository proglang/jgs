package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class IfStmtSuccess {
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void ifStmtSimpleTest() {
		
		System.out.println("SIMPLE IF STMT TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.addLocal("int_x");
		
		hs.checkLocalPC("int_x");
		hs.setLocalToCurrentAssignmentLevel("int_x");
		int x = 1;
		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalLevel("int_x"));

		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
		assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
		
		hs.checkCondition("123", "int_x");
		if (x == 1) {
			assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
			assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
			
			hs.setLocalFromString("int_x", "HIGH");
			
			hs.checkCondition("123", "int_x");
			if (x == 1) {

				assertEquals(CurrentSecurityDomain.top(), hs.getLocalPC());
				assertEquals(CurrentSecurityDomain.top(), hs.getGlobalPC());
				
				hs.exitInnerScope("123");
			}
			
			assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
			assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
			
			hs.exitInnerScope("123");
		}
		

		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
		assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
		
		hs.setLocalFromString("int_x", "HIGH");
		
		hs.checkCondition("123", "int_x");
		if (x == 1) {

			assertEquals(CurrentSecurityDomain.top(), hs.getLocalPC());
			assertEquals(CurrentSecurityDomain.top(), hs.getGlobalPC());
			
			hs.exitInnerScope("123");
		}
		

		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
		assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
		
		hs.setLocalFromString("int_x", "LOW");
		hs.pushLocalPC(CurrentSecurityDomain.top(), 234);
		hs.pushGlobalPC(CurrentSecurityDomain.top());
		
		hs.checkCondition("123", "int_x");
		if (x == 1) {

			assertEquals(CurrentSecurityDomain.top(), hs.getLocalPC());
			assertEquals(CurrentSecurityDomain.top(), hs.getGlobalPC());
			
			hs.exitInnerScope("123");
		}
		
		assertEquals(CurrentSecurityDomain.top(), hs.getLocalPC());
		assertEquals(CurrentSecurityDomain.top(), hs.getGlobalPC());
		
		hs.popLocalPC(234);
		hs.close();
		
		System.out.println("SIMPLE IF STMT TEST FINISHED");
		
	}

}

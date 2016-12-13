package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
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
		hs.addObjectToObjectMap(this);
		hs.addLocal("int_x");
		
		hs.checkLocalPC("int_x");
		hs.setLevelOfLocal("int_x");
		int x = 1;
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_x"));

		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.getGlobalPC());	
		
		hs.checkCondition("123", "int_x");
		if (x == 1) {
			assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
			assertEquals(SecurityLevel.bottom(), hs.getGlobalPC());	
			
			hs.makeLocal("int_x", "HIGH");
			
			hs.checkCondition("123", "int_x");
			if (x == 1) {

				assertEquals(SecurityLevel.top(), hs.getLocalPC());
				assertEquals(SecurityLevel.top(), hs.getGlobalPC());	
				
				hs.exitInnerScope("123");
			}
			
			assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
			assertEquals(SecurityLevel.bottom(), hs.getGlobalPC());	
			
			hs.exitInnerScope("123");
		}
		

		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.getGlobalPC());	
		
		hs.makeLocal("int_x", "HIGH");
		
		hs.checkCondition("123", "int_x");
		if (x == 1) {

			assertEquals(SecurityLevel.top(), hs.getLocalPC());
			assertEquals(SecurityLevel.top(), hs.getGlobalPC());	
			
			hs.exitInnerScope("123");
		}
		

		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.getGlobalPC());	
		
		hs.makeLocal("int_x", "LOW");
		hs.pushLocalPC(SecurityLevel.top(), 234);
		hs.pushGlobalPC(SecurityLevel.top());
		
		hs.checkCondition("123", "int_x");
		if (x == 1) {

			assertEquals(SecurityLevel.top(), hs.getLocalPC());
			assertEquals(SecurityLevel.top(), hs.getGlobalPC());	
			
			hs.exitInnerScope("123");
		}
		
		assertEquals(SecurityLevel.top(), hs.getLocalPC());
		assertEquals(SecurityLevel.top(), hs.getGlobalPC());
		
		hs.popLocalPC(234);
		hs.close();
		
		System.out.println("SIMPLE IF STMT TEST FINISHED");
		
	}

}

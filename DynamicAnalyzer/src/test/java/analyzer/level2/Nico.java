package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class Nico {
	
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
		hs.setLocalToCurrentAssignmentLevel("int_x");
		int x = 1;
		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalLevel("int_x"));
		
		hs.addLocal("int_y");
		hs.setLocalToCurrentAssignmentLevel("int_y");
		int y = 2;
		hs.setLocalFromString("int_y", "HIGH");
		assertEquals(CurrentSecurityDomain.top(), hs.getLocalLevel("int_y"));

		assertEquals(CurrentSecurityDomain.bottom(), hs.getLocalPC());
		assertEquals(CurrentSecurityDomain.bottom(), hs.getGlobalPC());
		
		
		hs.close();
		
		System.out.println("SIMPLE IF STMT TEST FINISHED");
		
	}

}

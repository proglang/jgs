package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
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
		hs.addObjectToObjectMap(this);
		
		hs.addLocal("int_x");
		hs.setLevelOfLocal("int_x");
		int x = 1;
		assertEquals(SecurityLevel.bottom(), hs.getLocalLevel("int_x"));
		
		hs.addLocal("int_y");
		hs.setLevelOfLocal("int_y");
		int y = 2;
		hs.makeLocal("int_y", null);
		assertEquals(SecurityLevel.top(), hs.getLocalLevel("int_y"));

		assertEquals(SecurityLevel.bottom(), hs.getLocalPC());
		assertEquals(SecurityLevel.bottom(), hs.getGlobalPC());	
		
		
		hs.close();
		
		System.out.println("SIMPLE IF STMT TEST FINISHED");
		
	}

}

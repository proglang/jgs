package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SimpleSuccess {
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void simpleTest() {

		System.out.println("SIMPLE TEST STARTED");
		
		assertEquals(1, 1);
		
		
		System.out.println("SIMPLE TEST ENDED");
	}

}

package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;

public class WhileStmtSuccess {
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void whileStmtSimpleTest() {
		int x = 0;
		while (x == 0) {
			x = 1;
		}
	}

}

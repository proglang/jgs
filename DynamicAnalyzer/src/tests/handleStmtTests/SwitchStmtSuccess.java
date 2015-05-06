package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;

public class SwitchStmtSuccess {
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void switchStmtSimpleTest() {
		
		int x = 0;
		switch(x) {
		case 0: break;
		case 1: break;
		default: break;
		}
	}

}

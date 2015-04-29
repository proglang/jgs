package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Test;

public class SwitchStmtSuccess {

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

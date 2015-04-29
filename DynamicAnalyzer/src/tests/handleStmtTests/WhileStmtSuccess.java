package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Test;

public class WhileStmtSuccess {

	@Test
	public void whileStmtSimpleTest() {
		int x = 0;
		while (x == 0) {
			x = 1;
		}
	}

}

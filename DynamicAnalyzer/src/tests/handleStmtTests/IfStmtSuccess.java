package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Test;

import analyzer.level2.HandleStmtForTests;

public class IfStmtSuccess {

	@Test
	public void ifStmtSimpleTest() {
		
		System.out.println("SIMPLE IF STMT TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		int x = 1;
		
		// hs.checkCondition
		if (x == 1) {

			HandleStmtForTests hs_inner = new HandleStmtForTests();
			
			hs_inner.close();
		}
		
		hs.close();
		
		System.out.println("SIMPLE IF STMT TEST FINISHED");
		
	}
	
	@Test
	public void ifStmt2Test() {
		
		int x = 1;
		
		if ( x == 1) {
			x = 2;
		}
		
	}

}

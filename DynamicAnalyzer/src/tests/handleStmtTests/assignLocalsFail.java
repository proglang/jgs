package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;
import exceptions.IllegalFlowException;

public class assignLocalsFail {

	@Test(expected = IllegalFlowException.class)
	public void assignConstantToLocal() {
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x", Level.LOW);
		hs.setLocalPC(Level.HIGH);
		// x = LOW, lpc = HIGH
		hs.assignLocalsToLocal("int_x");
	}

	@Test(expected = IllegalFlowException.class)
	public void assignLocalsToLocal() {
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x");
		hs.addLocal("int_y");
		hs.addLocal("int_z", Level.HIGH);
		
		/*
		 *  Assign Locals to Local
		 *  int x = y + z;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.setLocalPC(Level.HIGH);
		// x = LOW, lpc = HIGH
		assertEquals(Level.LOW, hs.getLocalLevel("int_x"));
		assertEquals(Level.LOW, hs.getLocalLevel("int_y"));
		assertEquals(Level.HIGH, hs.getLocalLevel("int_z"));
		assertEquals(Level.HIGH, hs.getLocalPC());
		hs.assignLocalsToLocal("int_x", "int_y", "int_z");
	}
	
	@Test
	public void assignNewObjectToLocal() {
		
	}
	
	@Test
	public void assignMethodResultToLocal() {
	
	}
}

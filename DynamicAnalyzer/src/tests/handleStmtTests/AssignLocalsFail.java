package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;
import analyzer.level2.storage.ObjectMap;
import exceptions.IllegalFlowException;

public class AssignLocalsFail {
	


	@Test(expected = IllegalFlowException.class)
	public void assignConstantToLocal() {
		
		System.out.println("ASSIGN CONSTANT TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x", Level.LOW);
		hs.setLocalPC(Level.HIGH);
		// x = LOW, lpc = HIGH
		hs.assignLocalsToLocal("int_x");
		hs.close();
		
		System.out.println("ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	

	@Test(expected = IllegalFlowException.class)
	public void assignLocalsToLocal() {
		
		System.out.println("ASSIGN LOCALS TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());
		
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
	
	@After
	public void close() {
	    ObjectMap m = ObjectMap.getInstance();
	    m.deleteLocalMapStack();
	}
}

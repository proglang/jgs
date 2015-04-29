package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;
import analyzer.level2.storage.ObjectMap;

public class InternalMethods {
	
	@Before
	public void init() {
	    ObjectMap m = ObjectMap.getInstance();
	    m.deleteLocalMapStack();
	}

	@Test
	public void checkLocalPCTest() {
		System.out.println("LOCAL PC TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x");
		
		// Level(x) = LOW, Level(lpc) = LOW
		assertEquals(true, hs.checkLocalPC("int_x"));
		
		// Level(x) = HIGH, Level(lpc) = LOW
		hs.makeLocalHigh("int_x");
		assertEquals(true, hs.checkLocalPC("int_x"));
		
		// Level(x) = HIGH, Level(lpc) = HIGH
		hs.setLocalPC(Level.HIGH);
		assertEquals(true, hs.checkLocalPC("int_x"));
		
		// Level(x) = LOW, Level(lpc) = HIGH
		hs.makeLocalLow("int_x");
		assertEquals(false, hs.checkLocalPC("int_x"));
		
	    hs.close();	
	    

	    assertEquals(0, m.sizeOfLocalMapStack());
		
		System.out.println("LOCAL PC TEST FINISHED");
	}
	
	@Test
	public void joinLocalsTest() {
		System.out.println("JOIN LOCALS TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    m.deleteLocalMapStack();
	    assertEquals(0, m.sizeOfLocalMapStack());

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x", Level.LOW);
		hs.addLocal("int_y", Level.HIGH);
		hs.addLocal("int_z", Level.LOW);
		assertEquals(Level.LOW, hs.joinLocals("int_x"));
		assertEquals(Level.HIGH, hs.joinLocals("int_x", "int_y"));		
		assertEquals(Level.HIGH, hs.joinLocals("int_x", "int_y", "int_z"));
		
		
		hs.setLocalPC(Level.HIGH);
		assertEquals(Level.HIGH, hs.joinLocals("int_x"));

	    hs.close();	
		
		System.out.println("JOIN LOCALS TEST FINISHED");
	}
	
	@Test
	public void localPCTest() {
		System.out.println("LOCAL PC TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());

		HandleStmtForTests hs = new HandleStmtForTests();
		
		// TODO

	    hs.close();	
		
		System.out.println("LOCAL PC TEST FINISHED");
	}
	
	@Test
	public void argumentsListTest() {
		
	}
	
	@Test
	public void localMapStackTest() {
		
	}

}

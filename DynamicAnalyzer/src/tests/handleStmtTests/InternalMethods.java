package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;

public class InternalMethods {
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void checkLocalPCTest() {
		System.out.println("LOCAL PC TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x");
		
		// Level(x) = LOW, Level(lpc) = LOW
		assertEquals(true, hs.checkLocalPC("int_x"));
		
		// Level(x) = HIGH, Level(lpc) = LOW
		hs.makeLocalHigh("int_x");
		assertEquals(true, hs.checkLocalPC("int_x"));
		
		// Level(x) = HIGH, Level(lpc) = HIGH
		hs.setLocalPC(SecurityLevel.HIGH);
		assertEquals(true, hs.checkLocalPC("int_x"));
		
		// Level(x) = LOW, Level(lpc) = HIGH
		hs.makeLocalLow("int_x");
		assertEquals(false, hs.checkLocalPC("int_x"));
		
	    hs.close();	
	    
		
		System.out.println("LOCAL PC TEST FINISHED");
	}
	
	@Test
	public void joinLocalsTest() {
		System.out.println("JOIN LOCALS TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x", SecurityLevel.LOW);
		hs.addLocal("int_y", SecurityLevel.HIGH);
		hs.addLocal("int_z", SecurityLevel.LOW);
		assertEquals(SecurityLevel.LOW, hs.joinLocals("int_x"));
		assertEquals(SecurityLevel.HIGH, hs.joinLocals("int_x", "int_y"));		
		assertEquals(SecurityLevel.HIGH, hs.joinLocals("int_x", "int_y", "int_z"));
		
		
		hs.setLocalPC(SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, hs.joinLocals("int_x"));

	    hs.close();	
		
		System.out.println("JOIN LOCALS TEST FINISHED");
	}
	
	@Test
	public void localPCTest() {
		System.out.println("LOCAL PC TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();

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

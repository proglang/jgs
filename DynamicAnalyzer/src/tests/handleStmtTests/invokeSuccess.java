package tests.handleStmtTests;

import static org.junit.Assert.*;
import main.level2.TestSubClass;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;
import analyzer.level2.storage.ObjectMap;

public class invokeSuccess {

	@Test
	public void invokeNewObject() {
		
		System.out.println("INVOKE TEST STARTED");

		ObjectMap m = ObjectMap.getInstance();
		assertEquals(0, m.sizeOfLocalMapStack());
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * Invoke New Stmt
		 * 1. Add new Object to ObjectMap
		 * 2. Add Fields to ObjectMap
		 */
		TestSubClass xy = new TestSubClass();
		assertTrue(hs.containsObjectInObjectMap(xy));
		assertEquals(1, hs.getNumberOfFields(xy));
	    hs.close();	
	    
	    System.out.println("INVOKE TEST FINISHED");
	}
	
	@Test
	public void staticInvoke() {
		// TODO: should be the same as other invoke
	}
	
	@Test
	public void invokeMethodWithoutArguments() {
		
		System.out.println("INVOKE METHOD WITHOUT ARGUMENTS TEST STARTED");

		ObjectMap m = ObjectMap.getInstance();
		assertEquals(0, m.sizeOfLocalMapStack());
		
		HandleStmtForTests hs = new HandleStmtForTests();
		/*
		 *  Invoke Method
		 *  1. Create new LocalMap
		 *  2. Add Locals to LocalMap
		 */
	    hs.close();	
	    
		System.out.println("INVOKE METHOD WITHOUT ARGUMENTS TEST STARTED");
		
    	}
	
	@Test
	public void invokeMethodWithArguments() {		
		
		System.out.println("INVOKE METHOD WITH ARGUMENTS TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    assertEquals(0, m.sizeOfLocalMapStack());
	    
		HandleStmtForTests hs = new HandleStmtForTests();
    	TestSubClass xy = new TestSubClass();
    	int a = 0;
    	int b = 1;
    	int c = 2;
		/*
		 *  Invoke Method With Arguments
		 *  1. Store argument levels in argument list
		 *  2. Create New Local Map
		 *  3. Add it to LocalMapStack
		 *  4. Update gpc
		 */
    	hs.storeArgumentLevels("int_a", "int_b", "int_c");
    	assertEquals(3, m.getActualArguments().size());
    	xy.methodWithParams(a, b, c);
    	assertEquals(Level.LOW, hs.getActualReturnLevel());
    	
    	
    	hs.makeLocalHigh("int_b");
    	hs.storeArgumentLevels("int_a", "int_b", "int_c");
    	assertEquals(3, m.getActualArguments().size());
    	xy.methodWithParams(a, b, c);
    	assertEquals(Level.HIGH, hs.getActualReturnLevel());
    	
	    hs.close();
	    assertEquals(0, m.sizeOfLocalMapStack());
	    
		System.out.println("INVOKE METHOD WITH ARGUMENTS TEST STARTED");
		
	}

}

package tests.handleStmtTests;

import static org.junit.Assert.*;
import tests.testClasses.TestSubClass;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmt;
import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;
import analyzer.level2.storage.ObjectMap;

public class invokeSuccess {

	private HandleStmt hs;

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
	
	@Test
	public void nestedMethodsTest() {
		
		System.out.println("INVOKE NESTED METHODS TEST STARTED");
		
		
		class SomeClass {

			public SomeClass() {
				HandleStmtForTests hs = new HandleStmtForTests();
				hs.addObjectToObjectMap(this);
				
				hs.close();
			}
			
			public void method1() {
				HandleStmtForTests hs = new HandleStmtForTests();
				ObjectMap om = ObjectMap.getInstance();
				
				assertEquals(2, om.getLocalMapStack().size());
				
				method2();

				assertEquals(2, om.getLocalMapStack().size());
				
				hs.close();
			}
			
			public void method2() {
				HandleStmtForTests hs = new HandleStmtForTests();
				ObjectMap om = ObjectMap.getInstance();

				assertEquals(3, om.getLocalMapStack().size());
				
				method3();

				assertEquals(3, om.getLocalMapStack().size());
				
				hs.close();
			}
			
			public void method3() {
				HandleStmtForTests hs = new HandleStmtForTests();
				ObjectMap om = ObjectMap.getInstance();

				assertEquals(4, om.getLocalMapStack().size());
				
				hs.close();
			}
			
		}
		HandleStmtForTests hs = new HandleStmtForTests();
		ObjectMap om = ObjectMap.getInstance();
		
		assertEquals(0, hs.getNumberOfElements());
		assertEquals(1, om.getLocalMapStack().size());
		
		hs.addObjectToObjectMap(this);		
		hs.addFieldToObjectMap(this, "SomeClass_sc");
		
		assertEquals(1, hs.getNumberOfElements());
		assertEquals(1, om.getLocalMapStack().size());
		
		SomeClass sc = new SomeClass();
		
		assertEquals(2, hs.getNumberOfElements());
		assertEquals(1, om.getLocalMapStack().size());
		
		sc.method1();
		
		assertEquals(1, om.getLocalMapStack().size());
		
		hs.close();

		System.out.println("INVOKE NESTED METHODS TEST FINISHED");
	}

}

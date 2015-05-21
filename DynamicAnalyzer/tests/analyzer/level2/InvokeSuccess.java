package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Logger;
import tests.testClasses.TestSubClass;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmt;
import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;

public class InvokeSuccess {

	Logger LOGGER = L2Logger.getLogger();

	private ObjectMap m = ObjectMap.getInstance();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void invokeNewObject() {
		
		LOGGER.log(Level.INFO, "INVOKE TEST STARTED");

		
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
	    
	    LOGGER.log(Level.INFO, "INVOKE TEST FINISHED");
	}
	
	@Test
	public void invokeMethodWithoutArguments() {
		
		LOGGER.log(Level.INFO, "INVOKE METHOD WITHOUT ARGUMENTS TEST STARTED");

		ObjectMap m = ObjectMap.getInstance();
		
		HandleStmtForTests hs = new HandleStmtForTests();
		/*
		 *  Invoke Method
		 *  1. Create new LocalMap
		 *  2. Add Locals to LocalMap
		 */
	    hs.close();	
	    
	    LOGGER.log(Level.INFO, "INVOKE METHOD WITHOUT ARGUMENTS TEST STARTED");
		
    	}
	
	@Test
	public void invokeMethodWithArguments() {		
		
		LOGGER.log(Level.INFO, "INVOKE METHOD WITH ARGUMENTS TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
	    
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
    	assertEquals(SecurityLevel.LOW, hs.getActualReturnLevel());
    	
    	
    	hs.makeLocalHigh("int_b");
    	hs.storeArgumentLevels("int_a", "int_b", "int_c");
    	assertEquals(3, m.getActualArguments().size());
    	xy.methodWithParams(a, b, c);
    	assertEquals(SecurityLevel.HIGH, hs.getActualReturnLevel());
    	
	    hs.close();
	    
	    LOGGER.log(Level.INFO, "INVOKE METHOD WITH ARGUMENTS TEST STARTED");		
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
				
				
				method2();

				
				hs.close();
			}
			
			public void method2() {
				HandleStmtForTests hs = new HandleStmtForTests();
				ObjectMap om = ObjectMap.getInstance();

				
				method3();

				
				hs.close();
			}
			
			public void method3() {
				HandleStmtForTests hs = new HandleStmtForTests();
				ObjectMap om = ObjectMap.getInstance();

				
				hs.close();
			}
			
		}
		HandleStmtForTests hs = new HandleStmtForTests();
		ObjectMap om = ObjectMap.getInstance();
		
		assertEquals(0, hs.getNumberOfElements());
		
		hs.addObjectToObjectMap(this);		
		hs.addFieldToObjectMap(this, "SomeClass_sc");
		
		assertEquals(1, hs.getNumberOfElements());
		
		SomeClass sc = new SomeClass();
		
		assertEquals(2, hs.getNumberOfElements());
		
		sc.method1();
		
		
		hs.close();

		System.out.println("INVOKE NESTED METHODS TEST FINISHED");
	}

}

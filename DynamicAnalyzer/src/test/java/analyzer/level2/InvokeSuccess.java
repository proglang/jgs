package analyzer.level2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import tests.testclasses.TestSubClass;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;



public class InvokeSuccess {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void invokeNewObject() {
		
		logger.log(Level.INFO, "INVOKE TEST STARTED");

		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("TestSubClass_xy");
		/*
		 * Invoke New Stmt
		 * 1. Add new Object to ObjectMap
		 * 2. Add Fields to ObjectMap
		 */
		
		hs.setLocalToCurrentAssignmentLevel("TestSubClass_xy");
		TestSubClass xy = new TestSubClass();
		assertTrue(hs.containsObjectInObjectMap(xy));
		assertEquals(2, hs.getNumberOfFieldsInObjectMap(xy)); // The third field is static
		hs.close();	
	    
		logger.log(Level.INFO, "INVOKE TEST FINISHED");
	}
	
	@Test
	public void invokeMethodWithoutArguments() {
		
		logger.log(Level.INFO, "INVOKE METHOD WITHOUT ARGUMENTS TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		/*
		 *  Invoke Method
		 *  1. Create new LocalMap
		 *  2. Add Locals to LocalMap
		 */
		hs.close();	
	    
		logger.log(Level.INFO, "INVOKE METHOD WITHOUT ARGUMENTS TEST STARTED");
		
    	}
	
	@Test
	public void invokeMethodWithArguments() {		
		
		logger.log(Level.INFO, "INVOKE METHOD WITH ARGUMENTS TEST STARTED");
	    
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		TestSubClass xy = new TestSubClass();
		int a = 0;
		int b = 1;
		int c = 2;
		hs.addLocal("int_a");
		hs.addLocal("int_b");
		hs.addLocal("int_c");
			
		
		/*
		 *  Invoke Method With Arguments
		 *  1. Store argument levels in argument list
		 *  2. Create New Local Map
		 *  3. Add it to LocalMapStack
		 *  4. Update gpc
		 */
		hs.storeArgumentLevels("int_a", "int_b", "int_c");
		xy.methodWithParams(a, b, c);
		assertEquals(CurrentSecurityDomain.bottom(), hs.getActualReturnLevel());
    	
    	
		hs.setLocalFromString("int_b", "HIGH");
		hs.storeArgumentLevels("int_a", "int_b", "int_c");
		xy.methodWithParams(a, b, c);
		assertEquals(CurrentSecurityDomain.top(), hs.getActualReturnLevel());
    	
		hs.close();
	    
		logger.log(Level.INFO, "INVOKE METHOD WITH ARGUMENTS TEST STARTED");		
	}
	
	@Test
	public void nestedMethodsTest() {
		
		logger.log(Level.INFO, "INVOKE NESTED METHODS TEST STARTED");
		
		
		class SomeClass {

			public SomeClass() {
				HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
				hs.addObjectToObjectMap(this);
				
				hs.close();
			}
			
			public void method1() {
				HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
				
				
				method2();

				
				hs.close();
			}
			
			public void method2() {
				HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);

				
				method3();

				
				hs.close();
			}
			
			public void method3() {
				HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);

				
				hs.close();
			}
			
		}
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		assertEquals(0, hs.getNumberOfElementsInObjectMap());
		
		hs.addObjectToObjectMap(this);		
		hs.addFieldToObjectMap(this, "SomeClass_sc");
		
		assertEquals(1, hs.getNumberOfElementsInObjectMap());
		
		SomeClass sc = new SomeClass();
		
		assertEquals(2, hs.getNumberOfElementsInObjectMap());
		
		sc.method1();
		
		
		hs.close();

		logger.log(Level.INFO, "INVOKE NESTED METHODS TEST FINISHED");
	}

}

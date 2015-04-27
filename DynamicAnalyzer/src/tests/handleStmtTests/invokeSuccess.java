package tests.handleStmtTests;

import static org.junit.Assert.*;
import main.level2.TestSubClass;

import org.junit.Test;

import analyzer.level2.HandleStmtForTests;

public class invokeSuccess {

	@Test
	public void invokeStmtTest() {
		
		System.out.println("INVOKE TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		/*
		 * Invoke New Stmt
		 * 1. Add new Object to ObjectMap
		 * 2. Add Fields to ObjectMap
		 */
		TestSubClass xy = new TestSubClass();
		assertTrue(hs.containsObjectInObjectMap(xy));
		assertEquals(1, hs.getNumberOfFields(xy));
		
		/*
		 *  Static Invoke
		 */
		// TODO: shold be the same as New Stmt
		
		/*
		 *  Invoke Method
		 *  1. Create new LocalMap
		 *  2. Add Locals to LocalMap
		 */
		
		System.out.println("INVOKE TEST FINISHED");
	}

}

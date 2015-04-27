package tests.handleStmtTests;

import static org.junit.Assert.*;
import main.level2.TestSubClass;

import org.junit.Test;

import analyzer.level2.HandleStmtForTests;

public class invokeSuccess {

	@Test
	public void invokeNewObject() {
		
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
		
	}
	
	@Test
	public void staticInvoke() {
		// TODO: should be the same as other invoke
	}
	
	@Test
	public void invokeMethodWithoutArguments() {
		/*
		 *  Invoke Method
		 *  1. Create new LocalMap
		 *  2. Add Locals to LocalMap
		 */
	}
	
	@Test
	public void invokeMethodWithArguments() {
		/*
		 *  Invoke Method
		 *  1. Create new LocalMap
		 *  2. Add Locals to LocalMap
		 */
	}

}

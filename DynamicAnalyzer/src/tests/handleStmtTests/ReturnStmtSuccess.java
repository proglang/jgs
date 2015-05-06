package tests.handleStmtTests;

import static org.junit.Assert.*;
import tests.testClasses.TestSubClass;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;

public class ReturnStmtSuccess {
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void returnStmtTest() {
		
		System.out.println("RETURN TEST STARTED");

		ObjectMap m = ObjectMap.getInstance();
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.setLocalPC(SecurityLevel.LOW);
		assertEquals(SecurityLevel.LOW, hs.addFieldToObjectMap(this, "int_resField1"));
		assertEquals(SecurityLevel.LOW, hs.addFieldToObjectMap(this, "int_resField2"));
		assertEquals(SecurityLevel.LOW, hs.addFieldToObjectMap(this, "int_resField3"));
		
		
		int resField1;
		int resField2;
		int resField3;
		
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "int_resField1"));
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "int_resField2"));
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "int_resField3"));
		
		TestSubClass tsc = new TestSubClass();
		tsc.methodWithConstReturn();
		assertEquals(SecurityLevel.LOW, hs.getActualReturnLevel());
		tsc.methodWithLowLocalReturn();
		assertEquals(SecurityLevel.LOW, hs.getActualReturnLevel());
		tsc.methodWithHighLocalReturn();
		assertEquals(SecurityLevel.HIGH, hs.getActualReturnLevel());
		
		
		tsc.method();
		
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "int_resField1"));
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "int_resField2"));
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(this, "int_resField3"));
		
	    hs.close();	
	    
		System.out.println("RETURN TEST FINISHED");
	}

}

package tests.handleStmtTests;

import static org.junit.Assert.*;
import tests.testClasses.TestSubClass;

import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.Level;
import analyzer.level2.storage.ObjectMap;

public class returnStmtSuccess {

	@Test
	public void returnStmtTest() {
		
		System.out.println("RETURN TEST STARTED");

		ObjectMap m = ObjectMap.getInstance();
		assertEquals(0, m.sizeOfLocalMapStack());
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.setLocalPC(Level.LOW);
		assertEquals(Level.LOW, hs.addFieldToObjectMap(this, "int_resField1"));
		assertEquals(Level.LOW, hs.addFieldToObjectMap(this, "int_resField2"));
		assertEquals(Level.LOW, hs.addFieldToObjectMap(this, "int_resField3"));
		
		
		int resField1;
		int resField2;
		int resField3;
		
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField1"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField2"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField3"));
		
		TestSubClass tsc = new TestSubClass();
		tsc.methodWithConstReturn();
		assertEquals(Level.LOW, hs.getActualReturnLevel());
		tsc.methodWithLowLocalReturn();
		assertEquals(Level.LOW, hs.getActualReturnLevel());
		tsc.methodWithHighLocalReturn();
		assertEquals(Level.HIGH, hs.getActualReturnLevel());
		
		
		tsc.method();
		
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField1"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField2"));
		assertEquals(Level.LOW, hs.getFieldLevel(this, "int_resField3"));
		
	    hs.close();	
	    
		System.out.println("RETURN TEST FINISHED");
	}

}

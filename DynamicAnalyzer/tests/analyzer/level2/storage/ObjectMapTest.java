package analyzer.level2.storage;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmt;
import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;

public class ObjectMapTest {

	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void singletonTest() {
		
		System.out.println("OBEJCT MAP AS SINGLETON TEST STARTED");
		
		ObjectMap m1 = ObjectMap.getInstance();
		ObjectMap m2 = ObjectMap.getInstance();
		
		assertSame("The two instances of ObjectMap are not the same", m1, m2);
		
		System.out.println("OBEJCT MAP AS SINGLETON TEST FINISHED");
	}
	
	@Test
	public void globalPCTest() {
		
		System.out.println("GLOBAL PC TEST STARTED");
		
		ObjectMap m = ObjectMap.getInstance();
		assertEquals(SecurityLevel.LOW, m.getGlobalPC());
		m.pushGlobalPC(SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, m.getGlobalPC());

		System.out.println("GLOBAL PC TEST FINISHED");
	}
	
	@Test
	public void insertNewObjectTest() {
		
		System.out.println("INSERT NEW OBJECT TEST STARTED");
		
		ObjectMap m = ObjectMap.getInstance();
		
		Object o1 = new Object();
		Object o2 = new Object();
		assertNotSame(o1, o2);
		
		m.insertNewObject(o1);
		m.insertNewObject(o2);
		
		assertEquals(2, m.getNumberOfElements()); // Da sind noch die Objekte aus den anderen Tests drin
		
		// The same object should not be inserted a second time
		m.insertNewObject(o1);
		//assertEquals(2, m.getNumberOfElements());
		
		System.out.println("INSERT NEW OBJECT TEST FINISHED");
	}
	
	@Test
	public void fieldsTest() {

		System.out.println("FIELDS IN OBJECT MAP TEST STARTED");
		
		ObjectMap m = ObjectMap.getInstance();
		Object o = new Object();
		m.insertNewObject(o);
		
		String f1 = "<int i1>";
		String f2 = "<int i2>";
		String f3 = "<String s1>";
		
		m.setField(o, f1, SecurityLevel.LOW);
		m.setField(o, f2);
		m.setField(o, f3, SecurityLevel.HIGH);
		
		Object o2 = new Object();
		m.insertNewObject(o2);
		
		m.setField(o2, f1, SecurityLevel.HIGH);
		m.setField(o2, f2, SecurityLevel.LOW);
		m.setField(o2, f3);

		System.out.println("FIELDS IN OBJECT MAP TEST FINISHED");
	}
	
	@Test 
	public void multipleObjectsTest() {

		System.out.println("MULTIPLE OBJECTS TEST STARTED");
		
		Logger l = L2Logger.getLogger();
		
		HandleStmtForTests hs = new HandleStmtForTests();
		ObjectMap m = ObjectMap.getInstance();
		int numOfEl = m.getNumberOfElements();
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this);
		// The map should contain the same object twice
		assertEquals(numOfEl + 1, m.getNumberOfElements());
		Integer i = new Integer(3);
		hs.addObjectToObjectMap(i);
		assertEquals(numOfEl + 2, m.getNumberOfElements());
		hs.close();
		
		System.out.println("MULTIPLE OBJECTS TEST FINISHED");
	}


}

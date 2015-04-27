package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import analyzer.level2.Level;
import analyzer.level2.storage.ObjectMap;

public class ObjectMapTest {


	@Test
	public void singletonTest() {
		ObjectMap m1 = ObjectMap.getInstance();
		ObjectMap m2 = ObjectMap.getInstance();
		
		assertSame("The two instances of ObjectMap are not the same", m1, m2);
	}
	
	@Test
	public void globalPCTest() {
		ObjectMap m = ObjectMap.getInstance();
		assertEquals(Level.LOW, m.getGlobalPC());
		m.setGlobalPC(Level.HIGH);
		assertEquals(Level.HIGH, m.getGlobalPC());
	}
	
	@Test
	public void insertNewObjectTest() {
		ObjectMap m = ObjectMap.getInstance();
		
		Object o1 = new Object();
		Object o2 = new Object();
		assertNotSame(o1, o2);
		
		m.insertNewObject(o1);
		m.insertNewObject(o2);
		
		//assertEquals(2, m.getNumberOfElements()); // Da sind noch die Objekte aus den anderen Tests drin
		
		// The same object should not be inserted a second time
		m.insertNewObject(o1);
		//assertEquals(2, m.getNumberOfElements());
	}
	
	@Test
	public void fieldsTest() {
		ObjectMap m = ObjectMap.getInstance();
		Object o = new Object();
		m.insertNewObject(o);
		
		String f1 = "<int i1>";
		String f2 = "<int i2>";
		String f3 = "<String s1>";
		
		m.setField(o, f1, Level.LOW);
		m.setField(o, f2);
		m.setField(o, f3, Level.HIGH);
		
		Object o2 = new Object();
		m.insertNewObject(o2);
		
		m.setField(o2, f1, Level.HIGH);
		m.setField(o2, f2, Level.LOW);
		m.setField(o2, f3);
	}

}

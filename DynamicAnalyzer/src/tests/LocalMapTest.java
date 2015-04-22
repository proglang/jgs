package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import analyzer.level2.Level;
import analyzer.level2.storage.LocalMap;

public class LocalMapTest {

	@Test
	public void testInsertElementStringLevel() {
		LocalMap lm = new LocalMap();
		lm.insertElement("a1", Level.LOW);
		lm.insertElement("a2");
		lm.insertElement("a3", Level.HIGH);
		assertSame(Level.LOW, lm.getLevel("a1"));
		assertSame(Level.LOW, lm.getLevel("a2"));
		assertSame(Level.HIGH, lm.getLevel("a3"));	
	}

	@Test
	public void testInsertElementString() {
		
	}

	@Test
	public void testGetLevel() {
		LocalMap lm = new LocalMap();
		assertSame(Level.LOW, lm.getLevel("a"));
	}

	@Test
	public void testSetLevel() {
	}

	@Test
	public void testPrintElements() {
		LocalMap lm = new LocalMap();
		lm.printElements();
		
	}

}

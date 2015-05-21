package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.LocalMap;

public class LocalMapTest {
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void testInsertElementStringLevel() {
		LocalMap lm = new LocalMap();
		lm.insertElement("a1", SecurityLevel.LOW);
		lm.insertElement("a2");
		lm.insertElement("a3", SecurityLevel.HIGH);
		assertSame(SecurityLevel.LOW, lm.getLevel("a1"));
		assertSame(SecurityLevel.LOW, lm.getLevel("a2"));
		assertSame(SecurityLevel.HIGH, lm.getLevel("a3"));	
	}

	@Test
	public void testInsertElementString() {
		
	}

	@Test
	public void testGetLevel() {
		LocalMap lm = new LocalMap();
		assertSame(SecurityLevel.LOW, lm.getLevel("a"));
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

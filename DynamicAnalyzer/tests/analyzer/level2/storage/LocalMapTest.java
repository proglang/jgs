package analyzer.level2.storage;

import static org.junit.Assert.assertSame;

import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.LocalMap;

import org.junit.Before;
import org.junit.Test;

public class LocalMapTest {
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void testInsertElementStringLevel() {
		LocalMap lm = new LocalMap();
		lm.insertLocal("a1", SecurityLevel.bottom());
		lm.insertLocal("a2");
		lm.insertLocal("a3", SecurityLevel.top());
		assertSame(SecurityLevel.bottom(), lm.getLevel("a1"));
		assertSame(SecurityLevel.bottom(), lm.getLevel("a2"));
		assertSame(SecurityLevel.top(), lm.getLevel("a3"));	
	}

	@Test
	public void testInsertElementString() {
		
	}

	@Test
	public void testGetLevel() {
		LocalMap lm = new LocalMap();
		lm.insertLocal("a");
		assertSame(SecurityLevel.bottom(), lm.getLevel("a"));
	}

	@Test
	public void testPrintElements() {
		LocalMap lm = new LocalMap();
		lm.printElements();
		
	}

}

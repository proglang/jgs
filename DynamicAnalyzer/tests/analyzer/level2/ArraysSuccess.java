package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

public class ArraysSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void createArrayTest() {

		LOGGER.log(Level.INFO, "CREATE ARRAY SUCCESS TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		hs.addLocal("String[]_a");
		String[] a = new String[] {"asd", "", ""};
		hs.addArrayToObjectMap(a);
		hs.addLocal("String[]_a");
		
		assertEquals(3, hs.getNumberOfFields(a));
		
		hs.close();

		LOGGER.log(Level.INFO, "CREATE ARRAY SUCCESS TEST FINISHED");
	}
	
	@Test
	public void readArray() {
		
		LOGGER.log(Level.INFO, "READ ARRAY SUCCESS TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"asd", "", ""};
		hs.addArrayToObjectMap(a);
		hs.addLocal("String[]_a");
		hs.addLocal("int_i");
		
		assertTrue(hs.containsObjectInObjectMap(a));
		assertEquals(3, hs.getNumberOfFields(a));
		
		/*
		 * check ( x >= lpc)
		 * x = Join(i,a, lpc, a_i)
		 */
		int i = 2;
		hs.addLevelOfArrayField(a, i);
		hs.addLevelOfLocal("int_i");
		hs.addLevelOfLocal("String[]_a");
		hs.addLocal("String_x");
		hs.setLevelOfLocal("String_x");
		String x = a[i];
		
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("String_x"));
		
		hs.close();
		
		LOGGER.log(Level.INFO, "READ ARRAY SUCCESS TEST FINISHED");
		
	}
	
	@Test
	public void writeArray() {

		LOGGER.log(Level.INFO, "WRITE ARRAY SUCCESS TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"a", "b", "c"};
		assertEquals(3, a.length);
		hs.addArrayToObjectMap(a);
		assertTrue(hs.containsObjectInObjectMap(a));
		assertEquals(3, hs.getNumberOfFields(a));
		
		hs.addLocal("String[]_a");
		hs.addLocal("int_i");
		
		/*
		 * check(a_i >= join(gpc, a, i))
		 * level(a_i) = join(gpc,local, i)
		 */
		int i = 2;
		hs.setLevelOfArrayField(a, i, "String[]_a", "int_i");
		a[i] = "3";
		
		assertEquals(SecurityLevel.LOW, hs.getFieldLevel(a, Integer.toString(i)));
		
		hs.close();
		
		LOGGER.log(Level.INFO, "WRITE ARRAY SUCCESS TEST FINISHED");
		
	}

}

package analyzer.level2;

import static org.junit.Assert.*;

import utils.exceptions.IllegalFlowException;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

public class ArraysFail {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}
	
	@Test(expected = IllegalFlowException.class)
	public void readArray() {
		
		LOGGER.log(Level.INFO, "READ ARRAY FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"asd", "", ""};
		hs.addArrayToObjectMap(a);
		hs.addLocal("String[]_a");
		int i = 2;
		hs.addLocal("int_i");
		hs.pushLocalPC(SecurityLevel.HIGH);
		
		
		assertTrue(hs.containsObjectInObjectMap(a));
		
		/*
		 * x = a[i]
		 * check x >= lpc
		 * level(x) = (i, a, gpc, a_i)
		 */
		hs.addLevelOfArrayField(a, Integer.toString(i));
		hs.addLevelOfLocal("int_i");
		hs.addLevelOfLocal("String[]_a");
		hs.setLevelOfLocal("String_x");
		@SuppressWarnings("unused")
		String x = a[i];
		
		
		hs.close();
		
		LOGGER.log(Level.INFO, "READ ARRAY FAIL TEST FINISHED");
		
	}
	
	@Test(expected = IllegalFlowException.class)
	public void writeArray() {

		LOGGER.log(Level.INFO, "WRITE ARRAY FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"asd", "v", "v"};
		assertEquals(3, a.length);
		hs.addArrayToObjectMap(a);
		assertTrue(hs.containsObjectInObjectMap(a));
		assertEquals(3, hs.getNumberOfFields(a));
		hs.setLevelOfLocal("String[]_a", SecurityLevel.HIGH);
		
		
		/*
		 * check(a_i >= join(gpc, a, i))
		 * level(a_i) = join(gpc,local, i)
		 */
		hs.setLevelOfArrayField(a, Integer.toString(2), "String[]_a");
		a[2] = "3";
		
		
		
		hs.close();
		
		LOGGER.log(Level.INFO, "WRITE ARRAY FAIL TEST FINISHED");
		
	}

}

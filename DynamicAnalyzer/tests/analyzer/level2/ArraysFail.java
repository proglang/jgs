package analyzer.level2;

import static org.junit.Assert.*;

import exceptions.IllegalFlowException;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

public class ArraysFail {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void createArrayTest() {

		LOGGER.log(Level.INFO, "CREATE ARRAY FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		hs.addLocal("String[]_a");
		String[] a = new String[] {"asd", "", ""};
		hs.addArrayToObjectMap(a);
		
		assertEquals(3, hs.getNumberOfFields(a));
		
		hs.close();

		LOGGER.log(Level.INFO, "CREATE ARRAY FAIL TEST FINISHED");
	}
	
	@Test
	public void readArray() {
		
		LOGGER.log(Level.INFO, "READ ARRAY FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"asd", "", ""};
		hs.addArrayToObjectMap(a);
		
		assertTrue(hs.containsObjectInObjectMap(a));
		
		/*
		 * x = Join(i,a, gpc, a_i)
		 */
		hs.assignArrayFieldToLocal("String_x", a , Integer.toString(2));
		String x = a[2];
		
		int i = 1;
		hs.assignArrayFieldToLocal("String_x", a , Integer.toString(2));
		x = a[i];
		
		hs.close();
		
		LOGGER.log(Level.INFO, "READ AARRAY FAIL TEST FINISHED");
		
	}
	
	@Test
	public void writeArray() {

		LOGGER.log(Level.INFO, "WRITE ARRAY FAIL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"asd", "v", "v"};
		assertEquals(3, a.length);
		hs.addArrayToObjectMap(a);
		assertTrue(hs.containsObjectInObjectMap(a));
		assertEquals(3, hs.getNumberOfFields(a));
		
		/*
		 * check(a_t >= pgc)
		 * level(a) = join(gpc,local, ??i??)
		 * level(a_i) = join(gpc,local, ??i??)
		 * i = ??
		 */
		hs.assignLocalsToArrayField(a, Integer.toString(2));
		a[2] = "3";
		
		int i = 2;
		hs.assignLocalsToArrayField(a, Integer.toString(2));
		a[i] = "3";
		
		hs.close();
		
		LOGGER.log(Level.INFO, "WRITE ARRAY FAIL TEST FINISHED");
		
	}

}

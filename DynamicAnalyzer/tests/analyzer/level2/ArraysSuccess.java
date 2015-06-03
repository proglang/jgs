package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Logger;

import org.junit.Test;

public class ArraysSuccess {
	
	Logger LOGGER = L2Logger.getLogger();

	@Test
	public void createArrayTest() {

		LOGGER.log(Level.INFO, "CREATE ARRAY SUCCESS TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		hs.addLocal("String[] a");
		String[] a = new String[] {"asd", "", ""};
		hs.addObjectToObjectMap(a);
		for(int i = 0; i < a.length ; i++) {
			hs.addFieldToObjectMap(a, Integer.toString(i));
		}
		
		assertEquals(3, hs.getNumberOfFields(a));
		
		hs.close();

		LOGGER.log(Level.INFO, "CREATE ARRAY SUCCESS TEST FINISHED");
	}
	
	@Test
	public void readArray() {
		
		LOGGER.log(Level.INFO, "READ ARRAY SUCCESS TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"asd", "", ""};
		
		/*
		 * x = Join(i,a, pgc)
		 */
		hs.assignArrayFieldToLocal("String_x", a , Integer.toString(2));
		String x = a[2];
		
		int i = 3;
		hs.assignArrayFieldToLocal("String_x", a , Integer.toString(2));
		x = a[i];
		
		hs.close();
		
		LOGGER.log(Level.INFO, "READ AARRAY SUCCESS TEST FINISHED");
		
	}
	
	@Test
	public void writeArray() {

		LOGGER.log(Level.INFO, "WRITE ARRAY SUCCESS TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"asd", "", ""};
		
		/*
		 * check(pgc)
		 * level(a) = join(gpc,local, ??i??)
		 * i = ??
		 */
		hs.assignLocalsToArrayField(a, Integer.toString(2), "int_3");
		a[2] = "3";
		
		int i = 4;
		hs.assignLocalsToArrayField(a, Integer.toString(2), "int_3");
		a[i] = "3";
		
		hs.close();
		
		LOGGER.log(Level.INFO, "WRITE ARRAY SUCCESS TEST FINISHED");
		
	}

}

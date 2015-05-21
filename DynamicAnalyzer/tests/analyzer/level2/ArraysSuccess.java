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
	// TODO Wie sieht es in Jimple aus?
		
		hs.addLocal("String[] a");
		String[] a = new String[] {"asd", "", ""};
		hs.addObjectToObjectMap(a);
		// TODO hs.addFieldToObjectMap(a, "")
		
		
		hs.close();

		LOGGER.log(Level.INFO, "CREATE ARRAY SUCCESS TEST FINISHED");
	}
	
	@Test
	public void readArray() {
		
		LOGGER.log(Level.INFO, "READ ARRAY SUCCESS TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"asd", "", ""};
		String x = a[2];
		
		hs.close();
		
		LOGGER.log(Level.INFO, "READ AARRAY SUCCESS TEST FINISHED");
		
	}
	
	@Test
	public void writeArray() {

		LOGGER.log(Level.INFO, "WRITE ARRAY SUCCESS TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[] a = new String[] {"asd", "", ""};
		a[2] = "3";
		
		hs.close();
		
		LOGGER.log(Level.INFO, "WRITE ARRAY SUCCESS TEST FINISHED");
		
	}

}

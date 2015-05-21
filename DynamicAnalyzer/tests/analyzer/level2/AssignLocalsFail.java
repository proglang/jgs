package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.ObjectMap;
import exceptions.IllegalFlowException;

public class AssignLocalsFail {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test(expected = IllegalFlowException.class)
	public void assignConstantToLocal() {
		
		LOGGER.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x", SecurityLevel.LOW);
		hs.setLocalPC(SecurityLevel.HIGH);
		// x = LOW, lpc = HIGH
		hs.assignLocalsToLocal("int_x");
		hs.close();
		
		LOGGER.log(Level.INFO, "ASSIGN CONSTANT TO LOCAL TEST FINISHED");
	}
	

	@Test(expected = IllegalFlowException.class)
	public void assignLocalsToLocal() {
		
		LOGGER.log(Level.INFO, "ASSIGN LOCALS TO LOCAL TEST STARTED");

	    ObjectMap m = ObjectMap.getInstance();
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_x");
		hs.addLocal("int_y");
		hs.addLocal("int_z", SecurityLevel.HIGH);
		
		/*
		 *  Assign Locals to Local
		 *  int x = y + z;
		 *  1. Check if Level(x) >= lpc
		 *  2. Assign Join(y, z, lpc) to x
		 */
		hs.setLocalPC(SecurityLevel.HIGH);
		// x = LOW, lpc = HIGH
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_x"));
		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("int_y"));
		assertEquals(SecurityLevel.HIGH, hs.getLocalLevel("int_z"));
		assertEquals(SecurityLevel.HIGH, hs.getLocalPC());
		hs.assignLocalsToLocal("int_x", "int_y", "int_z");
		
		
	}
	
	
	@Test
	public void assignNewObjectToLocal() {
		LOGGER.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST STARTED");
		
		

		LOGGER.log(Level.INFO, "ASSIGN NEW OBJECT TO LOCAL FAIL TEST FINISHED");
		
	}
	
	@Test
	public void assignMethodResultToLocal() {

		LOGGER.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
		

		LOGGER.log(Level.INFO, "ASSIGN METHOD RESULT TO LOCAL FAIL TEST STARTED");
	
	}
	
	@Test
	public void assignConstantAndLocalToLocal() {
		// TODO
	}

}

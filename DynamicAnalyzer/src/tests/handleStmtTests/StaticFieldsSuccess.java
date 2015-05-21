package tests.handleStmtTests;

import static org.junit.Assert.*;

import exceptions.IllegalFlowException;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import tests.testClasses.TestSubClass;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

public class StaticFieldsSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	static int field;
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@Test
	public void internalStaticFieldSuccessTest() {
		
		LOGGER.log(Level.INFO, "STATIC FIELD SUCCESS TEST STARTED");

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		
		LOGGER.log(Level.INFO, "STATIC FIELD SUCCESS TEST STARTED");
		
	}
	
	@Test
	public void externalStaticFieldSuccessTest() {
		
		LOGGER.log(Level.INFO, "STATIC FIELD SUCCESS TEST STARTED");

		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this.getClass());
		
		LOGGER.log(Level.INFO, "STATIC FIELD SUCCESS TEST STARTED");
		
	}
	

}

package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import utils.logging.L2Logger;

public class MultiArraySuccess {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}
	
	@Test
	public void createArray() {
		
		logger.info("createArray success test started");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[][] twoD = new String[2][2];
		hs.addArrayToObjectMap(twoD);
		hs.addFieldToObjectMap(twoD, "1");
		hs.addFieldToObjectMap(twoD, "2");

		// TODO
		
		hs.close();
		
		logger.info("createArray success test finished");
	}
	
	@Test
	public void findNewInstancesOfElements() {
		logger.info("createArray success test started");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[][] twoD = new String[2][2];
		hs.addArrayToObjectMap(twoD);
		hs.addFieldToObjectMap(twoD, "1");
		hs.addFieldToObjectMap(twoD, "2");

		twoD[0][0] = "first element";		
		twoD[0][1] = "second element";		
		twoD[1] = {"third element", "fourth element"};		
		hs.close();
		
		logger.info("createArray success test finished");
	}

	@Test
	public void readArray() {
		
		logger.info("readArray success test started");
		
		fail("Not yet implemented");
		
		logger.info("readArray success test finished");
	}
	
	@Test
	public void writeArray() {

		logger.info("writeArray success test started");
		
		fail("Not yet implemented");
		
		logger.info("writeArray success test finished");
	}
}

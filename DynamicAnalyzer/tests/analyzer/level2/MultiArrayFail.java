package analyzer.level2;


import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import utils.logging.L2Logger;

public class MultiArrayFail {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}
	
	@Test
	public void createArray() {
		
		logger.info("createArray fail test started");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		

		
		hs.close();
		
		logger.info("createArray fail test finished");
	}


	@Test
	public void readArray() {
		
		logger.info("readArray fail test started");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		

		
		hs.close();
		
		logger.info("readArray fail test finished");
	}
	
	@Test
	public void writeArray() {

		logger.info("writeArray fail test started");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		

		
		hs.close();
		
		logger.info("writeArray fail test finished");
	}

}

package analyzer.level2;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.logging.L2Logger;

import org.junit.Before;
import org.junit.Test;

import utils.exceptions.IllegalFlowException;

public class ForStmtFail {

	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}

	@SuppressWarnings("unused")
	@Test(expected = IllegalFlowException.class)
	public void forStmtLocalTestFail() {
		
		LOGGER.log(Level.INFO, "FOR STMT LOCAL TEST FAIL STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addLocal("int_i");
		hs.makeLocalHigh("int_i");
		hs.addLocal("int_res");
		
		int res = 0;
		
		hs.checkCondition("123", "int_i");
		for(int i = 0;i < 1; i++){
			
			hs.setLevelOfLocal("int_res");
			res = 2;
			
			hs.exitInnerScope("123");
		}
		
		hs.close();
		
		LOGGER.log(Level.INFO, "FOR STMT LOCAL TEST FAIL FINISHED");
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalFlowException.class)
	public void forStmtFieldTestFail() {
		
		LOGGER.log(Level.INFO, "FOR STMT FIELD TEST FAIL STARTED");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		hs.addObjectToObjectMap(this);
		hs.addLocal("int_i");
		hs.makeLocalHigh("int_i");
		hs.addFieldToObjectMap(this, "int_res");
		
		int i = 0; // Local
		int res; // Field
		
		hs.checkCondition("123", "int_i");
		for(i = 0;i < 1; i++){
			
			hs.setLevelOfField(this, "int_res");
			res = 2;
			
			hs.exitInnerScope("123");
		}
		
		hs.close();
		
		LOGGER.log(Level.INFO, "FOR STMT FIELD TEST FAIL FINISHED");
	}

}

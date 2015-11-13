package classfiletests;
import java.util.logging.Logger;

import org.junit.Test;

import utils.logging.L1Logger;

public class SimpleTest{
	


	Logger logger = L1Logger.getLogger();
	
	@Test
	public void test() {
		logger.info("Start of executing main.testclasses.Simple");
		
		ClassRunner.runClass2("main.testclasses.Simple");

		logger.info("Finished executing main.testclasses.Simple");
	}

}

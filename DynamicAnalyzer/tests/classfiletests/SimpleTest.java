package classfiletests;

import classfiletests.utils.ClassRunner;
import org.junit.Test;
import utils.logging.L1Logger;

import java.util.logging.Logger;



public class SimpleTest{

	Logger logger = L1Logger.getLogger();

	@Test
	public void test() {
		logger.info("Start of executing main.testclasses.Simple");

		ClassRunner.testClass("Simple", true);

		logger.info("Finished executing main.testclasses.Simple");
	}
}

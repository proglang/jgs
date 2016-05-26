package classfiletests;

import classfiletests.utils.ClassRunner;
import org.junit.Test;
import utils.logging.L1Logger;

import java.util.logging.Logger;


public class InvokeInternalMethodTest {

	Logger logger = L1Logger.getLogger();

	@Test
	public void test() {
		logger.info("Start of executing main.testclasses.InvokeInternalMethod");

		ClassRunner.testClass("InvokeInternalMethod", true);
		logger.info("Finished executing main.testclasses.InvokeInternalMethod");
	}
}
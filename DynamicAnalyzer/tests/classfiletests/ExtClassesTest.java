package classfiletests;


import classfiletests.utils.ClassRunner;
import org.junit.Test;
import utils.logging.L1Logger;

import java.util.logging.Logger;


public class ExtClassesTest {

	Logger logger = L1Logger.getLogger();

	@Test
	public void test() {
		logger.info("Start of executing main.testclasses.ExtClasses");
		ClassRunner.testClass("ExtClasses", true);
		logger.info("Finished executing main.testclasses.ExtClasses");
	}
}
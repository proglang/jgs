package classfiletests;

import classfiletests.utils.ClassRunner;
import org.junit.Test;
import utils.logging.L1Logger;

import java.util.logging.Logger;


public class ArrayRefTest {

	Logger logger = L1Logger.getLogger();

	@Test
	public void test() {
		logger.info("Start of executing main.testclasses.ArrayRef");
		System.out.println("Working Directory = " 
	        		+ System.getProperty("user.dir"));
	        
		ClassRunner.testClass("ArrayRef");

		logger.info("Finished executing main.testclasses.ArrayRef");
	}
}
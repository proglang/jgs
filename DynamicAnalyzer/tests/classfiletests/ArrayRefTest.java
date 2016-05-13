package classfiletests;

import static org.junit.Assert.assertEquals;

import classfiletests.utils.ClassRunner;
import org.junit.Test;
import utils.exceptions.IllegalFlowException;
import utils.logging.L1Logger;

import java.util.logging.Logger;


public class ArrayRefTest {

	Logger logger = L1Logger.getLogger();

	@Test
	public void test() {
		logger.info("Start of executing main.testclasses.ArrayRef");
	        System.out.println("Working Directory = " 
	        		+ System.getProperty("user.dir"));
		Exception catchedException = null;
		try {
			ClassRunner.runClass("ArrayRef");
		} catch (Exception e) {
			catchedException = e;
			System.out.println(e.toString());
			e.printStackTrace();
		}
		assertEquals(catchedException.getCause().getClass(),
				IllegalFlowException.class);
		logger.info("Finished executing main.testclasses.ArrayRef");
	}
}
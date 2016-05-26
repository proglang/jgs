package classfiletests;

import classfiletests.utils.ClassRunner;
import org.junit.Test;
import utils.logging.L1Logger;

import java.util.logging.Logger;


/**
 * 
 * @author koenigr
 */
public class FieldsSuccessTest {

	Logger logger = L1Logger.getLogger();

	@Test
	public void test() {
		logger.info("Start of executing main.testclasses.FieldsSuccess");

		ClassRunner.testClass("FieldsSuccess", true);
      
		logger.info("Finished executing main.testclasses.FieldsSuccess");
      
	}
}
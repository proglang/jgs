package classfiletests;

import classfiletests.utils.ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import utils.logging.L1Logger;

import java.util.Arrays;
import java.util.logging.Logger;

public class SingleTest{
    
	// If you want to test a single class then define its name here
	public String name = "InvokeLibMethod";
	public boolean hasIllegalFlow = true;
    
	Logger logger = L1Logger.getLogger();
	


	@Test
	public void test() {
		System.out.println("\n\n\n");
		logger.info("Start of executing main.testclasses." + name + "");

		ClassRunner.testClass(name, hasIllegalFlow, "int_i0");

		logger.info("Finished executing main.testclasses." + name + "");
	}
}

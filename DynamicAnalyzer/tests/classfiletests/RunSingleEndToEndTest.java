package classfiletests;

import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import utils.logging.L1Logger;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Class to run a single test. Note that three pieces of information need to be supplied:
 * Name, hasIllegalFlow, and involvedVars!
 * @author NicolasM
 *
 */
public class RunSingleEndToEndTest{
    
	// If you want to test a single class then define its name here
	public String name = "AccessFieldsOfObjectsFail";
	
	// Define its illegal FLow Value here
	public boolean hasIllegalFlow = true;
	
	/** Define the involved vars here. Involved vars are those that are present in the Exceptions.
	 *  Example for testmethod "AccessFieldsOfObjectsFail" : 
	 *  
	 *  "Assert: Is java.lang.String_$r6 contained in: utils.exceptions.IllegalFlowException: 
	 *   Passed argument java.lang.String_$r6 with a high security level to a method which doesn't allow it."
	 *   
	 *   Here, the involvedVars would be new String[] {"java.lang.String_$r6"}
	 */
	public String[] involvedVars = new String[] {"java.lang.String_$r6"};
    
	Logger logger = L1Logger.getLogger();
	


	@Test
	public void test() {
		System.out.println("\n\n\n");
		logger.info("Start of executing main.testclasses." + name + "");

		// TODO set Output directory
		ClassCompiler.compile(name);
		ClassRunner.testClass(name, hasIllegalFlow, involvedVars);

		logger.info("Finished executing main.testclasses." + name + "");
	}
}

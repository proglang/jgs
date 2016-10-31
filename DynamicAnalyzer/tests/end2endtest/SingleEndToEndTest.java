package end2endtest;

import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;

import org.junit.Test;
import utils.logging.L1Logger;

import java.util.logging.Logger;

/**
 * Class to run a single end to end test. We call this test "end-to-end"
 * because we compile and instrument a "real" java programm, execute it,
 * and see if it throws the desired exception.
 * 
 * Note that three pieces of information need to be
 * supplied: 1) name, the name of the test 2) hasIllegalFlow, a boolean indicating
 * if test should throw an IllegalFlowException 3) involvedVars, the variables
 * where information is leaked
 * 
 * @author NicolasM
 */
public class SingleEndToEndTest {

	public String name = "IfStmtSuccess";

	public boolean hasIllegalFlow = false;

	/**
	 * Define the involved vars here. Involved vars are those that are present
	 * in the Exceptions, eg those that leak information. Example for testmethod
	 * "AccessFieldsOfObjectsFail" :
	 * 
	 * "Assert: Is java.lang.String_$r6 contained in:
	 * utils.exceptions.IllegalFlowException: Passed argument
	 * java.lang.String_$r6 with a high security level to a method which doesn't
	 * allow it."
	 * 
	 * Here, the involvedVars would be new String[] {"java.lang.String_$r6"}
	 */
	public String[] involvedVars = new String[] {};

	Logger logger = L1Logger.getLogger();

	@Test
	public void test() {
		System.out.println("\n\n\n");
		logger.info("Start of executing main.testclasses." + name + "");

		// the output directory, where the compiled binary is put. 
		String outputDir = "junit";
		ClassCompiler.compile(name, outputDir);
		ClassRunner.testClass(name, outputDir, hasIllegalFlow, involvedVars);

		logger.info("Finished executing main.testclasses." + name + "");
	}
}

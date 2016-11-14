package end2endtest;

import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import utils.logging.L1Logger;

import java.util.Arrays;
import java.util.logging.Logger;

@RunWith(Parameterized.class)
public class AllEndToEndTests {

	private final String name;
	private final boolean hasIllegalFlow;
	private final String[] involvedVars;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Name of the class
	 * @param hasIllegalFlow
	 *            true if an exception is expected
	 * @param involvedVars
	 *            variables which are expected to be involved in the exception
	 */
	public AllEndToEndTests(String name, boolean hasIllegalFlow,
			String... involvedVars) {

		this.name = name;
		this.hasIllegalFlow = hasIllegalFlow;
		this.involvedVars = involvedVars;
	}

	Logger logger = L1Logger.getLogger();

	/**
	 * Create an Iterable for all testclasses. Arguments: String name, boolean
	 * hasIllegalFlow, String... involvedVars
	 * 
	 * @return Iterable
	 */
	@Parameters(name = "Name: {0}")
	public static Iterable<Object[]> generateParameters() {
		return Arrays.asList(
				new Object[] { "AccessFieldsOfObjectsFail", true, new String[] { "java.lang.String_$r6" } }, 	// set involved variable
				new Object[] { "AccessFieldsOfObjectsSuccess", false, new String[] {} },
				
				new Object[] { "ArithmeticExpressionsFail", true, new String[] { "int_i0" } }, 						
				new Object[] { "ArithmeticExpressionsSuccess", false, new String[] {} },
				
				new Object[] { "ArrayRefFail", true, new String[] {"java.lang.String_$r7"} }, 					// working now: rewrote non-expressive (?!) test
				new Object[] { "ArrayRefSuccess", false, new String[] {} }, 									// working now: rewritten (was one testfile ExtClasses)
				
				new Object[] { "BooleanPrintFail", true, new String[] {"boolean_z1"} }, 									// working now: rewritten (was one testfile ExtClasses)
				
				new Object[] { "ExtClassesFail", true, new String[] {} }, 										// fails: TODO: write external, manually instrumented class for this test
				new Object[] { "ExtClassesSuccess", false, new String[] {} }, 									// fails: same
				
				//new Object[] { "DominatorNullPointer", false, new String[] {} },								// NullPointerException!!!
				
				new Object[] { "FieldsSuccess", false, new String[] {} },
				new Object[] { "FieldWriteFail", true, new String[] {"main.testclasses.FieldWriteFail<main.testclasses.FieldWriteFail: int field>"} },								// failed because variable was missing
				
				new Object[] { "ForLoopSuccess", false, new String[] {} }, 										// fails with java.lang.VerifyError: (maybe invalid bytecode)
				new Object[] { "ForLoopFail", true, new String[] {} }, 											// fails, same reason
				
				new Object[] { "IfStmtSuccess", false, new String[] {"byte_b1"} }, 								// fails. SHOULD NOT FAIL?! 
				new Object[] { "IfStmtFail", true, new String[] { "int_i0" } }, 								// working now: split up from one test IfStmt 
				
				new Object[] { "InvokeInternalMethod", true, new String[] {} }, 								// fails because test is empty
				new Object[] { "InvokeLibMethod", true, new String[] { "int_i0" } },
				
				// Implicit flow from high-if guard to low-sec return
				new Object[] { "ImplicitFlow1", true, new String[] {"byte_b1"} },
				new Object[] { "ImplicitFlow2", true, new String[] {"high-security context"} },
				new Object[] { "ImplicitFlow3", true, new String[] {"high-security context"} },
			
				// Examples from readme.md
				new Object[] { "NSUPolicy1", true, new String[] {"int_i3"} },
				new Object[] { "NSUPolicy2", true, new String[] {"boolean_$z2"} },
				new Object[] { "NSUPolicy3", true, new String[] {"int_i3"} },
				new Object[] { "NSUPolicy4", true, new String[] {"int_i3"} },
				
				new Object[] { "MakeHigh", false, new String[] {} },
				
				new Object[] { "MulArray", false, new String[] {} },
				new Object[] { "MulArrayFail", true, new String[] {"java.lang.String_$r19"} }, 					// failed because there was no illegal flow, fixed now
				
				// Testing implicit and explicit flow with classes (which is unimplemented currently)
				new Object[] { "NewClassFail1", true, new String[] {"main.testclasses.C_r4"} },
				
				new Object[] { "NonStaticMethodsSuccess", false, new String[] {} },								// rename for consistency
				new Object[] { "NonStaticMethodsFail", true, new String[] {"int_i0"} }, 						// failed because no illegal flow was supplied
			
				// This is a collection of expressions that are not currently supported
				// new Object[] { "NotSupported", false, new String[] {} }, 
				
				new Object[] { "PrivateVariableSuccess", false, new String[] {} }, 
				
				// SystemOut1 and SystemOut2 are nearly the same! but behave differently!!
				new Object[] { "SystemOut1", true, new String[] {"int_i0"} },
				new Object[] { "SystemOut2", true, new String[] {"int_i0"} },							// does not fail even though it should!!
				
				// SimpleDebug is the test to try out stuff with - sort of a playground
				new Object[] { "SimpleDebug", true, new String[] { "java.lang.String_r3" } },
				
				new Object[] { "StaticMethodsSuccess", false, new String[] {} }, 
				new Object[] { "StaticMethodsFail", true, new String[] {} },									// fails: Unfinished test by Regina. Take closer look!
				
				// SwitchStmtFail{1, 2} are very similar, but behave differently
				new Object[] { "SwitchStmt", false, new String[] {} },
				new Object[] { "SwitchStmtFail1", true, new String[] { "int_i2" } }, 
				new Object[] { "SwitchStmtFail2", true, new String[] { "int_i2" } },
				
				new Object[] { "WhileLoop", false, new String[] {} }, 
				new Object[] { "WhileLoopFail", true, new String[] { "int_i1" } });
	}

	@Test
	/**
	 * Runs each testfile specified above. note that the outputDir can be set to ones liking.
	 */
	public void test() {
		System.out.println("\n\n\n");
		logger.info("Start of executing main.testclasses." + name + "");

		String outputDir = "junit";
		ClassCompiler.compile(name, outputDir);
		ClassRunner.testClass(name, outputDir, hasIllegalFlow, involvedVars);

		logger.info("Finished executing main.testclasses." + name + "");
	}
}

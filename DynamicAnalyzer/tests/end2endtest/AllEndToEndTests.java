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
				new Object[] { "AccessFieldsOfObjectsFail", true, new String[] { "java.lang.String_$r6" } }, 	
				new Object[] { "AccessFieldsOfObjectsSuccess", false, new String[] {} },
				
				new Object[] { "ArithmeticExpressionsFail", true, new String[] { "int_i0" } }, 						
				new Object[] { "ArithmeticExpressionsSuccess", false, new String[] {} },
				
				new Object[] { "ArrayRefFail", true, new String[] {"java.lang.String_$r7"} }, 					
				new Object[] { "ArrayRefSuccess", false, new String[] {} }, 									
				
				new Object[] { "BooleanPrintFail", true, new String[] {"boolean_z1"} }, 						

				new Object[] { "DirectPrintFail", true, new String[] {"java.lang.Object_$r3"} }, 						
				
				new Object[] { "ExternalFail1", true, new String[] {} }, 										// fails: TODO: write external, manually instrumented class for this test
				new Object[] { "ExternalFail2", true, new String[] {"int_i0"} },										// fails: TODO: write external, manually instrumented class for this test
				
				new Object[] { "EqualObjectsVerifySuccess", false, new String[] {} },							// throws java.lang.verify error
				
				new Object[] { "DominatorNullPointer", false, new String[] {} },								// NullPointerException!!!
				
				new Object[] { "FieldsSuccess", false, new String[] {} },
				new Object[] { "FieldWriteFail", true, new String[] {"main.testclasses.FieldWriteFail<main.testclasses.FieldWriteFail: int field>"} },					
				
				new Object[] { "IfStmtSuccess", false, new String[] {"byte_b1"} }, 								
				new Object[] { "IfStmtFail", true, new String[] { "int_i0" } }, 								
				
				// Implicit flow from high-if guard to low-sec return
				new Object[] { "ImplicitFlow1", true, new String[] {"byte_b1"} },
				new Object[] { "ImplicitFlow2", true, new String[] {"high-security context"} },
				new Object[] { "ImplicitFlow3", true, new String[] {"high-security context"} },

				new Object[] { "LowFieldHighInstance", true, new String[] {"boolean_z0"} },
			
				// Examples from readme.md
				new Object[] { "NSUPolicy", true, new String[] {"int_i0"} },
				new Object[] { "NSUPolicy2", true, new String[] {"boolean_$z2"} },
				new Object[] { "NSUPolicy3", true, new String[] {"<main.testclasses.utils.C: boolean f>"} },
				new Object[] { "NSUPolicy4", true, new String[] {"java.lang.String_r5"} },

				// More NSU Tests
				new Object[] { "NSU_FieldAccess", true, new String[] {"<main.testclasses.utils.C: boolean f>"} },
				new Object[] { "NSU_FieldAccessStatic", true, new String[] {"int f"} },
				new Object[] { "NSU_FieldAccess2", false, new String[] {} },
				new Object[] { "NSU_FieldAccess3", true, new String[] {"<main.testclasses.utils.C: boolean f>"} },
				new Object[] { "NSU_FieldAccess4", true, new String[] {"<main.testclasses.utils.C: boolean f>"} },
				new Object[] { "NSU_FieldAccess5", true, new String[] {"<main.testclasses.utils.C: boolean f>"} },
				
				new Object[] { "NSU_ForLoopSuccess", false, new String[] {} }, 										// fails with java.lang.VerifyError: (maybe invalid bytecode)
				new Object[] { "NSU_ForLoopFail", true, new String[] {"byte_b1"} }, 	
				
				new Object[] { "NSU_SwitchStmtFail", true, new String[] { "java.lang.String_r1" } }, 
				
				// two special cases, where compiler optimisation prevents NSU Exceptions
				new Object[] { "NSU_IfStmtSpecialCase", false, new String[] {  } }, 
				new Object[] { "NSU_SwitchStmtSpecialCase", false, new String[] {  } }, 

				new Object[] { "MakeHigh", false, new String[] {} },
				
				new Object[] { "MulArray", false, new String[] {} },
				new Object[] { "MulArrayFail", true, new String[] {"java.lang.String_$r19"} }, 					
				
				// Testing implicit and explicit flow with classes (which is unimplemented currently)
				new Object[] { "NewClassFail1", true, new String[] {"main.testclasses.utils.C_r4"} },
				
				new Object[] { "NonStaticMethodsSuccess", false, new String[] {} },								
				new Object[] { "NonStaticMethodsFail", true, new String[] {"int_i0"} }, 						
			
				// This is a collection of expressions that are not currently supported
				// new Object[] { "NotSupported", false, new String[] {} }, 
				
				new Object[] { "PrivateVariableSuccess", false, new String[] {} }, 
				
				// SystemOut1 and SystemOut2 are nearly the same! but behave differently!!
				new Object[] { "SystemOut1", true, new String[] {"int_i0"} },
				new Object[] { "SystemOut2", true, new String[] {"java.lang.Object_$r3"} },						
				
				// SimpleDebug is the test to try out stuff with - sort of a playground. Run only in SingleEndToEndTest
				// new Object[] { "SimpleDebug", true, new String[] { "java.lang.String_r3" } },
				
				new Object[] { "StaticMethodsFail", true, new String[] {} },									// fails: Unfinished test by Regina. Take closer look!
				
				// SwitchStmtFail{1, 2} are very similar, but behave differently
				new Object[] { "SwitchStmt", false, new String[] {} },
				
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

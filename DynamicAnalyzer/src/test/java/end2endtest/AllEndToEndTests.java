package end2endtest;

import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;

import utils.staticResults.superfluousInstrumentation.ExpectedException;
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
	private final ExpectedException expectedException;
	private final String[] involvedVars;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Name of the class
	 * @param expectedException
	 *            specifiy if, and what kind of exception is expected
	 * @param involvedVars
	 *            variables which are expected to be involved in the exception
	 */
	public AllEndToEndTests(String name, ExpectedException expectedException,
			String... involvedVars) {

		this.name = name;
		this.involvedVars = involvedVars;
		this.expectedException = expectedException;
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
				new Object[] { "AccessFieldsOfObjectsFail", ExpectedException.ILLEGAL_FLOW, new String[] {  } },
				new Object[] { "AccessFieldsOfObjectsSuccess", ExpectedException.NONE, new String[] {} },
				
				new Object[] { "ArithmeticExpressionsFail", ExpectedException.ILLEGAL_FLOW, new String[] {  } },
				new Object[] { "ArithmeticExpressionsSuccess", ExpectedException.NONE, new String[] {} },
				
				new Object[] { "ArrayRefFail", ExpectedException.ILLEGAL_FLOW, new String[] {"java.lang.String_$r7"} },
				new Object[] { "ArrayRefSuccess", ExpectedException.NONE, new String[] {} },
				
				new Object[] { "BooleanPrintFail", ExpectedException.ILLEGAL_FLOW, new String[] {"boolean_z1"} },

				// ============= Casts ===========
				new Object[] { "castStatic2Dyn_Success1", ExpectedException.NONE, new String[] {""} },
				new Object[] { "castStatic2Dyn_Fail1", ExpectedException.ILLEGAL_FLOW, new String[] {"java.lang.String_"} },

				new Object[] { "castDyn2Static_Fail1", ExpectedException.ILLEGAL_FLOW, new String[] {"java.lang.Integer_$r4"} },
				new Object[] { "castDyn2Static_Fail2", ExpectedException.ILLEGAL_FLOW, new String[] {} },
				new Object[] { "castDyn2Static_Success1", ExpectedException.NONE, new String[] {""} },
				new Object[] { "castDyn2Static_Success2", ExpectedException.NONE, new String[] {""} },

				new Object[] { "castDyn2DynSuccess", ExpectedException.NONE, new String[] {""} },

				// not usable bc unit test cannot handle exception thrown in instrumentation phase
				// new Object[] { "castStatic2Static_Fail", ExpectedException.ILLEGAL_FLOW, new String[] {"java.lang.String"} },

				new Object[] { "DirectPrintFail", ExpectedException.ILLEGAL_FLOW, new String[] {"java.lang.Object_$r3"} },

				// ExternalFail1 is fully tested in tests.end2endtest.compileToJarTests
				// new Object[] { "ExternalFail1", true, new String[] {} },
				new Object[] { "ExternalFail2", ExpectedException.ILLEGAL_FLOW, new String[] {"int_i0"} },
				
				new Object[] { "EqualObjectsVerifySuccess", ExpectedException.NONE, new String[] {} },
				
				new Object[] { "DominatorNullPointer", ExpectedException.NONE, new String[] {} },
				
				new Object[] { "FieldsSuccess", ExpectedException.NONE, new String[] {} },
				new Object[] { "FieldWriteFail", ExpectedException.NSU_FAILURE, new String[] {"testclasses.FieldWriteFail<testclasses.FieldWriteFail: int field>"} },

				new Object[] { "HighArgument", ExpectedException.ILLEGAL_FLOW, new String[] {"int_i0"} },

				new Object[] { "IfStmtSuccess", ExpectedException.NONE, new String[] {"byte_b1"} },
				new Object[] { "IfStmtFail", ExpectedException.ILLEGAL_FLOW, new String[] { "int_i0" } },
				
				// Implicit flow from high-if guard to low-sec return
				new Object[] { "ImplicitFlow1", ExpectedException.ILLEGAL_FLOW, new String[] {"byte_b1"} },
				new Object[] { "ImplicitFlow2", ExpectedException.ILLEGAL_FLOW, new String[] {"Invalid security context"} },
				new Object[] { "ImplicitFlow3", ExpectedException.ILLEGAL_FLOW, new String[] {"Invalid security context"} },

				new Object[] { "LowFieldHighInstance", ExpectedException.ILLEGAL_FLOW, new String[] {"boolean_z0"} },
				new Object[] { "PutfieldHighObject", ExpectedException.ILLEGAL_FLOW, new String[] {} },
				new Object[] { "PutfieldHighObjectNSU", ExpectedException.NSU_FAILURE, new String[] {} },
			
				// Examples from readme.md
				new Object[] { "NSUPolicy", ExpectedException.NSU_FAILURE, new String[] {"int_i0"} },
				new Object[] { "NSUPolicy2", ExpectedException.ILLEGAL_FLOW, new String[] {"boolean_$z2"} },
				new Object[] { "NSUPolicy3", ExpectedException.NSU_FAILURE, new String[] {"<testclasses.utils.C: boolean f>"} },
				new Object[] { "NSUPolicy4", ExpectedException.NSU_FAILURE, new String[] {"java.lang.String_r5"} },

				// More NSU Tests
				new Object[] { "NSU_FieldAccess", ExpectedException.NSU_FAILURE, new String[] {"<testclasses.utils.C: boolean f>"} },
				new Object[] { "NSU_FieldAccessStatic", ExpectedException.NSU_FAILURE, new String[] {"int f"} },
				new Object[] { "NSU_FieldAccess2", ExpectedException.NONE, new String[] {} },
				new Object[] { "NSU_FieldAccess3", ExpectedException.NSU_FAILURE, new String[] {"<testclasses.utils.C: boolean f>"} },
				new Object[] { "NSU_FieldAccess4", ExpectedException.NSU_FAILURE, new String[] {"<testclasses.utils.C: boolean f>"} },
				new Object[] { "NSU_FieldAccess5", ExpectedException.NSU_FAILURE, new String[] {"<testclasses.utils.C: boolean f>"} },
				
				new Object[] { "NSU_ForLoopSuccess", ExpectedException.NONE, new String[] {} },
				new Object[] { "NSU_ForLoopFail", ExpectedException.NSU_FAILURE, new String[] {"byte_b1"} },
				
				new Object[] { "NSU_SwitchStmtFail", ExpectedException.NSU_FAILURE, new String[] { "java.lang.String_r1" } },
				
				// two special cases, where compiler optimisation prevents NSU Exceptions
				new Object[] { "NSU_IfStmtSpecialCase", ExpectedException.NONE, new String[] {  } },
				new Object[] { "NSU_SwitchStmtSpecialCase", ExpectedException.NONE, new String[] {  } },

				new Object[] { "MakeHigh", ExpectedException.NONE, new String[] {} },
				
				new Object[] { "MulArray", ExpectedException.NONE, new String[] {} },
				new Object[] { "MulArrayFail", ExpectedException.ILLEGAL_FLOW, new String[] {"java.lang.String_$r"} },
				
				// Testing implicit and explicit flow with classes (which is unimplemented currently)
				new Object[] { "NewClassFail1", ExpectedException.ILLEGAL_FLOW, new String[] {} },
				
				new Object[] { "NonStaticMethodsSuccess", ExpectedException.NONE, new String[] {} },
				new Object[] { "NonStaticMethodsFail", ExpectedException.ILLEGAL_FLOW, new String[] {"int_i0"} },
			
				// This is a collection of expressions that are not currently supported
				// new Object[] { "NotSupported", false, new String[] {} }, 
				
				new Object[] { "PrivateVariableSuccess", ExpectedException.NONE, new String[] {} },
				
				// Acting with Mediums!
				new Object[] { "PrintMediumSuccess", ExpectedException.NONE, new String[] {} },
				new Object[] { "PrintMediumFail", ExpectedException.ILLEGAL_FLOW, new String[] {} },
				new Object[] { "PrintMediumFail2", ExpectedException.ILLEGAL_FLOW, new String[] {"java.lang.String_r3"} },
				
				// SystemOut1 and SystemOut2 are nearly the same! but behave differently!!
				new Object[] { "SystemOut1", ExpectedException.ILLEGAL_FLOW, new String[] {"int_i0"} },
				new Object[] { "SystemOut2", ExpectedException.ILLEGAL_FLOW, new String[] {"java.lang.Object_$r3"} },




				// SimpleDebug is the test to try out stuff with - sort of a playground. Run only in SingleEndToEndTest
				// new Object[] { "SimpleDebug", true, new String[] { "java.lang.String_r3" } },

                // StaticMethodsFail is fully tested in tests.end2endtest.compileToJarTests
				// new Object[] { "StaticMethodsFail", true, new String[] {} },
				
				// SwitchStmtFail{1, 2} are very similar, but behave differently
				new Object[] { "SwitchStmt", ExpectedException.NONE, new String[] {} },
				
				new Object[] { "WhileLoop", ExpectedException.NONE, new String[] {} },
				new Object[] { "WhileLoopFail", ExpectedException.NSU_FAILURE, new String[] { "int_i1" } });
	}

	@Test
	/**
	 * Runs each testfile specified above. note that the outputDir can be set to ones liking.
	 */
	public void test() {
		System.out.println("\n\n\n");
		logger.info("Start of executing testclasses." + name + "");

		String outputDir = "junit";
		ClassCompiler.compile(name, outputDir);
		ClassRunner.testClass(name, outputDir, "testclasses", expectedException, involvedVars);

		logger.info("Finished executing testclasses." + name + "");
	}
}

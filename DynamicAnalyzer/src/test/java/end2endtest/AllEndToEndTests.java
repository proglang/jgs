package end2endtest;

import analyzer.level2.SecurityMonitoringEvent;
import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;

import main.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import util.logging.L1Logger;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RunWith(Parameterized.class)
public class AllEndToEndTests {

	private final String name;
	private final SecurityMonitoringEvent securityMonitoringEvent;
	private final String[] involvedVars;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Name of the class
	 * @param securityMonitoringEvent
	 *            specify if, and what kind of exception is expected
	 * @param involvedVars
	 *            variables which are expected to be involved in the exception
	 */
	public AllEndToEndTests(String name, SecurityMonitoringEvent
            securityMonitoringEvent,
			String... involvedVars) {

		this.name = name;
		this.involvedVars = involvedVars;
		this.securityMonitoringEvent = securityMonitoringEvent;
	}

	private static final Logger logger = Logger.getLogger(AllEndToEndTests.class.getName());

	@Before
	public void setupLogger() {
		Main.setupLogger();
	}

	/**
	 * Create an Iterable for all testclasses. Arguments: String name, boolean
	 * hasIllegalFlow, String... involvedVars
	 * 
	 * @return Iterable
	 */
	@Parameters(name = "Name: {0}")
	public static Iterable<Object[]> generateParameters() {
	    List<Object[]> cases = Arrays.asList(
				new Object[] {"AccessFieldsOfObjectsFail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.String_$r6" } },
				new Object[] {"AccessFieldsOfObjectsSuccess", SecurityMonitoringEvent.PASSED, new String[] {} },
				
				new Object[] {"ArithmeticExpressionsFail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"int_i0" } },
				new Object[] {"ArithmeticExpressionsSuccess", SecurityMonitoringEvent.PASSED, new String[] {} },
				
				new Object[] {"ArrayRefFail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.String_$r7"} },
				new Object[] {"ArrayRefSuccess", SecurityMonitoringEvent.PASSED, new String[] {} },
				
				new Object[] {"BooleanPrintFail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"boolean_z1"} },

				// ============= Casts ===========
				new Object[] {"castStatic2Dyn_Success1", SecurityMonitoringEvent.PASSED, new String[] {""} },
				new Object[] {"castStatic2Dyn_Fail1", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.String_"} },

				// new Object[] {"castDyn2Static_Fail1", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.Integer_$r4"} },
				new Object[] {"castDyn2Static_Fail2", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.String_r1"} },
				new Object[] {"castDyn2Static_Success1", SecurityMonitoringEvent.PASSED, new String[] {""} },
				new Object[] {"castDyn2Static_Success2", SecurityMonitoringEvent.PASSED, new String[] {""} },

				new Object[] {"castDyn2DynSuccess", SecurityMonitoringEvent.PASSED, new String[] {""} },

				// not usable bc unit test cannot handle exception thrown in instrumentation phase
				// new Object[] { "castStatic2Static_Fail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.String"} },

				new Object[] {"DirectPrintFail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.Object_$r3"} },

				// ExternalFail1 is fully tested in tests.end2endtest.compileToJarTests
				new Object[] {"ExternalFail2", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"int_i0"} },
				
				new Object[] {"EqualObjectsVerifySuccess", SecurityMonitoringEvent.PASSED, new String[] {} },
				
				new Object[] {"DominatorNullPointer", SecurityMonitoringEvent.PASSED, new String[] {} },
				
				new Object[] {"FieldsSuccess", SecurityMonitoringEvent.PASSED, new String[] {} },
				new Object[] {"FieldWriteFail", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"testclasses.FieldWriteFail<testclasses.FieldWriteFail: int field>"} },

				new Object[] {"HighArgument", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"int_i0"} },

				new Object[] {"IfStmtSuccess", SecurityMonitoringEvent.PASSED, new String[] {"byte_b1"} },
				new Object[] {"IfStmtFail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"int_i0" } },
				
				// Implicit flow from high-if guard to low-sec return
				new Object[] {"ImplicitFlow1", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"byte_b1"} },
				new Object[] {"ImplicitFlow2", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"Invalid security context"} },
				new Object[] {"ImplicitFlow3", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"Invalid security context"} },

				new Object[] { "LowFieldHighInstance", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"boolean_z0"} },
				new Object[] { "PutfieldHighObject", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {} },
				new Object[] { "PutfieldHighObjectNSU", SecurityMonitoringEvent.NSU_FAILURE, new String[] {} },
			
				// Examples from readme.md
				new Object[] {"NSUPolicy", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"int_i0"} },
				new Object[] {"NSUPolicy2", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"boolean_$z2"} },
				new Object[] {"NSUPolicy3", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"<testclasses.util.C: boolean f>"} },
				new Object[] {"NSUPolicy4", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"java.lang.String_r5"} },

				// More NSU Tests
				new Object[] {"NSU_FieldAccess", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"<testclasses.util.C: boolean f>"} },
				new Object[] {"NSU_FieldAccessStatic", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"int f"} },
				new Object[] {"NSU_FieldAccess2", SecurityMonitoringEvent.PASSED, new String[] {} },
				new Object[] {"NSU_FieldAccess3", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"<testclasses.util.C: boolean f>"} },
				new Object[] {"NSU_FieldAccess4", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"<testclasses.util.C: boolean f>"} },
				new Object[] {"NSU_FieldAccess5", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"<testclasses.util.C: boolean f>"} },
				
				new Object[] {"NSU_ForLoopSuccess", SecurityMonitoringEvent.PASSED, new String[] {} },
				new Object[] {"NSU_ForLoopFail", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"byte_b1"} },
				
				new Object[] {"NSU_SwitchStmtFail", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"java.lang.String_r1" } },
				
				// two special cases, where compiler optimisation prevents NSU Exceptions
				new Object[] {"NSU_IfStmtSpecialCase", SecurityMonitoringEvent.PASSED, new String[] {  } },
				new Object[] {"NSU_SwitchStmtSpecialCase", SecurityMonitoringEvent.PASSED, new String[] {  } },
				new Object[] {"NoNSUForUninitializedVariables", SecurityMonitoringEvent.PASSED, new String[] {  } },

				new Object[] {"MakeHigh", SecurityMonitoringEvent.PASSED, new String[] {} },
				
				new Object[] {"MulArray", SecurityMonitoringEvent.PASSED, new String[] {} },
				new Object[] {"MulArrayFail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.String_$r"} },
				
				// Testing implicit and explicit flow with classes (which is unimplemented currently)
				new Object[] {"NewClassFail1", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"testclasses.util.C_r4"} },
				
				new Object[] {"NonStaticMethodsSuccess", SecurityMonitoringEvent.PASSED, new String[] {} },
				new Object[] {"NonStaticMethodsFail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"int_i0"} },
			
				// This is a collection of expressions that are not currently supported
				// new Object[] { "NotSupported", false, new String[] {} }, 
				
				new Object[] {"PrivateVariableSuccess", SecurityMonitoringEvent.PASSED, new String[] {} },
				
				// Acting with Mediums!
				new Object[] {"PrintMediumSuccess", SecurityMonitoringEvent.PASSED, new String[] {} },
				new Object[] {"PrintMediumFail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.String_r3"} },
				new Object[] {"PrintMediumFail2", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.String_r3"} },
				
				// SystemOut1 and SystemOut2 are nearly the same! but behave differently!!
				new Object[] {"SystemOut1", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"int_i0"} },
				new Object[] {"SystemOut2", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[] {"java.lang.Object_$r3"} },

				// SimpleDebug is the test to try out stuff with - sort of a playground. Run only in SingleEndToEndTest
				// new Object[] { "SimpleDebug", true, new String[] { "java.lang.String_r3" } },

                // StaticMethodsFail is fully tested in tests.end2endtest.compileToJarTests
				// new Object[] { "StaticMethodsFail", true, new String[] {} },
				
				// SwitchStmtFail{1, 2} are very similar, but behave differently
				new Object[] {"SwitchStmt", SecurityMonitoringEvent.PASSED, new String[] {} },
				
				new Object[] {"WhileLoop", SecurityMonitoringEvent.PASSED, new String[] {} },
				new Object[] {"WhileLoopFail", SecurityMonitoringEvent.NSU_FAILURE, new String[] {"int_i1" } });
	    return cases;
	}

	/**
	 * Runs each testfile specified above. note that the outputDir can be set to ones liking.
	 */
	@Test
	public void test() throws UnsupportedEncodingException {
		System.out.println("\n\n\n");
		logger.info("Start of executing testclasses." + name + "");

		String outputDir = "junit";
		ClassCompiler.compile(name, outputDir);
		ClassRunner.testClass(name, outputDir, "testclasses",
                              securityMonitoringEvent, involvedVars);

		logger.info("Finished executing testclasses." + name + "");
	}
}

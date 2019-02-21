package end2endtest;

import analyzer.level2.SecurityMonitoringEvent;
import analyzer.level2.storage.LowMediumHigh;
import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;
import de.unifreiburg.cs.proglang.jgs.instrumentation.MethodTypings;
import util.exceptions.IFCError;
import de.unifreiburg.cs.proglang.jgs.typing.FixedTypings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.Controller;
import util.exceptions.SuperfluousInstrumentation.LocalPcCalledException;
import util.logging.L1Logger;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Logger;


/**
 * Tests for the erasure implemented by "DynamicAnalyzer". We use fake type
 * analysis results here.
 */
@RunWith(Parameterized.class)
public class AllFakeAnalysisTests {

    private final String name;
    private final EnumSet<SecurityMonitoringEvent> expectedEvents;
    private final Controller isActive;
    private final MethodTypings<LowMediumHigh.Level> analysisResult;
    private final String[] involvedVars;

    private static final Logger logger = Logger.getLogger(AllFakeAnalysisTests.class.getName());

    /**
     * Create a test instance
     * @param name                      name of the test from testclasses to run
     * @param expectedEvent             the kind of exception we expect. See {@link SecurityMonitoringEvent}
     * @param analysisResult            the kind of static analysis result we want this test to run with. See {@link FakeMethodTypings}
     * @param isActive                  iff active, throw {@link LocalPcCalledException} on certain (superfluous) instrumentations
     * @param involvedVars              specify the variables involved. only used for {@link IFCError}
     */
    public AllFakeAnalysisTests(String name,
                                SecurityMonitoringEvent expectedEvent,
                                MethodTypings<LowMediumHigh.Level> analysisResult,
                                Controller isActive,
                                String... involvedVars) {

        this.name = name;
        // TODO: find a better solution than "null"
        if (expectedEvent == null) {
            this.expectedEvents = EnumSet.noneOf(SecurityMonitoringEvent.class);
        } else {
            this.expectedEvents = EnumSet.of(expectedEvent);
        }
        this.analysisResult = analysisResult;
        this.involvedVars = involvedVars;
        this.isActive = isActive;
    }

    /**
     * Testing a set of testclasses in different configurations. Refer to constructor of {@link AllFakeAnalysisTests} for details.
     */
    @Parameterized.Parameters(name = "Name: {0}, {1}, {2}, {3}")
    public static Iterable<Object[]> testErasure() {
        return Arrays.asList(


                // =========================================================================
                // custom typing. See util.staticResults.CustomTyping
                // =========================================================================

                // to make sure the Custom Typing works, FakeMethodTypings.CUSTOM_LowPlusPublic_AllDynamic makes everything (important) dynamic.
                // Must cause superfluous join_level_of_local exception, since int res = int a + int j obviously performs a join_level_of_local_and_assignment_level stmt.
                new Object[] {"LowPlusPublic", SecurityMonitoringEvent.JOIN_LEVEL_OF_LOCAL_AND_ASSIGNMENT_LEVEL, FakeMethodTypings.CUSTOM_LowPlusPublic_AllDynamic, Controller.ACTIVE, new String[] {}},
                // the following test tests if we are able to join a DYNAMIC(LOW) with a PUBLIC local variable.
                new Object[] {"LowPlusPublic", null, FakeMethodTypings.CUSTOM_LowPlusPublic, Controller.PASSIVE, new String[] {}},

                // =========================================================================
                // joinLevelOfLocalAndAssignmentLevelException
                // =========================================================================
                new Object[] {"SimpleIfStmt", SecurityMonitoringEvent.JOIN_LEVEL_OF_LOCAL_AND_ASSIGNMENT_LEVEL, FixedTypings.allDynamic(), Controller.ACTIVE, new String[] {}},
                new Object[] {"SimpleIfStmt", null, FixedTypings.allPublic(), Controller.ACTIVE, new String[] {}},

                // =========================================================================
                // Testing HandleStmt.assignArgumentToLocal;
                // Testclass HighArguments has a method callPrint(int i) which is called with HIGH int.
                // it does cause an Illegal Flow always. Assign_ARG_TO_LOCAL only if analysis
                // is dynamic and controller is active
                // =========================================================================
                new Object[] {"HighArgument", SecurityMonitoringEvent.ILLEGAL_FLOW, FixedTypings.allDynamic(), Controller.ACTIVE, new String[] {}},
                new Object[] {"HighArgument", SecurityMonitoringEvent.ASSIGN_ARG_TO_LOCAL, FixedTypings.allDynamic(), Controller.ACTIVE, new String[] {}},
                new Object[] {"HighArgument", null, FixedTypings.allPublic(), Controller.ACTIVE, new String[] {}},

                // =========================================================================
                //    The following are compinations of testclasses, expected exceptions
                //    and static analysis results that are inconsistent, eg. that would
                //    normally not appear like this
                // =========================================================================

                // should throw a IllegalFlow Exception regardless of the CX
                new Object[] {"AccessFieldsOfObjectsFail", SecurityMonitoringEvent.ILLEGAL_FLOW, FixedTypings.allDynamic(), Controller.PASSIVE, new String[] {"java.lang.String_$r6" } },
                new Object[] {"AccessFieldsOfObjectsFail", SecurityMonitoringEvent.ILLEGAL_FLOW, FakeMethodTypings.CX_PUBLIC, Controller.PASSIVE, new String[] {"java.lang.String_$r6" } },

                // NSU on local Variables.
                new Object[] {"NSUPolicy", SecurityMonitoringEvent.NSU_FAILURE, FixedTypings.allDynamic(), Controller.PASSIVE, new String[] {"int_i0"} },
                new Object[] {"NSUPolicy", null, FakeMethodTypings.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                // NSU on fields.
                new Object[] {"NSUPolicy3", SecurityMonitoringEvent.NSU_FAILURE, FixedTypings.allDynamic(), Controller.PASSIVE, new String[] {"<testclasses.util.C: boolean f>"} },
                new Object[] {"NSUPolicy3", null, FakeMethodTypings.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                // NSU on static fields
                new Object[] {"NSU_FieldAccessStatic", SecurityMonitoringEvent.NSU_FAILURE, FixedTypings.allDynamic(), Controller.PASSIVE, new String[] {"int f"} },
                new Object[] {"NSU_FieldAccessStatic", null, FakeMethodTypings.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                // NSU on static fields
                new Object[] {"NSU_FieldAccess", SecurityMonitoringEvent.NSU_FAILURE, FixedTypings.allDynamic(), Controller.PASSIVE, new String[] {"<testclasses.util.C: boolean f>"} },
                new Object[] {"NSU_FieldAccess", null, FakeMethodTypings.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                new Object[] {"NSU_FieldAccess2", null, FixedTypings.allDynamic(), Controller.PASSIVE , new String[] {} },    // does not throw an IllFlow Except
                new Object[] {"NSU_FieldAccess2", null, FakeMethodTypings.CX_PUBLIC, Controller.PASSIVE, new String[] {} },
                new Object[] {"NSU_FieldAccess2", null, FakeMethodTypings.VAR_AND_CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                new Object[] {"NSU_FieldAccess3", SecurityMonitoringEvent.NSU_FAILURE, FixedTypings.allDynamic(), Controller.PASSIVE, new String[] {"<testclasses.util.C: boolean f>"} },
                new Object[] {"NSU_FieldAccess3", null, FakeMethodTypings.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                new Object[] {"NSU_FieldAccess4", SecurityMonitoringEvent.NSU_FAILURE, FixedTypings.allDynamic(), Controller.PASSIVE, new String[] {"<testclasses.util.C: boolean f>"} },
                new Object[] {"NSU_FieldAccess4", null, FakeMethodTypings.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                new Object[] {"NSU_FieldAccess5", SecurityMonitoringEvent.NSU_FAILURE, FixedTypings.allDynamic(), Controller.PASSIVE, new String[] {"<testclasses.util.C: boolean f>"} },
                new Object[] {"NSU_FieldAccess5", null, FakeMethodTypings.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                // =========================================================================
                //    From here on, (hopefully) test only combinations of testclasses,
                //    expected exceptions and static analysis results that are consistent
                // =========================================================================

                // testing that the controller works: Throwing a CHECK_LOCAL_PC_CALLED exception on purpose
                new Object[] {"NSUPolicy", SecurityMonitoringEvent.CHECK_LOCAL_PC_CALLED, FixedTypings.allDynamic(), Controller.ACTIVE, new String[] {} },
                new Object[] {"NSUPolicy", SecurityMonitoringEvent.NSU_FAILURE, FixedTypings.allDynamic(), Controller.ACTIVE, new String[] {} },
                new Object[] {"NSU_FieldAccess4", SecurityMonitoringEvent.CHECK_LOCAL_PC_CALLED, FixedTypings.allDynamic(), Controller.ACTIVE, new String[] {} },

                // testing objects that may have invalid flows, but surely do not have NSU checks. Must never throw CHECK_LOCAL_PC_CALLED exception
                new Object[] {"NoNSU1", SecurityMonitoringEvent.ILLEGAL_FLOW, FixedTypings.allDynamic(), Controller.PASSIVE, new String[] {"int_i2"}},
                new Object[] {"NoNSU1", SecurityMonitoringEvent.CHECK_LOCAL_PC_CALLED, FixedTypings.allDynamic(), Controller.ACTIVE, new String[] {}},

                // NoNSU2 has no information leak, but does invoke NSUchecks if CX is dynamic. If it's public, we expect no NSU check
                new Object[] {"NoNSU2", SecurityMonitoringEvent.CHECK_LOCAL_PC_CALLED, FixedTypings.allDynamic(), Controller.ACTIVE, new String[] {}},
                new Object[] {"NoNSU2", null, FixedTypings.allPublic(), Controller.ACTIVE, new String[] {}},

                new Object[] {"NoNSU3_Fields", SecurityMonitoringEvent.CHECK_LOCAL_PC_CALLED, FixedTypings.allDynamic(), Controller.ACTIVE, new String[] {}},
                new Object[] {"NoNSU3_Fields", null, FixedTypings.allPublic(), Controller.ACTIVE, new String[] {}},

                new Object[] {"NoNSU4_staticField", null, FixedTypings.allPublic(), Controller.ACTIVE, new String[] {}}

        );
    }

    /**
     * Runs each testfile specified above. note that the outputDir can be set to ones liking.
     */
    @Test
    public void test() throws UnsupportedEncodingException {
        System.out.println("\n\n\n");
        logger.info("Start executing testclasses with fake analysis results." + name + "");
        String outputDir = "junit_fakeAnalysis";

        MethodTypings<LowMediumHigh.Level> methodTypings = analysisResult;

        // TODO: replace "expExc" with proper required and forbidden sets in the constructor of this class
        EnumSet<SecurityMonitoringEvent> requiredEvents = EnumSet.noneOf(SecurityMonitoringEvent.class);
        EnumSet<SecurityMonitoringEvent> forbiddenEvents = EnumSet.noneOf(SecurityMonitoringEvent.class);
        if (isActive.equals(Controller.PASSIVE)) {
            requiredEvents = expectedEvents;
        } else {
            forbiddenEvents = expectedEvents;
        }

        /*
        SecurityMonitorLog
                monitorLog = EventChecker.of(requiredEvents, forbiddenEvents);
                */
        ClassCompiler.compileWithFakeTyping(name, outputDir, methodTypings);
        ClassRunner.testClass(name, outputDir, "testclasses",
                              // TODO: this is only temporary.. it won't work
                              SecurityMonitoringEvent.PASSED,
                              involvedVars);

        logger.info("Finished executing testclasses with fake analysis results." + name + "");
    }

    }
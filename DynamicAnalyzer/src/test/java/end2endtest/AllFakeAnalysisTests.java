package end2endtest;

import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;
import utils.staticResults.superfluousInstrumentation.ExpectedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.Controller;
import utils.exceptions.InternalAnalyzerException;
import utils.exceptions.NSUCheckCalledException;
import utils.logging.L1Logger;
import utils.staticResults.*;
import utils.staticResults.implementation.Types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Desribe how we want the fake analysis results the be
 */
enum StaticAnalysis {
    ALL_DYNAMIC,         // Var, Cx & Instantiation all return Dynamic on any request
    CX_PUBLIC,           // same as ALL_DYNAMIC, except for Cx, which returns public on any request
    ALL_PUBLIC, VAR_AND_CX_PUBLIC      // same as CX_PUBLIC, except for Var, which returns public on any request
}



/**
 * Tests to test the impact of different static analysis results.
 */
@RunWith(Parameterized.class)
public class AllFakeAnalysisTests {

    private final String name;
    private final ExpectedException expEx;
    private final Controller isActive;
    private final StaticAnalysis analysisResult;
    private final String[] involvedVars;
    private Logger logger = L1Logger.getLogger();

    /**
     * Create a test instance
     * @param name                      name of the test from testclasses to run
     * @param expEx                     the kind of exception we expect. See {@link ExpectedException}
     * @param analysisResult            the kind of static analysis result we want this test to run with. See {@link StaticAnalysis}
     * @param isActive                  iff active, throw {@link NSUCheckCalledException} on certain (superfluous) instrumentations
     * @param involvedVars              specify the variables involved. only used for {@link utils.exceptions.IllegalFlowException}
     */
    public AllFakeAnalysisTests(String name,
                                ExpectedException expEx,
                                StaticAnalysis analysisResult,
                                Controller isActive,
                                String... involvedVars) {

        this.name = name;
        this.expEx = expEx;
        this.analysisResult = analysisResult;
        this.involvedVars = involvedVars;
        this.isActive = isActive;
    }

    /**
     * Testing a set of testclasses in different configurations. Refer to constructor of {@link AllFakeAnalysisTests} for details.
     */
    @Parameterized.Parameters(name = "Name: {0}, {1}, {2}, {3}")
    public static Iterable<Object[]> testForSuperfluousInstrumentation() {
        return Arrays.asList(

                // =========================================================================
                //    The following are compinations of testclasses, expected exceptions
                //    and static analysis results that are inconsistent, eg. that would
                //    normally not appear like this
                // =========================================================================

                // should throw a IllegalFlow Exception regardless of the CX
                new Object[] { "AccessFieldsOfObjectsFail", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC, Controller.PASSIVE, new String[] { "java.lang.String_$r6" } },
                new Object[] { "AccessFieldsOfObjectsFail", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.CX_PUBLIC, Controller.PASSIVE, new String[] { "java.lang.String_$r6" } },

                // NSU on local Variables.
                new Object[] { "NSUPolicy", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC, Controller.PASSIVE, new String[] {"int_i0"} },
                new Object[] { "NSUPolicy", ExpectedException.NONE, StaticAnalysis.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                // NSU on fields.
                new Object[] { "NSUPolicy3", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC, Controller.PASSIVE, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSUPolicy3",ExpectedException.NONE, StaticAnalysis.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                // NSU on static fields
                new Object[] { "NSU_FieldAccessStatic", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC, Controller.PASSIVE, new String[] {"int f"} },
                new Object[] { "NSU_FieldAccessStatic", ExpectedException.NONE, StaticAnalysis.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                // NSU on static fields
                new Object[] { "NSU_FieldAccess", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC,  Controller.PASSIVE, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess", ExpectedException.NONE,  StaticAnalysis.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                new Object[] { "NSU_FieldAccess2", ExpectedException.NONE, StaticAnalysis.ALL_DYNAMIC, Controller.PASSIVE , new String[] {} },    // does not throw an IllFlow Except
                new Object[] { "NSU_FieldAccess2", ExpectedException.NONE, StaticAnalysis.CX_PUBLIC, Controller.PASSIVE, new String[] {} },
                new Object[] { "NSU_FieldAccess2", ExpectedException.NONE, StaticAnalysis.VAR_AND_CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                new Object[] { "NSU_FieldAccess3", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC,  Controller.PASSIVE, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess3", ExpectedException.NONE, StaticAnalysis.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                new Object[] { "NSU_FieldAccess4", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC,  Controller.PASSIVE, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess4", ExpectedException.NONE, StaticAnalysis.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                new Object[] { "NSU_FieldAccess5", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC,  Controller.PASSIVE, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess5", ExpectedException.NONE, StaticAnalysis.CX_PUBLIC, Controller.PASSIVE, new String[] {} },

                // =========================================================================
                //    From here on, (hopefully) test only combinations of testclasses,
                //    expected exceptions and static analysis results that are consistent
                // =========================================================================

                // testing that the controller works: Throwing a CHECK_LOCAL_PC_CALLED exception on purpose
                new Object[] { "NSUPolicy", ExpectedException.CHECK_LOCAL_PC_CALLED, StaticAnalysis.ALL_DYNAMIC, Controller.ACTIVE, new String[] {} },
                new Object[] { "NSUPolicy", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC, Controller.ACTIVE, new String[] {} },
                new Object[] { "NSU_FieldAccess4", ExpectedException.CHECK_LOCAL_PC_CALLED, StaticAnalysis.ALL_DYNAMIC,  Controller.ACTIVE, new String[] {} },

                // testing objects that may have invalid flows, but surely do not have NSU checks. Must never throw CHECK_LOCAL_PC_CALLED exception
                new Object[] {"NoNSU1", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.ALL_DYNAMIC, Controller.PASSIVE, new String[] {"int_i2"}},
                new Object[] {"NoNSU1", ExpectedException.CHECK_LOCAL_PC_CALLED, StaticAnalysis.ALL_DYNAMIC, Controller.ACTIVE, new String[] {}},

                // NoNSU2 has no information leak, but does invoke NSUchecks if CX is dynamic. If it's public, we expect no NSU check
                new Object[] {"NoNSU2", ExpectedException.CHECK_LOCAL_PC_CALLED, StaticAnalysis.ALL_DYNAMIC, Controller.ACTIVE, new String[] {}},
                new Object[] {"NoNSU2", ExpectedException.NONE, StaticAnalysis.ALL_PUBLIC, Controller.ACTIVE, new String[] {}},

                new Object[] {"NoNSU3_Fields", ExpectedException.CHECK_LOCAL_PC_CALLED, StaticAnalysis.ALL_DYNAMIC, Controller.ACTIVE, new String[] {}},
                new Object[] {"NoNSU3_Fields", ExpectedException.NONE, StaticAnalysis.ALL_PUBLIC, Controller.ACTIVE, new String[] {}},

                new Object[] {"NoNSU4_staticField", ExpectedException.NONE, StaticAnalysis.ALL_PUBLIC, Controller.ACTIVE, new String[] {}}
        );
    }

    /**
     * Runs each testfile specified above. note that the outputDir can be set to ones liking.
     */
    @Test
    public void test() {
        System.out.println("\n\n\n");
        logger.info("Start of executing testclasses with fake analysis results." + name + "");
        String outputDir = "junit_fakeAnalysis";

        // here, we need to create the appropriate fake Maps to hand over the the ClassCompiler
        MSLMap<BeforeAfterContainer> fakeVarTypingsMap = new MSLMap<>();
        MSMap<Types> fakeCxTypingsMap = new MSMap<>();
        MIMap<Types> fakeInstantiationMap = new MIMap<>();

        Collection<String> allClasses = Collections.singletonList("testclasses." + name);
        switch (analysisResult) {
            case ALL_DYNAMIC:
                ResultsServer.setDynamic(fakeVarTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case CX_PUBLIC:
                ResultsServer.setDynamic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case VAR_AND_CX_PUBLIC:
                ResultsServer.setPublic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case ALL_PUBLIC:
                ResultsServer.setPublic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setPublic(fakeInstantiationMap, allClasses);
                break;
            default:
                throw new InternalAnalyzerException("Invalid analysis result requested!");
        }



        ClassCompiler.compileWithFakeTyping(name, outputDir, fakeVarTypingsMap, fakeCxTypingsMap, fakeInstantiationMap, isActive, expEx.getVal());
        ClassRunner.testClass(name, outputDir, expEx, involvedVars);

        logger.info("Finished executing testclasses with fake analysis results." + name + "");
    }

    }
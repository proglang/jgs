package end2endtest;

import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;
import classfiletests.utils.ExpectedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L1Logger;
import utils.staticResults.BeforeAfterContainer;
import utils.staticResults.MSLMap;
import utils.staticResults.MSMap;
import utils.staticResults.ResultsServer;
import utils.staticResults.implementation.Types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Desribe how we want the fake analysis results the be
 */
enum StaticAnalysis {
    allDynamic,         // Var, Cx & Instantiation all return Dynamic on any request
    CxPublic,           // same as allDynamic, except for Cx, which returns public on any request
    VarAndCxPublic      // same as CxPublic, except for Var, which returns public on any request
}

/**
 * Tests to test the impact of different static analysis results.
 */
@RunWith(Parameterized.class)
public class AllFakeAnalysisTests {

    private final String name;
    private final ExpectedException expEx;
    private final boolean controllerIsActive;
    private final StaticAnalysis analysisResult;
    private final String[] involvedVars;
    private Logger logger = L1Logger.getLogger();

    public AllFakeAnalysisTests(String name,
                                ExpectedException expEx,
                                StaticAnalysis analysisResult,
                                boolean controllerIsActive,
                                String... involvedVars) {

        this.name = name;
        this.expEx = expEx;
        this.analysisResult = analysisResult;
        this.involvedVars = involvedVars;
        this.controllerIsActive = controllerIsActive;
    }

    /**
     * Testing the same testcases with different static analysis results. For example,
     * we expect an NSU/IllegalFLow Exception when running NSUPolicy in dynamic CX, but if the CX
     * is pulic, we do not expect an NSU / IllegalFlow Exception.
     */
    @Parameterized.Parameters(name = "Name: {0}, {2}")
    public static Iterable<Object[]> generateParameters() {
        return Arrays.asList(
                // should throw a IllegalFlow Exception regardless of the CX
                new Object[] { "AccessFieldsOfObjectsFail", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.allDynamic, false, new String[] { "java.lang.String_$r6" } },
                new Object[] { "AccessFieldsOfObjectsFail", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.CxPublic, false, new String[] { "java.lang.String_$r6" } },

                // NSU on local Variables.
                new Object[] { "NSUPolicy", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.allDynamic, false, new String[] {"int_i0"} },
                new Object[] { "NSUPolicy", ExpectedException.NONE, StaticAnalysis.CxPublic, false, new String[] {} },

                // NSU on fields.
                new Object[] { "NSUPolicy3", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.allDynamic, false, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSUPolicy3",ExpectedException.NONE, StaticAnalysis.CxPublic, false, new String[] {} },

                // NSU on static fields
                new Object[] { "NSU_FieldAccessStatic", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.allDynamic, false, new String[] {"int f"} },
                new Object[] { "NSU_FieldAccessStatic", ExpectedException.NONE, StaticAnalysis.CxPublic, false, new String[] {} },

                // NSU on static fields
                new Object[] { "NSU_FieldAccess", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.allDynamic,  false, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess", ExpectedException.NONE, false, StaticAnalysis.CxPublic, false, new String[] {} },

                new Object[] { "NSU_FieldAccess2", ExpectedException.NONE, StaticAnalysis.allDynamic,  false, new String[] {} },    // does not throw an IllFlow Except
                new Object[] { "NSU_FieldAccess2", ExpectedException.NONE, StaticAnalysis.CxPublic, false, new String[] {} },
                new Object[] { "NSU_FieldAccess2", ExpectedException.NONE, StaticAnalysis.VarAndCxPublic, false, new String[] {} },

                new Object[] { "NSU_FieldAccess3", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.allDynamic,  false, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess3", ExpectedException.NONE, StaticAnalysis.CxPublic, false, new String[] {} },

                new Object[] { "NSU_FieldAccess4", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.allDynamic,  false, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess4", ExpectedException.NONE, StaticAnalysis.CxPublic, false, new String[] {} },

                new Object[] { "NSU_FieldAccess5", ExpectedException.ILLEGAL_FLOW, StaticAnalysis.allDynamic,  false, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess5", ExpectedException.NONE, StaticAnalysis.CxPublic, false, new String[] {} }

                // testing that the controller works!

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
        MSMap<Types> fakeInstantiationMap = new MSMap<>();

        Collection<String> allClasses = Collections.singletonList("testclasses." + name);
        switch (analysisResult) {
            case allDynamic:
                ResultsServer.setDynamic(fakeVarTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case CxPublic:
                ResultsServer.setDynamic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case VarAndCxPublic:
                ResultsServer.setPublic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            default:
                throw new InternalAnalyzerException("Invalid analysis result requested!");
        }



        ClassCompiler.compileWithFakeTyping(name, outputDir, fakeVarTypingsMap, fakeCxTypingsMap, fakeInstantiationMap, controllerIsActive);
        ClassRunner.testClass(name, outputDir, expEx, involvedVars);

        logger.info("Finished executing testclasses with fake analysis results." + name + "");
    }

    }
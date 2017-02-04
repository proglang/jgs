package end2endtest;

import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import soot.SootMethod;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L1Logger;
import utils.staticResults.ResultsServer;

import java.util.*;
import java.util.logging.Logger;

/**
 * Tests to test the impact of different static analysis results.
 */
@RunWith(Parameterized.class)
public class AllFakeAnalysisTests {

    private final String name;
    private final boolean hasIllegalFlow;
    private final StaticAnalysis analysisResult;
    private final String[] involvedVars;

    public AllFakeAnalysisTests(String name, boolean hasIllegalFlow,
                                StaticAnalysis analysisResult, String... involvedVars) {

        this.name = name;
        this.hasIllegalFlow = hasIllegalFlow;
        this.analysisResult = analysisResult;
        this.involvedVars = involvedVars;
    }

    Logger logger = L1Logger.getLogger();

    /**
     * Testing the same testcases with different static analysis results. For example,
     * we expect an NSU/IllegalFLow Exception when running NSUPolicy in dynamic CX, but if the CX
     * is pulic, we do not expect an NSU / IllegalFlow Exception.
     * @return
     */
    @Parameterized.Parameters(name = "Name: {0}, {2}")
    public static Iterable<Object[]> generateParameters() {
        return Arrays.asList(
                // should throw a IllegalFlow Exception regardless of the CX
                new Object[] { "AccessFieldsOfObjectsFail", true, StaticAnalysis.allDynamic, new String[] { "java.lang.String_$r6" } },
                new Object[] { "AccessFieldsOfObjectsFail", true, StaticAnalysis.CxPublic, new String[] { "java.lang.String_$r6" } },

                // should only throw an IllegalFLow Exception if NSU. NSU should only be thrown if CX is Dynamic
                new Object[] { "NSUPolicy", true, StaticAnalysis.allDynamic, new String[] {"int_i0"} },
                new Object[] { "NSUPolicy", false, StaticAnalysis.CxPublic, new String[] {} },

                new Object[] { "NSUPolicy2", true, StaticAnalysis.allDynamic, new String[] {"boolean_$z2"} },
                new Object[] { "NSUPolicy2", false, StaticAnalysis.CxPublic, new String[] {} },

                new Object[] { "NSUPolicy3", true, StaticAnalysis.allDynamic, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSUPolicy3", false, StaticAnalysis.CxPublic, new String[] {} },

                new Object[] { "NSUPolicy4", true, StaticAnalysis.allDynamic, new String[] {"java.lang.String_r5"} },
                new Object[] { "NSUPolicy4", false, StaticAnalysis.CxPublic, new String[] {} },

                // Testing StaticField NSU: JimpleInjector.SetLevelOfAssignmentStmt ( StaticFieldRed, ..)
                new Object[] { "NSU_FieldAccessStatic", true, StaticAnalysis.allDynamic, new String[] {"int f"} },
                new Object[] { "NSU_FieldAccessStatic", false, StaticAnalysis.CxPublic, new String[] {} }
       );
    }

    @Test
    /**
     * Runs each testfile specified above. note that the outputDir can be set to ones liking.
     */
    public void test() {
        System.out.println("\n\n\n");
        logger.info("Start of executing testclasses with fake analysis results." + name + "");
        String outputDir = "junit_fakeAnalysis";

        // here, we need to create the appropriate fake Maps to hand over the the ClassCompiler
        Map<SootMethod, VarTyping> fakeVarTypingsMap = new HashMap<>();
        Map<SootMethod, CxTyping> fakeCxTypingsMap = new HashMap<>();
        Map<SootMethod, Instantiation> fakeInstantiationMap = new HashMap<>();

        Collection<String> allClasses = Arrays.asList("testclasses." + name);
        switch (analysisResult) {
            case allDynamic:
                ResultsServer.setDynamicVar(fakeVarTypingsMap, allClasses);
                ResultsServer.setDynamicCx(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamicInst(fakeInstantiationMap, allClasses);
                break;
            case CxPublic:
                ResultsServer.setDynamicVar(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublicCx(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamicInst(fakeInstantiationMap, allClasses);
                break;
            default:
                throw new InternalAnalyzerException("Invalid analysis result requested!");
        }


        ClassCompiler.compileWithFakeTyping(name, outputDir, fakeVarTypingsMap, fakeCxTypingsMap, fakeInstantiationMap);
        ClassRunner.testClass(name, outputDir, hasIllegalFlow, involvedVars);

        logger.info("Finished executing testclasses with fake analysis results." + name + "");
    }

    }

/**
 * Desribe how we want the fake analysis results the be
 */
enum StaticAnalysis {
    allDynamic, CxPublic
    }
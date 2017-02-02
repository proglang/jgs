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
import utils.logging.L1Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Nicolas Müller on 30.01.17.
 */
@RunWith(Parameterized.class)
public class AllFakeAnalysisTests {

    private final String name;
    private final boolean hasIllegalFlow;
    private final String[] involvedVars;
    Map<SootMethod, VarTyping> varTypingMap;
    Map<SootMethod, CxTyping> cxTypingMap;
    Map<SootMethod, Instantiation> instantiationMap;

    public AllFakeAnalysisTests(String name, boolean hasIllegalFlow,
                                Map<SootMethod, VarTyping> varTyping,
                                Map<SootMethod, CxTyping> cxTyping,
                                Map<SootMethod, Instantiation> instantiation,
                                String... involvedVars) {

        this.name = name;
        this.hasIllegalFlow = hasIllegalFlow;
        this.involvedVars = involvedVars;
        this.varTypingMap = varTyping;
        this.cxTypingMap = cxTyping;
        this.instantiationMap = instantiation;
    }

    Logger logger = L1Logger.getLogger();

    @Parameterized.Parameters(name = "Name: {0}")
    public static Iterable<Object[]> generateParameters() {
        return Arrays.asList(
                new Object[] { "AccessFieldsOfObjectsFail", true, new String[] { "java.lang.String_$r6" } },
                new Object[] { "AccessFieldsOfObjectsFail", true, new String[] { "java.lang.String_$r6" } });
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



        ClassCompiler.compileWithFakeTyping(name, outputDir, varTypingMap, cxTypingMap, instantiationMap);
        ClassRunner.testClass(name, outputDir, hasIllegalFlow, involvedVars);

        logger.info("Finished executing testclasses with fake analysis results." + name + "");
    }

    }

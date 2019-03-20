package de.unifreiburg.cs.proglang.jgs;

import analyzer.level2.SecurityMonitoringEvent;
import classfiletests.utils.ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Logger;

@RunWith(Parameterized.class)
public class JGS_End2EndTests {

    private final String name;
    private final SecurityMonitoringEvent securityMonitoringEvents;
    private final String[] involvedVars;

    /**
     * Constructor.
     *
     * @param name
     *            Name of the class
     * @param securityMonitoringEvents
     *            specifiy if, and what kind of exception is expected
     * @param involvedVars
     *            variables which are expected to be involved in the exception
     */
    public JGS_End2EndTests(String name, SecurityMonitoringEvent
            securityMonitoringEvents,
                            String... involvedVars) {

        this.name = name;
        this.involvedVars = involvedVars;
        this.securityMonitoringEvents = securityMonitoringEvents;
    }

    Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    /**
     * Create an Iterable for all testclasses. Arguments: String name, boolean
     * hasIllegalFlow, String... involvedVars
     *
     * @return Iterable
     */
    @Parameters(name = "Name: {0}")
    public static Iterable<Object[]> generateParameters() {
        return Arrays.asList(
                new Object[]{"SimpleCast_Fail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[]{"java.lang.Integer_$r5"}},
                new Object[]{"SimpleCast_Fail2", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[]{"java.lang.Integer_$r5"}},
                new Object[]{"SimpleCast_Fail3", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[]{"java.lang.Integer_$r5"}},
                new Object[]{"SimpleCast_Fail4", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[]{"java.lang.String_r2"}},
                new Object[]{"NSUPolicy", SecurityMonitoringEvent.NSU_FAILURE, new String[]{""}},
                new Object[]{"NSUPolicyWithMakeHigh", SecurityMonitoringEvent.NSU_FAILURE, new String[]{""}},
                new Object[]{"ScratchMonomorphic_Success", SecurityMonitoringEvent.PASSED, new String[]{""}},
                new Object[]{"SimpleSuccess", SecurityMonitoringEvent.PASSED, new String[]{""}},
                new Object[]{"SimpleCasts", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[]{""}},
                new Object[]{"InitializeLocalWithDynamicField", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[]{""}},
                new Object[]{"PolymorphicMethods1_Success", SecurityMonitoringEvent.PASSED, new String[]{""}},
                new Object[]{"PolymorphicMethods2_Success", SecurityMonitoringEvent.PASSED, new String[]{""}},
                new Object[]{"PolymorphicMethods1_Fail", SecurityMonitoringEvent.ILLEGAL_FLOW, new String[]{""}},
                new Object[]{"PolymorphicMethods2_Fail", SecurityMonitoringEvent.NSU_FAILURE, new String[]{""}}
        );
    }

    @Test
    /**
     * Runs each testfile specified above. note that the outputDir can be set to ones liking.
     */
    public void test() throws UnsupportedEncodingException {

        String outputDir = "jgs_unit";

        // compile
        String[] args = {"-m", "jgstestclasses." + name, "-o", "sootOutput/" + outputDir};
        Main.main(args);

        // run
        ClassRunner.testClass(name, outputDir, "jgstestclasses",
                              securityMonitoringEvents, involvedVars);

    }
}

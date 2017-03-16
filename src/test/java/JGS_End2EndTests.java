
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
public class JGS_End2EndTests {

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
    public JGS_End2EndTests(String name, ExpectedException expectedException,
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
                new Object[]{"SimpleCast_Fail", ExpectedException.ILLEGAL_FLOW, new String[]{"java.lang.Integer_$r5"}},
                new Object[]{"SimpleCast_Fail2", ExpectedException.ILLEGAL_FLOW, new String[]{"java.lang.Integer_$r5"}},
                new Object[]{"SimpleCast_Fail3", ExpectedException.ILLEGAL_FLOW, new String[]{"java.lang.Integer_$r5"}},
                new Object[]{"SimpleCast_Fail4", ExpectedException.ILLEGAL_FLOW, new String[]{"java.lang.String_r2"}},
                new Object[]{"NSUPolicy", ExpectedException.ILLEGAL_FLOW, new String[]{""}},
                new Object[]{"ScratchMonomorphic_Success", ExpectedException.NONE, new String[]{""}},
                new Object[]{"SimpleSuccess", ExpectedException.NONE, new String[]{""}},
                new Object[]{"SimpleCasts", ExpectedException.ILLEGAL_FLOW, new String[]{""}}
        );
    }

    @Test
    /**
     * Runs each testfile specified above. note that the outputDir can be set to ones liking.
     */
    public void test() {

        String outputDir = "jgs_unit";

        // compile
        String[] args = {"-m", "jgstestclasses." + name, "-o", "sootOutput/" + outputDir};
        Main.main(args);

        // run
        ClassRunner.testClass(name, outputDir, "jgstestclasses", expectedException, involvedVars);

        logger.info("Finished executing testclasses." + name + "");
    }
}

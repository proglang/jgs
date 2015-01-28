package junit;

import static junit.utils.JUnitMessageStoreHelper.checkMethodStoreEquality;
import static logging.AnalysisLogLevel.SECURITY;
import static logging.AnalysisLogLevel.SIDEEFFECT;
import static main.AnalysisType.CONSTRAINTS;
import static org.junit.Assert.fail;

import java.util.logging.Level;

import junit.model.TestFile;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import soot.G;

public class TestConstraintsSucceeding {

    private static final String TEST_PACKAGE = "junitConstraints";
    private static Level[] CHECK_LEVELS = { SECURITY, SIDEEFFECT };
    private static final TestFile VALID01 = new TestFile(TEST_PACKAGE,
                                                         "Valid01");
    private static final TestFile VALID02 = new TestFile(TEST_PACKAGE,
                                                         "Valid02");
    private static final TestFile VALID03 = new TestFile(TEST_PACKAGE,
                                                         "Valid03");
    private static final TestFile VALID04 = new TestFile(TEST_PACKAGE,
                                                         "Valid04");
    private static final TestFile VALID05 = new TestFile(TEST_PACKAGE,
                                                         "Valid05");
    private static final TestFile VALID06 = new TestFile(TEST_PACKAGE,
                                                         "Valid06");
    private static final TestFile VALID07 = new TestFile(TEST_PACKAGE,
                                                         "Valid07");
    private static final TestFile LEVEL_FUNCTION =
        new TestFile(TEST_PACKAGE, "SuccessLevelFunction");
    private static final TestFile METHOD_INSTANCE =
        new TestFile(TEST_PACKAGE, "SuccessMethodInstance");
    private static final TestFile METHOD_STATIC =
        new TestFile(TEST_PACKAGE, "SuccessMethodStatic");
    private static final TestFile FIELD_INSTANCE =
        new TestFile(TEST_PACKAGE, "SuccessFieldInstance");
    private static final TestFile FIELD_STATIC =
        new TestFile(TEST_PACKAGE, "SuccessFieldStatic");
    private static final TestFile EXPR = new TestFile(TEST_PACKAGE,
                                                      "SuccessExpr");
    private static final TestFile IF = new TestFile(TEST_PACKAGE, "SuccessIf");
    private static final TestFile OBJECT = new TestFile(TEST_PACKAGE,
                                                        "SuccessObject");
    private static final TestFile WHILE = new TestFile(TEST_PACKAGE,
                                                       "SuccessWhile");
    private static final TestFile ARRAY = new TestFile(TEST_PACKAGE,
                                                       "SuccessArray");
    private static final TestFile INHERITANCE =
        new TestFile(TEST_PACKAGE, "SuccessInheritance");
    private static final TestFile EFFECT = new TestFile(TEST_PACKAGE,
                                                        "SuccessWriteEffect");
    private static final TestFile SPECIAL_01 = new TestFile(TEST_PACKAGE,
                                                            "SuccessSpecial01");
    private static final TestFile SPECIAL_02 = new TestFile(TEST_PACKAGE,
                                                            "SuccessSpecial02");
    private static final TestFile SPECIAL_03 = new TestFile(TEST_PACKAGE,
                                                            "SuccessSpecial03");
    private static final TestFile POLYMORPHIC_SETTER =
        new TestFile(TEST_PACKAGE, "SuccessPolymorphicSetter");
    private static final TestFile LOW_REF_HIGH_UPDATE =
        new TestFile(TEST_PACKAGE, "SuccessLowRefHighUpdate");

    @BeforeClass
    public static final void init() {
        if (!System.getProperty("user.dir").endsWith("TaintTracking")) {
            fail("Working director is not the folder of the 'TaintTracking' project.");
        }
    }

    @Before
    public final void reset() {
        G.reset();
    }

    @Test
    public final void test01Valid() {
        checkMethodStoreEquality(VALID01, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test02Valid() {
        checkMethodStoreEquality(VALID02, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test03Valid() {
        checkMethodStoreEquality(VALID03, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test04Valid() {
        checkMethodStoreEquality(VALID04, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test05Valid() {
        checkMethodStoreEquality(VALID05, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test06Valid() {
        checkMethodStoreEquality(VALID06, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test07Valid() {
        checkMethodStoreEquality(VALID07, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test30SuccessLevelFunction() {
        checkMethodStoreEquality(LEVEL_FUNCTION, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test31SuccessMethodInstance() {
        checkMethodStoreEquality(METHOD_INSTANCE, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test32SuccessMethodStatic() {
        checkMethodStoreEquality(METHOD_STATIC, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test33SuccessFieldInstance() {
        checkMethodStoreEquality(FIELD_INSTANCE, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test34SuccessFieldStatic() {
        checkMethodStoreEquality(FIELD_STATIC, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test35SuccessExpr() {
        checkMethodStoreEquality(EXPR, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test36SuccessIf() {
        checkMethodStoreEquality(IF, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test37SuccessObject() {
        checkMethodStoreEquality(OBJECT, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test38SuccessWhile() {
        checkMethodStoreEquality(WHILE, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test39SuccessArray() {
        checkMethodStoreEquality(ARRAY, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test40SuccessInheritance() {
        checkMethodStoreEquality(INHERITANCE, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test41SuccessEffect() {
        checkMethodStoreEquality(EFFECT, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test80SuccessSpecial() {
        checkMethodStoreEquality(SPECIAL_01, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test81SuccessSpecial() {
        checkMethodStoreEquality(SPECIAL_02, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test82SuccessSpecial() {
        checkMethodStoreEquality(SPECIAL_03, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test82SuccessLowRefHighUpdate() {
        checkMethodStoreEquality(LOW_REF_HIGH_UPDATE, CHECK_LEVELS, CONSTRAINTS);
    }

    @Test
    public final void test83SuccessPolymorphicSetter() {
        checkMethodStoreEquality(POLYMORPHIC_SETTER, CHECK_LEVELS, CONSTRAINTS);
    }
}

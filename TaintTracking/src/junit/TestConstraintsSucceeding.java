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
	
	private static final String TEST_PACKAGE = "junitConstraints";private static Level[] CHECK_LEVELS = { SECURITY, SIDEEFFECT };	
	private static final TestFile VALID01 = new TestFile(TEST_PACKAGE, "Valid01");
	private static final TestFile LEVEL_FUNCTION = new TestFile(TEST_PACKAGE, "SuccessLevelFunction");
	private static final TestFile METHOD = new TestFile(TEST_PACKAGE, "SuccessMethod");
	private static final TestFile FIELD_INSTANCE = new TestFile(TEST_PACKAGE, "SuccessFieldInstance");
	private static final TestFile FIELD_STATIC = new TestFile(TEST_PACKAGE, "SuccessFieldStatic");
	private static final TestFile EXPR = new TestFile(TEST_PACKAGE, "SuccessExpr");
	private static final TestFile ARRAY = new TestFile(TEST_PACKAGE, "SuccessArray");
	private static final TestFile SPECIAL_01 = new TestFile(TEST_PACKAGE, "SuccessSpecial01");
	private static final TestFile POLYMORPHIC_SETTER = new TestFile(TEST_PACKAGE, "SuccessPolymorphicSetter");
	private static final TestFile LOW_REF_HIGH_UPDATE = new TestFile(TEST_PACKAGE, "SuccessLowRefHighUpdate");
	

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
	public final void test30SuccessLevelFunction() {
		checkMethodStoreEquality(LEVEL_FUNCTION, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test31SuccessMethod() {
		checkMethodStoreEquality(METHOD, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test32SuccessFieldInstance() {
		checkMethodStoreEquality(FIELD_INSTANCE, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test33SuccessFieldStatic() {
		checkMethodStoreEquality(FIELD_STATIC, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test34SuccessExpr() {
		checkMethodStoreEquality(EXPR, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test35SuccessArray() {
		checkMethodStoreEquality(ARRAY, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test80SuccessSpecial() {
		checkMethodStoreEquality(SPECIAL_01, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test81SuccessLowRefHighUpdate() {
	    checkMethodStoreEquality(LOW_REF_HIGH_UPDATE, CHECK_LEVELS, CONSTRAINTS);
	}

	@Test
	public final void test82SuccessPolymorphicSetter() {
	    checkMethodStoreEquality(POLYMORPHIC_SETTER, CHECK_LEVELS, CONSTRAINTS);
	}
}

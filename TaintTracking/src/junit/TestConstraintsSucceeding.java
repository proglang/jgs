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
	private static final TestFile VALID01 = new TestFile(TEST_PACKAGE, "Valid01");
	private static final TestFile SPECIAL_01 = new TestFile(TEST_PACKAGE, "SuccessSpecial01");
	private static final TestFile LEVEL_FUNCTION = new TestFile(TEST_PACKAGE, "SuccessLevelFunction");
	private static final TestFile METHOD = new TestFile(TEST_PACKAGE, "SuccessMethod");
	private static final TestFile FIELD = new TestFile(TEST_PACKAGE, "SuccessField");
	private static final TestFile EXPR = new TestFile(TEST_PACKAGE, "SuccessExpr");

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
	public final void test32SuccessField() {
		checkMethodStoreEquality(FIELD, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test33Expr() {
		checkMethodStoreEquality(EXPR, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test90SuccessSpecial() {
		checkMethodStoreEquality(SPECIAL_01, CHECK_LEVELS, CONSTRAINTS);
	}
}

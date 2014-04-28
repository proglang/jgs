package junit;

import static junit.utils.JUnitMessageStoreHelper.checkMethodStoreEquality;
import static logging.AnalysisLogLevel.SECURITY;
import static logging.AnalysisLogLevel.SIDEEFFECT;
import static main.AnalysisType.LEVELS;
import static org.junit.Assert.fail;

import java.util.logging.Level;

import junit.model.TestFile;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import soot.G;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLevelSucceeding {
	
	private static final String TEST_PACKAGE = "junitLevel";
	private static Level[] CHECK_LEVELS = { SECURITY, SIDEEFFECT };
	private static final TestFile ARRAY = new TestFile(TEST_PACKAGE, "SuccessArray");	
	private static final TestFile EXPR = new TestFile(TEST_PACKAGE, "SuccessExpr");
	private static final TestFile FIELD = new TestFile(TEST_PACKAGE, "SuccessField");
	private static final TestFile ID = new TestFile(TEST_PACKAGE, "SuccessIdFunctions");
	private static final TestFile IFELSE = new TestFile(TEST_PACKAGE, "SuccessIfElse");
	private static final TestFile LOOP = new TestFile(TEST_PACKAGE, "SuccessLoop");
	private static final TestFile METHOD = new TestFile(TEST_PACKAGE, "SuccessMethod");
	private static final TestFile OBJECT = new TestFile(TEST_PACKAGE, "SuccessObject");
	private static final TestFile STATIC_FIELD = new TestFile(TEST_PACKAGE, "SuccessStaticField");
	private static final TestFile STATIC_METHOD = new TestFile(TEST_PACKAGE, "SuccessStaticMethod");	
	private static final TestFile VALID01 = new TestFile(TEST_PACKAGE, "Valid01");
	private static final TestFile VALID02 = new TestFile(TEST_PACKAGE, "Valid02");
	private static final TestFile VALID03 = new TestFile(TEST_PACKAGE, "Valid03");
	private static final TestFile VALID04 = new TestFile(TEST_PACKAGE, "Valid04");
	private static final TestFile VALID05 = new TestFile(TEST_PACKAGE, "Valid05");
	private static final TestFile VALID06 = new TestFile(TEST_PACKAGE, "Valid06");
	private static final TestFile VALID07 = new TestFile(TEST_PACKAGE, "Valid07");

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
		checkMethodStoreEquality(VALID01, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test02Valid() {
		checkMethodStoreEquality(VALID02, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test03Valid() {
		checkMethodStoreEquality(VALID03, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test04Valid() {
		checkMethodStoreEquality(VALID04, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test05Valid() {
		checkMethodStoreEquality(VALID05, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test06Valid() {
		checkMethodStoreEquality(VALID06, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test07Valid() {
		checkMethodStoreEquality(VALID07, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test08Array() {
		checkMethodStoreEquality(ARRAY, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test09Expr() {
		checkMethodStoreEquality(EXPR, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test10IdFunction() {
		checkMethodStoreEquality(ID, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test11Field() {
		checkMethodStoreEquality(FIELD, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test12StaticField() {
		checkMethodStoreEquality(STATIC_FIELD, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test13Object() {
		checkMethodStoreEquality(OBJECT, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test14Method() {
		checkMethodStoreEquality(METHOD, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test15StaticMethod() {
		checkMethodStoreEquality(STATIC_METHOD, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test16Loop() {
		checkMethodStoreEquality(LOOP, CHECK_LEVELS, LEVELS);
	}

	@Test
	public final void test17IfElse() {
		checkMethodStoreEquality(IFELSE, CHECK_LEVELS, LEVELS);
	}

}

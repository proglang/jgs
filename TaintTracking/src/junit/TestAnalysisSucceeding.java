package junit;

import static org.junit.Assert.fail;

import java.util.logging.Level;

import junit.model.TestFile;
import junit.utils.JUnitMessageStoreHelper;


import logging.AnalysisLogLevel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import soot.G;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAnalysisSucceeding {

	private static Level[] CHECK_LEVELS = { AnalysisLogLevel.ERROR,
			AnalysisLogLevel.EXCEPTION, AnalysisLogLevel.SECURITY,
			AnalysisLogLevel.SIDEEFFECT, AnalysisLogLevel.SECURITYCHECKER };
	private static final String TEST_PACKAGE = "junitAnalysis";

	private static final TestFile VALID01 = new TestFile(TEST_PACKAGE, "Valid01");
	private static final TestFile VALID02 = new TestFile(TEST_PACKAGE, "Valid02");
	private static final TestFile VALID03 = new TestFile(TEST_PACKAGE, "Valid03");
	private static final TestFile VALID04 = new TestFile(TEST_PACKAGE, "Valid04");
	private static final TestFile VALID05 = new TestFile(TEST_PACKAGE, "Valid05");
	private static final TestFile VALID06 = new TestFile(TEST_PACKAGE, "Valid06");
	private static final TestFile VALID07 = new TestFile(TEST_PACKAGE, "Valid07");
	private static final TestFile ARRAY = new TestFile(TEST_PACKAGE, "SuccessArray");
	private static final TestFile EXPR = new TestFile(TEST_PACKAGE, "SuccessExpr");
	private static final TestFile ID = new TestFile(TEST_PACKAGE, "SuccessIdFunctions");
	private static final TestFile FIELD = new TestFile(TEST_PACKAGE, "SuccessField");
	private static final TestFile STATIC_FIELD = new TestFile(TEST_PACKAGE, "SuccessStaticField");
	private static final TestFile OBJECT = new TestFile(TEST_PACKAGE, "SuccessObject");
	private static final TestFile IFELSE = new TestFile(TEST_PACKAGE, "SuccessIfElse");
	private static final TestFile LOOP = new TestFile(TEST_PACKAGE, "SuccessLoop");
	private static final TestFile METHOD = new TestFile(TEST_PACKAGE, "SuccessMethod");
	private static final TestFile STATIC_METHOD = new TestFile(TEST_PACKAGE, "SuccessStaticMethod");
	
	@BeforeClass
	public static final void init() {
		if (!System.getProperty("user.dir").endsWith("Testcases/src")) {
			fail("Working director is not the source folder of the 'Testcases' project.");
		}
	}

	@Before
	public final void reset() {
		G.reset();
	}
	
	@Test
	public final void test01Valid() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(VALID01, CHECK_LEVELS);
	}

	@Test
	public final void test02Valid() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(VALID02, CHECK_LEVELS);
	}

	@Test
	public final void test03Valid() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(VALID03, CHECK_LEVELS);
	}

	@Test
	public final void test04Valid() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(VALID04, CHECK_LEVELS);
	}

	@Test
	public final void test05Valid() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(VALID05, CHECK_LEVELS);
	}

	@Test
	public final void test06Valid() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(VALID06, CHECK_LEVELS);
	}

	@Test
	public final void test07Valid() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(VALID07, CHECK_LEVELS);
	}
	
	@Test
	public final void test08Array() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(ARRAY, CHECK_LEVELS);
	}
	
	@Test
	public final void test09Expr() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(EXPR, CHECK_LEVELS);
	}
	
	@Test
	public final void test10IdFunction() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(ID, CHECK_LEVELS);
	}
	
	@Test
	public final void test11Field() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(FIELD, CHECK_LEVELS);
	}
	
	@Test
	public final void test12StaticField() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(STATIC_FIELD, CHECK_LEVELS);
	}
	
	@Test
	public final void test13Object() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(OBJECT, CHECK_LEVELS);
	}
	
	@Test
	public final void test14Method() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(METHOD, CHECK_LEVELS);
	}
	
	@Test
	public final void test15StaticMethod() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(STATIC_METHOD, CHECK_LEVELS);
	}
	
	@Test
	public final void test16Loop() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(LOOP, CHECK_LEVELS);
	}
	
	@Test
	public final void test17IfElse() {
		JUnitMessageStoreHelper.checkMethodStoreEquality(IFELSE, CHECK_LEVELS);
	}

}

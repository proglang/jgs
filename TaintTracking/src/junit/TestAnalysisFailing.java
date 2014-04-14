package junit;

import static junit.utils.JUnitMessageStoreHelper.checkMethodStoreEquality;
import static logging.AnalysisLogLevel.SECURITY;
import static logging.AnalysisLogLevel.SIDEEFFECT;
import static org.junit.Assert.fail;

import java.util.logging.Level;

import junit.model.TestFile;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import soot.G;
import exception.ExtractorException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAnalysisFailing {


	private static final Level[] CHECK_LEVELS = { SECURITY, SIDEEFFECT};
	private static final String TEST_PACKAGE = "junitAnalysis";
	private static final TestFile ARRAY = new TestFile(TEST_PACKAGE, "FailArray");
	private static final TestFile EXPR = new TestFile(TEST_PACKAGE, "FailExpr");
	private static final TestFile FIELD = new TestFile(TEST_PACKAGE, "FailField");
	private static final TestFile ID = new TestFile(TEST_PACKAGE, "FailIdFunctions");
	private static final TestFile IF_ELSE = new TestFile(TEST_PACKAGE, "FailIfElse");
	private static final TestFile IF_ELSE2 = new TestFile(TEST_PACKAGE, "FailIfElse2");
	private static final TestFile INVALID01 = new TestFile(TEST_PACKAGE, "Invalid01");
	private static final TestFile INVALID02 = new TestFile(TEST_PACKAGE, "Invalid02");
	private static final TestFile INVALID03 = new TestFile(TEST_PACKAGE, "Invalid03");	
	private static final TestFile INVALID05 = new TestFile(TEST_PACKAGE, "Invalid05");
	private static final TestFile INVALID06 = new TestFile(TEST_PACKAGE, "Invalid06");
	private static final TestFile INVALID07 = new TestFile(TEST_PACKAGE, "Invalid07");
	private static final TestFile INVALID08 = new TestFile(TEST_PACKAGE, "Invalid08");
	private static final TestFile INVALID09 = new TestFile(TEST_PACKAGE, "Invalid09");
	private static final TestFile INVALID10 = new TestFile(TEST_PACKAGE, "Invalid10");
	private static final TestFile INVALID11 = new TestFile(TEST_PACKAGE, "Invalid11");
	private static final TestFile INVALID12 = new TestFile(TEST_PACKAGE, "Invalid12");
	private static final TestFile INVALID13 = new TestFile(TEST_PACKAGE, "Invalid13");
	private static final TestFile INVALID14 = new TestFile(TEST_PACKAGE, "Invalid14");
	private static final TestFile INVALID15 = new TestFile(TEST_PACKAGE, "Invalid15");
	private static final TestFile INVALID16 = new TestFile(TEST_PACKAGE, "Invalid16");
	private static final TestFile INVALID17 = new TestFile(TEST_PACKAGE, "Invalid17");
	private static final TestFile INVALID18 = new TestFile(TEST_PACKAGE, "Invalid18");
	private static final TestFile INVALID19 = new TestFile(TEST_PACKAGE, "Invalid19");
	private static final TestFile INVALID20 = new TestFile(TEST_PACKAGE, "Invalid20");
	private static final TestFile INVALID21 = new TestFile(TEST_PACKAGE, "Invalid21");
	private static final TestFile INVALID22 = new TestFile(TEST_PACKAGE, "Invalid22");
	private static final TestFile LOOP = new TestFile(TEST_PACKAGE, "FailLoop");
	private static final TestFile METHOD = new TestFile(TEST_PACKAGE, "FailMethod");
	private static final TestFile OBJECT = new TestFile(TEST_PACKAGE, "FailObject");
	private static final TestFile STATIC_FIELD = new TestFile(TEST_PACKAGE, "FailStaticField");
	private static final TestFile STATIC_METHOD = new TestFile(TEST_PACKAGE, "FailStaticMethod");

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

	
	@Test(expected = ExtractorException.class)
	public final void test01Invalid() {
		checkMethodStoreEquality(INVALID01, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test02Invalid() {
		checkMethodStoreEquality(INVALID02, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test03Invalid() {
		checkMethodStoreEquality(INVALID03, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test05Invalid() {
		checkMethodStoreEquality(INVALID05, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test06Invalid() {
		checkMethodStoreEquality(INVALID06, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test07Invalid() {
		checkMethodStoreEquality(INVALID07, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test08Invalid() {
		checkMethodStoreEquality(INVALID08, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test09Invalid() {
		checkMethodStoreEquality(INVALID09, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test10Invalid() {
		checkMethodStoreEquality(INVALID10, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test11Invalid() {
		checkMethodStoreEquality(INVALID11, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test12Invalid() {
		checkMethodStoreEquality(INVALID12, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test13Invalid() {
		checkMethodStoreEquality(INVALID13, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test14Invalid() {
		checkMethodStoreEquality(INVALID14, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test15Invalid() {
		checkMethodStoreEquality(INVALID15, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test16Invalid() {
		checkMethodStoreEquality(INVALID16, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test17Invalid() {
		checkMethodStoreEquality(INVALID17, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test18Invalid() {
		checkMethodStoreEquality(INVALID18, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test19Invalid() {
		checkMethodStoreEquality(INVALID19, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test20Invalid() {
		checkMethodStoreEquality(INVALID20, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test21Invalid() {
		checkMethodStoreEquality(INVALID21, CHECK_LEVELS);
	}

	@Test(expected = ExtractorException.class)
	public final void test22Invalid() {
		checkMethodStoreEquality(INVALID22, CHECK_LEVELS);
	}

	@Test
	public final void test23Array() {
		checkMethodStoreEquality(ARRAY, CHECK_LEVELS);
	}

	@Test
	public final void test24Expr() {
		checkMethodStoreEquality(EXPR, CHECK_LEVELS);
	}

	@Test
	public final void test25IdFunction() {
		checkMethodStoreEquality(ID, CHECK_LEVELS);
	}

	@Test
	public final void test26Field() {
		checkMethodStoreEquality(FIELD, CHECK_LEVELS);
	}

	@Test
	public final void test27StaticField() {
		checkMethodStoreEquality(STATIC_FIELD, CHECK_LEVELS);
	}
	
	@Test
	public final void test28Object() {
		checkMethodStoreEquality(OBJECT, CHECK_LEVELS);
	}
	
	@Test
	public final void test29Method() {
		checkMethodStoreEquality(METHOD, CHECK_LEVELS);
	}
	
	@Test
	public final void test30StaticMethod() {
		checkMethodStoreEquality(STATIC_METHOD, CHECK_LEVELS);
	}
	
	@Test
	public final void test31Lopp() {
		checkMethodStoreEquality(LOOP, CHECK_LEVELS);
	}
	
	@Test
	public final void test32IfElse() {
		checkMethodStoreEquality(IF_ELSE, CHECK_LEVELS);
	}
	
	@Test
	public final void test33IfElse2() {
		checkMethodStoreEquality(IF_ELSE2, CHECK_LEVELS);
	}
	
}

package junit;

import static junit.utils.JUnitMessageStoreHelper.checkMethodStoreEquality;
import static logging.AnalysisLogLevel.SECURITY;
import static logging.AnalysisLogLevel.SIDEEFFECT;
import static org.junit.Assert.fail;

import java.util.logging.Level;

import junit.model.TestFile;

import static main.AnalysisType.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import soot.G;
import exception.ExtractorException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConstraintsFailing {


	private static final Level[] CHECK_LEVELS = { SECURITY, SIDEEFFECT};
	private static final String TEST_PACKAGE = "junitConstraints";
	private static final TestFile INVALID01 = new TestFile(TEST_PACKAGE, "Invalid01");	
	private static final TestFile INVALID02 = new TestFile(TEST_PACKAGE, "Invalid02");
	private static final TestFile INVALID03 = new TestFile(TEST_PACKAGE, "Invalid03");
	private static final TestFile INVALID04 = new TestFile(TEST_PACKAGE, "Invalid04");
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
	private static final TestFile ID_FUNCTION = new TestFile(TEST_PACKAGE, "FailIdFunction");
	private static final TestFile METHOD = new TestFile(TEST_PACKAGE, "FailMethod");
	
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
		checkMethodStoreEquality(INVALID01, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test02Invalid() {
		checkMethodStoreEquality(INVALID02, CHECK_LEVELS, CONSTRAINTS);
	}

	@Test(expected = ExtractorException.class)
	public final void test03Invalid() {
		checkMethodStoreEquality(INVALID03, CHECK_LEVELS, CONSTRAINTS);
	}

	@Test(expected = ExtractorException.class)
	public final void test04Invalid() {
		checkMethodStoreEquality(INVALID04, CHECK_LEVELS, CONSTRAINTS);
	}

	@Test(expected = ExtractorException.class)
	public final void test05Invalid() {
		checkMethodStoreEquality(INVALID05, CHECK_LEVELS, CONSTRAINTS);
	}

	@Test(expected = ExtractorException.class)
	public final void test06Invalid() {
		checkMethodStoreEquality(INVALID06, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test07Invalid() {
		checkMethodStoreEquality(INVALID07, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test08Invalid() {
		checkMethodStoreEquality(INVALID08, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test09Invalid() {
		checkMethodStoreEquality(INVALID09, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test10Invalid() {
		checkMethodStoreEquality(INVALID10, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test11Invalid() {
		checkMethodStoreEquality(INVALID11, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test12Invalid() {
		checkMethodStoreEquality(INVALID12, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test13Invalid() {
		checkMethodStoreEquality(INVALID13, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test14Invalid() {
		checkMethodStoreEquality(INVALID14, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test()
	public final void test30IdFunction() {
		checkMethodStoreEquality(ID_FUNCTION, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test()
	public final void test31Method() {
		checkMethodStoreEquality(METHOD, CHECK_LEVELS, CONSTRAINTS);
	}
}

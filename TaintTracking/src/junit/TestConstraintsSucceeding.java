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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import soot.G;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConstraintsSucceeding {
	
	private static final String TEST_PACKAGE = "junitConstraints";
	private static Level[] CHECK_LEVELS = { SECURITY, SIDEEFFECT };	
	private static final TestFile VALID01 = new TestFile(TEST_PACKAGE, "Valid01");
	private static final TestFile SUCCESS_SPECIAL_01 = new TestFile(TEST_PACKAGE, "SuccessSpecial01");

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
	public final void test02SuccessSpecial() {
		checkMethodStoreEquality(SUCCESS_SPECIAL_01, CHECK_LEVELS, CONSTRAINTS);
	}
}

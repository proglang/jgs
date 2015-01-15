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
import exception.AnalysisException;
import exception.ExtractorException;

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
	private static final TestFile INVALID15 = new TestFile(TEST_PACKAGE, "Invalid15");
	private static final TestFile INVALID16 = new TestFile(TEST_PACKAGE, "Invalid16");
	private static final TestFile INVALID17 = new TestFile(TEST_PACKAGE, "Invalid17");
	private static final TestFile INVALID18 = new TestFile(TEST_PACKAGE, "Invalid18");
	private static final TestFile INVALID19 = new TestFile(TEST_PACKAGE, "Invalid19");
	private static final TestFile INVALID20 = new TestFile(TEST_PACKAGE, "Invalid20");
	private static final TestFile INVALID21 = new TestFile(TEST_PACKAGE, "Invalid21");
	private static final TestFile INVALID22 = new TestFile(TEST_PACKAGE, "Invalid22");
	private static final TestFile INVALID23 = new TestFile(TEST_PACKAGE, "Invalid23");
	private static final TestFile INVALID24 = new TestFile(TEST_PACKAGE, "Invalid24");
	private static final TestFile INVALID25 = new TestFile(TEST_PACKAGE, "Invalid25");
	private static final TestFile INVALID26 = new TestFile(TEST_PACKAGE, "Invalid26");
	private static final TestFile INVALID27 = new TestFile(TEST_PACKAGE, "Invalid27");
	private static final TestFile INVALID28 = new TestFile(TEST_PACKAGE, "Invalid28");
	private static final TestFile INVALID29 = new TestFile(TEST_PACKAGE, "Invalid29");
	private static final TestFile INVALID30 = new TestFile(TEST_PACKAGE, "Invalid30");
	private static final TestFile INVALID31 = new TestFile(TEST_PACKAGE, "Invalid31");
	private static final TestFile INVALID32 = new TestFile(TEST_PACKAGE, "Invalid32");
	private static final TestFile INVALID33 = new TestFile(TEST_PACKAGE, "Invalid33");
	private static final TestFile INVALID34 = new TestFile(TEST_PACKAGE, "Invalid34");
	private static final TestFile INVALID35 = new TestFile(TEST_PACKAGE, "Invalid35");
	private static final TestFile INVALID36 = new TestFile(TEST_PACKAGE, "Invalid36");
	private static final TestFile INVALID37 = new TestFile(TEST_PACKAGE, "Invalid37");
	private static final TestFile INVALID38 = new TestFile(TEST_PACKAGE, "Invalid38");
	private static final TestFile LEVEL_FUNCTION = new TestFile(TEST_PACKAGE, "FailLevelFunction");
	private static final TestFile METHOD_INSTANCE = new TestFile(TEST_PACKAGE, "FailMethodInstance");
	private static final TestFile METHOD_STATIC = new TestFile(TEST_PACKAGE, "FailMethodStatic");
	private static final TestFile FIELD_INSTANCE = new TestFile(TEST_PACKAGE, "FailFieldInstance");
	private static final TestFile FIELD_STATIC = new TestFile(TEST_PACKAGE, "FailFieldStatic");
	private static final TestFile EXPR = new TestFile(TEST_PACKAGE, "FailExpr");
	private static final TestFile IF = new TestFile(TEST_PACKAGE, "FailIf");
	private static final TestFile OBJECT = new TestFile(TEST_PACKAGE, "FailObject");
	private static final TestFile WHILE = new TestFile(TEST_PACKAGE, "FailWhile");
	private static final TestFile ARRAY = new TestFile(TEST_PACKAGE, "FailArray");
	private static final TestFile INHERITANCE = new TestFile(TEST_PACKAGE, "FailInheritance");
	private static final TestFile EFFECT = new TestFile(TEST_PACKAGE, "FailWriteEffect");
	private static final TestFile ARRAY_SIGNATURE = new TestFile(TEST_PACKAGE, "FailArraySignature");
	private static final TestFile ARRAY_ASSIGN = new TestFile(TEST_PACKAGE, "FailArrayAssign");
	private static final TestFile ASSIGN_SAME = new TestFile(TEST_PACKAGE, "FailAssignSame");
	private static final TestFile PUTFIELD_IMPLICIT_LEAK = new TestFile(TEST_PACKAGE, "FailPutfieldImplicitLeak");
	private static final TestFile EXAMPLE01 = new TestFile(TEST_PACKAGE, "FailExample01");
	
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

	@Test(expected = ExtractorException.class)
	public final void test15Invalid() {
		checkMethodStoreEquality(INVALID15, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test16Invalid() {
		checkMethodStoreEquality(INVALID16, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test17Invalid() {
		checkMethodStoreEquality(INVALID17, CHECK_LEVELS, CONSTRAINTS);
	}

	@Test(expected = ExtractorException.class)
	public final void test18Invalid() {
		checkMethodStoreEquality(INVALID18, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test19Invalid() {
		checkMethodStoreEquality(INVALID19, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test20Invalid() {
		checkMethodStoreEquality(INVALID20, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = AnalysisException.class)
	public final void test21Invalid() {
		checkMethodStoreEquality(INVALID21, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = AnalysisException.class)
	public final void test22Invalid() {
		checkMethodStoreEquality(INVALID22, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = AnalysisException.class)
	public final void test23Invalid() {
		checkMethodStoreEquality(INVALID23, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test24Invalid() {
		checkMethodStoreEquality(INVALID24, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test25Invalid() {
		checkMethodStoreEquality(INVALID25, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test26Invalid() {
		checkMethodStoreEquality(INVALID26, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test27Invalid() {
		checkMethodStoreEquality(INVALID27, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test28Invalid() {
		checkMethodStoreEquality(INVALID28, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test29Invalid() {
		checkMethodStoreEquality(INVALID29, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test(expected = ExtractorException.class)
	public final void test30Invalid() {
		checkMethodStoreEquality(INVALID30, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test31Invalid() {
		checkMethodStoreEquality(INVALID31, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test32Invalid() {
		checkMethodStoreEquality(INVALID32, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test33Invalid() {
		checkMethodStoreEquality(INVALID33, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test34Invalid() {
		checkMethodStoreEquality(INVALID34, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test35Invalid() {
		checkMethodStoreEquality(INVALID35, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test36Invalid() {
		checkMethodStoreEquality(INVALID36, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test37Invalid() {
		checkMethodStoreEquality(INVALID37, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test38Invalid() {
		checkMethodStoreEquality(INVALID38, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test50FailLevelFunction() {
		checkMethodStoreEquality(LEVEL_FUNCTION, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test51FailMethodInstance() {
		checkMethodStoreEquality(METHOD_INSTANCE, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test52FailMethodStatic() {
		checkMethodStoreEquality(METHOD_STATIC, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test52FailFieldInstance() {
		checkMethodStoreEquality(FIELD_INSTANCE, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test53FailFieldStatic() {
		checkMethodStoreEquality(FIELD_STATIC, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test54FailExpr() {
		checkMethodStoreEquality(EXPR, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test55FailIf() {
		checkMethodStoreEquality(IF, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test56FailObject() {
		checkMethodStoreEquality(OBJECT, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test57FailWhile() {
		checkMethodStoreEquality(WHILE, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test58FailArray() {
		checkMethodStoreEquality(ARRAY, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test59FailInheritance() {
		checkMethodStoreEquality(INHERITANCE, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test60FailEffect() {
		checkMethodStoreEquality(EFFECT, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test80FailAssignSame() {
	   checkMethodStoreEquality(ASSIGN_SAME, CHECK_LEVELS, CONSTRAINTS);
	}

	@Test
	public final void test81FailPutfieldImplicitLeak() {
	   checkMethodStoreEquality(PUTFIELD_IMPLICIT_LEAK, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test82FailArrayAssign() {
	    checkMethodStoreEquality(ARRAY_ASSIGN, CHECK_LEVELS, CONSTRAINTS);
	}
	
	@Test
	public final void test83FailArraySignature() {
		checkMethodStoreEquality(ARRAY_SIGNATURE, CHECK_LEVELS, CONSTRAINTS);
	}

	@Test
	public final void test100FailExample01() {
		checkMethodStoreEquality(EXAMPLE01, CHECK_LEVELS, CONSTRAINTS);
	}
	
}

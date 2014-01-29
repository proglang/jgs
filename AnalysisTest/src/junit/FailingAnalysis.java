package junit;

import static org.junit.Assert.*;

import java.util.logging.Level;

import junit.TestUtils.TestFile;

import logging.SootLoggerLevel;

import org.junit.*;
import org.junit.runners.MethodSorters;

import soot.G;
import analysisFail.FailArray;
import analysisFail.FailExpr;
import analysisFail.FailField;
import analysisFail.FailIdFunctions;
import analysisFail.FailIfElse;
import analysisFail.FailIfElse2;
import analysisFail.FailLoop;
import analysisFail.FailMethod;
import analysisFail.FailObject;
import analysisFail.FailStaticField;
import analysisFail.FailStaticMethod;
import annotationValidity.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FailingAnalysis {

	private static Level[] CHECK_LEVELS = { SootLoggerLevel.ERROR,
			SootLoggerLevel.EXCEPTION, SootLoggerLevel.SECURITY,
			SootLoggerLevel.SIDEEFFECT, SootLoggerLevel.SECURITYCHECKER };

	private static final TestFile<Invalid01> INVALID01 = new TestFile<Invalid01>(
			Invalid01.class);
	private static final TestFile<Invalid02> INVALID02 = new TestFile<Invalid02>(
			Invalid02.class);
	private static final TestFile<Invalid03> INVALID03 = new TestFile<Invalid03>(
			Invalid03.class);
	private static final TestFile<Invalid04> INVALID04 = new TestFile<Invalid04>(
			Invalid04.class);
	private static final TestFile<Invalid05> INVALID05 = new TestFile<Invalid05>(
			Invalid05.class);
	private static final TestFile<Invalid06> INVALID06 = new TestFile<Invalid06>(
			Invalid06.class);
	private static final TestFile<Invalid07> INVALID07 = new TestFile<Invalid07>(
			Invalid07.class);
	private static final TestFile<Invalid08> INVALID08 = new TestFile<Invalid08>(
			Invalid08.class);
	private static final TestFile<Invalid09> INVALID09 = new TestFile<Invalid09>(
			Invalid09.class);
	private static final TestFile<Invalid10> INVALID10 = new TestFile<Invalid10>(
			Invalid10.class);
	private static final TestFile<Invalid11> INVALID11 = new TestFile<Invalid11>(
			Invalid11.class);
	private static final TestFile<Invalid12> INVALID12 = new TestFile<Invalid12>(
			Invalid12.class);
	private static final TestFile<Invalid13> INVALID13 = new TestFile<Invalid13>(
			Invalid13.class);
	private static final TestFile<Invalid14> INVALID14 = new TestFile<Invalid14>(
			Invalid14.class);
	private static final TestFile<Invalid15> INVALID15 = new TestFile<Invalid15>(
			Invalid15.class);
	private static final TestFile<Invalid16> INVALID16 = new TestFile<Invalid16>(
			Invalid16.class);
	private static final TestFile<Invalid17> INVALID17 = new TestFile<Invalid17>(
			Invalid17.class);
	private static final TestFile<Invalid18> INVALID18 = new TestFile<Invalid18>(
			Invalid18.class);
	private static final TestFile<Invalid19> INVALID19 = new TestFile<Invalid19>(
			Invalid19.class);
	private static final TestFile<Invalid20> INVALID20 = new TestFile<Invalid20>(
			Invalid20.class);
	private static final TestFile<Invalid21> INVALID21 = new TestFile<Invalid21>(
			Invalid21.class);
	private static final TestFile<Invalid22> INVALID22 = new TestFile<Invalid22>(
			Invalid22.class);
	private static final TestFile<FailArray> ARRAY = new TestFile<FailArray>(
			FailArray.class);
	private static final TestFile<FailExpr> EXPR = new TestFile<FailExpr>(
			FailExpr.class);
	private static final TestFile<FailIdFunctions> ID = new TestFile<FailIdFunctions>(
			FailIdFunctions.class);
	private static final TestFile<FailField> FIELD = new TestFile<FailField>(
			FailField.class);
	private static final TestFile<FailStaticField> STATIC_FIELD = new TestFile<FailStaticField>(
			FailStaticField.class);
	private static final TestFile<FailObject> OBJECT = new TestFile<FailObject>(
			FailObject.class);
	private static final TestFile<FailMethod> METHOD = new TestFile<FailMethod>(
			FailMethod.class);
	private static final TestFile<FailStaticMethod> STATIC_METHOD = new TestFile<FailStaticMethod>(
			FailStaticMethod.class);
	private static final TestFile<FailLoop> LOOP = new TestFile<FailLoop>(
			FailLoop.class);
	private static final TestFile<FailIfElse> IF_ELSE = new TestFile<FailIfElse>(
			FailIfElse.class);
	private static final TestFile<FailIfElse2> IF_ELSE2 = new TestFile<FailIfElse2>(
			FailIfElse2.class);

	@BeforeClass
	public static final void init() {
		if (!System.getProperty("user.dir").endsWith("AnalysisTest/src")) {
			fail("Working director is not the source folder of the AnalysisTest project.");
		}
	}

	@Before
	public final void reset() {
		G.reset();
	}

	@Test
	public final void test01Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID01, CHECK_LEVELS);
	}

	@Test
	public final void test02Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID02, CHECK_LEVELS);
	}

	@Test
	public final void test03Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID03, CHECK_LEVELS);
	}

	@Test
	public final void test04Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID04, CHECK_LEVELS);
	}

	@Test
	public final void test05Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID05, CHECK_LEVELS);
	}

	@Test
	public final void test06Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID06, CHECK_LEVELS);
	}

	@Test
	public final void test07Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID07, CHECK_LEVELS);
	}

	@Test
	public final void test08Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID08, CHECK_LEVELS);
	}

	@Test
	public final void test09Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID09, CHECK_LEVELS);
	}

	@Test
	public final void test10Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID10, CHECK_LEVELS);
	}

	@Test
	public final void test11Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID11, CHECK_LEVELS);
	}

	@Test
	public final void test12Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID12, CHECK_LEVELS);
	}

	@Test
	public final void test13Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID13, CHECK_LEVELS);
	}

	@Test
	public final void test14Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID14, CHECK_LEVELS);
	}

	@Test
	public final void test15Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID15, CHECK_LEVELS);
	}

	@Test
	public final void test16Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID16, CHECK_LEVELS);
	}

	@Test
	public final void test17Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID17, CHECK_LEVELS);
	}

	@Test
	public final void test18Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID18, CHECK_LEVELS);
	}

	@Test
	public final void test19Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID19, CHECK_LEVELS);
	}

	@Test
	public final void test20Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID20, CHECK_LEVELS);
	}

	@Test
	public final void test21Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID21, CHECK_LEVELS);
	}

	@Test
	public final void test22Invalid() {
		TestUtils.checkMethodStoreEquality(INVALID22, CHECK_LEVELS);
	}

	@Test
	public final void test23Array() {
		TestUtils.checkMethodStoreEquality(ARRAY, CHECK_LEVELS);
	}

	@Test
	public final void test24Expr() {
		TestUtils.checkMethodStoreEquality(EXPR, CHECK_LEVELS);
	}

	@Test
	public final void test25IdFunction() {
		TestUtils.checkMethodStoreEquality(ID, CHECK_LEVELS);
	}

	@Test
	public final void test26Field() {
		TestUtils.checkMethodStoreEquality(FIELD, CHECK_LEVELS);
	}

	@Test
	public final void test27StaticField() {
		TestUtils.checkMethodStoreEquality(STATIC_FIELD, CHECK_LEVELS);
	}
	
	@Test
	public final void test28Object() {
		TestUtils.checkMethodStoreEquality(OBJECT, CHECK_LEVELS);
	}
	
	@Test
	public final void test29Method() {
		TestUtils.checkMethodStoreEquality(METHOD, CHECK_LEVELS);
	}
	
	@Test
	public final void test30StaticMethod() {
		TestUtils.checkMethodStoreEquality(STATIC_METHOD, CHECK_LEVELS);
	}
	
	@Test
	public final void test31Lopp() {
		TestUtils.checkMethodStoreEquality(LOOP, CHECK_LEVELS);
	}
	
	@Test
	public final void test32IfElse() {
		TestUtils.checkMethodStoreEquality(IF_ELSE, CHECK_LEVELS);
	}
	
	@Test
	public final void test33IfElse2() {
		TestUtils.checkMethodStoreEquality(IF_ELSE2, CHECK_LEVELS);
	}
	
}

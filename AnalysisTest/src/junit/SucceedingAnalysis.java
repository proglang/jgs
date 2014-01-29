package junit;

import static org.junit.Assert.*;

import java.util.logging.Level;

import junit.TestUtils.TestFile;

import logging.SootLoggerLevel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import soot.G;

import analysisSuccess.SuccessArray;
import analysisSuccess.SuccessExpr;
import analysisSuccess.SuccessField;
import analysisSuccess.SuccessIdFunctions;
import analysisSuccess.SuccessIfElse;
import analysisSuccess.SuccessLoop;
import analysisSuccess.SuccessMethod;
import analysisSuccess.SuccessObject;
import analysisSuccess.SuccessStaticField;
import analysisSuccess.SuccessStaticMethod;
import annotationValidity.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SucceedingAnalysis {

	private static Level[] CHECK_LEVELS = { SootLoggerLevel.ERROR,
			SootLoggerLevel.EXCEPTION, SootLoggerLevel.SECURITY,
			SootLoggerLevel.SIDEEFFECT, SootLoggerLevel.SECURITYCHECKER };

	private static final TestFile<Valid01> VALID01 = new TestFile<Valid01>(
			Valid01.class);
	private static final TestFile<Valid02> VALID02 = new TestFile<Valid02>(
			Valid02.class);
	private static final TestFile<Valid03> VALID03 = new TestFile<Valid03>(
			Valid03.class);
	private static final TestFile<Valid04> VALID04 = new TestFile<Valid04>(
			Valid04.class);
	private static final TestFile<Valid05> VALID05 = new TestFile<Valid05>(
			Valid05.class);
	private static final TestFile<Valid06> VALID06 = new TestFile<Valid06>(
			Valid06.class);
	private static final TestFile<Valid07> VALID07 = new TestFile<Valid07>(
			Valid07.class);
	private static final TestFile<SuccessArray> ARRAY = new TestFile<SuccessArray>(
			SuccessArray.class);
	private static final TestFile<SuccessExpr> EXPR = new TestFile<SuccessExpr>(
			SuccessExpr.class);
	private static final TestFile<SuccessIdFunctions> ID = new TestFile<SuccessIdFunctions>(
			SuccessIdFunctions.class);
	private static final TestFile<SuccessField> FIELD = new TestFile<SuccessField>(
			SuccessField.class);
	private static final TestFile<SuccessStaticField> STATIC_FIELD = new TestFile<SuccessStaticField>(
			SuccessStaticField.class);
	private static final TestFile<SuccessObject> OBJECT = new TestFile<SuccessObject>(
			SuccessObject.class);
	private static final TestFile<SuccessIfElse> IFELSE = new TestFile<SuccessIfElse>(
			SuccessIfElse.class);
	private static final TestFile<SuccessLoop> LOOP = new TestFile<SuccessLoop>(
			SuccessLoop.class);
	private static final TestFile<SuccessMethod> METHOD = new TestFile<SuccessMethod>(
			SuccessMethod.class);
	private static final TestFile<SuccessStaticMethod> STATIC_METHOD = new TestFile<SuccessStaticMethod>(
			SuccessStaticMethod.class);
	
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
	public final void test01Valid() {
		TestUtils.checkMethodStoreEquality(VALID01, CHECK_LEVELS);
	}

	@Test
	public final void test02Valid() {
		TestUtils.checkMethodStoreEquality(VALID02, CHECK_LEVELS);
	}

	@Test
	public final void test03Valid() {
		TestUtils.checkMethodStoreEquality(VALID03, CHECK_LEVELS);
	}

	@Test
	public final void test04Valid() {
		TestUtils.checkMethodStoreEquality(VALID04, CHECK_LEVELS);
	}

	@Test
	public final void test05Valid() {
		TestUtils.checkMethodStoreEquality(VALID05, CHECK_LEVELS);
	}

	@Test
	public final void test06Valid() {
		TestUtils.checkMethodStoreEquality(VALID06, CHECK_LEVELS);
	}

	@Test
	public final void test07Valid() {
		TestUtils.checkMethodStoreEquality(VALID07, CHECK_LEVELS);
	}
	
	@Test
	public final void test08Array() {
		TestUtils.checkMethodStoreEquality(ARRAY, CHECK_LEVELS);
	}
	
	@Test
	public final void test09Expr() {
		TestUtils.checkMethodStoreEquality(EXPR, CHECK_LEVELS);
	}
	
	@Test
	public final void test10IdFunction() {
		TestUtils.checkMethodStoreEquality(ID, CHECK_LEVELS);
	}
	
	@Test
	public final void test11Field() {
		TestUtils.checkMethodStoreEquality(FIELD, CHECK_LEVELS);
	}
	
	@Test
	public final void test12StaticField() {
		TestUtils.checkMethodStoreEquality(STATIC_FIELD, CHECK_LEVELS);
	}
	
	@Test
	public final void test13Object() {
		TestUtils.checkMethodStoreEquality(OBJECT, CHECK_LEVELS);
	}
	
	@Test
	public final void test14Method() {
		TestUtils.checkMethodStoreEquality(METHOD, CHECK_LEVELS);
	}
	
	@Test
	public final void test15StaticMethod() {
		TestUtils.checkMethodStoreEquality(STATIC_METHOD, CHECK_LEVELS);
	}
	
	@Test
	public final void test16Loop() {
		TestUtils.checkMethodStoreEquality(LOOP, CHECK_LEVELS);
	}
	
	@Test
	public final void test17IfElse() {
		TestUtils.checkMethodStoreEquality(IFELSE, CHECK_LEVELS);
	}

}

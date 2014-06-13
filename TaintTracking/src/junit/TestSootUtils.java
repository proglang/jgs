package junit;

import static org.junit.Assert.assertTrue;
import static soot.Modifier.PRIVATE;
import static soot.Modifier.PROTECTED;
import static soot.Modifier.PUBLIC;
import static soot.Modifier.STATIC;
import static soot.SootMethod.constructorName;
import static soot.SootMethod.staticInitializerName;
import static utils.AnalysisUtils.extractLineNumber;
import static utils.AnalysisUtils.generateFileName;
import static utils.AnalysisUtils.generateLevelFunctionName;
import static utils.AnalysisUtils.isClinitMethod;
import static utils.AnalysisUtils.isInitMethod;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import constraints.IConstraintComponent;

import security.ILevel;
import soot.ArrayType;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.Stmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JReturnVoidStmt;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import utils.AnalysisUtils;

/**
 * <h1>JUnit tests of class {@link AnalysisUtils}</h1>
 * 
 * JUnit tests which verify the accuracy of the methods of the class {@link AnalysisUtils}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class TestSootUtils {

	private static final RefType BOOL = RefType.v("boolean");
	private static final RefType INT = RefType.v("int");
	private static final ArrayType INT_ARR = ArrayType.v(INT, 1);
	private static final RefType LONG = RefType.v("long");
	private static SootClass sootClass1;
	private static SootClass sootClass2;
	private static SootField sootField1C1;
	private static SootField sootField1C2;
	private static SootField sootField2C1;
	private static SootField sootField2C2;
	private static SootField sootField3C1;
	private static SootField sootField3C2;
	private static SootField sootField4C1;
	private static SootField sootField4C2;
	private static SootField sootField5C1;
	private static SootField sootField5C2;
	private static SootMethod sootMethod1C1;
	private static SootMethod sootMethod1C2;
	private static SootMethod sootMethod2C1;
	private static SootMethod sootMethod2C2;
	private static SootMethod sootMethod3C1;
	private static SootMethod sootMethod3C2;
	private static SootMethod sootMethod4C1;
	private static SootMethod sootMethod4C2;
	private static SootMethod sootMethod5C1;
	private static SootMethod sootMethod5C2;
	private static SootMethod sootMethod6C1;
	private static SootMethod sootMethod6C2;
	private static SootMethod sootMethod7C1;
	private static SootMethod sootMethod7C2;
	private static final VoidType VOID = VoidType.v();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sootClass1 = new SootClass("TestC1", PUBLIC);
		VisibilityAnnotationTag visibilityAnnotationTagC1 = new VisibilityAnnotationTag(0);
		AnnotationTag annotationTagC1 = new AnnotationTag("Ltest/AnnotationString;", 1);
		AnnotationStringElem annotationStringElemC1 = new AnnotationStringElem("Test String Annotation", "s".charAt(0), "String Annotation");
		annotationTagC1.addElem(annotationStringElemC1);
		visibilityAnnotationTagC1.addAnnotation(annotationTagC1);
		sootClass1.addTag(visibilityAnnotationTagC1);
		sootField1C1 = new SootField("field1", INT, PUBLIC);
		sootClass1.addField(sootField1C1);
		sootField2C1 = new SootField("field2", BOOL, PROTECTED);
		sootClass1.addField(sootField2C1);
		sootField3C1 = new SootField("field3", LONG, PRIVATE);
		sootClass1.addField(sootField3C1);
		sootField4C1 = new SootField("field4", INT, STATIC ^ PUBLIC);
		sootClass1.addField(sootField4C1);
		sootField5C1 = new SootField("field5", INT, STATIC);
		sootClass1.addField(sootField5C1);
		sootField1C1.addTag(visibilityAnnotationTagC1);
		sootMethod1C1 = new SootMethod("method1", new ArrayList<Object>(), VOID, PUBLIC ^ STATIC);
		sootMethod1C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod1C1);
		sootMethod2C1 = new SootMethod("method2", new ArrayList<Object>(), BOOL, PUBLIC ^ STATIC);
		sootMethod2C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod2C1);
		sootMethod3C1 = new SootMethod("method3", new ArrayList<Object>(Arrays.asList(new RefType[] { INT })), BOOL, PUBLIC ^ STATIC);
		sootMethod3C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod3C1);
		sootMethod4C1 = new SootMethod("method4", new ArrayList<Object>(Arrays.asList(new RefType[] { INT })), INT, PUBLIC);
		sootMethod4C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod4C1);
		sootMethod5C1 = new SootMethod("method5", new ArrayList<Object>(Arrays.asList(new RefType[] { INT, BOOL })), BOOL, PROTECTED);
		sootMethod5C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod5C1);
		sootMethod6C1 = new SootMethod("method6", new ArrayList<Object>(Arrays.asList(new RefType[] { INT, BOOL })), INT, PRIVATE);
		sootMethod6C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod6C1);
		sootMethod7C1 = new SootMethod("method7", new ArrayList<Object>(), INT_ARR, PRIVATE);
		sootMethod7C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod7C1);
		sootMethod1C1.addTag(visibilityAnnotationTagC1);
		sootClass2 = new SootClass("test.TestC2", PUBLIC);
		VisibilityAnnotationTag visibilityAnnotationTagC2 = new VisibilityAnnotationTag(0);
		AnnotationTag annotationTagC2 = new AnnotationTag("Ltest/AnnotationStringArray;", 1);
		AnnotationStringElem annotationStringElem1C2 = new AnnotationStringElem("Test String Annotation 1", "s".charAt(0), "String Annotation");
		AnnotationStringElem annotationStringElem2C2 = new AnnotationStringElem("Test String Annotation 2", "s".charAt(0), "String Annotation");
		AnnotationArrayElem annotationArrayElemC2 = new AnnotationArrayElem(new ArrayList<AnnotationElem>(Arrays.asList(new AnnotationElem[] {
				annotationStringElem1C2, annotationStringElem2C2 })), "[".charAt(0), "String Array Annotation");
		annotationTagC2.addElem(annotationArrayElemC2);
		visibilityAnnotationTagC2.addAnnotation(annotationTagC2);
		sootClass2.addTag(visibilityAnnotationTagC2);
		sootField1C2 = new SootField("field1", INT, PUBLIC);
		sootClass2.addField(sootField1C2);
		sootField2C2 = new SootField("field2", BOOL, PROTECTED);
		sootClass2.addField(sootField2C2);
		sootField3C2 = new SootField("field3", LONG, PRIVATE);
		sootClass2.addField(sootField3C2);
		sootField4C2 = new SootField("field4", INT, PUBLIC ^ STATIC);
		sootClass2.addField(sootField4C2);
		sootField5C2 = new SootField("field5", INT, STATIC);
		sootClass2.addField(sootField5C2);
		sootField1C2.addTag(visibilityAnnotationTagC2);
		sootMethod1C2 = new SootMethod("method1", new ArrayList<Object>(), VOID, PUBLIC ^ STATIC);
		sootMethod1C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod1C2);
		sootMethod2C2 = new SootMethod("method2", new ArrayList<Object>(), BOOL, PUBLIC ^ STATIC);
		sootMethod2C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod2C2);
		sootMethod3C2 = new SootMethod("method3", new ArrayList<Object>(Arrays.asList(new RefType[] { INT })), BOOL, PUBLIC ^ STATIC);
		sootMethod3C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod3C2);
		sootMethod4C2 = new SootMethod("method4", new ArrayList<Object>(Arrays.asList(new RefType[] { INT })), INT, PUBLIC);
		sootMethod4C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod4C2);
		sootMethod5C2 = new SootMethod("method5", new ArrayList<Object>(Arrays.asList(new RefType[] { INT, BOOL })), BOOL, PROTECTED);
		sootMethod5C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod5C2);
		sootMethod6C2 = new SootMethod("method6", new ArrayList<Object>(Arrays.asList(new RefType[] { INT, BOOL })), INT, PRIVATE);
		sootMethod6C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod6C2);
		sootMethod7C2 = new SootMethod("method7", new ArrayList<Object>(), INT_ARR, PRIVATE);
		sootMethod7C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod7C2);
		sootMethod1C2.addTag(visibilityAnnotationTagC2);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public final void testExtractLn() {
		Stmt statement1 = new JInvokeStmt(new JStaticInvokeExpr(sootMethod1C1.makeRef(), new ArrayList<Object>()));
		Tag tag1 = new SourceLnPosTag(8, 8, 3, 4);
		statement1.addTag(tag1);
		Stmt statement2 = new JReturnVoidStmt();
		Tag tag2 = new SourceLnPosTag(532, 532, 3, 4);
		statement2.addTag(tag2);
		Stmt statement3 = new JReturnVoidStmt();
		assertTrue("Correct line number", extractLineNumber(statement1) == 8);
		assertTrue("Correct line number", extractLineNumber(statement2) == 532);
		assertTrue("No line number present", extractLineNumber(statement3) == 0);
	}

	@Test
	public final void testGenerateFileNameSootClass() {
		assertTrue("Correct file name for class1", generateFileName(sootClass1).equals("TestC1"));
		assertTrue("Correct file name for class2", generateFileName(sootClass2).equals("TestC2"));
	}

	@Test
	public final void testGenerateFileNameSootMethod() {
		assertTrue("Correct file name for method1 of class1.", generateFileName(sootMethod1C1).equals("TestC1"));
		assertTrue("Correct file name for method1 of class2.", generateFileName(sootMethod1C2).equals("TestC2"));
		assertTrue("Correct file name for method2 of class1.", generateFileName(sootMethod2C1).equals("TestC1"));
		assertTrue("Correct file name for method2 of class2.", generateFileName(sootMethod2C2).equals("TestC2"));
		assertTrue("Correct file name for method3 of class1.", generateFileName(sootMethod3C1).equals("TestC1"));
		assertTrue("Correct file name for method3 of class2.", generateFileName(sootMethod3C2).equals("TestC2"));
		assertTrue("Correct file name for method4 of class1.", generateFileName(sootMethod4C1).equals("TestC1"));
		assertTrue("Correct file name for method4 of class2.", generateFileName(sootMethod4C2).equals("TestC2"));
		assertTrue("Correct file name for method5 of class1.", generateFileName(sootMethod5C1).equals("TestC1"));
		assertTrue("Correct file name for method5 of class2.", generateFileName(sootMethod5C2).equals("TestC2"));
		assertTrue("Correct file name for method6 of class1.", generateFileName(sootMethod6C1).equals("TestC1"));
		assertTrue("Correct file name for method6 of class2.", generateFileName(sootMethod6C2).equals("TestC2"));
		assertTrue("Correct file name for method7 of class1.", generateFileName(sootMethod7C1).equals("TestC1"));
		assertTrue("Correct file name for method7 of class2.", generateFileName(sootMethod7C2).equals("TestC2"));
		assertTrue("Correct file name for unknown method of unknown class.", generateFileName(new SootClass("unknown")).equals("unknown"));
	}

	@Test
	public final void testGenerateLevelFunctionName() {
		ILevel l1 = new ILevel() {
			@Override
			public String getName() {
				return "high";
			}

			@Override
			public IConstraintComponent changeSignature(String signature) {
				return this;
			}
		};
		assertTrue("Correct level function name", generateLevelFunctionName(l1).equals("mkHigh"));
		ILevel l2 = new ILevel() {
			@Override
			public String getName() {
				return "low";
			}
			@Override
			public IConstraintComponent changeSignature(String signature) {
				return this;
			}
		};
		assertTrue("Correct level function name", generateLevelFunctionName(l2).equals("mkLow"));
	}

	@Test
	public final void testIsClinitMethod() {
		assertTrue("Method1 of class1 isn't a clinit.", !isClinitMethod(sootMethod1C1));
		assertTrue("Method1 of class2 isn't a clinit.", !isClinitMethod(sootMethod1C2));
		assertTrue("Method2 of class1 isn't a clinit.", !isClinitMethod(sootMethod2C1));
		assertTrue("Method2 of class2 isn't a clinit.", !isClinitMethod(sootMethod2C2));
		assertTrue("Method3 of class1 isn't a clinit.", !isClinitMethod(sootMethod3C1));
		assertTrue("Method3 of class2 isn't a clinit.", !isClinitMethod(sootMethod3C2));
		assertTrue("Method4 of class1 isn't a clinit.", !isClinitMethod(sootMethod4C1));
		assertTrue("Method4 of class2 isn't a clinit.", !isClinitMethod(sootMethod4C2));
		assertTrue("Method5 of class1 isn't a clinit.", !isClinitMethod(sootMethod5C1));
		assertTrue("Method5 of class2 isn't a clinit.", !isClinitMethod(sootMethod5C2));
		assertTrue("Method6 of class1 isn't a clinit.", !isClinitMethod(sootMethod6C1));
		assertTrue("Method6 of class2 isn't a clinit.", !isClinitMethod(sootMethod6C2));
		assertTrue("Method7 of class1 isn't a clinit.", !isClinitMethod(sootMethod7C1));
		assertTrue("Method7 of class2 isn't a clinit.", !isClinitMethod(sootMethod7C2));
		SootMethod clinit = new SootMethod(staticInitializerName, new ArrayList<Object>(), VOID, STATIC ^ PUBLIC);
		sootClass1.addMethod(clinit);
		clinit.setDeclaringClass(sootClass1);
		assertTrue("clinit of class1 is a clinit.", isClinitMethod(clinit));
	}

	@Test
	public final void testIsInitMethod() {
		assertTrue("Method1 of class1 isn't a init.", !isInitMethod(sootMethod1C1));
		assertTrue("Method1 of class2 isn't a init.", !isInitMethod(sootMethod1C2));
		assertTrue("Method2 of class1 isn't a init.", !isInitMethod(sootMethod2C1));
		assertTrue("Method2 of class2 isn't a init.", !isInitMethod(sootMethod2C2));
		assertTrue("Method3 of class1 isn't a init.", !isInitMethod(sootMethod3C1));
		assertTrue("Method3 of class2 isn't a init.", !isInitMethod(sootMethod3C2));
		assertTrue("Method4 of class1 isn't a init.", !isInitMethod(sootMethod4C1));
		assertTrue("Method4 of class2 isn't a init.", !isInitMethod(sootMethod4C2));
		assertTrue("Method5 of class1 isn't a init.", !isInitMethod(sootMethod5C1));
		assertTrue("Method5 of class2 isn't a init.", !isInitMethod(sootMethod5C2));
		assertTrue("Method6 of class1 isn't a init.", !isInitMethod(sootMethod6C1));
		assertTrue("Method6 of class2 isn't a init.", !isInitMethod(sootMethod6C2));
		assertTrue("Method7 of class1 isn't a init.", !isInitMethod(sootMethod7C1));
		assertTrue("Method7 of class2 isn't a init.", !isInitMethod(sootMethod7C2));
		SootMethod init = new SootMethod(constructorName, new ArrayList<Object>(Arrays.asList(new RefType[] { INT, BOOL })), VOID, PUBLIC);
		sootClass2.addMethod(init);
		init.setDeclaringClass(sootClass2);
		assertTrue("init of class2 is a init.", isInitMethod(init));
	}

}

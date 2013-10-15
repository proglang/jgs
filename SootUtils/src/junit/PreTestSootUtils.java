package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import soot.ArrayType;
import soot.Modifier;
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
import utils.SootUtils;

/**
 * <h1>JUnit tests of class {@link SootUtils}</h1>
 * 
 * JUnit tests which verify the accuracy of the methods of the class {@link SootUtils}. 
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class PreTestSootUtils {
	
	private static SootClass sootClass1;
	private static SootField sootField1C1;
	private static SootField sootField2C1;
	private static SootField sootField3C1;
	private static SootField sootField4C1;
	private static SootField sootField5C1;
	private static SootMethod sootMethod1C1;
	private static SootMethod sootMethod2C1;
	private static SootMethod sootMethod3C1;
	private static SootMethod sootMethod4C1;
	private static SootMethod sootMethod5C1;
	private static SootMethod sootMethod6C1;
	private static SootMethod sootMethod7C1;
	private static SootClass sootClass2;
	private static SootField sootField1C2;
	private static SootField sootField2C2;
	private static SootField sootField3C2;
	private static SootField sootField4C2;
	private static SootField sootField5C2;
	private static SootMethod sootMethod1C2;
	private static SootMethod sootMethod2C2;
	private static SootMethod sootMethod3C2;
	private static SootMethod sootMethod4C2;
	private static SootMethod sootMethod5C2;
	private static SootMethod sootMethod6C2;
	private static SootMethod sootMethod7C2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sootClass1 = new SootClass("TestC1", Modifier.PUBLIC);
		VisibilityAnnotationTag visibilityAnnotationTagC1 = new VisibilityAnnotationTag(0);
		AnnotationTag annotationTagC1 = new AnnotationTag("Ltest/AnnotationString;", 1);
		AnnotationStringElem annotationStringElemC1 = new AnnotationStringElem("Test String Annotation", "s".charAt(0), "String Annotation");
		annotationTagC1.addElem(annotationStringElemC1);
		visibilityAnnotationTagC1.addAnnotation(annotationTagC1);
		sootClass1.addTag(visibilityAnnotationTagC1);
		sootField1C1 = new SootField("field1", RefType.v("int"), Modifier.PUBLIC);
		sootClass1.addField(sootField1C1);
		sootField2C1 = new SootField("field2", RefType.v("boolean"), Modifier.PROTECTED);
		sootClass1.addField(sootField2C1);
		sootField3C1 = new SootField("field3", RefType.v("long"), Modifier.PRIVATE);
		sootClass1.addField(sootField3C1);
		sootField4C1 = new SootField("field4", RefType.v("int"), Modifier.STATIC ^ Modifier.PUBLIC);
		sootClass1.addField(sootField4C1);
		sootField5C1 = new SootField("field5", RefType.v("int"), Modifier.STATIC);
		sootClass1.addField(sootField5C1);
		sootField1C1.addTag(visibilityAnnotationTagC1);
		sootMethod1C1 = new SootMethod("method1", new ArrayList<Object>(), VoidType.v(), Modifier.PUBLIC ^ Modifier.STATIC);
		sootMethod1C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod1C1);
		sootMethod2C1 = new SootMethod("method2", new ArrayList<Object>(), RefType.v("boolean"), Modifier.PUBLIC ^ Modifier.STATIC);
		sootMethod2C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod2C1);
		sootMethod3C1 = new SootMethod("method3", new ArrayList<Object>(Arrays.asList(new RefType[] { RefType.v("int") })), RefType.v("boolean"), Modifier.PUBLIC ^ Modifier.STATIC);
		sootMethod3C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod3C1);
		sootMethod4C1 = new SootMethod("method4", new ArrayList<Object>(Arrays.asList(new RefType[] { RefType.v("int") })), RefType.v("int"), Modifier.PUBLIC);
		sootMethod4C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod4C1);
		sootMethod5C1 = new SootMethod("method5", new ArrayList<Object>(Arrays.asList(new RefType[] { RefType.v("int"), RefType.v("boolean") })), RefType.v("boolean"), Modifier.PROTECTED);
		sootMethod5C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod5C1);
		sootMethod6C1 = new SootMethod("method6", new ArrayList<Object>(Arrays.asList(new RefType[] { RefType.v("int"), RefType.v("boolean") })), RefType.v("int"), Modifier.PRIVATE);
		sootMethod6C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod6C1);
		sootMethod7C1 = new SootMethod("method7", new ArrayList<Object>(), ArrayType.v(RefType.v("int"), 1), Modifier.PRIVATE);
		sootMethod7C1.setDeclaringClass(sootClass1);
		sootClass1.addMethod(sootMethod7C1);
		sootMethod1C1.addTag(visibilityAnnotationTagC1);
		sootClass2 = new SootClass("test.TestC2", Modifier.PUBLIC);
		VisibilityAnnotationTag visibilityAnnotationTagC2 = new VisibilityAnnotationTag(0);
		AnnotationTag annotationTagC2 = new AnnotationTag("Ltest/AnnotationStringArray;", 1);
		AnnotationStringElem annotationStringElem1C2 = new AnnotationStringElem("Test String Annotation 1", "s".charAt(0), "String Annotation");
		AnnotationStringElem annotationStringElem2C2 = new AnnotationStringElem("Test String Annotation 2", "s".charAt(0), "String Annotation");
		AnnotationArrayElem annotationArrayElemC2 = new AnnotationArrayElem(new ArrayList<AnnotationElem>(Arrays.asList(new AnnotationElem[] { annotationStringElem1C2, annotationStringElem2C2})), "[".charAt(0), "String Array Annotation");
		annotationTagC2.addElem(annotationArrayElemC2);
		visibilityAnnotationTagC2.addAnnotation(annotationTagC2);
		sootClass2.addTag(visibilityAnnotationTagC2);
		sootField1C2 = new SootField("field1", RefType.v("int"), Modifier.PUBLIC);
		sootClass2.addField(sootField1C2);
		sootField2C2 = new SootField("field2", RefType.v("boolean"), Modifier.PROTECTED);
		sootClass2.addField(sootField2C2);
		sootField3C2 = new SootField("field3", RefType.v("long"), Modifier.PRIVATE);
		sootClass2.addField(sootField3C2);
		sootField4C2 = new SootField("field4", RefType.v("int"), Modifier.PUBLIC ^ Modifier.STATIC);
		sootClass2.addField(sootField4C2);
		sootField5C2 = new SootField("field5", RefType.v("int"), Modifier.STATIC);
		sootClass2.addField(sootField5C2);
		sootField1C2.addTag(visibilityAnnotationTagC2);
		sootMethod1C2 = new SootMethod("method1", new ArrayList<Object>(), VoidType.v(), Modifier.PUBLIC ^ Modifier.STATIC);
		sootMethod1C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod1C2);
		sootMethod2C2 = new SootMethod("method2", new ArrayList<Object>(), RefType.v("boolean"), Modifier.PUBLIC ^ Modifier.STATIC);
		sootMethod2C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod2C2);
		sootMethod3C2 = new SootMethod("method3", new ArrayList<Object>(Arrays.asList(new RefType[] { RefType.v("int") })), RefType.v("boolean"), Modifier.PUBLIC ^ Modifier.STATIC);
		sootMethod3C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod3C2);
		sootMethod4C2 = new SootMethod("method4", new ArrayList<Object>(Arrays.asList(new RefType[] { RefType.v("int") })), RefType.v("int"), Modifier.PUBLIC);
		sootMethod4C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod4C2);
		sootMethod5C2 = new SootMethod("method5", new ArrayList<Object>(Arrays.asList(new RefType[] { RefType.v("int"), RefType.v("boolean") })), RefType.v("boolean"), Modifier.PROTECTED);
		sootMethod5C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod5C2);
		sootMethod6C2 = new SootMethod("method6", new ArrayList<Object>(Arrays.asList(new RefType[] { RefType.v("int"), RefType.v("boolean") })), RefType.v("int"), Modifier.PRIVATE);
		sootMethod6C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod6C2);
		sootMethod7C2 = new SootMethod("method7", new ArrayList<Object>(), ArrayType.v(RefType.v("int"), 1), Modifier.PRIVATE);
		sootMethod7C2.setDeclaringClass(sootClass2);
		sootClass2.addMethod(sootMethod7C2);
		sootMethod1C2.addTag(visibilityAnnotationTagC2);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testExtractAnnotationString() {
		assertTrue("No annotation of type 'test' at class1.", SootUtils.extractAnnotationString("test", sootClass1.getTags()) == null);
		assertTrue("Correct annotation value of type 'Ltest/AnnotationString;' at class1.", SootUtils.extractAnnotationString("Ltest/AnnotationString;", sootClass1.getTags()).equals("Test String Annotation"));
		assertTrue("No annotation of type 'test' at field1 of class1.", SootUtils.extractAnnotationString("test", sootField1C1.getTags()) == null);
		assertTrue("Correct annotation value of type 'Ltest/AnnotationString;' at field1 of class1.", SootUtils.extractAnnotationString("Ltest/AnnotationString;", sootField1C1.getTags()).equals("Test String Annotation"));
		assertTrue("No annotation of type 'test' at method1 of class1.", SootUtils.extractAnnotationString("test", sootMethod1C1.getTags()) == null);
		assertTrue("Correct annotation value of type 'Ltest/AnnotationString;' at method1 of class1.", SootUtils.extractAnnotationString("Ltest/AnnotationString;", sootMethod1C1.getTags()).equals("Test String Annotation"));
	}

	@Test
	public final void testExtractAnnotationStringArray() {
		assertTrue("No annotation of type 'test' at class2.", SootUtils.extractAnnotationString("test", sootClass2.getTags()) == null);
		List<String> result1 = SootUtils.extractAnnotationStringArray("Ltest/AnnotationStringArray;", sootClass2.getTags());
		assertTrue("Correct annotation value of type 'Ltest/AnnotationStringArray;' at class2.", result1.containsAll(Arrays.asList(new String[] {"Test String Annotation 1", "Test String Annotation 2"})) && result1.size() == 2);
		assertTrue("No annotation of type 'test' at field1 of class2.", SootUtils.extractAnnotationString("test", sootField1C2.getTags()) == null);
		List<String> result2 = SootUtils.extractAnnotationStringArray("Ltest/AnnotationStringArray;", sootField1C2.getTags());
		assertTrue("Correct annotation value of type 'Ltest/AnnotationStringArray;' at field1 of class2.", result2.containsAll(Arrays.asList(new String[] {"Test String Annotation 1", "Test String Annotation 2"})) && result2.size() == 2);
		assertTrue("No annotation of type 'test' at method1 of class2.", SootUtils.extractAnnotationString("test", sootMethod1C2.getTags()) == null);
		List<String> result3 = SootUtils.extractAnnotationStringArray("Ltest/AnnotationStringArray;", sootMethod1C2.getTags());
		assertTrue("Correct annotation value of type 'Ltest/AnnotationStringArray;' at method1 of class2.", result3.containsAll(Arrays.asList(new String[] {"Test String Annotation 1", "Test String Annotation 2"})) && result3.size() == 2);
		
	}

	@Test
	public final void testExtractLn() {
		Stmt statement1 = new JInvokeStmt(new JStaticInvokeExpr(sootMethod1C1.makeRef(), new ArrayList<Object>()));
		Tag tag1 = new SourceLnPosTag(8, 8, 3, 4);
		statement1.addTag(tag1);
		Stmt statement2 = new JReturnVoidStmt();
		Tag tag2 = new SourceLnPosTag(532, 532, 3, 4);
		statement2.addTag(tag2);
		assertTrue("Correct line number", SootUtils.extractLn(statement1) == 8);
		assertTrue("Correct line number", SootUtils.extractLn(statement2) == 532);
	}

	@Test
	public final void testGenerateClassSignature() {
		assertTrue("Correct class signature of 'TestC1' without package.", SootUtils.generateClassSignature(sootClass1, false).equals("TestC1"));
		assertTrue("Correct class signature of 'TestC2' without package.", SootUtils.generateClassSignature(sootClass1, true).equals("TestC1"));
		assertTrue("Correct class signature of 'TestC1' with package.", SootUtils.generateClassSignature(sootClass2, false).equals("TestC2"));
		assertTrue("Correct class signature of 'TestC2' with package.", SootUtils.generateClassSignature(sootClass2, true).equals("test.TestC2"));
	}

	@Test
	public final void testGenerateFieldSignature() {
		assertTrue("Correct signature for field1 of class1 {false, false, false}", SootUtils.generateFieldSignature(sootField1C1, false, false, false).equals("TestC1.field1"));
		assertTrue("Correct signature for field1 of class1 {true, true, false}", SootUtils.generateFieldSignature(sootField1C1, true, true, false).equals("TestC1.field1 : int"));
		assertTrue("Correct signature for field1 of class1 {false, true, true}", SootUtils.generateFieldSignature(sootField1C1, true, true, true).equals("TestC1.field1 : int [+]"));
		assertTrue("Correct signature for field1 of class2 {false, false, false}", SootUtils.generateFieldSignature(sootField1C2, false, false, false).equals("TestC2.field1"));
		assertTrue("Correct signature for field1 of class2 {true, true, false}", SootUtils.generateFieldSignature(sootField1C2, true, true, false).equals("test.TestC2.field1 : int"));
		assertTrue("Correct signature for field1 of class2 {false, true, true}", SootUtils.generateFieldSignature(sootField1C2, false, true, true).equals("TestC2.field1 : int [+]"));
		assertTrue("Correct signature for field2 of class1 {false, false, true}", SootUtils.generateFieldSignature(sootField2C1, false, false, true).equals("TestC1.field2 [#]"));
		assertTrue("Correct signature for field2 of class1 {true, true, false}", SootUtils.generateFieldSignature(sootField2C1, true, true, false).equals("TestC1.field2 : boolean"));
		assertTrue("Correct signature for field2 of class1 {false, true, true}", SootUtils.generateFieldSignature(sootField2C1, false, true, true).equals("TestC1.field2 : boolean [#]"));
		assertTrue("Correct signature for field2 of class2 {false, false, true}", SootUtils.generateFieldSignature(sootField2C2, false, false, true).equals("TestC2.field2 [#]"));
		assertTrue("Correct signature for field2 of class2 {true, true, false}", SootUtils.generateFieldSignature(sootField2C2, true, true, false).equals("test.TestC2.field2 : boolean"));
		assertTrue("Correct signature for field2 of class2 {false, true, true}", SootUtils.generateFieldSignature(sootField2C2, false, true, true).equals("TestC2.field2 : boolean [#]"));
		assertTrue("Correct signature for field3 of class1 {false, false, false}", SootUtils.generateFieldSignature(sootField3C1, false, false, false).equals("TestC1.field3"));
		assertTrue("Correct signature for field3 of class1 {true, true, true}", SootUtils.generateFieldSignature(sootField3C1, true, true, true).equals("TestC1.field3 : long [-]"));
		assertTrue("Correct signature for field3 of class1 {false, false, true}", SootUtils.generateFieldSignature(sootField3C1, false, false, true).equals("TestC1.field3 [-]"));
		assertTrue("Correct signature for field3 of class2 {false, false, false}", SootUtils.generateFieldSignature(sootField3C2, false, false, false).equals("TestC2.field3"));
		assertTrue("Correct signature for field3 of class2 {true, true, true}", SootUtils.generateFieldSignature(sootField3C2, true, true, true).equals("test.TestC2.field3 : long [-]"));
		assertTrue("Correct signature for field3 of class2 {false, false, true}", SootUtils.generateFieldSignature(sootField3C2, false, false, true).equals("TestC2.field3 [-]"));
		assertTrue("Correct signature for field4 of class1 {false, true, true}", SootUtils.generateFieldSignature(sootField4C1, false, true, true).equals("TestC1.field4 : int [+]"));
		assertTrue("Correct signature for field4 of class2 {false, true, true}", SootUtils.generateFieldSignature(sootField4C2, false, true, true).equals("TestC2.field4 : int [+]"));
		assertTrue("Correct signature for field5 of class1 {false, true, true}", SootUtils.generateFieldSignature(sootField5C1, false, true, true).equals("TestC1.field5 : int [?]"));
		assertTrue("Correct signature for field5 of class2 {false, true, true}", SootUtils.generateFieldSignature(sootField5C2, false, true, true).equals("TestC2.field5 : int [?]"));
	}

	@Test
	public final void testGenerateFileNameSootClass() {
		assertTrue("Correct file name for class1", SootUtils.generateFileName(sootClass1).equals("TestC1"));
		assertTrue("Correct file name for class2", SootUtils.generateFileName(sootClass2).equals("TestC2"));
	}

	@Test
	public final void testGenerateFileNameSootMethod() {
		assertTrue("Correct file name for method1 of class1.", SootUtils.generateFileName(sootMethod1C1).equals("TestC1"));
		assertTrue("Correct file name for method1 of class2.", SootUtils.generateFileName(sootMethod1C2).equals("TestC2"));
		assertTrue("Correct file name for method2 of class1.", SootUtils.generateFileName(sootMethod2C1).equals("TestC1"));
		assertTrue("Correct file name for method2 of class2.", SootUtils.generateFileName(sootMethod2C2).equals("TestC2"));
		assertTrue("Correct file name for method3 of class1.", SootUtils.generateFileName(sootMethod3C1).equals("TestC1"));
		assertTrue("Correct file name for method3 of class2.", SootUtils.generateFileName(sootMethod3C2).equals("TestC2"));
		assertTrue("Correct file name for method4 of class1.", SootUtils.generateFileName(sootMethod4C1).equals("TestC1"));
		assertTrue("Correct file name for method4 of class2.", SootUtils.generateFileName(sootMethod4C2).equals("TestC2"));
		assertTrue("Correct file name for method5 of class1.", SootUtils.generateFileName(sootMethod5C1).equals("TestC1"));
		assertTrue("Correct file name for method5 of class2.", SootUtils.generateFileName(sootMethod5C2).equals("TestC2"));
		assertTrue("Correct file name for method6 of class1.", SootUtils.generateFileName(sootMethod6C1).equals("TestC1"));
		assertTrue("Correct file name for method6 of class2.", SootUtils.generateFileName(sootMethod6C2).equals("TestC2"));
		assertTrue("Correct file name for method7 of class1.", SootUtils.generateFileName(sootMethod7C1).equals("TestC1"));
		assertTrue("Correct file name for method7 of class2.", SootUtils.generateFileName(sootMethod7C2).equals("TestC2"));
		assertTrue("Correct file name for unknown method of unknown class.", SootUtils.generateFileName(new SootClass("unknown")).equals("unknown"));
	}

	@Test
	public final void testGenerateMethodSignature() {
		assertTrue("Correct method signature for method1 of class1 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod1C1, false, false, false).equals("TestC1.method1()"));
		assertTrue("Correct method signature for method1 of class2 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod1C2, false, false, false).equals("TestC2.method1()"));
		assertTrue("Correct method signature for method1 of class1 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod1C1, true, false, false).equals("TestC1.method1()"));
		assertTrue("Correct method signature for method1 of class2 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod1C2, true, false, false).equals("test.TestC2.method1()"));
		assertTrue("Correct method signature for method1 of class1 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod1C1, true, true, false).equals("TestC1.method1() : void"));
		assertTrue("Correct method signature for method1 of class2 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod1C2, true, true, false).equals("test.TestC2.method1() : void"));
		assertTrue("Correct method signature for method1 of class1 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod1C1, true, true, true).equals("TestC1.method1() : void [+]"));
		assertTrue("Correct method signature for method1 of class2 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod1C2, true, true, true).equals("test.TestC2.method1() : void [+]"));
		assertTrue("Correct method signature for method2 of class1 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod2C1, false, false, false).equals("TestC1.method2()"));
		assertTrue("Correct method signature for method2 of class2 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod2C2, false, false, false).equals("TestC2.method2()"));
		assertTrue("Correct method signature for method2 of class1 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod2C1, true, false, false).equals("TestC1.method2()"));
		assertTrue("Correct method signature for method2 of class2 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod2C2, true, false, false).equals("test.TestC2.method2()"));
		assertTrue("Correct method signature for method2 of class1 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod2C1, true, true, false).equals("TestC1.method2() : boolean"));
		assertTrue("Correct method signature for method2 of class2 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod2C2, true, true, false).equals("test.TestC2.method2() : boolean"));
		assertTrue("Correct method signature for method2 of class1 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod2C1, true, true, true).equals("TestC1.method2() : boolean [+]"));
		assertTrue("Correct method signature for method2 of class2 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod2C2, true, true, true).equals("test.TestC2.method2() : boolean [+]"));
		assertTrue("Correct method signature for method3 of class1 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod3C1, false, false, false).equals("TestC1.method3(arg0)"));
		assertTrue("Correct method signature for method3 of class2 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod3C2, false, false, false).equals("TestC2.method3(arg0)"));
		assertTrue("Correct method signature for method3 of class1 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod3C1, true, false, false).equals("TestC1.method3(arg0)"));
		assertTrue("Correct method signature for method3 of class2 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod3C2, true, false, false).equals("test.TestC2.method3(arg0)"));
		assertTrue("Correct method signature for method3 of class1 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod3C1, true, true, false).equals("TestC1.method3(arg0 : int) : boolean"));
		assertTrue("Correct method signature for method3 of class2 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod3C2, true, true, false).equals("test.TestC2.method3(arg0 : int) : boolean"));
		assertTrue("Correct method signature for method3 of class1 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod3C1, true, true, true).equals("TestC1.method3(arg0 : int) : boolean [+]"));
		assertTrue("Correct method signature for method3 of class2 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod3C2, true, true, true).equals("test.TestC2.method3(arg0 : int) : boolean [+]"));
		assertTrue("Correct method signature for method5 of class1 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod5C1, false, false, false).equals("TestC1.method5(arg0, arg1)"));
		assertTrue("Correct method signature for method5 of class2 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod5C2, false, false, false).equals("TestC2.method5(arg0, arg1)"));
		assertTrue("Correct method signature for method5 of class1 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod5C1, true, false, false).equals("TestC1.method5(arg0, arg1)"));
		assertTrue("Correct method signature for method5 of class2 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod5C2, true, false, false).equals("test.TestC2.method5(arg0, arg1)"));
		assertTrue("Correct method signature for method5 of class1 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod5C1, true, true, false).equals("TestC1.method5(arg0 : int, arg1 : boolean) : boolean"));
		assertTrue("Correct method signature for method5 of class2 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod5C2, true, true, false).equals("test.TestC2.method5(arg0 : int, arg1 : boolean) : boolean"));
		assertTrue("Correct method signature for method5 of class1 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod5C1, true, true, true).equals("TestC1.method5(arg0 : int, arg1 : boolean) : boolean [#]"));
		assertTrue("Correct method signature for method5 of class2 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod5C2, true, true, true).equals("test.TestC2.method5(arg0 : int, arg1 : boolean) : boolean [#]"));
		assertTrue("Correct method signature for method7 of class1 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod7C1, false, false, false).equals("TestC1.method7()"));
		assertTrue("Correct method signature for method7 of class2 {false, false, false}.", SootUtils.generateMethodSignature(sootMethod7C2, false, false, false).equals("TestC2.method7()"));
		assertTrue("Correct method signature for method7 of class1 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod7C1, true, false, false).equals("TestC1.method7()"));
		assertTrue("Correct method signature for method7 of class2 {true, false, false}.", SootUtils.generateMethodSignature(sootMethod7C2, true, false, false).equals("test.TestC2.method7()"));
		assertTrue("Correct method signature for method7 of class1 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod7C1, true, true, false).equals("TestC1.method7() : int[]"));
		assertTrue("Correct method signature for method7 of class2 {true, true, false}.", SootUtils.generateMethodSignature(sootMethod7C2, true, true, false).equals("test.TestC2.method7() : int[]"));
		assertTrue("Correct method signature for method7 of class1 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod7C1, true, true, true).equals("TestC1.method7() : int[] [-]"));
		assertTrue("Correct method signature for method7 of class2 {true, true, true}.", SootUtils.generateMethodSignature(sootMethod7C2, true, true, true).equals("test.TestC2.method7() : int[] [-]"));
	}

	@Test
	public final void testIsClinitMethod() {
		assertTrue("Method1 of class1 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod1C1));
		assertTrue("Method1 of class2 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod1C2));
		assertTrue("Method2 of class1 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod2C1));
		assertTrue("Method2 of class2 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod2C2));
		assertTrue("Method3 of class1 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod3C1));
		assertTrue("Method3 of class2 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod3C2));
		assertTrue("Method4 of class1 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod4C1));
		assertTrue("Method4 of class2 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod4C2));
		assertTrue("Method5 of class1 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod5C1));
		assertTrue("Method5 of class2 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod5C2));
		assertTrue("Method6 of class1 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod6C1));
		assertTrue("Method6 of class2 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod6C2));
		assertTrue("Method7 of class1 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod7C1));
		assertTrue("Method7 of class2 isn't a clinit.", ! SootUtils.isClinitMethod(sootMethod7C2));
		SootMethod clinit = new SootMethod(SootMethod.staticInitializerName, new ArrayList<Object>(), VoidType.v(), Modifier.STATIC ^ Modifier.PUBLIC);
		sootClass1.addMethod(clinit);
		clinit.setDeclaringClass(sootClass1);
		assertTrue("clinit of class1 is a clinit.", SootUtils.isClinitMethod(clinit));
	}

	@Test
	public final void testIsInitMethod() {
		assertTrue("Method1 of class1 isn't a init.", ! SootUtils.isInitMethod(sootMethod1C1));
		assertTrue("Method1 of class2 isn't a init.", ! SootUtils.isInitMethod(sootMethod1C2));
		assertTrue("Method2 of class1 isn't a init.", ! SootUtils.isInitMethod(sootMethod2C1));
		assertTrue("Method2 of class2 isn't a init.", ! SootUtils.isInitMethod(sootMethod2C2));
		assertTrue("Method3 of class1 isn't a init.", ! SootUtils.isInitMethod(sootMethod3C1));
		assertTrue("Method3 of class2 isn't a init.", ! SootUtils.isInitMethod(sootMethod3C2));
		assertTrue("Method4 of class1 isn't a init.", ! SootUtils.isInitMethod(sootMethod4C1));
		assertTrue("Method4 of class2 isn't a init.", ! SootUtils.isInitMethod(sootMethod4C2));
		assertTrue("Method5 of class1 isn't a init.", ! SootUtils.isInitMethod(sootMethod5C1));
		assertTrue("Method5 of class2 isn't a init.", ! SootUtils.isInitMethod(sootMethod5C2));
		assertTrue("Method6 of class1 isn't a init.", ! SootUtils.isInitMethod(sootMethod6C1));
		assertTrue("Method6 of class2 isn't a init.", ! SootUtils.isInitMethod(sootMethod6C2));
		assertTrue("Method7 of class1 isn't a init.", ! SootUtils.isInitMethod(sootMethod7C1));
		assertTrue("Method7 of class2 isn't a init.", ! SootUtils.isInitMethod(sootMethod7C2));
		SootMethod init = new SootMethod(SootMethod.constructorName, new ArrayList<Object>(Arrays.asList(new RefType [] { RefType.v("int"), RefType.v("boolean") })), VoidType.v(), Modifier.PUBLIC);
		sootClass2.addMethod(init);
		init.setDeclaringClass(sootClass2);
		assertTrue("init of class2 is a init.", SootUtils.isInitMethod(init));
	}

}

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

import security.Annotations;
import security.SecurityAnnotation;
import soot.Modifier;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;

/**
 * <h1>JUnit tests of class {@link SecurityAnnotation}</h1>
 * 
 * JUnit tests which verify the accuracy of the methods of the class {@link SecurityAnnotation}. 
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class PreTestSecurityAnnotation {
	
	private static SecurityAnnotation securityAnnotation;

	@BeforeClass
	public static final void setUpBeforeClass() throws Exception {
		List<String> levels = new ArrayList<String>();
		levels.add("high");
		levels.add("normal");
		levels.add("low");
		securityAnnotation = new SecurityAnnotation(levels);
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
	
	private final void clearListAndAddToList(List<String> list, String ... strings) {
		list.clear();
		for (String string : strings) {
			list.add(string);
		}
	}

	@Test
	public final void testGetEffectIdentifier() {
		assertTrue("Correct effect identifier for the annotation class 'WriteEffect'.", SecurityAnnotation.getEffectIdentifier(Annotations.WriteEffect.class).equals("EFFECT_ID#WRITEEFFECT"));
	}
	
	@Test 
	public final void testGetReturnSecurityLevelOfIdFunction() {
		SootClass sootC1 = new SootClass("security.SootSecurityLevel");
		SootMethod sootM1 = new SootMethod("lowId", Arrays.asList(new Type[] {RefType.v()}), RefType.v(), Modifier.PUBLIC | Modifier.STATIC);
		sootM1.setDeclaringClass(sootC1);
		sootC1.addMethod(sootM1);
		assertTrue("Id Function 'lowId' has correct return security level.", securityAnnotation.getReturnSecurityLevelOfIdFunction(sootM1).equals("low"));
		SootMethod sootM2 = new SootMethod("highId", Arrays.asList(new Type[] {RefType.v()}), RefType.v(), Modifier.PUBLIC | Modifier.STATIC);
		sootM2.setDeclaringClass(sootC1);
		sootC1.addMethod(sootM2);
		assertTrue("Id Function 'highId' has correct return security level.", securityAnnotation.getReturnSecurityLevelOfIdFunction(sootM2).equals("high"));
		SootMethod sootM3 = new SootMethod("helloDavId", Arrays.asList(new Type[] {RefType.v()}), RefType.v());
		sootM3.setDeclaringClass(sootC1);
		sootC1.addMethod(sootM3);
		assertTrue("Invalid id Function 'helloDavId' returns the weakest level as return security level.", securityAnnotation.getReturnSecurityLevelOfIdFunction(sootM3).equals("low"));
	}
	
	@Test 
	public final void testIsIdFunction() {
		SootClass sootC1 = new SootClass("security.SootSecurityLevel");
		SootMethod sootM1 = new SootMethod("lowId", Arrays.asList(new Type[] {RefType.v()}), RefType.v(), Modifier.PUBLIC | Modifier.STATIC);
		sootM1.setDeclaringClass(sootC1);
		sootC1.addMethod(sootM1);
		assertTrue("Method 'public static <T> T lowId(T)' is an id function.", securityAnnotation.isIdFunction(sootM1));
		SootMethod sootM2 = new SootMethod("normalId", Arrays.asList(new Type[] {RefType.v()}), RefType.v(), Modifier.PUBLIC);
		sootM2.setDeclaringClass(sootC1);
		sootC1.addMethod(sootM2);
		assertTrue("Method 'public <T> T normalId(T)' isn't an id function.", !securityAnnotation.isIdFunction(sootM2));
		SootMethod sootM3 = new SootMethod("lowId", Arrays.asList(new Type[] {RefType.v(), RefType.v()}), RefType.v(), Modifier.PUBLIC | Modifier.STATIC);
		sootM3.setDeclaringClass(sootC1);
		sootC1.addMethod(sootM3);
		assertTrue("Method 'public static <T> T lowId(T,T)' isn't an id function.", ! securityAnnotation.isIdFunction(sootM3));
		SootMethod sootM4 = new SootMethod("helloDavId", Arrays.asList(new Type[] {RefType.v()}), RefType.v(), Modifier.PUBLIC | Modifier.STATIC);
		sootM4.setDeclaringClass(sootC1);
		sootC1.addMethod(sootM4);
		assertTrue("Method 'private static <T> T highId(T)' isn't an id function.", ! securityAnnotation.isIdFunction(sootM4));
		SootMethod sootM5 = new SootMethod("highId", Arrays.asList(new Type[] {RefType.v()}), RefType.v(), Modifier.PRIVATE | Modifier.STATIC);
		sootM5.setDeclaringClass(sootC1);
		sootC1.addMethod(sootM5);
		assertTrue("Method 'private static <T> T highId(T)' isn't an id function.", ! securityAnnotation.isIdFunction(sootM5));
		SootClass sootC2 = new SootClass("security.SecurityLevel");
		SootMethod sootM6 = new SootMethod("highId", Arrays.asList(new Type[] {RefType.v()}), RefType.v(), Modifier.PUBLIC | Modifier.STATIC);
		sootM6.setDeclaringClass(sootC2);
		sootC2.addMethod(sootM6);
		assertTrue("Method 'public static <T> T highId(T)' in class 'SecurityLevel' isn't an id function.", ! securityAnnotation.isIdFunction(sootM6));
	}
	
	@Test 
	public final void testIsMethodOfSootSecurityLevelClass() {
		SootClass sootC1 = new SootClass("security.SootSecurityLevel");
		SootMethod sootM1 = new SootMethod("highId", Arrays.asList(new Type[] {RefType.v()}), RefType.v(), Modifier.PUBLIC | Modifier.STATIC);
		sootM1.setDeclaringClass(sootC1);
		sootC1.addMethod(sootM1);
		assertTrue("Method 'public static <T> T highId(T)' is a method of class 'SootSecurityLevel'.", securityAnnotation.isMethodOfSootSecurityLevelClass(sootM1));
		SootClass sootC2 = new SootClass("security.SecurityLevel");
		SootMethod sootM2 = new SootMethod("highId", Arrays.asList(new Type[] {RefType.v()}), RefType.v(), Modifier.PUBLIC | Modifier.STATIC);
		sootM2.setDeclaringClass(sootC2);
		sootC2.addMethod(sootM2);
		assertTrue("Method 'public static <T> T highId(T)' isn't a method of class 'SootSecurityLevel'.", ! securityAnnotation.isMethodOfSootSecurityLevelClass(sootM2));
	}
	
	@Test
	public final void testGetSootAnnotationTag() {
		assertTrue("Correct Soot annotation tag for the annotation class 'WriteEffect'.", SecurityAnnotation.getSootAnnotationTag(Annotations.WriteEffect.class).equals("Lsecurity/Annotations$WriteEffect;"));
		assertTrue("Correct Soot annotation tag for the annotation class 'ReturnSecurity'.", SecurityAnnotation.getSootAnnotationTag(Annotations.ReturnSecurity.class).equals("Lsecurity/Annotations$ReturnSecurity;"));
		assertTrue("Correct Soot annotation tag for the annotation class 'ParameterSecurity'.", SecurityAnnotation.getSootAnnotationTag(Annotations.ParameterSecurity.class).equals("Lsecurity/Annotations$ParameterSecurity;"));
		assertTrue("Correct Soot annotation tag for the annotation class 'FieldSecurity'.", SecurityAnnotation.getSootAnnotationTag(Annotations.FieldSecurity.class).equals("Lsecurity/Annotations$FieldSecurity;"));
	}
	
	@Test
	public final void testCheckValidityOfLevel() {
		assertTrue("Correct level 'high'", securityAnnotation.checkValidityOfLevel("high"));
		assertTrue("Correct level 'low'", securityAnnotation.checkValidityOfLevel("low"));
		assertTrue("Correct level 'normal'", securityAnnotation.checkValidityOfLevel("normal"));
		assertTrue("Incorrect level '*0'", ! securityAnnotation.checkValidityOfLevel("*0"));
		assertTrue("Incorrect level 'void'", ! securityAnnotation.checkValidityOfLevel("void"));
		assertTrue("Incorrect level 'hallo'", ! securityAnnotation.checkValidityOfLevel("hallo"));
		assertTrue("Incorrect level '*2'", ! securityAnnotation.checkValidityOfLevel("*2"));
	}

	@Test
	public final void testCheckValidityOfLevels() {
		List<String> list = new ArrayList<String>();
		clearListAndAddToList(list, "low", "normal", "high");
		assertTrue("Correct level list {'low', 'normal', 'high'}", securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "high", "normal", "low");
		assertTrue("Correct level list {'high', 'normal', 'low'}", securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "high", "high", "high");
		assertTrue("Correct level list {'high', 'high', 'high'}", securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "high");
		assertTrue("Correct level list {'high'}", securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "low");
		assertTrue("Correct level list {'low'}", securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "high", "void", "high");
		assertTrue("Incorrect level list {'high', 'void', 'high'}", ! securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "high", "low", "hallo");
		assertTrue("Incorrect level list {'high', 'low', 'hallo'}", ! securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "high", "hallo", "low");
		assertTrue("Incorrect level list {'high', 'hallo', 'low'}", ! securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "welt", "hallo", "low");
		assertTrue("Incorrect level list {'welt', 'hallo', 'low'}", ! securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "normal", "void", "low");
		assertTrue("Incorrect level list {'normal', '*0', 'low'}", ! securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "normal", "low", "*0");
		assertTrue("Incorrect level list {'normal', 'low', '*0'}", ! securityAnnotation.checkValidityOfLevels(list));
		clearListAndAddToList(list, "*0");
		assertTrue("Incorrect level list {'*0'}", ! securityAnnotation.checkValidityOfLevels(list));
	}

	@Test
	public final void testCheckValidityOfParameterLevels() {
		List<String> valid1 = new ArrayList<String>(Arrays.asList("low", "normal", "high"));
		assertTrue("Correct parameter list {'low', 'normal', 'high'}", securityAnnotation.checkValidityOfParameterLevels(valid1));
		List<String> valid2 = new ArrayList<String>(Arrays.asList("high", "normal", "low"));
		assertTrue("Correct parameter list {'high', 'normal', 'low'}", securityAnnotation.checkValidityOfParameterLevels(valid2));
		List<String> valid3 = new ArrayList<String>(Arrays.asList("high", "high", "high"));
		assertTrue("Correct parameter list {'high', 'high', 'high'}", securityAnnotation.checkValidityOfParameterLevels(valid3));
		List<String> valid4 = new ArrayList<String>(Arrays.asList("high"));
		assertTrue("Correct parameter list {'high'}", securityAnnotation.checkValidityOfParameterLevels(valid4));
		List<String> valid5 = new ArrayList<String>(Arrays.asList("low"));
		assertTrue("Correct parameter list {'low'}", securityAnnotation.checkValidityOfParameterLevels(valid5));
		List<String> valid6 = new ArrayList<String>(Arrays.asList("*0"));
		assertTrue("Correct parameter list {'*0'}", securityAnnotation.checkValidityOfParameterLevels(valid6));
		List<String> valid7 = new ArrayList<String>(Arrays.asList("*0", "*1"));
		assertTrue("Correct parameter list {'*0', '*1'}", securityAnnotation.checkValidityOfParameterLevels(valid7));
		List<String> valid8 = new ArrayList<String>(Arrays.asList("normal", "*0", "*1"));
		assertTrue("Correct parameter list {'normal', '*0', '*1'}", securityAnnotation.checkValidityOfParameterLevels(valid8));
		List<String> valid9 = new ArrayList<String>(Arrays.asList("*0", "normal", "*1"));
		assertTrue("Correct parameter list {'*1', 'normal', '*0'}", securityAnnotation.checkValidityOfParameterLevels(valid9));
		List<String> valid10 = new ArrayList<String>(Arrays.asList("*0", "*1", "high"));
		assertTrue("Correct parameter list {'*0', '*1', 'high'}", securityAnnotation.checkValidityOfParameterLevels(valid10));
		List<String> invalid1 = new ArrayList<String>(Arrays.asList("high", "void", "high"));
		assertTrue("Incorrect parameter list {'high', 'void', 'high'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid1));
		List<String> invalid2 = new ArrayList<String>(Arrays.asList("high", "low", "hallo"));
		assertTrue("Incorrect parameter list {'high', 'low', 'hallo'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid2));
		List<String> invalid3 = new ArrayList<String>(Arrays.asList("high", "hallo", "low"));
		assertTrue("Incorrect parameter list {'high', 'hallo', 'low'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid3));
		List<String> invalid4 = new ArrayList<String>(Arrays.asList("welt", "hallo", "low"));
		assertTrue("Incorrect parameter list {'welt', 'hallo', 'low'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid4));
		List<String> invalid5 = new ArrayList<String>(Arrays.asList("normal", "void", "low"));
		assertTrue("Incorrect parameter list {'normal', 'void', 'low'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid5));
		List<String> invalid6 = new ArrayList<String>(Arrays.asList("*1"));
		assertTrue("Incorrect parameter list {'*1'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid6));
		List<String> invalid7 = new ArrayList<String>(Arrays.asList("*0", "*2"));
		assertTrue("Incorrect parameter list {'*0', '*2'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid7));
		List<String> invalid8 = new ArrayList<String>(Arrays.asList("hallo", "*0", "*1"));
		assertTrue("Incorrect parameter list {'hallo', '*0', '*1'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid8));
		List<String> invalid9 = new ArrayList<String>(Arrays.asList("*0", "hallo", "*1"));
		assertTrue("Incorrect parameter list {'*0', 'hallo', '*1'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid9));
		List<String> invalid10 = new ArrayList<String>(Arrays.asList("*0", "*1", "hallo"));
		assertTrue("Incorrect parameter list {'*0', '*1', 'hallo'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid10));
		List<String> invalid11 = new ArrayList<String>(Arrays.asList("normal", "*0", "*2"));
		assertTrue("Incorrect parameter list {'normal', '*0', '*2'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid11));
		List<String> invalid12 = new ArrayList<String>(Arrays.asList("*2", "normal", "*0"));
		assertTrue("Incorrect parameter list {'*2', 'normal', '*0'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid12));
		List<String> invalid13 = new ArrayList<String>(Arrays.asList("*0", "*2", "hallo"));
		assertTrue("Incorrect parameter list {'*0', '*2', 'hallo'}", ! securityAnnotation.checkValidityOfParameterLevels(invalid13));
	}

	@Test
	public final void testGetAvailableLevels() {
		assertTrue("Correct amount of level items", securityAnnotation.getAvailableLevels().size() == 3);
		for (int i = 0; i < securityAnnotation.getAvailableLevels().size(); i++) {
			String[] result = new String[]{"high", "normal", "low"};
			assertTrue("Correct item at position " + i, securityAnnotation.getAvailableLevels().get(i).equals(result[i]));
		}
	}

	@Test
	public final void testGetInvalidLevels() {
		List<String> list = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		clearListAndAddToList(list, "low", "normal", "high");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for list {'low', 'normal', 'high'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list, "high", "normal", "low");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for list {'high', 'normal', 'low'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list, "high", "high", "high");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for list {'high', 'high', 'high'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list, "high");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for list {'high'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list, "low");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for list {'low'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list, "low", "hallo");
		clearListAndAddToList(result, "hallo");
		assertTrue("Incorrect level for list {'low', 'hallo'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list, "low", "hallo", "normal");
		clearListAndAddToList(result, "hallo");
		assertTrue("Incorrect level for list {'low', 'hallo'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list, "low", "hallo", "welt");
		clearListAndAddToList(result, "hallo", "welt");
		assertTrue("Incorrect level for list {'low', 'hallo', 'welt'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list, "!", "hallo", "welt");
		clearListAndAddToList(result, "!", "hallo", "welt");
		assertTrue("Incorrect level for list {'!', 'hallo', 'welt'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list, "normal", "*0", "high");
		clearListAndAddToList(result, "*0");
		assertTrue("Incorrect level for list {'normal', '*0', 'high'}", securityAnnotation.getInvalidLevels(list).equals(result));
		clearListAndAddToList(list);
		clearListAndAddToList(result);
		assertTrue("Incorrect level for list {}", securityAnnotation.getInvalidLevels(list).equals(result));
	}
	
	@Test
	public final void testGetInvalidParameterLevels() {
		List<String> list = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		clearListAndAddToList(list, "low", "normal", "high");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for parameter list {'low', 'normal', 'high'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "high", "normal", "low");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for parameter list {'high', 'normal', 'low'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "high", "high", "high");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for parameter list {'high', 'high', 'high'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "high");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for parameter list {'high'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "low");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for parameter list {'low'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "low", "hallo");
		clearListAndAddToList(result, "hallo");
		assertTrue("Incorrect level for parameter list {'low', 'hallo'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "low", "hallo", "normal");
		clearListAndAddToList(result, "hallo");
		assertTrue("Incorrect level for parameter list {'low', 'hallo'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "low", "hallo", "welt");
		clearListAndAddToList(result, "hallo", "welt");
		assertTrue("Incorrect level for parameter list {'low', 'hallo', 'welt'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "!", "hallo", "welt");
		clearListAndAddToList(result, "!", "hallo", "welt");
		assertTrue("Incorrect level for parameter list {'!', 'hallo', 'welt'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "normal", "*0", "high");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for parameter list {'normal', '*0', 'high'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list);
		clearListAndAddToList(result);
		assertTrue("Incorrect level for parameter list {}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "normal", "*0", "*1");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for parameter list {'normal', '*0', '*1'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "normal", "*1", "*0");
		clearListAndAddToList(result);
		assertTrue("Incorrect level for parameter list {'normal', '*1', '*0'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "normal", "*2", "*0");
		clearListAndAddToList(result, "*2");
		assertTrue("Incorrect level for parameter list {'normal', '*2', '*0'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
		clearListAndAddToList(list, "hallo", "*2", "*0");
		clearListAndAddToList(result, "hallo", "*2");
		assertTrue("Incorrect level for parameter list {'hallo', '*2', '*0'}", securityAnnotation.getInvalidParameterLevels(list).equals(result));
	}
	
	@Test
	public final void testGetMaxLevel1() {
		assertTrue("Correct maximal level of {'low', 'low'}", securityAnnotation.getMaxLevel("low", "low").equals("low"));
		assertTrue("Correct maximal level of {'high', 'low'}", securityAnnotation.getMaxLevel("high", "low").equals("high"));
		assertTrue("Correct maximal level of {'normal', 'low'}", securityAnnotation.getMaxLevel("normal", "low").equals("normal"));
		assertTrue("Correct maximal level of {'low', 'normal'}", securityAnnotation.getMaxLevel("low", "normal").equals("normal"));
		assertTrue("Correct maximal level of {'low', 'high'}", securityAnnotation.getMaxLevel("low", "high").equals("high"));
		assertTrue("Correct maximal level of {'normal', 'high'}", securityAnnotation.getMaxLevel("normal", "high").equals("high"));
		assertTrue("Correct maximal level of {'normal', 'normal'}", securityAnnotation.getMaxLevel("normal", "normal").equals("normal"));
		assertTrue("Correct maximal level of {'high', 'normal'}", securityAnnotation.getMaxLevel("high", "normal").equals("high"));
		assertTrue("Correct maximal level of {'high', 'high'}", securityAnnotation.getMaxLevel("high", "high").equals("high"));
	}
	
	@Test
	public final void testGetMinLevel() {
		assertTrue("Correct minimal level of {'low', 'low'}", securityAnnotation.getMinLevel("low", "low").equals("low"));
		assertTrue("Correct minimal level of {'high', 'low'}", securityAnnotation.getMinLevel("high", "low").equals("low"));
		assertTrue("Correct minimal level of {'normal', 'low'}", securityAnnotation.getMinLevel("normal", "low").equals("low"));
		assertTrue("Correct minimal level of {'low', 'normal'}", securityAnnotation.getMinLevel("low", "normal").equals("low"));
		assertTrue("Correct minimal level of {'low', 'high'}", securityAnnotation.getMinLevel("low", "high").equals("low"));
		assertTrue("Correct minimal level of {'normal', 'high'}", securityAnnotation.getMinLevel("normal", "high").equals("normal"));
		assertTrue("Correct minimal level of {'normal', 'normal'}", securityAnnotation.getMinLevel("normal", "normal").equals("normal"));
		assertTrue("Correct minimal level of {'high', 'normal'}", securityAnnotation.getMinLevel("high", "normal").equals("normal"));
		assertTrue("Correct minimal level of {'high', 'high'}", securityAnnotation.getMinLevel("high", "high").equals("high"));
	}

	@Test
	public final void testGetMaxLevel2() {
		List<String> levels = new ArrayList<String>();
		clearListAndAddToList(levels, "high", "normal", "low");
		assertTrue("Correct strongest item of high normal low", securityAnnotation.getMaxLevel(levels).equals("high"));
		clearListAndAddToList(levels, "low", "normal", "high");
		assertTrue("Correct strongest item of low normal high", securityAnnotation.getMaxLevel(levels).equals("high"));
		clearListAndAddToList(levels, "normal", "high", "low");
		assertTrue("Correct strongest item of normal high low", securityAnnotation.getMaxLevel(levels).equals("high"));
		clearListAndAddToList(levels, "normal", "low", "high");
		assertTrue("Correct strongest item of normal low high", securityAnnotation.getMaxLevel(levels).equals("high"));
		clearListAndAddToList(levels, "normal", "high", "high");
		assertTrue("Correct strongest item of normal high high", securityAnnotation.getMaxLevel(levels).equals("high"));
		clearListAndAddToList(levels, "normal", "normal", "low");
		assertTrue("Correct strongest item of normal normal high", securityAnnotation.getMaxLevel(levels).equals("normal"));
		clearListAndAddToList(levels, "low", "low", "low");
		assertTrue("Correct strongest item of low low low", securityAnnotation.getMaxLevel(levels).equals("low"));
		clearListAndAddToList(levels, "normal", "normal", "normal");
		assertTrue("Correct strongest item of normal normal normal", securityAnnotation.getMaxLevel(levels).equals("normal"));
		clearListAndAddToList(levels);
		assertTrue("Correct strongest item of {}", securityAnnotation.getMaxLevel(levels).equals("low"));
	}
	
	@Test
	public final void testGetMinLevel2() {
		List<String> levels = new ArrayList<String>();
		clearListAndAddToList(levels, "high", "normal", "low");
		assertTrue("Correct weakest item of high normal low", securityAnnotation.getMinLevel(levels).equals("low"));
		clearListAndAddToList(levels, "low", "normal", "high");
		assertTrue("Correct weakest item of low normal high", securityAnnotation.getMinLevel(levels).equals("low"));
		clearListAndAddToList(levels, "normal", "high", "low");
		assertTrue("Correct weakest item of normal high low", securityAnnotation.getMinLevel(levels).equals("low"));
		clearListAndAddToList(levels, "normal", "low", "high");
		assertTrue("Correct weakest item of normal low high", securityAnnotation.getMinLevel(levels).equals("low"));
		clearListAndAddToList(levels, "normal", "high", "high");
		assertTrue("Correct weakest item of normal high high", securityAnnotation.getMinLevel(levels).equals("normal"));
		clearListAndAddToList(levels, "normal", "normal", "low");
		assertTrue("Correct weakest item of normal normal high", securityAnnotation.getMinLevel(levels).equals("low"));
		clearListAndAddToList(levels, "low", "low", "low");
		assertTrue("Correct weakest item of low low low", securityAnnotation.getMinLevel(levels).equals("low"));
		clearListAndAddToList(levels, "normal", "normal", "normal");
		assertTrue("Correct weakest item of normal normal normal", securityAnnotation.getMinLevel(levels).equals("normal"));
		clearListAndAddToList(levels, "high");
		assertTrue("Correct weakest item of high", securityAnnotation.getMinLevel(levels).equals("high"));
		clearListAndAddToList(levels);
		assertTrue("Correct weakest item of {}", securityAnnotation.getMinLevel(levels).equals("low"));
	}
	
	@Test
	public final void testGetWeakestSecurityLevel() {
		assertTrue("Correct weakest item", securityAnnotation.getWeakestSecurityLevel().equals("low"));
		assertTrue("Incorrect weakest item", ! securityAnnotation.getWeakestSecurityLevel().equals("high"));
	}
	
	@Test
	public final void testIsWeakerOrEqualsThan() {
		assertTrue("Correct comparison of {'low', 'low'}", securityAnnotation.isWeakerOrEqualsThan("low", "low"));
		assertTrue("Correct comparison of {'low', 'normal'}", securityAnnotation.isWeakerOrEqualsThan("low", "normal"));
		assertTrue("Correct comparison of {'low', 'high'}", securityAnnotation.isWeakerOrEqualsThan("low", "high"));
		assertTrue("Correct comparison of {'normal', 'low'}", !securityAnnotation.isWeakerOrEqualsThan("normal", "low"));
		assertTrue("Correct comparison of {'high', 'low'}", !securityAnnotation.isWeakerOrEqualsThan("high", "low"));
		assertTrue("Correct comparison of {'normal', 'normal'}", securityAnnotation.isWeakerOrEqualsThan("normal", "normal"));
		assertTrue("Correct comparison of {'high', 'normal'}", !securityAnnotation.isWeakerOrEqualsThan("high", "normal"));
		assertTrue("Correct comparison of {'normal', 'high'}", securityAnnotation.isWeakerOrEqualsThan("normal", "high"));
		assertTrue("Correct comparison of {'high', 'high'}", securityAnnotation.isWeakerOrEqualsThan("high", "high"));
	}
	
}

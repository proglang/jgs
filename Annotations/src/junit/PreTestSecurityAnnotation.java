package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import security.SecurityAnnotation;

public class PreTestSecurityAnnotation {
	
	private static SecurityAnnotation securityAnnotation;

	@BeforeClass
	public static final void init() {
		List<String> levels = new ArrayList<String>();
		levels.add("high");
		levels.add("normal");
		levels.add("low");
		securityAnnotation = new SecurityAnnotation(levels);
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
	public final void testGetWeakestSecurityLevel() {
		assertTrue("Correct weakest item", securityAnnotation.getWeakestSecurityLevel().equals("low"));
	}

	@Test
	public final void testGetStrongestLevelOf() {
		List<String> levels = new ArrayList<String>();
		clearListAndAddToList(levels, "high", "normal", "low");
		assertTrue("Correct strongest item of high normal low", securityAnnotation.getStrongestLevelOf(levels).equals("high"));
		clearListAndAddToList(levels, "low", "normal", "high");
		assertTrue("Correct strongest item of low normal high", securityAnnotation.getStrongestLevelOf(levels).equals("high"));
		clearListAndAddToList(levels, "normal", "high", "low");
		assertTrue("Correct strongest item of normal high low", securityAnnotation.getStrongestLevelOf(levels).equals("high"));
		clearListAndAddToList(levels, "normal", "low", "high");
		assertTrue("Correct strongest item of normal low high", securityAnnotation.getStrongestLevelOf(levels).equals("high"));
		clearListAndAddToList(levels, "normal", "high", "high");
		assertTrue("Correct strongest item of normal high high", securityAnnotation.getStrongestLevelOf(levels).equals("high"));
		clearListAndAddToList(levels, "normal", "normal", "low");
		assertTrue("Correct strongest item of normal normal high", securityAnnotation.getStrongestLevelOf(levels).equals("normal"));
		clearListAndAddToList(levels, "low", "low", "low");
		assertTrue("Correct strongest item of low low low", securityAnnotation.getStrongestLevelOf(levels).equals("low"));
		clearListAndAddToList(levels, "normal", "normal", "normal");
		assertTrue("Correct strongest item of normal normal normal", securityAnnotation.getStrongestLevelOf(levels).equals("normal"));
	}
	
	private final void clearListAndAddToList(List<String> list, String ... strings) {
		list.clear();
		for (String string : strings) {
			list.add(string);
		}
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
	public final void testGetMaxLevel() {
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

}

package junit;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import utils.ExtendedSecurityLevelImplChecker;

public class LevelChecker {
	
	private static ExtendedSecurityLevelImplChecker extendedSecurityLevelImplChecker;

	@BeforeClass
	public static final void init() {
		try {
			extendedSecurityLevelImplChecker = ExtendedSecurityLevelImplChecker.getExtendedSecurityLevelImplChecker(null, false, false);
		} catch (Exception e) {
			// Wait for the failing reason
		}		
	}
	
	@Test
	public final void illegalLevel() {
		assertTrue("Level names contain valid signs and are not the same as the internal levels.", extendedSecurityLevelImplChecker.getIllegalLevelNames().isEmpty());
	}
	
	@Test
	public final void unavailableIdMethod() {
		assertTrue("Id-Method for every specified level is available.", extendedSecurityLevelImplChecker.getUnavailableIdFunctions().isEmpty());
	}
	
	@Test
	public final void invalidIdMethod() {
		assertTrue("For every Id-Method the corresponding annotation is available.", extendedSecurityLevelImplChecker.getInvalidIdFunctions().isEmpty());
	}
	
	@Test
	public final void unavailableAnnotation() {
		assertTrue("Id-Method for every specified level is valid.", extendedSecurityLevelImplChecker.getUnavailableIdFunctionAnnotation().isEmpty());
	}
	
	@Test
	public final void invalidAnnotation() {
		assertTrue("For every Id-Method the corresponding annotation is valid.", extendedSecurityLevelImplChecker.getInvalidIdFunctionAnnotation().isEmpty());
	}
	
	@Test
	public final void levelMethodAvailable() {
		assertTrue("Method which returns the ordered list of levels available.", extendedSecurityLevelImplChecker.isOrderedLevelMethodAvailable());
	}
	
	@Test
	public final void levelMethodValid() {
		assertTrue("Method which returns the ordered list of levels is valid (two or more levels).", extendedSecurityLevelImplChecker.isOrderedLevelMethodCorrect());
	}

}

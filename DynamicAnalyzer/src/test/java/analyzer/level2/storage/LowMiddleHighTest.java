package analyzer.level2.storage;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import analyzer.level2.SecurityLevel;

/**
 * The most fun-to-write unit test in the whole project
 * @author Nicolas Müller
 *
 */
public class LowMiddleHighTest {
	
	@Before
	 public void beforeMethod() {
	     org.junit.Assume.assumeTrue(SecurityLevel.secDomain instanceof LowMediumHigh);
	     // execute only if correct lattice is used
	 }
	
	@Test
	public void LessThanTest() {
			assertTrue(SecurityLevel.lt(LowMediumHigh.Level.LOW, LowMediumHigh.Level.MEDIUM));
			assertTrue(SecurityLevel.lt(LowMediumHigh.Level.LOW, LowMediumHigh.Level.HIGH));
			assertTrue(SecurityLevel.lt(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.HIGH));
			
			assertTrue(!SecurityLevel.lt(LowMediumHigh.Level.LOW, LowMediumHigh.Level.LOW));
			assertTrue(!SecurityLevel.lt(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.MEDIUM));
			assertTrue(!SecurityLevel.lt(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.HIGH));
			
			assertTrue(!SecurityLevel.lt(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.LOW));
			assertTrue(!SecurityLevel.lt(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.MEDIUM));
			assertTrue(!SecurityLevel.lt(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.LOW));
	}
	
	@Test
	public void LessThanEqualTest() {
		assertTrue(SecurityLevel.le(LowMediumHigh.Level.LOW, LowMediumHigh.Level.LOW));
		assertTrue(SecurityLevel.le(LowMediumHigh.Level.LOW, LowMediumHigh.Level.MEDIUM));
		assertTrue(SecurityLevel.le(LowMediumHigh.Level.LOW, LowMediumHigh.Level.HIGH));
		assertTrue(SecurityLevel.le(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.MEDIUM));
		assertTrue(SecurityLevel.le(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.HIGH));
		assertTrue(SecurityLevel.le(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.HIGH));
	}
	
	@Test
	public void GreatestLowerBound(){
		assertTrue(SecurityLevel.glb(LowMediumHigh.Level.LOW, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.LOW));
		assertTrue(SecurityLevel.glb(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.LOW));
		assertTrue(SecurityLevel.glb(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.LOW));
		
		assertTrue(SecurityLevel.glb(LowMediumHigh.Level.LOW, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.LOW));
		assertTrue(SecurityLevel.glb(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(SecurityLevel.glb(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.MEDIUM));
		
		assertTrue(SecurityLevel.glb(LowMediumHigh.Level.LOW, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.LOW));
		assertTrue(SecurityLevel.glb(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(SecurityLevel.glb(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.HIGH));
	}
	
	@Test
	public void LeastUpperBound() {
		assertTrue(SecurityLevel.lub(LowMediumHigh.Level.LOW, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.LOW));
		assertTrue(SecurityLevel.lub(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(SecurityLevel.lub(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.HIGH));

		assertTrue(SecurityLevel.lub(LowMediumHigh.Level.LOW, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(SecurityLevel.lub(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(SecurityLevel.lub(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.HIGH));

		assertTrue(SecurityLevel.lub(LowMediumHigh.Level.LOW, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.HIGH));
		assertTrue(SecurityLevel.lub(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.HIGH));
		assertTrue(SecurityLevel.lub(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.HIGH));
	}
	
	@Test
	public void parserTest() {
		assertTrue(SecurityLevel.readLevel("HIGH").equals(LowMediumHigh.Level.HIGH));
		assertTrue(SecurityLevel.readLevel("MEDIUM").equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(SecurityLevel.readLevel("LOW").equals(LowMediumHigh.Level.LOW));
	}

}

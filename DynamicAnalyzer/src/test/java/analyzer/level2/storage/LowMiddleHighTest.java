package analyzer.level2.storage;

import static org.junit.Assert.assertTrue;

import analyzer.level2.CurrentSecurityDomain;
import org.junit.Before;
import org.junit.Test;

/**
 * The most fun-to-write unit test in the whole project
 * @author Nicolas Müller
 *
 */
public class LowMiddleHighTest {
	
	/*@Before
	 public void beforeMethod() {
	     org.junit.Assume.assumeTrue(CurrentSecurityDomain.INSTANCE instanceof LowMediumHigh);
	     // execute only if correct lattice is used
	 }*/
	
	@Test
	public void LessThanTest() {
			assertTrue(CurrentSecurityDomain.lt(LowMediumHigh.Level.LOW, LowMediumHigh.Level.MEDIUM));
			assertTrue(CurrentSecurityDomain.lt(LowMediumHigh.Level.LOW, LowMediumHigh.Level.HIGH));
			assertTrue(CurrentSecurityDomain.lt(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.HIGH));
			
			assertTrue(!CurrentSecurityDomain.lt(LowMediumHigh.Level.LOW, LowMediumHigh.Level.LOW));
			assertTrue(!CurrentSecurityDomain.lt(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.MEDIUM));
			assertTrue(!CurrentSecurityDomain.lt(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.HIGH));
			
			assertTrue(!CurrentSecurityDomain.lt(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.LOW));
			assertTrue(!CurrentSecurityDomain.lt(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.MEDIUM));
			assertTrue(!CurrentSecurityDomain.lt(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.LOW));
	}
	
	@Test
	public void LessThanEqualTest() {
		assertTrue(CurrentSecurityDomain.le(LowMediumHigh.Level.LOW, LowMediumHigh.Level.LOW));
		assertTrue(CurrentSecurityDomain.le(LowMediumHigh.Level.LOW, LowMediumHigh.Level.MEDIUM));
		assertTrue(CurrentSecurityDomain.le(LowMediumHigh.Level.LOW, LowMediumHigh.Level.HIGH));
		assertTrue(CurrentSecurityDomain.le(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.MEDIUM));
		assertTrue(CurrentSecurityDomain.le(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.HIGH));
		assertTrue(CurrentSecurityDomain.le(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.HIGH));
	}
	
	@Test
	public void GreatestLowerBound(){
		assertTrue(CurrentSecurityDomain.glb(LowMediumHigh.Level.LOW, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.LOW));
		assertTrue(CurrentSecurityDomain.glb(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.LOW));
		assertTrue(CurrentSecurityDomain.glb(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.LOW));
		
		assertTrue(CurrentSecurityDomain.glb(LowMediumHigh.Level.LOW, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.LOW));
		assertTrue(CurrentSecurityDomain.glb(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(CurrentSecurityDomain.glb(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.MEDIUM));
		
		assertTrue(CurrentSecurityDomain.glb(LowMediumHigh.Level.LOW, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.LOW));
		assertTrue(CurrentSecurityDomain.glb(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(CurrentSecurityDomain.glb(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.HIGH));
	}
	
	@Test
	public void LeastUpperBound() {
		assertTrue(CurrentSecurityDomain.lub(LowMediumHigh.Level.LOW, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.LOW));
		assertTrue(CurrentSecurityDomain.lub(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(CurrentSecurityDomain.lub(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.LOW).equals(LowMediumHigh.Level.HIGH));

		assertTrue(CurrentSecurityDomain.lub(LowMediumHigh.Level.LOW, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(CurrentSecurityDomain.lub(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(CurrentSecurityDomain.lub(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.MEDIUM).equals(LowMediumHigh.Level.HIGH));

		assertTrue(CurrentSecurityDomain.lub(LowMediumHigh.Level.LOW, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.HIGH));
		assertTrue(CurrentSecurityDomain.lub(LowMediumHigh.Level.MEDIUM, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.HIGH));
		assertTrue(CurrentSecurityDomain.lub(LowMediumHigh.Level.HIGH, LowMediumHigh.Level.HIGH).equals(LowMediumHigh.Level.HIGH));
	}
	
	@Test
	public void parserTest() {
		assertTrue(CurrentSecurityDomain.readLevel("HIGH").equals(LowMediumHigh.Level.HIGH));
		assertTrue(CurrentSecurityDomain.readLevel("MEDIUM").equals(LowMediumHigh.Level.MEDIUM));
		assertTrue(CurrentSecurityDomain.readLevel("LOW").equals(LowMediumHigh.Level.LOW));
	}

}

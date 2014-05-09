package junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import security.ALevel;
import security.ILevel;
import constraints.ConstraintProgramCounterRef;
import constraints.ConstraintParameterRef;
import constraints.ConstraintReturnRef;
import constraints.IConstraintComponent;

public class TestComponent {
	
	private static class SLevel extends ALevel {
		
		private final String level;
		
		private SLevel(String level) {
			this.level = level;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((level == null) ? 0 : level.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			SLevel other = (SLevel) obj;
			if (level == null) {
				if (other.level != null) return false;
			} else if (!level.equals(other.level)) return false;
			return true;
		}

		@Override
		public String getName() {
			return level;
		}

		@Override
		public String toString() {
			return "SLevel: " + level;
		}
		
	}

	@Test
	public final void testEqualsLevel() {
		ILevel level1 = new SLevel("low");
		ILevel level2 = new SLevel("high");
		IConstraintComponent level3 = new SLevel("low");
		assertTrue("Same levels", level1.equals(level1));
		assertTrue("Equal levels", level1.equals(new SLevel("low")));
		assertTrue("Equal levels", level1.equals(level3));
		assertTrue("Equal levels", new SLevel("low").equals(level1));
		assertFalse("Not equal levels", level1.equals(level2));
		assertFalse("Not equal levels", level2.equals(level1));
		assertFalse("Not equal levels", level1.equals(new SLevel("high")));
		assertFalse("Not equal levels", new SLevel("high").equals(level1));
		assertFalse("Not equal levels", level1.equals(new ConstraintParameterRef(0, "test")));
	}
	
	@Test
	public final void testEqualsParamRef() {
		ConstraintParameterRef p1 = new ConstraintParameterRef(0, "test");
		ConstraintParameterRef p2 = new ConstraintParameterRef(1, "test");
		ConstraintParameterRef p3 = new ConstraintParameterRef(1, "test-b");
		ConstraintParameterRef p4 = new ConstraintParameterRef(0, "test-b");
		IConstraintComponent c1 = new ConstraintParameterRef(0, "test");
		IConstraintComponent c2 = new ConstraintParameterRef(1, "test");
		IConstraintComponent c3 = new ConstraintParameterRef(1, "test-b");
		IConstraintComponent c4 = new ConstraintParameterRef(0, "test-b");
		assertTrue("Same param refs", p1.equals(p1));
		assertTrue("Equal param refs", p1.equals(new ConstraintParameterRef(0, "test")));
		assertTrue("Equal param refs", new ConstraintParameterRef(0, "test").equals(p1));
		assertFalse("Not equal param refs", p1.equals(p2));
		assertFalse("Not equal param refs", p2.equals(p1));
		assertFalse("Not equal param refs", p1.equals(p3));
		assertFalse("Not equal param refs", p3.equals(p1));
		assertFalse("Not equal param refs", p1.equals(p4));
		assertFalse("Not equal param refs", p4.equals(p1));
		assertTrue("Same param refs", p1.equals(c1));
		assertFalse("Not equal param refs", p1.equals(c2));
		assertFalse("Not equal param refs", c2.equals(p1));
		assertFalse("Not equal param refs", p1.equals(c3));
		assertFalse("Not equal param refs", c3.equals(p1));
		assertFalse("Not equal param refs", p1.equals(c4));
		assertFalse("Not equal param refs", c4.equals(p1));
		assertFalse("Not equal param refs", p1.equals(new ConstraintParameterRef(0, "testb")));
		assertFalse("Not equal param refs", new ConstraintParameterRef(1, "test").equals(p1));
	}

	@Test
	public final void testEqualsReturnRef() {
		ConstraintReturnRef r1 = new ConstraintReturnRef("test");
		ConstraintReturnRef r2 = new ConstraintReturnRef("test-b");
		IConstraintComponent c1 = new ConstraintReturnRef("test");
		IConstraintComponent c2 = new ConstraintReturnRef("test-b");
		assertTrue("Same return refs", r1.equals(r1));
		assertTrue("Equal return refs", r1.equals(new ConstraintReturnRef("test")));
		assertTrue("Equal return refs", new ConstraintReturnRef("test").equals(r1));
		assertFalse("Not equal return refs", r1.equals(r2));
		assertFalse("Not equal return refs", r2.equals(r1));
		assertTrue("Same return refs", r1.equals(c1));
		assertFalse("Not equal return refs", r1.equals(c2));
		assertFalse("Not equal return refs", c2.equals(r1));
		assertFalse("Not equal return refs", r1.equals(new ConstraintReturnRef("testb")));
		assertFalse("Not equal return refs", new ConstraintReturnRef("testb").equals(r1));
	}
	
	@Test
	public final void testEqualsPCRef() {
		ConstraintProgramCounterRef pc1 = new ConstraintProgramCounterRef("a");
		IConstraintComponent c1 = new ConstraintProgramCounterRef("a");
		assertTrue("Same pc refs", pc1.equals(pc1));
		assertTrue("Equal pc refs", pc1.equals(new ConstraintProgramCounterRef("a")));
		assertTrue("Equal pc refs", new ConstraintProgramCounterRef("a").equals(pc1));
		assertTrue("Same pc refs", pc1.equals(c1));
	}
	
}

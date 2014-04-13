package junit;

import static org.junit.Assert.*;

import org.junit.Test;

import security.ILevel;

import constraints.AConstraint;
import constraints.ConstraintParameterRef;
import constraints.ConstraintReturnRef;
import constraints.LEQConstraint;

public class TestConstraint {
	
	private ILevel level1 = new ILevel() {
		
		@Override
		public String getName() {
			return "high";
		}
	};
	private ILevel level2 = new ILevel() {
		
		@Override
		public String getName() {
			return "low";
		}
	};
	private ConstraintReturnRef returnRef = new ConstraintReturnRef();
	private ConstraintParameterRef paramRef1 = new ConstraintParameterRef(0);
	private ConstraintParameterRef paramRef2 = new ConstraintParameterRef(1);

	@Test
	public final void testGetLhs() {
		LEQConstraint cons1 = new LEQConstraint(level1, returnRef);
		assertTrue("Get left handside of the constraint [level1].", cons1.getLhs().equals(level1));
		assertTrue("Get not the right handside of the constraint [return].", ! cons1.getLhs().equals(returnRef));
		LEQConstraint cons2 = new LEQConstraint(returnRef, paramRef1);
		assertTrue("Get left handside of the constraint [return].", cons2.getLhs().equals(returnRef));
		assertTrue("Get not the right handside of the constraint [param1].", ! cons2.getLhs().equals(paramRef1));
		LEQConstraint cons3 = new LEQConstraint(paramRef1, level1);
		assertTrue("Get left handside of the constraint [param1].", cons3.getLhs().equals(paramRef1));
		assertTrue("Get not the right handside of the constraint [level1].", ! cons3.getLhs().equals(level1));
	}

	@Test
	public final void testGetRhs() {
		LEQConstraint cons1 = new LEQConstraint(returnRef, level1);
		assertTrue("Get right handside of the constraint [level1].", cons1.getRhs().equals(level1));
		assertTrue("Get not the left handside of the constraint [return].", ! cons1.getRhs().equals(returnRef));
		LEQConstraint cons2 = new LEQConstraint(paramRef1, returnRef);
		assertTrue("Get right handside of the constraint [return].", cons2.getRhs().equals(returnRef));
		assertTrue("Get not the left handside of the constraint [param1].", ! cons2.getRhs().equals(paramRef1));
		LEQConstraint cons3 = new LEQConstraint(level1, paramRef1);
		assertTrue("Get right handside of the constraint [param1].", cons3.getRhs().equals(paramRef1));
		assertTrue("Get not the left handside of the constraint [level1].", ! cons3.getRhs().equals(level1));
	}

	@Test
	public final void testContainsReturnRef() {
		LEQConstraint cons1 = new LEQConstraint(returnRef, level1);
		LEQConstraint cons2 = new LEQConstraint(paramRef1, returnRef);
		LEQConstraint cons3 = new LEQConstraint(level1, paramRef1);
		LEQConstraint cons4 = new LEQConstraint(returnRef, returnRef);
		LEQConstraint cons5 = new LEQConstraint(level1, level2);
		LEQConstraint cons6 = new LEQConstraint(paramRef1, paramRef2);
		assertTrue("Constraint contains a return reference [cons1]", cons1.containsReturnRef());
		assertTrue("Constraint contains a return reference [cons2]", cons2.containsReturnRef());
		assertTrue("Constraint does not contain a return reference [cons3]", ! cons3.containsReturnRef());
		assertTrue("Constraint contains a return reference [cons4]", cons4.containsReturnRef());
		assertTrue("Constraint does not contain a return reference [cons5]", ! cons5.containsReturnRef());
		assertTrue("Constraint does not contain a return reference [cons6]", ! cons6.containsReturnRef());
	}

	@Test
	public final void testContainsParameterRef() {
		LEQConstraint cons1 = new LEQConstraint(returnRef, level1);
		LEQConstraint cons2 = new LEQConstraint(paramRef1, returnRef);
		LEQConstraint cons3 = new LEQConstraint(level1, paramRef1);
		LEQConstraint cons4 = new LEQConstraint(returnRef, returnRef);
		LEQConstraint cons5 = new LEQConstraint(level1, level2);
		LEQConstraint cons6 = new LEQConstraint(paramRef1, paramRef2);
		assertTrue("Constraint does not contain a parameter reference [cons1]", ! cons1.containsParameterRef());
		assertTrue("Constraint contains a parameter reference [cons2]", cons2.containsParameterRef());
		assertTrue("Constraint contains a parameter reference [cons3]", cons3.containsParameterRef());
		assertTrue("Constraint does not contain a parameter reference [cons4]", ! cons4.containsParameterRef());
		assertTrue("Constraint does not contain a parameter reference [cons5]", ! cons5.containsParameterRef());
		assertTrue("Constraint contains a parameter reference [cons6]", cons6.containsParameterRef());
	}

	@Test
	public final void testContainsLevel() {
		LEQConstraint cons1 = new LEQConstraint(returnRef, level1);
		LEQConstraint cons2 = new LEQConstraint(paramRef1, returnRef);
		LEQConstraint cons3 = new LEQConstraint(level1, paramRef1);
		LEQConstraint cons4 = new LEQConstraint(returnRef, returnRef);
		LEQConstraint cons5 = new LEQConstraint(level1, level2);
		LEQConstraint cons6 = new LEQConstraint(paramRef1, paramRef2);
		assertTrue("Constraint contains a security level [cons1]", cons1.containsLevel());
		assertTrue("Constraint does not contain a security level [cons2]", ! cons2.containsLevel());
		assertTrue("Constraint contains a security level [cons3]", cons3.containsLevel());
		assertTrue("Constraint does not contain a security level [cons4]", ! cons4.containsLevel());
		assertTrue("Constraint contains a security level [cons5]", cons5.containsLevel());
		assertTrue("Constraint does not contain a security level [cons6]", ! cons6.containsLevel());
	}

	@Test
	public final void testIsParameterRef() {
		assertTrue("Component is a parameter reference", AConstraint.isParameterRef(paramRef1));
		assertTrue("Component is a parameter reference", AConstraint.isParameterRef(paramRef2));
		assertTrue("Component is not a parameter reference",! AConstraint.isParameterRef(level1));
		assertTrue("Component is not a parameter reference", !AConstraint.isParameterRef(level2));
		assertTrue("Component is not a parameter reference", !AConstraint.isParameterRef(returnRef));
	}

	@Test
	public final void testIsReturnRef() {
		assertTrue("Component is a return reference", AConstraint.isReturnRef(returnRef));
		assertTrue("Component is not a return reference", !AConstraint.isReturnRef(level1));
		assertTrue("Component is not a return reference", !AConstraint.isReturnRef(level2));
		assertTrue("Component is not a return reference", !AConstraint.isReturnRef(paramRef1));
		assertTrue("Component is not a return reference", !AConstraint.isReturnRef(paramRef2));
	}

	@Test
	public final void testIsLevel() {
		assertTrue("Component is a security level", AConstraint.isLevel(level1));
		assertTrue("Component is a security level", AConstraint.isLevel(level2));
		assertTrue("Component is not a security level", !AConstraint.isLevel(paramRef1));
		assertTrue("Component is not a security level", !AConstraint.isLevel(paramRef2));
		assertTrue("Component is not a security level", !AConstraint.isLevel(returnRef));
	}

}

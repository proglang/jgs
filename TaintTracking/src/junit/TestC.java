package junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import security.ILevel;
import constraints.ConstraintParameterRef;
import constraints.ConstraintReturnRef;
import constraints.LEQConstraint;

public class TestC {

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
	private ConstraintParameterRef paramRef1 = new ConstraintParameterRef(0, "");
	private ConstraintParameterRef paramRef2 = new ConstraintParameterRef(1,"");
	private ConstraintReturnRef returnRef = new ConstraintReturnRef("");

//	@Test
//	public final void testContainsLevel() {
//		LEQConstraint cons1 = new LEQConstraint(returnRef, level1);
//		LEQConstraint cons2 = new LEQConstraint(paramRef1, returnRef);
//		LEQConstraint cons3 = new LEQConstraint(level1, paramRef1);
//		LEQConstraint cons4 = new LEQConstraint(returnRef, returnRef);
//		LEQConstraint cons5 = new LEQConstraint(level1, level2);
//		LEQConstraint cons6 = new LEQConstraint(paramRef1, paramRef2);
//		assertTrue("Constraint contains a security level [cons1]", cons1.containsLevel());
//		assertFalse("Constraint does not contain a security level [cons2]", cons2.containsLevel());
//		assertTrue("Constraint contains a security level [cons3]", cons3.containsLevel());
//		assertFalse("Constraint does not contain a security level [cons4]", cons4.containsLevel());
//		assertTrue("Constraint contains a security level [cons5]", cons5.containsLevel());
//		assertFalse("Constraint does not contain a security level [cons6]", cons6.containsLevel());
//	}
//
//	@Test
//	public final void testContainsParameterRef() {
//		LEQConstraint cons1 = new LEQConstraint(returnRef, level1);
//		LEQConstraint cons2 = new LEQConstraint(paramRef1, returnRef);
//		LEQConstraint cons3 = new LEQConstraint(level1, paramRef1);
//		LEQConstraint cons4 = new LEQConstraint(returnRef, returnRef);
//		LEQConstraint cons5 = new LEQConstraint(level1, level2);
//		LEQConstraint cons6 = new LEQConstraint(paramRef1, paramRef2);
//		assertFalse("Constraint does not contain a parameter reference [cons1]", cons1.containsParameterRef());
//		assertTrue("Constraint contains a parameter reference [cons2]", cons2.containsParameterRef());
//		assertTrue("Constraint contains a parameter reference [cons3]", cons3.containsParameterRef());
//		assertFalse("Constraint does not contain a parameter reference [cons4]", cons4.containsParameterRef());
//		assertFalse("Constraint does not contain a parameter reference [cons5]", cons5.containsParameterRef());
//		assertTrue("Constraint contains a parameter reference [cons6]", cons6.containsParameterRef());
//	}
//
//	@Test
//	public final void testContainsReturnRef() {
//		LEQConstraint cons1 = new LEQConstraint(returnRef, level1);
//		LEQConstraint cons2 = new LEQConstraint(paramRef1, returnRef);
//		LEQConstraint cons3 = new LEQConstraint(level1, paramRef1);
//		LEQConstraint cons4 = new LEQConstraint(returnRef, returnRef);
//		LEQConstraint cons5 = new LEQConstraint(level1, level2);
//		LEQConstraint cons6 = new LEQConstraint(paramRef1, paramRef2);
//		assertTrue("Constraint contains a return reference [cons1]", cons1.containsReturnRef());
//		assertTrue("Constraint contains a return reference [cons2]", cons2.containsReturnRef());
//		assertFalse("Constraint does not contain a return reference [cons3]", cons3.containsReturnRef());
//		assertTrue("Constraint contains a return reference [cons4]", cons4.containsReturnRef());
//		assertFalse("Constraint does not contain a return reference [cons5]", cons5.containsReturnRef());
//		assertFalse("Constraint does not contain a return reference [cons6]", cons6.containsReturnRef());
//	}
//
//	@Test
//	public final void testGetLhs() {
//		LEQConstraint cons1 = new LEQConstraint(level1, returnRef);
//		assertTrue("Get left handside of the constraint [level1].", cons1.getLhs().equals(level1));
//		assertFalse("Get not the right handside of the constraint [return].", cons1.getLhs().equals(returnRef));
//		LEQConstraint cons2 = new LEQConstraint(returnRef, paramRef1);
//		assertTrue("Get left handside of the constraint [return].", cons2.getLhs().equals(returnRef));
//		assertFalse("Get not the right handside of the constraint [param1].", cons2.getLhs().equals(paramRef1));
//		LEQConstraint cons3 = new LEQConstraint(paramRef1, level1);
//		assertTrue("Get left handside of the constraint [param1].", cons3.getLhs().equals(paramRef1));
//		assertFalse("Get not the right handside of the constraint [level1].", cons3.getLhs().equals(level1));
//	}
//
//	@Test
//	public final void testGetRhs() {
//		LEQConstraint cons1 = new LEQConstraint(returnRef, level1);
//		assertTrue("Get right handside of the constraint [level1].", cons1.getRhs().equals(level1));
//		assertFalse("Get not the left handside of the constraint [return].", cons1.getRhs().equals(returnRef));
//		LEQConstraint cons2 = new LEQConstraint(paramRef1, returnRef);
//		assertTrue("Get right handside of the constraint [return].", cons2.getRhs().equals(returnRef));
//		assertFalse("Get not the left handside of the constraint [param1].", cons2.getRhs().equals(paramRef1));
//		LEQConstraint cons3 = new LEQConstraint(level1, paramRef1);
//		assertTrue("Get right handside of the constraint [param1].", cons3.getRhs().equals(paramRef1));
//		assertFalse("Get not the left handside of the constraint [level1].", cons3.getRhs().equals(level1));
//	}
//
//	@Test
//	public final void testIsLevel() {
//		assertTrue("Component is a security level", isLevel(level1));
//		assertTrue("Component is a security level", isLevel(level2));
//		assertFalse("Component is not a security level", isLevel(paramRef1));
//		assertFalse("Component is not a security level", isLevel(paramRef2));
//		assertFalse("Component is not a security level", isLevel(returnRef));
//	}
//
//	@Test
//	public final void testIsParameterRef() {
//		assertTrue("Component is a parameter reference", isParameterRef(paramRef1));
//		assertTrue("Component is a parameter reference", isParameterRef(paramRef2));
//		assertFalse("Component is not a parameter reference", isParameterRef(level1));
//		assertFalse("Component is not a parameter reference", isParameterRef(level2));
//		assertFalse("Component is not a parameter reference", isParameterRef(returnRef));
//	}
//
//	@Test
//	public final void testIsReturnRef() {
//		assertTrue("Component is a return reference", isReturnRef(returnRef));
//		assertFalse("Component is not a return reference", isReturnRef(level1));
//		assertFalse("Component is not a return reference", isReturnRef(level2));
//		assertFalse("Component is not a return reference", isReturnRef(paramRef1));
//		assertFalse("Component is not a return reference", isReturnRef(paramRef2));
//	}
//	
//	@Test
//	public final void testEquals() {
//		LEQConstraint cons1 = new LEQConstraint(level1 , level2);
//		LEQConstraint cons2 = new LEQConstraint(returnRef , level2);
//		assertTrue("Same constraints", cons1.equals(cons1));
//		assertTrue("Equal constraints", cons1.equals(new LEQConstraint(level1, level2)));
//		assertTrue("Equal constraints", new LEQConstraint(level1, level2).equals(cons1));
//		assertFalse("Not equal constraints", cons1.equals(cons2));
//		assertFalse("Not equal constraints", cons2.equals(cons1));
//	}
//	

}

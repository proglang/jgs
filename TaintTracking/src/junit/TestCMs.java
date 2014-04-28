package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import security.ALevel;
import security.ILevel;

import constraints.ConstraintParameterRef;
import constraints.ConstraintProgramCounterRef;
import constraints.ConstraintReturnRef;
import constraints.ConstraintsUtils;
import constraints.IConstraint;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;

public class TestCMs {

	private static class SLevel extends ALevel {

		private final String level;

		private SLevel(String level) {
			this.level = level;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) return true;
			if (obj == null) return false;
			if (obj.getClass() != getClass()) return false;
			SLevel lev = (SLevel) obj;
			return lev.level.equals(level);
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
//
//	@Test
//	public final void testContains() {
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_lev_low_pc }), cons_lev_low_pc));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_pc_lev_high }), cons_pc_lev_high));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_ret_a_pc }), cons_ret_a_pc));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_par3_b_ret_b }), cons_par3_b_ret_b));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_lev_low_pc }), new LEQConstraint(comp_lev_low, comp_pc)));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_pc_lev_high }), new LEQConstraint(comp_pc, comp_lev_high)));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_ret_a_pc }), new LEQConstraint(comp_ret_a, comp_pc)));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_par3_b_ret_b }), new LEQConstraint(comp_par3_b, comp_ret_b)));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_lev_low_pc, cons_lev_low_ret_a }), cons_lev_low_pc));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_ret_b_lev_high, cons_pc_lev_high }), cons_pc_lev_high));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_pc_ret_b, cons_ret_a_pc }), cons_ret_a_pc));
//		assertTrue(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_ret_b_lev_high, cons_par3_b_ret_b, cons_ret_a_pc }),
//				cons_par3_b_ret_b));
//		assertFalse(ConstraintsMethods.contains(mkList(new IConstraint[] {}), cons_lev_low_pc));
//		assertFalse(ConstraintsMethods.contains(mkList(new IConstraint[] {}), cons_pc_lev_high));
//		assertFalse(ConstraintsMethods.contains(mkList(new IConstraint[] {}), cons_ret_a_pc));
//		assertFalse(ConstraintsMethods.contains(mkList(new IConstraint[] {}), cons_par3_b_ret_b));
//		assertFalse(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_lev_low_par1_a }), cons_lev_low_pc));
//		assertFalse(ConstraintsMethods.contains(mkList(new IConstraint[] { cons_lev_low_par1_a, cons_ret_a_par3_a }), cons_lev_low_pc));
//	}
//
//	@Test
//	public final void testContainsParameterRefListOfIConstraint() {
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc })));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_par1_b })));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_lev_high, cons_par2_b_pc })));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc, cons_pc_lev_high })));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_ret_b_lev_high, cons_par2_b_pc, cons_pc_lev_high })));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] {})));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_ret_b })));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_ret_b, cons_lev_low_ret_a })));
//	}
//
//	@Test
//	public final void testContainsParameterRefListOfIConstraintString() {
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc }), "b"));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_lev_high, cons_par2_b_pc }), "b"));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc, cons_pc_lev_high }), "b"));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_ret_b_lev_high, cons_par2_b_pc, cons_pc_lev_high }),
//				"b"));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc }), "a"));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_lev_high, cons_par2_b_pc }), "a"));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc, cons_pc_lev_high }), "a"));
//		assertFalse(ConstraintsMethods.containsParameterRef(
//				mkList(new IConstraint[] { cons_ret_b_lev_high, cons_par2_b_pc, cons_pc_lev_high }), "a"));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] {}), "b"));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_ret_b }), "b"));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_ret_b, cons_lev_low_ret_a }), "b"));
//	}
//
//	@Test
//	public final void testContainsParameterRefListOfIConstraintStringInt() {
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc }), "b", 1));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_lev_high, cons_par2_b_pc }), "b", 1));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc, cons_pc_lev_high }), "b", 1));
//		assertTrue(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_ret_b_lev_high, cons_par2_b_pc, cons_pc_lev_high }),
//				"b", 1));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc }), "b", 0));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_lev_high, cons_par2_b_pc }), "b", 0));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc, cons_pc_lev_high }), "b", 0));
//		assertFalse(ConstraintsMethods.containsParameterRef(
//				mkList(new IConstraint[] { cons_ret_b_lev_high, cons_par2_b_pc, cons_pc_lev_high }), "b", 0));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc }), "a", 1));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_lev_high, cons_par2_b_pc }), "a", 1));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_par2_b_pc, cons_pc_lev_high }), "a", 1));
//		assertFalse(ConstraintsMethods.containsParameterRef(
//				mkList(new IConstraint[] { cons_ret_b_lev_high, cons_par2_b_pc, cons_pc_lev_high }), "a", 1));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] {}), "b", 1));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_ret_b }), "b", 1));
//		assertFalse(ConstraintsMethods.containsParameterRef(mkList(new IConstraint[] { cons_pc_ret_b, cons_lev_low_ret_a }), "b", 1));
//	}
//
//	@Test
//	public final void testContainsReturnRefListOfIConstraint() {
//		assertTrue(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_ret_a_pc })));
//		assertTrue(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_par3_b_ret_b })));
//		assertTrue(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_pc_par1_b, cons_par3_b_ret_b })));
//		assertTrue(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_pc_par1_b })));
//		assertFalse(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] {})));
//		assertFalse(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_lev_low_pc })));
//		assertFalse(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_par2_a_lev_high, cons_lev_low_par1_a })));
//	}
//
//	@Test
//	public final void testContainsReturnRefListOfIConstraintString() {
//		assertTrue(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_ret_a_pc }), "a"));
//		assertTrue(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_par3_b_ret_b }), "b"));
//		assertTrue(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_pc_par1_b, cons_par3_b_ret_b }), "b"));
//		assertTrue(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_pc_par1_b }), "b"));
//		assertFalse(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_ret_a_pc }), "b"));
//		assertFalse(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_par3_b_ret_b }), "a"));
//		assertFalse(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] {}), "a"));
//		assertFalse(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_lev_low_pc }), "a"));
//		assertFalse(ConstraintsMethods.containsReturnRef(mkList(new IConstraint[] { cons_par2_a_lev_high, cons_lev_low_par1_a }), "a"));
//	}
//
//	@Test
//	public final void testContainsProgramCounterRef() {
//		assertTrue(ConstraintsMethods.containsProgramCounterRef(mkList(new IConstraint[] { cons_ret_a_pc })));
//		assertTrue(ConstraintsMethods.containsProgramCounterRef(mkList(new IConstraint[] { cons_pc_lev_high })));
//		assertTrue(ConstraintsMethods.containsProgramCounterRef(mkList(new IConstraint[] { cons_ret_a_par3_a, cons_ret_a_pc })));
//		assertTrue(ConstraintsMethods.containsProgramCounterRef(mkList(new IConstraint[] { cons_ret_a_pc, cons_ret_a_par3_a })));
//		assertFalse(ConstraintsMethods.containsProgramCounterRef(mkList(new IConstraint[] {})));
//		assertFalse(ConstraintsMethods.containsProgramCounterRef(mkList(new IConstraint[] { cons_ret_a_par3_a })));
//		assertFalse(ConstraintsMethods.containsProgramCounterRef(mkList(new IConstraint[] { cons_ret_a_par3_a, cons_ret_b_lev_high })));
//	}
//
//	@Test
//	public final void testContainsLevel() {
//		assertTrue(ConstraintsMethods.containsLevel(mkList(new IConstraint[] { cons_lev_low_pc }), (ILevel) comp_lev_low));
//		assertTrue(ConstraintsMethods.containsLevel(mkList(new IConstraint[] { cons_ret_b_lev_high }), (ILevel) comp_lev_high));
//		assertTrue(ConstraintsMethods.containsLevel(mkList(new IConstraint[] { cons_lev_low_pc, cons_par2_a_lev_high }), (ILevel) comp_lev_low));
//		assertTrue(ConstraintsMethods.containsLevel(mkList(new IConstraint[] { cons_par2_a_lev_high, cons_lev_low_pc }), (ILevel) comp_lev_low));
//		assertFalse(ConstraintsMethods.containsLevel(mkList(new IConstraint[] {}), (ILevel) comp_lev_low));
//		assertFalse(ConstraintsMethods.containsLevel(mkList(new IConstraint[] { cons_pc_lev_high }), (ILevel) comp_lev_low));
//		assertFalse(ConstraintsMethods.containsLevel(mkList(new IConstraint[] { cons_par3_b_ret_b }), (ILevel) comp_lev_low));
//		assertFalse(ConstraintsMethods.containsLevel(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_pc_par1_b }), (ILevel) comp_lev_low));
//	}
//
//	@Test
//	public final void testGetConstraintsIncludingParameterListOfIConstraint() {
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] {})), mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_pc })),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_par1_a })),
//				mkList(new IConstraint[] { cons_lev_low_par1_a })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_par1_a, cons_pc_par1_b })),
//				mkList(new IConstraint[] { cons_lev_low_par1_a, cons_pc_par1_b })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_par1_a, cons_pc_par1_b,
//						cons_lev_low_par1_a })), mkList(new IConstraint[] { cons_lev_low_par1_a, cons_pc_par1_b })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_par1_a, cons_ret_b_lev_high })),
//				mkList(new IConstraint[] { cons_lev_low_par1_a })));
//		assertFalse(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] {})),
//				mkList(new IConstraint[] { cons_lev_low_par1_a })));
//		assertFalse(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_par3_b_ret_b })),
//				mkList(new IConstraint[] { cons_lev_low_par1_a })));
//		assertFalse(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_par3_b_ret_b })),
//				mkList(new IConstraint[] {})));
//	}
//
//	@Test
//	public final void testGetConstraintsIncludingParameterListOfIConstraintString() {
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] {}), "a"),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_pc }), "a"),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_pc_par1_b }), "a"),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_par1_a }), "a"),
//				mkList(new IConstraint[] { cons_lev_low_par1_a })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_par1_a, cons_pc_par1_b }), "a"),
//				mkList(new IConstraint[] { cons_lev_low_par1_a })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_par1_a, cons_pc_par1_b }), "b"),
//				mkList(new IConstraint[] { cons_pc_par1_b })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_par2_b_pc, cons_pc_par1_b }), "b"),
//				mkList(new IConstraint[] { cons_pc_par1_b, cons_par2_b_pc })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_par2_b_pc, cons_pc_par1_b }), "a"),
//				mkList(new IConstraint[] {})));
//	}
//
//	@Test
//	public final void testGetConstraintsIncludingParameterListOfIConstraintStringInt() {
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] {}), "a", 0),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_par2_a_lev_high }), "a", 0),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_pc_par1_b }), "a", 0),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_par1_a }), "a", 0),
//				mkList(new IConstraint[] { cons_lev_low_par1_a })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingParameter(mkList(new IConstraint[] { cons_lev_low_par1_a,
//						new LEQConstraint(comp_par1_a_, comp_lev_low_) }), "a", 0), mkList(new IConstraint[] { cons_lev_low_par1_a,
//						new LEQConstraint(comp_par1_a, comp_lev_low) })));
//	}
//
//	@Test
//	public final void testGetConstraintsIncludingReturnListOfIConstraint() {
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] {})), mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_lev_low_pc })),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_lev_low_ret_a })),
//				mkList(new IConstraint[] { cons_lev_low_ret_a })));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_lev_low_ret_a, cons_pc_ret_b })),
//				mkList(new IConstraint[] { cons_lev_low_ret_a, cons_pc_ret_b })));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_pc_ret_b, cons_pc_ret_b })),
//				mkList(new IConstraint[] { cons_pc_ret_b })));
//	}
//
//	@Test
//	public final void testGetConstraintsIncludingReturnListOfIConstraintString() {
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] {}), "a"), mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_lev_low_pc }), "a"),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_lev_low_ret_a }), "a"),
//				mkList(new IConstraint[] { cons_lev_low_ret_a })));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_lev_low_ret_a }), "b"),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_lev_low_ret_a, cons_pc_ret_b }), "a"),
//				mkList(new IConstraint[] { cons_lev_low_ret_a })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_lev_low_ret_a, cons_pc_ret_b }), "b"),
//				mkList(new IConstraint[] { cons_pc_ret_b })));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_pc_ret_b, cons_pc_ret_b }), "b"),
//				mkList(new IConstraint[] { cons_pc_ret_b })));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingReturn(mkList(new IConstraint[] { cons_pc_ret_b, cons_pc_ret_b }), "a"),
//				mkList(new IConstraint[] {})));
//	}
//
//	@Test
//	public final void testGetConstraintsIncludingProgramCounter() {
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingProgramCounter(mkList(new IConstraint[] {})),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingProgramCounter(mkList(new IConstraint[] { cons_lev_low_ret_a })),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingProgramCounter(mkList(new IConstraint[] { cons_lev_low_ret_a, cons_pc_ret_b })),
//				mkList(new IConstraint[] { cons_pc_ret_b })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingProgramCounter(mkList(new IConstraint[] { cons_pc_ret_b, cons_lev_low_ret_a })),
//				mkList(new IConstraint[] { cons_pc_ret_b })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingProgramCounter(mkList(new IConstraint[] { cons_pc_par1_b, cons_pc_ret_b })),
//				mkList(new IConstraint[] { cons_pc_ret_b, cons_pc_par1_b })));
//	}
//
//	@Test
//	public final void testGetConstraintsIncludingLevelsListOfIConstraint() {
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] {})), mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_pc_ret_b })),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_pc_ret_b, cons_ret_a_par3_a })),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_lev_low_pc })),
//				mkList(new IConstraint[] { cons_lev_low_pc })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_lev_low_pc, cons_ret_a_par3_a })),
//				mkList(new IConstraint[] { cons_lev_low_pc })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_ret_a_par3_a, cons_lev_low_pc })),
//				mkList(new IConstraint[] { cons_lev_low_pc })));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_par2_a_lev_high, cons_lev_low_pc })),
//				mkList(new IConstraint[] { cons_lev_low_pc, cons_par2_a_lev_high })));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_par2_a_lev_high, cons_par2_b_pc,
//				cons_lev_low_pc })), mkList(new IConstraint[] { cons_lev_low_pc, cons_par2_a_lev_high })));
//	}
//
//	@Test
//	public final void testGetConstraintsIncludingLevelsListOfIConstraintILevel() {
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] {}), (ILevel) comp_lev_low),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_pc_ret_b }), (ILevel) comp_lev_low),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_par2_a_lev_high }), (ILevel) comp_lev_low),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(
//				ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_lev_low_pc }), (ILevel) comp_lev_low),
//				mkList(new IConstraint[] { cons_lev_low_pc })));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingLevels(
//				mkList(new IConstraint[] { cons_lev_low_pc, cons_par2_a_lev_high }), (ILevel) comp_lev_low),
//				mkList(new IConstraint[] { cons_lev_low_pc })));
//		assertTrue(equalList(ConstraintsMethods.getConstraintsIncludingLevels(mkList(new IConstraint[] { cons_lev_low_pc, cons_par2_a_lev_high,
//				cons_pc_ret_b }), (ILevel) comp_lev_low), mkList(new IConstraint[] { cons_lev_low_pc })));
//	}
//
//	@Test
//	public final void testGetContainedLevels() {
//		assertTrue(equalList(ConstraintsMethods.getContainedLevels(mkList(new IConstraint[] {})), mkList(new ILevel[] {})));
//		assertTrue(equalList(ConstraintsMethods.getContainedLevels(mkList(new IConstraint[] { cons_pc_ret_b })), mkList(new ILevel[] {})));
//		assertTrue(equalList(ConstraintsMethods.getContainedLevels(mkList(new IConstraint[] { cons_pc_lev_high })),
//				mkList(new ILevel[] { (ILevel) comp_lev_high })));
//		assertTrue(equalList(ConstraintsMethods.getContainedLevels(mkList(new IConstraint[] { cons_pc_lev_high, cons_par3_b_ret_b })),
//				mkList(new ILevel[] { (ILevel) comp_lev_high })));
//		assertTrue(equalList(
//				ConstraintsMethods.getContainedLevels(mkList(new IConstraint[] { cons_par2_a_lev_high, cons_pc_lev_high, cons_par3_b_ret_b })),
//				mkList(new ILevel[] { (ILevel) comp_lev_high })));
//	}
//
//	@Test
//	public final void testGetContainedParameterRefsListOfIConstraint() {
//		assertTrue(equalList(ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] {})),
//				mkList(new ConstraintParameterRef[] {})));
//		assertTrue(equalList(ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_ret_a_pc })),
//				mkList(new ConstraintParameterRef[] {})));
//		assertTrue(equalList(ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_par3_b_ret_b })),
//				mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par3_b })));
//		assertTrue(equalList(
//				ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_par2_a_lev_high })),
//				mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par3_b, (ConstraintParameterRef) comp_par2_a })));
//		assertTrue(equalList(
//				ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_pc_ret_b, cons_par2_a_lev_high })),
//				mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par3_b, (ConstraintParameterRef) comp_par2_a })));
//	}
//
//	@Test
//	public final void testGetContainedParameterRefsListOfIConstraintString() {
//		assertTrue(equalList(ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] {}), "a"),
//				mkList(new ConstraintParameterRef[] {})));
//		assertTrue(equalList(ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] {}), "b"),
//				mkList(new ConstraintParameterRef[] {})));
//		assertTrue(equalList(ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_ret_a_pc }), "a"),
//				mkList(new ConstraintParameterRef[] {})));
//		assertTrue(equalList(ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_ret_a_pc }), "b"),
//				mkList(new ConstraintParameterRef[] {})));
//		assertTrue(equalList(ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_par3_b_ret_b }), "b"),
//				mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par3_b })));
//		assertTrue(equalList(ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_par3_b_ret_b }), "a"),
//				mkList(new ConstraintParameterRef[] {})));
//		assertTrue(equalList(
//				ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_par2_a_lev_high }), "a"),
//				mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par2_a })));
//		assertTrue(equalList(
//				ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_par2_a_lev_high }), "b"),
//				mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par3_b })));
//		assertTrue(equalList(
//				ConstraintsMethods.getContainedParameterRefs(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_lev_low_par1_a,
//						cons_par2_a_lev_high }), "a"), mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par1_a,
//						(ConstraintParameterRef) comp_par2_a })));
//	}
//
//	@Test
//	public final void testHighestParameterRefNumber() {
//		assertTrue(ConstraintsMethods.highestParameterRefNumber(mkList(new ConstraintParameterRef[] {}), "a") == -1);
//		assertTrue(ConstraintsMethods.highestParameterRefNumber(mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par1_a }),
//				"a") == 0);
//		assertTrue(ConstraintsMethods.highestParameterRefNumber(mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par1_b }),
//				"a") == -1);
//		assertTrue(ConstraintsMethods.highestParameterRefNumber(mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par3_a }),
//				"a") == 2);
//		assertTrue(ConstraintsMethods.highestParameterRefNumber(mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par2_a,
//				(ConstraintParameterRef) comp_par1_a }), "a") == 1);
//		assertTrue(ConstraintsMethods.highestParameterRefNumber(mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par1_a,
//				(ConstraintParameterRef) comp_par2_a }), "a") == 1);
//		assertTrue(ConstraintsMethods.highestParameterRefNumber(mkList(new ConstraintParameterRef[] { (ConstraintParameterRef) comp_par1_a,
//				(ConstraintParameterRef) comp_par2_b }), "a") == 0);
//	}
//
//	protected IConstraintComponent comp_lev_low = new SLevel("low");
//	protected IConstraintComponent comp_lev_low_ = new SLevel("low");
//	protected IConstraintComponent comp_lev_high = new SLevel("high");
//
//	protected IConstraintComponent comp_pc = new ConstraintProgramCounterRef();
//	protected IConstraintComponent comp_pc_ = new ConstraintProgramCounterRef();
//
//	protected IConstraintComponent comp_ret_a = new ConstraintReturnRef("a");
//	protected IConstraintComponent comp_ret_a_ = new ConstraintReturnRef("a");
//	protected IConstraintComponent comp_ret_b = new ConstraintReturnRef("b");
//
//	protected IConstraintComponent comp_par1_a = new ConstraintParameterRef(0, "a");
//	protected IConstraintComponent comp_par1_a_ = new ConstraintParameterRef(0, "a");
//	protected IConstraintComponent comp_par2_a = new ConstraintParameterRef(1, "a");
//	protected IConstraintComponent comp_par3_a = new ConstraintParameterRef(2, "a");
//	protected IConstraintComponent comp_par1_b = new ConstraintParameterRef(0, "b");
//	protected IConstraintComponent comp_par2_b = new ConstraintParameterRef(1, "b");
//	protected IConstraintComponent comp_par3_b = new ConstraintParameterRef(2, "b");
//
//	protected IConstraint cons_lev_low_pc = new LEQConstraint(comp_lev_low, comp_pc);
//	protected IConstraint cons_lev_low_ret_a = new LEQConstraint(comp_lev_low, comp_ret_a);
//	protected IConstraint cons_lev_low_par1_a = new LEQConstraint(comp_lev_low, comp_par1_a);
//	protected IConstraint cons_pc_lev_high = new LEQConstraint(comp_pc, comp_lev_high);
//	protected IConstraint cons_ret_b_lev_high = new LEQConstraint(comp_ret_b, comp_lev_high);
//	protected IConstraint cons_par2_a_lev_high = new LEQConstraint(comp_par2_a, comp_lev_high);
//	protected IConstraint cons_pc_ret_b = new LEQConstraint(comp_pc, comp_ret_b);
//	protected IConstraint cons_pc_par1_b = new LEQConstraint(comp_pc, comp_par1_b);
//	protected IConstraint cons_ret_a_pc = new LEQConstraint(comp_ret_a, comp_pc);
//	protected IConstraint cons_par2_b_pc = new LEQConstraint(comp_par2_b, comp_pc);
//	protected IConstraint cons_ret_a_par3_a = new LEQConstraint(comp_ret_a, comp_par3_a);
//	protected IConstraint cons_par3_b_ret_b = new LEQConstraint(comp_par3_b, comp_ret_b);
//
//	@Test
//	public final void testGetLEQConstraintsWithLeftHandSide() {
//		assertTrue(equalList(ConstraintsMethods.getLEQConstraintsWithLeftHandSide(mkList(new IConstraint[] {}), comp_pc),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getLEQConstraintsWithLeftHandSide(mkList(new IConstraint[] { cons_par3_b_ret_b }), comp_pc),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(
//				ConstraintsMethods.getLEQConstraintsWithLeftHandSide(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_par2_b_pc }), comp_pc),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(
//				ConstraintsMethods.getLEQConstraintsWithLeftHandSide(mkList(new IConstraint[] { cons_par3_b_ret_b, cons_pc_lev_high }), comp_pc),
//				mkList(new IConstraint[] { cons_pc_lev_high })));
//		assertTrue(equalList(
//				ConstraintsMethods.getLEQConstraintsWithLeftHandSide(mkList(new IConstraint[] { cons_pc_par1_b, cons_pc_lev_high }), comp_pc),
//				mkList(new IConstraint[] { cons_pc_par1_b, cons_pc_lev_high })));
//		assertTrue(equalList(
//				ConstraintsMethods.getLEQConstraintsWithLeftHandSide(mkList(new IConstraint[] { cons_pc_par1_b, cons_pc_lev_high, cons_ret_a_pc }), comp_pc),
//				mkList(new IConstraint[] { cons_pc_par1_b, cons_pc_lev_high })));
//	}
//
//	@Test
//	public final void testGetLEQConstraintsWithRightHandSide() {
//		assertTrue(equalList(ConstraintsMethods.getLEQConstraintsWithRightHandSide(mkList(new IConstraint[] {}), comp_pc),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getLEQConstraintsWithRightHandSide(mkList(new IConstraint[] {cons_ret_a_par3_a}), comp_pc),
//				mkList(new IConstraint[] {})));
//		assertTrue(equalList(ConstraintsMethods.getLEQConstraintsWithRightHandSide(mkList(new IConstraint[] {cons_ret_a_pc}), comp_pc),
//				mkList(new IConstraint[] {cons_ret_a_pc})));
//		assertTrue(equalList(ConstraintsMethods.getLEQConstraintsWithRightHandSide(mkList(new IConstraint[] {cons_par3_b_ret_b, cons_ret_a_pc}), comp_pc),
//				mkList(new IConstraint[] {cons_ret_a_pc})));
//		assertTrue(equalList(ConstraintsMethods.getLEQConstraintsWithRightHandSide(mkList(new IConstraint[] {cons_pc_par1_b, cons_ret_a_pc}), comp_pc),
//				mkList(new IConstraint[] {cons_ret_a_pc})));
//	}
//
//	@Test
//	public final void testIsLevelIConstraintComponent() {
//		assertTrue(ConstraintsMethods.isLevel(comp_lev_low));
//		assertTrue(ConstraintsMethods.isLevel(comp_lev_high));
//		assertFalse(ConstraintsMethods.isLevel(comp_par1_a));
//		assertFalse(ConstraintsMethods.isLevel(comp_pc));
//		assertFalse(ConstraintsMethods.isLevel(comp_ret_a));
//	}
//
//	@Test
//	public final void testIsLevelIConstraintComponentILevel() {
//		assertTrue(ConstraintsMethods.isLevel(comp_lev_low, new SLevel("low")));
//		assertTrue(ConstraintsMethods.isLevel(comp_lev_low, (ILevel) comp_lev_low));
//		assertFalse(ConstraintsMethods.isLevel(comp_lev_low, new SLevel("high")));
//		assertFalse(ConstraintsMethods.isLevel(comp_lev_low, (ILevel) comp_lev_high));
//		assertFalse(ConstraintsMethods.isLevel(comp_par1_a, new SLevel("low")));
//		assertFalse(ConstraintsMethods.isLevel(comp_par1_a, (ILevel) comp_lev_low));
//		assertFalse(ConstraintsMethods.isLevel(comp_pc, new SLevel("low")));
//		assertFalse(ConstraintsMethods.isLevel(comp_pc, (ILevel) comp_lev_low));
//		assertFalse(ConstraintsMethods.isLevel(comp_ret_a, new SLevel("low")));
//		assertFalse(ConstraintsMethods.isLevel(comp_ret_a, (ILevel) comp_lev_low));
//	}
//
//	@Test
//	public final void testIsParameterRefIConstraintComponent() {
//		assertTrue(ConstraintsMethods.isParameterRef(comp_par1_a));
//		assertTrue(ConstraintsMethods.isParameterRef(comp_par2_a));
//		assertTrue(ConstraintsMethods.isParameterRef(comp_par1_b));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_pc));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_ret_a));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_lev_low));
//	}
//
//	@Test
//	public final void testIsParameterRefIConstraintComponentString() {
//		assertTrue(ConstraintsMethods.isParameterRef(comp_par1_a, "a"));
//		assertTrue(ConstraintsMethods.isParameterRef(comp_par2_a, "a"));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_par1_b, "a"));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_par2_b, "a"));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_pc, "a"));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_ret_a, "a"));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_lev_low, "a"));
//	}
//
//	@Test
//	public final void testIsParameterRefIConstraintComponentStringInt() {
//		System.out.println(ConstraintsMethods.isParameterRef(comp_par1_a, "a", 1));
//		assertTrue(ConstraintsMethods.isParameterRef(comp_par1_a, "a", 0));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_par2_a, "a", 0));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_par1_b, "a", 0));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_par2_b, "a", 0));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_pc, "a", 0));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_ret_a, "a", 0));
//		assertFalse(ConstraintsMethods.isParameterRef(comp_lev_low, "a", 0));
//	}
//
//	@Test
//	public final void testIsProgramCounterRef() {
//		assertTrue(ConstraintsMethods.isProgramCounterRef(comp_pc));
//		assertFalse(ConstraintsMethods.isProgramCounterRef(comp_ret_a));
//		assertFalse(ConstraintsMethods.isProgramCounterRef(comp_par1_a));
//		assertFalse(ConstraintsMethods.isProgramCounterRef(comp_lev_low));
//	}
//
//	@Test
//	public final void testIsReturnRefIConstraintComponent() {
//		assertTrue(ConstraintsMethods.isReturnRef(comp_ret_a));
//		assertTrue(ConstraintsMethods.isReturnRef(comp_ret_b));
//		assertFalse(ConstraintsMethods.isReturnRef(comp_par1_a));
//		assertFalse(ConstraintsMethods.isReturnRef(comp_pc));
//		assertFalse(ConstraintsMethods.isReturnRef(comp_lev_low));
//	}
//
//	@Test
//	public final void testIsReturnRefIConstraintComponentString() {
//		assertTrue(ConstraintsMethods.isReturnRef(comp_ret_a, "a"));
//		assertFalse(ConstraintsMethods.isReturnRef(comp_ret_b, "a"));
//		assertFalse(ConstraintsMethods.isReturnRef(comp_par1_a, "a"));
//		assertFalse(ConstraintsMethods.isReturnRef(comp_pc, "a"));
//		assertFalse(ConstraintsMethods.isReturnRef(comp_lev_low, "a"));
//	}

	public static <U> List<U> mkList(U[] array) {
		return new ArrayList<U>(Arrays.asList(array));
	}

	public static <U> boolean equalList(List<U> l1, List<U> l2) {
		if (l1.size() != l2.size()) return false;
		for (U u : l1) {
			if (!l2.contains(u)) return false;
		}
		for (U u : l2) {
			if (!l1.contains(u)) return false;
		}
		return true;
	}
}

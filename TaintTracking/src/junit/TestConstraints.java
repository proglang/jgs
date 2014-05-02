package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import junit.Fed1.VelS;

import logging.AnalysisLog;
import main.Mediator;

import org.junit.Before;
import org.junit.Test;

import security.ILevelDefinition;
import soot.Local;
import soot.RefType;
import soot.jimple.internal.JimpleLocal;

import constraints.ConstraintLocal;
import constraints.ConstraintParameterRef;
import constraints.ConstraintProgramCounterRef;
import constraints.ConstraintReturnRef;
import constraints.Constraints;
import constraints.IConstraint;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;

public class TestConstraints {

	
	private AnalysisLog log = new AnalysisLog(false, new Level[] {});
	private ILevelDefinition def = new Fed1();
	private Mediator mediator = new Mediator(def);

	private VelS low = new VelS("low");
	private VelS high = new VelS("high");
	private IConstraintComponent par0a = new ConstraintParameterRef(0, "a");
	private IConstraintComponent par1a = new ConstraintParameterRef(1, "a");
	private IConstraintComponent reta = new ConstraintReturnRef("a");
	private IConstraintComponent par0b = new ConstraintParameterRef(0, "b");
	private IConstraintComponent par1b = new ConstraintParameterRef(1, "b");
	private IConstraintComponent retb = new ConstraintReturnRef("b");
	private IConstraintComponent pc = new ConstraintProgramCounterRef();
	private IConstraint p0a_p1a = new LEQConstraint(par0a, par1a);
	private IConstraint l_p0a = new LEQConstraint(low, par0a);
	private IConstraint ra_h = new LEQConstraint(reta, high);
	private IConstraint ra_pc = new LEQConstraint(reta, pc);
	private IConstraint p1a_ra = new LEQConstraint(par1a, reta);
	private IConstraint l_p1a = new LEQConstraint(low, par1a);
	private IConstraint l_pc = new LEQConstraint(low, pc);
	private IConstraint p0a_pc = new LEQConstraint(par0a, pc);
	private IConstraint l_h = new LEQConstraint(low, high);
	private IConstraint l_ra = new LEQConstraint(low, reta);
	private IConstraint p0a_ra = new LEQConstraint(par0a, reta);
	private IConstraint p0a_h = new LEQConstraint(par0a, high);
	private IConstraint p1a_h = new LEQConstraint(par1a, high);
	private IConstraint p1a_pc = new LEQConstraint(par1a, pc);
	private IConstraint p0a_l = new LEQConstraint(par0a, low);
	private IConstraint h_l = new LEQConstraint(high, low);
	private Constraints c1 = new Constraints(mediator, log);
	private Local la = new JimpleLocal("a", RefType.v("int"));
	private Local lb = new JimpleLocal("b", RefType.v("foo.baz.Bar"));

	private static List<IConstraint> EMPTY_CONSTRAINT_LIST = mkList(new IConstraint[] {});

	@Before
	public final void reset() {
		c1.clear();
	}

	@Test
	public final void testGetTransitiveClosure1() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addSmart(p0a_p1a);
		Constraints c2 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c2.getConstraintsList(), mkList(new IConstraint[] { p0a_p1a })));

		c1.addSmart(l_p0a);
		Constraints c3 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c3.getConstraintsList(), mkList(new IConstraint[] { p0a_p1a, l_p0a, l_p1a })));

		c1.addSmart(ra_h);
		Constraints c4 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c4.getConstraintsList(), mkList(new IConstraint[] { p0a_p1a, l_p0a, l_p1a, ra_h })));

		c1.addSmart(ra_pc);
		Constraints c5 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c5.getConstraintsList(), mkList(new IConstraint[] { p0a_p1a, l_p0a, l_p1a, ra_h, ra_pc })));

		c1.addSmart(p1a_ra);
		Constraints c6 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c6.getConstraintsList(), mkList(new IConstraint[] { p0a_p1a, l_p0a, l_p1a, ra_h, ra_pc, p1a_ra, l_pc,
				p0a_pc, l_h, l_ra, p0a_ra, p0a_h, p1a_h, p1a_pc })));
	}

	@Test
	public final void testGetTransitiveClosure2() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addSmart(l_p0a);
		Constraints c2 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c2.getConstraintsList(), mkList(new IConstraint[] { l_p0a })));

		c1.addSmart(p0a_p1a);
		Constraints c3 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c3.getConstraintsList(), mkList(new IConstraint[] { l_p0a, p0a_p1a, l_p1a })));

		c1.addSmart(p1a_h);
		Constraints c4 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c4.getConstraintsList(), mkList(new IConstraint[] { l_p0a, p0a_p1a, p1a_h, l_p1a, p0a_h, l_h })));
	}

	@Test
	public final void testGetInequations1() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(EMPTY_CONSTRAINT_LIST);
		assertTrue(equalContentOfLists(c1.getInequality(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { ra_h, l_h, p0a_l }));
		assertTrue(equalContentOfLists(c1.getInequality(), EMPTY_CONSTRAINT_LIST));

		List<IConstraint> l1 = mkList(new IConstraint[] { h_l });
		c1.addAllSmart(l1);
		assertTrue(equalContentOfLists(c1.getInequality(), l1));
	}

	@Test
	public final void testGetInequations2() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(EMPTY_CONSTRAINT_LIST);
		assertTrue(equalContentOfLists(c1.getInequality(), EMPTY_CONSTRAINT_LIST));

		List<IConstraint> l1 = mkList(new IConstraint[] { h_l });
		c1.addAllSmart(mkList(new IConstraint[] { h_l, ra_h, l_h, p0a_l }));
		assertTrue(equalContentOfLists(c1.getInequality(), l1));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor1() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(EMPTY_CONSTRAINT_LIST);
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor2() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		List<IConstraint> l1 = mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc });
		c1.addAllSmart(l1);
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsList(), l1));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor3() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { p1a_h, new LEQConstraint(low, retb), ra_h, l_h, l_pc }));
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc })));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor4() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { p1a_h, ra_h, new LEQConstraint(par1b, high), l_h, l_pc }));
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc })));
	}
	
	@Test
	public final void testRemoveConstraintsContainingReferencesFor5() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { p1a_h, ra_h, new LEQConstraint(par1b, high), l_h, l_pc, new LEQConstraint(par1a, par0b) }));
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc })));
	}
	
	@Test
	public final void testRemoveConstraintsContainingReferencesFor6() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { p1a_h, ra_h, new LEQConstraint(retb, par1b), new LEQConstraint(par1b, high), l_h, l_pc, new LEQConstraint(par1a, par0b) }));
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc })));
	}
	
	@Test
	public final void testRemoveConstraintsContaining1() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(EMPTY_CONSTRAINT_LIST);
		c1.removeConstraintsContaining(la);
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));
	}
	
	@Test
	public final void testRemoveConstraintsContaining2() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		List<IConstraint> l1 = mkList(new IConstraint[] { ra_h, l_h, p0a_p1a });
		c1.addAllSmart(l1);
		c1.removeConstraintsContaining(la);
		assertTrue(equalContentOfLists(c1.getConstraintsList(), l1));
	}
	
	@Test
	public final void testRemoveConstraintsContaining3() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		List<IConstraint> l1 = mkList(new IConstraint[] { ra_h, new LEQConstraint(par1b, new ConstraintLocal(lb)), l_h, p0a_p1a});
		c1.addAllSmart(l1);
		c1.removeConstraintsContaining(la);
		assertTrue(equalContentOfLists(c1.getConstraintsList(), l1));
	}
	
	@Test
	public final void testRemoveConstraintsContaining4() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { ra_h, new LEQConstraint(par1a, new ConstraintLocal(la)), l_h, p0a_p1a, new LEQConstraint(new ConstraintLocal(lb), pc) }));
		c1.removeConstraintsContaining(la);
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a, new LEQConstraint(new ConstraintLocal(lb), pc) })));
	}
	
	@Test
	public final void testRemoveConstraintsContaining5() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { ra_h, new LEQConstraint(par1b, new ConstraintLocal(la)), l_h, p0a_p1a}));
		c1.removeConstraintsContaining(la);
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a})));
	}
	
	@Test
	public final void testRemoveConstraintsContaining6() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { ra_h, new LEQConstraint(new ConstraintLocal(la), par1b), l_h, p0a_p1a, new LEQConstraint(new ConstraintLocal(la), high) }));
		c1.removeConstraintsContaining(la);
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a })));
	}
	
	@Test
	public final void testRemoveConstraintsContainingLocal1() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(EMPTY_CONSTRAINT_LIST);
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));
	}
	
	@Test
	public final void testRemoveConstraintsContainingLocal2() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		List<IConstraint> l1 = mkList(new IConstraint[] { ra_h, l_h, p0a_p1a });
		c1.addAllSmart(l1);
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsList(), l1));
	}
	
	@Test
	public final void testRemoveConstraintsContainingLocal3() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { ra_h, new LEQConstraint(par1a, new ConstraintLocal(lb)), l_h, p0a_p1a, new LEQConstraint(new ConstraintLocal(lb), pc) }));
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a })));
	}
	
	@Test
	public final void testRemoveConstraintsContainingLocal4() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { ra_h, new LEQConstraint(par1a, new ConstraintLocal(la)), l_h, p0a_p1a, new LEQConstraint(new ConstraintLocal(lb), pc) }));
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a })));
	}
	
	@Test
	public final void testRemoveConstraintsContainingLocal5() {
		assertTrue(equalContentOfLists(c1.getConstraintsList(), EMPTY_CONSTRAINT_LIST));

		c1.addAllSmart(mkList(new IConstraint[] { ra_h, new LEQConstraint(par1a, new ConstraintLocal(la)), l_h, p0a_p1a, new LEQConstraint(new ConstraintLocal(lb), pc), new LEQConstraint(new ConstraintLocal(la), high) }));
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsList(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a })));
	}
	
	public static <U> boolean equalContentOfLists(List<U> list1, List<U> list2) {
		if (list1.size() != list2.size()) return false;
		for (U e : list1) {
			if (!list2.contains(e)) return false;
		}
		for (U e : list2) {
			if (!list1.contains(e)) return false;
		}
		return true;
	}

	public static <U> List<U> mkList(U[] array) {
		return new ArrayList<U>(Arrays.asList(array));
	}

}
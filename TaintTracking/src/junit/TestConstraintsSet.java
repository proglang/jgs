package junit;

import static org.junit.Assert.assertTrue;
import static junit.utils.JUnitHelper.*;
import java.util.Set;

import junit.example.Fed1;
import junit.example.Fed1.VelS;
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
import constraints.ConstraintsSet;
import constraints.IConstraint;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;

public class TestConstraintsSet {

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
	private IConstraintComponent pca = new ConstraintProgramCounterRef("a");
	private IConstraint p0a_p1a = new LEQConstraint(par0a, par1a);
	private IConstraint l_p0a = new LEQConstraint(low, par0a);
	private IConstraint ra_h = new LEQConstraint(reta, high);
	private IConstraint ra_pc = new LEQConstraint(reta, pca);
	private IConstraint p1a_ra = new LEQConstraint(par1a, reta);
	private IConstraint l_p1a = new LEQConstraint(low, par1a);
	private IConstraint l_pc = new LEQConstraint(low, pca);
	private IConstraint p0a_pc = new LEQConstraint(par0a, pca);
	private IConstraint l_h = new LEQConstraint(low, high);
	private IConstraint l_ra = new LEQConstraint(low, reta);
	private IConstraint p0a_ra = new LEQConstraint(par0a, reta);
	private IConstraint p0a_h = new LEQConstraint(par0a, high);
	private IConstraint p1a_h = new LEQConstraint(par1a, high);
	private IConstraint p1a_pc = new LEQConstraint(par1a, pca);
	private IConstraint p0a_l = new LEQConstraint(par0a, low);
	private IConstraint h_l = new LEQConstraint(high, low);

	private ConstraintsSet c1 = new ConstraintsSet(mediator);

	private Local local_a = new JimpleLocal("a", RefType.v("int"));
	private Local local_b = new JimpleLocal("b", RefType.v("foo.baz.Bar"));

	private static Set<IConstraint> EMPTY_CONSTRAINT_LIST = mkList(new IConstraint[] {});

	@Before
	public final void reset() {
		c1.clear();
	}

	@Test
	public final void testGetTransitiveClosure1() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.add(p0a_p1a);
		ConstraintsSet c2 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c2.getConstraintsSet(), mkList(new IConstraint[] { p0a_p1a })));

		c1.add(l_p0a);
		ConstraintsSet c3 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c3.getConstraintsSet(), mkList(new IConstraint[] { p0a_p1a, l_p0a, l_p1a })));

		c1.add(ra_h);
		ConstraintsSet c4 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c4.getConstraintsSet(), mkList(new IConstraint[] { p0a_p1a, l_p0a, l_p1a, ra_h })));

		c1.add(ra_pc);
		ConstraintsSet c5 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c5.getConstraintsSet(), mkList(new IConstraint[] { p0a_p1a, l_p0a, l_p1a, ra_h, ra_pc })));

		c1.add(p1a_ra);
		ConstraintsSet c6 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c6.getConstraintsSet(), mkList(new IConstraint[] { p0a_p1a, l_p0a, l_p1a, ra_h, ra_pc, p1a_ra, l_pc,
				p0a_pc, l_h, l_ra, p0a_ra, p0a_h, p1a_h, p1a_pc })));
	}

	@Test
	public final void testGetTransitiveClosure2() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.add(l_p0a);
		ConstraintsSet c2 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c2.getConstraintsSet(), mkList(new IConstraint[] { l_p0a })));

		c1.add(p0a_p1a);
		ConstraintsSet c3 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c3.getConstraintsSet(), mkList(new IConstraint[] { l_p0a, p0a_p1a, l_p1a })));

		c1.add(p1a_h);
		ConstraintsSet c4 = c1.getTransitiveClosure();
		assertTrue(equalContentOfLists(c4.getConstraintsSet(), mkList(new IConstraint[] { l_p0a, p0a_p1a, p1a_h, l_p1a, p0a_h, l_h })));
	}

	@Test
	public final void testGetInequations1() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(EMPTY_CONSTRAINT_LIST);
		assertTrue(equalContentOfLists(c1.getInequality(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { ra_h, l_h, p0a_l }));
		assertTrue(equalContentOfLists(c1.getInequality(), EMPTY_CONSTRAINT_LIST));

		Set<IConstraint> l1 = mkList(new IConstraint[] { h_l });
		c1.addAll(l1);
		assertTrue(equalContentOfLists(c1.getInequality(), l1));
	}

	@Test
	public final void testGetInequations2() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(EMPTY_CONSTRAINT_LIST);
		assertTrue(equalContentOfLists(c1.getInequality(), EMPTY_CONSTRAINT_LIST));

		Set<IConstraint> l1 = mkList(new IConstraint[] { h_l });
		c1.addAll(mkList(new IConstraint[] { h_l, ra_h, l_h, p0a_l }));
		assertTrue(equalContentOfLists(c1.getInequality(), l1));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor1() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(EMPTY_CONSTRAINT_LIST);
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor2() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		Set<IConstraint> l1 = mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc });
		c1.addAll(l1);
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), l1));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor3() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { p1a_h, new LEQConstraint(low, retb), ra_h, l_h, l_pc }));
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc })));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor4() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { p1a_h, ra_h, new LEQConstraint(par1b, high), l_h, l_pc }));
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc })));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor5() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { p1a_h, ra_h, new LEQConstraint(par1b, high), l_h, l_pc, new LEQConstraint(par1a, par0b) }));
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc })));
	}

	@Test
	public final void testRemoveConstraintsContainingReferencesFor6() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { p1a_h, ra_h, new LEQConstraint(retb, par1b), new LEQConstraint(par1b, high), l_h, l_pc,
				new LEQConstraint(par1a, par0b) }));
		c1.removeConstraintsContainingReferencesFor("b");
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { p1a_h, ra_h, l_h, l_pc })));
	}

	@Test
	public final void testRemoveConstraintsContaining1() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(EMPTY_CONSTRAINT_LIST);
		c1.removeConstraintsContaining(local_a);
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));
	}

	@Test
	public final void testRemoveConstraintsContaining2() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		Set<IConstraint> l1 = mkList(new IConstraint[] { ra_h, l_h, p0a_p1a });
		c1.addAll(l1);
		c1.removeConstraintsContaining(local_a);
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), l1));
	}

	@Test
	public final void testRemoveConstraintsContaining3() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		Set<IConstraint> l1 = mkList(new IConstraint[] { ra_h, new LEQConstraint(par1b, new ConstraintLocal(local_b)), l_h, p0a_p1a });
		c1.addAll(l1);
		c1.removeConstraintsContaining(local_a);
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), l1));
	}

	@Test
	public final void testRemoveConstraintsContaining4() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { ra_h, new LEQConstraint(par1a, new ConstraintLocal(local_a)), l_h, p0a_p1a,
				new LEQConstraint(new ConstraintLocal(local_b), pca) }));
		c1.removeConstraintsContaining(local_a);
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a,
				new LEQConstraint(new ConstraintLocal(local_b), pca) })));
	}

	@Test
	public final void testRemoveConstraintsContaining5() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { ra_h, new LEQConstraint(par1b, new ConstraintLocal(local_a)), l_h, p0a_p1a }));
		c1.removeConstraintsContaining(local_a);
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a })));
	}

	@Test
	public final void testRemoveConstraintsContaining6() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { ra_h, new LEQConstraint(new ConstraintLocal(local_a), par1b), l_h, p0a_p1a,
				new LEQConstraint(new ConstraintLocal(local_a), high) }));
		c1.removeConstraintsContaining(local_a);
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a })));
	}

	@Test
	public final void testRemoveConstraintsContainingLocal1() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(EMPTY_CONSTRAINT_LIST);
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));
	}

	@Test
	public final void testRemoveConstraintsContainingLocal2() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		Set<IConstraint> l1 = mkList(new IConstraint[] { ra_h, l_h, p0a_p1a });
		c1.addAll(l1);
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), l1));
	}

	@Test
	public final void testRemoveConstraintsContainingLocal3() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { ra_h, new LEQConstraint(par0b, new ConstraintLocal(local_b)), l_h, p0a_p1a,
				new LEQConstraint(new ConstraintLocal(local_b), pca) }));
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a, new LEQConstraint(par0b, pca) })));
	}

	@Test
	public final void testRemoveConstraintsContainingLocal4() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { ra_h, new LEQConstraint(par1a, new ConstraintLocal(local_a)), l_h, p0a_p1a,
				new LEQConstraint(new ConstraintLocal(local_b), pca) }));
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a })));
	}

	@Test
	public final void testRemoveConstraintsContainingLocal5() {
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), EMPTY_CONSTRAINT_LIST));

		c1.addAll(mkList(new IConstraint[] { ra_h, new LEQConstraint(par0b, new ConstraintLocal(local_a)), l_h, p0a_p1a,
				new LEQConstraint(new ConstraintLocal(local_b), pca), new LEQConstraint(new ConstraintLocal(local_a), high) }));
		c1.removeConstraintsContainingLocal();
		assertTrue(equalContentOfLists(c1.getConstraintsSet(), mkList(new IConstraint[] { ra_h, l_h, p0a_p1a, new LEQConstraint(par0b, high) })));
	}

}
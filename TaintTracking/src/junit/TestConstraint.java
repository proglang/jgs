package junit;

import static constraints.ConstraintsUtils.containsSetParameterReference;
import static constraints.ConstraintsUtils.containsSetParameterReferenceFor;
import static constraints.ConstraintsUtils.containsSetProgramCounterReference;
import static constraints.ConstraintsUtils.containsSetReturnReference;
import static constraints.ConstraintsUtils.containsSetReturnReferenceFor;
import static constraints.ConstraintsUtils.getContainedLevelsOfSet;
import static constraints.ConstraintsUtils.getInvalidParameterReferencesOfSet;
import static constraints.ConstraintsUtils.getInvalidReturnReferencesOfSet;
import static constraints.ConstraintsUtils.isLevel;
import static constraints.ConstraintsUtils.isLocal;
import static constraints.ConstraintsUtils.isParameterReference;
import static constraints.ConstraintsUtils.isProgramCounterReference;
import static constraints.ConstraintsUtils.isReturnReference;
import static junit.utils.JUnitHelper.equalContentOfLists;
import static junit.utils.JUnitHelper.mkList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.example.Fed1.VelS;

import org.junit.Test;

import security.ILevel;
import soot.Local;
import soot.RefType;
import soot.jimple.internal.JimpleLocal;
import constraints.ComponentLocal;
import constraints.ComponentParameterRef;
import constraints.ComponentProgramCounterRef;
import constraints.ComponentReturnRef;
import constraints.LEQConstraint;

public class TestConstraint {

	private VelS low = new VelS("low");
	private VelS high = new VelS("high");
	private ComponentParameterRef par0a = new ComponentParameterRef(0, "a");
	private ComponentParameterRef par1a = new ComponentParameterRef(1, "a");
	private ComponentReturnRef reta = new ComponentReturnRef("a");
	private ComponentProgramCounterRef pca = new ComponentProgramCounterRef("a");
	private LEQConstraint p0a_p1a = new LEQConstraint(par0a, par1a);
	private LEQConstraint l_p0a = new LEQConstraint(low, par0a);
	private LEQConstraint ra_h = new LEQConstraint(reta, high);
	private LEQConstraint ra_pc = new LEQConstraint(reta, pca);
	private LEQConstraint p1a_ra = new LEQConstraint(par1a, reta);
	private LEQConstraint l_p1a = new LEQConstraint(low, par1a);
	private LEQConstraint l_pc = new LEQConstraint(low, pca);
	private LEQConstraint p0a_pc = new LEQConstraint(par0a, pca);
	private LEQConstraint l_h = new LEQConstraint(low, high);
	private LEQConstraint l_ra = new LEQConstraint(low, reta);
	private LEQConstraint p0a_ra = new LEQConstraint(par0a, reta);
	private LEQConstraint p0a_h = new LEQConstraint(par0a, high);
	private LEQConstraint p1a_h = new LEQConstraint(par1a, high);
	private LEQConstraint p1a_pc = new LEQConstraint(par1a, pca);
	private LEQConstraint p0a_l = new LEQConstraint(par0a, low);
	private LEQConstraint h_l = new LEQConstraint(high, low);
	private Local local_a = new JimpleLocal("a", RefType.v("int"));
	private Local local_b = new JimpleLocal("b", RefType.v("foo.baz.Bar"));
	private ComponentLocal loca = new ComponentLocal(local_a);
	private ComponentLocal locb = new ComponentLocal(local_b);

	@Test
	public final void testIsLevel() {
		assertTrue(isLevel(low));
		assertTrue(isLevel(high));
		assertFalse(isLevel(par0a));
		assertFalse(isLevel(pca));
		assertFalse(isLevel(reta));
		assertFalse(isLevel(loca));
	}

	@Test
	public final void testIsParameterReference1() {
		assertTrue(isParameterReference(par0a));
		assertTrue(isParameterReference(par1a));
		assertFalse(isParameterReference(low));
		assertFalse(isParameterReference(pca));
		assertFalse(isParameterReference(reta));
		assertFalse(isParameterReference(loca));
	}

	@Test
	public final void testIsParameterReference2() {
		assertTrue(isParameterReference(par0a, "a"));
		assertTrue(isParameterReference(par1a, "a"));
		assertFalse(isParameterReference(par0a, "b"));
		assertFalse(isParameterReference(par1a, "b"));
		assertFalse(isParameterReference(low, "a"));
		assertFalse(isParameterReference(pca, "a"));
		assertFalse(isParameterReference(reta, "a"));
		assertFalse(isParameterReference(loca, "a"));
	}

	@Test
	public final void testIsParameterReference3() {
		assertTrue(isParameterReference(par0a, "a", 0));
		assertTrue(isParameterReference(par1a, "a", 1));
		assertFalse(isParameterReference(par0a, "a", 1));
		assertFalse(isParameterReference(par1a, "b", 1));
		assertFalse(isParameterReference(par1a, "b", 0));
		assertFalse(isParameterReference(low, "a", 0));
		assertFalse(isParameterReference(pca, "a", 0));
		assertFalse(isParameterReference(reta, "a", 0));
		assertFalse(isParameterReference(loca, "a", 0));
	}

	@Test
	public final void testIsProgramCounterReference1() {
		assertTrue(isProgramCounterReference(pca));
		assertFalse(isProgramCounterReference(low));
		assertFalse(isProgramCounterReference(par0a));
		assertFalse(isProgramCounterReference(reta));
		assertFalse(isProgramCounterReference(loca));
	}

	@Test
	public final void testIsProgramCounterReference2() {
		assertTrue(isProgramCounterReference(pca, "a"));
		assertFalse(isProgramCounterReference(pca, "b"));
		assertFalse(isProgramCounterReference(low, "a"));
		assertFalse(isProgramCounterReference(par0a, "a"));
		assertFalse(isProgramCounterReference(reta, "a"));
		assertFalse(isProgramCounterReference(loca, "a"));
	}

	@Test
	public final void testIsReturnReference1() {
		assertTrue(isReturnReference(reta));
		assertFalse(isReturnReference(low));
		assertFalse(isReturnReference(par0a));
		assertFalse(isReturnReference(pca));
		assertFalse(isReturnReference(loca));
	}

	@Test
	public final void testIsReturnReference2() {
		assertTrue(isReturnReference(reta, "a"));
		assertFalse(isReturnReference(reta, "b"));
		assertFalse(isReturnReference(low, "a"));
		assertFalse(isReturnReference(par0a, "a"));
		assertFalse(isReturnReference(pca, "a"));
		assertFalse(isReturnReference(loca, "a"));
	}

	@Test
	public final void testIsLocal() {
		assertTrue(isLocal(loca));
		assertTrue(isLocal(locb));
		assertFalse(isLocal(par0a));
		assertFalse(isLocal(pca));
		assertFalse(isLocal(reta));
		assertFalse(isLocal(low));
	}

	@Test
	public final void testContainsSetParameterReference() {
		assertTrue(containsSetParameterReference(mkList(new LEQConstraint[] { p0a_h })));
		assertTrue(containsSetParameterReference(mkList(new LEQConstraint[] { p0a_h, l_pc })));
		assertTrue(containsSetParameterReference(mkList(new LEQConstraint[] { l_pc, p1a_h })));
		assertFalse(containsSetParameterReference(mkList(new LEQConstraint[] {})));
		assertFalse(containsSetParameterReference(mkList(new LEQConstraint[] { l_pc, ra_pc, l_ra })));
	}

	@Test
	public final void testContainsSetParameterReferenceFor() {
		assertTrue(containsSetParameterReferenceFor(mkList(new LEQConstraint[] { p0a_h }), "a", 0));
		assertTrue(containsSetParameterReferenceFor(mkList(new LEQConstraint[] { p0a_h, l_pc }), "a", 0));
		assertTrue(containsSetParameterReferenceFor(mkList(new LEQConstraint[] { l_pc, p1a_h }), "a", 1));
		assertFalse(containsSetParameterReferenceFor(mkList(new LEQConstraint[] { p0a_h }), "b", 0));
		assertFalse(containsSetParameterReferenceFor(mkList(new LEQConstraint[] { p0a_h, l_pc }), "a", 1));
		assertFalse(containsSetParameterReferenceFor(mkList(new LEQConstraint[] { l_pc, p1a_h }), "b", 0));
		assertFalse(containsSetParameterReferenceFor(mkList(new LEQConstraint[] {}), "a", 0));
		assertFalse(containsSetParameterReferenceFor(mkList(new LEQConstraint[] { l_pc, ra_pc, l_ra }), "a", 0));
	}

	@Test
	public final void testContainsSetProgramCounterReference() {
		assertTrue(containsSetProgramCounterReference(mkList(new LEQConstraint[] { p0a_pc })));
		assertTrue(containsSetProgramCounterReference(mkList(new LEQConstraint[] { p0a_h, l_pc })));
		assertTrue(containsSetProgramCounterReference(mkList(new LEQConstraint[] { l_pc, p1a_h })));
		assertFalse(containsSetProgramCounterReference(mkList(new LEQConstraint[] {})));
		assertFalse(containsSetProgramCounterReference(mkList(new LEQConstraint[] { p1a_ra, l_p1a, l_ra })));
	}

	@Test
	public final void testContainsSetReturnReference() {
		assertTrue(containsSetReturnReference(mkList(new LEQConstraint[] { ra_h })));
		assertTrue(containsSetReturnReference(mkList(new LEQConstraint[] { p0a_h, ra_pc })));
		assertTrue(containsSetReturnReference(mkList(new LEQConstraint[] { p0a_ra, p1a_h })));
		assertFalse(containsSetReturnReference(mkList(new LEQConstraint[] {})));
		assertFalse(containsSetReturnReference(mkList(new LEQConstraint[] { p1a_h, l_p1a, l_pc })));
	}

	@Test
	public final void testContainsSetReturnReferenceFor() {
		assertTrue(containsSetReturnReferenceFor(mkList(new LEQConstraint[] { ra_h }), "a"));
		assertTrue(containsSetReturnReferenceFor(mkList(new LEQConstraint[] { ra_pc, h_l }), "a"));
		assertTrue(containsSetReturnReferenceFor(mkList(new LEQConstraint[] { p1a_pc, p0a_ra }), "a"));
		assertFalse(containsSetReturnReferenceFor(mkList(new LEQConstraint[] { ra_h }), "b"));
		assertFalse(containsSetReturnReferenceFor(mkList(new LEQConstraint[] { p0a_h, ra_h }), "b"));
		assertFalse(containsSetReturnReferenceFor(mkList(new LEQConstraint[] { ra_h, p1a_h }), "b"));
		assertFalse(containsSetReturnReferenceFor(mkList(new LEQConstraint[] {}), "a"));
		assertFalse(containsSetReturnReferenceFor(mkList(new LEQConstraint[] { l_pc, l_p0a, p0a_p1a }), "a"));
	}

	@Test
	public final void testGetContainedLevelsOfSet() {
		assertTrue(equalContentOfLists(getContainedLevelsOfSet(mkList(new LEQConstraint[] {})), mkList(new ILevel[] {})));
		assertTrue(equalContentOfLists(getContainedLevelsOfSet(mkList(new LEQConstraint[] { ra_pc })), mkList(new ILevel[] {})));
		assertTrue(equalContentOfLists(getContainedLevelsOfSet(mkList(new LEQConstraint[] { ra_h })), mkList(new ILevel[] { high })));
		assertTrue(equalContentOfLists(getContainedLevelsOfSet(mkList(new LEQConstraint[] { ra_h, p1a_h })), mkList(new ILevel[] { high })));
		assertTrue(equalContentOfLists(getContainedLevelsOfSet(mkList(new LEQConstraint[] { ra_h, p1a_h, p0a_l })), mkList(new ILevel[] { high,
				low })));
		assertTrue(equalContentOfLists(getContainedLevelsOfSet(mkList(new LEQConstraint[] { p1a_pc, p0a_p1a, ra_pc })), mkList(new ILevel[] {})));
	}

	@Test
	public final void testGetInvalidParameterReferencesOfSet() {
		assertTrue(equalContentOfLists(getInvalidParameterReferencesOfSet(mkList(new LEQConstraint[] {}), "a", 0),
				mkList(new ComponentParameterRef[] {})));
		assertTrue(equalContentOfLists(getInvalidParameterReferencesOfSet(mkList(new LEQConstraint[] { p1a_h }), "a", 1),
				mkList(new ComponentParameterRef[] { par1a })));
		assertTrue(equalContentOfLists(getInvalidParameterReferencesOfSet(mkList(new LEQConstraint[] { p1a_h }), "a", 2),
				mkList(new ComponentParameterRef[] {})));
		assertTrue(equalContentOfLists(getInvalidParameterReferencesOfSet(mkList(new LEQConstraint[] { ra_h, p1a_h }), "b", 1),
				mkList(new ComponentParameterRef[] { par1a })));
		assertTrue(equalContentOfLists(getInvalidParameterReferencesOfSet(mkList(new LEQConstraint[] { p1a_h, p0a_l }), "a", 2),
				mkList(new ComponentParameterRef[] {})));
		assertTrue(equalContentOfLists(getInvalidParameterReferencesOfSet(mkList(new LEQConstraint[] { p1a_pc, p0a_p1a, ra_pc }), "a", 2),
				mkList(new ComponentParameterRef[] {})));
	}

	@Test
	public final void testGetInvalidReturnReferencesOfSet() {
		assertTrue(equalContentOfLists(getInvalidReturnReferencesOfSet(mkList(new LEQConstraint[] {}), "a"), mkList(new ComponentReturnRef[] {})));
		assertTrue(equalContentOfLists(getInvalidReturnReferencesOfSet(mkList(new LEQConstraint[] { ra_pc }), "a"),
				mkList(new ComponentReturnRef[] {})));
		assertTrue(equalContentOfLists(getInvalidReturnReferencesOfSet(mkList(new LEQConstraint[] { ra_pc }), "b"),
				mkList(new ComponentReturnRef[] { reta })));
		assertTrue(equalContentOfLists(getInvalidReturnReferencesOfSet(mkList(new LEQConstraint[] { ra_h, p1a_h }), "a"),
				mkList(new ComponentReturnRef[] {})));
		assertTrue(equalContentOfLists(getInvalidReturnReferencesOfSet(mkList(new LEQConstraint[] { p1a_h, ra_h }), "a"),
				mkList(new ComponentReturnRef[] {})));
		assertTrue(equalContentOfLists(getInvalidReturnReferencesOfSet(mkList(new LEQConstraint[] { p1a_pc, p0a_p1a, ra_pc }), "b"),
				mkList(new ComponentReturnRef[] { reta })));
	}

	@Test
	public final void testGetLhs() {
		LEQConstraint cons1 = new LEQConstraint(high, reta);
		assertTrue(cons1.getLhs().equals(high));
		assertFalse(cons1.getLhs().equals(reta));
		LEQConstraint cons2 = new LEQConstraint(reta, par0a);
		assertTrue(cons2.getLhs().equals(reta));
		assertFalse(cons2.getLhs().equals(par0a));
		LEQConstraint cons3 = new LEQConstraint(par0a, high);
		assertTrue(cons3.getLhs().equals(par0a));
		assertFalse(cons3.getLhs().equals(high));
	}

	@Test
	public final void testGetRhs() {
		LEQConstraint cons1 = new LEQConstraint(reta, high);
		assertTrue(cons1.getRhs().equals(high));
		assertFalse(cons1.getRhs().equals(reta));
		LEQConstraint cons2 = new LEQConstraint(par0a, reta);
		assertTrue(cons2.getRhs().equals(reta));
		assertFalse(cons2.getRhs().equals(par0a));
		LEQConstraint cons3 = new LEQConstraint(high, par0a);
		assertTrue(cons3.getRhs().equals(par0a));
		assertFalse(cons3.getRhs().equals(high));
	}

	@Test
	public final void testContainsLevel() {
		assertTrue(equalContentOfLists(p0a_h.getContainedLevel(), mkList(new ILevel[] { high })));
		assertTrue(equalContentOfLists(l_h.getContainedLevel(), mkList(new ILevel[] { high, low })));
		assertTrue(equalContentOfLists(l_p0a.getContainedLevel(), mkList(new ILevel[] { low })));
		assertTrue(equalContentOfLists(p0a_p1a.getContainedLevel(), mkList(new ILevel[] {})));
		assertTrue(equalContentOfLists(ra_pc.getContainedLevel(), mkList(new ILevel[] {})));
	}

	@Test
	public final void testContainsParameterReference() {
		assertTrue(p0a_h.containsParameterReference());
		assertTrue(p0a_p1a.containsParameterReference());
		assertTrue(l_p1a.containsParameterReference());
		assertFalse(l_h.containsParameterReference());
		assertFalse(ra_pc.containsParameterReference());
		assertFalse(l_ra.containsParameterReference());
	}

	@Test
	public final void testContainsParameterReferenceFor1() {
		assertTrue(p0a_h.containsParameterReferenceFor("a"));
		assertTrue(p0a_p1a.containsParameterReferenceFor("a"));
		assertTrue(l_p1a.containsParameterReferenceFor("a"));
		assertFalse(p0a_h.containsParameterReferenceFor("b"));
		assertFalse(p0a_p1a.containsParameterReferenceFor("b"));
		assertFalse(l_p1a.containsParameterReferenceFor("b"));
		assertFalse(l_h.containsParameterReferenceFor("a"));
		assertFalse(ra_pc.containsParameterReferenceFor("a"));
		assertFalse(l_ra.containsParameterReferenceFor("a"));
	}

	@Test
	public final void testContainsParameterReferenceFor2() {
		assertTrue(p0a_h.containsParameterReferenceFor("a", 0));
		assertTrue(p0a_p1a.containsParameterReferenceFor("a", 1));
		assertTrue(l_p1a.containsParameterReferenceFor("a", 1));
		assertFalse(p0a_h.containsParameterReferenceFor("a", 1));
		assertFalse(p0a_p1a.containsParameterReferenceFor("a", 2));
		assertFalse(l_p1a.containsParameterReferenceFor("a", 0));
		assertFalse(p0a_h.containsParameterReferenceFor("b", 0));
		assertFalse(p0a_p1a.containsParameterReferenceFor("b", 0));
		assertFalse(l_p1a.containsParameterReferenceFor("b", 0));
		assertFalse(l_h.containsParameterReferenceFor("a", 0));
		assertFalse(ra_pc.containsParameterReferenceFor("a", 0));
		assertFalse(l_ra.containsParameterReferenceFor("a", 0));
	}

	@Test
	public final void testContainsProgramCounterReference() {
		assertTrue(p1a_pc.containsProgramCounterReference());
		assertTrue(new LEQConstraint(pca, locb).containsProgramCounterReference());
		assertFalse(l_ra.containsProgramCounterReference());
		assertFalse(p1a_ra.containsProgramCounterReference());
		assertFalse(p0a_l.containsProgramCounterReference());
	}

	@Test
	public final void testContainsProgramCounterReferenceFor() {
		assertTrue(p1a_pc.containsProgramCounterReferenceFor("a"));
		assertTrue(new LEQConstraint(pca, locb).containsProgramCounterReferenceFor("a"));
		assertFalse(p1a_pc.containsProgramCounterReferenceFor("b"));
		assertFalse(new LEQConstraint(pca, locb).containsProgramCounterReferenceFor("b"));
		assertFalse(l_ra.containsProgramCounterReferenceFor("a"));
		assertFalse(p1a_ra.containsProgramCounterReferenceFor("a"));
		assertFalse(p0a_l.containsProgramCounterReferenceFor("a"));
	}

	@Test
	public final void testContainsReturnReference() {
		assertTrue(p0a_ra.containsReturnReference());
		assertTrue(ra_pc.containsReturnReference());
		assertFalse(p0a_p1a.containsReturnReference());
		assertFalse(l_h.containsReturnReference());
		assertFalse(p1a_pc.containsReturnReference());
	}

	@Test
	public final void testContainsReturnReferenceFor() {
		assertTrue(p0a_ra.containsReturnReferenceFor("a"));
		assertTrue(ra_pc.containsReturnReferenceFor("a"));
		assertFalse(p0a_ra.containsReturnReferenceFor("b"));
		assertFalse(ra_pc.containsReturnReferenceFor("b"));
		assertFalse(p0a_p1a.containsReturnReferenceFor("a"));
		assertFalse(l_h.containsReturnReferenceFor("a"));
		assertFalse(p1a_pc.containsReturnReferenceFor("a"));
	}

	@Test
	public final void testContainsLocal() {
		assertTrue(new LEQConstraint(locb, par1a).containsLocal());
		assertTrue(new LEQConstraint(pca, loca).containsLocal());
		assertFalse(p0a_p1a.containsLocal());
		assertFalse(l_h.containsLocal());
		assertFalse(p1a_pc.containsLocal());
	}

	@Test
	public final void testContainsComponent() {
		assertTrue(new LEQConstraint(locb, par1a).containsComponent(locb));
		assertTrue(p0a_p1a.containsComponent(par1a));
		assertFalse(p0a_p1a.containsComponent(pca));
		assertFalse(l_h.containsComponent(reta));
		assertFalse(p1a_pc.containsComponent(par0a));
	}

	@Test
	public final void testGetInvalidParameterReferencesFor() {
		assertTrue(equalContentOfLists(p0a_h.getInvalidParameterReferencesFor("a", 0), mkList(new ComponentParameterRef[] { par0a })));
		assertTrue(equalContentOfLists(p0a_h.getInvalidParameterReferencesFor("a", 1), mkList(new ComponentParameterRef[] {})));
		assertTrue(equalContentOfLists(p0a_p1a.getInvalidParameterReferencesFor("a", 0), mkList(new ComponentParameterRef[] { par0a, par1a })));
		assertTrue(equalContentOfLists(p0a_p1a.getInvalidParameterReferencesFor("a", 1), mkList(new ComponentParameterRef[] { par1a })));
		assertTrue(equalContentOfLists(p0a_p1a.getInvalidParameterReferencesFor("a", 2), mkList(new ComponentParameterRef[] {})));
		assertTrue(equalContentOfLists(p0a_p1a.getInvalidParameterReferencesFor("b", 0), mkList(new ComponentParameterRef[] { par0a, par1a })));
		assertTrue(equalContentOfLists(p0a_p1a.getInvalidParameterReferencesFor("b", 2), mkList(new ComponentParameterRef[] { par0a, par1a })));
		assertTrue(equalContentOfLists(l_h.getInvalidParameterReferencesFor("a", 3), mkList(new ComponentParameterRef[] {})));
		assertTrue(equalContentOfLists(ra_pc.getInvalidParameterReferencesFor("b", 0), mkList(new ComponentParameterRef[] {})));
	}

	@Test
	public final void testGetInvalidReturnReferencesFor() {
		assertTrue(equalContentOfLists(ra_pc.getInvalidReturnReferencesFor("a"), mkList(new ComponentReturnRef[] {})));
		assertTrue(equalContentOfLists(ra_pc.getInvalidReturnReferencesFor("b"), mkList(new ComponentReturnRef[] { reta })));
		assertTrue(equalContentOfLists(p1a_ra.getInvalidReturnReferencesFor("a"), mkList(new ComponentReturnRef[] {})));
		assertTrue(equalContentOfLists(p1a_ra.getInvalidReturnReferencesFor("b"), mkList(new ComponentReturnRef[] { reta })));
		assertTrue(equalContentOfLists(p0a_h.getInvalidReturnReferencesFor("a"), mkList(new ComponentReturnRef[] {})));
		assertTrue(equalContentOfLists(p0a_p1a.getInvalidReturnReferencesFor("a"), mkList(new ComponentReturnRef[] {})));
		assertTrue(equalContentOfLists(l_h.getInvalidReturnReferencesFor("a"), mkList(new ComponentReturnRef[] {})));
	}

}

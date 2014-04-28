package junit;

import java.util.logging.Level;

import constraints.ConstraintParameterRef;
import constraints.ConstraintReturnRef;
import constraints.LEQConstraint;

import junit.Fed1;
import junit.Fed1.VelS;

import logging.AnalysisLog;
import main.Mediator;
import security.ILevelDefinition;

public class TestCs {

	private AnalysisLog log = new AnalysisLog(false, new Level[] {});

	private ILevelDefinition def = new Fed1();
	private Mediator mediator = new Mediator(def);
	
	private VelS low = new VelS("low");
	private VelS high = new VelS("high");

	private ConstraintReturnRef returnRef = new ConstraintReturnRef("");
	private ConstraintParameterRef paramRef1 = new ConstraintParameterRef(0, "");
	private ConstraintParameterRef paramRef2 = new ConstraintParameterRef(1, "");
	private ConstraintParameterRef paramRef3 = new ConstraintParameterRef(7, "");
	private LEQConstraint l1l2 = new LEQConstraint(low, high);
	private LEQConstraint l1r = new LEQConstraint(low, returnRef);
	private LEQConstraint rr = new LEQConstraint(returnRef, returnRef);
	private LEQConstraint l1p1 = new LEQConstraint(low, paramRef1);
	private LEQConstraint p2p1 = new LEQConstraint(paramRef2, paramRef1);
	private LEQConstraint rp2 = new LEQConstraint(returnRef, paramRef2);
	private LEQConstraint p3l1 = new LEQConstraint(paramRef3, low);

	
	
	//
	// @Test
	// public final void testContains() {
	// Constraints cons1 = new Constraints();
	// assertFalse("Empty constraints does not contain anything", cons1.contains(p2p1));
	// assertFalse("Empty constraints does not contain anything", cons1.contains(rr));
	// assertFalse("Empty constraints does not contain anything", cons1.contains(l1p1));
	// Constraints cons2 = new Constraints();
	// cons2.add(p2p1);
	// assertTrue("Constraints does contain constraint [p2p1]", cons2.contains(p2p1));
	// assertFalse("Constraints does not contain constraint [p2p1]", cons2.contains(rr));
	// assertFalse("Constraints does not contain constraint [p2p1]", cons2.contains(l1p1));
	// assertFalse("Constraints does not contain constraint [p2p1]", cons2.contains(l1r));
	// assertTrue("Constraints does contain constraint [p2p1]", cons2.contains(new LEQConstraint(paramRef2, paramRef1)));
	// Constraints cons3 = new Constraints();
	// cons3.add(p2p1);
	// cons3.add(rr);
	// cons3.add(l1r);
	// assertTrue("Constraints does contain constraint [p2p1,rr,l1r]", cons3.contains(p2p1));
	// assertTrue("Constraints does not contain constraint [p2p1,rr,l1r]", cons3.contains(rr));
	// assertFalse("Constraints does not contain constraint [p2p1,rr,l1r]", cons3.contains(l1p1));
	// assertTrue("Constraints does not contain constraint [p2p1,rr,l1r]", cons3.contains(l1r));
	// }
	//
	// @Test
	// public final void testContainsParameterRef() {
	// Constraints cons1 = new Constraints();
	// assertTrue("Empty constraints object does not contain a constraint which contains parameter references",
	// !cons1.containsParameterRef());
	// Constra
	// ints cons2 = new Constraints();
	// cons2.add(l1l2);
	// assertTrue("Constraints object without constraints which contains parameter references", !cons2.containsParameterRef());
	// Constraints cons3 = new Constraints();
	// cons3.add(l1l2);
	// cons3.add(rr);
	// assertTrue("Constraints object without constraints which contains parameter references", !cons3.containsParameterRef());
	// Constraints cons4 = new Constraints();
	// cons4.add(l1l2);
	// cons4.add(rp2);
	// cons4.add(rr);
	// assertTrue("Constraints object with 1 constraint containing parameter references", cons4.containsParameterRef());
	// Constraints cons5 = new Constraints();
	// cons5.add(p3l1);
	// cons5.add(l1l2);
	// cons5.add(rr);
	// cons5.add(rp2);
	// assertTrue("Constraints object with 2 constraint containing parameter references", cons5.containsParameterRef());
	// }
	//
	// @Test
	// public final void testContainsReturnRef() {
	// Constraints cons1 = new Constraints();
	// assertTrue("Empty constraints object does not contain a constraint which contains return references", !cons1.containsReturnRef());
	// Constraints cons2 = new Constraints();
	// cons2.add(l1l2);
	// assertTrue("Constraints object without constraints which contains return references", !cons2.containsReturnRef());
	// Constraints cons3 = new Constraints();
	// cons3.add(l1l2);
	// cons3.add(l1p1);
	// assertTrue("Constraints object without constraints which contains return references", !cons3.containsReturnRef());
	// Constraints cons4 = new Constraints();
	// cons4.add(l1l2);
	// cons4.add(rp2);
	// cons4.add(l1p1);
	// assertTrue("Constraints object with 1 constraint containing return references", cons4.containsReturnRef());
	// Constraints cons5 = new Constraints();
	// cons5.add(p3l1);
	// cons5.add(l1l2);
	// cons5.add(l1r);
	// cons5.add(rp2);
	// assertTrue("Constraints object with 2 constraint containing return references", cons5.containsReturnRef());
	// }
	//
	// @Test
	// public final void testGetConstraints() {
	// Constraints cons1 = new Constraints();
	// assertTrue("Empty constraints set for the empty constraints object", cons1.getConstraints().equals(new ArrayList<IConstraint>()));
	// Constraints cons2 = new Constraints();
	// cons2.add(l1l2);
	// assertTrue("Constraints object with 1 constarint",
	// cons2.getConstraints().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] { l1l2 }))));
	// cons2.add(l1l2);
	// assertTrue("Constraints object with 1 constarint (2 times added)",
	// cons2.getConstraints().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] { l1l2 }))));
	// Constraints cons3 = new Constraints();
	// cons3.add(l1l2);
	// cons3.add(rp2);
	// cons3.add(p3l1);
	// assertTrue("Constraints object with 3 constarint",
	// cons3.getConstraints().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] { l1l2, rp2, p3l1 }))));
	// }
	//
	// @Test
	// public final void testGetConstraintsIncludingParameter() {
	// Constraints cons1 = new Constraints();
	// assertTrue("Empty constraints object without constraints which contains parameter references", cons1.getConstraintsIncludingParameter()
	// .equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] {}))));
	// Constraints cons2 = new Constraints();
	// cons2.add(l1l2);
	// assertTrue("Constraints object without constraints which contains parameter references", cons2.getConstraintsIncludingParameter()
	// .equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] {}))));
	// Constraints cons3 = new Constraints();
	// cons3.add(l1l2);
	// cons3.add(l1p1);
	// assertTrue("Constraints object with 1 constraint containing parameter references",
	// cons3.getConstraintsIncludingParameter().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] { l1p1 }))));
	// Constraints cons4 = new Constraints();
	// cons4.add(l1l2);
	// cons4.add(l1p1);
	// cons4.add(l1p1);
	// assertTrue("Constraints object with 1 constraint containing parameter references",
	// cons4.getConstraintsIncludingParameter().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] { l1p1 }))));
	// Constraints cons5 = new Constraints();
	// cons5.add(p3l1);
	// cons5.add(l1l2);
	// cons5.add(l1p1);
	// cons5.add(p2p1);
	// assertTrue("Constraints object with 3 constraint containing parameter references",
	// cons5.getConstraintsIncludingParameter().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] { p3l1, l1p1, p2p1 }))));
	// }
	//
	// @Test
	// public final void testGetConstraintsIncludingReturn() {
	// Constraints cons1 = new Constraints();
	// assertTrue("Empty constraints object without constraints which contains return references", cons1.getConstraintsIncludingReturn()
	// .equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] {}))));
	// Constraints cons2 = new Constraints();
	// cons2.add(l1l2);
	// assertTrue("Constraints object without constraints which contains return references",
	// cons2.getConstraintsIncludingReturn().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] {}))));
	// Constraints cons3 = new Constraints();
	// cons3.add(l1l2);
	// cons3.add(rp2);
	// assertTrue("Constraints object with 1 constraint containing return references",
	// cons3.getConstraintsIncludingReturn().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] { rp2 }))));
	// Constraints cons4 = new Constraints();
	// cons4.add(rp2);
	// cons4.add(rp2);
	// cons4.add(l1p1);
	// assertTrue("Constraints object with 1 constraint containing return references",
	// cons4.getConstraintsIncludingReturn().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] { rp2 }))));
	// Constraints cons5 = new Constraints();
	// cons5.add(rp2);
	// cons5.add(l1l2);
	// cons5.add(l1r);
	// cons5.add(p2p1);
	// assertTrue("Constraints object with 2 constraint containing return references",
	// cons5.getConstraintsIncludingReturn().equals(new ArrayList<IConstraint>(Arrays.asList(new IConstraint[] { rp2, l1r }))));
	// }
	//
	// @Test
	// public final void testGetContainedLevels() {
	// Constraints cons1 = new Constraints();
	// assertTrue("Empty constraints object without constraints which contains levels",
	// cons1.getContainedLevels().equals(new ArrayList<ILevel>(Arrays.asList(new ILevel[] {}))));
	// Constraints cons2 = new Constraints();
	// cons2.add(rr);
	// assertTrue("Constraints object without constraints which contains levels",
	// cons2.getContainedLevels().equals(new ArrayList<ILevel>(Arrays.asList(new ILevel[] {}))));
	// Constraints cons3 = new Constraints();
	// cons3.add(l1l2);
	// cons3.add(rp2);
	// assertTrue("Constraints object with 1 constraint containing levels",
	// cons3.getContainedLevels().equals(new ArrayList<ILevel>(Arrays.asList(new ILevel[] { level1, level2 }))));
	// Constraints cons4 = new Constraints();
	// cons4.add(rp2);
	// cons4.add(l1p1);
	// assertTrue("Constraints object with 1 constraint containing levels",
	// cons4.getContainedLevels().equals(new ArrayList<ILevel>(Arrays.asList(new ILevel[] { level1 }))));
	// Constraints cons5 = new Constraints();
	// cons5.add(rp2);
	// cons5.add(l1l2);
	// cons5.add(l1r);
	// cons5.add(p2p1);
	// assertTrue("Constraints object with 2 constraint containing levels",
	// cons5.getContainedLevels().equals(new ArrayList<ILevel>(Arrays.asList(new ILevel[] { level1, level2 }))));
	// }
	//
	// @Test
	// public final void testGetContainedParameterRefs() {
	// Constraints cons1 = new Constraints();
	// assertTrue("Empty constraints object without constraints which contains parameter references", cons1.getContainedParameterRefs()
	// .equals(new ArrayList<ConstraintParameterRef>(Arrays.asList(new ConstraintParameterRef[] {}))));
	// Constraints cons2 = new Constraints();
	// cons2.add(l1l2);
	// assertTrue("Constraints object without constraints which contains parameter references",
	// cons2.getContainedParameterRefs().equals(new ArrayList<ConstraintParameterRef>(Arrays.asList(new ConstraintParameterRef[] {}))));
	// Constraints cons3 = new Constraints();
	// cons3.add(l1l2);
	// cons3.add(rp2);
	// assertTrue(
	// "Constraints object with 1 constraint containing parameter references",
	// cons3.getContainedParameterRefs().equals(
	// new ArrayList<ConstraintParameterRef>(Arrays.asList(new ConstraintParameterRef[] { paramRef2 }))));
	// Constraints cons4 = new Constraints();
	// cons4.add(rp2);
	// cons4.add(l1p1);
	// assertTrue(
	// "Constraints object with 2 constraint containing parameter references",
	// cons4.getContainedParameterRefs().equals(
	// new ArrayList<ConstraintParameterRef>(Arrays.asList(new ConstraintParameterRef[] { paramRef2, paramRef1 }))));
	// Constraints cons5 = new Constraints();
	// cons5.add(rp2);
	// cons5.add(l1l2);
	// cons5.add(l1r);
	// cons5.add(p2p1);
	// assertTrue(
	// "Constraints object with 2 constraint containing parameter references",
	// cons5.getContainedParameterRefs().equals(
	// new ArrayList<ConstraintParameterRef>(Arrays.asList(new ConstraintParameterRef[] { paramRef2, paramRef1 }))));
	// }
	//
	// @Test
	// public final void testHighestParameterRefNumber() {
	// Constraints cons1 = new Constraints();
	// assertTrue("Highest reference for the empty constraints object is -1", cons1.highestParameterRefNumber() == -1);
	// Constraints cons2 = new Constraints();
	// cons2.add(l1l2);
	// assertTrue("Highest reference for the constraints object without reference is -1", cons2.highestParameterRefNumber() == -1);
	// Constraints cons3 = new Constraints();
	// cons3.add(rp2);
	// cons3.add(p2p1);
	// assertTrue("Highest reference for the constraints object is 1", cons3.highestParameterRefNumber() == 1);
	// Constraints cons4 = new Constraints();
	// cons4.add(rp2);
	// cons4.add(p3l1);
	// cons4.add(p2p1);
	// assertTrue("Highest reference for the constraints object is 1", cons4.highestParameterRefNumber() == 7);
	// Constraints cons5 = new Constraints();
	// cons5.add(l1p1);
	// cons5.add(rr);
	// assertTrue("Highest reference for the constraints object is 1", cons5.highestParameterRefNumber() == 0);
	// }
	//
	// @Test
	// public final void testSize() {
	// Constraints cons1 = new Constraints();
	// assertTrue("Empty constraints object with 0 constraints", cons1.size() == 0);
	// cons1.add(l1l2);
	// assertTrue("Constraints object with 1 constraints", cons1.size() == 1);
	// cons1.add(l1l2);
	// assertTrue("Constraints object with 1 constraints (2 times added)", cons1.size() == 1);
	// Constraints cons2 = new Constraints();
	// cons2.add(l1l2);
	// cons2.add(rp2);
	// cons2.add(l1r);
	// assertTrue("Constraints object with 3 constraints", cons2.size() == 3);
	// cons2.add(l1r);
	// assertTrue("Constraints object with 3 constraints (2 times added)", cons2.size() == 3);
	// }

}

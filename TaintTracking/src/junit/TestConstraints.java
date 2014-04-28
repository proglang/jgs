package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import junit.Fed1.VelS;

import logging.AnalysisLog;
import main.Mediator;

import org.junit.Test;

import security.ILevelDefinition;

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
	

	@Test
	public final void testCalculateTransitiveClosure() {
		Constraints c1 = new Constraints(mediator, log);
		IConstraintComponent par0 = new ConstraintParameterRef(0, "a");
		IConstraintComponent par1 =  new ConstraintParameterRef(1, "a");
		IConstraintComponent ret1 =  new ConstraintReturnRef("a");
		IConstraintComponent pc =  new ConstraintProgramCounterRef();
		IConstraint cons1 = new LEQConstraint(par0,par1);
		IConstraint cons2 = new LEQConstraint(low, par0);
		IConstraint cons3 = new LEQConstraint(ret1, high);
		IConstraint cons4 = new LEQConstraint(ret1, pc);
		IConstraint cons5 = new LEQConstraint(par1, ret1);
		assertTrue(equalContentOfLists(c1.getConstraints(), mkList(new IConstraint[] {})));
		c1.addSmart(cons1);
		Constraints c1R = c1.calculateTransitiveClosure();
		assertTrue(equalContentOfLists(c1R.getConstraints(), mkList(new IConstraint[] {cons1})));
		c1.addSmart(cons2);
		Constraints c2R = c1.calculateTransitiveClosure();
		IConstraint aCons1 = new LEQConstraint(low, par1);
		assertTrue(equalContentOfLists(c2R.getConstraints(), mkList(new IConstraint[] {cons1, cons2, aCons1})));
		c1.addSmart(cons3);
		Constraints c3R = c1.calculateTransitiveClosure();
		assertTrue(equalContentOfLists(c3R.getConstraints(), mkList(new IConstraint[] {cons1, cons2, aCons1, cons3})));
		c1.addSmart(cons4);
		Constraints c4R = c1.calculateTransitiveClosure();
		assertTrue(equalContentOfLists(c4R.getConstraints(), mkList(new IConstraint[] {cons1, cons2, aCons1, cons3, cons4})));
		c1.addSmart(cons5);
		Constraints c5R = c1.calculateTransitiveClosure();
		IConstraint aCons2 = new LEQConstraint(low, pc);
		IConstraint aCons3 = new LEQConstraint(par0, pc);
		IConstraint aCons4 = new LEQConstraint(low, high);
		IConstraint aCons5 = new LEQConstraint(low, ret1);
		IConstraint aCons6 = new LEQConstraint(par0, ret1);
		IConstraint aCons7 = new LEQConstraint(par0, high);
		IConstraint aCons8 = new LEQConstraint(par1, high);
		IConstraint aCons9 = new LEQConstraint(par1, pc);
		assertTrue(equalContentOfLists(c5R.getConstraints(), mkList(new IConstraint[] {cons1, cons2, aCons1, cons3, cons4, cons5, aCons2, aCons3, aCons4, aCons5, aCons6, aCons7, aCons8, aCons9})));
	
		Constraints c2 = new Constraints(mediator, log);
		IConstraint cons6 = new LEQConstraint(par0,low);
		IConstraint cons7 = new LEQConstraint(low, par0);
		IConstraint cons8 = new LEQConstraint(par0, par1);
		IConstraint cons9 = new LEQConstraint(par1, high);
		c2.addSmart(cons6);
		Constraints c6R = c2.calculateTransitiveClosure();
		assertTrue(equalContentOfLists(c6R.getConstraints(), mkList(new IConstraint[] {cons6, cons7})));
		c2.addSmart(cons8);
		Constraints c7R = c2.calculateTransitiveClosure();
		IConstraint aCons10 = new LEQConstraint(low, par1);
		assertTrue(equalContentOfLists(c7R.getConstraints(), mkList(new IConstraint[] {cons6, cons7, cons8, aCons10})));
		c2.addSmart(cons9);
		Constraints c8R = c2.calculateTransitiveClosure();
		IConstraint aCons11 = new LEQConstraint(par0, high);
		IConstraint aCons12 = new LEQConstraint(low, high);
		assertTrue(equalContentOfLists(c8R.getConstraints(), mkList(new IConstraint[] {cons6, cons7, cons8, cons9, aCons10, aCons11, aCons12})));
	}
	
	public static <U>  boolean equalContentOfLists(List<U> list1, List<U> list2) {
		if (list1.size() != list2.size()) return false;
		for (U e : list1) {
			if (! list2.contains(e)) return false;
		}
		for (U e : list2) {
			if (! list1.contains(e)) return false;
		}
		return true;
	}
	
	public static <U> List<U> mkList(U[] array) {
		return new ArrayList<U>(Arrays.asList(array));
	}

}

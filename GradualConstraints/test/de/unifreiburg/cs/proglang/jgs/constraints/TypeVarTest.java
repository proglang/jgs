package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

public class TypeVarTest {
    
    TypeVars tvars;
    
    @Before
    public void setUp() {
        tvars = new TypeVars("x");
    }

    @Test
    public void testCreate() {
        TypeVar x = tvars.fresh("x");
        TypeVar x2 = tvars.fresh("x2");
        TypeVar y = tvars.fresh("y");
        
        assertNotEquals(x, x2);
        assertNotEquals(x, y);
        assertNotEquals(x2, y);
        assertEquals(x, x);
    }
    

}

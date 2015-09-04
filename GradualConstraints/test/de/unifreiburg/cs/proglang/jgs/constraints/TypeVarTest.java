package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

public class TypeVarTest {
    
    TypeVars tvars;
    
    @Before
    public void setUp() {
        tvars = new TypeVars();
    }

    @Test
    public void testCreate() {
        TypeVar x = tvars.fresh("x");
        TypeVar x2 = tvars.fresh("x");
        TypeVar y = tvars.fresh("y");
        
        assertNotEquals(x, x2);
        assertNotEquals(x, y);
        assertNotEquals(x2, y);
        assertEquals(x, x);

        Optional<TypeVar> alsoX = tvars.tryGet("x");

        assertTrue("Cannot find `x' even though it was created", alsoX.isPresent());
        assertEquals(alsoX.get(), x);
        
        assertFalse("Found `z' even though it was not created", tvars.tryGet("z").isPresent());
    }
    

}

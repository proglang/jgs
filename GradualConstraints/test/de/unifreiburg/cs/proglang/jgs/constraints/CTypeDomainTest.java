package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LH;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static org.junit.Assert.*;

public class CTypeDomainTest {
    
    private TypeVars tvars;
    private Assignment<LH.Level> a1;
    private CTypeDomain<LH.Level> ctypes;
    private TypeVar vh1, vh2, vl, vd, vb;
    private CTypeDomain<LH.Level>.CType h1, h2;
    
    @Before
    public void setUp() {
       this.tvars = new TypeVars("x"); 
       this.ctypes = new CTypeDomain<>();
       Map<TypeVar, TypeDomain<LH.Level>.Type> m = new HashMap<>();
       vh1 = tvars.fresh();
       vh2 = tvars.fresh();
       vl = tvars.fresh();
       vd = tvars.fresh();
       vb = tvars.fresh();

       h1 = ctypes.variable(vh1);
       h2 = ctypes.variable(vh2);
       ctypes.variable(vl);
       ctypes.variable(vd);
       ctypes.variable(vb);

       m.put(vh1, THIGH);
       m.put(vh2, THIGH);
       m.put(vl, TLOW);
       m.put(vd, DYN);
       m.put(vb, PUB);
       this.a1 = new Assignment<LH.Level>(m);
    }

    @Test
    public void test() {
        
        assertEquals(h1.apply(a1), THIGH);
        assertEquals(h1, ctypes.variable(vh1));
        assertNotEquals(h1, h2);
        assertEquals(h1.apply(a1), h2.apply(a1));

    }

}

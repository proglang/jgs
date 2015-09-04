package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LH;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LH.Level;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static org.junit.Assert.*;

public class CTypeDomainTest {
    
    private TypeVars tvars;
    private Assignment<LH.Level> a1;
    private CTypeDomain<LH.Level> ctypes;
    private CTypeDomain<LH.Level>.CType h1, h2, l, d, b;
    
    @Before
    public void setUp() {
       this.tvars = new TypeVars(); 
       this.ctypes = new CTypeDomain<>();
       Map<TypeVar, TypeDomain<LH.Level>.Type> m = new HashMap<>();
       m.put(this.tvars.fresh("h1"), THIGH);
       m.put(this.tvars.fresh("h2"), THIGH);
       m.put(this.tvars.fresh("l"), TLOW);
       m.put(this.tvars.fresh("d"), DYN);
       m.put(this.tvars.fresh("b"), PUB);
       this.h1 = ctypes.variable(tvars.get("h1"));
       this.h2 = ctypes.variable(tvars.get("h2"));
       this.l = ctypes.variable(tvars.get("l"));
       this.d = ctypes.variable(tvars.get("d"));
       this.b = ctypes.variable(tvars.get("b"));
       this.a1 = new Assignment<LH.Level>(m);
       this.ctypes = new CTypeDomain<>();
    }

    @Test
    public void test() {
        
        assertEquals(h1.apply(a1), THIGH);
        assertEquals(h1, ctypes.variable(tvars.get("h1")));
        assertNotEquals(h1, h2);
        assertEquals(h1.apply(a1), h2.apply(a1));

    }

}

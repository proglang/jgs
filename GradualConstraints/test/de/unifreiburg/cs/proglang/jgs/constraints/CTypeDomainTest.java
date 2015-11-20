package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.*;

public class CTypeDomainTest {
    
    private TypeVars tvars;
    private Assignment<Level> a1;
    private TypeVar vh1, vh2, vl, vd, vb;
    private CType<Level> h1, h2;
    
    @Before
    public void setUp() {
       this.tvars = new TypeVars("x"); 
       Map<TypeVar, Type<Level>> m = new HashMap<>();
       vh1 = tvars.fresh();
       vh2 = tvars.fresh();
       vl = tvars.fresh();
       vd = tvars.fresh();
       vb = tvars.fresh();

       h1 = variable(vh1);
       h2 = variable(vh2);
       variable(vl);
       variable(vd);
       variable(vb);

       m.put(vh1, THIGH);
       m.put(vh2, THIGH);
       m.put(vl, TLOW);
       m.put(vd, DYN);
       m.put(vb, PUB);
       this.a1 = new Assignment<Level>(m);
    }

    @Test
    public void test() {
        
        assertEquals(h1.apply(a1), THIGH);
        assertEquals(h1, variable(vh1));
        assertNotEquals(h1, h2);
        assertEquals(h1.apply(a1), h2.apply(a1));

    }

}

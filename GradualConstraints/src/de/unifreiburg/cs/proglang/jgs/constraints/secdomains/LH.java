package de.unifreiburg.cs.proglang.jgs.constraints.secdomains;

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;

/**
 * The classical LOW-HIGH security lattice.
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 */
public class LH implements SecDomain<LH.Level>{
    
    public static enum Level {
        LOW, HIGH
    }

    @Override
    public Level bottom() {
        return Level.LOW;
    }

    @Override
    public Level top() {
        return Level.HIGH;
    }

    @Override
    public Level lub(Level l1, Level l2) {
        // TODO Auto-generated method stub
        return l1.equals(Level.LOW)? l2 : Level.HIGH;
    }

    @Override
    public Level glb(Level l1, Level l2) {
        return l1.equals(Level.HIGH) ? l2 : Level.LOW;
    }

    @Override
    public boolean le(Level l1, Level l2) {
        return l1.equals(Level.LOW) || l1.equals(l2);
    }
    
    

}

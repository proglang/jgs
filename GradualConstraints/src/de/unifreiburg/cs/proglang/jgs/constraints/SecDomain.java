package de.unifreiburg.cs.proglang.jgs.constraints;

/**
 * The interface for a security lattice.
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 * @param <Level> The type of security levels.
 */
public interface SecDomain<Level> {
   Level bottom(); 
   Level top();
   Level lub(Level l1, Level l2);
   Level glb(Level l1, Level l2);
   boolean le(Level l1, Level l2);
}

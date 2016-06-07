package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;

import java.util.Iterator;

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

   /**
    * A way to parse security levels from Strings.
    */
   AnnotationParser<Level> levelParser();

   /**
    * Optional method for enumerable domains
    * @return A stream of all the security levels.
    */
   Iterator<Level> enumerate();
}

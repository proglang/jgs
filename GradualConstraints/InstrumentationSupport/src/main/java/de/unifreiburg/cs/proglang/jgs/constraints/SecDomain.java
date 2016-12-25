package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;
import scala.Option;

import java.util.Iterator;

/**
 * The interface for a security lattice.
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 * @param <Level> The type of security levels. It's {@code toString} method should return a string that can be recognized by {@code readLevel}
 */
public interface SecDomain<Level> {
   Level bottom();
   Level top();
   Level lub(Level l1, Level l2);
   Level glb(Level l1, Level l2);
   boolean le(Level l1, Level l2);

    /**
     * Parses a String into a Level. Should throw an IllegalArgumentException on parse errors. This method should be to instantiate Levels during run-time monitoring.
     */
   Level readLevel(String levelString);
   /*
   public Level readLevel(String levelString) {
      // TODO: throw a recognizable exception here (for debugging purposes)
      Option<Level> maybeLevel = this.levelParser().parse(levelString);
      if (maybeLevel.isDefined()) {
         return maybeLevel.get();
      } else {
         throw new IllegalArgumentException(String.format("Error parsing string %s to a level", levelString));
      }
   }
   */

   /**
    * Optional method for enumerable domains
    * @return A stream of all the security levels.
    */
   public abstract Iterator<Level> enumerate();
}

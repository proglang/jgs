package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;
import scala.Option;

import java.util.Iterator;

/**
 * The interface for a security lattice.
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 * @param <Level> The type of security levels.
 */
public abstract class SecDomain<Level> {
   public abstract Level bottom();
   public abstract Level top();
   public abstract Level lub(Level l1, Level l2);
   public abstract Level glb(Level l1, Level l2);
   public abstract boolean le(Level l1, Level l2);

   /**
    * A way to parse security levels from Strings.
    */
   public abstract AnnotationParser<Level> levelParser();

    /**
     * Parses a String into a Level. Throws an exception on parse errors. This method should be to instantiate Levels during run-time monitoring.
     */
   public Level readLevel(String levelString) {
      // TODO: throw a recognizable exception here (for debugging purposes)
      Option<Level> maybeLevel = this.levelParser().parse(levelString);
      if (maybeLevel.isDefined()) {
         return maybeLevel.get();
      } else {
         throw new IllegalArgumentException(String.format("Error parsing string %s to a level", levelString));
      }
   }

   /**
    * Optional method for enumerable domains
    * @return A stream of all the security levels.
    */
   public abstract Iterator<Level> enumerate();
}

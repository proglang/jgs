package de.unifreiburg.cs.proglang.jgs.constraints.secdomains;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;
import soot.JastAddJ.Opt;

/**
 * The classical LOW-HIGH security lattice.
 * @author Luminous Fennell 
 *
 */
public class LowHigh implements SecDomain<LowHigh.Level> {
    
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
        return l1.equals(Level.LOW) ? l2 : Level.HIGH;
    }

    @Override
    public Level glb(Level l1, Level l2) {
        return l1.equals(Level.HIGH) ? l2 : Level.LOW;
    }

    @Override
    public boolean le(Level l1, Level l2) {
        return l1.equals(Level.LOW) || l1.equals(l2);
    }

    @Override
    public AnnotationParser<Level> levelParser() {
        return s -> {
            if (s.equals("LOW")) {
               return Optional.of(Level.LOW);
            } else if (s.equals("HIGH")) {
                return Optional.of(Level.HIGH);
            } else {
                return Optional.empty();
            }
        };
    }

    @Override
    public Stream<Level> enumerate() {
        return Arrays.asList(Level.values()).stream();
    }
    
    

}

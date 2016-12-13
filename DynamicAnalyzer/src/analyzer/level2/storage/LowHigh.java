package analyzer.level2.storage;

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;

import java.util.Arrays;
import java.util.Iterator;

public class LowHigh extends SecDomain<LowHigh.Level> {	// like: SecDomain ist die Relation; LowHigh.Level die "Menge"
    
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
  /*      return new AnnotationParser<Level>() {
            @Override
            public Option<Level> parse(String s) {
            if (s.equals("LOW")) {
               return Option.apply(Level.LOW);
            } else if (s.equals("HIGH")) {
                return Option.apply(Level.HIGH);
            } else {
                return Option.empty();
            }
            }
		  }; */
		return null;
	}
	
	@Override
	public Level readLevel(String s) {
		if (s.equals("LOW")) {
            return Level.LOW;
         } else if (s.equals("HIGH")) {
             return Level.HIGH;
         } else {
	         throw new IllegalArgumentException(String.format("Error parsing string %s to a level", s));
         }
	}

	@Override
    public Iterator<Level> enumerate() {
		return Arrays.asList(Level.values()).iterator();
	}
}

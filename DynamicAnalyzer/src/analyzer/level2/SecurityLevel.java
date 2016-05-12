package analyzer.level2;

public enum SecurityLevel {
	LOW, HIGH
}

/*import java.util.Arrays;
import java.util.Iterator;

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;

public class SecurityLevel implements SecDomain<SecurityLevel.Level> {
    
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
    public Iterator<Level> enumerate() {
        return Arrays.asList(Level.values()).iterator();
    }

	@Override
	public AnnotationParser<Level> levelParser() {
		// TODO Auto-generated method stub
		return null;
	}
    
    

}*/
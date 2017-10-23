package analyzer.level2.storage;

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;

import java.util.Arrays;
import java.util.Iterator;

import util.exceptions.InternalAnalyzerException;

// TODO: remove
public class LowMediumHigh implements SecDomain<LowMediumHigh.Level> { 

	public static enum Level {
		LOW, MEDIUM, HIGH
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
		return le(l1, l2) ? l2 : l1; // because LOW-MEDIUM-HIGH has total
										// ordering
	}

	@Override
	public Level glb(Level l1, Level l2) {
		return le(l1, l2) ? l1 : l2;
	}

	@Override
	public boolean le(Level l1, Level l2) {
		switch (l1) {
		case HIGH:
			return l2.equals(Level.HIGH) ? true : false; // if l1 == HIGH, then
															// l2 needs to be
															// HIGH to return
															// true
		case MEDIUM:
			if (l2.equals(Level.LOW)) {
				return false; // l1 == MEDIUM, l2 == LOW
			} else {
				return true; // l1 == MEDIUM, l2 == MEDIUM or HIGH
			}
		case LOW:
			return true;
		default:
			throw new InternalAnalyzerException("Weird level: " + l1);
		}
	}

	@Override
	public Level readLevel(String s) {
		if (s.equals("LOW")) {
			return Level.LOW;
		} else if (s.equals("MEDIUM")) {
			return Level.MEDIUM;
		} else if (s.equals("HIGH")) {
			return Level.HIGH;
		} else {
			throw new IllegalArgumentException(String.format(
					"Error parsing string %s to a level", s));
		}
	}

	@Override
	public Iterator<Level> enumerate() {
		return Arrays.asList(Level.values()).iterator();
	}
}

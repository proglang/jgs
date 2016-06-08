package de.unifreiburg.cs.proglang.jgs.constraints;

/**
 * A switch for {@code TypeView}s
 */
public interface TypeViewSwitch<Level, T> {

    T caseLit(Level level);

    T caseDyn();

    T casePub();
}

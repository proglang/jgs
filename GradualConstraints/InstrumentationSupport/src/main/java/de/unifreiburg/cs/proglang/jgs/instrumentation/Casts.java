package de.unifreiburg.cs.proglang.jgs.instrumentation;


import soot.jimple.Stmt;

/**
 * The casts that occur in a method. (a simplified version of jimpleutils.Casts)
 *
 * Created by fennell on 20.02.17.
 */
public interface Casts<Level> {

    interface Conversion<Level>{
        Type<Level> getSrcType();
        Type<Level> getDestType();
    }

    /**
     * @return true if statement <code>s</code> is a value cast.
     */
    boolean isValueCast(Stmt s);

    /**
     * Get the ValueCast for statment <code>s</code>. Throws an <code>IllegalargumentException</code> if <code>isValueCast</code> returns false.
     */
    Conversion<Level> getValueCast(Stmt s);

    /**
     *
     * @return true if statment <code>s</code> starts a context casts.
     */
    boolean isCxCastStart(Stmt s);

    /**
     * Get the CxCast for statement <code>s</code>. Throws an <code>IllegalargumentException</code> if <code>isCxCastStart(s)</code> returns false.
     */
    Conversion<Level> getCxCast(Stmt s);


    /**
     * @return true if statement <code>s</code> ends a context casts.
     */
    boolean isCxCastEnd(Stmt s);
}

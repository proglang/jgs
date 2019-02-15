package de.unifreiburg.cs.proglang.jgs.instrumentation;


import scala.Option;
import soot.Value;
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

    interface ValueConversion<Level> extends Conversion<Level> {
        Option<Value> getSrcValue();
    }

    /**
     * @return true if statement <code>s</code> is a value cast.
     */
    boolean isValueCast(Stmt s);

    /**
     * Get the ValueCast for statement <code>s</code>. Throws an <code>IllegalArgumentException</code> if <code>isValueCast</code> returns false.
     */
    ValueConversion<Level> getValueCast(Stmt s);

    /**
     *
     * @return true if statement <code>s</code> starts a context casts.
     */
    boolean isCxCastStart(Stmt s);

    /**
     * Get the CxCast for statement <code>s</code>. Throws an <code>IllegalArgumentException</code> if <code>isCxCastStart(s)</code> returns false.
     */
    Conversion<Level> getCxCast(Stmt s);


    /**
     * @return true if statement <code>s</code> ends a context casts.
     */
    boolean isCxCastEnd(Stmt s);
}

package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by fennell on 3/31/16.
 */
public class Vars {

    /**
     * see getAll(Value)
     */
    public static Stream<Var<?>> getAllFromValues(Collection<Value> bs) {
        return bs.stream().flatMap(Vars::getAll);
    }

    /**
     * see getAll(Value)
     */
    public static Stream<Var<?>> getAllFromValueBoxes(Collection<ValueBox> bs) {
        return bs.stream().flatMap((ValueBox b) -> getAll(b));
    }

    /**
     * see getAll(Value)
     */
    public static Stream<Var<?>> getAll(ValueBox b) {
        return getAll(b.getValue());
    }

    /**
     * Collect all variables from a jimple Value
     */
    public static Stream<Var<?>> getAll(Value val) {
        Stream.Builder<Var<?>> result = Stream.builder();
        val.apply(new AbstractJimpleValueSwitch() {

            @Override
            public void caseThisRef(ThisRef v) {
                result.add(Var.fromThis(v));
            }

            @Override
            public void caseParameterRef(ParameterRef v) {
                result.add(Var.fromParam(Symbol.param(v.getIndex())));
            }

            @Override
            public void caseLocal(Local l) {
                result.add(Var.fromLocal(l));
            }

            @SuppressWarnings("unchecked")
            @Override
            public void defaultCase(Object object) {
                Value v = (Value) object;
                v.getUseBoxes().forEach(o -> {
                    ValueBox b = (ValueBox) o;
                    b.getValue().apply(this);
                });
            }
        });
        return result.build();
    }
}

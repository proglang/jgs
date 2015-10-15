package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;

/**
 * JGS' variables are what Jimple calls "Local" "ThisRef" and "ParameterRef".
 * This class wraps the three Jimple classes into one type, namely Var<?>.
 * 
 * @author fennell
 *
 * @param <T>
 */
public class Var<T> {
    T me;

    // factory methods

    public static Var<Local> fromLocal(Local v) {
        return new Var<>(v);
    }

    public static Var<ThisRef> fromThis(ThisRef v) {
        return new Var<>(v);
    }

    public static Var<ParameterRef> fromParam(ParameterRef v) {
        return new Var<>(v);
    }
    
    /**
     * @see getAll(Value)
     */
    public static List<Var<?>> getAll(Collection<ValueBox> bs) {
        return bs.stream().flatMap((ValueBox b) -> getAll(b).stream()).collect(Collectors.toList());
    }
    
    /** 
     * @see getAll(Value)
     */
    public static List<Var<?>> getAll(ValueBox b) {
        return getAll(b.getValue());
    }


    /**
     * Collect all variables from a jimple Value
     */
    public static List<Var<?>> getAll(Value val) {
        ArrayList<Var<?>> result = new ArrayList<>();
        val.apply(new AbstractJimpleValueSwitch() {

            @Override
            public void caseThisRef(ThisRef v) {
                result.add(Var.fromThis(v));
            }

            @Override
            public void caseParameterRef(ParameterRef v) {
                result.add(Var.fromParam(v));
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
        return result;
    }

    private Var(T me) {
        super();
        this.me = me;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((me == null) ? 0 : me.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Var other = (Var) obj;
        if (me == null) {
            if (other.me != null)
                return false;
        } else if (!me.equals(other.me))
            return false;
        return true;
    }

}

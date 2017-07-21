package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import scala.Option;
import soot.SootField;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.*;

/**
 * A table mapping fields to their security types.
 */
public class FieldTable<Level> {

    public static <Level> FieldTable<Level> of(Map<SootField, TypeView<Level>> fieldTable) {
        return new FieldTable<>(new HashMap<SootField, TypeView<Level>>(fieldTable));
    }

    private final Map<SootField, TypeView<Level>> fieldTable;

    private FieldTable(Map<SootField, TypeView<Level>> fieldTable) {
        this.fieldTable = fieldTable;
    }

    public Option<TypeView<Level>> get(SootField f) {
        return Option.apply(fieldTable.get(f));
    }

    @Override
    public String toString() {
        return "FieldTable{" +
                "fieldTable=" + fieldTable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldTable<?> that = (FieldTable<?>) o;

        return !(fieldTable != null ? !fieldTable.equals(that.fieldTable) : that.fieldTable != null);

    }

    @Override
    public int hashCode() {
        return fieldTable != null ? fieldTable.hashCode() : 0;
    }
}

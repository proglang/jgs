package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import scala.Option;
import soot.SootField;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.*;

public class FieldTable<Level> {

    private final Map<SootField, TypeView<Level>> fieldTable;

    public FieldTable(Map<SootField, TypeView<Level>> fieldTable) {
        this.fieldTable = new HashMap<>(fieldTable);
    }

    public Option<TypeView<Level>> get(SootField f) {
        return Option.apply(fieldTable.get(f));
    }

    public Map<SootField, TypeView<Level>> getRawTable() {
        return Collections.unmodifiableMap(this.fieldTable);
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

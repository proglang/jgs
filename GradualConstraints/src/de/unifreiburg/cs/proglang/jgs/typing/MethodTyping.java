package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraints;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import soot.SootMethod;

import java.util.Optional;

/**
 * Context for typing methods
 * Created by fennell on 1/12/16.
 */
public class MethodTyping<Level> {
    final private Casts<Level> casts;
    final private ConstraintSetFactory<Level> csets;
    final private Constraints<Level> cstrs;
    final private TypeVars tvars;
    final private SignatureTable<Level> signatures;

    /**
     * @param tvars Factory for generating type variables
     * @param csets Factory for constraint sets
     * @param cstrs The domain of constraints
     * @param casts Specification of cast methods
     * @param signatures
     */
    public MethodTyping(TypeVars tvars, ConstraintSetFactory<Level> csets, Constraints<Level> cstrs, Casts<Level> casts, SignatureTable<Level> signatures) {
        this.csets = csets;
        this.casts = casts;
        this.cstrs = cstrs;
        this.tvars = tvars;
        this.signatures = signatures;
    }

    public Optional<ConstraintSet<Level>.RefinementError> check(SootMethod method) {
      throw new RuntimeException("NOT IMPLEMENTED");
    }
}

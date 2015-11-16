package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;

/**
 * Context for generating typing constraints and environments for whole method bodies.
 *
 * Created by fennell on 11/15/15.
 */
public class MethodBodyTyping<Level> {

    final private BasicStatementTyping<Level> bsTyping;
    final private ConstraintSetFactory<Level> csets;
    final private TypeVars tvars;

    public MethodBodyTyping(ConstraintSetFactory<Level> csets, TypeDomain<Level> types, TypeVars tvars, Constraints<Level> cstrs) {
        bsTyping = new BasicStatementTyping<>(csets, types, tvars, cstrs);
        this.csets = csets;
        this.tvars = tvars;
    }

    /**
     * Generate typing constraints.
     * @return A ForwardAnalysis result that contains typing Results for every statement in <code>body</code>
     */
    public Gen generate(UnitGraph body, Environment env,
                                   TypeVar pc,
                                   SignatureTable<Level> signatures,
                                   Casts<Level> casts) throws TypeError {
        return new Gen(body);
    }

    public static class ResultBox<Level> {
        public Result<Level> getResult() {
            return result;
        }

        public void setResult(Result<Level> result) {
            this.result = result;
        }

        private Result<Level> result;

        public ResultBox(Result<Level> result) {
            this.result = result;
        }

    }

    /**
     * Forward analysis for generating constraints, transitions, and effects. When completed, the typing.Result _after_ each statement describes the pre/post-environments, constraints and effects for that particular statement.
     */
    public class Gen extends ForwardFlowAnalysis<Unit, ResultBox<Level>> {

        public Gen(DirectedGraph<Unit> graph) {
            super(graph);
            this.doAnalysis();
        }

        @Override
        protected void flowThrough(ResultBox<Level> in, Unit d, ResultBox<Level> out) {

            throw new RuntimeException("Not Implemented!");
        }

        @Override
        protected ResultBox<Level> newInitialFlow() {
            return new ResultBox<>(Result.empty(csets));
        }

        @Override
        protected ResultBox<Level> entryInitialFlow() {
            return new ResultBox<>(Result.fromParameters(tvars));
        }

        @Override
        protected void merge(ResultBox<Level> in1, ResultBox<Level> in2, ResultBox<Level> out) {
            throw new RuntimeException("Not Implemented!");
        }

        @Override
        protected void copy(ResultBox<Level> source, ResultBox<Level> dest) {
            throw new RuntimeException("Not Implemented!");
        }
    }
}

package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toSet;

/**
 * Context for generating typing constraints and environments for whole method bodies.
 *
 * Created by fennell on 11/15/15.
 */
public class MethodBodyTyping<Level> {

    final private BasicStatementTyping<Level> bsTyping;
    final private Casts<Level> casts;
    final private ConstraintSetFactory<Level> csets;
    final private TypeVars tvars;

    public MethodBodyTyping(ConstraintSetFactory<Level> csets, TypeDomain<Level> types, TypeVars tvars, Constraints<Level> cstrs, Casts<Level> casts) {
        bsTyping = new BasicStatementTyping<>(csets, types, tvars, cstrs);
        this.csets = csets;
        this.tvars = tvars;
        this.casts = casts;
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
        private Result<Level> result;
        private final Map<Stmt, TypeVar> localContexts;
        private final TypeVar topLevelContext;

        public Result<Level> getResult() {
            return result;
        }

        public void setResult(Result<Level> result) {
            this.result = result;
        }
        
        public void addContext(Stmt s, TypeVar v) {
            if (this.localContexts.containsKey(s)) {
                throw new RuntimeException(String.format("There is already a local context for %s: %s", s, localContexts.get(s)));
            }
            this.localContexts.put(s, v);
        }
        
        public void removeContext(Stmt s) {
            if (!this.localContexts.containsKey(s)) {
                throw new RuntimeException(String.format("There is no context recorded for %s", s));
            }
            this.localContexts.remove(s);
        }
        
        public Set<TypeVar> getPcs() {
            return Stream.concat(Stream.of(topLevelContext), localContexts.values().stream()).collect(toSet());
        }


        public ResultBox(Result<Level> result, TypeVar topLevelContext) {
            this.result = result;
            this.topLevelContext = topLevelContext;
            this.localContexts = new HashMap<>();
        }

    }

    /**
     * Forward analysis for generating constraints, transitions, and effects. When completed, the typing.Result _after_ each statement describes the pre/post-environments, constraints and effects for that particular statement.
     */
    public class Gen extends ForwardFlowAnalysis<Unit, ResultBox<Level>> {

        private final SignatureTable<Level> signatures;

        public Gen(DirectedGraph<Unit> graph, SignatureTable<Level> signatures) {
            super(graph);
            this.signatures = signatures;
            this.doAnalysis();
        }

        @Override
        protected void flowThrough(ResultBox<Level> in, Unit d, ResultBox<Level> out) {

            d.apply(bsTyping.generate((Stmt)d, in.getResult().getFinalEnv(), in.getPcs(), signatures, casts));
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

package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Context for generating typing constraints and environments for whole method bodies.
 * <p>
 * Created by fennell on 11/15/15.
 */
public class MethodBodyTyping<Level> {

    final private BasicStatementTyping<Level> bsTyping;
    final private Casts<Level> casts;
    final private ConstraintSetFactory<Level> csets;
    final private TypeVars tvars;
    final private Environment env;

    public MethodBodyTyping(ConstraintSetFactory<Level> csets, TypeDomain<Level> types, Environment env, TypeVars tvars, Constraints<Level> cstrs, Casts<Level> casts) {
        this.env = env;
        bsTyping = new BasicStatementTyping<>(csets, types, tvars, cstrs);
        this.csets = csets;
        this.tvars = tvars;
        this.casts = casts;
    }

    /**
     * Generate typing constraints.
     *
     * @return A ForwardAnalysis result that contains typing Results for every statement in <code>body</code>
     */
    public Gen generate(DirectedGraph<Unit> body,
                        TypeVar pc,
                        SignatureTable<Level> signatures) throws TypeError {
        Gen g;
        try {
            g = new Gen(body, signatures, pc);
        } catch (AnalysisException e) {
            throw e.error;
        }
        return g;
    }

    /**
     * Package a TypeError into a RuntimeException to allow to throw it from an soot.Analysis.
     */
    private static class AnalysisException extends RuntimeException {

        public final TypeError error;

        public AnalysisException(TypeError error) {
            this.error = error;
        }

    }

    public static class ResultBox<Level> {
        private Result<Level> result;
        private Map<Unit, TypeVar> localContexts;
        private final TypeVar topLevelContext;

        public Result<Level> getResult() {
            return result;
        }

        public void setResult(Result<Level> result) {
            this.result = result;
        }

        public void setLocalContexts(ResultBox<Level> other) {
            this.localContexts = new HashMap<>(other.localContexts);
        }

        /**
         * Add a new pc (variable <code>v</code>) for a context-opening statement <code>s</code> to the set of active contexts. Throws an exception if there is already a context recorded for <code>s</code>.
         */
        public void addContext(Stmt s, TypeVar v) {
            if (this.localContexts.containsKey(s)) {
                throw new RuntimeException(String.format("There is already a local context for %s: %s", s, localContexts.get(s)));
            }
            this.localContexts.put(s, v);
        }

        /**
         * Remove the context variable of a (if-)statement from the contexts, if possible. Ignores statements that are not recorded to have a contexts.
         */
        public void removeContext(Unit s) {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ResultBox<?> resultBox = (ResultBox<?>) o;

            if (result != null ? !result.equals(resultBox.result) : resultBox.result != null) return false;
            if (localContexts != null ? !localContexts.equals(resultBox.localContexts) : resultBox.localContexts != null)
                return false;
            return !(topLevelContext != null ? !topLevelContext.equals(resultBox.topLevelContext) : resultBox.topLevelContext != null);

        }

        @Override
        public int hashCode() {
            int result1 = result != null ? result.hashCode() : 0;
            result1 = 31 * result1 + (localContexts != null ? localContexts.hashCode() : 0);
            result1 = 31 * result1 + (topLevelContext != null ? topLevelContext.hashCode() : 0);
            return result1;
        }
    }

    /**
     * Forward analysis for generating constraints, transitions, and effects. When completed, the typing.Result _after_ each statement describes the pre/post-environments, constraints and effects for that particular statement.
     */
    public class Gen extends ForwardFlowAnalysis<Unit, ResultBox<Level>> {

        private final SignatureTable<Level> signatures;
        private final TypeVar topLevelContext;
        private final DominatorsFinder<Unit> postdoms;

        public Gen(DirectedGraph<Unit> graph, SignatureTable<Level> signatures, TypeVar topLevelContext) {
            super(graph);
            this.signatures = signatures;
            this.topLevelContext = topLevelContext;
            //noinspection unchecked
            this.postdoms = new MHGPostDominatorsFinder(graph);
            this.doAnalysis();
        }

        @Override
        protected void flowThrough(ResultBox<Level> in, Unit d, ResultBox<Level> out) {

            // fix up the contexts (in case we are at a join point)
            out.setLocalContexts(in);
            out.removeContext(postdoms.getImmediateDominator(d));

            Stmt s = (Stmt) d;
            Result<Level> r;

            // generate a new pc when branching
            Optional<TypeVar> newPc = s.branches()? Optional.of(tvars.fresh("pc")) : Optional.empty();

            try {
                r = bsTyping.generate(s, in.getResult().getFinalEnv(), out.getPcs(), signatures, casts, newPc);
            } catch (TypeError e) {
                throw new AnalysisException(e);
            }
            // include the constraints of "in"
            r = Result.addEffects(Result.addConstraints(r, in.getResult().getConstraints()), in.getResult().getEffects());

            // fix up the context in case we are branching
            if (s.branches()) {
                out.addContext(s, tvars.fresh("pc"));
            }
            // set a result
            out.setResult(r);

        }

        @Override
        protected ResultBox<Level> newInitialFlow() {
            return new ResultBox<>(Result.empty(csets), topLevelContext);
        }

        @Override
        protected ResultBox<Level> entryInitialFlow() {
            return new ResultBox<>(Result.fromEnv(csets, env), topLevelContext);
        }

        @Override
        protected void merge(ResultBox<Level> in1, ResultBox<Level> in2, ResultBox<Level> out) {
            out.setResult(Result.join(in1.getResult(), in2.getResult(), csets, tvars));
        }

        @Override
        protected void copy(ResultBox<Level> source, ResultBox<Level> dest) {
            dest.setResult(source.getResult());
            dest.setLocalContexts(source);
        }
    }
}

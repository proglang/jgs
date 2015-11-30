package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import soot.Unit;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

/**
 * Easily constructable Unit-Graphs, for testing
 */
// TODO: could be implemented more efficiently (i.e. imperatively)
public class Graphs {

    /**
     * Modify the edges in a graph by overrding preds and succs .
     */
    private static abstract class Modified implements DirectedGraph<Unit> {
        private final DirectedGraph<Unit> linear;

        protected Modified(DirectedGraph<Unit> linear) {
            this.linear = linear;
        }

        @Override
        public final List<Unit> getHeads() {
            return linear.getHeads();
        }

        @Override
        public final List<Unit> getTails() {
            return linear.getTails();
        }

        @Override
        public final int size() {
            return  linear.size();
        }

        @Override
        public final Iterator<Unit> iterator() {
            return linear.iterator();
        }
    }

    public static DirectedGraph<Unit> singleton(Unit u) {
        return new DirectedGraph<Unit>() {
            @Override
            public List<Unit> getHeads() {
                return Collections.singletonList(u);
            }

            @Override
            public List<Unit> getTails() {
                return Collections.singletonList(u);
            }

            @Override
            public List<Unit> getPredsOf(Unit unit) {
                return Collections.emptyList();
            }

            @Override
            public List<Unit> getSuccsOf(Unit unit) {
                return Collections.emptyList();
            }

            @Override
            public int size() {
                return 1;
            }

            @Override
            public Iterator<Unit> iterator() {
                return getHeads().iterator();
            }
        };
    }

    public static DirectedGraph<Unit> seq(DirectedGraph<Unit> first, DirectedGraph<Unit> second) {
        return new DirectedGraph<Unit>() {
            @Override
            public List<Unit> getHeads() {
                return first.getHeads();
            }

            @Override
            public List<Unit> getTails() {
                return second.getTails();
            }

            @Override
            public List<Unit> getPredsOf(Unit unit) {
                List<Unit> result = second.getPredsOf(unit);
                if (result.isEmpty()) {
                    if (second.getHeads().contains(unit)) {
                        result = first.getTails();
                    } else {
                        result = first.getPredsOf(unit);
                    }
                }
                return result;
            }

            @Override
            public List<Unit> getSuccsOf(Unit unit) {
                List<Unit> result = first.getSuccsOf(unit);
                if (result.isEmpty()) {
                    if (first.getTails().contains(unit)) {
                        result = second.getHeads();
                    } else {
                        result = second.getSuccsOf(unit);
                    }
                }
                return result;
            }

            @Override
            public int size() {
                return first.size() + second.size();
            }

            @Override
            public Iterator<Unit> iterator() {
                Iterator<Unit> firstIter = first.iterator();
                Iterator<Unit> secondIter = second.iterator();
                return new Iterator<Unit>() {
                    @Override
                    public boolean hasNext() {
                        return firstIter.hasNext() || secondIter.hasNext();
                    }

                    @Override
                    public Unit next() {
                        if (firstIter.hasNext()) {
                            return firstIter.next();
                        } else {
                            return secondIter.next();
                        }
                    }
                };
            }
        };
    }

    @SafeVarargs
    public static DirectedGraph<Unit> seq(DirectedGraph<Unit> first, DirectedGraph<Unit>... rest) {
        return asList(rest).stream().reduce(first, Graphs::seq);
    }

    public static DirectedGraph<Unit> seq(Unit first, Unit... rest) {
        return seq(singleton(first), rest);
    }

    public static DirectedGraph<Unit> seq(DirectedGraph<Unit> first, Unit... rest) {
        return asList(rest).stream().map(Graphs::singleton).reduce(first, Graphs::seq);
    }

    public static DirectedGraph<Unit> branchIf(Expr test,  DirectedGraph<Unit> then, DirectedGraph<Unit> els) {
        if (then.getHeads().size() > 1 || els.getHeads().size() > 1) {
            throw new RuntimeException("Only single-head graphs allowed for branchIf bodies!");
        }
        Unit ifTarget = then.getHeads().get(0);
        Unit elseTarget = els.getHeads().get(0);
        Stmt sIf = Jimple.v().newIfStmt(test,ifTarget);
        Stmt sGoto = Jimple.v().newGotoStmt(elseTarget);
        Stmt sEnd = Jimple.v().newNopStmt();
        DirectedGraph<Unit> linear = seq(singleton(sIf), singleton(sGoto), els, then, singleton(sEnd));
        return new Modified(linear) {

            @Override
            public List<Unit> getPredsOf(Unit unit) {
                return Stream.concat(
                        unit.equals(sEnd) ? els.getTails().stream() : Stream.empty(),
                        linear.getPredsOf(unit).stream().map(p -> unit.equals(ifTarget) ? sIf : p)
                ).collect(toList());
            }

            @Override
            public List<Unit> getSuccsOf(Unit unit) {
                return Stream.concat(
                       unit.equals(sIf) ? then.getHeads().stream() : Stream.empty(),
                        linear.getSuccsOf(unit).stream().map(s -> els.getTails().contains(unit)? sEnd : s)
                ).collect(toList());
            }
        };
    }

    public static DirectedGraph<Unit> branchWhile(Expr test,  DirectedGraph<Unit> body) {
        if (body.getHeads().size() > 1) {
            throw new RuntimeException("Only single-head graphs allowed for branchWhile bodies!");
        }
        Unit doTarget = body.getHeads().get(0);
        Stmt sIf = Jimple.v().newIfStmt(test,doTarget);
        Stmt sEnd = Jimple.v().newNopStmt();
        Stmt sGotoLoop = Jimple.v().newGotoStmt(sIf);
        Stmt sGotoEnd = Jimple.v().newGotoStmt(sEnd);
        DirectedGraph<Unit> linear = seq(singleton(sIf), singleton(sGotoEnd), body, singleton(sGotoLoop), singleton(sEnd));
        return new Modified(linear) {

            @Override
            public List<Unit> getPredsOf(Unit unit) {
                if (unit.equals(sIf)) {
                    return Collections.singletonList(sGotoLoop);
                } else if (unit.equals(doTarget)) {
                    return Collections.singletonList(sIf);
                } else if (unit.equals(sEnd)) {
                    return Collections.singletonList(sGotoEnd);
                } else {
                    return linear.getPredsOf(unit);
                }
            }

            @Override
            public List<Unit> getSuccsOf(Unit unit) {
                if (unit.equals(sIf)) {
                    return asList(sGotoEnd, doTarget);
                } else if (unit.equals(sGotoEnd)) {
                    return Collections.singletonList(sEnd);
                } else if (unit.equals(sGotoLoop)) {
                    return Collections.singletonList(sIf);
                } else {
                    return linear.getSuccsOf(unit);
                }
            }
        };
    }

    public static String toString(DirectedGraph<Unit> g) {
        Stream.Builder<Unit> units = Stream.builder();
        g.forEach(units);
        return units.build().map(Object::toString).collect(joining(", "));
    }
}

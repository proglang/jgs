package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.List;
import java.util.Optional;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

public interface Transition {

    Environment getInit();

    Environment getFinal();

    default Optional<Atom> asAtom() {
        return Optional.empty();
    }

    default Optional<Branch> asBranch() {
        return Optional.empty();
    }

    default Optional<Seq> asSeq() {
        return Optional.empty();
    }

    public static class Atom implements Transition {

        public final Environment init;
        public final Environment fin;

        public Atom(Environment init, Environment fin) {
            super();
            this.init = init;
            this.fin = fin;
        }

        @Override
        public Environment getInit() {
            return init;
        }

        @Override
        public Environment getFinal() {
            return fin;
        }

        @Override
        public Optional<Atom> asAtom() {
            return Optional.of(this);
        }

    }

    public static class Branch implements Transition {

        public final Environment init;
        public final Environment fin;
        public final TypeVar innerCx;
        public final List<Transition> branches;

        public Branch(Environment init,
                      Environment fin,
                      TypeVar innerCx,
                      List<Transition> branches) {
            super();
            this.init = init;
            this.fin = fin;
            this.innerCx = innerCx;
            this.branches = branches;
        }

        @Override
        public Environment getInit() {
            return init;
        }

        @Override
        public Environment getFinal() {
            return fin;
        }
    }

    public static class Seq implements Transition {
        
        public final Transition first;
        public final Transition second;
        
        public Seq(Transition first, Transition second) {
            super();
            this.first = first;
            this.second = second;
        }

        @Override
        public Environment getInit() {
            return first.getInit();
        }

        @Override
        public Environment getFinal() {
            return second.getFinal();
        }
    }

}

package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.List;

import main.java.de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.typing.Environment;

/**
 *
 * A transition combines environments that are active before and after a statement (Pre- and Post-environments).
 *
 * @author fennell
 *
 */
public abstract class Transition {
    
    // Factory methods:
    public static Transition makeAtom(Environment init, Environment fin) {
        return new Atom(init, fin);
    }

    public static Transition makeBranch(Environment init,
                                        Environment fin,
                                        TypeVar innerCx,
                                        List<Transition> branches) {
        return new Branch(init, fin, innerCx, branches);
    }

    public static Transition makeSeq(Transition fst, Transition snd) {
        return new Seq(fst, snd);
    }

    public static Transition makeId(Environment env) {
        return makeAtom(env, env);
    }
    
    
    // Interface:
    
    /**
     * All transitions start with an initial environment.
     */
    public abstract Environment getInit();

    /**
     * All transitions end with a final environment.
     */
    public abstract Environment getFinal();
    

    public static class Atom extends Transition {


        public final Environment init;
        public final Environment fin;

        Atom(Environment init, Environment fin) {
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
    }

    public static class Branch extends Transition {

        public final Environment init;
        public final Environment fin;
        public final TypeVar innerCx;
        public final List<Transition> branches;

        Branch(Environment init,
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

    public static class Seq extends Transition {

        public final Transition first;
        public final Transition second;

        Seq(Transition first, Transition second) {
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

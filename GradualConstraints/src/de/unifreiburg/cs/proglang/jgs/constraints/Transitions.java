package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.typing.Environment;

import java.util.List;

import javax.sound.midi.Sequence;

import de.unifreiburg.cs.proglang.jgs.constraints.Transition.*;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

/**
 * Companion class to <code>Transition</code>
 * 
 * @author fennell
 *
 */
public class Transitions {

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

}

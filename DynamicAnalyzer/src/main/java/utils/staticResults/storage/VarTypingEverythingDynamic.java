package utils.staticResults.storage;

import analyzer.level1.storage.Dynamic;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.jimple.Stmt;
import utils.exceptions.InternalAnalyzerException;

import java.util.HashMap;
import java.util.Map;

/**
 * Fake Variable Typing, which always return the Dynamic Type.
 */
public class VarTypingEverythingDynamic<Level> implements VarTyping<Level>{

    Map<Stmt, Map<Local, BeforeAfterContainer>> var_typing = new HashMap<>();
    Body sootBody;

    public VarTypingEverythingDynamic(Body body) {
        sootBody = body;
        Dynamic<Level> dyn = new Dynamic<>();
        BeforeAfterContainer dyn_dyn = new BeforeAfterContainer(dyn, dyn);
        for (Unit u: sootBody.getUnits()) {
            Stmt s = (Stmt) u;

            Map<Local, BeforeAfterContainer> tmp = new HashMap<>();
            for (Local l: sootBody.getLocals()) {
                tmp.put(l, dyn_dyn);
            }

            var_typing.put(s, tmp);
        }
    }



    @Override
    public Type<Level> getBefore(Instantiation<Level> instantiation, Stmt s, Local l) {
        if (! sootBody.getUnits().contains((Unit) s) || ! sootBody.getLocals().contains(l)) {
            throw new InternalAnalyzerException("Required VarTyping for Stmt "  + s.toString() + " and Local " + l.toString() + ", which is not present in the Fake Variable Typing");
        }
        return var_typing.get(s).get(l).getBefore();
    }

    @Override
    public Type<Level> getAfter(Instantiation<Level> instantiation, Stmt s, Local l) {
        if (! sootBody.getUnits().contains((Unit) s) || ! sootBody.getLocals().contains(l)) {
            throw new InternalAnalyzerException("Required VarTyping for Stmt "  + s.toString() + " and Local " + l.toString() + ", which is not present in the Fake Variable Typing");
        }
        return var_typing.get(s).get(l).getAfter();
    }
}



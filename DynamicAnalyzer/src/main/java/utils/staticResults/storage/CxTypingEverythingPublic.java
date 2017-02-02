package utils.staticResults.storage;

import analyzer.level1.storage.Public;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;
import soot.Body;
import soot.Unit;
import soot.jimple.Stmt;
import utils.exceptions.InternalAnalyzerException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicolas Müller on 02.02.17.
 */
public class CxTypingEverythingPublic<Level> implements CxTyping<Level> {
    Body sootBody;
    Map<Stmt, Type<Level>> CxTyping = new HashMap<>();

    public CxTypingEverythingPublic(Body body) {
        sootBody = body;
        for (Unit u: sootBody.getUnits()) {
            CxTyping.put((Stmt) u, new Public<Level>());
        }
    }


    @Override
    public Type<Level> get(Instantiation<Level> instantiation, Stmt s) {
        if (!CxTyping.containsKey(s)) {
            throw new InternalAnalyzerException("Key" + s.toString() + " not present in CX-Typing!");
        }
        return CxTyping.get(s);
    }
}

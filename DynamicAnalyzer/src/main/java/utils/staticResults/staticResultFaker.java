package utils.staticResults;

import analyzer.level1.storage.Dynamic;
import analyzer.level1.storage.SecValueTuple;
import analyzer.level2.storage.LowMediumHigh;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.jimple.Stmt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicolas Müller on 23.01.17.
 */
public class staticResultFaker {

    public static void make(Body sootBody, Map<Stmt, Map<Local, SecValueTuple>> var_typing_results) {
        Dynamic<LowMediumHigh.Level> dyn = new Dynamic<>();
        SecValueTuple dyn_dyn = new SecValueTuple(dyn, dyn);
        for (Unit u: sootBody.getUnits()) {
            Stmt s = (Stmt) u;

            Map<Local, SecValueTuple> tmp = new HashMap<>();
            for (Local l: sootBody.getLocals()) {
                tmp.put(l, dyn_dyn);
            }

            var_typing_results.put(s, tmp);
        }
    }
}

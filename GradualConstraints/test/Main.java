import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.typing.ClassHierarchyTyping;
import org.apache.commons.lang3.StringUtils;
import soot.JastAddJ.Opt;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.typing.ClassHierarchy;
import soot.options.Options;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Arrays.asList;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;

public class Main {

    private static Logger log =
            Logger.getLogger("de.unifreiburg.cs.proglang.jgs.typing.test");

    public static void main(String[] args) {
        Options o = Options.v();
        o.set_soot_classpath("testclasses-java:result/lib/openjdk/jre/lib/rt.jar");
        o.set_process_dir(Collections.singletonList("testclasses-java"));

        Scene s = Scene.v();
        log.info(String.format("Classpath: %s", s.getSootClassPath()));

        s.loadNecessaryClasses();
        log.info(String.format("Number of classes found: %d", s.getClasses().size()));
        log.info(String.format("Application classes: [%s]",
                               StringUtils.join(s.getApplicationClasses(), ", ")));


        System.out.println("Application classes and methods:");
        for (SootClass c : s.getApplicationClasses()) {
            System.out.println(c.toString());

            for (SootMethod m : c.getMethods()) {
                System.out.println(String.format(" method: %s", m.toString()));
            }
        }

        System.out.print("Checking class hierarchy: ");
        ClassHierarchyTyping.check(csets, types, new Code(new TypeVars()).signatures, s.getApplicationClasses().stream());
    }
}

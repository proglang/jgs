import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Signature;
import de.unifreiburg.cs.proglang.jgs.typing.ClassHierarchyTyping;
import org.apache.commons.lang3.StringUtils;
import soot.JastAddJ.Opt;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.typing.ClassHierarchy;
import soot.options.Options;
import soot.tagkit.AnnotationTag;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static java.util.stream.Collectors.toList;

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

        List<SootClass> classes =
                s.getApplicationClasses().stream().collect(toList());
        log.info(String.format("Application classes: [%s]",
                               StringUtils.join(classes, ", ")));


        System.out.println("Application classes and methods:");
        for (SootClass c : classes) {
            System.out.println(c.toString());

            for (SootMethod m : c.getMethods()) {
                System.out.println(String.format(" method: %s", m.toString()));
                Signature<Level> sig = extractSignatureFromTags(m.getTags());
                System.out.println(String.format(" sig: %s", sig.toString()));
            }
        }


        System.out.print("Checking class hierarchy: ");
        ClassHierarchyTyping.check(csets,
                                   types,
                                   new Code(new TypeVars()).signatures,
                                   s.getApplicationClasses().stream());
    }
}

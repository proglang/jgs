import analyzer.level2.storage.LowMediumHigh;
import de.unifreiburg.cs.proglang.jgs.JgsCheck;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.ACasts;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CastsFromConstants;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Methods;
import utils.parser.ArgParser;
import utils.parser.ArgumentContainer;
import utils.staticResults.superfluousInstrumentation.ExpectedException;

import java.util.*;
import java.util.logging.Level;

/**
 * This is the main entry point for jgs.
 * Launches the static analyzer, then the dynamic analyzer.
 *
 * Example:
 * -m pkg.ScratchMonomorphic_Success -s GradualConstraints/JGSTestclasses/Scratch/src/main/java/pkg/ScratchMonomorphic_Success.java -p . GradualConstraints/JGSTestclasses/Scratch/target/scala-2.11/classes GradualConstraints/JGSSupport/target/scala-2.11/classes -o sootOutput
 */
public class Main {
    public static void main(String[] args) {


        /*
        // those are needed because of soot-magic i guess
        Scene.v().addBasicClass("analyzer.level2.HandleStmt");
        Scene.v().addBasicClass("analyzer.level2.HandleStmtUtils");
        Scene.v().addBasicClass("analyzer.level2.SecurityLevel");
        Scene.v().addBasicClass("analyzer.level2.storage.ObjectMap");
        Scene.v().addBasicClass("utils.staticResults.superfluousInstrumentation.PassivController");
        Scene.v().addBasicClass("analyzer.level2.storage.LocalMap");
        Scene.v().addBasicClass("utils.logging.L2Logger");
        Scene.v().addBasicClass("java.util.ArrayList");
        Scene.v().addBasicClass("java.util.ArrayList$Itr");
        Scene.v().addBasicClass("java.util.ArrayList$ListItr");
        Scene.v().addBasicClass("java.util.ArrayList$SubList");
        Scene.v().addBasicClass("java.util.LinkedList");
        Scene.v().addBasicClass("java.util.LinkedList$DescendingIterator");
        Scene.v().addBasicClass("java.util.LinkedList$Node");
        Scene.v().addBasicClass("java.util.LinkedList$ListItr");
        Scene.v().addBasicClass("java.util.ListIterator");
        Scene.v().addBasicClass("java.util.Arrays");
        Scene.v().addBasicClass("java.util.Arrays$ArrayList");
        Scene.v().addBasicClass("java.util.Arrays$LegacyMergeSort");
        Scene.v().addBasicClass("java.util.HashMap");
        Scene.v().addBasicClass("java.util.HashSet");
        Scene.v().addBasicClass("java.util.NoSuchElementException");
        Scene.v().addBasicClass("java.util.Iterator");
        Scene.v().addBasicClass("java.util.TimSort");
        Scene.v().addBasicClass("java.util.ComparableTimSort");
        Scene.v().addBasicClass("utils.staticResults.superfluousInstrumentation.ExpectedException");
        Scene.v().addBasicClass("utils.staticResults.superfluousInstrumentation.ControllerFactory");
        Scene.v().addBasicClass("utils.staticResults.superfluousInstrumentation.ActiveController");
        Scene.v().addBasicClass("java.util.logging.Level");
        Scene.v().addBasicClass("java.util.ResourceBundle");
        Scene.v().addBasicClass("java.util.MissingResourceException");
        Scene.v().addBasicClass("sun.reflect.Reflection");
        Scene.v().addBasicClass("java.util.logging.Level$KnownLevel");
        Scene.v().addBasicClass("java.util.logging.Logger");
        Scene.v().addBasicClass("java.util.logging.Logger$1");
        Scene.v().addBasicClass("java.util.logging.Logger$LoggerHelper");
        Scene.v().addBasicClass("java.util.logging.Formatter");
        Scene.v().addBasicClass("java.util.logging.Handler");
        Scene.v().addBasicClass("java.util.logging.ConsoleHandler");
        Scene.v().addBasicClass("java.util.logging.Filter");
        Scene.v().addBasicClass("java.util.logging.LogRecord");
        Scene.v().addBasicClass("java.util.logging.LogManager$LoggerWeakRef");
        Scene.v().addBasicClass("java.util.logging.LogManager");
        Scene.v().addBasicClass("utils.logging.L2Formatter");
        Scene.v().addBasicClass("utils.exceptions.InternalAnalyzerException");
        Scene.v().addBasicClass("org.apache.commons.collections4.map.ReferenceIdentityMap");
        Scene.v().addBasicClass("org.apache.commons.collections4.map.AbstractReferenceMap$ReferenceStrength");
        Scene.v().addBasicClass("org.apache.commons.collections4.map.AbstractReferenceMap$WeakRef");
        Scene.v().addBasicClass("org.apache.commons.collections4.map.AbstractHashedMap$KeySet");
        Scene.v().addBasicClass("org.apache.commons.collections4.map.AbstractHashedMap$Values");
        Scene.v().addBasicClass("java.lang.System");
        Scene.v().addBasicClass("analyzer.level2.storage.SecurityOptional");
        Scene.v().addBasicClass("analyzer.level2.storage.LPCDominatorPair");
//    Scene.v().addBasicClass("");
        Scene.v().addBasicClass("java.security.PrivilegedAction");
        Scene.v().addBasicClass("java.security.AccessController");
        Scene.v().addBasicClass("java.util.concurrent.CopyOnWriteArrayList");
        Scene.v().addBasicClass("java.util.Map$Entry");
        Scene.v().addBasicClass("utils.exceptions.IFCError");

        Scene.v().addBasicClass("java.util.ConcurrentModificationException");
        Scene.v().addBasicClass("java.lang.Math");
        Scene.v().addBasicClass("java.lang.reflect.Array");
        Scene.v().addBasicClass("java.util.DualPivotQuicksort");
        Scene.v().addBasicClass("de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined");
        Scene.v().addBasicClass("de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined$Edge");
        */

        main.Main.doSootSetup(args);


        ArgumentContainer sootOptionsContainer = ArgParser.getSootOptions(args);
        // run static
        ACasts<LowMediumHigh.Level> casts =
                new CastsFromConstants<>(new TypeDomain<>(new LowMediumHigh()),
                        "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object cast(java.lang.String,java.lang.Object)>",
                        "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object castCx(java.lang.Object)>",
                        "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object castCxEnd(java.lang.Object)>");

        // Static Check
        JgsCheck.log().setLevel(Level.WARNING);
        // TODO: parse external methods and strings from yaml file
        //  these placeholder values are UNSECURE and just for debugging.
        Map<String, String> externalFields = new HashMap<>();
        externalFields.put("<java.lang.System: java.io.PrintStream out>", "pub");
        Map<String, JgsCheck.Annotation> externalMethods = new HashMap<>();
        externalMethods.put("<java.io.PrintStream: void println(java.lang.String)>", new JgsCheck.Annotation(new String[]{}, new String[]{}));
        externalMethods.put("<java.io.PrintStream: void println(java.lang.Object)>", new JgsCheck.Annotation(new String[]{}, new String[]{}));
        externalMethods.put("<java.io.PrintStream: void println(int)>", new JgsCheck.Annotation(new String[]{}, new String[]{}));
        externalMethods.put("<utils.analyzer.HelperClass: java.lang.Object makeHigh(java.lang.Object)>",
                            new JgsCheck.Annotation(new String[]{"? <= @ret"},
                                                    new String[]{}));
        externalMethods.put("<utils.analyzer.HelperClass: java.lang.Object makeLow(java.lang.Object)>",
                            new JgsCheck.Annotation(new String[]{"? <= @ret"},
                                                    new String[]{}));
        externalMethods.put("<java.lang.Integer: java.lang.Integer valueOf(int)>",
                            new JgsCheck.Annotation(new String[]{"@0 <= @ret"},
                                                    new String[]{}));
        externalMethods.put("<java.lang.Integer: int intValue()>",
                            new JgsCheck.Annotation(new String[]{},
                                                    new String[]{}));
        List<String> errors = new ArrayList<>();
        Methods<LowMediumHigh.Level> typeCheckResult = JgsCheck.typeCheck(
                sootOptionsContainer.getMainclass(),
                sootOptionsContainer.getAddClassesToClasspath().toArray(new String[0]),
                sootOptionsContainer.getAddDirsToClasspath().toArray(new String[0]),
                externalMethods,
                externalFields,
                new LowMediumHigh(), casts, errors
        );

        if(!errors.isEmpty()) {
            System.out.println("THERE WERE ERRORS DURING TYPCHECKING. ABORTING.");
            System.exit(-1);
        }

        // Dynamic Check
        // G.reset();
        System.out.print("");
        System.out.println("== START DYNAMIC ANALYSIS ==");
        System.out.print("");
        main.Main.executeWithoutSootSetup(args, true,
                typeCheckResult, false, ExpectedException.NONE.getVal(), casts);
    }
}

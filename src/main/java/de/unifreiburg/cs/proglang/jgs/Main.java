package de.unifreiburg.cs.proglang.jgs;

import analyzer.level2.CurrentSecurityDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.instrumentation.*;
import de.unifreiburg.cs.proglang.jgs.instrumentation.MethodTypings;
import de.unifreiburg.cs.proglang.jgs.typing.FixedTypings;
import util.logging.L1Logger;
import util.parser.ArgParser;
import util.parser.ArgumentContainer;

import java.util.*;
import java.util.logging.Logger;

/**
 * This is the main entry point for jgs.
 * Launches the static analyzer, then the dynamic analyzer.
 *
 * Example:
 * -m pkg.ScratchMonomorphic_Success -s GradualConstraints/JGSTestclasses/Scratch/src/main/java/pkg/ScratchMonomorphic_Success.java -p . GradualConstraints/JGSTestclasses/Scratch/target/scala-2.11/classes GradualConstraints/JGSSupport/target/scala-2.11/classes -o sootOutput
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        main.Main.doSootSetup(args);
        ArgumentContainer sootOptionsContainer = ArgParser.getSootOptions(args);

        SecDomain<String> secdomain;
        /*
        List<URL> secDomainclassPath = sootOptionsContainer.getSecDomainClasspath();
        try {
            URLClassLoader secDomainCl = new URLClassLoader(secDomainclassPath.toArray(new URL[0]));
        // SecDomain<String> secdomain = UserDefined.lowHigh(); // new LowMediumHigh();

            secdomain =
                    (SecDomain<String>) Class.forName("analyzer.level2.CurrentSecurityDomain",
                                                      true, secDomainCl)
                                             .getField("INSTANCE").get(null);
        } catch (IllegalAccessException
                | NoSuchFieldException
                | ClassNotFoundException
                | ClassCastException e) {
            // TODO: standardized error messages and program abort
            System.err.println("Unable to get security domain: "
                               + e.toString()
                               + "\n  "
                               + e.getMessage());
            System.exit(-1);
            throw new RuntimeException("Should have exited here");
        }
        */
        secdomain = CurrentSecurityDomain.INSTANCE;


        // run static
        ACasts<String> casts =
                new CastsFromConstants<>(new TypeDomain<>(secdomain),
                        "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object cast(java.lang.String,java.lang.Object)>",
                        "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object castCx(java.lang.Object)>",
                        "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object castCxEnd(java.lang.Object)>");

        // Static Check
        // TODO: parse external methods and strings from yaml file
        //  these placeholder values are UNSECURE and just for debugging.
        Map<String, String> externalFields = new HashMap<>();
        externalFields.put("<java.lang.System: java.io.PrintStream out>", "pub");
        Map<String, JgsCheck.Annotation> externalMethods = new HashMap<>();
        externalMethods.put("<java.io.PrintStream: void println(java.lang.String)>",
                            Annotations.lowSink(1));
        externalMethods.put("<java.io.PrintStream: void print(java.lang.String)>",
                            Annotations.lowSink(1));
        externalMethods.put("<java.io.PrintStream: void println()>",
                            Annotations.lowSink(0));
        externalMethods.put("<java.io.PrintStream: void println(java.lang.Object)>",
                            new JgsCheck.Annotation(new String[]{"@0 <= LOW"},
                                                    new String[]{"LOW"}));
        externalMethods.put("<java.io.PrintStream: void println(int)>",
                            new JgsCheck.Annotation(new String[]{"@0 <= LOW"},
                                                    new String[]{"LOW"}));
        externalMethods.put("<de.unifreiburg.cs.proglang.jgs.support.DynamicLabel: java.lang.Object makeHigh(java.lang.Object)>",
                            new JgsCheck.Annotation(new String[]{"? <= @ret"},
                                                    new String[]{}));
        externalMethods.put("<de.unifreiburg.cs.proglang.jgs.support.DynamicLabel: java.lang.Object makeLow(java.lang.Object)>",
                            new JgsCheck.Annotation(new String[]{"? <= @ret", "@0 <= LOW"},
                                                    new String[]{}));
        externalMethods.put("<de.unifreiburg.cs.proglang.jgs.support.IOUtils: void printSecret(java.lang.String)>",
                            new JgsCheck.Annotation(new String[]{},
                                                    new String[]{"LOW"}));
        externalMethods.put("<de.unifreiburg.cs.proglang.jgs.support.IOUtils: void printPublicDynamic(java.lang.String)>",
                            new JgsCheck.Annotation(new String[]{"@0 <= ?"},
                                                    new String[]{"?"}));
        externalMethods.put("<de.unifreiburg.cs.proglang.jgs.support.IOUtils: java.lang.String readSecret()>",
                            new JgsCheck.Annotation(new String[]{"HIGH <= @ret"},
                                                    new String[]{"LOW"}));
        externalMethods.put("<de.unifreiburg.cs.proglang.jgs.support.IOUtils: java.lang.String read()>",
                            new JgsCheck.Annotation(new String[]{},
                                                    new String[]{"LOW"}));
        externalMethods.put("<java.lang.Integer: java.lang.Integer valueOf(int)>",
                            new JgsCheck.Annotation(new String[]{"@0 <= @ret"},
                                                    new String[]{}));
        externalMethods.put("<java.lang.String: java.lang.String valueOf(int)>",
                            new JgsCheck.Annotation(new String[]{"@0 <= @ret"},
                                                    new String[]{}));
        externalMethods.put("<java.lang.Integer: int intValue()>",
                            new JgsCheck.Annotation(new String[]{},
                                                    new String[]{}));
        externalMethods.put("<java.lang.String: boolean equals(java.lang.Object)>",
                            new JgsCheck.Annotation(new String[]{"@0 <= @ret"},
                                                    new String[]{}));
        externalMethods.put("<de.unifreiburg.cs.proglang.jgs.support.StringUtil: java.util.List bits(java.lang.String)>",
                            Annotations.pureInputToOutput(1));
        externalMethods.put("<java.util.List: java.util.Iterator iterator()>",
                            Annotations.polymorphicGetter());
        externalMethods.put("<java.util.Iterator: boolean hasNext()>", Annotations.polymorphicGetter());
        externalMethods.put("<java.util.Iterator: java.lang.Object next()>", Annotations.polymorphicGetter());
        externalMethods.put("<java.lang.Boolean: boolean "
                                             + "booleanValue()>", Annotations.polymorphicGetter());
        List<String> errors = new ArrayList<>();
        MethodTypings<String> typeCheckResult;
        if (sootOptionsContainer.isOnlyDynamic()) {
            typeCheckResult = FixedTypings.allDynamic();
        } else {
            typeCheckResult = JgsCheck.typeCheck(
                    sootOptionsContainer.getMainclass(),
                    sootOptionsContainer.getAddClassesToClasspath().toArray(new String[0]),
                    sootOptionsContainer.getAddDirsToClasspath().toArray(new String[0]),
                    externalMethods,
                    externalFields,
                    secdomain,
                    casts,
                    logger,
                    errors,
                    sootOptionsContainer.forceMonomorphicMethods()
            );
        }

        if(!errors.isEmpty()) {
            System.err.println("THERE WERE ERRORS DURING TYPCHECKING. ABORTING.");
            System.exit(-1);
        }

        // Dynamic Check
        // G.reset();
        logger.info("Start Instrumentation");
        main.Main.executeWithoutSootSetup(args,
                                          typeCheckResult, casts);
    }
}

import de.unifreiburg.cs.proglang.jgs.JgsCheck;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.ACasts;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CastsFromConstants;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Methods;
import soot.G;
import utils.parser.ArgParser;
import utils.parser.ArgumentContainer;
import main.*;
import utils.staticResults.superfluousInstrumentation.ExpectedException;

import java.util.Collections;

/**
 * This is the main entry point for jgs.
 * Launches the static analyzer, then the dynamic analyzer.
 *
 * Example:
 * -m pkg.ScratchMonomorphic -s GradualConstraints/JGSTestclasses/Scratch/src/main/java/pkg/ScratchMonomorphic.java -p . GradualConstraints/JGSTestclasses/Scratch/target/scala-2.11/classes GradualConstraints/JGSSupport/target/scala-2.11/classes -o sootOutput
 */
public class Main {
    public static void main(String[] args) {
        ArgumentContainer sootOptionsContainer = ArgParser.getSootOptions(args);
        // run static
        ACasts<LowHigh.Level> casts =
                new CastsFromConstants<>(new TypeDomain<>(new LowHigh()),
                        "<de.unifreiburg.cs.proglang.jgs.instrumentation.Casts: java.lang.Object cast(java.lang.Object)>",
                        "<de.unifreiburg.cs.proglang.jgs.instrumentation.Casts: java.lang.Object castCx(java.lang.Object)>",
                        "<de.unifreiburg.cs.proglang.jgs.instrumentation.Casts: java.lang.Object castCxEnd(java.lang.Object)>");


        /*
        typeCheck[Level](mainClass : String,
                       otherClasses : Array[String],
                       sootClasspath : Array[String],
                       // support : Array[String],
                       externalMethodAnnotations : java.util.Map[String, Annotation],
                       externalFieldAnnotations : java.util.Map[String, String],
                       secdomain : SecDomain[Level],
                       casts : ACasts[Level]) : instrumentation.Methods[Level]
         */

        // Static Check
        Methods<LowHigh.Level> typeCheckResult = JgsCheck.typeCheck(
                sootOptionsContainer.getMainclass(),
                sootOptionsContainer.getAddClassesToClasspath().toArray(new String[0]),
                sootOptionsContainer.getAddDirsToClasspath().toArray(new String[0]),
                Collections.<String, JgsCheck.Annotation>emptyMap(),
                Collections.<String, String>emptyMap(),
                new LowHigh(), casts
        );

        // Dynamic Check
        //G.reset();
        System.out.print("");
        System.out.println("== START DYNAMIC ANALYSIS ==");
        System.out.print("");
        main.Main.execute(args, true,
                typeCheckResult, false, ExpectedException.NONE.getVal(), casts);
    }
}

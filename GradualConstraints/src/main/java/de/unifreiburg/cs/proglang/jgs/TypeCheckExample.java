package de.unifreiburg.cs.proglang.jgs;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.ACasts;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Casts;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CastsFromConstants;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Methods;
import soot.Scene;
import soot.SootMethod;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by fennell on 3/6/17.
 */
public class TypeCheckExample {

    public static void main(String[] args) {

        ACasts<LowHigh.Level> casts =
                new CastsFromConstants<>(new TypeDomain<>(new LowHigh()),
                                         "<de.unifreiburg.cs.proglang.jgs.instrumentation.Casts: java.lang.Object cast(java.lang.Object)>",
                                         "<de.unifreiburg.cs.proglang.jgs.instrumentation.Casts: java.lang.Object castCx(java.lang.Object)>",
                                         "<de.unifreiburg.cs.proglang.jgs.instrumentation.Casts: java.lang.Object castCxEnd(java.lang.Object)>");
        Methods<LowHigh.Level> typeCheckResult = JgsCheck.typeCheck(
                "pkg.ScratchMonomorphic",
                new String[]{"GradualConstraints/JGSTescclasses/Scratch/src/main/java/pkg/ScratchMonomorphic.java"},
                new String[]{".", "GradualConstraints/JGSTestclasses/Scratch/target/scala-2.11/classes", "GradualConstraints/JGSSupport/target/scala-2.11/classes"},
                Collections.<String, JgsCheck.Annotation>emptyMap(),
                Collections.<String, String>emptyMap(),
                new LowHigh(), casts
        );

        for (SootMethod m : Scene.v().getMainClass().getMethods()) {
            System.out.print("Checking result of method: " + m + ": ")   ;
            try {
                typeCheckResult.getMonomorphicInstantiation(m);
                System.out.println("ok");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


    }
}

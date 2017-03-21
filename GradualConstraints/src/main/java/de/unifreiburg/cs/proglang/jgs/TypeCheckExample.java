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
import java.util.logging.Logger;

public class TypeCheckExample {

    public static void main(String[] args) {

        ACasts<LowHigh.Level> casts =
                new CastsFromConstants<>(new TypeDomain<>(new LowHigh()),
                                         "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object cast(java.lang.String,java.lang.Object)>",
                                         "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object castCx(java.lang.Object)>",
                                         "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object castCxEnd(java.lang.Object)>");
        Methods<LowHigh.Level> typeCheckResult = JgsCheck.typeCheck(
                "pkg.ScratchMonomorphic",
                new String[]{"GradualConstraints/JGSTestclasses/Scratch/src/main/java/pkg/ScratchMonomorphic.java"},
                new String[]{".", "GradualConstraints/JGSTestclasses/Scratch/target/scala-2.11/classes",
                        "GradualConstraints/JGSSupport/target/scala-2.11/classes"},
                Collections.<String, JgsCheck.Annotation>emptyMap(),
                Collections.<String, String>emptyMap(),
                new LowHigh(), casts, Logger.getGlobal(),
                new java.util.ArrayList<String>()
        );

        for (SootMethod m : Scene.v().getMainClass().getMethods()) {
            System.out.print("Checking vartyping of method: " + m + ": ");
            try {
                typeCheckResult.getVarTyping(m);
                System.out.println("ok");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        for (SootMethod m : Scene.v().getMainClass().getMethods()) {
            System.out.print("Checking cxtyping of method: " + m + ": ");
            try {
                typeCheckResult.getCxTyping(m);
                System.out.println("ok");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        for (SootMethod m : Scene.v().getMainClass().getMethods()) {
            System.out.print("Checking instatiation of method: " + m + ": ")   ;
            try {
                typeCheckResult.getMonomorphicInstantiation(m);
                System.out.println("ok");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


    }
}

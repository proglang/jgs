package de.unifreiburg.cs.proglang.jgs;

/**
 * Some useful, standard annotations for external methods.
 */
public class Annotations {
    // plus, some usefull standard annotations
    public static JgsCheck.Annotation pureInputToOutput(int numberOfParameters) {
        String[] args = new String[numberOfParameters];
        for (int i = 0; i < numberOfParameters; i++) {
            args[i] = String.format("@%d <= @ret", i);
        }
        return new JgsCheck.Annotation(args, new String[]{});
    }
    public static JgsCheck.Annotation polymorphicGetter(){
        return pureInputToOutput(0);
    }

    // TODO: should be "public sink" and should not mention "LOW"
    public static JgsCheck.Annotation lowSink(int numberOfParameters) {
        String[] args = new String[numberOfParameters];
        for (int i = 0; i < numberOfParameters; i++) {
            args[i] = String.format("@%d <= LOW", i);
        }
       return new JgsCheck.Annotation(args, new String[]{"LOW"});
    }
}

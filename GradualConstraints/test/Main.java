import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.cli.Format;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Effects;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.SigConstraint;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.SigConstraintSet;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Signature;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.ConstraintParser;
import de.unifreiburg.cs.proglang.jgs.typing.ClassHierarchyTyping;
import org.apache.commons.lang3.StringUtils;
import org.javafp.parsecj.Reply;
import org.javafp.parsecj.State;
import soot.JastAddJ.Opt;
import soot.JastAddJ.Signatures;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods.extractSignatureFromTags;
import static de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods.extractStringArrayAnnotation;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.emptyEffect;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeEffects;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeSignature;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Main {

    private static Logger log =
            Logger.getLogger("de.unifreiburg.cs.proglang.jgs.typing.test");

    private static <T> Optional<T> getAtMostOneOrThrow(Stream<T> s, RuntimeException e) {
        Optional<T> result = Optional.empty();
        Iterator<T> iter = s.iterator();
        if (iter.hasNext()) {
            result = Optional.of(iter.next());
            if (iter.hasNext()) {
                throw e;
            }
        }
        return result;
    }

    private static <T> T getReplyOrThrow(Reply<?, T> reply, Function<Exception, RuntimeException> re) {
        T result;
        try {
            result = reply.getResult();
        } catch (Exception e) {
            throw re.apply(e);
        }
        return result;
    }

    private static Stream<SigConstraint<Level>> parseConstraints(List<String> constraintStrings) {
        Stream<SigConstraint<Level>> constraints =
                constraintStrings.stream().map((String s) -> {
                            return getReplyOrThrow(new ConstraintParser<Level>(types.typeParser()).constraintParser().parse(State.of(s)),
                                                   e -> new RuntimeException(String.format("Error parsing constraint %s;\n%s", s, e.getMessage())))
                                    ;
                        });
        return constraints;
    }

    private static Stream<Type<Level>> parseEffects(List<String> effectStrings) {
        Stream<Type<Level>> effects = effectStrings.stream().map(
                            (String s) ->
                                    types.typeParser().parse(s)
                                         .orElseGet(() -> {
                                             throw new RuntimeException(String.format("Error parsing type %s", s));
                                         }));

        return effects;
    }


    private static Signature<Level> parseSignature(SootMethod m) {
        List<String> constraintStrings =
                getAtMostOneOrThrow(extractStringArrayAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Constraints;", m.getTags().stream()),
                                    new IllegalArgumentException(
                                            "Found more than one constraint annotation on "
                                            + m.getName())).orElse(emptyList());
        List<String> effectStrings =
                getAtMostOneOrThrow(extractStringArrayAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Effects;", m.getTags().stream()),
                                    new IllegalArgumentException(
                                            "Found more than one effect annotation on "
                                            + m.getName())).orElse(emptyList());
        return makeSignature(m.getParameterCount(),
                             parseConstraints(constraintStrings).collect(toList()),
                             makeEffects(parseEffects(effectStrings)
                                                 .collect(toList())));
    }

    private static void addSpecialSignatures(Map<SootMethod, Signature<Level>> signatureMap) {
        Scene s = Scene.v();
        Set<String> specialClasses =
                Stream.of("java.lang.Object")
                      .collect(toSet());
        s.getClasses().stream()
         .filter(c -> { return c.isApplicationClass() || specialClasses.contains(c.getName());})
         .flatMap(c -> c.getMethods().stream())
         .filter(m -> { return m.getName().contains("<init>")
                      || m.getName().contains("<clinit>");})
         .forEach(m -> {
             if (!signatureMap.containsKey(m)) {
                 signatureMap.put(m, makeSignature(m.getParameterCount(),
                                                   emptyList(),
                                                   emptyEffect()));
             }
         });
    }

    public static void main(String[] args) {

        log.setLevel(java.util.logging.Level.WARNING);

        Options o = Options.v();
        o.set_soot_classpath("JGSSupport/bin:testclasses-java:result/lib/openjdk/jre/lib/rt.jar");
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
        Map<SootMethod, Signature<Level>> signatureMap = new HashMap<>();
        for (SootClass c : classes) {
            System.out.println(c.toString());

            for (SootMethod m : c.getMethods()) {
                System.out.println(" " + m.toString());
                Signature<Level> sig = parseSignature(m);
                System.out.println("  sig:" + sig.toString());
                signatureMap.put(m, sig);
            }
            System.out.println();
        }

        System.out.print("Checking class hierarchy: ");
        addSpecialSignatures(signatureMap);
        SignatureTable<Level> signatures =
                SignatureTable.makeTable(signatureMap);
        ClassHierarchyTyping.Result<Level>
                result = ClassHierarchyTyping.check(csets,
                                                    types,
                                                    signatures,
                                                    s.getApplicationClasses().stream());

        System.out.println(Format.pprint(Format.classHierarchyCheck(result)));
    }
}

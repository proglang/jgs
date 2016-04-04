import de.unifreiburg.cs.proglang.jgs.cli.Format;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.CastsFromMapping;
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.SigConstraint;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Signature;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.ConstraintParser;
import de.unifreiburg.cs.proglang.jgs.typing.ClassHierarchyTyping;
import de.unifreiburg.cs.proglang.jgs.typing.MethodTyping;
import de.unifreiburg.cs.proglang.jgs.typing.TypingException;
import de.unifreiburg.cs.proglang.jgs.util.Interop;
import org.javafp.parsecj.Reply;
import org.javafp.parsecj.State;
import soot.*;
import soot.options.Options;

import java.util.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods.extractStringAnnotation;
import static de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods.extractStringArrayAnnotation;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.emptyEffect;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeEffects;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeSignature;
import static de.unifreiburg.cs.proglang.jgs.util.Interop.asScalaIterator;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class TestMain {

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
                        Interop.asJavaOptional(types.typeParser().parse(s))
                                     .orElseGet(() -> {
                                 throw new RuntimeException(String.format("Error parsing type %s", s));
                             }));

        return effects;
    }


    private static Signature<Level> parseSignature(SootMethod m) {
        List<String> constraintStrings =
                getAtMostOneOrThrow(extractStringArrayAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Constraints;", asScalaIterator(m.getTags())).stream(),
                                    new IllegalArgumentException(
                                            "Found more than one constraint annotation on "
                                            + m.getName())).orElse(emptyList());
        List<String> effectStrings =
                getAtMostOneOrThrow(extractStringArrayAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Effects;", asScalaIterator(m.getTags())).stream(),
                                    new IllegalArgumentException(
                                            "Found more than one effect annotation on "
                                            + m.getName())).orElse(emptyList());
        return makeSignature(m.getParameterCount(),
                             parseConstraints(constraintStrings).collect(toList()),
                             makeEffects(parseEffects(effectStrings)
                                                 .collect(toList())));
    }

    private static Type<Level> parseType(SootField f) {
        return getAtMostOneOrThrow(extractStringAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Sec;",
                                                           asScalaIterator(f.getTags())).stream(),
                                   new IllegalArgumentException(
                                           "Found more than one security level on "
                                           + f.getName()))
                .map(s -> Interop.asJavaOptional(types.typeParser().parse(s)).orElseGet(() -> {
                    throw new IllegalArgumentException(
                            "Unable to parse security type " + s);
                }))
                .orElse(types.pub());
    }

    private static void addSpecialSignatures(Map<SootMethod, Signature<Level>> signatureMap) {
        Scene s = Scene.v();
        Set<String> specialClasses =
                Stream.of("java.lang.Object")
                      .collect(toSet());
        s.getClasses().stream()
         .filter(c -> {
             return c.isApplicationClass()
                    || specialClasses.contains(c.getName());
         })
         .flatMap(c -> c.getMethods().stream())
         .filter(m -> {
             return m.getName().contains("<init>")
                    || m.getName().contains("<clinit>");
         })
         .forEach(m -> {
             if (!signatureMap.containsKey(m)) {
                 signatureMap.put(m, makeSignature(m.getParameterCount(),
                                                   emptyList(),
                                                   emptyEffect()));
             }
         });
        SootMethod printInt = s.getSootClass("de.unifreiburg.cs.proglang.jgs.support.IO").getMethodByName("printInt");
        signatureMap.put(printInt, makeSignature(1, singletonList(leS(0, Symbol.literal(TLOW))), makeEffects(singletonList(TLOW))));
        SootMethod valueOfInt = s.getSootClass("java.lang.Integer").getMethod("valueOf", singletonList(IntType.v()));
        SootMethod intValue = s.getSootClass("java.lang.Integer").getMethod("intValue", emptyList());
        signatureMap.put(valueOfInt, makeSignature(1, singletonList(leS(0, Symbol.ret())), emptyEffect()));
        signatureMap.put(intValue, makeSignature(0, emptyList(), emptyEffect()));
    }

    public final static String TESTCLASSES_DIR = "JGSTestclasses/src";

    public static void main(String[] args) {

        log.setLevel(java.util.logging.Level.WARNING);

        Options o = Options.v();
        o.set_soot_classpath("JGSSupport/bin:"+TESTCLASSES_DIR+":openjdk7/lib/openjdk/jre/lib/rt.jar");
        o.set_process_dir(Collections.singletonList(TESTCLASSES_DIR));

        Scene s = Scene.v();
        log.info(String.format("Classpath: %s", s.getSootClassPath()));

        s.loadNecessaryClasses();
        log.info(String.format("Number of classes found: %d", s.getClasses().size()));

        List<SootClass> classes =
                s.getApplicationClasses().stream().collect(toList());

        // Create the signature table
        Map<SootMethod, Signature<Level>> signatureMap = new HashMap<>();
        Map<SootField, Type<Level>> fieldTypeTable = new HashMap<>();
        log.info("Application classes: ");
        for (SootClass c : classes) {
            log.info(" " + c.toString());

            for (SootField f : c.getFields()) {
                log.info("  " + f.toString());
                Type<Level> t = parseType(f);
                log.info("    security level: " + t.toString());
                fieldTypeTable.put(f, t);
            }

            for (SootMethod m : c.getMethods()) {
                log.info("  " + m.toString());
                Signature<Level> sig = parseSignature(m);
                log.info("    sig:" + sig.toString());
                log.info("    active body? " + (m.hasActiveBody() ? "yes" : "no"));
                signatureMap.put(m, sig);
            }
        }


        Map<SootMethod, Signature<Level>> special = new HashMap<>();
        addSpecialSignatures(special);
        log.info("Special signatures: ");
        for (Map.Entry<SootMethod, Signature<Level>> e : special.entrySet()) {
            log.info(String.format("  %s : %s", e.getKey(), e.getValue()));
        }

        signatureMap.putAll(special);
        SignatureTable<Level> signatures =
                SignatureTable.makeTable(signatureMap);
        FieldTable<Level> fieldTable = new FieldTable<>(fieldTypeTable);

        // Create the field table

        System.out.print("Checking class hierarchy: ");
        ClassHierarchyTyping.Result<Level>
                result = ClassHierarchyTyping.check(csets,
                                                    types,
                                                    signatures,
                                                    s.getApplicationClasses().stream());

        System.out.println(Format.pprint(Format.classHierarchyCheck(result)));

        // configuring cast methods
        Map<String,String> valueCasts = new HashMap<>();
        valueCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castHighToDyn", "HIGH ~> ?");
        valueCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castLowToDyn", "LOW ~> ?");
        valueCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castDynToLow", "? ~> LOW");
        Map<String,String> cxCasts = new HashMap<>();
        cxCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castCxHighToDyn", "HIGH ~> ?");
        cxCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castCxLowToDyn", "LOW ~> ?");
        cxCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castCxDynToLow", "? ~> LOW");
        cxCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castCxDynToHigh", "? ~> HIGH");
        String cxCastEnd = "de.unifreiburg.cs.proglang.jgs.support.Casts.castCxEnd";

        Casts<Level> casts = new CastsFromMapping<Level>(CastsFromMapping.<Level>parseConversionMap(types.typeParser(), valueCasts).get(),
                                                    CastsFromMapping.<Level>parseConversionMap(types.typeParser(), cxCasts).get(),
                                                    cxCastEnd);

        System.out.print("\nChecking method bodies: ");
        for (SootClass c : classes) {
            for (SootMethod m : c.getMethods()) {
                log.info(" Checking body of method: " + m.toString());
                MethodTyping<Level> methodTyping =
                        new MethodTyping<>(csets, cstrs,
                                           casts);
                MethodTyping.Result<Level> mresult;
                try {
                    mresult =
                            methodTyping.check(new TypeVars(), signatures, fieldTable, m);
                    System.out.println(String.format("* Type checking method %s:  %s", m.toString(), Format.pprint(Format.methodTypingResult(mresult))));
                } catch (TypingException e) {
                    System.err.println(" failed: " + e.getMessage());
                    e.printStackTrace();
                }
                System.out.println();
            }
        }
    }
}

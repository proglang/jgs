package end2endtest;

import analyzer.level2.storage.LowMediumHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.*;
import de.unifreiburg.cs.proglang.jgs.typing.ExampleTypes;
import soot.Local;
import soot.SootMethod;
import soot.jimple.Stmt;
import util.staticResults.implementation.Dynamic;
import de.unifreiburg.cs.proglang.jgs.typing.FixedTypings;

/**
 * Describe how we want the fake analysis results to be
 */
class FakeMethodTypings {

    // same as ALL_DYNAMIC, except for Cx, which returns public on any request
    public static final MethodTypings<LowMediumHigh.Level> CX_PUBLIC = new MethodTypings<LowMediumHigh.Level>() {


        @Override
        public Instantiation<LowMediumHigh.Level> getSingleInstantiation(SootMethod m, Type<LowMediumHigh.Level> defaultType) {
            return FixedTypings.<LowMediumHigh.Level>allDynamic().getSingleInstantiation(m, defaultType);
        }

        @Override
        public VarTyping<LowMediumHigh.Level> getVarTyping(SootMethod m) {
            return FixedTypings.<LowMediumHigh.Level>allDynamic().getVarTyping(m);
        }

        @Override
        public CxTyping<LowMediumHigh.Level> getCxTyping(SootMethod m) {
            return FixedTypings.<LowMediumHigh.Level>allPublic().getCxTyping(m);
        }

        @Override
        public Effect<LowMediumHigh.Level> getEffectType(SootMethod m) {
            return FixedTypings.<LowMediumHigh.Level>allDynamic().getEffectType(m);
        }
    };

    // just instantiation of dynamic
    public static final MethodTypings<LowMediumHigh.Level> VAR_AND_CX_PUBLIC = new MethodTypings<LowMediumHigh.Level>() {


        @Override
        public Instantiation<LowMediumHigh.Level> getSingleInstantiation(SootMethod m, Type<LowMediumHigh.Level> defaultType) {
            return FixedTypings.<LowMediumHigh.Level>allDynamic().getSingleInstantiation(m, defaultType);
        }

        @Override
        public VarTyping<LowMediumHigh.Level> getVarTyping(SootMethod m) {
            return FixedTypings.<LowMediumHigh.Level>allPublic().getVarTyping(m);
        }

        @Override
        public CxTyping<LowMediumHigh.Level> getCxTyping(SootMethod m) {
            return FixedTypings.<LowMediumHigh.Level>allPublic().getCxTyping(m);
        }

        @Override
        public Effect<LowMediumHigh.Level> getEffectType(SootMethod m) {
            return FixedTypings.<LowMediumHigh.Level>allPublic().getEffectType(m);
        }
    };

    // AllDynamic especially to test if typing map in CustomTyping.scala works
    public static final MethodTypings<LowMediumHigh.Level> CUSTOM_LowPlusPublic_AllDynamic = FixedTypings.allDynamic();

    // Mapping such that i is Dynamic, j is public and res = i + j
    public static final MethodTypings<LowMediumHigh.Level> CUSTOM_LowPlusPublic =
            /*
            case CUSTOM_LowPlusPublic:
                ResultsServer.custom_lowPlugPublic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setPublic(fakeInstantiationMap, allClasses);
                break;
             */
            new MethodTypings<LowMediumHigh.Level>() {
                @Override
                public Instantiation<LowMediumHigh.Level> getSingleInstantiation(SootMethod m, Type<LowMediumHigh.Level> defaultType) {
                    return FixedTypings.<LowMediumHigh.Level>allPublic().getSingleInstantiation(m, defaultType);
                }

                private Type<LowMediumHigh.Level> fromBool(boolean isDynamic) {
                   if (isDynamic) {
                       return new Dynamic<>();
                   } else {
                       return new ExampleTypes.Static<>();
                   }
                }



                @Override
                public VarTyping<LowMediumHigh.Level> getVarTyping(SootMethod m) {
                    return new VarTyping<LowMediumHigh.Level>() {
                        @Override
                        public Type<LowMediumHigh.Level> getBefore(Instantiation<LowMediumHigh.Level> instantiation, Stmt s, Local l) {
                            return fromBool(FakeTypingMaps.isDynamicBefore(FakeTypingMaps.LowPlusPublic(), s.toString(), l.toString()));
                        }

                        @Override
                        public Type<LowMediumHigh.Level> getAfter
                                (Instantiation<LowMediumHigh.Level> instantiation, Stmt s, Local l) {
                            return fromBool(FakeTypingMaps.isDynamicAfter(FakeTypingMaps.LowPlusPublic(), s.toString(), l.toString()));
                        }
                    };
                }

                @Override
                public CxTyping<LowMediumHigh.Level> getCxTyping(SootMethod m) {
                    return FixedTypings.<LowMediumHigh.Level>allPublic().getCxTyping(m);
                }

                @Override
                public Effect<LowMediumHigh.Level> getEffectType(SootMethod m) {
                    return FixedTypings.<LowMediumHigh.Level>allPublic().getEffectType(m);
                }
            };
}
/*
{
            case ALL_DYNAMIC:
                ResultsServer.setDynamic(fakeVarTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case CX_PUBLIC:
                ResultsServer.setDynamic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case VAR_AND_CX_PUBLIC:
                ResultsServer.setPublic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case allPublic():
                ResultsServer.setPublic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setPublic(fakeInstantiationMap, allClasses);
                break;
            case CUSTOM_LowPlusPublic_AllDynamic:
                ResultsServer.custom_lowPlusPublic_AllDynamic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setPublic(fakeInstantiationMap, allClasses);
                break;
            case CUSTOM_LowPlusPublic:
                ResultsServer.custom_lowPlugPublic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setPublic(fakeInstantiationMap, allClasses);
                break;
            default:
                throw new InternalAnalyzerException("Invalid analysis result requested!");
        }
 */

package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains
        .UnknownSecurityLevelException;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.iterators.TransformIterator;
import scala.Option;

import java.util.Arrays;
import java.util.Iterator;

/**
 * The domain of security types.
 *
 * @param <Level> The type of security levels.
 * @author Luminous Fennell
 */
public class TypeDomain<Level> {

    public SecDomain<Level> getSecDomain() {
        return secDomain;
    }

    private final SecDomain<Level> secDomain;
    private final Type<Level> dyn;
    private final Type<Level> pub;

    public TypeDomain(SecDomain<Level> secDomain) {
        super();
        this.secDomain = secDomain;
        this.dyn = new Dyn<>();
        this.pub = new Public<>();
    }

    public Type<Level> dyn() {
        return this.dyn;
    }

    public Type<Level> pub() {
        return this.pub;
    }

    public Type<Level> level(Level level) {
        return new SecLevel<>(level);
    }

    public AnnotationParser<Type<Level>> typeParser() {
        final TypeDomain<Level> self = this;
        return new AnnotationParser<Type<Level>>() {
            @Override
            public Option<Type<Level>> parse(String s) {
                if (s.equals("?")) {
                    return Option.apply(dyn());
                } else if (s.equals("pub")) {
                    return Option.apply(pub());
                } else {
                    // TODO: we remove the levelParser from SecDomain, so we should remove the typeParser here.
                    try {
                        Level level = secDomain.readLevel(s);
                        return Option.apply(self.level(level));
                    } catch (UnknownSecurityLevelException e){
                        return Option.empty();
                    }
                }
            }

        };
    }


    /**
     * Enumerates all types by prepending "pub" and "dyn" to the security
     * domain. Thus, this method only works if the security domain is also
     * enumerable.
     */
    public Iterator<Type<Level>> enumerate() {
        final TypeDomain<Level> self = this;
        Iterator<Type<Level>> secLevels =
                new TransformIterator<>(this.secDomain.enumerate(), new Transformer<Level, Type<Level>>() {
                    @Override
                    public Type<Level> transform(Level level) {
                        return self.level(level);
                    }
                });
        return new IteratorChain<>(Arrays.asList(this.pub(), this.dyn()).iterator(),
                                   secLevels);
    }

    private Option<Level> tryGetLevel(Type<Level> t) {
        return t.accept(new TypeSwitch<Level, Option<Level>>() {

            @Override
            public Option<Level> caseLevel(SecLevel<Level> level) {
                return Option.apply(level.getLevel());
            }

            @Override
            public Option<Level> caseDyn(Dyn<Level> dyn) {
                return Option.empty();
            }

            @Override
            public Option<Level> casePublic(Public<Level> pub) {
                return Option.empty();
            }
        });
    }

    public Type<Level> glb(Type<Level> t1, final Type<Level> t2) {
        return t1.accept(new TypeSwitch<Level, Type<Level>>() {

            @Override
            public Type<Level> caseLevel(SecLevel<Level> level) {
                final Level l1 = level.getLevel();
                Option<Level> maybeType = tryGetLevel(t2);
                if (maybeType.isDefined()) {
                    return level(secDomain.glb(l1, maybeType.get()));
                } else {
                    return pub();
                }
            }

            @Override
            public Type<Level> caseDyn(Dyn<Level> dyn) {
                return t2.equals(dyn) ? dyn : pub();
            }

            @Override
            public Type<Level> casePublic(Public<Level> pub) {
                return pub;
            }

        });
    }

    public boolean le(Type<Level> t1, final Type<Level> t2) {
        return t1.accept(new TypeSwitch<Level, Boolean>() {

            @Override
            Boolean caseLevel(SecLevel<Level> level) {
                final Level l1 = level.getLevel();
                Option<Level> olevel = tryGetLevel(t2);
                if (olevel.isDefined()) {
                    return secDomain.le(l1, olevel.get()) ;
                } else {
                    return false;
                }
            }

            @Override
            Boolean caseDyn(Dyn<Level> dyn) {
                return t2.equals(dyn);
            }

            @Override
            Boolean casePublic(Public<Level> pub) {
                return true;
            }
        });
    }

    /**
     * Visitor for security types
     *
     * @param <Level>
     * @param <T>
     * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
     */
    static abstract class TypeSwitch<Level, T> {
        abstract T caseLevel(SecLevel<Level> level);

        abstract T caseDyn(Dyn<Level> dyn);

        abstract T casePublic(Public<Level> pub);
    }

    /**
     * Security types.
     *
     * @param <Level> concreteConstraints security level
     * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
     */
    public static abstract class Type<Level> implements de.unifreiburg.cs.proglang.jgs.instrumentation.Type<Level> {
        // TODO: remove TypeSwitch and pattern-match on TypeView instead (implies converting to Scala)
        abstract <T> T accept(TypeSwitch<Level, T> sw);

        public abstract TypeViews.TypeView<Level> inspect();

    }

    static class SecLevel<Level> extends Type<Level> {

        private final Level level;

        public SecLevel(Level level) {
            super();
            this.level = level;
        }

        @Override
        public String toString() {
            return String.format("[%s]", level.toString());
        }

        @Override
        public <T> T accept(TypeSwitch<Level, T> sw) {
            return sw.caseLevel(this);
        }

        @Override
        public TypeViews.TypeView<Level> inspect() {
            return new TypeViews.Lit<>(this.level);
        }

        @Override
        public boolean isStatic() {
            return true;
        }

        @Override
        public boolean isDynamic() {
            return false;
        }

        @Override
        public boolean isPublic() {
            return false;
        }

        public Level getLevel() {
            return level;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((level == null) ? 0 : level.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            @SuppressWarnings("rawtypes")
            SecLevel other = (SecLevel) obj;
            if (level == null) {
                if (other.level != null)
                    return false;
            } else if (!level.equals(other.level))
                return false;
            return true;
        }
    }

    static class Dyn<Level> extends Type<Level> {

        @Override
        public <T> T accept(TypeSwitch<Level, T> sw) {
            return sw.caseDyn(this);
        }

        @Override
        public TypeViews.TypeView<Level> inspect() {
            return new TypeViews.Dyn<Level>();
        }

        @Override
        public String toString() {
            return "?";
        }


        @Override
        public boolean isStatic() {
            return false;
        }

        @Override
        public boolean isDynamic() {
            return true;
        }

        @Override
        public boolean isPublic() {
            return false;
        }

        @Override
        public Level getLevel() {
            throw new IllegalArgumentException("dynamic type has not level");
        }
    }

    static class Public<Level> extends Type<Level> {

        @Override
        public <T> T accept(TypeSwitch<Level, T> sw) {
            return sw.casePublic(this);
        }

        @Override
        public TypeViews.TypeView<Level> inspect() {
            return new TypeViews.Pub<Level>();
        }

        @Override
        public String toString() {
            return "PUB";
        }

        @Override
        public boolean isStatic() {
            return false;
        }

        @Override
        public boolean isDynamic() {
            return false;
        }

        @Override
        public boolean isPublic() {
            return true;
        }

        @Override
        public Level getLevel() {
            throw new IllegalArgumentException("public type has no level");
        }
    }

}

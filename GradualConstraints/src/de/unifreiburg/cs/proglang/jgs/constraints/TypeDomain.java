package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;
import soot.JastAddJ.Opt;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The domain of security types.
 * 
 * @author Luminous Fennell 
 * 
 * @param <Level>
 *            The type of security levels.
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
        return s -> {
            if (s.equals("?")) {
                return Optional.of(dyn());
            } else if (s.equals("pub")) {
                return Optional.of(pub());
            } else {
                return secDomain.levelParser().parse(s).map(this::level);
            }
        };
    }


    /**
     * Enumerates all types by prepending "pub" and "dyn" to the security
     * domain. Thus, this method only works if the security domain is also
     * enumerable.
     */
    public Stream<Type<Level>> enumerate() {
        return Stream.concat(Arrays.asList(this.pub(), this.dyn()).stream(),
                             this.secDomain.enumerate().map(l -> this.level(l)));
    }

    private Optional<Level> tryGetLevel(Type<Level> t) {
        return t.accept(new TypeSwitch<Level,Optional<Level>>() {

            @Override
            public Optional<Level> caseLevel(SecLevel<Level> level) {
                return Optional.of(level.getLevel());
            }

            @Override
            public Optional<Level> caseDyn(Dyn<Level> dyn) {
                return Optional.empty();
            }

            @Override
            public Optional<Level> casePublic(Public<Level> pub) {
                return Optional.empty();
            }
        });
    }

    public Type<Level> glb(Type<Level> t1, Type<Level> t2) {
        return t1.accept(new TypeSwitch<Level,Type<Level>>() {

            @Override
            public Type<Level> caseLevel(SecLevel<Level> level) {
                Level l1 = level.getLevel();
                Optional<Type<Level>> maybeType =
                    tryGetLevel(t2).map(l2 -> level(secDomain.glb(l1, l2)));
                return maybeType.orElse(pub());
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

    public boolean le(Type<Level> t1, Type<Level> t2) {
        return t1.accept(new TypeSwitch<Level,Boolean>() {

            @Override
            Boolean caseLevel(SecLevel<Level> level) {
                Level l1 = level.getLevel();
                return tryGetLevel(t2).map(l2 -> secDomain.le(l1, l2))
                                      .orElse(false);
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
     * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
     *
     * @param <Level>
     * @param <T>
     */
    static abstract class TypeSwitch<Level, T> {
        abstract T caseLevel(SecLevel<Level> level);

        abstract T caseDyn(Dyn<Level> dyn);

        abstract T casePublic(Public<Level> pub);
    }

    /**
     * Security types.
     * 
     * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
     *
     * @param <Level>
     *            concreteConstraints security level
     */
    public static abstract class Type<Level> {
        abstract <T> T accept(TypeSwitch<Level, T> sw);
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
        public <T> T accept(TypeSwitch<Level,T> sw) {
            return sw.caseDyn(this);
        }

        @Override
        public String toString() {
            return "?";
        }
        

    }

    static class Public<Level> extends Type<Level> {

        @Override
        public <T> T accept(TypeSwitch<Level,T> sw) {
            return sw.casePublic(this);
        }

        @Override
        public String toString() {
            return "PUB";
        }

    }

}

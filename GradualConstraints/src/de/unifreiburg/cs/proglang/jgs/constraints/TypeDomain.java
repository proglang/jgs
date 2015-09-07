package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Optional;

/**
 * The domain of security types.
 * 
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 * 
 * @param <Level>
 *            The type of security levels.
 */
public class TypeDomain<Level> {

    private final SecDomain<Level> secDomain;
    private final Type dyn;
    private final Type pub;

    public TypeDomain(SecDomain<Level> secDomain) {
        super();
        this.secDomain = secDomain;
        this.dyn = new Dyn();
        this.pub = new Public();
    }

    public Type dyn() {
        return this.dyn;
    }

    public Type pub() {
        return this.pub;
    }

    public Type level(Level level) {
        return new SecLevel(level);
    }

    private Optional<Level> tryGetLevel(Type t) {
        return t.accept(new TypeSwitch<Optional<Level>>() {

            @Override
            public Optional<Level> caseLevel(SecLevel level) {
                return Optional.of(level.getLevel());
            }

            @Override
            public Optional<Level> caseDyn(Dyn dyn) {
                return Optional.empty();
            }

            @Override
            public Optional<Level> casePublic(Public pub) {
                return Optional.empty();
            }
        });
    }

    public Type glb(Type t1, Type t2) {
        return t1.accept(new TypeSwitch<Type>() {

            @Override
            public Type caseLevel(SecLevel level) {
                Level l1 = level.getLevel();
                Optional<Type> maybeType =
                    tryGetLevel(t2).map(l2 -> level(secDomain.glb(l1, l2)));
                return maybeType.orElse(pub());
            }

            @Override
            public Type caseDyn(Dyn dyn) {
                return t2.equals(dyn) ? dyn : pub();
            }

            @Override
            public Type casePublic(Public pub) {
                return pub;
            }

        });
    }

    public boolean le(TypeDomain<Level>.Type t1, Type t2) {
        return t1.accept(new TypeSwitch<Boolean>() {

            @Override
            Boolean caseLevel(TypeDomain<Level>.SecLevel level) {
                Level l1 = level.getLevel();
                return tryGetLevel(t2).map(l2 -> secDomain.le(l1, l2)).orElse(false);
            }

            @Override
            Boolean caseDyn(TypeDomain<Level>.Dyn dyn) {
                return t2.equals(dyn);
            }

            @Override
            Boolean casePublic(TypeDomain<Level>.Public pub) {
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
    abstract class TypeSwitch<T> {
        abstract T caseLevel(SecLevel level);

        abstract T caseDyn(Dyn dyn);

        abstract T casePublic(Public pub);
    }

    /**
     * Security types.
     * 
     * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
     *
     * @param <Level>
     *            concrete security level
     */
    abstract class Type {
        abstract <T> T accept(TypeSwitch<T> sw);
    }

    class SecLevel extends Type {

        private final Level level;

        public SecLevel(Level level) {
            super();
            this.level = level;
        }

        @Override
        public <T> T accept(TypeSwitch<T> sw) {
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
            @SuppressWarnings("unchecked")
            SecLevel other = (SecLevel) obj;
            if (level == null) {
                if (other.level != null)
                    return false;
            } else if (!level.equals(other.level))
                return false;
            return true;
        }
    }

    class Dyn extends Type {

        @Override
        public <T> T accept(TypeSwitch<T> sw) {
            return sw.caseDyn(this);
        }

    }

    class Public extends Type {

        @Override
        public <T> T accept(TypeSwitch<T> sw) {
            return sw.casePublic(this);
        }

    }

}

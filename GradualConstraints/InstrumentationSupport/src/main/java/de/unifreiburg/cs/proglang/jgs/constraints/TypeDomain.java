package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains
        .UnknownSecurityLevelException;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.iterators.TransformIterator;
import scala.Option;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.*;


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
    private final TypeView<Level> dyn;
    private final TypeView<Level> pub;

    public TypeDomain(SecDomain<Level> secDomain) {
        super();
        this.secDomain = secDomain;
        this.dyn = new TypeViews.Dyn<>();
        this.pub = new TypeViews.Pub<>();
    }

    public TypeView<Level> dyn() {
        return this.dyn;
    }

    public TypeView<Level> pub() {
        return this.pub;
    }

    public TypeView<Level> level(Level level) {
        return new TypeViews.Lit<>(level);
    }

    /**
     * A parser for types.
     *
     * It reads static types like the secdomain.
     * "?" reads as the dynamic type.
     * "pub" reads as the public type.
     * @return
     */
    public AnnotationParser<TypeView<Level>> typeParser() {
        final TypeDomain<Level> self = this;
        return new AnnotationParser<TypeView<Level>>() {
            @Override
            public Option<TypeView<Level>> parse(String s) {
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
    public Iterator<TypeView<Level>> enumerate() {
        final TypeDomain<Level> self = this;
        Iterator<TypeView<Level>> secLevels =
                new TransformIterator<>(this.secDomain.enumerate(), new Transformer<Level, TypeView<Level>>() {
                    @Override
                    public TypeView<Level> transform(Level level) {
                        return self.level(level);
                    }
                });
        return new IteratorChain<>(Arrays.asList(this.pub(), this.dyn()).iterator(),
                                   secLevels);
    }

    public TypeViews.TypeView<Level> glb(TypeViews.TypeView<Level> t1, final TypeViews.TypeView<Level> t2) {
        if (t1.isStatic()) {
            final Level l1 = t1.getLevel();
            if (t2.isStatic()) {
                return level(secDomain.glb(l1, t2.getLevel()));
            } else {
                return pub();
            }
        } else if (t1.isDynamic()) {
            return t2.equals(dyn) ? dyn : pub();
        } else  { // t1.isPublic()
            return pub;
        }
    }

    public boolean le(TypeView<Level> t1, final TypeView<Level> t2) {
        if (t1.isStatic()) {
            if (t2.isStatic()) {
                Level l1 = t1.getLevel();
                return secDomain.le(l1, t2.getLevel());
            } else {
                return false;
            }
        } else if (t1.isDynamic()) {
            return t2.equals(dyn);

        } else { // t1.isPublic()
            return true;
        }
    }

}

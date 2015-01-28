package security;

import constraints.IComponent;

public abstract class ALevel implements ILevel {

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract String getName();

    public abstract String toString();

    public IComponent changeSignature(String signature) {
        return this;
    }

}

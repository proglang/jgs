package security;

import constraints.IComponentConstant;

public interface ILevel extends IComponentConstant {

    public boolean equals(Object obj);

    public int hashCode();

    public String getName();

    public String toString();

}

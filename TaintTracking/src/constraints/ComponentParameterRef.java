package constraints;

public class ComponentParameterRef extends AComponentReference implements
        IComponentArrayBase {

    private final int parameterPos;

    public ComponentParameterRef(int parameterPos, String signature) {
        super(signature);
        this.parameterPos = parameterPos;
    }

    public String toString() {
        return "PR[" + parameterPos + "]@" + reduceInternalSignature();
    }

    protected int getParameterPos() {
        return parameterPos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + parameterPos;
        result =
            prime * result + ((signature == null) ? 0 : signature.hashCode());
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
        ComponentParameterRef other = (ComponentParameterRef) obj;
        if (parameterPos != other.parameterPos)
            return false;
        if (signature == null) {
            if (other.signature != null)
                return false;
        } else if (!signature.equals(other.signature))
            return false;
        return true;
    }

    @Override
    public IComponentArrayBase changeSignature(String signature) {
        return new ComponentParameterRef(parameterPos, signature);
    }

}

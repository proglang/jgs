package constraints;

public abstract class AComponentReference implements IComponentVar {

    protected final String signature;

    public AComponentReference(String signature) {
        this.signature = signature;
    }

    protected final String getSignature() {
        return signature;
    }

    public abstract String toString();

    protected String reduceInternalSignature() {
        if (signature.contains(" ")) {
            String sign = signature.substring(1, signature.length() - 1);
            String[] comp = sign.split(":");
            // String klass = comp[0];
            String[] method = comp[1].trim().split(" ");
            String name = method[1];
            return name;
        }
        return signature;
    }
}

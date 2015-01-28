package constraints;

public interface IComponent {

    public String toString();

    public boolean equals(Object object);

    public int hashCode();

    public IComponent changeSignature(String signature);

}

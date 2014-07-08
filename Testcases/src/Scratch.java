
// Quick throw-away examples and testcases for the security type checker
import static security.Definition.*;

public class Scratch {
    
    public int assignSame() {
        int x = mkHigh(42);
        x = x;
        return x;
    }
}

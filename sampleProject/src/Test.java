

import static security.Definition.*;
import security.Definition.Constraints;
import security.Definition.ParameterSecurity;
import security.Definition.ReturnSecurity;

public class Test {

    @ReturnSecurity("high")
    @Constraints({ "high <= @return" })
    public int ifReturnExpr() {
        int High = mkHigh(42);
        return High;
    }
    
    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

}

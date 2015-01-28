import static security.Definition.*;

@WriteEffect({})
public class TaintTrackingIfTest {

    @ParameterSecurity({})
    @ReturnSecurity("low")
    @WriteEffect({ "low" })
    public int forLoop() {
        int var3Low = mkLow(42);
        for (int i = mkHigh(0); i < 100; i++) {
            // if (i == 50) {
            // var3Low = highId(42);
            // } else {
            assignLow();
            // }
        }
        return var3Low;
    }

    // @ParameterSecurity({})
    // @ReturnSecurity("void")
    // @WriteEffect({"low"})
    // public void invoke() {
    // if (highId(23) == 5) {
    // assignLow();
    // }
    // }

    @ParameterSecurity({})
    @ReturnSecurity("void")
    @WriteEffect({ "low" })
    public void assignLow() {
        lowField = 42;
    }

    @FieldSecurity("low")
    public int lowField = mkLow(42);

    @FieldSecurity("high")
    public int highField = mkHigh(42);

    @ParameterSecurity({})
    @WriteEffect({ "high", "low" })
    public TaintTrackingIfTest() {
        super();
    }

}

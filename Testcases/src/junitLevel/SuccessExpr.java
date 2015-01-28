package junitLevel;

import static security.Definition.*;

public class SuccessExpr {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    @ReturnSecurity("high")
    public int expr() {
        int op1High = mkHigh(42);
        int op2High = mkHigh(42);
        return op1High - op2High;
    }

    @ReturnSecurity("high")
    public int expr2() {
        int op1High = mkHigh(42);
        int op2High = mkHigh(42);
        return op1High + op2High;
    }

    @ReturnSecurity("high")
    public int expr3() {
        int op1High = mkHigh(42);
        int op2High = mkHigh(42);
        return op1High * op2High;
    }

    @ReturnSecurity("high")
    public int expr4() {
        int op1High = mkHigh(42);
        int op2High = mkHigh(42);
        return op1High / op2High;
    }

    @ReturnSecurity("high")
    public boolean expr5() {
        int op1High = mkHigh(42);
        int op2High = mkHigh(42);
        return op1High > op2High;
    }

    @ReturnSecurity("high")
    public boolean expr6() {
        int op1High = mkHigh(42);
        int op2High = mkHigh(42);
        return op1High >= op2High;
    }

    @ReturnSecurity("high")
    public boolean expr7() {
        int op1High = mkHigh(42);
        int op2High = mkHigh(42);
        return op1High < op2High;
    }

    @ReturnSecurity("high")
    public boolean expr8() {
        int op1High = mkHigh(42);
        int op2High = mkHigh(42);
        return op1High <= op2High;
    }

    @ReturnSecurity("high")
    public boolean expr9() {
        int op1High = mkHigh(42);
        int op2High = mkHigh(42);
        return op1High == op2High;
    }

    @ReturnSecurity("high")
    public boolean expr10() {
        boolean op1High = mkHigh(false);
        boolean op2High = mkHigh(false);
        return op1High && op2High;
    }

    @ReturnSecurity("high")
    public boolean expr11() {
        boolean op1High = mkHigh(false);
        boolean op2High = mkHigh(false);
        return op1High || op2High;
    }

    @ReturnSecurity("high")
    public boolean expr12() {
        boolean op1High = mkHigh(false);
        boolean op2High = mkHigh(false);
        return op1High ^ op2High;
    }

    @ReturnSecurity("high")
    public boolean expr13() {
        boolean op1High = mkHigh(false);
        boolean op2High = mkHigh(false);
        return op1High & op2High;
    }

    @ReturnSecurity("high")
    public boolean expr14() {
        boolean op1High = mkHigh(false);
        boolean op2High = mkHigh(false);
        return op1High | op2High;
    }

    @ReturnSecurity("high")
    public int expr15() {
        int op1High = mkHigh(42);
        int op2Low = mkLow(42);
        return op1High - op2Low;
    }

    @ReturnSecurity("high")
    public int expr16() {
        int op1High = mkHigh(42);
        int op2Low = mkLow(42);
        return op1High + op2Low;
    }

    @ReturnSecurity("high")
    public int expr17() {
        int op1High = mkHigh(42);
        int op2Low = mkLow(42);
        return op1High * op2Low;
    }

    @ReturnSecurity("high")
    public int expr18() {
        int op1High = mkHigh(42);
        int op2Low = mkLow(42);
        return op1High / op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr19() {
        int op1High = mkHigh(42);
        int op2Low = mkLow(42);
        return op1High > op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr20() {
        int op1High = mkHigh(42);
        int op2Low = mkLow(42);
        return op1High >= op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr21() {
        int op1High = mkHigh(42);
        int op2Low = mkLow(42);
        return op1High < op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr22() {
        int op1High = mkHigh(42);
        int op2Low = mkLow(42);
        return op1High <= op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr23() {
        int op1High = mkHigh(42);
        int op2Low = mkLow(42);
        return op1High == op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr24() {
        boolean op1High = mkHigh(false);
        boolean op2Low = mkLow(false);
        return op1High && op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr25() {
        boolean op1High = mkHigh(false);
        boolean op2Low = mkLow(false);
        return op1High || op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr26() {
        boolean op1High = mkHigh(false);
        boolean op2Low = mkLow(false);
        return op1High ^ op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr27() {
        boolean op1High = mkHigh(false);
        boolean op2Low = mkLow(false);
        return op1High & op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr28() {
        boolean op1High = mkHigh(false);
        boolean op2Low = mkLow(false);
        return op1High | op2Low;
    }

    @ReturnSecurity("high")
    public int expr29() {
        int op1Low = mkLow(42);
        int op2High = mkHigh(42);
        return op1Low - op2High;
    }

    @ReturnSecurity("high")
    public int expr30() {
        int op1Low = mkLow(42);
        int op2High = mkHigh(42);
        return op1Low + op2High;
    }

    @ReturnSecurity("high")
    public int expr31() {
        int op1Low = mkLow(42);
        int op2High = mkHigh(42);
        return op1Low * op2High;
    }

    @ReturnSecurity("high")
    public int expr32() {
        int op1Low = mkLow(42);
        int op2High = mkHigh(42);
        return op1Low / op2High;
    }

    @ReturnSecurity("high")
    public boolean expr33() {
        int op1Low = mkLow(42);
        int op2High = mkHigh(42);
        return op1Low > op2High;
    }

    @ReturnSecurity("high")
    public boolean expr34() {
        int op1Low = mkLow(42);
        int op2High = mkHigh(42);
        return op1Low >= op2High;
    }

    @ReturnSecurity("high")
    public boolean expr35() {
        int op1Low = mkLow(42);
        int op2High = mkHigh(42);
        return op1Low < op2High;
    }

    @ReturnSecurity("high")
    public boolean expr36() {
        int op1Low = mkLow(42);
        int op2High = mkHigh(42);
        return op1Low <= op2High;
    }

    @ReturnSecurity("high")
    public boolean expr37() {
        int op1Low = mkLow(42);
        int op2High = mkHigh(42);
        return op1Low == op2High;
    }

    @ReturnSecurity("high")
    public boolean expr38() {
        boolean op1Low = mkLow(false);
        boolean op2High = mkHigh(false);
        return op1Low && op2High;
    }

    @ReturnSecurity("high")
    public boolean expr39() {
        boolean op1Low = mkLow(false);
        boolean op2High = mkHigh(false);
        return op1Low || op2High;
    }

    @ReturnSecurity("high")
    public boolean expr40() {
        boolean op1Low = mkLow(false);
        boolean op2High = mkHigh(false);
        return op1Low ^ op2High;
    }

    @ReturnSecurity("high")
    public boolean expr41() {
        boolean op1Low = mkLow(false);
        boolean op2High = mkHigh(false);
        return op1Low & op2High;
    }

    @ReturnSecurity("high")
    public boolean expr42() {
        boolean op1Low = mkLow(false);
        boolean op2High = mkHigh(false);
        return op1Low | op2High;
    }

    @ReturnSecurity("high")
    public int expr43() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low - op2Low;
    }

    @ReturnSecurity("high")
    public int expr44() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low + op2Low;
    }

    @ReturnSecurity("high")
    public int expr45() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low * op2Low;
    }

    @ReturnSecurity("high")
    public int expr46() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low / op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr47() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low > op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr48() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low >= op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr49() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low < op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr50() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low <= op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr51() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low == op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr52() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low && op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr53() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low || op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr54() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low ^ op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr55() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low & op2Low;
    }

    @ReturnSecurity("high")
    public boolean expr56() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low | op2Low;
    }

    @ReturnSecurity("low")
    public int expr57() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low - op2Low;
    }

    @ReturnSecurity("low")
    public int expr58() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low + op2Low;
    }

    @ReturnSecurity("low")
    public int expr59() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low * op2Low;
    }

    @ReturnSecurity("low")
    public int expr60() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low / op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr61() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low > op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr62() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low >= op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr63() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low < op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr64() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low <= op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr65() {
        int op1Low = mkLow(42);
        int op2Low = mkLow(42);
        return op1Low == op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr66() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low && op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr67() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low || op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr68() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low ^ op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr69() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low & op2Low;
    }

    @ReturnSecurity("low")
    public boolean expr70() {
        boolean op1Low = mkLow(false);
        boolean op2Low = mkLow(false);
        return op1Low | op2Low;
    }

}

package junitLevel;

import static security.Definition.*;

public class Invalid19 {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    // too few parameter security levels
    @ReturnSecurity("high")
    public static int methodInclusivParameter(int arg) {
        return arg;
    }

}
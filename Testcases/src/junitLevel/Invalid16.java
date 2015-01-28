package junitLevel;

import static security.Definition.*;

public class Invalid16 {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    @ParameterSecurity({ "high" })
    // method has a return security level
    public static int methodInclusivParameter(int arg) {
        return arg;
    }

}
package junitLevel;

import static security.Definition.*;

public class Invalid03 {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    @ParameterSecurity({ "high", "low" })
    // too many parameter security levels
    public Invalid03(int arg) {
    }

}
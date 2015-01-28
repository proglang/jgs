package junitLevel;

import static security.Definition.*;

public class Invalid15 {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    // no field security level
    public int instanceField = 42;

}
package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid20 {

    @FieldSecurity({ "low", "low", "high" })
    public static int[] field;

    public static void main(String[] args) {
    }

}

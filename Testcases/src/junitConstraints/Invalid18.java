package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid18 {

    @FieldSecurity({ "low" })
    public static int[] field;

    public static void main(String[] args) {
    }

}

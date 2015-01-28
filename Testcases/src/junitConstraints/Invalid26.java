package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid26 {

    @FieldSecurity({ "low", "confidential" })
    public int[] field;

    public static void main(String[] args) {
    }

}

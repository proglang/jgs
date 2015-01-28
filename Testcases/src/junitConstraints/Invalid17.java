package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid17 {

    @FieldSecurity({ "low", "high" })
    public int field;

    public static void main(String[] args) {
    }

}

package junitConstraints;

import static security.Definition.*;

public class Invalid13 {

    // invalid security level
    @FieldSecurity("confidential")
    public static int field;

    public static void main(String[] args) {
    }

}

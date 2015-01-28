package junitConstraints;

import static security.Definition.*;

public class Invalid12 {

    // invalid security level
    @FieldSecurity("confidential")
    public int field;

    public static void main(String[] args) {
    }

}

package junitConstraints;

import static security.Definition.*;

public class Invalid34 {

    // @security("missing write effect to low")
    @FieldSecurity("low")
    public static int lowIField = 42;

    public static void main(String[] args) {
    }

}

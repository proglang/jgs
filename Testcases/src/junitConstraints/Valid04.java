package junitConstraints;

import static security.Definition.*;

@Constraints("@pc <= low")
public class Valid04 {

    @FieldSecurity("low")
    public static int lowIField = 42;

    public static void main(String[] args) {
    }

}

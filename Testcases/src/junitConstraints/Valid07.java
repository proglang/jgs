package junitConstraints;

import static security.Definition.*;

@Constraints("@pc <= high")
public class Valid07 {

    @FieldSecurity("high")
    public static int highIField = 42;

    public static void main(String[] args) {
    }

}

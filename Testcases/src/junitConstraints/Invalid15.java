package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid15 {

    @FieldSecurity("high")
    public final static int highField = 1;

    public static void main(String[] args) {
    }

}

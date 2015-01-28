package junitConstraints;

import static security.Definition.*;

public class Valid03 {

    @FieldSecurity("high")
    public int highIField = mkHigh(42);

    public static void main(String[] args) {
    }

    public Valid03() {
    }

}

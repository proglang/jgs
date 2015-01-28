package junitConstraints;

import static security.Definition.*;

public class Invalid03 {

    public static void main(String[] args) {
    }

    // constraints contain an invalid additional parameter reference
    @Constraints({ "@1 <= low" })
    public Invalid03(int arg) {
    }

}

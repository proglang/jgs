package junitConstraints;

import static security.Definition.*;

public class Invalid05 {

    public static void main(String[] args) {
    }

    // constraints contain an invalid security level for the program counter
    // reference
    @Constraints({ "@pc <= confidential" })
    public Invalid05(int arg) {
    }

}

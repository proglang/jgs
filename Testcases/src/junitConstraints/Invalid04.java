package junitConstraints;

import static security.Definition.*;

public class Invalid04 {

    public static void main(String[] args) {
    }

    // constraints contain an invalid parameter reference
    @Constraints({ "@1 <= low" })
    public Invalid04(int arg) {
    }

}

package junitConstraints;

import static security.Definition.*;

public class Invalid06 {

    public static void main(String[] args) {
    }

    // constraints contain an invalid parameter reference
    @Constraints({ "@0 <= low" })
    public Invalid06() {
    }

}

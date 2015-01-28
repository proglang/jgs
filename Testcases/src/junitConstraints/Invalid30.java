package junitConstraints;

import static security.Definition.*;

public class Invalid30 {

    public static void main(String[] args) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@0[[ = low" })
    public void invalid32(int[] i) {
    }

}

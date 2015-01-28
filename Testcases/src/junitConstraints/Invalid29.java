package junitConstraints;

import static security.Definition.*;

public class Invalid29 {

    public static void main(String[] args) {
    }

    @Constraints({ "@0 <= low", "@0[ = low" })
    public void invalid31(int i) {
    }

}

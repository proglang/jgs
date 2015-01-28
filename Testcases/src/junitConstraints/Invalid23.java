package junitConstraints;

import static security.Definition.*;

public class Invalid23 {

    public static void main(String[] args) {
    }

    public void test() {
        int[] arr1 = arrayIntHigh(1);
        Object obj = (Object) arr1;
        int[] arr2 = (int[]) obj;
        arr2[0] = 1;
    }

}

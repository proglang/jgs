package testclasses;

import utils.analyzer.HelperClass;

/**
 * Created by Nicolas Müller on 14.02.17.
 */
public class HighArgument {
    public static void main(String[] args) {
        int i = 42;
        i = HelperClass.makeHigh(i);
        callPrint(i);
    }

    static void callPrint(int arg) {
        System.out.println(arg);
    }
}

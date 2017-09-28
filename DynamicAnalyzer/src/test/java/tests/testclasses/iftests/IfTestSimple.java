package tests.testclasses.iftests;

public class IfTestSimple {

    public static int method1 (int c) {
        int a = 5;
        int b = 4;
        if (a == c) {
            b = 3 * c;
        } else {
            a = 42;
            c = b * a;
        }
        return (a + b + c);
    }
}

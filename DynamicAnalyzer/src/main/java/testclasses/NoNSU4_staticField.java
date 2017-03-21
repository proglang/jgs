package testclasses;

import testclasses.utils.C;

/**
 * Created by Nicolas Müller on 07.02.17.
 */
public class NoNSU4_staticField {
    static int f = 0;
    public static void main(String[] args) {

        C b = new C();
        C c = b;

        f = 1;
        if (c.equals(b)){
            f = 2;                  // NSU Check, but nor Illegal Flow Exception
        }

    }
}

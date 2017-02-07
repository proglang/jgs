package testclasses;

/**
 * Created by Nicolas Müller on 07.02.17.
 * A test doing stuff that might trigger a NSU check.
 */
public class NoNSU2 {
    public static void main(String[] args) {
        int i = 5;
        for (String s: args) {
            i -= 1;
        }
        boolean b;
        if (i > 0) {
            System.out.print("Less than five args were supplied");
            b = true;
        } else {
            System.out.print("More than four args were supplied");
            b = false;
        }
        int ret = doStuff(b);
        System.out.println(ret);


    }

    private static int doStuff(boolean b) {
        int i = 0;
        if (b) {
            i += 1;
        } else {
            i-= 1;
        }
        return i;
    }
}
